package controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Transaction;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateQueryException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import dao.Extension;
import dao.ProfileDAO;
import model.Account;
import model.Log;
import model.Notification;
import model.Orders;
import model.Product;

@RequestMapping("/admin/")
@Transactional
@Controller
public class admin_NotificationController {
	@Autowired
	SessionFactory factory;

	Extension extension = new Extension();
	ArrayList<Notification> list_notification = null;

	// Show trang tất cả thông báo
	@RequestMapping(value = "notification")
	public String getNotification(ModelMap model, HttpServletRequest request, HttpSession httpSession) {
		Session session = factory.openSession();
		try {
			//
			list_notification = extension.getListNotification("*", ProfileDAO.accountID + "");
			model.addAttribute("notification", list_notification);
			model.addAttribute("noti_unread",
					extension.getNotificationUnRead(ProfileDAO.accountID + ""));

			// Phân trang
			Query Query_count = session.createQuery("SELECT count(ID) FROM Notification");
			List<Notification> List_count = Query_count.list();
			model.addAttribute("countnotification", List_count);
		} catch (HibernateQueryException e) {
			e.printStackTrace();
		}
		return "admin/pages/notification";
	}

	@RequestMapping(value = "read-notification", method = RequestMethod.POST)
	public String readNotification(ModelMap model, HttpServletRequest request) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		//
		String ID = request.getParameter("ID");
		String IDOrders = request.getParameter("IDOrders");
		//
		Notification notification;
		notification = (Notification) session.get(Notification.class, Integer.parseInt(ID));
		try {
			notification.setIsReaded(true);
			session.update(notification);
			t.commit();
		} catch (HibernateQueryException e) {
			e.printStackTrace();
		}
		return "redirect:/admin/orders/detail-" + IDOrders;
	}
}
