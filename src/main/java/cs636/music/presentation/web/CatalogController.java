package cs636.music.presentation.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.http.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import cs636.music.service.CatalogService;
import cs636.music.service.SalesService;
import cs636.music.service.ServiceException;
import cs636.music.service.data.CartItemData;
import cs636.music.config.MusicSystemConfig;
//cart,product mainly
import cs636.music.domain.*;

// Stub for CatalogController (rename to MusicController if no SalesController supplied)
@Controller
@SessionAttributes("cart")
public class CatalogController{

	@Autowired
	private CatalogService catalogService;

	private static final String WELCOME_URL = "/";
	//private static final String WELCOME_VIEW = "/welcome";
	

	@RequestMapping(WELCOME_URL)
	public String handleWelcome() {
		return "/catalog";
	}

	@RequestMapping("/download")
	public String showProduct(Model model,@RequestParam(value = "productCode", required=false)
	String productCode, HttpServletRequest request) throws ServletException
	{
		String url = null;
		if(request.getSession().getAttribute("user") == null) {
			return "/userWelcome";
		}
		model.addAttribute("productCode", productCode);
		return "/sound/" + productCode + "/sound";
	}
	@RequestMapping("AddToCart") 
	public String addtoCart(HttpServletRequest request, @RequestParam(value="quantity", required=false) Integer productQuantity
	, @RequestParam(value = "productCode", required=false) String productCode) throws ServletException{
		if(request.getSession().getAttribute("user") == null) {
			return "userWelcome";
		}
		Product product = null;
		Cart cart = null;

		System.out.println("The quantity is:- " + productQuantity);

		try {
			product = catalogService.getProductByCode(productCode);
		} catch (Exception e) {
			throw new ServletException("productCode not found" + e);
		}

		try {
			cart = (Cart) request.getSession().getAttribute("cart");
			if (checkCart(request) == false) {
				cart = catalogService.createCart();
				request.getSession().setAttribute("cart", cart);
			}
			
			catalogService.addItemtoCart(product.getId(), cart, productQuantity);
		}catch(Exception e) {
			throw new ServletException(e);
		}

	
		//catalogService.addItemtoCart(product.getId(), cart, productQuantity);
		// System.out.println("Item added to cart");
		

		return "catalog";
	}

	@RequestMapping("cart.html")
	public String showcart(Model model,HttpServletRequest request) throws ServletException {
	
		if(request.getSession().getAttribute("user") == null) {
			return "userWelcome";
		}
		Set<CartItemData> setofcartdata = new HashSet<CartItemData>();
		// BigDecimal bg = new BigDecimal("50.00");
		// CartItemData cv = new CartItemData(1,5,"Code","Desc",bg);
		// setofcartdata.add(cv);
		System.out.println("Test1");
		
		Cart cart = null;
		try {
			cart = (Cart) request.getSession().getAttribute("cart");
			cart.getItems().forEach(i->System.out.println(i.getProductId()));
			// if (checkCart(request) == false) {
			// 	cart = catalogService.createCart();
			// }
			setofcartdata = catalogService.getCartInfo(cart);
			// setofcartdata.forEach((item)->{
			// 	System.out.println("the code"+item.getCode());
			// 	System.out.println("description test"+item.getDescription());
			// 	System.out.println(item.getProductId());
			// });
			// if(checkCart(request) == false) {
			// 	request.getSession().setAttribute("cart", cart);
			// }
			//model.addAttribute("allProducts", setofcartdata);
		}catch(Exception e) {
			System.out.println("Exception in setofcartdata" + e);
		}	
		model.addAttribute("products", setofcartdata);
		request.setAttribute("products", setofcartdata);
		//setofcartdata.forEach(i->System.out.println("1123"+i.getProductId()));
		return "cart";
	}
	
	private boolean checkCart(HttpServletRequest request) throws IOException {
		 HttpSession session = request.getSession();
			Cart cart = (Cart) session.getAttribute("cart");
			return (cart != null);
		}
}

