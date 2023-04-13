import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class TestImplementation {

    private WebDriver driver;
    private static Properties properties;

    @BeforeClass
    public static void setupWebdriverChromeDriver() {
        File file = new File("src/test/resources/testray.properties");

        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        properties = new Properties();
        try {
            properties.load(fileInput);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.setProperty("webdriver.chrome.driver",properties.getProperty("browser.chrome.bin.file"));
    }

    @Before
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        logIn();
    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void logIn(){
        //Maximize window
        //driver.manage().window().maximize();

        //Define window size
        driver.manage().window().setSize(new Dimension(1440, 1280));

        //Wait 10 Seconds
        driver.get(properties.getProperty("base.url"));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //Get the site Title
        String title = driver.getTitle();
        System.out.println("The Page Title is: " + title);

        //Define testray user name, password, and sign in Field
        WebElement userNameFieldtestray = driver.findElement(By.xpath("//div/span[contains(@class,'username')]//following-sibling::input"));
        WebElement passwordFieldtestray = driver.findElement(By.xpath("//div/span[contains(@class,'password')]//following-sibling::input"));
        WebElement signInButtontestray = driver.findElement(By.xpath("//div/input[@type='submit']"));

        //Sing In Liferay
        userNameFieldtestray.isDisplayed();
        userNameFieldtestray.clear();
        userNameFieldtestray.sendKeys(properties.getProperty("testray.user.name"));
        passwordFieldtestray.sendKeys(properties.getProperty("testray.password"));
        signInButtontestray.click();
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);


        //Define user name, password, and sign in Field
        WebElement userNameField = driver.findElement(By.xpath("//label[normalize-space(text())='Email Address']//following-sibling::input"));
        WebElement passwordField = driver.findElement(By.xpath("//label[normalize-space(text())='Password']//following-sibling::input"));
        WebElement signInButton = driver.findElement(By.xpath("//button/span[normalize-space(text())='Sign In']"));

        //Sing In Testray
        userNameField.isDisplayed();
        userNameField.clear();
        userNameField.sendKeys(properties.getProperty("testray.user.name"));
        passwordField.sendKeys(properties.getProperty("testray.password"));
        signInButton.click();

        //Assert user Avatar
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement userAvatar = driver.findElement(By.xpath("//div[contains(@class,'user-icon')]"));
        userAvatar.isDisplayed();
    }

    @Test
    public void overrideCaseResults() {
        //Open url
        driver.get(properties.getProperty("compare.url"));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //Define locator of A failed and B passed
        WebElement statusAFailAndBPass = driver.findElement(By.xpath("//table[1]//tr[3]//td[@class='compare-status failed passed']/a"));
        String compareDetailsUrl = statusAFailAndBPass.getAttribute("href");
        driver.get(compareDetailsUrl);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //Get the counts of failed tests
        List<WebElement> listPassedResults = driver.findElements(By.xpath("//tbody/tr/td[@class='table-cell last']"));
        int passedNumber = listPassedResults.size();
        System.out.println("Acceptance Pass cases count is: " + passedNumber);

        for (int i = 1; i <= passedNumber; i++) {
            //Define Fail Button and Case Name
            WebElement byFailedButton = driver.findElement(By.xpath("//tbody/tr[1]/td/a[normalize-space(text())='Failed']"));
            WebElement caseName = driver.findElement(By.xpath("//tbody/tr[1]/td/a[normalize-space(text())='Failed']/../preceding-sibling::td[contains(@class,'table-column-main')]"));
            System.out.println("Editing case: " + caseName.getText() + ".");
            System.out.println("Case Link: " + byFailedButton.getAttribute("href"));

            //open Failed Case Link
            driver.get(byFailedButton.getAttribute("href"));

            //click edit button
            driver.findElement(By.xpath("//span[normalize-space(text())='Edit']")).click();

            //select Blocked Status
            Select dropStatus = new Select(driver.findElement(By.xpath("//select[@id='status']")));
            dropStatus.selectByVisibleText("Blocked");

            //comment
            WebElement commentField = driver.findElement(By.xpath("//textarea[@name='comment']"));
            commentField.sendKeys("The case has been passed on the second run.");

            //click Save button
            driver.findElement(By.xpath("//span[normalize-space(text())='Save']")).click();

            //Assert Blocked
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space(text())='Blocked']")));

            System.out.println("Test case has been updated!");
            System.out.println("===============================");

            driver.get(compareDetailsUrl);
        }
    }
    
}