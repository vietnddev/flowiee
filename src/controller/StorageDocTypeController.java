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

import dao.Extension;
import dao.ProfileDAO;
import dao.RoleDAO;
import model.*;

@RequestMapping("/storage/")
@Transactional
@Controller
public class StorageDocTypeController {
	private Path path;

	@Autowired
	SessionFactory factory;

	@Autowired
	ServletContext application;

	Extension extension = new Extension();
	ArrayList<Notification> list_notification = null;

	RoleDAO checkrole = new RoleDAO();

	DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	LocalDateTime now = LocalDateTime.now();

	int IDParent = 0;

	@RequestMapping(value = "doctype")
	public String getDocumentType(ModelMap model, HttpServletRequest request, HttpSession httpSession) {
		/*
		 * Load màn hình danh sách loại tài liệu
		 */
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();

		String SQL_doctype = "From DocType Order by Sort";
		try {
			Query Query_doctype = session.createQuery(SQL_doctype);
			List<DocType> List_doctype = Query_doctype.list();

			model.addAttribute("doctype", List_doctype);

			// Phân trang
//			String SQL_count = "SELECT count(ID) FROM Storage";
//			Query Query_count = session.createQuery(SQL_count);
//			List<Storage> List_count = Query_count.list();
//			model.addAttribute("countstorage", List_count);

			//
			t.commit();

			//
			Log log = new Log();
			log.setUsers(httpSession.getAttribute("name") + " (" + httpSession.getAttribute("username_profile") + ")");
			log.setAction("Truy cập chức năng");
			log.setUrl("/admin/document-type");
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

		return "admin/pages/storage/document-type";
	}

	// Thêm mới loại tài liệu
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(value = "doctype/insert", method = RequestMethod.POST)
	public String insertDocType(ModelMap model, @ModelAttribute("doctype") DocType doctype, HttpServletRequest request,
			HttpSession httpSession) {
		model.addAttribute("doctype");
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();

		try {
			doctype.setStatus(true);
			session.save(doctype);
			t.commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			model.addAttribute("message", "Thêm mới thất bại!");
			e.printStackTrace();
		} finally {
			session.close();
		}
		return "redirect:/storage/doctype";
	}

	// Cập nhật loại tài liệu
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(value = "doctype/update", params = "btnUpdate", method = RequestMethod.POST)
	public String updateDocType(ModelMap model, HttpServletRequest request, HttpSession httpSession) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			String IDDocType = request.getParameter("txtUpdateDocType");
			DocType docType = (DocType) session.get(DocType.class, Integer.parseInt(IDDocType));
			//
			docType.setName(request.getParameter("name_update"));
			docType.setDescribes(request.getParameter("describes_update"));
			docType.setSort(Integer.parseInt(request.getParameter("sort_update")));			
			if(request.getParameter("status_update").equals("1")) {
				docType.setStatus(true);
			} else {
				docType.setStatus(false);
			}	
			//
			session.save(docType);
			t.commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			model.addAttribute("message", "Cập nhật thất bại!");
			e.printStackTrace();
		} finally {
			session.close();
		}
		return "redirect:/storage/doctype";
	}
}
