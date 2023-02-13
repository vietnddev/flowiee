package controller;

import java.io.File;
import java.io.IOException;
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
public class admin_SystemConfigController {
	@Autowired
	SessionFactory factory;

	Extension extension = new Extension();
	ArrayList<Notification> list_notification = null;

	RoleDAO checkrole = new RoleDAO();

	// index (New) - ok
	@RequestMapping(value = "config")
	public String index(ModelMap model, HttpServletRequest request, HttpSession httpSession) {
		try {
			Session session = factory.openSession();
			// Truy vấn SQL
			Query query = session.createQuery("From SystemConfig");

			// Đổ kết quả truy vấn được vào list
			List<SystemConfig> list = query.list();

			SystemConfig sc = (SystemConfig) query.uniqueResult();

			// Khởi tạo model để dán kết quả lên page
			model.addAttribute("id_sc", sc.getID());
			model.addAttribute("name_sc", sc.getName());
			model.addAttribute("address_sc", sc.getAddress());
			model.addAttribute("email_sc", sc.getEmail());
			model.addAttribute("phone_sc", sc.getPhone());
			model.addAttribute("favicon_sc", sc.getFavicon());
			model.addAttribute("logo_sc", sc.getLogo());
			model.addAttribute("loginlock_sc", sc.getLoginLock());
			model.addAttribute("describe_sc", sc.getDescribes());

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
		return "admin/pages/admin/config";
	}

	// Cập nhật
	@RequestMapping(value = "updateConfig")
	public String update(@RequestParam("logo") MultipartFile logo, @RequestParam("favicon") MultipartFile favicon,
			ModelMap model, HttpServletRequest request) throws IllegalStateException, IOException {
		//
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy-HHmmss");
		LocalDateTime now = LocalDateTime.now();
		try {
			Session session = factory.openSession();
			Transaction t = session.beginTransaction();

			//
			SystemConfig sc;

			//
			String id = request.getParameter("txtID");
			sc = (SystemConfig) session.get(SystemConfig.class, Integer.parseInt(id));
			//
			sc.setCode("code");
			sc.setName(request.getParameter("name"));
			sc.setAddress(request.getParameter("address"));
			sc.setEmail(request.getParameter("email"));
			sc.setPhone(request.getParameter("phone"));
			sc.setLoginLock(Integer.parseInt(request.getParameter("LoginLock")));
			sc.setDescribes(request.getParameter("describes"));

			// Save logo
			String rootDir = request.getSession().getServletContext().getRealPath("");
			if (logo != null && !logo.isEmpty()) {
				rootDir += "\\WEB-INF\\views\\admin\\assets\\logo\\" + dtf.format(now) + "_" + "logo" + "_"
						+ logo.getOriginalFilename();
				// Lưu hình đã chọn vào thư mục đã cấu hình
				logo.transferTo(new File(rootDir));
				sc.setLogo(dtf.format(now) + "_" + "logo" + "_" + logo.getOriginalFilename());
				System.out.println("Logo save at: " + rootDir);
				System.out.println("getContentType: " + logo.getContentType());
				System.out.println("getName: " + logo.getName());
				System.out.println("getOriginalFilename: " + logo.getOriginalFilename());
				System.out.println("getSize: " + logo.getSize());	
			}

			// Save favicon
			String rootDir_favicon = request.getSession().getServletContext().getRealPath("");
			if (favicon != null && !favicon.isEmpty()) {
				rootDir_favicon += "\\WEB-INF\\views\\admin\\assets\\favicon\\" + dtf.format(now) + "_" + "favicon"
						+ "_" + favicon.getOriginalFilename();
				// Lưu hình đã chọn vào thư mục đã cấu hình
				favicon.transferTo(new File(rootDir_favicon));
				sc.setFavicon(dtf.format(now) + "_" + "favicon" + "_" + favicon.getOriginalFilename());
				System.out.println("Favifon save at: " + rootDir_favicon);
			}
			//
			session.update(sc);
			t.commit();
		} catch (HibernateQueryException e) {
			e.printStackTrace();
		}
		return "redirect:/admin/config";
	}
}
