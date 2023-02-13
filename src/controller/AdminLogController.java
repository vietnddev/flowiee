package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Transaction;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.jni.File;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateQueryException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import dao.ExportToExcel;
import dao.Extension;
import dao.LogDAO;
import dao.ProfileDAO;
import dao.RoleDAO;
import model.Account;
import model.DocShare2;
import model.Log;
import model.Notification;
import model.Orders;

@RequestMapping("/admin/")
@Transactional
@Controller
public class AdminLogController {
	@Autowired
	SessionFactory factory;

	Extension extension = new Extension();
	ArrayList<Notification> list_notification = null;

	RoleDAO checkrole = new RoleDAO();

	LogDAO logHelper = new LogDAO();

	List<Log> list_log;

	private String datetime;

	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_hhmmss");
		LocalDateTime now = LocalDateTime.now();
		datetime = dtf.format(now);
	}

	// index (New) - ok
	@RequestMapping(value = "log")
	public String getLog(ModelMap model, HttpServletRequest request, HttpSession httpSession) {
		try {
			Session session = factory.openSession();
			// Truy vấn SQL
			Query query_log = session.createQuery("From Log Order by ID DESC");

			// Đổ kết quả truy vấn được vào list
			list_log = query_log.list();
			model.addAttribute("Log", list_log);

			// Phân trang
			String SQL_count = "SELECT count(ID) FROM Log";
			Query Query_count = session.createQuery(SQL_count);
			List<Log> List_count = Query_count.list();
			model.addAttribute("countlog", List_count);
			//
			list_notification = extension.getListNotification(extension.default_notification,
					ProfileDAO.accountID + "");
			model.addAttribute("notification", list_notification);
			model.addAttribute("noti_unread", extension.getNotificationUnRead(ProfileDAO.accountID + ""));
		} catch (HibernateQueryException e) {
			e.printStackTrace();
		}
		// Check role
//		if (checkrole.checkRole(Integer.parseInt(ProfileDAO.accountID + ""), checkrole.ID_ML) > 0) {
//			return "admin/pages/admin/log";
//		} else {
//			return "admin/error/not-auth";
//		}
		return "admin/pages/admin/log";
	}

	@RequestMapping(value = "log/export", method = RequestMethod.GET)
	public void exportToExcel(HttpServletResponse response) {		
		String fileName = "SystemLog_" + datetime +  ".xlsx";
		String sheetName = "SystemLog_" + datetime;
		String[] header = {"ID", "Tài khoản truy cập", "Hoạt động", "URL", "Thời gian truy cập", "IP"};
		
		ExportToExcel export = new ExportToExcel();
		export.ExportLog(response, list_log, header, fileName, sheetName);	
	}
}
