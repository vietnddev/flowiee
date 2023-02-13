package controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.orm.hibernate4.HibernateQueryException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mchange.net.MailSender;

import dao.Extension;
import dao.ProfileDAO;
import model.*;

@RequestMapping("/admin")
@Transactional
@Controller
public class admin_LoginController {
	@Autowired
	SessionFactory factory;

	@Autowired
	private JavaMailSender mailSender;

	public List<Account> list;

	Extension extension = new Extension();
	ArrayList<Notification> list_notification = null;

//	// Truy cập trang admin
//	@RequestMapping(value = "")
//	public String index() {
//		return "admin/pages/login";
//	}

	// Truy cập trang login
	@RequestMapping(value = "/login")
	public String login() {
		return "admin/pages/login";
	}

	// Đăng nhập
	@RequestMapping(value = "/login", params = "sign-in", method = RequestMethod.POST)
	public String login(@RequestParam("Username") String username_form, @RequestParam("password") String password_form,
			ModelMap model, HttpSession httpSession, HttpServletRequest request) throws UnknownHostException {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		// Lấy IP
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}

		// Lấy thời gian đăng nhập
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		//
		String sql_login = "FROM Account Where Username =  '" + username_form + "' and Password =  '" + password_form
				+ "'";
		Query query = session.createQuery(sql_login);
		list = query.list();
		//
		Account account = (Account) query.uniqueResult();
		// Đúng -> Vào màn
		if (list.size() == 1) {
			ProfileDAO.accountID = account.getID();
			ProfileDAO.accountName = account.getName();
			ProfileDAO.accountUsername = account.getUsername();
			ProfileDAO.accountPhone = account.getPhone();
			ProfileDAO.accountEmail = account.getEmail();
			ProfileDAO.accountIsGender = account.isGender();
			ProfileDAO.accountIsAdmin = account.isIsAdmin();
			ProfileDAO.accountPassword = account.getPassword();
			httpSession.setAttribute("name", account.getName());
			//
			Log log = new Log();
			log.setUsers(account.getName() + " (" + account.getUsername() + ")");
			log.setAction("Đăng nhập hệ thống");
			log.setUrl("/Url/");
			log.setCreated(dateTimeFormatter.format(now));
			log.setIP(ipAddress);
			//
			session.save(log);
			t.commit();
			//
			return "redirect:/";
		} else {
			return "redirect:/admin/login";
		}
	}

	// Đăng xuất (logout)
	@RequestMapping(value = "/logout")
	public String logout(HttpSession httpSession) {
		httpSession.removeAttribute("name");
		return "admin/pages/login";
	}

	// Vào trang chủ
	@RequestMapping(value = "/forgotPassword")
	public String forgotPassword(ModelMap model) {
		//
		return "admin/pages/resetpassword";
	}

	// Rs mật khẩu
	@RequestMapping(value = "/resetPassword")
	public String resetPassword(ModelMap model, @RequestParam("email") String to) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		//
		int code = (int) Math.floor(((Math.random() * 89999999) + 10000000));
		try {
			SimpleMailMessage smp = new SimpleMailMessage();
			smp.setTo(to);
			smp.setSubject("[Flowiee Official] Thông báo hệ thống - Cấp lại mật khẩu");
			smp.setText("Mật khẩu mới của bạn là: " + code);
			mailSender.send(smp);

			//
			String SQL = "FROM Account Where email = '" + to + "'";
			List<Account> list = session.createQuery(SQL).list();
			Account account = list.get(0);
			account.setPassword(code + "");

			//
			session.update(account);
			t.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "redirect:/admin/login";
	}
}