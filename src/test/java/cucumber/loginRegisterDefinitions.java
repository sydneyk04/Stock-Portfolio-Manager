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
public class loginRegisterDefinitions {
	
	private final WebDriver driver = new ChromeDriver();
	private String validUsername = "ashott";
	private String invalidUsername = "invalid";
	private String invalidPassword = "invalid";
	private String validPassword = "41325";
	private String notregisteredUsername = "notRegistered";
	private String takenUsername = "ashott";
	
	//log in tests
	@Given("I am on the log in page")
	public void i_am_on_the_log_in_page() {
		driver.get("http://localhost:8080/login.jsp");
	}
	@Given("I input my correct username and password")
	public void i_input_my_correct_username_and_password() {
		WebElement username = driver.findElement(By.id("username"));
	    username.sendKeys(validUsername);
	    WebElement password = driver.findElement(By.id("password"));
	    password.sendKeys(validPassword);
	}
	@Given("I input my correct username but an incorrect password")
	public void i_input_my_correct_username_but_an_incorrect_password() {
		WebElement username = driver.findElement(By.id("username"));
	    username.sendKeys(validUsername);
	    WebElement password = driver.findElement(By.id("password"));
	    password.sendKeys(invalidPassword);
	}
	@Given("I input a username not registered and a password")
	public void i_input_a_username_not_registered_and_a_password() {
		WebElement username = driver.findElement(By.id("username"));
	    username.sendKeys(notregisteredUsername);
	    WebElement password = driver.findElement(By.id("password"));
	    password.sendKeys(validPassword);
	}
	@When("I click the login button")
	public void i_click_the_login_button() {
		WebElement login = driver.findElement(By.id("login"));
		login.click();
	}
	@Then("I should see a message redirecting me to the signup page")
	public void i_should_see_a_message_redirecting_me_to_the_signup_page() {
		WebElement message = driver.findElement(By.id("errorMessage"));
		assertTrue(message.getText().contains("redirect"));
	}
	@Then("I should see an error message saying I've entered an incorrect password")
	public void i_should_see_an_error_message_saying_I_ve_entered_an_incorrect_password() {
		WebElement message = driver.findElement(By.id("errorMessage"));
		assertTrue(message.getText().contains("incorrect password"));
	}
	@Then("I should see my portfolio dashboard")
	public void i_should_see_my_portfolio_dashboard() {
	    String currentURL = driver.getCurrentUrl();
	    assertTrue(currentURL.contains("dashboard"));
	}
	
	//register tests
	@Given("I am on the register page")
	public void i_am_on_the_register_page() {
		driver.get("http://localhost:8080/signup.jsp");
	}
	@Given("I type in an already used username and the password and confirmation password")
	public void i_type_in_an_already_used_username_and_the_password_and_confirmation_password() {
		WebElement username = driver.findElement(By.id("username"));
	    username.sendKeys(takenUsername);
	    WebElement password = driver.findElement(By.id("password"));
	    password.sendKeys(validPassword);
	    WebElement confPassword = driver.findElement(By.id("confirm-input"));
	    confPassword.sendKeys(validPassword);
	}
	@Given("I type in a unique username and a password")
	public void i_type_in_a_unique_username_and_a_password() {
		WebElement username = driver.findElement(By.id("username"));
	    username.sendKeys(validUsername);
	    WebElement password = driver.findElement(By.id("password"));
	    password.sendKeys(validPassword);
	    WebElement confPassword = driver.findElement(By.id("confirm-input"));
	    confPassword.sendKeys(validPassword);
	}
	@Given("I type in the confirmation password incorrectly")
	public void i_type_in_the_confirmation_password_incorrectly() {
		WebElement username = driver.findElement(By.id("username"));
	    username.sendKeys(validUsername);
	    WebElement password = driver.findElement(By.id("password"));
	    password.sendKeys(validPassword);
	    WebElement confPassword = driver.findElement(By.id("confirm-input"));
	    confPassword.sendKeys(invalidPassword);
	}
	@When("I click the register button")
	public void i_click_the_register_button() {
	    WebElement register = driver.findElement(By.id("submit-button"));
	    register.click();
	}
	@Then("I should see a message saying that my username is already taken")
	public void i_should_see_a_message_saying_that_my_username_is_already_taken() {
		WebElement message = driver.findElement(By.id("errorMessage"));
		assertTrue(message.getText().contains("taken"));
	}
	@Then("I should see a message saying that my passwords don't match")
	public void i_should_see_a_message_saying_that_my_passwords_don_t_match() {
		WebElement message = driver.findElement(By.id("errorMessage"));
		assertTrue(message.getText().contains("match"));
	}
	@After()
	public void cleanup() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.quit();
	}
}
