package lambdatest;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LambdaTestScenario {
    private RemoteWebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    @Parameters({"browserName", "browserVersion", "platformName"})
    public void setup( String browserName, String browserVersion, String platformName) throws MalformedURLException {
        String username = "adarshadash"; //System.getenv("LT_USERNAME") == null ? "adarshadash" : System.getenv("LT_USERNAME");
        String authkey = "lZJ6AQAB5VLZ94LfzZ8FdF8HCRugJAevfa3Oh5XImHKYLlM4RO" ;//System.getenv("LT_ACCESS_KEY") == null ? "lZJ6AQAB5VLZ94LfzZ8FdF8HCRugJAevfa3Oh5XImHKYLlM4RO" : System.getenv("LT_ACCESS_KEY");

        /*
        Steps to run Smart UI project (https://beta-smartui.lambdatest.com/)
        Step - 1 : Change the hub URL to @beta-smartui-hub.lambdatest.com/wd/hub
        Step - 2 : Add "smartUI.project": "<Project Name>" as a capability above
        Step - 3 : Add "((JavascriptExecutor) driver).executeScript("smartui.takeScreenshot");" code wherever you need to take a screenshot
        Note: for additional capabilities navigate to https://www.lambdatest.com/support/docs/test-settings-options/
        */

        String hub = "@hub.lambdatest.com/wd/hub";

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

        // Browser-specific options
        if ("chrome".equalsIgnoreCase(browserName)) {
            ChromeOptions options = new ChromeOptions();
            options.setPlatformName(platformName);
            options.setBrowserVersion(browserVersion);
            options.setCapability("LT:Options", ltOptions);
            driver = new RemoteWebDriver(new URL("https://" + username + ":" + authkey + "@hub.lambdatest.com/wd/hub"), options);
        } else if ("firefox".equalsIgnoreCase(browserName)) {
            FirefoxOptions options = new FirefoxOptions();
            options.setPlatformName(platformName);
            options.setBrowserVersion(browserVersion);
            options.setCapability("LT:Options", ltOptions);
            driver = new RemoteWebDriver(new URL("https://" + username + ":" + authkey + "@hub.lambdatest.com/wd/hub"), options);
        } else if ("MicrosoftEdge".equalsIgnoreCase(browserName)) {
            EdgeOptions options = new EdgeOptions();
            options.setPlatformName(platformName);
            options.setBrowserVersion(browserVersion);
            options.setCapability("LT:Options", ltOptions);
            driver = new RemoteWebDriver(new URL("https://" + username + ":" + authkey + "@hub.lambdatest.com/wd/hub"), options);
        }else {
            throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        // Print LambdaTest Session ID (Test ID)
        String sessionId = driver.getSessionId().toString();
        System.out.println("LambdaTest Session ID: " + sessionId);

    }


    @Test
    public void testScenario() {
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

        // Step 7: Scroll to 'Codeless Automation'
        WebElement codelessAutomation = driver.findElement(By.xpath("//h2[contains(text(), 'Codeless Automation')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", codelessAutomation);

        // Step 8: Click on 'INTEGRATE TESTING WHIZ WITH LAMBDATEST'
        WebElement testingWhizLink = driver.findElement(By.linkText("INTEGRATE TESTING WHIZ WITH LAMBDATEST"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", testingWhizLink);
        String linkHrefWhiz = testingWhizLink.getAttribute("href");
        driver.get(linkHrefWhiz);
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("body")));

        // Step 9: Verify the page title
        // Initialize SoftAssert
        SoftAssert softAssert = new SoftAssert();
        String expectedTitle = "TestingWhiz Integration With LambdaTest";
        String actualTitle = driver.getTitle();
        softAssert.assertEquals(actualTitle, expectedTitle,
                "Page title does not match. Expected: " + expectedTitle + ", but found: " + actualTitle);

        // Step 10: Close the current window
        driver.close();

        // Step 11: Print the current window count
        driver.switchTo().window(windowHandles.get(0));
        System.out.println("Current window count: " + driver.getWindowHandles().size());

        // Step 12: Set URL to the blog
        driver.get("https://www.lambdatest.com/blog");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("body")));

        // Step 13: Click on the 'Community' link and verify URL
        WebElement communityLink = driver.findElement(By.xpath("//a[contains(@href,'community.lambdatest.com')]/parent::li[contains(@id,'menu-item')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", communityLink);


        // Perform the click action
        Actions actions = new Actions(driver);
        actions.click(communityLink).perform();
        String communityUrl = "https://community.lambdatest.com/";
        softAssert.assertEquals(driver.getCurrentUrl(), communityUrl, "Community URL mismatch!");

        // Step 14: Close the browser
        driver.quit();
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
