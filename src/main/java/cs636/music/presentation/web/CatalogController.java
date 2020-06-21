package cs636.music.presentation.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
import cs636.music.domain.Cart;
import cs636.music.domain.Product;
import cs636.music.domain.Track;

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

		return "/sound/" + productCode + "/sound";
	}

	@RequestMapping("cart")
	public String cart(Model model,@RequestParam(value="productCode",required = false) 
	String productCode, @RequestParam(value="productQuantity",required = false) Integer productQuantity
			,HttpServletRequest request) throws ServletException {
		
		Cart cart = null;
		Product product = new Product();
		try {
			 product = catalogService.getProductByCode(productCode);
			 
		}catch(Exception e) {
			throw new ServletException(e);
		}
		long productId = product.getId();
		System.out.println("The product Id is:=" + productId);
		System.out.println("The product Quantity is:=" + productQuantity);
		model.addAttribute("productId", productId);
		model.addAttribute("productQuantity", productQuantity);
		
		try {
			cart = (Cart) request.getSession().getAttribute("cart");
			if (checkCart(request) == false) {
				cart = catalogService.createCart();
			}
			catalogService.addItemtoCart(productId, cart, productQuantity);
			if(checkCart(request) == false) {
			request.getSession().setAttribute("cart", cart);
			}
		}catch(Exception e) {
			throw new ServletException(e);
		}	
		
		return "cart";
	}
	
	@RequestMapping("cart.html")
	public String showcart(Model model,HttpServletRequest request) throws ServletException {
		
		Set<CartItemData> setofcartdata = new HashSet<CartItemData>();
		ArrayList<Long> id = new ArrayList<>();
		ArrayList<Integer> quantity = new ArrayList<>();
	
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		try {
			if(cart == null) {
				cart = catalogService.createCart();
			}
			setofcartdata = catalogService.getCartInfo(cart);
			for(CartItemData a : setofcartdata) {
				 id.add(a.getProductId());
				quantity.add(a.getQuantity());
			}
			
			request.getSession().setAttribute("cart", cart);
			model.addAttribute("productId", id);
			model.addAttribute("productQuantity", quantity);
		}catch(Exception e) {
			throw new ServletException(e);
		}	
		
		return "cart";
	}
	
	private boolean checkCart(HttpServletRequest request) throws IOException {
		 HttpSession session = request.getSession();
			Cart cart = (Cart) session.getAttribute("cart");
			return (cart != null);
		}
}

