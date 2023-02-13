package controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import dao.CategoryDAO;
import dao.Extension;
import dao.ProfileDAO;
import dao.RoleDAO;
import dao.StgDocDAO;
import model.*;

@RequestMapping("/storage/")
@Transactional
@Controller
public class StorageDocFieldController {
	private Path path;

	@Autowired
	SessionFactory factory;

	@Autowired
	ServletContext application;

	// Khai báo lớp helper
	Extension extension = new Extension();
	CategoryDAO categoryHelper = new CategoryDAO();
	StgDocDAO stgDocHelper = new StgDocDAO();

	ArrayList<Notification> list_notification = null;

	DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	LocalDateTime now = LocalDateTime.now();

	int iDDocType = 0;

	@RequestMapping(value = "docfield-iddoctype={ID}")
	public String getDocumentType(ModelMap model, @PathVariable("ID") int iDDocType, HttpServletRequest request,
			HttpSession httpSession) {
		this.iDDocType = iDDocType;
		/*
		 * Load màn hình danh sách loại tài liệu
		 */
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();

		String SQL_doctfield = "From DocField Where IDDocType = " + iDDocType + "Order by Sort";
		try {
			Query Query_doctfield = session.createQuery(SQL_doctfield);
			List<DocField> List_doctfield = Query_doctfield.list();

			model.addAttribute("docfield", List_doctfield);

			List<Category> list_category = categoryHelper.getCategoryByID(11);
			model.addAttribute("fieldtype", list_category);

			//
			t.commit();

			//
			Log log = new Log();
			log.setUsers(httpSession.getAttribute("name") + " (" + httpSession.getAttribute("username_profile") + ")");
			log.setAction("Truy cập chức năng");
			log.setUrl("/admin/document-field");
			log.setCreated(dateTimeFormatter.format(now));
			// Lấy IP
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
			}
			log.setIP(ipAddress);
			//
			session.save(log);

			// Load chuông thông báo
			list_notification = extension.getListNotification(extension.default_notification,
					ProfileDAO.accountID + "");
			model.addAttribute("notification", list_notification);
			model.addAttribute("noti_unread", extension.getNotificationUnRead(ProfileDAO.accountID + ""));
		} catch (Exception e) {
			e.printStackTrace();
			t.rollback();
		}

		return "admin/pages/storage/document-field";
	}

	// Thêm mới thư mục
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(value = "docfield/insert", method = RequestMethod.POST)
	public String insertDocType(ModelMap model, @ModelAttribute("docfield") DocField docfield,
			HttpServletRequest request, HttpSession httpSession) {
		List<Storage> listIDStgdoc;
		try {
			docfield.setIDDocType(iDDocType);
			String type = request.getParameter("required");
			if (type == null) {
				docfield.setRequired(false);
			} else {
				docfield.setRequired(true);
			}
			Session session = factory.openSession();
			Transaction t = session.beginTransaction();
			session.save(docfield);
			t.commit();
			// insert value default của filed mới xuống bảng docdata
			listIDStgdoc = stgDocHelper.getIDStgdocByIDDocType(iDDocType);
			for (int i = 0; i < listIDStgdoc.size(); i++) {
				DocData docData = new DocData();
				docData.setIDDocField(docfield.getID());
				docData.setValue("");
				docData.setIDDoc(listIDStgdoc.get(i).getID());
				Session session2 = factory.openSession();
				Transaction t2 = session.beginTransaction();
				session2.save(docData);
				t2.commit();
			}
		} catch (Exception e) {
			// session.getTransaction().rollback();
			model.addAttribute("message", "Thêm mới thất bại!");
			e.printStackTrace();
		}
		return "redirect:/storage/docfield-iddoctype=" + iDDocType;
	}

	// Cập nhật docfield
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(value = "docfield/update", params = "btnUpdate", method = RequestMethod.POST)
	public String updateDocType(ModelMap model, HttpServletRequest request, HttpSession httpSession) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		String IDDocField = request.getParameter("txtUpdateDocField");
		try {
			DocField docField = (DocField) session.get(DocField.class, Integer.parseInt(IDDocField));
			//
			docField.setType(request.getParameter("type_update"));
			docField.setName(request.getParameter("name_update"));
			docField.setSort(Integer.parseInt(request.getParameter("sort_update")));
			if (request.getParameter("required_update").equals("1")) {
				docField.setRequired(true);
			} else {
				docField.setRequired(false);
			}
			//
			session.save(docField);
			t.commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			model.addAttribute("message", "Cập nhật thất bại!");
			e.printStackTrace();
		} finally {
			session.close();
		}
		return "redirect:/storage/docfield-iddoctype=" + iDDocType;
	}
}
