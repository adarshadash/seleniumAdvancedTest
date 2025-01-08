### **Assignment Task: Selenium Advanced**

#### Test Scenario

1. Navigate to https://www.lambdatest.com.
2. Perform an explicit wait till the time all the elements in the DOM
   are available.
3. Scroll to the WebElement ‘Explore all Integrations’ using the
   scrollIntoView() method. You are free to use any of the available
   web locators (e.g., XPath, CssSelector, etc.)
4. Click on the link and ensure that it opens in the new tab.
5. Save the window handles in a List (or array). Print the window handles
   of the opened windows (now there are two windows open).
6. Verify whether the URL is the same as the expected URL (if not, throw
   an Assert).
7. On that page, scroll to the page where the WebElement
   (Codeless Automation) is present
8. Click the ‘INTEGRATE TESTING WHIZ WITH LAMBDATEST’ link for
   Testing Whiz. The page should open in the same window.
9. Check if the title of the page is ‘TestingWhiz Integration With
   LambdaTest’. If not, raise an Assert.
10. Close the current window using the window handle [which we
    obtained in step (5)]
11. Print the current window count.
12. On the current window, set the URL to
    https://www.lambdatest.com/blog.
13. Click on the ‘Community’ link and verify whether the URL is
    https://community.lambdatest.com/.
14. Close the current browser window. 

#### Execution

The test scenarios should be demonstrated on the following combinations of
browsers and platforms (using Selenium 4 Grid and programming language of
your choice):
1. Chrome + 128.0 + Windows 10 (Test Scenario 1)
2. Microsoft Edge + 127.0 + macOS Ventura (Test Scenario 2)


### Solutions Using Java Testng Selenium Framework

#### Step 1: Lambdatest Credentials

Set LambdaTest username and access key in environment variables in the file lamdatest.env

Replace the values with your credentials, you can find them at (https://www.lambdatest.com/capabilities-generator/)

ln 1: export LT_USERNAME="Your Username"
ln 2: export LT_ACCESS_KEY="Your Access key"
Lamdatest Credentials

After your save your credentials at lambdatest.env please run the command:

$ source lambdatest.env

#### Step 2: Setting up 
You can genegate the test capabilites at (https://www.lambdatest.com/capabilities-generator/) and choose Java TestNg as the language.
In this framework, the common capabilities are hardcoded in LambdaTestScenario Java file and specific scenarios capabilities are 
mentioned in testng XML file for parallel execution.

## Step 3: Running Test

Run above framework in all browser platform version combination mentioned on testNg XML file using below command-

_`mvn clean install -Dsuite=src/test/resources/testng.xml`_
