package lambdatest;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LambdaTestScenario {

    // ThreadLocal to hold WebDriver and WebDriverWait for each thread
    private static ThreadLocal<RemoteWebDriver> driverThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<WebDriverWait> waitThreadLocal = new ThreadLocal<>();

    private RemoteWebDriver getDriver() {
        return driverThreadLocal.get();
    }

    private WebDriverWait getWait() {
        return waitThreadLocal.get();
    }

    @BeforeClass
    @Parameters({"browserName", "browserVersion", "platformName"})
    public void setup(String browserName, String browserVersion, String platformName) throws MalformedURLException {
        String username = "adarshadash";
        String authkey = "lZJ6AQAB5VLZ94LfzZ8FdF8HCRugJAevfa3Oh5XImHKYLlM4RO";
        String hub = "https://@hub.lambdatest.com/wd/hub";

        Map<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("user", username);
        ltOptions.put("accessKey", authkey);
        ltOptions.put("build", "Selenium 4 Framework Build");
        ltOptions.put("name", browserName + " Test");
        ltOptions.put("network", true);
        ltOptions.put("console", true);
        ltOptions.put("video", true);
        ltOptions.put("visual", true);
        ltOptions.put("plugin", "java-testNG");

        RemoteWebDriver driver;

        // Browser-specific options
        if ("chrome".equalsIgnoreCase(browserName)) {
            ChromeOptions options = new ChromeOptions();
            options.setPlatformName(platformName);
            options.setBrowserVersion(browserVersion);
            options.setCapability("LT:Options", ltOptions);
            driver = new RemoteWebDriver(new URL(hub), options);
        } else if ("firefox".equalsIgnoreCase(browserName)) {
            FirefoxOptions options = new FirefoxOptions();
            options.setPlatformName(platformName);
            options.setBrowserVersion(browserVersion);
            options.setCapability("LT:Options", ltOptions);
            driver = new RemoteWebDriver(new URL(hub), options);
        } else if ("MicrosoftEdge".equalsIgnoreCase(browserName)) {
            EdgeOptions options = new EdgeOptions();
            options.setPlatformName(platformName);
            options.setBrowserVersion(browserVersion);
            options.setCapability("LT:Options", ltOptions);
            driver = new RemoteWebDriver(new URL(hub), options);
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }

        // Set WebDriver and WebDriverWait in ThreadLocal
        driverThreadLocal.set(driver);
        waitThreadLocal.set(new WebDriverWait(driver, Duration.ofSeconds(20)));

        // Print LambdaTest Session ID (Test ID)
        String sessionId = driver.getSessionId().toString();
        System.out.println("LambdaTest Session ID: " + sessionId + "Click on the URl to view test session -"+ "https://automation.lambdatest.com/logs/?sessionID="+sessionId);
    }

    @Test
    public void testScenario() {
        RemoteWebDriver driver = getDriver();
        WebDriverWait wait = getWait();

        // Step 1: Navigate to the URL
        driver.get("https://www.lambdatest.com");

        // Step 2: Wait for all elements to load in the DOM
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("body")));

        // Step 3: Scroll to 'Explore all Integrations'
        WebElement exploreIntegrations = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Explore all Integrations')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", exploreIntegrations);

        // Step 4: Open the link in a new tab using JavaScript
        String linkHref = exploreIntegrations.getAttribute("href");
        ((JavascriptExecutor) driver).executeScript("window.open(arguments[0], '_blank');", linkHref);

        // Step 5: Save window handles and print them
        List<String> windowHandles = new ArrayList<>(driver.getWindowHandles());
        System.out.println("Window Handles: " + windowHandles);

        // Switch to the new tab
        driver.switchTo().window(windowHandles.get(1));

        // Step 6: Verify the URL
        String expectedUrl = "https://www.lambdatest.com/integrations";
        String actualUrl = driver.getCurrentUrl();
        Assert.assertEquals(actualUrl, expectedUrl, "URL mismatch!");

        // Other test steps...

        // Step 14: Close the browser
        driver.quit();
    }

    @AfterClass
    public void teardown() {
        RemoteWebDriver driver = getDriver();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            waitThreadLocal.remove();
        }
    }
}
