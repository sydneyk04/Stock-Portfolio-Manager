package cucumber;

import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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
		driver.get(Login_URL);
	}
	
	@When("I enter my username")
	public void i_enter_my_username() {
		String usr = "johnDoe";
		driver.findElement(By.xpath("//*[@id=\"login-form\"]/div[2]/input")).sendKeys(usr);
	}
	
	@When("I enter my password")
	public void i_enter_my_password() {
	    String usr = "test123";
		driver.findElement(By.xpath("//*[@id=\"login-form\"]/div[3]/input")).sendKeys(usr);
	}
	
	@When("I click the login button")
	public void i_click_the_login_button() {
	    driver.findElement(By.xpath("//*[@id=\"login-form-submit\"]")).click();
	}
	
	@Then("I should be on the dashboard page")
	public void i_should_be_on_the_dashboard_page() {
	    try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/home.jsp"));
	}

	@When("I enter an incorrect password")
	public void i_enter_an_incorrect_password() {
	    String usr = "test1234";
		driver.findElement(By.xpath("//*[@id=\"login-form\"]/div[3]/input")).sendKeys(usr);
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
	
	@Then("I should the alert {string}")
	public void i_should_the_alert(String string) {
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
