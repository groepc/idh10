package edu.avans.hartigehap.web.it;

import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.openqa.selenium.*;

@Slf4j
public class HomePageLoginIT {

    public static String URL = "http://localhost:8080/hh";

    @Test
    public void login() {
        WebDriver driver = BrowserUtils.getWebDriver();
        driver.get(URL);
        log.info("Congratulations, the home page is available ;-) {}", URL);

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

        if (loggedIn) {
            js.executeScript("document.getElementById('btn-logout').click();");
        }

        log.info("Timeout of 10 seconds");
//        js.executeScript("document.getElementById('dropdown-toggle-login').removeAttribute('display');");
//        js.executeScript("document.getElementById('dropdown-toggle-login').setAttribute('display','block');");

        // Added bootstrap support
        log.info("Timeout of 10 seconds");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        String source = driver.getPageSource();
        log.info("HTML Source of webpage:" + source);

        // Dropdown menu is now visible
        log.info("Looking for #login-test div");
        WebElement loginDiv = driver.findElement(By.id("login-test"));
        assertNotNull(loginDiv);
        log.info("Looking for j_username input element");
        js.executeScript("document.getElementById('form-username-j-username').setAttribute('value', 'manager')");

        log.info("Looking for j_password input element");
        js.executeScript("document.getElementById('form-username-j-password').setAttribute('value', 'manager')");

        String sourceAfterAttributes = driver.getPageSource();
        log.info("HTML Source of webpage after attributes:" + sourceAfterAttributes);

        log.info("Submit form");
        js.executeScript("document.getElementsByName('submit')[0].click();");

        String sourceAfter = driver.getPageSource();
        log.info("HTML Source of webpage:" + sourceAfter);

        try {
            WebElement errorDiv = driver.findElement(By.className("error"));
            fail("For a succesful login, an error div is not expected: " + errorDiv);
        } catch (NoSuchElementException ex) {
            log.debug("Login succeeded ;-)");
        }
    }

}
