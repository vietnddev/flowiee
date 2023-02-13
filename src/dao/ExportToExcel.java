package dao;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.DocShare2;
import model.Log;

public class ExportToExcel {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private String datetime;

	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_hhmmss");
		LocalDateTime now = LocalDateTime.now();
		datetime = dtf.format(now);
	}

	private void WriteHeader(HttpServletResponse response, String[] header, String fileName, String sheetName) {
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, headerValue);

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet(sheetName);

		Row row = sheet.createRow(0);

		for (int i = 0; i < header.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(header[i]);
		}
	}

	private void WriteDataLog(List<Log> list) {
		for (int i = 0; i < list.size(); i++) {
			Row row = sheet.createRow(i + 1);

			Cell id = row.createCell(0);
			id.setCellValue(list.get(i).getID());

			Cell user = row.createCell(1);
			user.setCellValue(list.get(i).getUsers());

			Cell action = row.createCell(2);
			action.setCellValue(list.get(i).getAction());

			Cell url = row.createCell(3);
			url.setCellValue(list.get(i).getUrl());

			Cell created = row.createCell(4);
			created.setCellValue(list.get(i).getCreated());

			Cell ip = row.createCell(5);
			ip.setCellValue(list.get(i).getIP());
		}
	}
	
	private void WriteDataDocShare2(List<DocShare2> list) {
		for (int i = 0; i < list.size(); i++) {
			Row row = sheet.createRow(i + 1);

			Cell id = row.createCell(0);
			id.setCellValue(list.get(i).getID());

			Cell iduser = row.createCell(1);
			iduser.setCellValue(list.get(i).getIDUser());

			Cell iddoc = row.createCell(2);
			iddoc.setCellValue(list.get(i).getIDDoc());
		}
	}
	
	private void BuildFile(HttpServletResponse response) {
		try {
			ServletOutputStream fos = response.getOutputStream();
			workbook.write(fos);
			fos.close();
		} catch (Exception e) {
			System.out.println(e.getCause());
		}

	}

	public void ExportLog(HttpServletResponse response, List<Log> list, String[] header, String fileName, String sheetName) {
		try {
			WriteHeader(response, header, fileName, sheetName);
			WriteDataLog(list);			
			BuildFile(response);
		} catch (Exception e) {
			System.out.println(e.getCause());
		}		
	}
	
	public void ExportDocShare2(HttpServletResponse response, List<DocShare2> list, String[] header, String fileName, String sheetName) {
		try {
			WriteHeader(response, header, fileName, sheetName);
			WriteDataDocShare2(list);			
			BuildFile(response);
		}
		catch (Exception e) {
			System.out.println(e.getCause());
		}		
	}
}
