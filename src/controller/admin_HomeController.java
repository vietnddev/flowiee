package controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.channels.GatheringByteChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
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
import model.*;

@Transactional
@Controller
public class admin_HomeController {
	private Path path;

	@Autowired
	SessionFactory factory;

	Extension extension = new Extension();
	ArrayList<Notification> list_notification = null;

	@RequestMapping(value = "")
	public String home() {
		
		return "admin/home";
	}
	
	// Vào trang chủ
	@RequestMapping(value = "/index")
	public String admin_home(ModelMap model, HttpSession httpSession, HttpServletRequest request) {
		Session session = factory.openSession();
		// Xử lý chuông thông báo
		list_notification = extension.getListNotification(extension.default_notification, ProfileDAO.accountID + "");
		model.addAttribute("notification", list_notification);
		model.addAttribute("noti_unread", extension.getNotificationUnRead(ProfileDAO.accountID + ""));

		// Xử lý biểu đồ trạng thái đơn hàng
		Query Query_Orders_UnConfirm = session.createQuery("SELECT count(ID) FROM Orders where Status = 1");
		Query Query_Orders_Delivery = session.createQuery("SELECT count(ID) FROM Orders where Status = 2");
		Query Query_Orders_Completed = session.createQuery("SELECT count(ID) FROM Orders where Status = 3");
		Query Query_Orders_Cancel = session.createQuery("SELECT count(ID) FROM Orders where Status = 4");

		model.addAttribute("Pie_UnConfirm", Query_Orders_UnConfirm.uniqueResult());
		model.addAttribute("Pie_Delivery", Query_Orders_Delivery.uniqueResult());
		model.addAttribute("Pie_Completed", Query_Orders_Completed.uniqueResult());
		model.addAttribute("Pie_Cancel", Query_Orders_Cancel.uniqueResult());

		// Xử lý biểu đồ trồn theo Channel
		model.addAttribute("Pie_Shopee", extension.getChartChanel("Shopee"));
		model.addAttribute("Pie_Facebook", extension.getChartChanel("Facebook"));
		model.addAttribute("Pie_Instagram", extension.getChartChanel("Instagram"));
		model.addAttribute("Pie_Hotline", extension.getChartChanel("Hotline"));
		model.addAttribute("Pie_Zalo", extension.getChartChanel("Zalo"));
		model.addAttribute("Pie_Other", extension.getChartChanel("Khác"));

		// Xử lý biểu đề line theo doanh thu FebruaryFebruary
		model.addAttribute("January", extension.getLineChartPrice("1"));
		model.addAttribute("February", extension.getLineChartPrice("2"));
		model.addAttribute("March", extension.getLineChartPrice("3"));
		model.addAttribute("April", extension.getLineChartPrice("4"));
		model.addAttribute("May", extension.getLineChartPrice("5"));
		model.addAttribute("June", extension.getLineChartPrice("6"));
		model.addAttribute("July", extension.getLineChartPrice("7"));
		model.addAttribute("August", extension.getLineChartPrice("8"));
		model.addAttribute("September", extension.getLineChartPrice("9"));
		model.addAttribute("October", extension.getLineChartPrice("10"));
		model.addAttribute("November", extension.getLineChartPrice("11"));
		model.addAttribute("December", extension.getLineChartPrice("12"));

		System.out.println("IDAccount: " + ProfileDAO.accountID);

		//
		return "admin/index";
	}
}
