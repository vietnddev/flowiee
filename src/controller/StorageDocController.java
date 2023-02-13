package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import dao.StgDocDAO;
import model.*;

@RequestMapping("/storage/")
@Transactional
@Controller
public class StorageDocController {
	private Path path;

	@Autowired
	SessionFactory factory;

	@Autowired
	ServletContext application;

	Extension extension = new Extension();
	ArrayList<Notification> list_notification = null;

	RoleDAO checkrole = new RoleDAO();
	StgDocDAO stgDocHelper = new StgDocDAO();

	DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	LocalDateTime now = LocalDateTime.now();

	int IDParent = 0;
	int IDDocDetail = 0;

	// Danh sách docfield và data kèm theo
	List<DocField_DocData> listDocField;

	//
	List<Account> listAccountNotShare = null;
	List<Account> listAccountShared = null;

	// load màn danh sách
	@RequestMapping(value = "docs")
	public String getStgRoot(ModelMap model, HttpServletRequest request, HttpSession httpSession) {
		//
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime now = LocalDateTime.now();
		model.addAttribute("currentdate", dtf.format(now));
		//
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();

		//
		try {					
			IDParent = 0;

			// Load danh sách loại tài liệu
			Query query_doctype = session.createQuery("From DocType Where Status = " + true);
			List<DocType> list_doctype = query_doctype.list();
			model.addAttribute("listDocType", list_doctype);

			// load danh sách tài liệu
			List<Storage> List_stg_root = stgDocHelper.getListDoc(IDParent, ProfileDAO.accountID);
			model.addAttribute("storage", List_stg_root);

			// Phân trang
			String SQL_count = "SELECT count(ID) FROM Storage";
			Query Query_count = session.createQuery(SQL_count);
			List<Storage> List_count = Query_count.list();
			model.addAttribute("countstorage", List_count);

			//
			t.commit();
			
			//
			Log log = new Log();
			log.setUsers(httpSession.getAttribute("name") + " (" + httpSession.getAttribute("username_profile") + ")");
			log.setAction("Truy cập chức năng");
			log.setUrl("/admin/storage");
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
		// Check role
		return "admin/pages/storage/stgdocs";
	}

	// load màn danh sách theo idparent
	@RequestMapping(value = "folder-id={ID}")
	public String getStgParent(ModelMap model, @PathVariable("ID") int id, HttpServletRequest request,
			HttpSession httpSession) {
		//
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime now = LocalDateTime.now();
		model.addAttribute("currentdate", dtf.format(now));
		//
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();

		IDParent = id; // đây là id để load lại đúng trang parent -> Không được xóa
		//
		try {
			// Load danh sách loại tài liệu
			Query query_doctype = session.createQuery("From DocType Where Status = " + true);
			List<DocType> list_doctype = query_doctype.list();
			model.addAttribute("listDocType", list_doctype);

			// load danh sách sản phẩm
			List<Storage> List_stg = stgDocHelper.getListDoc(IDParent, ProfileDAO.accountID);

			model.addAttribute("storage", List_stg);

			// Phân trang
			String SQL_count = "SELECT count(ID) FROM Storage";
			Query Query_count = session.createQuery(SQL_count);
			List<Storage> List_count = Query_count.list();
			model.addAttribute("countstorage", List_count);

			//
			t.commit();

			//
			Log log = new Log();
			log.setUsers(httpSession.getAttribute("name") + " (" + httpSession.getAttribute("username_profile") + ")");
			log.setAction("Truy cập chức năng");
			log.setUrl("/storage/folder-id=" + id);
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
		// Check role
		return "admin/pages/storage/stgdocs";
	}

	// Thêm mới thư mục
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(value = "insertFolder", method = RequestMethod.POST)
	public String insertFolderRoot(ModelMap model, @ModelAttribute("storage") Storage storage,
			HttpServletRequest request, HttpSession httpSession) {
		model.addAttribute("storage");
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();

		try {
			storage.setIDParent(IDParent);
			storage.setType(0);
			storage.setAuthor(ProfileDAO.accountID + "");
			//
			session.save(storage);
			t.commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			model.addAttribute("message", "Thêm mới thất bại!");
			e.printStackTrace();
		} finally {
			session.close();
		}
		if (IDParent == 0) {
			return "redirect:/storage/docs";
		} else {
			return "redirect:/storage/folder-id=" + IDParent;
		}
	}

	// Thêm mới tài liệu
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(value = "insertFile", method = RequestMethod.POST)
	public String insertFileRoot(@RequestParam("file") MultipartFile file, ModelMap model,
			@ModelAttribute("storage") Storage storage, HttpServletRequest request, HttpSession httpSession) {
		//
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddhhmmss");
		LocalDateTime now = LocalDateTime.now();
		model.addAttribute("currentdate", dtf.format(now));
		//
		model.addAttribute("storage");
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();

		try {
			// Xử lý file
			String rootDir = request.getSession().getServletContext().getRealPath("/");
			if (file != null && !file.isEmpty()) {
				path = Paths.get(rootDir + "\\WEB-INF\\views\\admin\\assets\\storage\\" + dtf.format(now) + "-"
						+ file.getOriginalFilename());

				System.out.println("Img save at: " + path);

				// Lưu hình đã chọn vào thư mục đã cấu hình
				file.transferTo(new File(path.toString()));
				System.out.println("Save ok?");
			}
			//
			storage.setIDParent(IDParent);
			storage.setType(1);
			storage.setAuthor(ProfileDAO.accountID + "");
			storage.setSize(file.getSize());
			storage.setStgName(dtf.format(now) + "-" + file.getOriginalFilename());
			storage.setPath(path + "");
			//
			session.save(storage);

			// Set docdata = null khi vừa up file lên
			Query query_lsdocfiled = session
					.createQuery("From DocField Where IDDocType = " + request.getParameter("IDDocType"));
			List<DocField> list_lsdocfiled = query_lsdocfiled.list();
			for (int i = 0; i < list_lsdocfiled.size(); i++) {
				DocData docData = new DocData();
				docData.setIDDocField(list_lsdocfiled.get(i).getID());
				docData.setValue("");
				docData.setIDDoc(storage.getID());
				session.save(docData);
			}

			t.commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			model.addAttribute("message", "Thêm mới thất bại!");
			e.printStackTrace();
		} finally {
			session.close();
		}
		if (IDParent == 0) {
			return "redirect:/storage/docs";
		} else {
			return "redirect:/storage/folder-id=" + IDParent;
		}
	}

	@RequestMapping(value = "delete-idstgdoc={ID}")
	public String DeleteDoc(@PathVariable("ID") int IDStgDoc) {
		/*
		 * Xóa tài liệu hoặc thư mục
		 */
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		//
		Storage storage = (Storage) session.get(Storage.class, IDStgDoc);
		try {
			session.delete(storage);
			t.commit();
		} catch (Exception e) {
			t.rollback();
			e.printStackTrace();
		}
		if (IDParent != 0) {
			return "redirect:/storage/folder-id=" + IDParent;
		} else {
			return "redirect:/storage/docs";
		}
	}

	@RequestMapping(value = "update-idstgdoc={ID}")
	public String UpdateDoc(ModelMap model, HttpServletRequest request, @PathVariable("ID") int IDDoc) {
		/*
		 * Cập nhật tên và mô tả của tài liệu
		 */
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		//
		Storage storage = (Storage) session.get(Storage.class, IDDoc);
		try {
			storage.setName(request.getParameter("nameUpdate"));
			storage.setDescribes(request.getParameter("describesUpdate"));
			session.update(storage);
			t.commit();
		} catch (Exception e) {
			t.rollback();
			e.printStackTrace();
		}
		if (IDParent != 0) {
			return "redirect:/storage/folder-id=" + IDParent;
		} else {
			return "redirect:/storage/docs";
		}
	}

	@RequestMapping(value = "docs/update", params = "btnUpdateDocData", method = RequestMethod.POST)
	public String UpdateMetadataDoc(ModelMap model, HttpServletRequest request) {
		/*
		 * Cập nhật metadata cho Doc
		 */
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			for (int i = 0; i < listDocField.size(); i++) {
				String IDDocData = request.getParameter("IDDocData_" + listDocField.get(i).getIDDocData());
				System.out.println("IDDocData in form: " + IDDocData);
				String valueDocData = request.getParameter("ValueDocData_" + listDocField.get(i).getIDDocData());
				System.out.println("ValueDocData in form: " + valueDocData);
				//
				DocData docdata = (DocData) session.get(DocData.class, Integer.parseInt(IDDocData));
				docdata.setValue(valueDocData);
				//
				session.update(docdata);
			}
			t.commit();
		} catch (Exception e) {
			// t.rollback();
			e.printStackTrace();
		}
		System.out.println("IDDoc: " + request.getParameter("IDDoc"));
		return "redirect:/storage/detail-iddoc=" + request.getParameter("IDDoc");
	}

	@RequestMapping(value = "detail-iddoc={ID}")
	public String getAccountDetail(ModelMap model, @PathVariable("ID") int IDDoc, HttpSession httpSession) {
		/*
		 * Xem chi tiết tài liệu
		 */
		Session session = factory.openSession();
		this.IDDocDetail = IDDoc;

		// Load thông tin chi tiết của File
		Query query_detail = session.createQuery("From Storage Where ID = " + IDDoc);
		List<Storage> list_detail = query_detail.list();
		model.addAttribute("detailDoc", list_detail);

		// Load danh sách loại tài liệu
		Query query_doctype = session.createQuery("From DocType Where Status = " + true);
		List<DocType> list_doctype = query_doctype.list();
		model.addAttribute("listDocType", list_doctype);

		// Load danh sách tài khoản đã được phân quyền
		listAccountShared = stgDocHelper.GetListAccountDuocPhanQuyen(IDDoc);
		model.addAttribute("listAccountShared", listAccountShared);

		// Load danh sách tài khoản chưa được phân quyền
		listAccountNotShare = session.createQuery("From Account where Status = true").list();

		// Kiểm tra nếu trong ds đã chia sẽ có user A -> thì remove user A ra khỏi list
		// chưa chia sẽ
		for (int i = 0; i < listAccountShared.size(); i++) {
			System.out.println("listAccountShared " + listAccountShared.get(i).getID());
			for (int j = 0; j < listAccountNotShare.size(); j++) {
				System.out.println(listAccountNotShare.get(j).getID());
				if (listAccountShared.get(i).getID() - listAccountNotShare.get(j).getID() == 0) {
					listAccountNotShare.remove(listAccountNotShare.get(j));
				}
			}
		}

		model.addAttribute("listAccountNotShared", listAccountNotShare);

		// Dán tên của loại tài liệu lên field
		model.addAttribute("nameDocType", stgDocHelper.getNameDocTypeByID(list_detail.get(0).getIDDocType()));

		// Load danh sách trường thông tin
		listDocField = stgDocHelper.getDocFieldDocData(IDDoc);
		model.addAttribute("listDocField", listDocField);

		// Breadcrumb
		model.addAttribute("page", "storage"); // Redirect về trang /account
		model.addAttribute("breadcrumb", "Thư mục gốc");
		// Load chuông thông báo
		list_notification = extension.getListNotification(extension.default_notification, ProfileDAO.accountID + "");
		model.addAttribute("notification", list_notification);
		model.addAttribute("noti_unread", extension.getNotificationUnRead(ProfileDAO.accountID + ""));
		//
		return "admin/pages/storage/storage-detail";
	}

	@RequestMapping(value = "docs/update", params = "changeFile", method = RequestMethod.POST)
	public String updateFileAttach(@RequestParam("file") MultipartFile file, ModelMap model,
			HttpServletRequest request) {
		/*
		 * Thay file
		 */
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddhhmmss");
		LocalDateTime now = LocalDateTime.now();

		try {
			Storage storage = (Storage) session.get(Storage.class, IDDocDetail);
			// Xử lý file
			String rootDir = request.getSession().getServletContext().getRealPath("/");
			if (file != null && !file.isEmpty()) {
				path = Paths.get(rootDir + "\\WEB-INF\\views\\admin\\assets\\storage\\" + dtf.format(now) + "-"
						+ file.getOriginalFilename());

				System.out.println("fileUpdate save at: " + path);

				// Lưu file đã chọn vào thư mục đã cấu hình
				file.transferTo(new File(path.toString()));
				System.out.println("Save ok?");
			}

			storage.setSize(file.getSize());
			storage.setStgName(dtf.format(now) + "-" + file.getOriginalFilename());
			storage.setPath(path + "");
			session.update(storage);

			t.commit();
		} catch (Exception e) {
			// t.rollback();
			e.printStackTrace();
		}
		System.out.println("IDDoc: " + request.getParameter("IDDoc"));
		return "redirect:/storage/detail-iddoc=" + IDDocDetail;
	}

	// Download file về
	@RequestMapping(value = "docs/download-iddoc={ID}")
	public void DownloadFile(HttpServletRequest request, HttpServletResponse response, @PathVariable("ID") int IDDoc) {
		// Lấy tên của file lưu dưới server
		String docName = stgDocHelper.getStgNameDocByID(IDDoc);
		String rootDir = request.getSession().getServletContext().getRealPath("/");
		path = Paths.get(rootDir + "\\WEB-INF\\views\\admin\\assets\\storage\\" + docName);
		// Kiểm tra nếu file tồn tại thì tải về
		if (Files.exists(path)) {
			System.out.println("File tồn tại!");
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=" + docName);
			try {
				Files.copy(path, response.getOutputStream());
				response.getOutputStream().flush();
				response.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("File không tồn tại!");
		}
	}

	// Cập nhật phân quyền xem tài liệu
	@RequestMapping(value = "docs/update", params = "updateShare", method = RequestMethod.POST)
	public String updateDocShare(HttpServletRequest request) {
		Session session = factory.openSession();
		// Gỡ toàn bộ quyền xem của tất cả user trên tài liệu này
		stgDocHelper.DeleteDocShareByIDUser(IDDocDetail);
		// Nếu có check thì insert lại
		for (int i = 0; i < listAccountShared.size(); i++) {
			String IDUser = request.getParameter("checkboxshared" + listAccountShared.get(i).getID());			
			if (IDUser != null) {
				DocShare docShare = new DocShare(Integer.parseInt(IDUser), IDDocDetail);
				session.save(docShare);				
			}
		}
		// Insert thêm chia sẽ
		for (int i = 0; i < listAccountNotShare.size(); i++) {
			String IDUser = request.getParameter("checkboxnotshare" + listAccountNotShare.get(i).getID());			
			if (IDUser != null) {
				DocShare docShare = new DocShare(Integer.parseInt(IDUser), IDDocDetail);
				session.save(docShare);				
			}
		}
		return "redirect:/storage/detail-iddoc=" + IDDocDetail;
	}
}
