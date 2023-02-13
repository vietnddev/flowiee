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

import model.*;

@RequestMapping("/admin/")
@Transactional
@Controller
public class admin_ProfileController {
	private Path path;

	@Autowired
	SessionFactory factory;

	// Cập nhật thông tin cá nhân
	@RequestMapping(value = "updateProfile", method = RequestMethod.POST)
	public String updateProfile(@RequestParam("file") MultipartFile img, HttpSession httpSession,
			HttpServletRequest request) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		//
		Account account;
		//
		String id = httpSession.getAttribute("profileid").toString();
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		try {
			account = (Account) session.get(Account.class, Integer.parseInt(id));
			account.setName(name);
			account.setPhone(phone);
			account.setEmail(email);
			//
			if (httpSession.getAttribute("role") == "1") {
				account.setIsAdmin(true);
			} else {
				account.setIsAdmin(false);
			}
			//
			if (gender == "1") {
				account.setStatus(true);
			} else {
				account.setStatus(false);
			}
			//
			String rootDir = request.getSession().getServletContext().getRealPath("/");
			if (img != null && !img.isEmpty()) {
				path = Paths.get(rootDir + "\\WEB-INF\\views\\admin\\assets\\img\\avatars\\" + id + ".png");
				System.out.println("Avatar save at: " + path);
				// Lưu hình đã chọn vào thư mục đã cấu hình
				img.transferTo(new File(path.toString()));
				System.out.println("Save ok?");
			}
			// Thực hiện xóa xuống SQL
			session.update(account);
			// Lưu lại
			t.commit();
			//
			httpSession.setAttribute("name", name);
			httpSession.setAttribute("phone_profile", phone);
			httpSession.setAttribute("email_profile", email);
			httpSession.setAttribute("gender", gender);
		} catch (Exception e) {
			// Quay xe
			t.rollback();
			e.printStackTrace();
		}
		return "redirect:/admin/index";
	}

	// Đổi mật khẩu
	@RequestMapping(value = "change-password", method = RequestMethod.POST)
	public String changPassword(HttpSession httpSession, HttpServletRequest request) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		//
		Account account;
		//
		String id = httpSession.getAttribute("profileid").toString();
		String pass = request.getParameter("password");
		String pass_new = request.getParameter("password_new");
		String pass_re = request.getParameter("password_re");
		try {
			account = (Account) session.get(Account.class, Integer.parseInt(id));
			if (httpSession.getAttribute("pass").equals(pass) && pass_new.equals(pass_re)) {
				account.setPassword(pass_new);
				httpSession.setAttribute("pass", pass_new);
			}
			// Thực hiện xóa xuống SQL
			session.update(account);
			// Lưu lại
			t.commit();
			//
		} catch (Exception e) {
			// Quay xe
			t.rollback();
			e.printStackTrace();
		}
		return "redirect:/admin/index";
	}

}
