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
public class landingpageDefinitions {
	
	private final WebDriver driver = new ChromeDriver();
	
	//log in tests
	@Given("I am on the landing page")
	public void i_am_on_the_landing_page() {
		driver.get("http://localhost:8080/");
	}
	@When("I click the {String} button")
	public void i_click_the_button(String button) {
		WebElement signup = driver.findElement(By.id("signup_button"));
		WebElement login = driver.findElement(By.id("login_button"));
		
		if(button.equals("Sign Up")) {
			signup.click();
		}
		else {
			login.click();
		}
	    
	}
	@Then("I should be brought to the {String} page")
	public void i_should_be_brought_to_the_page(String page){
		String currentURL = driver.getCurrentUrl();
		
		if(page.equals("Sign up")) {
			assertTrue(currentURL.contains("signup"));
		}
		else {
			assertTrue(currentURL.contains("login"));
		}
		
	}

}
