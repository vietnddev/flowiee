package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/task/")
public class TaskController {
	
	@RequestMapping(value = "home")
	public String homeTask() {
		//
		return "admin/pages/task/home";
	}	
}
