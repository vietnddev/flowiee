package controller;

import java.io.File;
import java.nio.channels.GatheringByteChannel;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
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

@RequestMapping("/admin/")
@Transactional
@Controller
public class AdminRoleController {
	@Autowired
	SessionFactory factory;

	Extension extension = new Extension();
	ArrayList<Notification> list_notification = null;

	HttpServletRequest requestx;

	RoleDAO checkrole = new RoleDAO();

	// show trang nhóm quyền
	@RequestMapping(value = "role")
	public String getRole(ModelMap model, HttpServletRequest request, HttpSession httpSession) {

		System.out.println("getScheme:  " + request.getScheme());
		System.out.println("getContextPath:  " + request.getContextPath());

		System.out.println("getServletContext:  " + request.getServletContext());
		System.out.println("getServerName:  " + request.getServerName());
		System.out.println("getServerPort:  " + request.getServerPort());
		System.out.println("getSession:  " + request.getSession());
		System.out.println("getServletPath:  " + request.getServletPath());
		System.out.println("Scheme " + request.getScheme());
		System.out.println("Scheme " + request.getScheme());

		try {
			Session session = factory.openSession();
			Query query = session.createQuery("From Role ORDER BY Sort");
			List<Role> list = query.list();
			model.addAttribute("Role", list);
			//
			list_notification = extension.getListNotification(extension.default_notification,
					ProfileDAO.accountID + "");
			model.addAttribute("notification", list_notification);
			model.addAttribute("noti_unread",
					extension.getNotificationUnRead(ProfileDAO.accountID + ""));
		} catch (HibernateQueryException e) {
			e.printStackTrace();
		}
		// Check role
		return "admin/pages/admin/role";
	}

	// insert nhóm quyền
	@RequestMapping(value = "insertRole", params = "btnInsert", method = RequestMethod.POST)
	public String insert(ModelMap model, @ModelAttribute("role") Role role, HttpServletRequest request) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			role.setStatus(true);
			session.save(role);
			t.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/admin/role";
	}//

	// Update
	@RequestMapping(value = "updateRole", params = "btnUpdateRole", method = RequestMethod.POST)
	public String update(ModelMap model, HttpServletRequest request) {
		System.out.println("getScheme:  " + request.getScheme());
		System.out.println("getContextPath:  " + request.getContextPath());

		System.out.println("getServletContext:  " + request.getServletContext());
		System.out.println("getServerName:  " + request.getServerName());
		System.out.println("getServerPort:  " + request.getServerPort());
		System.out.println("getSession:  " + request.getSession());
		System.out.println("getServletPath:  " + request.getServletPath());
		System.out.println("Scheme " + request.getScheme());
		System.out.println("Scheme " + request.getScheme());
		System.out.println("Scheme " + request.getScheme());

		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		String id = request.getParameter("txtUpdate");
		//
		Role role;
		role = (Role) session.get(Role.class, Integer.parseInt(id));
		try {
			role.setCode(request.getParameter("codeu"));
			role.setName(request.getParameter("nameu"));
			role.setSort(Integer.parseInt(request.getParameter("sortu")));
			if (request.getParameter("statusu").equals("true")) {
				role.setStatus(true);
			} else {
				role.setStatus(false);
			}
			role.setDescribes(request.getParameter("describesu"));
			role.setType(Integer.parseInt(request.getParameter("typeu")));
			// Thực hiện xóa xuống SQL
			session.update(role);
			t.commit();
		} catch (Exception e) {
			// Quay xe
			t.rollback();
			e.printStackTrace();
		}
		return "redirect:/admin/role";
	}//

	// Delete
	@RequestMapping(value = "updateRole", params = "btnDeleteRole", method = RequestMethod.POST)
	public String delete(ModelMap model, HttpServletRequest request) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		Role role;
		String id = request.getParameter("txtDelete");
		role = (Role) session.get(Role.class, Integer.parseInt(id));
		try {
			session.delete(role);
			t.commit();
		} catch (Exception e) {
			// Quay xe
			t.rollback();
			e.printStackTrace();
		}
		return "redirect:/admin/role";
	}
}// end class
