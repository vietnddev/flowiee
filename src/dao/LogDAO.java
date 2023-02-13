package dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.Category;
import model.Log;

public class LogDAO extends ConnectDB {
	//
	public List<Category> getCategoryByID(int Type) {
		/*
		 * Get danh mục từ Category theo ID truy�?n vào
		 */
		List<Category> list = new ArrayList<Category>();
		String sql = "SELECT * From Category Where Type = " + Type + " and Code = '' and Status = '1' order by Sort";
		//
		try {
			PreparedStatement pstm = con.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				Category c = new Category();
				c.setID(rs.getInt("ID"));
				c.setName(rs.getString("Name"));
				list.add(c);
				System.out.println("Name fieldtype: " + c.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public void exportToExcel(HttpServletResponse response, List<Log> list_log) throws IOException {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_hhmmss");
		LocalDateTime now = LocalDateTime.now();

		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=log.xlsx";
		response.setHeader(headerKey, headerValue);

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("SystemLog_" + dtf.format(now));

		Row header = sheet.createRow(0);
		//
		Cell IDLog = header.createCell(0);
		IDLog.setCellValue("ID Log");
		//
		Cell user = header.createCell(1);
		user.setCellValue("Tài khoản truy cập");
		//
		Cell action = header.createCell(2);
		action.setCellValue("Hoạt động");
		//
		Cell url = header.createCell(3);
		url.setCellValue("Link");
		//
		Cell created = header.createCell(4);
		created.setCellValue("Th�?i gian truy cập");
		//
		Cell ip = header.createCell(5);
		ip.setCellValue("�?ịa chỉ IP");

		for (int i = 0; i < list_log.size(); i++) {
			Row row = sheet.createRow(i + 1);

			Cell cellID = row.createCell(0);
			cellID.setCellValue(list_log.get(i).getID());

			Cell cellUser = row.createCell(1);
			cellUser.setCellValue(list_log.get(i).getUsers());

			Cell cellAction = row.createCell(2);
			cellAction.setCellValue(list_log.get(i).getAction());

			Cell cellURL = row.createCell(3);
			cellURL.setCellValue(list_log.get(i).getUrl());

			Cell cellCreated = row.createCell(4);
			cellCreated.setCellValue(list_log.get(i).getCreated());

			Cell cellIP = row.createCell(5);
			cellIP.setCellValue(list_log.get(i).getIP());
		}

		ServletOutputStream fos = response.getOutputStream();
		workbook.write(fos);
		fos.close();
	}
}
