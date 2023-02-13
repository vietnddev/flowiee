package controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.channels.GatheringByteChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateQueryException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import dao.Extension;
import dao.ProfileDAO;
import dao.RoleDAO;
import model.*;

@Transactional
@Controller
public class AdminAccountController {
	private Path path;

	@Autowired
	ServletContext context;

	@Autowired
	SessionFactory factory;

	//
	Extension extension = new Extension();
	ArrayList<Notification> list_notification = null;

	//
	List<Role> list_role = null;

	//
	RoleDAO checkrole = new RoleDAO();

	// index (New) - ok
	@RequestMapping(value = "/admin/home")
	public String getAllAdmin(ModelMap model, HttpServletRequest request, HttpSession httpSession) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDateTime now = LocalDateTime.now();
		model.addAttribute("currentdate", dtf.format(now));

		try {
			Session session = factory.openSession();
			// Truy vấn SQL
			Query query_account = session.createQuery("From Account");

			// Đổ kết quả truy vấn được vào list
			List<Account> list_account = query_account.list();

			// Khởi tạo model để dán kết quả lên page
			model.addAttribute("Total_list_account", list_account.size());
			model.addAttribute("Account", list_account);

			// Check role
			RoleDAO check = new RoleDAO();
			if (check.checkRole(Integer.parseInt(ProfileDAO.accountID + ""), check.ID_MAI) > 0) {
				model.addAttribute("enableRoleI", "true");
			} else if (check.checkRole(Integer.parseInt(ProfileDAO.accountID + ""), check.ID_MAD) > 0) {
				model.addAttribute("enableRoleD", "true");
			}

			// Load thông báo
			list_notification = extension.getListNotification(extension.default_notification,
					ProfileDAO.accountID + "");
			model.addAttribute("notification", list_notification);
			model.addAttribute("noti_unread", extension.getNotificationUnRead(ProfileDAO.accountID + ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Check role
		return "admin/pages/admin/account";
	}

	// Thêm mới account
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(value = "/admin/insertAccount", params = "btnInsert", method = RequestMethod.POST)
	public String insert(@RequestParam("file") MultipartFile img, ModelMap model,
			@ModelAttribute("account") Account account, HttpServletRequest request) {
		model.addAttribute("account");
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		// Thư mục chứa ảnh
		String rootDir = request.getSession().getServletContext().getRealPath("/");
		try {
			if (request.getParameter("gender").equals("0")) {
				account.setIsAdmin(false);
			} else if (request.getParameter("gender").equals("1")) {
				account.setIsAdmin(true);
			}
			if (request.getParameter("isAdmin").equals("0")) {
				account.setIsAdmin(false);
			} else if (request.getParameter("isAdmin").equals("1")) {
				account.setIsAdmin(true);
			}
			if (request.getParameter("status").equals("0")) {
				account.setStatus(true);
			} else if (request.getParameter("status").equals("1")) {
				account.setStatus(false);
			}
			if (request.getParameter("password").equals(request.getParameter("repassword"))) {
				session.save(account);
				model.addAttribute("message", "Thêm mới thành công!");
			}
			// Nếu có đính kèm file thì thực hiện các lệnh bên dưới
			if (img != null && !img.isEmpty()) {
				// Đường dẫn full (Thư mục + tên file)
				path = Paths
						.get(rootDir + "\\WEB-INF\\views\\admin\\assets\\img\\avatars\\" + account.getID() + ".png");
				System.out.println("Img save at: " + path);
				account.setAvatar(path.toString());
				img.transferTo(new File(path.toString()));
				System.out.println("Save ok?");
			}
			t.commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			model.addAttribute("message", "Thêm mới thất bại!");
			e.printStackTrace();
		} finally {
			session.close();
		}
		return "redirect:/admin/home";
	}// end method

	// Delete (New) - ok
	@RequestMapping(value = "/admin/updateAccount", params = "btnDeleteAccount", method = RequestMethod.GET)
	public String delete(ModelMap model, HttpServletRequest request, @ModelAttribute("acc") Account acc) {
		model.addAttribute("account");
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		String id = request.getParameter("txtDeleteAccount");
		acc = (Account) session.get(Account.class, Integer.parseInt(id));
		try {
			session.delete(acc);
			t.commit();
		} catch (Exception e) {
			t.rollback();
			e.printStackTrace();
		}
		return "redirect:/admin/home";
	}

	// Cập nhật ACC
	@RequestMapping(value = "/admin/updateAccount", params = "btnUpdateAccount", method = RequestMethod.POST)
	public String Reset(ModelMap model, HttpServletRequest request, @RequestParam("file") MultipartFile img) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		// Lấy ID của account cần xóa
		Account account;
		String iDUser = request.getParameter("txtUpdateAccount");
		System.out.println("UPDATE ACC - ID " + iDUser);
		account = (Account) session.get(Account.class, Integer.parseInt(iDUser));
		account.setUsername(request.getParameter("username"));
		account.setName(request.getParameter("name"));
		account.setPassword(request.getParameter("password"));
		account.setNotes(request.getParameter("notes"));
		account.setEmail(request.getParameter("email"));
		account.setPhone(request.getParameter("phone"));
		if (request.getParameter("gender").equals("0")) {
			account.setGender(false);
		} else if (request.getParameter("gender").equals("1")) {
			account.setGender(true);
		}
		if (request.getParameter("isAdmin").equals("0")) {
			account.setIsAdmin(false);
		} else if (request.getParameter("isAdmin").equals("1")) {
			account.setIsAdmin(true);
		}
		if (request.getParameter("status").equals("1")) {
			account.setStatus(true);
		} else if (request.getParameter("status").equals("0")) {
			account.setStatus(false);
		}
		try {
			session.update(account);
			//
			String rootDir = request.getSession().getServletContext().getRealPath("/");
			if (img != null && !img.isEmpty()) {
				path = Paths
						.get(rootDir + "\\WEB-INF\\views\\admin\\assets\\img\\avatars\\" + account.getID() + ".png");
				System.out.println("Img save at: " + path);
				account.setAvatar(path.toString());
				img.transferTo(new File(path.toString()));
			}

			// Cập nhật quyền
			String iDRole = ""; // Khởi tạo ID Role
			System.out.println("Update Role - iDUser: " + iDUser);

			// Xóa tất cả các role của User theo ID
			checkrole.deleteUserRole(Integer.parseInt(iDUser));

			// Cập nhật quyền cho UserRole
			for (int i = 0; i < list_role.size(); i++) {
				iDRole = request.getParameter("checkbox_" + list_role.get(i).getID());
				if (iDRole != null) {
					UserRole userrole = new UserRole(Integer.parseInt(iDUser), Integer.parseInt(iDRole));
					session.save(userrole);
				}
			}

			//
			t.commit();
		} catch (Exception e) {
			t.rollback();
			e.printStackTrace();
		}
		return "redirect:/admin/account-id=" + iDUser;
	}

	// Show trang chi tiết
	@RequestMapping(value = "/admin/account-id={ID}")
	public String getAccountDetail(ModelMap model, @PathVariable("ID") int id, HttpSession httpSession) {
		Session session = factory.openSession();

		// Load thông tin chi tiết của ACC
		Query query_detail = session.createQuery("From Account Where ID = " + id);
		List<Account> list_detail = query_detail.list();
		model.addAttribute("Detail_Account", list_detail);

		// Load tất cả role
		Query query_role = session.createQuery("From Role Where Status = 1 ORDER BY Sort");
		list_role = query_role.list();

		// Checked các quyền đã được share lên màn hình
		Query query_userrole = session.createQuery("From UserRole Where IDUser = " + id);
		List<UserRole> list_userrole = query_userrole.list();

		for (int i = 0; i < list_userrole.size(); i++) {
			for (Role x : list_role) {
				if (list_userrole.get(i).getIDRole() - x.getID() == 0) {
					x.setStatus(false);
				}
			}
		}

		//
		model.addAttribute("IDUser", id);

		// Dán list role lên trang jsp
		model.addAttribute("Role", list_role);

		// Breadcrumb
		model.addAttribute("page", "account"); // Redirect về trang /account
		model.addAttribute("breadcrumb", "Danh sách tài khoản");
		// Load chuông thông báo
		list_notification = extension.getListNotification(extension.default_notification, ProfileDAO.accountID + "");
		model.addAttribute("notification", list_notification);
		model.addAttribute("noti_unread", extension.getNotificationUnRead(ProfileDAO.accountID + ""));
		//
		return "admin/pages/admin/account-detail";
	}

	@RequestMapping(value = "/admin/updateAccount", params = "updateRole", method = RequestMethod.POST)
	public String updateRole(ModelMap model, HttpSession httpSession, HttpServletRequest request) {
		//
		Session session = factory.openSession();

		// Lấy ID
		int iDUser = Integer.parseInt(request.getParameter("txtUpdateRole")); // Lấy ID của ACC
		String iDRole = ""; // Khởi tạo ID Role

		System.out.println("Update Role - iDUser: " + iDUser);

		// Xóa tất cả các role của User theo ID
		checkrole.deleteUserRole(iDUser);

		// Cập nhật quyền cho UserRole
		for (int i = 0; i < list_role.size(); i++) {
			iDRole = request.getParameter("checkbox_" + list_role.get(i).getID());
			System.out.println("iDRole " + iDRole);
			if (iDRole != null) {
				System.out.println("iDRole != null " + iDRole);
				UserRole userrole = new UserRole(iDUser, Integer.parseInt(iDRole));
				session.save(userrole);
			}
		}
		//
		return "redirect:/admin/account-id=" + iDUser;
	}
}
