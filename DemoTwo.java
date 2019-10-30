package selenium;

//Imports for java tools
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.Duration;

//Imports for selenium
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver; //Need to import for each browser you intend to use
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

/* In this demo we will go to Yahoo's Finance web site and scrap some stock data so
 * that we can calculate some numbers to make our next position on a stock. */
public class DemoThree {
	
	/* @var TIMEOUT --> Total time until giving up locating an element (seconds)
	 * @var POLLING_RATE --> Attempt to find element at this rate (seconds)*/
	static int TIMEOUT = 30;
	static int POLLING_RATE = 1;
	
	/* Helper function
	 * Creates a 'wait' that will check every @POLLING_RATE, for a total duration of @TIMEOUT
	 * until the elements loads onto the DOM through 'visibilityOfElementLocated'.
	 * Lastly, returns the webElement once it is found.
	 * 
	 * */

	public static WebElement getXPathEle(WebDriver driver, String ele) {
		
		//Initialize our wait driver
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(TIMEOUT)).pollingEvery(Duration.ofSeconds(POLLING_RATE)).ignoring(NoSuchElementException.class);
		
		//Wait until element is visible on DOM
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ele)));
		
		//Return our element
		return driver.findElement(By.xpath(ele));
	}
	
	public static List<WebElement> getClassEles(WebDriver driver, String ele) {
		
		//Initialize our wait driver
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(TIMEOUT)).pollingEvery(Duration.ofSeconds(POLLING_RATE)).ignoring(NoSuchElementException.class);
		
		//Wait until element is visible on DOM
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(ele)));
			
		//Return our List of elements
		return driver.findElements(By.className(ele));
	}
	
	public static void gatherStockData(WebDriver driver) {
				
		/*Local Variable
		 * @var dataMap --> Dictionary of all data on page, consist of a key[stock symbol],
		 * 					and values [stock name, price, change, % change, volume, avg vol, market cap, pe ratio]
		 */
		Map<String, List<String>> dataMap = new HashMap<String, List<String>>();
		
		/*Begin webscraping 
		 * Gather all of the stock symbols, names, and financial data of the top ~100 stocks that gained today... 
		 * We can use the WebDriver function 'findElements' to return an ArrayList of all WebElements
		 * found by a method of our choice, in this case 'className & xpath'
		 * 
		 * For our dictionary we want to...
		 * Add a key --> stock symbol
		 * Add values --> stock name, price, change, % change, volume, avg vol, market cap, pe ratio
		 * */
		
		//Get the number of elements we need to find
		//int elementsLength = driver.findElements(By.className("SimpleDataTableRow")).size();
		int elementsLength = getClassEles(driver, "SimpleDataTableRow").size();
		
		//Get the number of header elements (We subtract 2 because we don't need the Symbol or 52 Week Range
		int headersLength = driver.findElements(By.xpath("//*[@id='scr-res-table']/div[1]/table/thead/tr").tagName("th")).size()-2;
		
		//Iterate through number of elements to get a dictionary key and it's values
		for(int i=1; i<elementsLength; i++) {
			
			//Initialize a new list for each element
			List<String> values = new ArrayList<String>();
			
			//Grab our current element text by xpath and using i as our way to keep track of where we are
			String key = getXPathEle(driver, "//*[@id='scr-res-table']/div[2]/table/tbody/tr["+i+"]/td[1]").getText();
			
			//Iterate through the headers to grab the values of each elements (starts at 2 'name', ends at 8 'PE Ratio)
			for(int j=2; j<=headersLength; j++) {
				values.add(getXPathEle(driver,"//*[@id='scr-res-table']/div[2]/table/tbody/tr["+i+"]/td["+j+"]").getText());
			}
			
			//Let's watch it gather some data!
			System.out.println(values);
			
			//Add the key & values to the data map
			dataMap.put(key, values);
		}
		
		/* Now we have a dictionary that has a key[stock symbol] and values if we want to access specific stocks or all stocks to 
		 * crunch numbers for our next investment*/
		System.out.println("\n" + dataMap);
	}

	public static void main(String[] args) {
		
			/* Browser does not have a built-in server to run the automation code, we need to give it one in order to
			 * communicate our Selenium code to the browser. This needs to be done before our driver is initialized or
			 * before and selenium command takes place.
			 * Also remember to escape backslashes in your file path.*/
			System.setProperty("webdriver.chrome.driver", "D:\\Java Programming\\selenium-java-3.141.59\\chromedriver.exe");
	
			//Initialize our web driver, this allows us to utilize Selenium's APIs methods
			WebDriver driver = new ChromeDriver();
			
			/* get - Navigates to a given url 
			 * must add http(s) in order to navigate to url*/
			System.out.println("Navigating to URL...\n");
			driver.get("https://finance.yahoo.com/gainers?offset=0&count=100");
			
			//Start webscraping
			System.out.println("Finding data on page...\n");
			gatherStockData(driver);
		
			System.out.println("\n Navigating to top stock losers...");
			
			//Navigate to the ~100 losers
			driver.get("https://finance.yahoo.com/losers?offset=0&count=100");
			
			//Start webscraping
			System.out.println("Finding data on page...\n");
			gatherStockData(driver);
			
			System.out.println("\n Automation has ended.");
	}
}
