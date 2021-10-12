import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SmokeTest {

    public AndroidDriver<MobileElement> driver;
    public WebDriverWait wait;
    public  String userName = "UserName"; //User real UserName
    public String password = "Password"; // Use real Password

    @BeforeMethod
    public void setup() throws MalformedURLException {
        File appDir  = new File("src");
        File app = new File(appDir,"com.linkedin.android.apk");
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, "11");
        cap.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
        cap.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
        cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 500);
        cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
        cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 500);
        cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");

        // Declaring the driver as "Android driver" with the Host and Port number to communicate with Appium desktop
        driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), cap);
        wait = new WebDriverWait(driver, 10);

    }

    @Test
    public void testLinkedApp() throws InterruptedException {
        //Verify Home screen displayed
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElement(By.id("ad_full_logo_image")).isDisplayed();


        // verify the CarouselText for 3 moving banners
        Assert.assertTrue(validateCarouselText("Find and land your next job"));
        Assert.assertTrue(validateCarouselText("Build your network on the go"));
        Assert.assertTrue(validateCarouselText("Stay ahead with curated content for your career"));

        // Login with correct credential
        driver.findElement(By.id("growth_prereg_fragment_login_button")).click();
        driver.findElement(By.id("growth_join_split_form_first_name")).sendKeys(userName);
        driver.findElement(By.id("growth_join_split_form_last_name")).sendKeys(password);
        driver.findElement(By.id("growth_join_split_form_join_button")).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        // In the global search, type text ‘Callsign’
        driver.findElement(By.id("search_bar_text")).sendKeys("Callsign");

        //click on show all results
        driver.findElement(By.id("search_typeahead_see_all_button")).click();


        //verify all results have callsign string
        List<MobileElement> elements= driver.findElements(By.id("search_typeahead_entity_item"));
        for (MobileElement element : elements){
                Assert.assertTrue(element.getText().toLowerCase().contains("callsign"));
            }

        //Navigate back and Open chat from top right
        driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc=\"Back\"]")).click();
        driver.findElement(By.id("ad_notification_badge_icon")).click();

        //Tap on the filter icon
        driver.findElement(By.id("conversation_filter_lever_image")).click();

        //Click on the My connetion button
        driver.findElement(By.id("filter_connection_lever_btn")).click();

        //Click on back button
        driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc=\"Back\"]")).click();

        //Tap on profile photo on top left and then tap on settings
        driver.findElement(By.id("me_launcher")).click();




    }



    private boolean validateCarouselText(String expectedText) {
        long start_time = System.currentTimeMillis();
        long wait_time = 20000;
        long end_time = start_time + wait_time;
        while(System.currentTimeMillis() < end_time) {
            String actualText = driver.findElement(By.id("growth_prereg_carousel_item_text")).getText();
            if (actualText.contentEquals(expectedText)) {
                return true;
            }
        }
        return false;
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }
}
