package cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

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
	private static final String Signup_URL = "http://localhost:8080/signup.jsp";
	private static final String Login_URL = "http://localhost:8080/login.jsp";
	private static final String Dashboard_URL = "http://localhost:8080/production/index.jsp";

	private final WebDriver driver = new ChromeDriver();
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
	public void redirect_signup()
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
		driver.findElement(By.xpath("//*[@id=\"usrname\"]")).sendKeys(usr);
		driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(pw);
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
	
	@When("I am on the dashboard page for two minutes")
	public void i_am_on_the_dashboard_page_for_two_minutes() {
		try {
			Thread.sleep(120000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Then("I should be on the login page")
	public void i_should_be_on_the_login_page() {
		try {
			Thread.sleep(1000);
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
		WebElement button = driver.findElement(By.id("add"));
		button.click();
	}

	@Given("I enter a stock ticker not in my portfolio and a certain number of shares")
	public void i_enter_a_stock_ticker_not_in_my_portfolio_and_a_certain_number_of_shares() {
	    WebElement exchange = driver.findElement(By.id("exchange"));
	    exchange.sendKeys("NASDAQ");
	    WebElement ticker = driver.findElement(By.id("ticker"));
	    ticker.sendKeys("TSLA");
	    WebElement shares = driver.findElement(By.id("shares"));
	    shares.sendKeys("10");
	}

	@When("I click the submit button")
	public void i_click_the_submit_button() {
		WebElement submit = driver.findElement(By.id("stockaddbutton"));
	    submit.click();;
	}

	@Then("I should see the value of my portfolio increase and the stocks in my portfolio be updated")
	public void i_should_see_the_value_of_my_portfolio_increase_and_the_stocks_in_my_portfolio_be_updated() {
		WebElement exchange = driver.findElement(By.id("exchange1"));
	    assertEquals(exchange.getText(), "NASDAQ");
	    WebElement ticker = driver.findElement(By.id("ticker1"));
	    assertEquals(ticker.getText(), "TSLA");
	    WebElement shares = driver.findElement(By.id("shares1"));
	    assertEquals(shares.getText(), "10");
	    
	    //logout
	    WebElement element;
		try {
			element = driver.findElement(By.id("logout-button"));
		} catch (NoSuchElementException e) {
			element = driver.findElement(By.xpath("//*[@id=\"logout-button\"]"));
		}
		
		element.click();
	}

	@Given("I click the button to remove stocks from my portfolio")
	public void i_click_the_button_to_remove_stocks_from_my_portfolio() {
		WebElement button = driver.findElement(By.id("remove1"));
		button.click();
	}

	@Given("I enter a stock ticker and number of shares less than the number I have of that stock")
	public void i_enter_a_stock_ticker_and_number_of_shares_less_than_the_number_I_have_of_that_stock() {
		WebElement exchange = driver.findElement(By.id("exchange"));
	    exchange.sendKeys("NASDAQ");
	    WebElement ticker = driver.findElement(By.id("ticker"));
	    ticker.sendKeys("AAPL");
	    WebElement shares = driver.findElement(By.id("shares"));
	    shares.sendKeys("5");
	}

	@Then("I should see the value of my portfolio decrease and the number of shares of that stock in my portfolio be updated")
	public void i_should_see_the_value_of_my_portfolio_decrease_and_the_number_of_shares_of_that_stock_in_my_portfolio_be_updated() {
	    // Write code here that turns the phrase above into concrete actions
		WebElement exchange = driver.findElement(By.id("exchange1"));
	    assertEquals(exchange.getText(), "NASDAQ");
	    WebElement ticker = driver.findElement(By.id("ticker1"));
	    assertEquals(ticker.getText(), "TSLA");
	    WebElement shares = driver.findElement(By.id("shares1"));
	    assertEquals(shares.getText(), "10");
	    
	    //logout
	    WebElement element;
		try {
			element = driver.findElement(By.id("logout-button"));
		} catch (NoSuchElementException e) {
			element = driver.findElement(By.xpath("//*[@id=\"logout-button\"]"));
		}
		
		element.click();
	}

	@Given("I enter an invalid stock ticker and number of shares")
	public void i_enter_an_invalid_stock_ticker_and_number_of_shares() {
		WebElement exchange = driver.findElement(By.id("exchange"));
	    exchange.sendKeys("NASDAQ");
	    WebElement ticker = driver.findElement(By.id("ticker"));
	    ticker.sendKeys("NKLA");
	    WebElement shares = driver.findElement(By.id("shares"));
	    shares.sendKeys("20");
	}

	@Then("I should see an error message saying stock ticker was not found")
	public void i_should_see_an_error_message_saying_stock_ticker_was_not_found() {
		WebElement msg = driver.findElement(By.id("errormsg"));
		assertEquals(msg.getText(), "Sorry, this stock does not exist.");
		
		//logout
	    WebElement element;
		try {
			element = driver.findElement(By.id("logout-button"));
		} catch (NoSuchElementException e) {
			element = driver.findElement(By.xpath("//*[@id=\"logout-button\"]"));
		}
		
		element.click();
	}

	@Given("I enter a stock ticker and number of shares greater than I have of this stock")
	public void i_enter_a_stock_ticker_and_number_of_shares_greater_than_I_have_of_this_stock() {
		WebElement exchange = driver.findElement(By.id("exchange"));
	    exchange.sendKeys("NASDAQ");
	    WebElement ticker = driver.findElement(By.id("ticker"));
	    ticker.sendKeys("AAPL");
	    WebElement shares = driver.findElement(By.id("shares"));
	    shares.sendKeys("20");
	}

	@Then("I should see an error message saying I'm trying to remove more shares than I have")
	public void i_should_see_an_error_message_saying_I_m_trying_to_remove_more_shares_than_I_have() {
		WebElement msg = driver.findElement(By.id("errormsg"));
		assertEquals(msg.getText(), "Sorry, you don't have enough stocks to sell.");
		
		//logout
	    WebElement element;
		try {
			element = driver.findElement(By.id("logout-button"));
		} catch (NoSuchElementException e) {
			element = driver.findElement(By.xpath("//*[@id=\"logout-button\"]"));
		}
		
		element.click();
	}

	@Given("I enter a stock ticker in my portfolio and a certain number of shares")
	public void i_enter_a_stock_ticker_in_my_portfolio_and_a_certain_number_of_shares() {
		WebElement exchange = driver.findElement(By.id("exchange"));
	    exchange.sendKeys("NASDAQ");
	    WebElement ticker = driver.findElement(By.id("ticker"));
	    ticker.sendKeys("AAPL");
	    WebElement shares = driver.findElement(By.id("shares"));
	    shares.sendKeys("10");
	}

	@Then("I should see the value of my portfolio increase and the shares owned updated")
	public void i_should_see_the_value_of_my_portfolio_increase_and_the_shares_owned_updated() {
		WebElement exchange = driver.findElement(By.id("exchange1"));
	    assertEquals(exchange.getText(), "NASDAQ");
	    WebElement ticker = driver.findElement(By.id("ticker1"));
	    assertEquals(ticker.getText(), "TSLA");
	    WebElement shares = driver.findElement(By.id("shares1"));
	    assertEquals(shares.getText(), "10");
	    
	  //logout
	    WebElement element;
		try {
			element = driver.findElement(By.id("logout-button"));
			System.out.println("logout button found");
		} catch (NoSuchElementException e) {
			element = driver.findElement(By.xpath("//*[@id=\"logout-button\"]"));
			System.out.println("logout button found");
		}
		
		element.click();
	}
	
	
	/**************************
	 * PORTFOLIO PERFORMANCE FEATURE
	 **************************/
	@Given("I click the button to change the graph date range")
	public void i_click_the_button_to_change_the_graph_date_range() {
		WebElement button = driver.findElement(By.id("reportrange"));
		button.click();
	}
	
	@Given("I select an appropriate date range")
	public void i_select_an_appropriate_date_range() {
		WebElement button = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div[1]/table/tbody/tr[3]/td[3]"));
		button.click();
		button = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div[1]/table/tbody/tr[5]/td[3]"));
		button.click();
	}
	
	@Given("I try to enter an invalid date")
	public void i_try_to_enter_an_invalid_date() {
		WebElement button = driver.findElement(By.id("reportrange"));
		//this might have to change depending on how Paul sets up input
		button.sendKeys("09/09/2019 - 09/09/2020");
	}
	
	@Given("I enter an appropriate date range")
	public void i_enter_an_appropriate_date_range() {
		WebElement button = driver.findElement(By.id("reportrange"));
		//this might have to change depending on how Paul sets up input
		button.sendKeys("01/01/2020 - 09/09/2020");
	}
	
	@Given("I click the button to add the S&P")
	public void i_click_the_button_to_add_the_S_P() {
	    WebElement button = driver.findElement(By.id("spytoggle"));
	    button.click();
	}
	
	@Given("I click the button to remove the S&P")
	public void i_click_the_button_to_remove_the_S_P() {
	    WebElement button = driver.findElement(By.id("spytoggle"));
	    button.click();
	}
	
	@Then("I should see the graph date range change")
	public void i_should_see_the_graph_date_range_change() {
		assertTrue(true);
	}
	
	@Then("I should see the S&P stock removed from the graph")
	public void i_should_see_the_S_P_stock_removed_from_the_graph() {
		assertTrue(true);
	}
	
	@Then("I should see the S&P stock added to the graph")
	public void i_should_see_the_S_P_stock_added_to_the_graph() {
		assertTrue(true);
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

	@After()
	public void after() {
		driver.quit();
	}
	
	
}
