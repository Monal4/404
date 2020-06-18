package cs636.music.service;

import java.sql.Connection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs636.music.dao.CatalogDbDAO;
import cs636.music.dao.DownloadDAO;
import cs636.music.dao.ProductDAO;
import cs636.music.domain.Cart;
import cs636.music.domain.CartItem;
import cs636.music.domain.Download;
import cs636.music.domain.Product;
import cs636.music.domain.Track;
import cs636.music.service.data.CartItemData;
import cs636.music.service.data.DownloadData;

/**
 * 
 * Provides product-related services to user apps 
 * 
 */
@Service
public class CatalogService {
	@Autowired
	private CatalogDbDAO catalogDbDAO;
	@Autowired
	private DownloadDAO downloadDb;
	@Autowired
	private ProductDAO prodDb;

	public Set<Product> getProductList() throws ServiceException {
		Connection connection = null;
		try {
			connection = catalogDbDAO.startTransaction();
			Set<Product> prodList = prodDb.findAllProducts(connection);
			catalogDbDAO.commitTransaction(connection);
			return prodList;
		} catch (Exception e) {
			catalogDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Can't find product list in db: ", e);
		}
	}

	public Cart createCart() {
		return new Cart();
	}
	
	public Set<CartItemData> getCartInfo(Cart cart) throws ServiceException {
		Set<CartItemData> items = new HashSet<CartItemData>();
		Connection connection = null;
		try {
			connection = catalogDbDAO.startTransaction();
			for (CartItem item : cart.getItems()) {
				Product product = prodDb.findProductById(connection, item.getProductId());
				CartItemData itemData = new CartItemData(item.getProductId(), item.getQuantity(), product.getCode(),
						product.getDescription(), product.getPrice());
				items.add(itemData);
			}
			catalogDbDAO.commitTransaction(connection);
		} catch (Exception e) {
			catalogDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Can't find product in cart: ", e);
		}
		return items;

	}
	
	public void addItemtoCart(long productId, Cart cart, int quantity) {
		System.out.println("The product Quantity is:=");
		CartItem item = cart.findItem(productId);
		if (item != null) { 
			int qty = item.getQuantity();
			item.setQuantity(qty + quantity);
		} else { 
			item = new CartItem(productId, quantity);
			cart.getItems().add(item);
		}
	}

	public void changeCart(long productId, Cart cart, int quantity) {
		CartItem item = cart.findItem(productId);
		if (item != null) {
			if (quantity > 0) {
				item.setQuantity(quantity);
			} else { 
				cart.removeItem(productId);
			}
		}
	}

	public void removeCartItem(long productId, Cart cart) {
		CartItem item = cart.findItem(productId);
		if (item != null) {
			cart.removeItem(productId);
		}
	}

	public Product getProduct(long productId) throws ServiceException {
		Connection connection = null;
		try {
			connection = catalogDbDAO.startTransaction();
			Product prd = prodDb.findProductById(connection, productId);
			if (prd == null)
				return null;
			for (Track track : prd.getTracks())
				track.getSampleFilename();
			catalogDbDAO.commitTransaction(connection);
			return prd;
		} catch (Exception e) {
			catalogDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Error accessing product "+productId, e);
		}
	}
	
	public Product getProductByCode(String prodCode) throws ServiceException {
		Connection connection = null;
		try {
			connection = catalogDbDAO.startTransaction();
			Product prd = prodDb.findProductByCode(connection, prodCode);
			for (Track track : prd.getTracks())
				track.getSampleFilename();
			catalogDbDAO.commitTransaction(connection);
			return prd;
		} catch (Exception e) {
			catalogDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Error accessing product "+prodCode, e);
		}
	}

	public void addDownload(String emailAddress, Track track) throws ServiceException {
		Connection connection = null;
		try {
			connection = catalogDbDAO.startTransaction();
			Download download = new Download();
			download.setEmailAddress(emailAddress);
			download.setTrack(track);
			download.setDownloadDate(new Date());
			downloadDb.insertDownload(connection, download);
			catalogDbDAO.commitTransaction(connection);
		} catch (Exception e) {
			catalogDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Can't add download: ", e);
		}
	}
	
	public void initializeDB()throws ServiceException {
		try {
			catalogDbDAO.initializeDb();
		} catch (Exception e) { 
			throw new ServiceException(
					"Can't initialize DB: (probably need to load DB)", e);
		}
	}
	
	public Set<DownloadData> getListofDownloads() throws ServiceException {
		Connection connection = null;
		
		Set<Download> downloads = null;
		try {
			connection = catalogDbDAO.startTransaction();
			downloads = downloadDb.findAllDownloads(connection);
			catalogDbDAO.commitTransaction(connection);
		} catch (Exception e)
		{
			catalogDbDAO.rollbackAfterException(connection);
			throw new ServiceException("Can't find download list: ", e);
		}
		Set<DownloadData> downloads1 = new HashSet<DownloadData>();
		for (Download d: downloads) {
			downloads1.add(new DownloadData(d));
		}
		return downloads1;
	}
	
	
}
