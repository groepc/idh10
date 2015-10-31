package edu.avans.hartigehap.web.it;

import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.openqa.selenium.*;

@Slf4j
public class ScheduleNoAccess {
    public static String URL = "http://localhost:8080/hh";

    @Test
    public void access() {
        WebDriver driver = BrowserUtils.getWebDriver();
        driver.get(URL);
        log.debug("Congratulations, the home page is available ;-) {}", URL);

        // Add javascript support
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Added bootstrap support
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        boolean loggedIn = false;
        try {
            driver.findElement(By.xpath("//*[contains(text(),'Logout')]"));
            loggedIn = true;
        } catch (Exception e) {
            loggedIn = false;
        }

        if(loggedIn) {
            js.executeScript("document.getElementById('btn-logout').click();");
        }

        WebElement scheduleButton = driver.findElement(By.id("schedule-test"));
        assertNotNull(scheduleButton);

        scheduleButton.click();

        try {
            driver.findElement(By.xpath("//*[contains(text(),'Report')]"));
            fail("For a succesful security, the schedule page is not expected");
        } catch (Exception ex) {
            log.debug("No access :-)");
        }

    }
}
