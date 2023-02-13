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
public class AdminCategoryController {
	@Autowired
	SessionFactory factory;

	Category category;

	Extension extension = new Extension();
	ArrayList<Notification> list_notification = null;

	RoleDAO checkrole = new RoleDAO();

	// index
	// Danh mục gốc
	@RequestMapping(value = "category-type")
	public String getCategoryType(ModelMap model, HttpServletRequest request, HttpSession httpSession) {
		try {
			Session session = factory.openSession();
			Query query = session.createQuery("From Category Where Code != '' order by Sort");
			List<Category> list = query.list();
			model.addAttribute("Category", list);
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
		return "admin/pages/admin/category-type";
	}

	// index
	// Danh mục chi tiết
	@RequestMapping(value = "category-type/{link}")
	public String getCategory(ModelMap model, HttpServletRequest request, HttpSession httpSession) {
		try {
			// Get type từ trang danh sách gốc
			String type = request.getParameter("type");
			// Dán type lên addAttribute cho chi tiết nhận
			model.addAttribute("type", type);
			Session session = factory.openSession();
			Query query = session.createQuery("From Category Where Type = " + type
					+ " and Code = '' and (Status = '1' OR Status = '2') order by Sort");
			List<Category> list = query.list();
			model.addAttribute("Category", list);
			//
			// Phân trang
			Query Query_count = session.createQuery("Select count(ID) FROM Category Where Type = " + type
					+ " and Code = '' and (Status = '1' OR Status = '2') order by Sort");
			List<Category> List_count = Query_count.list();
			model.addAttribute("countcategory", List_count);
			//
			model.addAttribute("page", "category-type");
			model.addAttribute("breadcrumb", "Danh sách loại danh mục");
			//
			list_notification = extension.getListNotification(extension.default_notification,
					ProfileDAO.accountID + "");
			model.addAttribute("notification", list_notification);
			model.addAttribute("noti_unread",
					extension.getNotificationUnRead(ProfileDAO.accountID + ""));
		} catch (HibernateQueryException e) {
			e.printStackTrace();
		}
		return "admin/pages/admin/category";
	}

	// insert danh mục chi tiết
	@RequestMapping(value = "insertCategory", params = "btnInsert", method = RequestMethod.POST)
	public String insert(ModelMap model, @ModelAttribute("category") Category category, HttpServletRequest request) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		String type = request.getParameter("type-insert");
		try {
			category.setType(type);
			category.setStatus(1);
			session.save(category);
			t.commit();
			// Trả về addAttribute type cho thằng view detail nhận để load tại chỗ
			model.addAttribute("type", type);
		} catch (Exception e) {
			session.getTransaction().rollback();
			model.addAttribute("message", "Thêm mới thất bại!");
			e.printStackTrace();
		} finally {
			session.close();
		}
		if (type == "1") {
			return "redirect:/admin/category-type/product";
		} else if (type == "2") {
			return "redirect:/admin/category-type/color";
		} else {
			return "redirect:/admin/category-type/size";
		}
	}// end method

	// Delete (New) - ok
	@RequestMapping(value = "updateCategory", params = "btnDeleteCategory", method = RequestMethod.POST)
	public String delete(ModelMap model, HttpServletRequest request, @ModelAttribute("category") Category category) {
		model.addAttribute("category");
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		// Lấy ID của account cần xóa
		String id = request.getParameter("txtDelete");
		// Khởi tạo account và truyền ID account cần xóa vào
		category = (Category) session.get(Category.class, Integer.parseInt(id));
		String type = request.getParameter("type-insert");
		try {
			// Thực hiện xóa xuống SQL
			session.delete(category);
			// Lưu lại
			t.commit();
			// Trả về addAttribute type cho thằng view detail nhận để load tại chỗ
			model.addAttribute("type", type);
		} catch (Exception e) {
			// Quay xe
			t.rollback();
			e.printStackTrace();
		}
		if (type == "1") {
			return "redirect:/admin/category-type/product";
		} else if (type == "2") {
			return "redirect:/admin/category-type/color";
		} else {
			return "redirect:/admin/category-type/size";
		}
	}

	// Update (New) - ok
	@RequestMapping(value = "updateCategory", params = "btnUpdateCategory", method = RequestMethod.POST)
	public String update(ModelMap model, HttpServletRequest request, @ModelAttribute("category") Category category) {
		model.addAttribute("category");
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();

		String id = request.getParameter("txtUpdateCategory");
		category = (Category) session.get(Category.class, Integer.parseInt(id));
		int sort = Integer.parseInt(request.getParameter("sort_update"));
		String type = request.getParameter("type-insert");
		try {
			category.setCode(category.getCode());
			category.setName(request.getParameter("name_update"));
			category.setLink(category.getLink());
			category.setStatus(1);
			category.setSort(sort);
			category.setNote(request.getParameter("note_update"));
			// Thực hiện xóa xuống SQL
			session.update(category);
			// Lưu lại
			t.commit();
			// Trả về addAttribute type cho thằng view detail nhận để load tại chỗ
			model.addAttribute("type", type);
		} catch (Exception e) {
			// Quay xe
			t.rollback();
			e.printStackTrace();
		}
		if (type == "1") {
			return "redirect:/admin/category-type/product";
		} else if (type == "2") {
			return "redirect:/admin/category-type/color";
		} else {
			return "redirect:/admin/category-type/size";
		}
	}
}
