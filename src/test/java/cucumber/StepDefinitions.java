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

import java.util.Random;

/**
 * Step definitions for Cucumber tests.
 */
public class StepDefinitions {
	private static final String ROOT_URL = "http://localhost:8080/";
	private static final String Signup_URL = "http://localhost:8080/signup.jsp";

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

	@Given("I am on the sign up page")
	public void i_am_on_the_sign_up_page() {
	    // Write code here that turns the phrase above into concrete actions
		
		driver.get(Signup_URL);
		
	    //throw new io.cucumber.java.PendingException();
	}

	@When("I enter an email")
	public void i_enter_an_email() {
	    // Write code here that turns the phrase above into concrete actions
		
		String temp_email = getSaltString();
		
		temp_email = temp_email + "@gmail.com";
		
		driver.findElement(By.xpath("//*[@id=\"username\"]")).sendKeys(temp_email);
		
	    //throw new io.cucumber.java.PendingException();
	}

	@When("I enter the first hidden password field")
	public void i_enter_the_first_hidden_password_field() {
	    // Write code here that turns the phrase above into concrete actions
		
		String temp_pass = getSaltString();
		
		entered_pass = temp_pass;
		
		driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(temp_pass);
		
	    //throw new io.cucumber.java.PendingException();
	}

	@When("I enter the second hidden password field")
	public void i_enter_the_second_hidden_password_field() {
	    // Write code here that turns the phrase above into concrete actions
		
		driver.findElement(By.xpath("//*[@id=\"password2\"]")).sendKeys(entered_pass);
		
	    //throw new io.cucumber.java.PendingException();
	}

	@When("I click the sign up button")
	public void i_click_the_sign_up_button() {
	    // Write code here that turns the phrase above into concrete actions
		
		driver.findElement(By.xpath("/html/body/form/button")).click();
		
		try {
			Thread.sleep(1000);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	    //throw new io.cucumber.java.PendingException();
	}

	@Then("I should be brought to the dashboard page")
	public void i_should_be_brought_to_the_dashboard_page() {
	    // Write code here that turns the phrase above into concrete actions
		
		assertTrue(driver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/home.jsp"));
		
		
	    //throw new io.cucumber.java.PendingException();
	}



	@When("I leave a password field blank")
	public void i_leave_a_password_field_blank() {
	    // Write code here that turns the phrase above into concrete actions
		
		
	    //throw new io.cucumber.java.PendingException();
	}

	@Then("I should see alert: {string}")
	public void i_should_see_alert(String string) {
	    // Write code here that turns the phrase above into concrete actions
		
		System.out.println("what should be there: " + string);
		
		System.out.println("what was there: " + driver.findElement(By.id("error")).getText());
		
		assertTrue(driver.findElement(By.id("error")).getText().equalsIgnoreCase(string));
		
		
		
	    //throw new io.cucumber.java.PendingException();
	}


	@When("I enter an invalid email")
	public void i_enter_an_invalid_email() {
	    // Write code here that turns the phrase above into concrete actions
		String temp_email = getSaltString();
		
		driver.findElement(By.xpath("//*[@id=\"username\"]")).sendKeys(temp_email);
		
		
	    //throw new io.cucumber.java.PendingException();
	}



	@Then("I should the alert {string}")
	public void i_should_the_alert(String string) {
	    // Write code here that turns the phrase above into concrete actions
		
		assertTrue(driver.findElement(By.xpath("//*[@id=\"error\"]") ).getText().equalsIgnoreCase(string));
		
		
	    //throw new io.cucumber.java.PendingException();
	}




	@When("I enter the second hidden password field incorrectly")
	public void i_enter_the_second_hidden_password_field_incorrectly() {
	    // Write code here that turns the phrase above into concrete actions
		
		String temp_pass = getSaltString();
		
		
		driver.findElement(By.xpath("//*[@id=\"password2\"]")).sendKeys(temp_pass);
		
		
	    //throw new io.cucumber.java.PendingException();
	}

	
	@After()
	public void after() {
		driver.quit();
	}
}
