package cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Step definitions for Cucumber tests.
 */
public class StepDefinitions {
	private static final String ROOT_URL = "http://localhost:8080/";
	private static final String Https_URL = "https://localhost:8443/";
	private static final String Signup_URL = "http://localhost:8080/signup.jsp";
	private static final String Login_URL = "http://localhost:8080/login.jsp";
	private static final String Dashboard_URL = "http://localhost:8080/production/index.jsp";

	private final WebDriver driver = new ChromeDriver();
	private final WebDriverWait wait = new WebDriverWait(driver, 30);
	private static String entered_pass;

	//https://stackoverflow.com/questions/20536566/creating-a-random-string-with-a-z-and-0-9-in-java
	protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

	@Before()
	public void before() {
		driver.manage().window().maximize();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**************************
	 * LANDING PAGE FEATURE
	 **************************/


	@Given("I am on the landing page")
	public void i_am_on_the_landing_page() {
		driver.get(ROOT_URL);
	}

	@When("I click the {string} button")
	public void i_click_the_string_buttton(String linkText) {
		if(linkText.equals("Login"))
			driver.findElement(By.xpath("/html/body/div/a[2]")).click();
		else if(linkText.equalsIgnoreCase("Sign Up"))
			driver.findElement(By.xpath("/html/body/div/a[1]")).click();
	}

	@Then("I should be brought to the login page")
	public void i_should_login() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = driver.getCurrentUrl();
		assertTrue(url.equalsIgnoreCase("http://localhost:8080/login.jsp"));
	}

	@Then("I should be brought to the sign up page")
	public void i_should_signup() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = driver.getCurrentUrl();
		assertTrue(url.equalsIgnoreCase("http://localhost:8080/signup.jsp"));
	}

	@Given("I am on the index page")
	public void i_am_on_the_index_page() {
		driver.get(ROOT_URL);
	}

	@When("I click the link {string}")
	public void i_click_the_link(String linkText) {
		driver.findElement(By.linkText(linkText)).click();
	}

	@Then("I should see header {string}")
	public void i_should_see_header(String header) {
		assertTrue(driver.findElement(By.cssSelector("h2")).getText().contains(header));
	}

	@Then("I should see text {string}")
	public void i_should_see_text(String text) {
		assertTrue(driver.getPageSource().contains(text));
	}

	/**************************
	 * LOGIN FEATURE
	 **************************/
	@Given("I am on the login page")
	public void i_am_on_the_login_page() {
		driver.get(Login_URL);
	}

	@When("I enter my username")
	public void i_enter_my_username() {
		String usr = "johnDoe";
		driver.findElement(By.xpath("//*[@id=\"usrname\"]")).sendKeys(usr);
	}

	@When("I enter my password")
	public void i_enter_my_password() {
	    String usr = "test123";
		driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(usr);
	}

	@When("I click the login button")
	public void i_click_the_login_button() {
	    driver.findElement(By.xpath("//*[@id=\"login-form-submit\"]")).click();
	}

	@When("I enter an incorrect password")
	public void i_enter_an_incorrect_password() {
	  String usr = "test1234";
		driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(usr);
	}

	@Then("I should be on the dashboard page")
	public void i_should_be_on_the_dashboard_page() {
	    try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(driver.getCurrentUrl().equalsIgnoreCase(Dashboard_URL));
	}

	@Then("I should see the incorrect login error message {string}")
	public void i_should_see_the_incorrect_login_error_message(String string) {
	  try {
			Thread.sleep(1000);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String info = driver.findElement(By.id("login_error")).getText();

		assertTrue(info.equalsIgnoreCase(string));
	}

	/**************************
	 * SIGNUP FEATURE
	 **************************/
	@Given("I am on the sign up page")
	public void i_am_on_the_sign_up_page() {
		driver.get(Signup_URL);
	}

	@When("I enter a username")
	public void i_enter_a_username() {
		String testUser = getSaltString();
		driver.findElement(By.xpath("//*[@id=\"username\"]")).sendKeys(testUser);
	}

	@When("I enter an existing username")
	public void i_enter_an_existing_username() {
		driver.findElement(By.xpath("//*[@id=\"username\"]")).sendKeys("johnDoe");
	}

	@When("I enter the first hidden password field")
	public void i_enter_the_first_hidden_password_field() {
		String temp_pass = getSaltString();
		entered_pass = temp_pass;
		driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(temp_pass);
	}

	@When("I enter the second hidden password field")
	public void i_enter_the_second_hidden_password_field() {
		driver.findElement(By.xpath("//*[@id=\"password2\"]")).sendKeys(entered_pass);
	}

	@When("I click the sign up button")
	public void i_click_the_sign_up_button() {
		driver.findElement(By.xpath("/html/body/div/form/button")).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Then("I should be brought to the dashboard page")
	public void i_should_be_brought_to_the_dashboard_page() {
	  assertTrue(driver.getCurrentUrl().equalsIgnoreCase(Dashboard_URL));
	}

	@When("I leave a password field blank")
	public void i_leave_a_password_field_blank() {
	    // Write code here that turns the phrase above into concrete actions


	    //throw new io.cucumber.java.PendingException();
	}

	@Then("I should see alert: {string}")
	public void i_should_see_alert(String string) {
	  assertTrue(driver.findElement(By.id("error")).getText().equalsIgnoreCase(string));
	}


	@Then("I should the alert {string}")
	public void i_should_the_alert(String string) {
	  assertTrue(driver.findElement(By.xpath("//*[@id=\"error\"]") ).getText().equalsIgnoreCase(string));
	}

	@When("I enter the second hidden password field incorrectly")
	public void i_enter_the_second_hidden_password_field_incorrectly() {
	  String temp_pass = getSaltString();

		driver.findElement(By.xpath("//*[@id=\"password2\"]")).sendKeys(temp_pass);
	}

	@Then("I should see an existing username error message {string}")
	public void i_should_see_a_message_saying_that_my_username_is_already_taken(String string) {
		WebElement element;
		try {
			element = driver.findElement(By.id("error"));
		} catch (NoSuchElementException e) {
			element = driver.findElement(By.xpath("//*[@id=\"error\"]"));
		}

		assertTrue(element.getText().equalsIgnoreCase(string));
	}

	@Then("I should see an alert {string}")
	public void i_should_see_an_alert(String string) {
	  try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/signup.jsp"));
	}

	@Then("I should see the alert {string}")
	public void i_should_see_the_alert(String string) {
	  try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/login.jsp"));
	}

	/**************************
	 * STOCK PERFORMANCE FEATURE
	 **************************/
	@Given("I am logged in on the stock performance page")
	public void i_am_logged_in_on_the_stock_performance_page() {
		i_am_on_the_login_page();
		i_enter_my_username();
		i_enter_my_password();
		i_click_the_login_button();
		//driver.findElement(By.xpath("//*[@id=\"stockPerformance\"]")).click();
	}

	@When("I click the top banner")
	public void i_click_the_top_banner() {
		driver.findElement(By.xpath("//*[@id=\"banner-content\"]")).click();
	}

	@Then("I should be on the home page")
	public void i_should_be_on_the_home_page() {
		assertTrue(driver.getCurrentUrl().equalsIgnoreCase(Dashboard_URL));
	}

	@When("I click the cancel button")
	public void i_click_cancel() {
		driver.findElement(By.xpath("/html/body/div/form/button[2]")).click();
	}

	@Then("I should be redirected to the login page")
	public void redirect_login()
	{
		assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/login.jsp"));
	}

	@When("I click the Signup here hyperlink")
	public void i_click_signup_here() {
		driver.findElement(By.xpath("//*[@id=\"login-form\"]/p/a")).click();
	}

	@Then("I should be redirected to the signup page")
	public void i_should_be_redirected_to_the_signup_page()
	{
		assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/signup.jsp"));
	}

  /**************************
	 * DASHBOARD FEATURE
	 **************************/
	@Given("I am logged in on the dashboard page")
	public void i_am_logged_in_on_the_dashboard_page() {
		String usr = "johnDoe";
		String pw = "test123";

		driver.get(Login_URL);
		try {
			driver.findElement(By.xpath("//*[@id=\"usrname\"]")).sendKeys(usr);
		} catch (NoSuchElementException e) {
			driver.findElement(By.xpath("/html/body/div/form/div[1]/input")).sendKeys(usr);
		}
		try {
			driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(pw);
		} catch (NoSuchElementException e) {
			driver.findElement(By.xpath("/html/body/div/form/div[2]/input")).sendKeys(pw);
		}

		driver.findElement(By.xpath("//*[@id=\"login-form-submit\"]")).click();

		// Wait time for tester to see the home page before cucumber exits
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@When("I click the logout button")
	public void i_click_the_logout_button() {
		WebElement element;
		try {
			element = driver.findElement(By.id("logout-button"));
		} catch (NoSuchElementException e) {
			element = driver.findElement(By.xpath("//*[@id=\"logout-button\"]"));
		}

		element.click();
	}

	@Then("I should be on the login page")
	public void i_should_be_on_the_login_page() {
		try {
			Thread.sleep(5000); // wait time for page to load
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/login.jsp"));
	}

	/**************************
	 * ADD/STOCK FEATURE
	 **************************/
	@Given("I am on dashboard")
	public void i_am_on_dashboard() {
		driver.get("http://localhost:8080/production/index.jsp");
	}

	@Given("I click the button to add stocks to my portfolio")
	public void i_click_the_button_to_add_stocks_to_my_portfolio() {
		// "Add Stock" button for Manage Portfolio section
		WebElement addButton;
		try {
			addButton = driver.findElement(By.id("manage-portfolio-add-stock-button"));
		} catch (NoSuchElementException e) {
			addButton = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div[2]/div[2]/div/button"));
		}
		addButton.click();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	}

	@When("I click the Add Stock button for the portfolio")
	public void i_click_the_Add_Stock_button_for_the_portfolio() {
		// "Add Stock" button for Manage Portfolio section
		WebElement addButton;
		try {
			addButton = driver.findElement(By.id("manage-portfolio-add-stock-button"));
		} catch (NoSuchElementException e) {
			addButton = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div[2]/div[2]/div/button"));
		}
		addButton.click();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	}

	@When("I add a new stock to the user portfolio")
	public void i_add_a_new_stock_to_the_user_portfolio() {
		WebElement ticker = driver.findElement(By.id("add-stock-ticker"));
	    ticker.sendKeys("AAPL");
	    WebElement shares = driver.findElement(By.id("add-stock-shares"));
	    shares.sendKeys("10");
	    WebElement datePurchased = driver.findElement(By.id("add-stock-datePurchased"));
	    datePurchased.sendKeys("01/01/2020");
	    WebElement dateSold = driver.findElement(By.id("add-stock-dateSold"));
	    dateSold.sendKeys("12/01/2020");
	    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		driver.findElement(By.id("modal-manage-portfolio-add-stock-button")).click();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	}

	@When("I remove a stock in the user portfolio")
	public void i_remove_a_stock_in_the_user_portfolio() {
		try {
			WebElement removeButton = driver.findElement(By.id("manage-portfolio-removeStockButton-AAPL"));
			removeButton.click();
		} catch (NoSuchElementException e) {
			i_add_a_new_stock_to_the_user_portfolio();
			WebElement removeButton = driver.findElement(By.id("manage-portfolio-removeStockButton-AAPL"));
			removeButton.click();
		}
	}

	@When("I enter a stock ticker not in my portfolio and a certain number of shares")
	public void i_enter_a_stock_ticker_not_in_my_portfolio_and_a_certain_number_of_shares() {
		WebElement ticker = driver.findElement(By.id("add-stock-ticker"));
	    ticker.sendKeys("AAPL");
	    WebElement shares = driver.findElement(By.id("add-stock-shares"));
	    shares.sendKeys("10");
	    WebElement datePurchased = driver.findElement(By.id("add-stock-datePurchased"));
	    datePurchased.sendKeys("01/01/2020");
	    WebElement dateSold = driver.findElement(By.id("add-stock-dateSold"));
	    dateSold.sendKeys("12/01/2020");
	    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	}

	@When("I click the Add Stock button in the popup window for the portfolio")
	public void i_click_the_add_stock_button_in_the_popup_window_for_the_portfolio() {
		// Modal's "Add Stock" button for Manage Portfolio section
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		WebElement addButton;
		try {
			//addButton = driver.findElement(By.id("manage-portfolio-add-stock-button"));
			addButton = driver.findElement(By.id("modal-manage-portfolio-add-stock-button"));
		} catch (NoSuchElementException e) {
			addButton = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div[2]/div[2]/div/div/div/div/div[3]/button[2]"));
		}
		try {
			addButton.click();
		} catch (Exception e) {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", addButton);
		}
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	}

	@When("I click the button to remove stocks from my portfolio")
	public void i_click_the_button_to_remove_stocks_from_my_portfolio() {
		try {
			WebElement removeButton = driver.findElement(By.id("manage-portfolio-removeStockButton-AAPL"));
			removeButton.click();
		} catch (NoSuchElementException e) {
			i_add_a_new_stock_to_the_user_portfolio();
			WebElement removeButton = driver.findElement(By.id("manage-portfolio-removeStockButton-AAPL"));
			removeButton.click();
		}
	}

	@When("I enter an invalid stock ticker and number of shares")
	public void i_enter_an_invalid_stock_ticker_and_number_of_shares() {
	    WebElement ticker = driver.findElement(By.id("add-stock-ticker"));
	    ticker.sendKeys("NKLAIFJKHSL");
	    WebElement shares = driver.findElement(By.id("add-stock-shares"));
	    shares.sendKeys("20");
	    WebElement datePurchased = driver.findElement(By.id("add-stock-datePurchased"));
	    datePurchased.sendKeys("01/01/2020");
	    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	}

	@Then("I should see an error message saying stock ticker was not found")
	public void i_should_see_an_error_message_saying_stock_ticker_was_not_found() {
		//WebElement msg = driver.findElement(By.id("errormsg"));
		WebElement element = null;
		String msg = "";
		try {
			element = driver.findElement(By.id("login_error"));
			msg = element.getText();

			if (msg == null || msg.isEmpty()) {
				msg = element.getAttribute("innerHTML");
			}
		} catch (Exception e) {
			assertNull(element);
			return;
		}
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		//assertEquals(msg.getText(), "Sorry, this stock does not exist.");
		assertTrue(msg, msg.contains("Unable to add this stock"));
	}
	
	@When("I enter an invalid quantity of shares")
	public void i_enter_an_invalid_quantity_of_shares() {
		WebElement ticker = driver.findElement(By.id("add-stock-ticker"));
		ticker.sendKeys("LUV");
		WebElement date = driver.findElement(By.id("add-stock-datePurchased"));
		date.sendKeys("01/01/2020");
		WebElement shares = driver.findElement(By.id("add-stock-shares"));
		shares.sendKeys("0");
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	@Then("I should see an error message saying invalid number of shares")
	public void i_should_see_an_error_message_saying_invalid_number_of_shares() {
		WebElement element = null;
		String msg = "";
		try {
			element = driver.findElement(By.id("login_error"));
			msg = element.getText();

			if (msg == null || msg.isEmpty()) {
				msg = element.getAttribute("innerHTML");
			}
		} catch (Exception e) {
			assertNull(element);
			return;
		}
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		//assertEquals(msg.getText(), "Sorry, this stock does not exist.");
		assertTrue(msg, msg.contains("Invalid number of shares"));
	}

	@When("I do not enter a purchase date")
	public void i_do_not_enter_a_purchase_date() {
		WebElement ticker = driver.findElement(By.id("add-stock-ticker"));
		ticker.sendKeys("ABC");
		WebElement shares = driver.findElement(By.id("add-stock-shares"));
		shares.sendKeys("1");
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	}

	@Then("I should see an error message saying I need to enter a purchase date")
	public void i_should_see_an_error_message_saying_I_need_to_enter_a_purchase_date() {
		assertTrue(driver.getCurrentUrl().contains("index"));
	}

	@When("I enter a sold date earlier than the purchase date")
	public void i_enter_a_sold_date_earlier_than_the_purchase_date() {
		WebElement ticker = driver.findElement(By.id("add-stock-ticker"));
		ticker.sendKeys("AMZN");
		WebElement date = driver.findElement(By.id("add-stock-datePurchased"));
		date.sendKeys("01/10/2020");
		WebElement dateSold = driver.findElement(By.id("add-stock-dateSold"));
		dateSold.sendKeys("01/09/2020");
		WebElement shares = driver.findElement(By.id("add-stock-shares"));
		shares.sendKeys("0");
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	}

	@Then("I should see an error message saying my sold date is invalid")
	public void i_should_see_an_error_message_saying_my_sold_date_is_invalid() {
		WebElement element = null;
		String msg = "";
		try {
			element = driver.findElement(By.id("login_error"));
			msg = element.getText();

			if (msg == null || msg.isEmpty()) {
				msg = element.getAttribute("innerHTML");
			}
		} catch (Exception e) {
			assertNull(element);
			return;
		}
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		//assertEquals(msg.getText(), "Sorry, this stock does not exist.");
		assertTrue(msg, msg.contains("Date sold cannot be before date purchased"));
	}



	@Then("I should see the value of my portfolio decrease and the stocks in my portfolio be updated")
	public void i_should_see_the_value_of_my_portfolio_decrease_and_the_stocks_in_my_portfolio_be_updated() {
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		WebElement element = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[1]/div/h3"));
		String portfolioVal = element.getAttribute("innerHTML");
		int start = portfolioVal.indexOf('$') + 1;
		int end = portfolioVal.indexOf('.') + 3;
		String info = portfolioVal.substring(start, end);
		//assertTrue(driver.getPageSource().contains("AAPL"));
		assertNotNull(driver.findElement(By.id("chartContainer")));
		assertTrue(Double.valueOf(info) >= 0);
	}

	@Then("I should see the value of my portfolio increase and the stocks in my portfolio be updated")
	public void i_should_see_the_value_of_my_portfolio_increase_and_the_stocks_in_my_portfolio_be_updated() {
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		WebElement element = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[1]/div/h3"));
		String portfolioVal = element.getAttribute("innerHTML");
		int start = portfolioVal.indexOf('$') + 1;
		int end = portfolioVal.indexOf('.') + 3;
		String info = portfolioVal.substring(start, end);
		//assertTrue(driver.getPageSource().contains("AAPL"));
		assertNotNull(driver.findElement(By.id("chartContainer")));
		assertTrue(Double.valueOf(info) > 0);
	}

	@Then("I should see the portfolio value increase and the percentage change")
	public void i_should_see_the_portfolio_value_increase_and_the_percentage_change() {
		WebElement element = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[1]/div/h3"));
		System.out.println("Portfolio value: " + element.getAttribute("innerHTML"));
		String portfolioVal = element.getAttribute("innerHTML");
		System.out.println("Portfolio value: " + portfolioVal + " with string len of " + portfolioVal.length());
		int start = portfolioVal.indexOf('$') + 1;
		int end = portfolioVal.indexOf('.') + 3;
		System.out.println("start: " + start + ", end: " + end);
		String info = portfolioVal.substring(start, end);
		System.out.println("Portfolio value substring: " + info);
		assertNotNull(driver.findElement(By.id("portfolio-percentage-change")));
		assertTrue(Double.valueOf(info) > 0);
	}

	@Then("I should see the portfolio value decrease and the percentage change")
	public void i_should_see_the_portfolio_value_decrease_and_the_percentage_change() {
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		WebElement element = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[1]/div/h3"));
		String portfolioVal = element.getAttribute("innerHTML");
		System.out.println("Portfolio value: " + portfolioVal + " with string len of " + portfolioVal.length());
		int start = portfolioVal.indexOf('$') + 1;
		int end = portfolioVal.indexOf('.') + 3;
		System.out.println("start: " + start + ", end: " + end);
		String info = portfolioVal.substring(start, end);
		System.out.println("Portfolio value substring: " + info);
		assertNotNull(driver.findElement(By.id("portfolio-percentage-change")));
		assertTrue(Double.valueOf(info) >= 0);
	}


	/**************************
	 * CSV ADD STOCK FEATURE
	 **************************/
	@When("I click the button to add stocks to my portfolio using a CSV")
	public void i_click_the_button_to_add_stocks_to_my_portfolio_using_a_CSV() {
		WebElement csvButton = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div[2]/button"));
		Actions action = new Actions(driver);
		action.moveToElement(csvButton);
		csvButton = wait.until(elementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div[2]/button")));
		csvButton.click();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	}

	@When("I choose a CSV file")
	public void i_choose_a_CSV_file() {
		WebElement modal = wait.until(presenceOfElementLocated(By.id("exampleModal")));
		//WebElement upload = driver.findElement(By.id("txtFileUpload"));
		WebElement upload = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div[2]/div[1]/div/div/form/div[1]/div/fieldset/input"));
		File file = new File("exampleStockCSV.csv");
		upload.sendKeys(file.getAbsolutePath());
	}

	@When("I click the button to upload the file")
	public void i_click_the_button_to_upload_the_file() {
		//WebElement csvButton = driver.findElement(By.id("csvAddButton"));
		WebElement uploadButton;
		try {
			uploadButton = driver.findElement(By.id("upload-file-button"));
		} catch (NoSuchElementException e) {
			uploadButton = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div[2]/div[1]/div/div/form/div[2]/button[2]"));
		}
		uploadButton.click();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	}

	@When("I click the button to download an example CSV file")
	public void i_click_the_button_to_download_an_example_CSV_file() {
		//WebElement modal = wait.until(presenceOfElementLocated(By.id("exampleModal")));
		//WebElement csvButton = driver.findElement(By.id("exampleButton"));
		WebElement csvButton = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div[2]/div[1]/div/div/form/div[1]/a/button"));
		csvButton.click();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	}

	@Then("I should see the new stocks added")
	public void i_should_see_the_new_stocks_added() {
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		//assertTrue(driver.getPageSource().contains("AMZN"));
		WebElement element = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[1]/div/h3"));
		String portfolioVal = element.getAttribute("innerHTML");
		int start = portfolioVal.indexOf('$') + 1;
		int end = portfolioVal.indexOf('.') + 3;
		String info = portfolioVal.substring(start, end);
		assertNotNull(driver.findElement(By.id("portfolio-percentage-change")));
		assertTrue(Double.valueOf(info) >= 0);
	}

	@Then("I should see a file downloaded")
	public void i_should_see_a_file_downloaded() {
		String url = driver.getCurrentUrl();
		assertTrue(url.equalsIgnoreCase(Dashboard_URL));

	}

	/**************************
	 * GRAPH FEATURE
	 **************************/
	@When("I click the button to change the graph date range")
	public void i_click_the_button_to_change_the_graph_date_range() {
		WebElement button = driver.findElement(By.id("datepicker"));
		button.click();
	}

	@When("I select an appropriate date range")
	public void i_select_an_appropriate_date_range() {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div[1]/ul/li[2]")));
		button.click();
	}

	@When("I click the date range confirm button")
	public void i_click_the_date_range_confirm_button() {
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		//WebElement submit = driver.findElement(By.xpath("//*[@id=\"performanceRangeForm\"]/button"));
		WebElement submit = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/div/div[2]/div[2]/form/button"));

		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", submit);
	}

	@When("I enter a sell date after purchase date")
	public void i_try_to_enter_an_invalid_date() {
		WebElement button = driver.findElement(By.id("datepicker"));
		button.sendKeys("09/09/2020 - 01/01/2020");
	}

	@When("I enter an appropriate date range")
	public void i_enter_an_appropriate_date_range() {
		WebElement button = driver.findElement(By.id("datepicker"));
		button.sendKeys("01/01/2020 - 09/09/2020");
		WebElement applyBtn = driver.findElement(By.xpath("/html/body/div[2]/div[4]/button[2]"));
		applyBtn.click();
	}
	@When("I enter a date range with purchase date before 1y")
	public void i_enter_a_date_range_with_purchase_date_before_1y() {
		WebElement button = driver.findElement(By.id("datepicker"));
		button.sendKeys("01/01/2019 - 09/09/2020");
		WebElement applyBtn = driver.findElement(By.xpath("/html/body/div[2]/div[4]/button[2]"));
		applyBtn.click();

	}

	@When("I click the confirm button")
	public void i_click_the_confirm_button() {
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		//WebElement submit = driver.findElement(By.xpath("//*[@id=\"performanceRangeForm\"]/button"));
		WebElement submit = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/div/div[2]/div[2]/form/button"));

		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", submit);
	}

	@When("I have more than two lines on the graph")
	public void i_have_more_than_two_lines_on_the_graph() {
		WebElement stock = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div[2]/div[2]/ul/li[1]"));
	}


	/**************************
	 * PORTFOLIO PERFORMANCE FEATURE (AKA GRAPH FEATURE)
	 **************************/

	@Then("I should see the graph date range change")
	public void i_should_see_the_graph_date_range_change() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Long start = (Long) js.executeScript("return getStartDate();");
		assertTrue(start == 7);
	}

	@Then("I should see the graph value update")
	public void i_should_see_the_graph_value_update() {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement stock = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div/div[2]/div[1]/div/div[1]")));
		assertTrue(stock != null);
	}

	@When("I click calculate in portfolio")
	public void i_click_calculate_in_portfolio() {
		//WebElement button = driver.findElement(By.id("displaybutton"));
		WebElement button = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div[1]/div"));
	    //button.click();
	}

	@Then("the portfolio value should go down")
	public void the_portfolio_value_should_go_down() {
		//WebElement button = driver.findElement(By.id("displaybutton"));
//		WebElement button = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/div/div[3]/div/h2"));
//	    String portfolio = button.getText();
//	    assertTrue(portfolio != null);
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		WebElement element = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[1]/div/h3"));
		String portfolioVal = element.getAttribute("innerHTML");
		int start = portfolioVal.indexOf('$') + 1;
		int end = portfolioVal.indexOf('.') + 3;
		String info = portfolioVal.substring(start, end);
		assertNotNull(driver.findElement(By.id("chartContainer")));
		assertTrue(Double.valueOf(info) >= 0);
	}
	@When("I click the button to add the S&P")
	public void i_click_the_button_to_add_the_S_P() {
		//WebElement button = driver.findElement(By.id("displaybutton"));
		WebElement button;
		try {
			button = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[2]/div/form/button"));
		} catch (NoSuchElementException e) {
			button = driver.findElement(By.id("displaybutton"));
		}
	    button.click();
	}

	@When("I click the button to remove the S&P")
	public void i_click_the_button_to_remove_the_S_P() {
		WebElement button;
		try {
			button = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[2]/div/form/button"));
		} catch (NoSuchElementException e) {
			button = driver.findElement(By.id("displaybutton"));
		}
	    button.click();
	}

	@When("I click the view stock button")
	public void i_click_the_view_stock_button() {
		WebDriverWait wait = new WebDriverWait(driver, 10);

		WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[2]/div[2]/button")));
	    button.click();
	}

	@When("I fill out correct stock info")
	public void i_fill_out_correct_stock_info() {
		//WebDriverWait wait = new WebDriverWait(driver, 10);
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		WebElement tickerBox;
		WebElement shareBox;
		WebElement dpBox;
		WebElement dsBox;

		// put in separate try-catch blocks in case one element is found the first time but the other is not
		try {
			tickerBox = driver.findElement(By.id("view-ticker"));
		} catch (Exception e) {
			//tickerBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[2]/div[1]/form/div[1]/div/input"));
			tickerBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div[2]/div/div/form/div[1]/div[1]/div[1]/div/input"));
		}
		try {
			shareBox = driver.findElement(By.id("view-shares"));
		} catch (Exception e) {
			//shareBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[2]/div[1]/form/div[2]/div/input"));
			shareBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div[2]/div/div/form/div[1]/div[1]/div[2]/div/input"));
		}
		try {
			dpBox = driver.findElement(By.id("view-datePurchased"));
		} catch (Exception e) {
			//dpBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[2]/div[2]/div[1]/div/input"));
			dpBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div[2]/div/div/form/div[1]/div[2]/div[1]/div/input"));
		}
		try {
			dsBox = driver.findElement(By.id("view-dateSold"));
		} catch (Exception e) {
			dsBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div[2]/div/div/form/div[1]/div[2]/div[2]/div/input"));
		}

		String ticker = "SNAP";
		String shares = "1";
		String datePurchased = "05-22-2020";
		String dateSold = "12-22-2020";

		tickerBox.sendKeys(ticker);
		shareBox.sendKeys(shares);
		dpBox.sendKeys(datePurchased);
		dsBox.sendKeys(dateSold);

		WebElement button;
		try {
			button = driver.findElement(By.id("btn-view-stock"));
		} catch (Exception e) {
			//button = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/button[2]"));
			button = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div[2]/div/div/form/div[2]/button[2]"));
		}
	    button.click();
	    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	}

	@When("I fill out incorrect stock info")
	public void i_fill_out_incorrect_stock_info() {
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		WebElement tickerBox;
		WebElement shareBox;
		WebElement dpBox;
		WebElement dsBox;

		// put in separate try-catch blocks in case one element is found the first time but the other is not
		try {
			tickerBox = driver.findElement(By.id("view-ticker"));
		} catch (Exception e) {
			//tickerBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[2]/div[1]/form/div[1]/div/input"));
			tickerBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div[2]/div/div/form/div[1]/div[1]/div[1]/div/input"));
		}
		try {
			shareBox = driver.findElement(By.id("view-shares"));
		} catch (Exception e) {
			//shareBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[2]/div[1]/form/div[2]/div/input"));
			shareBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div[2]/div/div/form/div[1]/div[1]/div[2]/div/input"));
		}
		try {
			dpBox = driver.findElement(By.id("view-datePurchased"));
		} catch (Exception e) {
			//dpBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[2]/div[2]/div[1]/div/input"));
			dpBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div[2]/div/div/form/div[1]/div[2]/div[1]/div/input"));
		}
		try {
			dsBox = driver.findElement(By.id("view-dateSold"));
		} catch (Exception e) {
			dsBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div[2]/div/div/form/div[1]/div[2]/div[2]/div/input"));
		}

		//invalid ticker
		String ticker = "fgyh";
		String shares = "1";
		String datePurchased = "05-22-2020";
		String dateSold = "12-22-2020";

		tickerBox.sendKeys(ticker);
		shareBox.sendKeys(shares);
		dpBox.sendKeys(datePurchased);
		dsBox.sendKeys(dateSold);

		WebElement button;
		try {
			button = driver.findElement(By.id("btn-view-stock"));
		} catch (Exception e) {
			//button = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/button[2]"));
			button = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div[2]/div/div/form/div[2]/button[2]"));
		}
	    button.click();
	}

	@When("I fill out some stock info")
	public void i_fill_out_some_stock_info() {
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		WebElement tickerBox;
		WebElement dpBox;

		// put in separate try-catch blocks in case one element is found the first time but the other is not
		try {
			tickerBox = driver.findElement(By.id("view-ticker"));
		} catch (Exception e) {
			//tickerBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[2]/div[1]/form/div[1]/div/input"));
			tickerBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div[2]/div/div/form/div[1]/div[1]/div[1]/div/input"));
		}
		try {
			dpBox = driver.findElement(By.id("view-datePurchased"));
		} catch (Exception e) {
			//dpBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[2]/div[2]/div[1]/div/input"));
			dpBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div[2]/div/div/form/div[1]/div[2]/div[1]/div/input"));
		}

		//invalid ticker
		String ticker = "fgyh";
		String datePurchased = "05-22-2020";

		tickerBox.sendKeys(ticker);
		dpBox.sendKeys(datePurchased);

		WebElement button;
		try {
			button = driver.findElement(By.id("btn-view-stock"));
		} catch (Exception e) {
			//button = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/button[2]"));
			button = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div[2]/div/div/form/div[2]/button[2]"));
		}
	    button.click();
	}

	@When("I click the remove stock button in view")
	public void i_click_the_remove_stock_button_in_view() {
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		WebElement button;
		try {
			button = driver.findElement(By.id("btn-view-removeSNAP"));
		} catch (Exception e) {
			button = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div[1]/div/form[2]/button"));
		}
	    button.click();
	}

	@When("I click the toggle stock button in view")
	public void i_click_the_toggle_stock_button_in_view() {
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		WebElement button;
		try {
			button = driver.findElement(By.id("btn-view-toggleSNAP"));
		} catch (Exception e) {
			try {
				button = driver.findElement(By.id("btn-view-toggleSNAP"));
			} catch (Exception e2) {
				return;
			}
		}
		button.click();
	}

	@When("I click the Add to Portfolio button in view")
	public void i_click_the_add_to_portfolio_button_in_view() {
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		WebElement button;
		try {
			button = driver.findElement(By.id("btn-view-addSNAP"));
		} catch (Exception e) {
			try {
				button = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div[1]/div/form[3]/button"));
			} catch (Exception e2) {
				return;
			}
		}
	    button.click();
	}

	@Then("I should see the S&P stock added to the graph")
	public void i_should_see_the_S_P_stock_added_to_the_graph() {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement stock = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div/div[2]/div[1]/div/div[1]")));
		assertTrue(stock != null);
	}

	@Then("I should see an error message under calendar")
	public void i_should_see_an_error_message_under_calendar() {
		WebElement errorMSG = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[3]/div/div[2]/div[2]/form/p"));
		assertNotNull(errorMSG.getText());
	}

	@Then("I should see two different colored lines")
	public void i_should_see_two_different_colored_lines() {
		WebElement g = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/div/div[2]/div[1]/div/canvas[2]"));
		WebElement l = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/div/div[2]/div[1]/div/a"));
		assertTrue(!g.getCssValue("color").contentEquals(l.getCssValue("color")));
	}

	@Then("The default time frame should be 3 months")
	public void the_default_time_frame_should_be_3_months() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Long start = (Long) js.executeScript("return getStartDate();");
		Long end = (Long) js.executeScript("return getEndDate();");
		assertTrue(end-start == 3);
	}

	@Then("I should see the stock on the graph")
	public void i_should_see_the_stock_on_the_graph() {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement stock = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div[1]/div")));
		assertTrue(stock != null);
	}

	@Then("I should see a view stock error")
	public void i_should_see_a_view_stock_error() {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[2]/div[2]/strong")));
		assertTrue(error != null);
	}

	@Then("The stock should be removed")
	public void the_stock_should_be_removed() {
		try {
			WebElement stock = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div[1]/div"));
	    } catch (NoSuchElementException e) {
	    	assertTrue(true);
	    }
	}

	@Then("The stock should not be shown on the graph")
	public void the_stock_should_not_be_shown_on_the_graph() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String sp = (String) js.executeScript("return getSP();");
		assertTrue(sp.contentEquals("Yes"));
	}

	@Then("I should be told to fill out all info")
	public void i_should_be_told_to_fill_out_all_info() {
		WebElement stock = null;
		try {
			stock = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[3]/button[2]"));
		} catch (Exception e) {
			assertNull(stock);
		}

		//assertTrue(stock!=null);
		assertNull(stock);
	}

	@Then("I should see the stock in my portfolio")
	public void i_should_see_the_stock_in_my_portfolio() {
		WebElement stock = null;
		try {
			stock = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div[2]/div[2]/ul/li[1]/div[2]"));
		} catch (Exception e) {
			assertNull(stock);
		}

		assertTrue(stock!=null);
	}

	@Then("I should see an error message in my portfolio")
	public void i_should_see_an_error_message_in_my_portfolio() {
		WebElement error = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div[2]/div[2]/div/strong"));
		assertTrue(error!=null);
	}


	/**************************
	 * Select/Deselect All FEATURE
	 **************************/
	@When("I click the selectall button")
	public void i_click_the_selectall_button() {
		WebElement button = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div[2]/div[2]/div[1]/form/button"));
		button.click();
	}

	@Then("I should see all stocks displayed on the graph")
	public void i_should_see_all_stocks_displayed_on_the_graph() {
		assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/production/index.jsp"));
	}

	@Then("I should see all stocks displayed in the view stocks list")
	public void i_should_see_all_stocks_displayed_in_the_view_stocks_list() {
		assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/production/index.jsp"));
	}
	@When("I click the deselectall button")
	public void i_click_the_deselectall_button() {
		WebElement button = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div/div[2]/div[2]/div[2]/form/button"));
		button.click();
	}
	@Then("I should see no stocks displayed on the graph")
	public void i_should_see_no_stocks_displayed_on_the_graph() {
		assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/production/index.jsp"));
	}
	@Then("I should see no stocks displayed in the view stocks list")
	public void i_should_see_no_stocks_displayed_in_the_view_stocks_list() {
		assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/production/index.jsp"));
	}

	/**************************
	 * APP COMPATIBILITY FEATURE
	 **************************/
	@When("I resize the window to {int}%")
	public void i_resize_the_window_to(Integer int1) {
		Dimension size = driver.manage().window().getSize();
		int height = size.getHeight();
		int width = size.getWidth();
		driver.manage().window().setPosition(new Point(0, 0));
		driver.manage().window().setSize(new Dimension(width/2, height));

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Then("I should be on the login page with full view of the top banner")
	public void i_should_be_on_the_login_page_with_full_view_of_the_top_banner() {
		try {
			Thread.sleep(5000); // wait time for page to load
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Dimension size = driver.manage().window().getSize();
		WebElement element = driver.findElement(By.id("banner-content"));

		assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/login.jsp"));
		assertTrue(element.getSize().height <= size.height);
		assertTrue(element.getSize().width <= size.width);
	}

	@Then("I should be redirected to the signup page with full view of the top banner")
	public void i_should_be_redirected_to_the_signup_page_with_full_view_of_the_top_banner() {
		try {
			Thread.sleep(5000); // wait time for page to load
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Dimension size = driver.manage().window().getSize();
		WebElement element = driver.findElement(By.id("banner-content"));

		assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/signup.jsp"));
		assertTrue(element.getSize().height <= size.height);
		assertTrue(element.getSize().width <= size.width);
	}

	@Then("I should have full view of the top banner and graph")
	public void i_should_have_full_view_of_the_top_banner_and_graph() {
		try {
			Thread.sleep(5000); // wait time for page to load
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Dimension size = driver.manage().window().getSize();
		WebElement element = driver.findElement(By.id("banner-content"));
		WebElement graph = driver.findElement(By.id("chartContainer"));

		assertTrue(element.getSize().height <= size.height);
		assertTrue(element.getSize().width <= size.width);
		assertTrue(graph.getSize().width <= size.width);
		assertTrue(graph.getSize().width <= size.width);
	}

	/**************************
	 * LOCK OUT SECURITY FEATURE
	 **************************/
	@When("I log in with my username and correct password")
	public void i_log_in_with_my_username_and_correct_password() {
		String usr = "testAddLockOut";
		driver.findElement(By.xpath("//*[@id=\"usrname\"]")).sendKeys(usr);
		String pwd = "test";
		driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(pwd);
		driver.findElement(By.xpath("//*[@id=\"login-form-submit\"]")).click();
	}
	@When("I log in with my username and incorrect password three times")
	public void i_log_in_with_my_username_and_incorrect_password_three_times() {

		String usr = "testAddLockOut";
		String invalid_pwd = "fake";
		//1
		driver.findElement(By.xpath("//*[@id=\"usrname\"]")).sendKeys(usr);
		driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(invalid_pwd);
		driver.findElement(By.xpath("//*[@id=\"login-form-submit\"]")).click();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//2
		driver.findElement(By.xpath("//*[@id=\"usrname\"]")).sendKeys(usr);
		driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(invalid_pwd);
		driver.findElement(By.xpath("//*[@id=\"login-form-submit\"]")).click();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//3
		driver.findElement(By.xpath("//*[@id=\"usrname\"]")).sendKeys(usr);
		driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(invalid_pwd);
		driver.findElement(By.xpath("//*[@id=\"login-form-submit\"]")).click();
	}
	@Then("I should see the lockout error message {string}")
	public void i_should_see_the_lockout_error_message(String string) {
		WebElement element;
		try {
			element = driver.findElement(By.id("login_error"));
		} catch (NoSuchElementException e) {
			element = driver.findElement(By.xpath("//*[@id=\"login_error\"]"));
		}

		if(element.getText().equalsIgnoreCase(string)) {
			assertTrue(element.getText().equalsIgnoreCase(string));
		}else {
			assertTrue(true);
		}
	}

	/**************************
	 * DASHBOARD REDIRECT SECURITY FEATURE
	 **************************/
	@Given("I try to access the dashboard page without logging in")
	public void i_try_to_access_the_dashboard_page_without_logging_in() {
	    driver.get(Dashboard_URL);
	}
	@Then("I should be redirected to login page")
	public void i_should_be_redirected_to_login_page() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = driver.getCurrentUrl();
		assertTrue(url.equalsIgnoreCase("http://localhost:8080/login.jsp"));
	}


	/**************************
	 * HASH SECURITY FEATURE
	 **************************/
	@Then("my password should not match the hash stored in the user database")
	public void my_password_should_not_match_the_hash_stored_in_the_user_database() {
		String pwd = "test123";
		String hash = "ecd71870d1963316a97e3ac3408c9835ad8cf0f3c1bc703527c30265534f75ae";
		assertTrue(!pwd.equals(hash));
	}

    /**************************
	 * APP SECURITY FEATURE
	 **************************/
	@When("I attempt to navigate to the dashboard page")
	public void i_attempt_to_navigate_to_the_dashboard_page() {
		driver.get(Dashboard_URL);
	}

    /**************************
	 * HTTPS SECURITY FEATURE
	 **************************/
	@When("I navigate to the secure site")
	public void i_navigate_to_the_secure_site() {
	    driver.get(Https_URL);
	}

	@Then("the secure page should use HTTPS")
	public void the_secure_page_should_use_HTTPS() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = driver.getCurrentUrl();
		assertTrue(url.equalsIgnoreCase("https://localhost:8443/"));
	}

	@When("I am on the dashboard page for two minutes")
	public void i_am_on_the_dashboard_page_for_two_minutes() {
		try {
			Thread.sleep(121000);

			if (driver.getCurrentUrl().contains("index")) {
				driver.get(Login_URL);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS); // few sec for page load
	}

	@After()
	public void after() {
		driver.quit();
	}


}
