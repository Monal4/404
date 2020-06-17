package cs636.music.presentation.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import cs636.music.service.*;
import cs636.music.service.data.UserData;


@Controller
@SessionAttributes({"user","cart"})
public class SalesController {
	
	@Autowired
	private SalesService salesService;

	private static final String USER_WELCOME_URL = "/userWelcome.html";
	
	@RequestMapping(USER_WELCOME_URL)
	public String welcomeuser(HttpServletRequest request) throws ServletException{
		if(request.getSession().getAttribute("user") != null) {
			return "catalog";
		}
		return "userWelcome";
	}

	@RequestMapping("Registered.html")
	public String displayWelcome(Model model,@RequestParam(value = "email", required = false) String email,
			@RequestParam(value="firstName" , required=false) String firstname, @RequestParam(value = 
			"lastName", required = false) String lastname,
			 @RequestParam(value ="address", required = false) String address,HttpServletRequest request) throws ServletException{
		
		System.out.println("Starting User registration:");
		
		//String forwardURL;
		
		if(request.getSession().getAttribute("user") != null) {
			return "catalog";
		}
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if(user == null) {
			user = new UserBean();
			user.setEmail(email);
		}
		request.getSession().setAttribute("user", user);
	
			System.out.print("The inserted values are:-"+ email + firstname + lastname );
			try {
				salesService.registerUser(firstname, lastname, email);
			}catch(Exception e){
				System.out.println(e);
				throw new ServletException("Cannot insert user into Database:-");
			}
			
			try {
			if(salesService.userIsCustomer(email)) {
				System.out.println("address:");
				salesService.getUserInfoByEmail(email);
			}else {
				model.addAttribute("email", email);
				return "address";
			}
			}catch(Exception e) {
				throw new ServletException("address update problem"+e);
			}
			
			return "catalog";
		}
	
	@RequestMapping("addAddress.html")
	public String addAddress(Model model,@RequestParam(value = "email" , required = false) 
	String email,@RequestParam(value ="address" , required = false) String address, HttpServletRequest request) throws ServletException {
		
		UserData user = new UserData();
		
		try {
			user = salesService.getUserInfoByEmail(email);
		}catch(Exception e){
			throw new ServletException("Cannot get info from email");
		}
		long id = user.getId();
		try {
			salesService.addUserAddress(id, address);
		}catch(Exception e){
			throw new ServletException("Cannot update address");
		}
		
		return "catalog";
	}
	
	@RequestMapping("address.html")
	public String addressForm(){
		return "address";
	}
	
	@RequestMapping("catalog.html")
	public String showCatalog(HttpServletRequest request){
		return "catalog";
	}
	
}

