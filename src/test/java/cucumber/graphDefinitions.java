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
public class graphDefinitions {
	
	private final WebDriver driver = new ChromeDriver();
	
	@Given("I am on dashboard")
	public void i_am_on_dashbaord() {
		driver.get("http://localhost:8080/production/index.jsp");
	}
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
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}
	@Then("I should see the S&P stock removed from the graph")
	public void i_should_see_the_S_P_stock_removed_from_the_graph() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}
	@Then("I should see the S&P stock added to the graph")
	public void i_should_see_the_S_P_stock_added_to_the_graph() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}
}
