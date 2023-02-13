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
import javax.servlet.http.HttpServletResponse;
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
import dao.QuerySQL;
import dao.RoleDAO;
import model.*;

@Transactional
@Controller
public class AdminQuerySQLController {
	@Autowired
	SessionFactory factory;

	String resultSQL = "";
	
	@RequestMapping(value = "/admin/executesql")
	public String homeQuerySQL(ModelMap model, HttpServletRequest request) {
		//
		model.addAttribute("result", resultSQL);
		
		QuerySQL querySQL = new QuerySQL();
		//querySQL.readTxtFromFile();
		model.addAttribute("query", querySQL.readFile() + "");		
		//
		return "admin/pages/admin/executeSQL";
	}

	@RequestMapping(value = "/admin/executesql", params = "executesql")
	public String ExecuteSQL(ModelMap model, HttpServletRequest request) {
		try {
			String query = request.getParameter("query");
			QuerySQL querySQL = new QuerySQL();
			resultSQL = querySQL.excute(query);

			// model.addAttribute("result", query);
		} catch (Exception e) {
			e.printStackTrace();
			// model.addAttribute("result", e.getCause());
		}
		return "redirect:/admin/executesql";
	}

}
