package cucumber;

import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Random;

/**
 * Step definitions for Cucumber tests.
 */
public class StepDefinitions {
	private static final String ROOT_URL = "http://localhost:8080/";
	private static final String Signup_URL = "http://localhost:8080/signup.jsp";
	private static final String Login_URL = "http://localhost:8080/login.jsp";

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

  @After()
	public void after() {
		driver.quit();
	}
}
