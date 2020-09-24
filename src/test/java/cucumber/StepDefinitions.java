package cucumber;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.Assert.assertTrue;

/**
 * Step definitions for Cucumber tests.
 */
public class StepDefinitions {
	private static final String ROOT_URL = "http://localhost:8080/";
	private static final String Login_URL = "http://localhost:8080/login.jsp";

	private final WebDriver driver = new ChromeDriver();

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
	    // Write code here that turns the phrase above into concrete actions
		
		driver.get(Login_URL);
		
	    //throw new io.cucumber.java.PendingException();
	}
	
	@When("I enter my username")
	public void i_enter_my_username() {
	    // Write code here that turns the phrase above into concrete actions
		
		String usr = "johnDoe";
		driver.findElement(By.xpath("//*[@id=\"login-form\"]/div[2]/input")).sendKeys(usr);
		
		
		
	    //throw new io.cucumber.java.PendingException();
	}
	@When("I enter my password")
	public void i_enter_my_password() {
	    // Write code here that turns the phrase above into concrete actions
		
		String usr = "test123";
		driver.findElement(By.xpath("//*[@id=\"login-form\"]/div[3]/input")).sendKeys(usr);
		
	    //throw new io.cucumber.java.PendingException();
	}
	@When("I click the login button")
	public void i_click_the_login_button() {
	    // Write code here that turns the phrase above into concrete actions
		
		driver.findElement(By.xpath("//*[@id=\"login-form-submit\"]")).click();
		
	    //throw new io.cucumber.java.PendingException();
	}
	@Then("I should be on the dashboard page")
	public void i_should_be_on_the_dashboard_page() {
	    // Write code here that turns the phrase above into concrete actions
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/home.jsp"));
		
	    //throw new io.cucumber.java.PendingException();
	}

	@When("I enter an incorrect password")
	public void i_enter_an_incorrect_password() {
	    // Write code here that turns the phrase above into concrete actions
		
		String usr = "test1234";
		driver.findElement(By.xpath("//*[@id=\"login-form\"]/div[3]/input")).sendKeys(usr);
		
	    //throw new io.cucumber.java.PendingException();
	}
	
	@Then("I should the error message {string}")
	public void i_should_the_error_message(String string) {
	    // Write code here that turns the phrase above into concrete actions
		
		try {
			Thread.sleep(1000);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//String info = driver.switchTo().alert().getText();
		
		String info = driver.findElement(By.id("login_error")).getText();
		
		
		assertTrue(info.equalsIgnoreCase(string));
		
	    //throw new io.cucumber.java.PendingException();
	}
	
	@Then("I should the alert {string}")
	public void i_should_the_alert(String string) {
	    // Write code here that turns the phrase above into concrete actions
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/login.jsp"));
		
		//String info = driver.switchTo().alert().getText();
		
		//assertTrue(info.equalsIgnoreCase(string));
		
	    //throw new io.cucumber.java.PendingException();
	}
	
	
	@After()
	public void after() {
		driver.quit();
	}
}
