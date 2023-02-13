package controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

//import com.sun.xml.internal.messaging.saaj.packaging.mime.util.QEncoderStream;

import model.*;

@RequestMapping(value = "/customer/")
@Controller
public class SalesCustomerController {
	private Path path;
	@Autowired
	SessionFactory factory;

	Extension extension = new Extension();
	ArrayList<Notification> list_notification = null;

	RoleDAO checkrole = new RoleDAO();

	// load màn danh sách
	@RequestMapping(value = "home")
	public String getAllProducts(ModelMap model, HttpServletRequest request, HttpSession httpSession) {
		//
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		//
		try {
			// load danh sách khách hàng
			String sql_customer_all = "From Customer";
			Query query_customer_all = session.createQuery(sql_customer_all);
			List<Customer> list_customer_all = query_customer_all.list();
			model.addAttribute("customer", list_customer_all);
			t.commit();

			//
			list_notification = extension.getListNotification(extension.default_notification,
					ProfileDAO.accountID + "");
			model.addAttribute("notification", list_notification);
			model.addAttribute("noti_unread",
					extension.getNotificationUnRead(ProfileDAO.accountID + ""));
		} catch (Exception e) {
			e.printStackTrace();
			t.rollback();
		}
		System.out.println("IDUser: " + httpSession.getAttribute("profileid"));
		System.out.println("IDRole: " + checkrole.ID_MCu);
		// Check role
		return "admin/pages/sales/customer/customer";
	}

	@RequestMapping(value = "detail-id={ID}")
	public String getCustomerDetail(ModelMap model, @PathVariable("ID") int id, HttpSession httpSession) {
		// model.addAttribute("product", new Product());
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		//
		String sql_customer_detail = "From Customer Where ID = " + id;
		String sql_orders = "From Orders Where IDCustomer = " + id + " Order by Date DESC";
		//
		try {
			Query query_customer_detail = session.createQuery(sql_customer_detail);
			List<Customer> list_customer_detail = query_customer_detail.list();
			model.addAttribute("detail", list_customer_detail);

			//
			Query query_orders = session.createQuery(sql_orders);
			List<Orders> list_orders = query_orders.list();
			model.addAttribute("orders", list_orders);
			for (int i = 0; i < list_orders.size(); i++) {
				if (list_orders.get(i).getStatus().equals("1")) {
					list_orders.get(i).setStatus("Chờ xác nhận");
				} else if (list_orders.get(i).getStatus().equals("2")) {
					list_orders.get(i).setStatus("Đang giao hàng");
				} else if (list_orders.get(i).getStatus().equals("3")) {
					list_orders.get(i).setStatus("Đã hoàn thành");
				} else {
					list_orders.get(i).setStatus("Đã hủy");
				}
			}

			//
			model.addAttribute("page", "customers");
			model.addAttribute("breadcrumb", "Danh sách khách hàng");
			//
			list_notification = extension.getListNotification(extension.default_notification,
					ProfileDAO.accountID + "");
			model.addAttribute("notification", list_notification);
			model.addAttribute("noti_unread",
					extension.getNotificationUnRead(ProfileDAO.accountID + ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "admin/pages/sales/customer/customer-detail";
	}

	// Thêm mới account (New) - ok
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(ModelMap model, @ModelAttribute("customer") Customer customer, HttpServletRequest request) {
		// model.addAttribute("product");
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			customer.setPassword("Flowiee@123");
			if (request.getParameter("email").equals("")) {
				customer.setEmail("Flowiee@");
			}
			if (request.getParameter("status").equals("yes")) {
				customer.setStatus(true);
			} else if (request.getParameter("status").equals("no")) {
				customer.setStatus(false);
			}
			session.save(customer);
			t.commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			model.addAttribute("message", "Thêm mới thất bại!");
			e.printStackTrace();
		} finally {
			session.close();
		}
		return "redirect:/customer/home";
	}

	// Thêm mới account (New) - ok
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(ModelMap model, HttpServletRequest request) {
		//
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		//
		Customer customer;
		//
		String IDCustomer = request.getParameter("IDCustomer");
		System.out.println("ID: " + IDCustomer);
		customer = (Customer) session.get(Customer.class, Integer.parseInt(IDCustomer));
		try {
			customer.setName(request.getParameter("name"));
			customer.setPhone(request.getParameter("phone"));
			customer.setEmail(request.getParameter("email"));
			customer.setAddress(request.getParameter("address"));
			if (request.getParameter("status").equals("yes")) {
				customer.setStatus(true);
			} else if (request.getParameter("status").equals("no")) {
				customer.setStatus(false);
			}
			session.update(customer);
			t.commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			model.addAttribute("message", "Thêm mới thất bại!");
			e.printStackTrace();
		} finally {
			session.close();
		}
		return "redirect:/customer/detail-id=" + IDCustomer;
	}
}
