package teste1;

import java.text.ParseException;

import org.testng.annotations.Test;

public class cenarios extends testBaseMethods {

	@Test
	public void buySingleItem() throws ParseException {
		this.login();
		// add items to the cart, parameter as nbr of items to add
		this.addItemstoCart(1);
		// validates items in cart comparing to what was chosen earlier
		this.navigateToCart();
		// fills customer info
		this.checkout();
		// validates overview, list and total purchase prices
		this.checkoutOverview();
		// validates checkout and final messages
		this.finishPurchase();
	}
	
	@Test
	public void buyMultipleMID() throws ParseException {
		this.login();
		// add items to the cart, parameter as nbr of items to add
		this.addItemstoCart(3);
		// validates items in cart comparing to what was chosen earlier
		this.navigateToCart();
		// fills customer info
		this.checkout();
		// validates overview, list and total purchase prices
		this.checkoutOverview();
		// validates checkout and final messages
		this.finishPurchase();
	}

	@Test
	public void buyMultipleMAX() throws ParseException { 
		this.login();
		// add items to the cart, parameter as nbr of items to add
		this.addItemstoCart(6);
		// validates items in cart comparing to what was chosen earlier
		this.navigateToCart();
		// fills customer info
		this.checkout();
		// validates overview, list and total purchase prices
		this.checkoutOverview();
		// validates checkout and final messages
		this.finishPurchase();
	}
	
	@Test
	public void removeItems() {
		this.login();
		this.addItemstoCart(2);
		this.removeItem();
		this.navigateToCart();
		this.removeItem();
	}
}
