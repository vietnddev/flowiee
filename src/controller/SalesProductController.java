package controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
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

@RequestMapping(value = "/product/")
@Controller
public class SalesProductController {
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

	// Xử lý filter
	String ftype = ""; // khởi tạo giá trị cho filter
	String fcolor = ""; // khởi tạo giá trị cho filter
	String fstatus = ""; // khởi tạo giá trị cho filter
	List<Product> list_product;
	String SQL = "From Product order by Type";
	
	// load màn danh sách
	@RequestMapping(value = "home")
	public String getAllProducts(ModelMap model, HttpServletRequest request, HttpSession httpSession) {
		//
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime now = LocalDateTime.now();
		model.addAttribute("currentdate", dtf.format(now));
		//
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		//
		int type_product = 1;
		int type_color = 2;
		int type_size = 3;
		// Dán filter đã được chọn lên page
		if (ftype != "") {
			model.addAttribute("ftype", ftype);
		}
		if (fcolor != "") {
			model.addAttribute("fcolor", fcolor);
		}
		if (fstatus != "") {
			model.addAttribute("fstatus", fstatus);
		}
		//
		try {
			// load danh sách sản phẩm
			// String sql_product_all = "From Product order by Type";
			Query query_product_all = session.createQuery(SQL);
			list_product = query_product_all.list();
			model.addAttribute("product", list_product);
			ftype = "Chọn loại sản phẩm";
			fcolor = "Chọn màu sắc";
			fstatus = "Chọn trạng thái";
			SQL = "From Product order by Type";

			// load danh mục dropdownlist
			Query query_category_product = session.createQuery(
					"From Category Where Type = " + type_product + " and Code = '' and Status = '1' order by Sort ");
			List<Category> list_category_product = query_category_product.list();
			model.addAttribute("category_product", list_category_product);

			// load danh mục dropdownlist
			Query query_category_color = session.createQuery(
					"From Category Where Type = " + type_color + " and Code = '' and Status = '1' order by Sort ");
			List<Category> list_category_color = query_category_color.list();
			model.addAttribute("category_color", list_category_color);

			// load danh mục dropdownlist
			Query query_category_size = session.createQuery(
					"From Category Where Type = " + type_size + " and Code = '' and Status = '1' order by Sort ");
			List<Category> list_category_size = query_category_size.list();
			model.addAttribute("category_size", list_category_size);

			// Phân trang
			String SQL_count = "SELECT count(ID) FROM Product";
			Query Query_count = session.createQuery(SQL_count);
			List<Product> List_count = Query_count.list();
			model.addAttribute("countproduct", List_count);

			// Dán số lượng records lên page
			model.addAttribute("Total_list_product", list_product.size());

			//
			t.commit();

			// Làm mờ nút addToCart nếu hết hàng
			boolean addToCart = false;
			List<ProductDTO> list = (List<ProductDTO>) httpSession.getAttribute("cart");
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getSoLuong() - list.get(i).getProduct().getStorage() == 0) {
						addToCart = false;
					} else {
						addToCart = true;
					}
				}
			}
			httpSession.setAttribute("isAddToCart", addToCart);

			//
			Log log = new Log();
			log.setUsers(httpSession.getAttribute("name") + " (" + httpSession.getAttribute("username_profile") + ")");
			log.setAction("Truy cập chức năng");
			log.setUrl("/admin/products");
			log.setCreated(dateTimeFormatter.format(now));
			// Lấy IP
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
			}
			log.setIP(ipAddress);
			//
			session.save(log);

			if (checkrole.checkRole(Integer.parseInt(ProfileDAO.accountID + ""),
					checkrole.ID_MPI) > 0) {
				model.addAttribute("enableRole", "true");
			} else {
				model.addAttribute("enableRole", "false");
			}

			// Load chuông thông báo
			list_notification = extension.getListNotification(extension.default_notification,
					ProfileDAO.accountID + "");
			model.addAttribute("notification", list_notification);
			model.addAttribute("noti_unread",
					extension.getNotificationUnRead(ProfileDAO.accountID + ""));
		} catch (Exception e) {
			e.printStackTrace();
			t.rollback();
		}
		// Check role
		return "admin/pages/sales/product/product";
	}

	@RequestMapping(value = "detail-id={ID}")
	public String getProductDetail(ModelMap model, @PathVariable("ID") int id, HttpSession httpSession) {
		model.addAttribute("product", new Product());
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		//
		String sql_product_detail = "From Product Where ID = " + id;
		//
		int type_product = 1;
		int type_color = 2;
		int type_size = 3;
		try {
			Query query_product_detail = session.createQuery(sql_product_detail);
			List<Product> list_product_detail = query_product_detail.list();
			model.addAttribute("detail", list_product_detail);

			// load danh mục dropdownlist
			String sql_category_product = "From Category Where Type = " + type_product
					+ " and Code = '' order by Sort ";
			Query query_category_product = session.createQuery(
					"From Category Where Type = " + type_product + " and Code = '' and Status = '1' order by Sort ");
			List<Category> list_category_product = query_category_product.list();
			model.addAttribute("category_product_detail", list_category_product);

			// load danh mục dropdownlist
			String sql_category_color = "From Category Where Type = " + type_color
					+ " and Code = '' and Status = '1' order by Sort ";
			Query query_category_color = session.createQuery(sql_category_color);
			List<Category> list_category_color = query_category_color.list();
			model.addAttribute("category_color_detail", list_category_color);

			// load danh mục dropdownlist
			String sql_category_size = "From Category Where Type = " + type_size
					+ " and Code = '' and Status = '1' order by Sort ";
			Query query_category_size = session.createQuery(
					"From Category Where Type = " + type_size + " and Code = '' and Status = '1' order by Sort ");
			List<Category> list_category_size = query_category_size.list();
			model.addAttribute("category_size_detail", list_category_size);

			// load bình luận cmt
			String sql_cmt = "From Comment Where IDProduct = " + id + " AND Status = 1 AND Code = 0";
			Query query_cmt = session.createQuery(sql_cmt);
			List<Comment> list_cmt = query_cmt.list();
			model.addAttribute("listcomment", list_cmt);

			String sql_cmt2 = "SELECT comment.ID, comment.name, rep.IDCmt, rep.Name as 'Name rep' FROM comment "
					+ "left join Rep " + " ON rep.IDCmt = comment.ID " + "	WHERE IDProduct = " + id;

			Query query_cmt2 = session.createQuery(sql_cmt);
			List<Comment> list_cmt2 = query_cmt.list();
			model.addAttribute("listcomment2", list_cmt2);

			// load danh sách sub-img
			String sql_image = "From Image Where IDProduct = " + id;
			Query query_image = session.createQuery(sql_image);
			List<Image> list_image = query_image.list();
			model.addAttribute("list_image", list_image);
			//
			model.addAttribute("page", "products");
			model.addAttribute("breadcrumb", "Danh sách sản phẩm");

			// Check role
			if (checkrole.checkRole(Integer.parseInt(ProfileDAO.accountID + ""),
					checkrole.ID_MPU) > 0) {
				model.addAttribute("enableRole", "true");
			} else {
				model.addAttribute("enableRole", "false");
			}

			// Load chuông thông báo
			list_notification = extension.getListNotification(extension.default_notification,
					ProfileDAO.accountID + "");
			model.addAttribute("notification", list_notification);
			model.addAttribute("noti_unread",
					extension.getNotificationUnRead(ProfileDAO.accountID + ""));

			// Get thống kê sp đã bán by channel
			model.addAttribute("shoppe", extension.getProductSoldOnChannel(id, "Shopee"));
			model.addAttribute("facebook", extension.getProductSoldOnChannel(id, "Facebook"));
			model.addAttribute("instagram", extension.getProductSoldOnChannel(id, "Instagram"));
			model.addAttribute("hotline", extension.getProductSoldOnChannel(id, "Hotline"));
			model.addAttribute("zalo", extension.getProductSoldOnChannel(id, "Zalo"));
			model.addAttribute("other", extension.getProductSoldOnChannel(id, "Khác"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Check role
		return "admin/pages/sales/product/product-detail";
	}

	// Thêm mới sản phẩm (New) - ok
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(value = "insert", params = "btnInsert", method = RequestMethod.POST)
	public String insert(@RequestParam("file") MultipartFile img, ModelMap model,
			@ModelAttribute("product") Product product, HttpServletRequest request) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		//
		model.addAttribute("product");
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			product.setIDBrand(1);

			if (request.getParameter("highlight").equals("yes")) {
				product.setHighLight(true);
			} else {
				product.setHighLight(false);
			}

			if (request.getParameter("status").equals("yes")) {
				product.setStatus(true);
			} else {
				product.setStatus(false);
			}
			product.setDate(dateTimeFormatter.format(now));
			product.setPromotion(0);
			session.save(product);
			// Xử lý lưu ảnh
			String rootDir = request.getSession().getServletContext().getRealPath("/");
			if (img != null && !img.isEmpty()) {
				// Đường dẫn full (Thư mục + tên file)

				path = Paths
						.get(rootDir + "\\WEB-INF\\views\\admin\\assets\\img\\products\\" + product.getID() + ".png");

				System.out.println("Img save at: " + path);

				// Lưu hình đã chọn vào thư mục đã cấu hình
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
		return "redirect:/product/home";
	}

	// Xóa sản phẩm (New) - ok
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(value = "deleteProduct-{ID}", params = "delete")
	public String delete(ModelMap model, HttpServletRequest request, @PathVariable("ID") int iDProduct,
			HttpSession httpSession) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		Product product;
		//
		product = (Product) session.get(Product.class, iDProduct);
		try {
			session.delete(product);
			t.commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			model.addAttribute("message", "Xóa thất bại!");
			e.printStackTrace();
		} finally {
			session.close();
		}
		return "redirect:/product/home";
	}

	// Thêm mới sub img
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(value = "update", params = "btnInsert", method = RequestMethod.POST)
	public String insertImage(@RequestParam("filesub") MultipartFile img, ModelMap model, HttpServletRequest request,
			@ModelAttribute("image") Image image) {
		//
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		String iDProduct = request.getParameter("IDProduct");

		try {
			image.setIDProduct(Integer.parseInt(iDProduct));
			image.setNote(request.getParameter("notesub"));
			image.setStatus(true);

			session.save(image);

			String rootDir = request.getSession().getServletContext().getRealPath("/");
			if (img != null && !img.isEmpty()) {
				path = Paths.get(rootDir + "\\WEB-INF\\views\\admin\\assets\\img\\products\\" + image.getID() + ".png");
				img.transferTo(new File(path.toString()));
				System.out.println(path);
				t.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/product/detail-id=" + iDProduct;
	}

	// Xóa sub img
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(value = "update", params = "btnDeleteImage", method = RequestMethod.POST)
	public String deleteSubImage(ModelMap model, HttpServletRequest request) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		Image image;
		String iDImage = request.getParameter("subID");
		String iDProduct = request.getParameter("IDProduct");
		//
		image = (Image) session.get(Image.class, Integer.parseInt(iDImage));
		try {
			session.delete(image);
			t.commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			model.addAttribute("message", "Xóa thất bại!");
			e.printStackTrace();
		} finally {
			session.close();
		}
		return "redirect:/product/detail-id=" + iDProduct;
	}

	// Update sản phẩm @RequestParam("file") MultipartFile img,
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(value = "update", params = "btnUpdate", method = RequestMethod.POST)
	public String update(@RequestParam("filesub") MultipartFile img, ModelMap model, HttpServletRequest request,
			@RequestParam("filemain") MultipartFile img_main, @ModelAttribute("image") Image image,
			HttpSession httpSession) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		String iDProduct = request.getParameter("IDProduct");
		System.out.println(iDProduct);
		//
		Product product;
		//
		String iD = request.getParameter("IDProduct");
		product = (Product) session.get(Product.class, Integer.parseInt(iD));
		//
		String price = (request.getParameter("price"));
		try {
			product.setCode(request.getParameter("code"));
			product.setName(request.getParameter("name"));

			product.setPrice(Double.parseDouble(price.replace(".", "")));// Do có chứa dấu "." trong input nên cần cắt
																			// chuỗi về thuần số
			product.setStorage(Integer.parseInt(request.getParameter("storage")));
			product.setQuantity(Integer.parseInt(request.getParameter("quantity")));
			product.setDescribes(request.getParameter("describes"));
			product.setType(request.getParameter("type"));
			product.setSize(request.getParameter("size"));
			product.setColor(request.getParameter("color"));
			product.setDate(request.getParameter("date"));
			product.setPromotion(Integer.parseInt(request.getParameter("promotion")));
			if (request.getParameter("highLight").equals("yes")) {
				product.setHighLight(true);
			} else {
				product.setHighLight(false);
			}
			if (request.getParameter("status").equals("yes")) {
				product.setStatus(true);
			} else {
				product.setStatus(false);
			}
			session.update(product);
			//
			String rootDir = request.getSession().getServletContext().getRealPath("/");
			if (img_main != null && !img_main.isEmpty()) {
				path = Paths
						.get(rootDir + "\\WEB-INF\\views\\admin\\assets\\img\\products\\" + product.getID() + ".png");
				img_main.transferTo(new File(path.toString()));
			}
			System.out.println("path: " + path);
			t.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/product/detail-id=" + iDProduct;
	}

	// Trả lời bình luận trong chi tiết sản phẩm
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(value = "update", params = "btnReply", method = RequestMethod.POST)
	public String insertComment(ModelMap model, HttpServletRequest request, HttpSession httpSession) {
		//
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		//
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		String iDProduct = request.getParameter("IDProduct");
		System.out.println("IDProduct " + iDProduct);
		String iDCmt = request.getParameter("IDComment");
		System.out.println("IDComment " + iDCmt);
		//
		Comment cmt = new Comment();
		try {
			cmt.setCode(Integer.parseInt(iDCmt));
			cmt.setCreated(dtf.format(now));
			cmt.setDescribes(request.getParameter("txtReply"));
			cmt.setEmail(httpSession.getAttribute("email_profile") + "");
			cmt.setIDProduct(Integer.parseInt(iDProduct));
			cmt.setName(httpSession.getAttribute("name") + "");
			cmt.setPhone(httpSession.getAttribute("phone_profile") + "");
			cmt.setStatus(true);
			session.save(cmt);
			t.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/product/detail-id=" + iDProduct;
	}

	// filter
	@RequestMapping(value = "filter", method = RequestMethod.POST)
	public String filter(ModelMap model, HttpServletRequest request, HttpSession httpSession) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		String ftype = request.getParameter("ftype");
		String fcolor = request.getParameter("fcolor");
		String fstatus = request.getParameter("fstatus");

		// Gán giá trị đã chọn của bộ lọc lên page
		this.ftype = ftype;
		this.fcolor = fcolor;
		if (fstatus == "1") {
			this.fstatus = "Đang kinh doanh";
		} else if (fstatus == "0") {
			this.fstatus = "Ngừng kinh doanh";
		}

		// Xử lý bỏ dấu câu
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		//
		String temp_type = Normalizer.normalize(ftype, Normalizer.Form.NFD);
		String temp_color = Normalizer.normalize(fcolor, Normalizer.Form.NFD);
		String temp_status = Normalizer.normalize(fstatus, Normalizer.Form.NFD);
		//
		String type = pattern.matcher(temp_type).replaceAll("");
		String color = pattern.matcher(temp_color).replaceAll("");
		String status = pattern.matcher(temp_status).replaceAll("");

		// Xử lý nếu người dùng không chọn giá trị trong bộ lọc
		if (type.equals("Chon loai san pham")) {
			type = "";
		}
		if (color.equals("Chon mau sac")) {
			color = "";
		}
		if (status.equals("Chon trang thai")) {
			status = "";
		}

		// Khởi tạo câu truy vấn khi sử dụng bộ lọc
		// User chỉ chọn loại SP
		if (type != "" && color == "" && status == "") {
			SQL = "From Product Where Type = " + "'" + type + "'";
		}
		// User chỉ chọn màu sắc
		else if (type == "" && color != "" && status == "") {
			SQL = "From Product Where Color = " + "'" + color + "'";
		}
		// User chỉ chọn trạng thái
		else if (type == "" && color == "" && status != "") {
			SQL = "From Product Where Status = " + "'" + status + "'";
		}
		// User chọn cả 3
		else if (type != "" && color != "" && status != "") {
			SQL = "From Product Where Type = " + "'" + type + "' AND Color = " + "'" + color + "'" + "' AND Status = "
					+ "'" + status + "'";
		} else {
			SQL = "From Product order by Type";
		}

		System.out.println("SQL: Select * " + SQL);

		System.out.println("Type: " + type + ";" + " Color: " + color + " Status: ");

		try {
			Query Query = session.createQuery(SQL);
			list_product = Query.list();
			model.addAttribute("product", list_product);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Check role
		return "redirect:/product/home";
	}
}
