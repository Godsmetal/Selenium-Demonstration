package MyPackage;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class DemoOne {
	public static void main(String[] args) {

		//Set Chromedriver
		System.setProperty("webdriver.chrome.driver", "PATH TO CHROMEDRIVER");
		//Create Instance of ChromeDriver
		WebDriver driver = new ChromeDriver();
			
		//Go to http://www.google.com
		driver.get("http://www.google.com");
		//Get the form Element
		WebElement element = driver.findElement(By.name("q"));
		//Go to Enter RIT into search bar
		element.sendKeys("RIT");
		//Send search
		element.submit();
			
		//Get the search results element
		WebElement rit= driver.findElement(By.id("search"));
		//Get all the link elements inside of the search results element
		List<WebElement> results = rit.findElements(By.tagName("a"));
		//Print name of the result
		System.out.println(results.get(0).getText());
		//Click on the result
		results.get(0).click();
		//Print title of the page
		System.out.println(driver.getTitle());

		//Quit session and close windows
		driver.quit();
	}
}
