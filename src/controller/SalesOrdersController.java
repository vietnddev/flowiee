package controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import dao.Extension;
import dao.OrderDAO;
import dao.ProfileDAO;
import dao.RoleDAO;
import model.*;

@RequestMapping("/orders/")
@Transactional
@Controller
public class SalesOrdersController {
	@Autowired
	SessionFactory factory;

	// Khai báo trạng thái đơn hàng
	String ID_UnConfirm = "1";
	String ID_Delivery = "2";
	String ID_Completed = "3";
	String ID_Cancel = "4";
	
	Extension extension = new Extension();
	ArrayList<Notification> list_notification = null;

	RoleDAO checkrole = new RoleDAO();
	
	OrderDAO orderHelper = new OrderDAO();

	// show trang tất cả đơn hàng
	@RequestMapping(value = "home")
	public String showListOrders(ModelMap model, HttpSession httpSession) {
		Session session = factory.openSession();
		try {					
			model.addAttribute("list_orders", orderHelper.getListByStatus("", ""));

			// Phân trang
			String SQL_count = "SELECT count(ID) FROM Orders";
			Query Query_count = session.createQuery(SQL_count);
			List<Orders> List_count = Query_count.list();
			model.addAttribute("countorders", List_count);
			//
			list_notification = extension.getListNotification(extension.default_notification,
					ProfileDAO.accountID + "");
			model.addAttribute("notification", list_notification);
			model.addAttribute("noti_unread", extension.getNotificationUnRead(ProfileDAO.accountID + ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Check role
		return "admin/pages/sales/orders";
	}

	// show các đơn hàng -> Chờ xác nhận
	@RequestMapping(value = "unconfirm")
	public String showListUnConfirm(ModelMap model, HttpSession httpSession) {
		Session session = factory.openSession();
		try {
			model.addAttribute("list_orders", orderHelper.getListByStatus(ID_UnConfirm, ""));

			// Phân trang
			String SQL_count = "SELECT count(ID) FROM Orders Where Status = '" + ID_UnConfirm + "'";
			Query Query_count = session.createQuery(SQL_count);
			List<Orders> List_count = Query_count.list();

			//
			model.addAttribute("countorders", List_count);
			//
			list_notification = extension.getListNotification(extension.default_notification,
					ProfileDAO.accountID + "");
			model.addAttribute("notification", list_notification);
			model.addAttribute("noti_unread", extension.getNotificationUnRead(ProfileDAO.accountID + ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Check role
		return "admin/pages/sales/orders";
	}

	// show các đơn hàng -> Đang giao hàng
	@RequestMapping(value = "delivery")
	public String showListDelivery(ModelMap model, HttpSession httpSession) {
		Session session = factory.openSession();
		try {
			model.addAttribute("list_orders", orderHelper.getListByStatus(ID_Delivery, ""));

			// Phân trang
			String SQL_count = "SELECT count(ID) FROM Orders Where Status = '" + ID_Delivery + "'";
			Query Query_count = session.createQuery(SQL_count);
			List<Orders> List_count = Query_count.list();
			model.addAttribute("countorders", List_count);

			//
			list_notification = extension.getListNotification(extension.default_notification,
					ProfileDAO.accountID + "");
			model.addAttribute("notification", list_notification);
			model.addAttribute("noti_unread", extension.getNotificationUnRead(ProfileDAO.accountID + ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Check role
		return "admin/pages/sales/orders";
	}

	// show các đơn hàng -> Đã hoàn thành
	@RequestMapping(value = "completed")
	public String showListCompleted(ModelMap model, HttpSession httpSession) {
		Session session = factory.openSession();
		try {
			model.addAttribute("list_orders", orderHelper.getListByStatus(ID_Completed, ""));

			// Phân trang
			String SQL_count = "SELECT count(ID) FROM Orders Where Status = '" + ID_Completed + "'";
			Query Query_count = session.createQuery(SQL_count);
			List<Orders> List_count = Query_count.list();
			model.addAttribute("countorders", List_count);

			//
			list_notification = extension.getListNotification(extension.default_notification,
					ProfileDAO.accountID + "");
			model.addAttribute("notification", list_notification);
			model.addAttribute("noti_unread", extension.getNotificationUnRead(ProfileDAO.accountID + ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Check role
		return "admin/pages/sales/orders";
	}

	// show các đơn hàng -> Đã hủy
	@RequestMapping(value = "cancel")
	public String showListCancel(ModelMap model, HttpSession httpSession) {
		Session session = factory.openSession();
		try {
			model.addAttribute("list_orders", orderHelper.getListByStatus(ID_Cancel, ""));

			// Phân trang
			String SQL_count = "SELECT count(ID) FROM Orders Where Status = '" + ID_Cancel + "'";
			Query Query_count = session.createQuery(SQL_count);
			List<Orders> List_count = Query_count.list();
			model.addAttribute("countorders", List_count);

			//
			list_notification = extension.getListNotification(extension.default_notification,
					ProfileDAO.accountID + "");
			model.addAttribute("notification", list_notification);
			model.addAttribute("noti_unread", extension.getNotificationUnRead(ProfileDAO.accountID + ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Check role
		return "admin/pages/sales/orders";
	}

	// show trang đơn hàng chi tiết
	@RequestMapping(value = "detail-id={ID}")
	public String showListDetaiOrder(ModelMap model, @PathVariable("ID") int iDOrders, HttpServletRequest request,
			HttpSession httpSession) {
		Session session = factory.openSession();
		String status = request.getParameter("status");
		double total = 0;
		try {
			// load đơn hàng chi tiết
			String SQL_ALL_DETAILORDERS = "FROM DetailOrder Where IDOrders = " + iDOrders + " Order by ID";
			Query All_DetailOrders = session.createQuery(SQL_ALL_DETAILORDERS);
			List<DetailOrder> list_all_detailorders = All_DetailOrders.list();						
			
			for (int i = 0; i < list_all_detailorders.size(); i++) {
				total += list_all_detailorders.get(i).getTotalMoney();
			}
			
			model.addAttribute("list_detailorders", list_all_detailorders);
			httpSession.setAttribute("listProduct_detailOrders", list_all_detailorders);

			model.addAttribute("list_orders_by_id", orderHelper.getListByStatus("", iDOrders + ""));		

			// load danh mục dropdownlist
			String sql_category_channel = "From Category Where Type = " + 5
					+ " and Code = '' and (Status = '1' OR Status = '2') order by Sort ";
			Query query_category_channel = session.createQuery(sql_category_channel);
			List<Category> list_category_channel = query_category_channel.list();
			model.addAttribute("category_channel", list_category_channel);

			//
			model.addAttribute("total", total);
			model.addAttribute("page", "orders");
			model.addAttribute("breadcrumb", "Danh sách đơn hàng");

			//
			list_notification = extension.getListNotification(extension.default_notification,
					ProfileDAO.accountID + "");
			model.addAttribute("notification", list_notification);
			model.addAttribute("noti_unread", extension.getNotificationUnRead(ProfileDAO.accountID + ""));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Check role
		return "admin/pages/sales/orders-detail";
	}

	@RequestMapping(value = "update", params = "btnUpdate", method = RequestMethod.POST)
	public String update(ModelMap model, HttpServletRequest request) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		//
		Orders orders;
		String iD = request.getParameter("IDOrders");
		System.out.println("ID: " + iD);
		orders = (Orders) session.get(Orders.class, Integer.parseInt(iD));
		//
		try {
			orders.setCode(request.getParameter("code_orders"));
			orders.setDate(request.getParameter("datetime"));
			orders.setSales(request.getParameter("sales"));
			orders.setName(request.getParameter("name_customer"));
			orders.setPhone(request.getParameter("phone"));
			orders.setAddress(request.getParameter("address"));
			orders.setEmail(request.getParameter("email"));
			orders.setNote(request.getParameter("note"));
			orders.setChannel(request.getParameter("channel"));
			orders.setStatus(request.getParameter("status"));					

			//
			session.update(orders);
			t.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/orders/detail-id=" + iD;
	}

	@RequestMapping(value = "update", params = "btnCancel", method = RequestMethod.POST)
	public String cancel(ModelMap model, HttpServletRequest request, HttpSession httpSession) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		//
		Product product;
		Orders orders;
		//
		String iD = request.getParameter("IDOrders");
		System.out.println("IDOrders: " + iD);
		orders = (Orders) session.get(Orders.class, Integer.parseInt(iD));
		//
		List<DetailOrder> listProduct = (List<DetailOrder>) httpSession.getAttribute("listProduct_detailOrders");
		//
		try {
			// Cập nhật trạng thái đơn
			orders.setCode(request.getParameter("code_orders"));
			orders.setDate(request.getParameter("datetime"));
			orders.setSales(request.getParameter("sales"));
			orders.setName(request.getParameter("name_customer"));
			orders.setPhone(request.getParameter("phone"));
			orders.setAddress(request.getParameter("address"));
			orders.setEmail(request.getParameter("email"));
			orders.setNote(request.getParameter("note"));
			orders.setChannel(request.getParameter("channel"));
			orders.setStatus(ID_Cancel + "");
			session.update(orders);

			// Cập nhật lại số lượng sản phẩm trong Kho
			for (int i = 0; i < listProduct.size(); i++) {
				// Khởi tạo sản phẩm sẽ update
				product = (Product) session.get(Product.class, listProduct.get(i).getIDProduct());
				// Lấy số lượng đang có trong Kho
				int storage = extension.getProductInStorageByID(listProduct.get(i).getIDProduct());
				// Cập nhật lại số lượng sp trong Kho
				product.setStorage(storage + listProduct.get(i).getQuantity());
			}
			t.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/orders/detail-id=" + iD;
	}
}
