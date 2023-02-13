package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dao.Extension;
import dao.StgDocDAO;

@RequestMapping("/admin/")
@Controller
public class test {
	@RequestMapping("import")
	public void start() {
		StgDocDAO stgDocHelper = new StgDocDAO(); 
		stgDocHelper.insertABC();		
	}
}
