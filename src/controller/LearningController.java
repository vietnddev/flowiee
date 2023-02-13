package controller;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateQueryException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import dao.Extension;
import dao.ProfileDAO;
import dao.RoleDAO;
import model.Account;
import model.Category;
import model.Customer;
import model.Learning;
import model.Log;
import model.Notification;
import model.Product;

import java.text.Normalizer;
import java.util.regex.Pattern;

@RequestMapping("/learning/")
@Transactional
@Controller
public class LearningController {
	@Autowired
	SessionFactory factory;

	Extension extension = new Extension();
	ArrayList<Notification> list_notification = null;

	RoleDAO checkrole = new RoleDAO();

	int type_vocabulary = 8;
	int type_grammar = 9;

	boolean typelearning; // dùng để phân loại từ vựng/ ngữ pháp khi insert

	List<Learning> listlearning;
	List<Learning> listcount;

	String SQL_Vocabulary = "From Learning Where isVocabulary = 1";
	String SQL_Grammar = "From Learning Where isGrammar = 1";

	String filter = ""; // khởi tạo giá trị cho filter

	Learning learning;

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
	LocalDateTime now = LocalDateTime.now();
	
	@RequestMapping(value = "home")
	public String home() {
		return "admin/pages/learning/home";
	}

	// Show trang từ vựng
	@RequestMapping(value = "vocabulary")
	public String showVocabulary(ModelMap model, HttpSession httpSession, HttpServletRequest request) {
		model.addAttribute("title", "Từ vựng");
		model.addAttribute("TITLE", "TỪ VỰNG");
		// Dán filter đã được chọn lên page
		if (filter != "") {
			model.addAttribute("filter", filter);
		}

		//
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();

		typelearning = false; // dùng để phân loại từ vựng/ ngữ pháp khi insert (false = từ vựng)
		try {
			// load từ vựng
			// String SQL_Learning_Vocabulary = "From Learning Where isVocabulary = 1";
			Query Query_Learning_Vocabulary = session.createQuery(SQL_Vocabulary);
			listlearning = Query_Learning_Vocabulary.list();
			model.addAttribute("Learning", listlearning);
			filter = "Chưa xác định";
			SQL_Vocabulary = "From Learning Where isVocabulary = 1";

			// load danh mục dropdownlist
			Query Query_Category_Vocabulary = session.createQuery(
					"From Category Where Type = " + type_vocabulary + " and Code = '' and Status = '1' order by Sort ");
			List<Category> list_category_vocabulary = Query_Category_Vocabulary.list();
			model.addAttribute("category_vocabulary_grammar", list_category_vocabulary);

			// Dán số lượng records lên page
			model.addAttribute("Total_list_learning", listlearning.size());

			// Phân trang
			String SQL_count = "SELECT count(ID) " + SQL_Vocabulary;
			Query Query_count = session.createQuery(SQL_count);
			List<Learning> List_count = Query_count.list();
			model.addAttribute("countlearning", List_count);
			System.out.println("List_count: " + List_count);

			// Ghi log
			Log log = new Log();
			log.setUsers(httpSession.getAttribute("name") + " (" + httpSession.getAttribute("username_profile") + ")");
			log.setAction("Truy cập chức năng");
			log.setUrl("/admin/learning/vocabulary");
			log.setCreated(dtf.format(now));
			// Lấy IP
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
			}
			log.setIP(ipAddress);
			//
			session.save(log);
			t.commit();

			// Chuông thông báo
			list_notification = extension.getListNotification(extension.default_notification,
					ProfileDAO.accountID + "");
			model.addAttribute("notification", list_notification);
			model.addAttribute("noti_unread",
					extension.getNotificationUnRead(ProfileDAO.accountID + ""));
		} catch (HibernateQueryException e) {
			e.printStackTrace();
		}
		return "admin/pages/learning/learning";
	}

	// Show trang từ vựng
	@RequestMapping(value = "grammar")
	public String showGrammar(ModelMap model, HttpSession httpSession, HttpServletRequest request) {
		model.addAttribute("title", "Ngữ pháp");
		model.addAttribute("TITLE", "NGỮ PHÁP");
		// Dán filter đã được chọn lên page
		if (filter != "") {
			model.addAttribute("filter", filter);
		}

		//
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();

		typelearning = true; // dùng để phân loại từ vựng/ ngữ pháp khi insert (true = ngữ pháp)
		try {
			// load ngữ pháp
			// String SQL_Learning_Grammar = "From Learning Where isGrammar = 1";
			Query Query_Learning_Grammar = session.createQuery(SQL_Grammar);
			listlearning = Query_Learning_Grammar.list();
			model.addAttribute("Learning", listlearning);
			filter = "Chưa xác định";
			SQL_Grammar = "From Learning Where isGrammar = 1";

			// load danh mục dropdownlist
			Query Query_Category_Grammar = session.createQuery(
					"From Category Where Type = " + type_grammar + " and Code = '' and Status = '1' order by Sort ");
			List<Category> list_category_grammar = Query_Category_Grammar.list();
			model.addAttribute("category_vocabulary_grammar", list_category_grammar);

			// Dán số lượng records lên page
			model.addAttribute("Total_list_learning", listlearning.size());

			// Phân trang
			String SQL_count = "SELECT count(ID) " + SQL_Grammar;
			Query Query_count = session.createQuery(SQL_count);
			List<Learning> List_count = Query_count.list();
			model.addAttribute("countlearning", List_count);
			System.out.println("List_count: " + List_count);

			// Ghi log
			Log log = new Log();
			log.setUsers(httpSession.getAttribute("name") + " (" + httpSession.getAttribute("username_profile") + ")");
			log.setAction("Truy cập chức năng");
			log.setUrl("/admin/learning/grammar");
			log.setCreated(dtf.format(now));
			// Lấy IP
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
			}
			log.setIP(ipAddress);
			//
			session.save(log);
			t.commit();

			// Chuông thông báo
			list_notification = extension.getListNotification(extension.default_notification,
					ProfileDAO.accountID + "");
			model.addAttribute("notification", list_notification);
			model.addAttribute("noti_unread",
					extension.getNotificationUnRead(ProfileDAO.accountID + ""));
		} catch (HibernateQueryException e) {
			e.printStackTrace();
		}
		return "admin/pages/learning/learning";
	}

	// Thêm mới từ vựng
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(ModelMap model, @ModelAttribute("learning") Learning learning, HttpServletRequest request) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();

		System.out.println("Type: " + request.getParameter("type"));

		try {
			if (typelearning == true) {
				learning.setIsGrammar(true);
				learning.setIsVocabulary(false);
			} else {
				learning.setIsGrammar(false);
				learning.setIsVocabulary(true);
			}
			learning.setStatus(true);
			session.save(learning);
			t.commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			model.addAttribute("message", "Thêm mới thất bại!");
			e.printStackTrace();
		}
		if (typelearning == false) {
			return "redirect:/learning/vocabulary";
		} else {
			return "redirect:/learning/grammar";
		}
	}

	// Delete - ok
	@RequestMapping(value = "update", params = "btnDeleteLearning", method = RequestMethod.GET)
	public String delete(ModelMap model, HttpServletRequest request) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		String ID = request.getParameter("txtDelete");

		//
		Learning learning;
		learning = (Learning) session.get(Learning.class, Integer.parseInt(ID));
		try {
			session.delete(learning);
			t.commit();
		} catch (Exception e) {
			t.rollback();
			e.printStackTrace();
		}
		if (typelearning == false) {
			return "redirect:/learning/vocabulary";
		} else {
			return "redirect:/learning/grammar";
		}
	}

	// Update
	@RequestMapping(value = "update", params = "btnUpdateLearning", method = RequestMethod.GET)
	public String update(ModelMap model, HttpServletRequest request) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		String ID = request.getParameter("txtUpdate");
		System.out.println("ID: " + ID);

		//
		learning = (Learning) session.get(Learning.class, Integer.parseInt(ID));
		try {
			learning.setType(request.getParameter("type"));
			learning.setName(request.getParameter("name"));
			learning.setPronounce(request.getParameter("pronounce"));
			learning.setTranslate(request.getParameter("translate"));
			learning.setNote(request.getParameter("note"));
			if (request.getParameter("created").equals("")) {
				learning.setCreated(request.getParameter("created2"));
			} else {
				learning.setCreated(request.getParameter("created"));
			}
			learning.setStatus(true);
			if (typelearning == true) {
				learning.setIsGrammar(true);
				learning.setIsVocabulary(false);
			} else {
				learning.setIsGrammar(false);
				learning.setIsVocabulary(true);
			}
			//
			session.update(learning);
			t.commit();
		} catch (Exception e) {
			t.rollback();
			e.printStackTrace();
		}
		if (typelearning == false) {
			return "redirect:/learning/vocabulary";
		} else {
			return "redirect:/learning/grammar";
		}
	}

	// filter
	@RequestMapping(value = "filter", method = RequestMethod.GET)
	public String filter(ModelMap model, HttpServletRequest request) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		String inputFiler = request.getParameter("filter");

		// Truyền giá trị cho biến filter -> để dán lên page sau lọc
		filter = inputFiler;

		// Xử lý bỏ dấu câu
		String temp = Normalizer.normalize(inputFiler, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

		String SQL = "From Learning Where Type = " + "'" + pattern.matcher(temp).replaceAll("") + "'";

		System.out.println("SQL: Select * " + SQL);

		try {
			Query Query = session.createQuery(SQL);
			listlearning = Query.list();
			model.addAttribute("Learning", listlearning);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Trả về câu sql cho page view
		if (typelearning == false) {
			SQL_Vocabulary = "From Learning Where isVocabulary = 1 AND Type = " + "'"
					+ pattern.matcher(temp).replaceAll("") + "'";
			return "redirect:/learning/vocabulary";
		} else {
			SQL_Grammar = "From Learning Where isGrammar = 1 AND Type = " + "'" + pattern.matcher(temp).replaceAll("")
					+ "'";
			return "redirect:/learning/grammar";
		}
	}
}
