package cucumber;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Step definitions for Cucumber tests.
 */
public class tickerAddRemoveDefinitions {
	private static final String ROOT_URL = "http://localhost:8080/";

	private final WebDriver driver = new ChromeDriver();

	@Given("I am on dashboard")
	public void i_am_on_dashboard() {
		driver.get("https://www.index.com/index/");
	}

	@Given("I click the button to add stocks to my portfolio")
	public void i_click_the_button_to_add_stocks_to_my_portfolio() {
		WebElement button = driver.findElement(By.id("add"));
		button.click();
	}

	@Given("I enter a stock ticker not in my portfolio and a certain number of shares")
	public void i_enter_a_stock_ticker_not_in_my_portfolio_and_a_certain_number_of_shares() {
	    WebElement exchange = driver.findElement(By.id("exchange"));
	    exchange.sendKeys("NASDAQ");
	    WebElement ticker = driver.findElement(By.id("ticker"));
	    ticker.sendKeys("TSLA");
	    WebElement shares = driver.findElement(By.id("shares"));
	    shares.sendKeys("10");
	}

	@When("I click the submit button")
	public void i_click_the_submit_button() {
		WebElement submit = driver.findElement(By.id("stockaddbutton"));
	    submit.click();;
	}

	@Then("I should see the value of my portfolio increase and the stocks in my portfolio be updated")
	public void i_should_see_the_value_of_my_portfolio_increase_and_the_stocks_in_my_portfolio_be_updated() {
		WebElement exchange = driver.findElement(By.id("exchange1"));
	    assertEquals(exchange.getText(), "NASDAQ");
	    WebElement ticker = driver.findElement(By.id("ticker1"));
	    assertEquals(ticker.getText(), "TSLA");
	    WebElement shares = driver.findElement(By.id("shares1"));
	    assertEquals(shares.getText(), "10");
	}

	@Given("I click the button to remove stocks from my portfolio")
	public void i_click_the_button_to_remove_stocks_from_my_portfolio() {
		WebElement button = driver.findElement(By.id("remove1"));
		button.click();
	}

	@Given("I enter a stock ticker and number of shares less than the number I have of that stock")
	public void i_enter_a_stock_ticker_and_number_of_shares_less_than_the_number_I_have_of_that_stock() {
		WebElement exchange = driver.findElement(By.id("exchange"));
	    exchange.sendKeys("NASDAQ");
	    WebElement ticker = driver.findElement(By.id("ticker"));
	    ticker.sendKeys("AAPL");
	    WebElement shares = driver.findElement(By.id("shares"));
	    shares.sendKeys("5");
	}

	@Then("I should see the value of my portfolio decrease and the number of shares of that stock in my portfolio be updated")
	public void i_should_see_the_value_of_my_portfolio_decrease_and_the_number_of_shares_of_that_stock_in_my_portfolio_be_updated() {
	    // Write code here that turns the phrase above into concrete actions
		WebElement exchange = driver.findElement(By.id("exchange1"));
	    assertEquals(exchange.getText(), "NASDAQ");
	    WebElement ticker = driver.findElement(By.id("ticker1"));
	    assertEquals(ticker.getText(), "TSLA");
	    WebElement shares = driver.findElement(By.id("shares1"));
	    assertEquals(shares.getText(), "10");
	}

	@Given("I enter an invalid stock ticker and number of shares")
	public void i_enter_an_invalid_stock_ticker_and_number_of_shares() {
		WebElement exchange = driver.findElement(By.id("exchange"));
	    exchange.sendKeys("NASDAQ");
	    WebElement ticker = driver.findElement(By.id("ticker"));
	    ticker.sendKeys("NKLA");
	    WebElement shares = driver.findElement(By.id("shares"));
	    shares.sendKeys("20");
	}

	@Then("I should see an error message saying stock ticker was not found")
	public void i_should_see_an_error_message_saying_stock_ticker_was_not_found() {
		WebElement msg = driver.findElement(By.id("errormsg"));
		assertEquals(msg.getText(), "Sorry, this stock does not exist.");
	}

	@Given("I enter a stock ticker and number of shares greater than I have of this stock")
	public void i_enter_a_stock_ticker_and_number_of_shares_greater_than_I_have_of_this_stock() {
		WebElement exchange = driver.findElement(By.id("exchange"));
	    exchange.sendKeys("NASDAQ");
	    WebElement ticker = driver.findElement(By.id("ticker"));
	    ticker.sendKeys("AAPL");
	    WebElement shares = driver.findElement(By.id("shares"));
	    shares.sendKeys("20");
	}

	@Then("I should see an error message saying I'm trying to remove more shares than I have")
	public void i_should_see_an_error_message_saying_I_m_trying_to_remove_more_shares_than_I_have() {
		WebElement msg = driver.findElement(By.id("errormsg"));
		assertEquals(msg.getText(), "Sorry, you don't have enough stocks to sell.");
	}

	@Given("I enter a stock ticker in my portfolio and a certain number of shares")
	public void i_enter_a_stock_ticker_in_my_portfolio_and_a_certain_number_of_shares() {
		WebElement exchange = driver.findElement(By.id("exchange"));
	    exchange.sendKeys("NASDAQ");
	    WebElement ticker = driver.findElement(By.id("ticker"));
	    ticker.sendKeys("AAPL");
	    WebElement shares = driver.findElement(By.id("shares"));
	    shares.sendKeys("10");
	}

	@Then("I should see the value of my portfolio increase and the shares owned updated")
	public void i_should_see_the_value_of_my_portfolio_increase_and_the_shares_owned_updated() {
		WebElement exchange = driver.findElement(By.id("exchange1"));
	    assertEquals(exchange.getText(), "NASDAQ");
	    WebElement ticker = driver.findElement(By.id("ticker1"));
	    assertEquals(ticker.getText(), "TSLA");
	    WebElement shares = driver.findElement(By.id("shares1"));
	    assertEquals(shares.getText(), "10");
	}
}
