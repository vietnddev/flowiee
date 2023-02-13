package controller;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import dao.Extension;
import dao.ProfileDAO;
import dao.SalesReportDAO;
import dao.StgDocDAO;
import model.DocType;
import model.Notification;

@Controller
public class ReportController {
	@Autowired
	SessionFactory factory;

	Extension extension = new Extension();
	ArrayList<Notification> list_notification = null;

	StgDocDAO stgDocHelper = new StgDocDAO();

	SalesReportDAO salesReport = new SalesReportDAO();

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	LocalDateTime now = LocalDateTime.now();

	@RequestMapping(value = "/sales/home")
	public String homeSale(ModelMap model) {
		/*
		 * Page báo cáo tình hình kinh doanh theo doanh thu
		 */
		Session session = factory.openSession();
		// Xử lý chuông thông báo
		list_notification = extension.getListNotification(extension.default_notification, ProfileDAO.accountID + "");
		model.addAttribute("notification", list_notification);
		model.addAttribute("noti_unread", extension.getNotificationUnRead(ProfileDAO.accountID + ""));

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

		/*
		 * Cơ cấu doanh thu từ các kênh bán hàng
		 * 
		 */
		// Dán list channel lên page
		List<String> listChannel = new ArrayList<String>();
		for (int i = 0; i < salesReport.getListChannel().size(); i++) {
			listChannel.add("'" + salesReport.getListChannel().get(i) + "'");
		}
		model.addAttribute("listChannel", listChannel);

		// Dán doanh thu theo channel lên page
		List<String> listRevenue = new ArrayList<String>();
		for (int i = 0; i < salesReport.getRevenueByChannel().size(); i++) {
			listRevenue.add("'" + salesReport.getRevenueByChannel().get(i) + "'");
		}
		model.addAttribute("listRevenue", listRevenue);

		return "admin/pages/sales/home";
	}

	@RequestMapping(value = "/sales/report-orders")
	public String reportOrders(ModelMap model) {
		/*
		 * Page báo cáo tình hình kinh doanh theo đơn hàng
		 */
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

		return "admin/pages/sales/report-orders";
	}

	@RequestMapping(value = "/storage/home")
	public String reportSizeStorage(ModelMap model) {
		Session session = factory.openSession();
		/*
		 * Thống kê dung lượng đã sử dụng
		 */
		try {
			String path = "E:\\FLOWIEE\\Source\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\flowiee\\WEB-INF\\views\\admin\\assets\\storage";

			File dir = new File(path);
			File[] children = dir.listFiles();
			double usedSize = 0;
			for (File file : children) {
				File files = new File(file.getAbsolutePath());
				usedSize += files.length();
			}

			Double totalSize = 53687091200.d / 1024 / 1024 / 1024;
			Double availableSize = totalSize - (usedSize / 1024 / 1024 / 1024);

			model.addAttribute("currentDate", dtf.format(now));
			model.addAttribute("usedSize", (usedSize / 1024 / 1024 / 1024));
			model.addAttribute("availableSize", availableSize);

			/*
			 * Thống kê chung
			 */
			// Số lượng folder
			Query queryFolder = session.createQuery("Select Count(ID) From Storage Where Type = 0");
			model.addAttribute("quantityFolder", (long) queryFolder.uniqueResult());

			// Số lượng file
			Query queryFile = session.createQuery("Select Count(ID) From Storage Where Type = 1");
			model.addAttribute("quantityFile", (long) queryFile.uniqueResult());

			// Số lượng loại tài liệu
			Query queryDocType = session.createQuery("Select Count(ID) From DocType Where Status = 1");
			model.addAttribute("quantityDocType", (long) queryDocType.uniqueResult());

			// Lấy danh sách tên loại tài liệu
			List<String> nameDocType = new ArrayList<String>();
			for (int i = 0; i < stgDocHelper.report_getListIDDocType().size(); i++) {
				nameDocType.add("'" + stgDocHelper.report_getListIDDocType().get(i).getName() + "'");
			}
			model.addAttribute("nameDocType", nameDocType);

			// Lấy số lượng doc được gán cho loại tài liệu
			List<String> usedDocType = new ArrayList<String>();
			for (int i = 0; i < stgDocHelper.report_getQuatityDocTypeUsed().size(); i++) {
				usedDocType.add("'" + stgDocHelper.report_getQuatityDocTypeUsed().get(i) + "'");
			}
			model.addAttribute("usedDocType", usedDocType);

			// Gán màu sắc lên biểu đồ
			model.addAttribute("listColor",
					stgDocHelper.report_getListColor(stgDocHelper.report_getListIDDocType().size()));
		} catch (Exception e) {
			System.out.println("Có lỗi xãy ra!");
		}

		return "admin/pages/storage/home";
	}
}
