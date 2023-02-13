package controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import dao.Extension;
import dao.ProfileDAO;
import model.Account;
import model.Category;
import model.Customer;
import model.DetailOrder;
import model.Image;
import model.Notification;
import model.Orders;
import model.Product;
import model.ProductDTO;

@Controller
public class admin_CartController {
	@Autowired
	private SessionFactory factory;

	Extension extension = new Extension();
	ArrayList<Notification> list_notification = null;

	// Thêm vào giỏi hàng
	@RequestMapping(value = "/product/addToCart", method = RequestMethod.POST)
	public String addCartDetailProduct(ModelMap model, @RequestParam("id") int id, HttpSession httpSession) {
		Session session = factory.openSession();
		@SuppressWarnings("unchecked")
		List<ProductDTO> list = (List<ProductDTO>) httpSession.getAttribute("cart");
		Product product = (model.Product) session.get(Product.class, id);
		ProductDTO productDTO;
		try {
			if (list == null) {
				list = new ArrayList<ProductDTO>();
				productDTO = new ProductDTO(product);
				list.add(productDTO);
			} else {
				boolean kt = true;
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getProduct().getID() - product.getID() == 0) {
						productDTO = list.get(i);
						productDTO.setSoLuong(productDTO.getSoLuong() + 1);
						list.set(i, productDTO);
						kt = false;
						break;
					}
				}
				if (kt == true) {
					productDTO = new ProductDTO(product);
					list.add(productDTO);
					for (int i = 0; i < list.size(); i++) {
					}
				}
			}
			httpSession.setAttribute("sizecart", list.size());
			httpSession.setAttribute("cart", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/product/home";
	}

	// Show trang giỏ hàng
	@RequestMapping("/sales/checkout")
	public String showBill(HttpSession ss, ModelMap model, HttpSession httpSession) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			List<ProductDTO> list = (List<ProductDTO>) ss.getAttribute("cart");
			double tottal = 0;
			if (list == null) {
				model.addAttribute("message", "Bạn chưa thêm sản phẩm vào giỏ hàng!");
			} else {
				for (int i = 0; i < list.size(); i++) {
					int soluong = list.get(i).getSoLuong();
					double giagoc = list.get(i).getProduct().getPrice();
					int promotion = list.get(i).getProduct().getPromotion();
					//
					tottal += soluong * (giagoc * (100 - promotion) / 100);
				}
				httpSession.setAttribute("total", tottal);
			}
			// load danh mục dropdownlist channel
			String sql_category_channel = "From Category Where Type = " + 5
					+ " and Code = '' and (Status = '1' OR Status = '2') order by Sort ";
			Query query_category_channel = session.createQuery(sql_category_channel);
			List<Category> list_category_channel = query_category_channel.list();
			model.addAttribute("category_channel", list_category_channel);
			//
			list_notification = extension.getListNotification(extension.default_notification,
					ProfileDAO.accountID + "");
			model.addAttribute("notification", list_notification);
			model.addAttribute("noti_unread",
					extension.getNotificationUnRead(ProfileDAO.accountID + ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "admin/pages/sales/cart";
	}

	// Xóa sản phẩm ra khỏi giỏ hàng
	@RequestMapping("removeCart-id={ID}")
	public String removeCart(HttpSession ss, @PathVariable("ID") int id) {
		List<ProductDTO> list = (List<ProductDTO>) ss.getAttribute("cart");
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getProduct().getID() == id) {
				list.remove(i);
			}
		}
		ss.setAttribute("sizecart", list.size());
		ss.setAttribute("cart", list);
		return "redirect:/sales/checkout";
	}

	// Cập nhật giỏ hàng
	@RequestMapping(value = "updateCart", method = RequestMethod.POST)
	public String editCart(HttpSession httpSession, HttpServletRequest request) {
		@SuppressWarnings("unchecked")
		List<ProductDTO> list = (List<ProductDTO>) httpSession.getAttribute("cart");
		//
		String action = request.getParameter("action");
		int id = Integer.parseInt(request.getParameter("id"));
		if (action.equals("+")) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getProduct().getID() == id) {
					ProductDTO pd = list.get(i);
					pd.setSoLuong(list.get(i).getSoLuong() + 1);
					list.set(i, pd);
				}
			}
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getProduct().getID() == id && list.get(i).getSoLuong() > 1) {
					ProductDTO pd = list.get(i);

					if (list.get(i).getSoLuong() > 0) {
						pd.setSoLuong(list.get(i).getSoLuong() - 1);
					}
					list.set(i, pd);
					if (list.get(i).getSoLuong() == 0) {
						list.remove(i);
					}
				}
			}
		}
		httpSession.setAttribute("sizecart", list.size());
		httpSession.setAttribute("cart", list);
		return "redirect:/admin/checkout";
	}

	// Load thông tin khách hàng lên
	@RequestMapping(value = "/sales/order", params = "search", method = RequestMethod.POST)
	public String getAllProducts(ModelMap model, HttpServletRequest request, HttpSession httpSession) {
		// model.addAttribute("orders");
		//
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		//
		try {
			// load danh sách khách hàng
			String sql_customer = "From Customer Where Phone = " + Integer.parseInt(request.getParameter("phone"));
			Query query_customer = session.createQuery(sql_customer);
			Customer customer = (Customer) query_customer.uniqueResult();
			model.addAttribute("ID", customer.getID());
			model.addAttribute("phone", customer.getPhone());
			model.addAttribute("customername", customer.getName());
			model.addAttribute("email", customer.getEmail());
			model.addAttribute("address", customer.getAddress());
			// load danh mục dropdownlist
			String sql_category_channel = "From Category Where Type = " + 5
					+ " and Code = '' and (Status = '1' OR Status = '2') order by Sort ";
			Query query_category_channel = session.createQuery(sql_category_channel);
			List<Category> list_category_channel = query_category_channel.list();
			model.addAttribute("category_channel", list_category_channel);
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
		return "admin/pages/sales/cart";
	}

	// insert Cart xuống DB
	@RequestMapping(value = "sales/order", params = "btnSubmit", method = RequestMethod.POST)
	public String payCart(HttpSession httpSession, ModelMap model, HttpServletRequest request) {
		//
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		//
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		DateTimeFormatter MaDH = DateTimeFormatter.ofPattern("yyMMdd");
		LocalDateTime now = LocalDateTime.now();
		String date = dateTimeFormatter.format(now);
		//
		@SuppressWarnings("unchecked")
		List<ProductDTO> list = (List<ProductDTO>) httpSession.getAttribute("cart");

		// Get thông tin đơn hàng
		String code = MaDH.format(now);
		String IDCustomer = request.getParameter("IDCustomer");
		if (IDCustomer.equals("")) {
			IDCustomer = "0";
		} else {
			IDCustomer = request.getParameter("IDCustomer");
		}
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		String address = request.getParameter("address");
		String note = request.getParameter("note");
		String channel = request.getParameter("channel");

		// Thêm 1 record xuống DB Orders
		Orders orders = new Orders(code, Integer.parseInt(IDCustomer), name, phone, email, address, note, date,
				Double.parseDouble(request.getParameter("totalMoney")), request.getParameter("sales"), channel, "1");
		session.save(orders);
		orders.setCode(orders.getCode() + orders.getID()); // Lưu mã đơn

		// Thêm đơn chi tiết xuống DB
		try {
			for (int i = 0; i < list.size(); i++) {
				double unitPrice = list.get(i).getProduct().getPrice() * (100 - list.get(i).getProduct().getPromotion())
						/ 100;
				double totalMoney = unitPrice * list.get(i).getSoLuong();
				DetailOrder detailOrder = new DetailOrder(orders.getID(), list.get(i).getProduct().getID(),
						list.get(i).getProduct().getName(), unitPrice, list.get(i).getSoLuong(), totalMoney,
						"Khuyến mãi " + list.get(i).getProduct().getPromotion() + " %", true);
				session.save(detailOrder);

				// - số lượng sản phẩm trong Kho và số lượng đã bán
				Product product = (Product) session.get(Product.class, list.get(i).getProduct().getID());
				product.setStorage(list.get(i).getProduct().getStorage() - list.get(i).getSoLuong());
				product.setQuantity(list.get(i).getProduct().getQuantity() + list.get(i).getSoLuong());
				session.update(product);
			}

			// Gửi thông báo chuông
			String noti_Sender = "1";
			Notification notification = new Notification(date, "0", noti_Sender, false, true, orders.getCode(), 1,
					orders.getID());
			session.save(notification);

			//
			t.commit();
		} catch (Exception e) {
			t.rollback();
			model.addAttribute("message", "Thất bại!");
		}
		list.removeAll(list);
		httpSession.setAttribute("sizecart", list.size());
		httpSession.setAttribute("cart", list);
		httpSession.removeAttribute("total");
		httpSession.removeAttribute("isAddToCart");
		return "redirect:/sales/checkout";
	}
}
