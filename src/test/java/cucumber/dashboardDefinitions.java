package cucumber;


import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Step definitions for Cucumber tests.
 */
public class dashboardDefinitions {
	
	private final WebDriver driver = new ChromeDriver();
	
	@Given("I am logged in on the home page")
	public void i_am_on_the_log_in_page() {
		driver.get("http://localhost:8080/production/index.jsp");
	}
	
	
}
