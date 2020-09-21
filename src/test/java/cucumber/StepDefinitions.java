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
	
	@Given("I am on the landing page")
	public void i_am_on_the_landing_page() {
		driver.get(ROOT_URL);
	}

	@When("I click the {string} button")
	public void i_click_the_string_buttton(String linkText)
	{
		
		if(linkText.equals("Login"))
			driver.findElement(By.xpath("/html/body/div/a[2]")).click();
		else if(linkText.equalsIgnoreCase("Sign Up"))
			driver.findElement(By.xpath("/html/body/div/a[1]")).click();
		
		
	}
	
	@Then("I should be brought to the login page")
	public void i_should_login()
	{
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(driver.getCurrentUrl());
		String url = driver.getCurrentUrl();
		assertTrue(url.equalsIgnoreCase("http://localhost:8080/login.jsp"));
	}
	
	@Then("I should be brought to the sign up page")
	public void i_should_signup()
	{
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(driver.getCurrentUrl());
		String url = driver.getCurrentUrl();
		assertTrue(url.equalsIgnoreCase("http://localhost:8080/signup.jsp"));
	}
	
	
	
	
	@After()
	public void after() {
		driver.quit();
	}
}
