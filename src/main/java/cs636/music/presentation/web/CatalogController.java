package cs636.music.presentation.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.PathVariable;

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

		return "catalog";
	}

	@RequestMapping("cart.html")
	public String showcart(Model model,HttpServletRequest request) throws ServletException {
	
		if(request.getSession().getAttribute("user") == null) {
			return "userWelcome";
		}
		Set<CartItemData> setofcartdata = new HashSet<CartItemData>();
		
		Cart cart = null;
		try {
			cart = (Cart) request.getSession().getAttribute("cart");
			//cart.getItems().forEach(i->System.out.println(i.getProductId()));
			setofcartdata = catalogService.getCartInfo(cart);
		}catch(Exception e) {
			System.out.println("Exception in setofcartdata" + e);
		}	
		model.addAttribute("products", setofcartdata);

		BigDecimal total = GetSubTotal(0,setofcartdata);
		
		model.addAttribute("Total", total);
		return "cart";
	}

	@RequestMapping("/Update/{id}")
	public String updateQuantity(HttpServletRequest request, Model model, @PathVariable("id") Integer id, 
	@RequestParam(value="newQuantity", required=false) Integer quantity) throws ServletException{

		System.out.println(quantity);
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		Set<CartItemData> set = new HashSet<>();

		catalogService.changeCart(id, cart, quantity);

		try{
			set = catalogService.getCartInfo(cart);
		}catch(Exception e) {
			System.out.println("Exception in updating cart: " + e);
		}

		model.addAttribute("products", set);

		BigDecimal total = GetSubTotal(0,set);
		
		model.addAttribute("Total", total);

		return "cart";
	}

	public static BigDecimal GetSubTotal(int init, Set<CartItemData> set) {
		BigDecimal total = new BigDecimal(init);

		if(set.size()!=0) {
			for(CartItemData item : set) {
				BigDecimal newquantity = new BigDecimal(item.getQuantity());
				total = total.add(item.getPrice().multiply(newquantity));
			}
		}
		System.out.println(total);
		return total;
	}

	@RequestMapping("/checkout")
	public String checkout(HttpServletRequest request, Model model) throws ServletException{
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		Set<CartItemData> allproducts = new HashSet<>();

		try{
			allproducts = catalogService.getCartInfo(cart);
		}catch(Exception e) {
			System.out.println("Exception in updating cart: " + e);
		}

		System.out.println("Checkout initiated" + cart.getItems().size());
		
		if(cart.getItems().size() == 0) return "catalog";
		model.addAttribute("Products", allproducts);
		
		BigDecimal total = GetSubTotal(0,allproducts);
		model.addAttribute("total", total);

		return "checkout";
	}

	@RequestMapping("/remove/{ID}")
	public String removeCartItem(HttpServletRequest request, Model model, @PathVariable("ID") Integer Id) throws ServletException {
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		catalogService.removeCartItem(Id, cart);

		Set<CartItemData> setofcartdata = new HashSet<CartItemData>();

		try {
			setofcartdata = catalogService.getCartInfo(cart);
		}catch(Exception e) {
			System.out.println("Exception in setofcartdata" + e);
		}	
		model.addAttribute("products", setofcartdata);
		
		BigDecimal total = GetSubTotal(0,setofcartdata);
		model.addAttribute("Total", total);

		return "cart";
	}
	
	private boolean checkCart(HttpServletRequest request) throws IOException {
		 HttpSession session = request.getSession();
			Cart cart = (Cart) session.getAttribute("cart");
			return (cart != null);
		}
}

