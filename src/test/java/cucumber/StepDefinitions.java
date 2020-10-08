package cucumber;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.After;
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
	private static final String Notfound_URL = "http://localhost:8080/notfound.jsp";
	private static final String Predict_URL = "http://localhost:8080/predict.jsp";
	private static final String Portfolio_URL = "http://localhost:8080/portfolio_perf.jsp";

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
	
	//Landing Page Feature Tests
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
	
	@Then("I should be on the dashboard page")
	public void i_should_be_on_the_dashboard_page() {
	    try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/home.jsp"));
	}

	
	//Sign Up Feature
	@Given("I am on the sign up page")
	public void i_am_on_the_sign_up_page() {
		driver.get(Signup_URL);
	}

	@When("I enter a username")
	public void i_enter_a_username() {
		String testUser = getSaltString();
		driver.findElement(By.xpath("//*[@id=\"username\"]")).sendKeys(testUser);
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
	  assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/home.jsp"));
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

	@When("I enter an incorrect password")
	public void i_enter_an_incorrect_password() {
	  String usr = "test1234";
		driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(usr);
	}
	
	@Then("I should the error message {string}")
	public void i_should_the_error_message(String string) {
	  try {
			Thread.sleep(1000);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		String info = driver.findElement(By.id("login_error")).getText();
		
		assertTrue(info.equalsIgnoreCase(string));
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
	
	//Stock Performance Feature
	@Given("I am logged in on the stock performance page")
	public void i_am_logged_in_on_the_stock_performance_page() {
		i_am_on_the_login_page();
		i_enter_my_username();
		i_enter_my_password();
		i_click_the_login_button();
		driver.findElement(By.xpath("//*[@id=\"stockPerformance\"]")).click();
	}
	
	@When("I click the top banner")
	public void i_click_the_top_banner() {
		driver.findElement(By.xpath("//*[@id=\"banner-content\"]")).click();
	}
	
	@Then("I should be on the home page")
	public void i_should_be_on_the_home_page() {
		assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/home.jsp"));
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
	 * HOME FEATURE
	 **************************/
	@Given("I am logged in on the home page")
	public void i_am_logged_in_on_the_home_page() {
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
		driver.findElement(By.id("logout-button")).click();
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

	@When("I click the Portfolio Performance button")
	public void i_click_the_Portfolio_Performance_button() {
		driver.findElement(By.id("portfolio-performance")).click();
	}	

  @Then("I should be on the Portfolio Performance page")
	public void i_should_be_on_the_Portfolio_Performance_page() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertFalse(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/home.jsp"));
	}

	@When("I click the Portfolio Prediction button")
	public void i_click_the_Portfolio_Prediction_button() {
		driver.findElement(By.id("portfolio-prediction")).click();
	}

	@Then("I should be on the Portfolio Prediction page")
	public void i_should_be_on_the_Portfolio_Prediction_page() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertFalse(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/home.jsp"));
	}

	@When("I go to the Portfolio Value section")
	public void i_go_to_the_Portfolio_Value_section() {
		WebElement element = driver.findElement(By.id("main-content"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	@Then("I should see the total value of my stock portfolio")
	public void i_should_see_the_total_value_of_my_stock_portfolio() {
		WebElement element = driver.findElement(By.id("portfolio-value"));
	    assertTrue(element.getText().length() > 16);
	}

	@When("I go to the Portfolio section")
	public void i_go_to_the_Portfolio_section() {
		WebElement element = driver.findElement(By.id("portfolio"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	@Then("I should see a table of stocks in my portfolio.")
	public void i_should_see_a_table_of_stocks_in_my_portfolio() {		
		assertNotNull(driver.findElement(By.id("portfolio-table")));
	}
	
	@Given("I am logged in with an empty portfolio on the home page")
	public void i_am_logged_in_with_an_empty_portfolio_on_the_home_page() {
		String usr = "emptyPortfolio";
		String pw = "test123";
		
		driver.get(Login_URL);		
		driver.findElement(By.xpath("//*[@id=\"usrname\"]")).sendKeys(usr);
		driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(pw);
		driver.findElement(By.xpath("//*[@id=\"login-form-submit\"]")).click();
	}
	
	@Then("I should see the portfolio message {string}")
	public void i_should_see_the_portfolio_message(String string) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	    String mssg = driver.findElement(By.id("empty-portfolio-mssg")).getText();
	    assertTrue(mssg.equals(string));
	}

	/**************************
	 * PORTFOLIO PERFORMANCE FEATURE
	 **************************/
	@Given("I am logged in on the Portfolio Performance page")
	public void i_am_logged_in_on_the_Portfolio_Performance_page() {
	    // Write code here that turns the phrase above into concrete actions
		i_am_on_the_login_page();
		i_enter_my_username();
		i_enter_my_password();
		i_click_the_login_button();
		driver.get(Portfolio_URL);
		
	    //throw new io.cucumber.java.PendingException();
	}

	@When("I click the top banner of the Portfolio Performance page")
	public void i_click_the_top_banner_of_the_Portfolio_Performance_page() {
	    // Write code here that turns the phrase above into concrete actions
	    
		driver.findElement(By.xpath("//*[@id=\"banner-content\"]/a")).click();
		
		
		
		//throw new io.cucumber.java.PendingException();
	}

	@When("I click the 1 week button of the Portfolio Performance page.")
	public void i_click_the_drop_down_list_of_the_Portfolio_Performance_page() {
	    // Write code here that turns the phrase above into concrete actions
		
		driver.findElement(By.xpath("//*[@id=\"aweek\"]")).click();
		
	    //throw new io.cucumber.java.PendingException();
	}
	
	@When("I click the 1 month button of the Portfolio Performance page.")
	public void i_click_the_1m() {
	    // Write code here that turns the phrase above into concrete actions
		
		driver.findElement(By.xpath("//*[@id=\"amonth\"]")).click();
		
	    //throw new io.cucumber.java.PendingException();
	}
	
	@When("I click the 1 year button of the Portfolio Performance page.")
	public void i_click_the_1y() {
	    // Write code here that turns the phrase above into concrete actions
		
		driver.findElement(By.xpath("//*[@id=\"ayear\"]")).click();
		
	    //throw new io.cucumber.java.PendingException();
	}

	@Then("the graph should re-adjust on the Portfolio Performance page.")
	public void the_graph_should_re_adjust_on_the_Portfolio_Performance_page() {
	    // Write code here that turns the phrase above into concrete actions
		
		// not yet implemented 
		
		assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/portfolio_perf.jsp"));
		
	    //throw new io.cucumber.java.PendingException();
	}
	
  /**************************
	 * NOTFOUND FEATURE
	 **************************/
  @Given("I am on the notfound page") 
	public void i_am_on_the_notfound_page() {
		driver.get(Notfound_URL);
	}
	
	@When("I click the top banner in notfound page")
	public void i_click_the_top_banner_in_notfound_page() {
		WebElement topBanner = driver.findElement(By.xpath("//*[@id=\"banner-content\"]/a"));
		topBanner.click();
	}
	
	@When("I enter {string} in the search bar")
	public void i_enter_in_the_search_bar(String string) {
		WebElement searchBar = driver.findElement(By.xpath("/html/body/div/form/div/div/input"));
		searchBar.sendKeys(string);
		searchBar.sendKeys(Keys.ENTER);
	}
	
	@Then("I should be on the goog stock page")
	public void i_should_be_on_the_goog_stock_page() {
		assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/stock.jsp"));
	}
	
	@Then("I should be on the notfound page")
	public void i_should_be_on_the_notfound_page() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/notfound.jsp"));
	}
	
  /**************************
	 * PREDICT FEATURE
	 **************************/
	@Given("I am on the predict page")
	public void i_am_on_the_predict_page() {
		driver.get(Predict_URL);
	}
	
	@When("I choose a future date")
	public void i_choose_a_future_date() {
		WebElement date = driver.findElement(By.xpath("//*[@id=\"datepicker\"]/div/table/tbody/tr[4]/td[4]/a"));
		date.click();
	}
	
	@Then("I will see the predicted portfolio value")
	public void i_will_see_the_predicted_portfolio_value() {
		WebElement value = driver.findElement(By.xpath("//*[@id=\"portfolioValue\"]"));
		assertNotNull(value.getText());
	}
  
	@When("I click the top banner in predict page")
	public void i_click_the_top_banner_in_predict_page() {
		WebElement topBanner = driver.findElement(By.xpath("//*[@id=\"banner-content\"]/a"));
		topBanner.click();
	}
	
	@After()
	public void after() {
		driver.quit();
	}
	
}
