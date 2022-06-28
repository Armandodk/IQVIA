package teste1;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;


public class testBaseMethods {
	protected WebDriver driver;
	protected WebDriverWait wait;
	protected List<products> productslist;
	
	@BeforeMethod
	public void init(){
		driver = new ChromeDriver();
	    driver.manage().window().maximize();
	    driver.get("https://www.saucedemo.com/");
	    wait = new WebDriverWait(driver,30);
	}
	
	@AfterMethod
	public void finish() {
		driver.close();
	}
	
	protected void login() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
		driver.findElement(By.id("user-name")).sendKeys("standard_user");
		driver.findElement(By.id("password")).sendKeys("secret_sauce");
		driver.findElement(By.id("login-button")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#header_container > div.header_secondary_container > span")));
	}
	
	
	// add items to the cart
	protected void addItemstoCart(Integer items) {
		this.productslist = new ArrayList<products>(); //arraylist containing all products in the cart 
		for(int i = 1; i <= items; i++) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div/div[2]/div/div/div/div["+i+"]/div[2]/div[1]/a/div")));
			String name = driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div/div/div["+i+"]/div[2]/div[1]/a/div")).getText();
			String value = driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div/div/div["+i+"]/div[2]/div[2]/div")).getText(); 
			String description = driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div/div/div["+i+"]/div[2]/div[1]/div")).getText(); 
			products p = new products(name, value, description);
			productslist.add(p);
			WebElement e = driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div/div/div["+i+"]/div[2]/div[2]/button"));
			e.click(); //click to add the item to the cart 
			e = driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div/div/div["+i+"]/div[2]/div[2]/button"));
			Assert.assertEquals("REMOVE", e.getText()); //validate that once added, button changes to Remove label 
		}
		WebElement icon = driver.findElement(By.cssSelector("#shopping_cart_container > a > span"));
		Assert.assertEquals((items.toString()), icon.getText()); // validates that the shopping cart icon is displaying the right nbr of items added 
	}

	
	//validates items in cart comparing to what was chosen earlier 
	protected void navigateToCart() {
		driver.findElement(By.cssSelector("#shopping_cart_container > a")).click(); 
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("#header_container > div.header_secondary_container > span"), "YOUR CART"));
		this.compareLists(); 
	}
	
	private void compareLists() //compares the list of products added earlier, with the overview or cart list 
	{
		for(int i = 1; i <= productslist.size(); i++) {
			String name = driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div[1]/div["+(i+2)+"]/div[2]/a/div")).getText();
			String description = driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div[1]/div["+(i+2)+"]/div[2]/div[1]")).getText(); 
			String value = driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div[1]/div["+(i+2)+"]/div[2]/div[2]/div")).getText(); 
			products p = this.returnProductbyName(name);
			Assert.assertEquals(name, p.getName());
			Assert.assertEquals(description, p.getDescription());
			Assert.assertEquals(value, p.getValue());
		}
	}
	
	private products returnProductbyName(String name) {
		products error = null;
		 for (products p: productslist) 
	        {
	            if (p.getName().equals(name))
	            {
	               return p;
	            }
	        }
		 return error;
	}
	
	// fills customer info
	protected void checkout() {
		driver.findElement(By.cssSelector("#checkout")).click(); 
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("#header_container > div.header_secondary_container > span"), "CHECKOUT: YOUR INFORMATION"));
		driver.findElement(By.cssSelector("#first-name")).sendKeys("testeIQVIA");
		driver.findElement(By.cssSelector("#last-name")).sendKeys("testeQintess");
		driver.findElement(By.cssSelector("#postal-code")).sendKeys("27703"); 
		driver.findElement(By.cssSelector("#continue")).click(); 
	}
	
	// validates overview, list and total purchase prices
	protected void checkoutOverview() throws ParseException {
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("#header_container > div.header_secondary_container > span"), "CHECKOUT: OVERVIEW"));
		this.compareLists();
		double total = 0;
		for (products p: productslist) 
        {
			String price = p.getValue().replace("$", ""); 
			total = total + Double.parseDouble(price);
        } 		
		
		String s  = driver.findElement(By.cssSelector("#checkout_summary_container > div > div.summary_info > div.summary_subtotal_label")).getText(); //subtotal
		s = s.replace("Item total: $", ""); 
		Assert.assertEquals(String.valueOf(total), s); // validate that the total amount for items are matching
		
		s = driver.findElement(By.cssSelector("#checkout_summary_container > div > div.summary_info > div.summary_tax_label")).getText();  //taxes 
		s = s.replace("Tax: $", ""); 
		double taxes = Double.parseDouble(s);
		total = total + taxes;
		 
		s = driver.findElement(By.cssSelector("#checkout_summary_container > div > div.summary_info > div.summary_total_label")).getText();  //total
		s = s.replace("Total: $", ""); 
		Assert.assertEquals(String.valueOf(total), s); // validates if the total price is right 
		
	} 

	// validates checkout and final messages
	protected void finishPurchase() {
		driver.findElement(By.cssSelector("#finish")).click();
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("#header_container > div.header_secondary_container > span"), "CHECKOUT: COMPLETE!"));
		Assert.assertEquals(driver.findElement(By.cssSelector("#checkout_complete_container > h2")).getText(), "THANK YOU FOR YOUR ORDER");
		Assert.assertEquals(driver.findElement(By.cssSelector("#checkout_complete_container > div")).getText(), "Your order has been dispatched, and will arrive just as fast as the pony can get there!");
	}
	
	// removes item 
	protected void removeItem() {
		String name = "";
		// if the element to be removed is in products UI
		if((driver.findElement(By.cssSelector("#header_container > div.header_secondary_container > span")).getText()).equals("PRODUCTS")) {
		name = driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div/div/div[1]/div[2]/div[1]/a/div")).getText();	
		driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div/div/div[1]/div[2]/div[2]/button")).click();
		WebElement e  = driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div/div/div[1]/div[2]/div[2]/button"));
		Assert.assertEquals("ADD TO CART", e.getText()); //validates that the button text changes
		}
		// if the element to be removed is in your cart 
		if((driver.findElement(By.cssSelector("#header_container > div.header_secondary_container > span")).getText()).equals("YOUR CART")) {
			name = driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div[1]/div[3]/div[2]/a/div")).getText();	
			driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div[1]/div[3]/div[2]/div[2]/button")).click();
		}	
		WebElement icon = driver.findElement(By.cssSelector("#shopping_cart_container > a"));
		if(productslist.size()-1 > 0 ) { Assert.assertEquals(String.valueOf(productslist.size()-1), icon.getText()); }
		else Assert.assertEquals("", icon.getText());
		products p = this.returnProductbyName(name);
		productslist.remove(p);
	}
}
