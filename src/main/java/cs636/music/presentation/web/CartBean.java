package cs636.music.presentation.web;

import java.util.Set;
import java.util.HashSet;

public class CartBean {

	private Set<CartItemBean> items;
	
	public CartBean() {
		items = new HashSet<>();
	}
	
	public void setCart(Set<CartItemBean> items) {
		this.items = items;
	}
	
	public Set<CartItemBean> getCart() {
		return items;
	}
	
}
