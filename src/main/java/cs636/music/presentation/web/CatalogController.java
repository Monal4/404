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
//	@Autowired 
//	private SalesService salesService;
	
	// String constants for URLs
	private static final String WELCOME_URL = "/welcome.html";
	private static final String WELCOME_VIEW = "/welcome";
	

	@RequestMapping(WELCOME_URL)
	public String handleWelcome() {
		return WELCOME_VIEW;
	}

	@RequestMapping("/download")
	public String showProduct(Model model,@RequestParam(value = "productCode", required=false)
	String productCode, @RequestParam(value = "Quantity", required=false)
	Integer Quantity,HttpServletRequest request) throws ServletException
	{
		String url = null;
		if (productCode.equals("pf01")) {
			productCode = "pf01";
			model.addAttribute("productCode", productCode);
			model.addAttribute("title", "Whiskey Before Breakfast");
			model.addAttribute("src", "/sound/pf01/whiskey.mp3");
			model.addAttribute("title1", "64 corvair");
			model.addAttribute("src1", "/sound/pf01/corvair.mp3");
			url = "/sound/universal"; 
			
		} else if(productCode.equals("8601")) {
			model.addAttribute("productCode", productCode);
			model.addAttribute("title", "You Are a Star");
			model.addAttribute("src",
					"/sound/8601/star.mp3");
			
			model.addAttribute("title1", "Don't Make No Difference");
			model.addAttribute("src1",
					"/sound/8601/no_difference.mp3");
			url = "/sound/universal"; 
		} else if(productCode.equals("jr01")) {
			model.addAttribute("productCode", productCode);
			model.addAttribute("title", "Filter");
			model.addAttribute("src",
					"/sound/jr01/filter.mp3");
			
			model.addAttribute("title1", "So Long Lazy Ray");
			model.addAttribute("src1",
					"/sound/jr01/so_long.mp3");
			url = "/sound/universal"; 
		} else if(productCode.equals("pf02")) {
			model.addAttribute("productCode", productCode);
			model.addAttribute("title", "Neon Lights");
			model.addAttribute("src",
					"/sound/pf02/neon.mp3");
			
			model.addAttribute("title1", "Tank Hill");
			model.addAttribute("src1",
					"/sound/pf02/tank.mp3");
			url = "/sound/universal"; 
		} 
		model.addAttribute("productCode", productCode);
	//	model.addAttribute("", attributeValue)
		//model.addAttribute("NoofProducts", MusicSystemConfig.Max_Quantity);
		
		return url;
	}

	@RequestMapping("cart")
	public String cart(Model model,@RequestParam(value="productCode",required = false) 
	String productCode, @RequestParam(value="productQuantity",required = false) Integer productQuantity
			,HttpServletRequest request) throws ServletException {
		
		//CartItemData cart = new CartItemData();
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
		model.addAttribute("productCode", productCode);
		model.addAttribute("productQuantity", productQuantity);
	//	Set<CartItemData> setofcartdata = new HashSet<CartItemData>();
		
		try {
			//model.addAttribute("productQuantity", productQuantity);
			
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
		
	//	model.addAttribute("allProducts", setofcartdata);
		
		return "cart";
	}
	
	@RequestMapping("cart.html")
	public String showcart(Model model,HttpServletRequest request) throws ServletException {
		
		Set<CartItemData> setofcartdata = new HashSet<CartItemData>();
		ArrayList<Long> id = new ArrayList<>();
		ArrayList<Integer> quantity = new ArrayList<>();
		
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		try {
			setofcartdata = catalogService.getCartInfo(cart);
			for(CartItemData a : setofcartdata) {
				 id.add(a.getProductId());
				quantity.add(a.getQuantity());
			}
			
			model.addAttribute("productId", id);
			model.addAttribute("productQuantity", quantity);
		}catch(Exception e) {
			throw new ServletException(e);
		}	
		//model.addAttribute("allProducts", setofcartdata);
		
	//	model.addAttribute("productId")
		
		return "cart";
	}
	
	private boolean checkCart(HttpServletRequest request) throws IOException {
		 HttpSession session = request.getSession();
			Cart cart = (Cart) session.getAttribute("cart");
			return (cart != null);
		}
}

