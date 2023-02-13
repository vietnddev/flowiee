package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/spend/")
@Controller
public class SpendController {
	@RequestMapping(value = "home")
	public String homeSpend() {
		//
		return "admin/pages/spend/home";
	}
}
