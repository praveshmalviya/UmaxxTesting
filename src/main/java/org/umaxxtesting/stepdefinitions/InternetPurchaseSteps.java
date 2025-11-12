package org.umaxxtesting.stepdefinitions;

import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;

import java.time.Duration;

public class InternetPurchaseSteps {

    WebDriver driver;
    WebDriverWait wait;

    String navToggler = "//button[contains(@class,'navbar-toggler')]";
    String navLink2 = "//*[@id='nav-link-2']";
    String plansSection = "//*[@id=\"plans-section\"]/div/div[1]/div/div[3]/a";

    String coverageCode = "//*[@id='coverage-code']";
    String hboEmail = "//*[@id='free_hbo_email_address']";
    String zipSubmit = "//*[@id='zip-form']//button";

    String order_email = "//*[@id='order_email']";
    String order_password = "//*[@id='order_password']";
    String order_phone = "//*[@id='order_phone']";
    String order_first_name = "//*[@id='order_first_name']";
    String order_last_name = "//*[@id='order_last_name']";
    String order_address = "//*[@id='order_address']";

    String nokia = "//*[@id='nokia']";
    String acceptCheck = "//*[@id='accept-checkbox']";
    String newsletterCheck = "//*[@id='newsletter-checkbox']";
    String offersCheck = "//*[@id='offers-checkbox']";

    String newOrderComplete = "//*[@id=\"new_order\"]/div[15]/div/div/button";
    String subscriptionPlanPostOrder = "//*[@id=\"subscription-plan-box\"]/div/div/div/a";

    String makePaymentBtn = "//*[@id='checkout-stripe-payment-options']//button";
    String completePurchaseBtn = "//*[@id='payment-form']//button";
    String homeButton = "//*[@id='nav-link-0']";

    // ✅ CLICK
    public void safeClick(String xpath) {
        WebElement ele = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ele);
            ele.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ele);
        }
    }

    // ✅ SEND
    public void safeSendXpath(String xpath, String value) {
        WebElement ele = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ele);
        ele.clear();
        ele.sendKeys(value);
    }

    public void safeSendId(String id, String value) {
        WebElement ele = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
        ele.clear();
        ele.sendKeys(value);
    }

    // ✅ GOOGLE ADDRESS AUTOFILL – FINAL WORKING VERSION
    public void selectAddressWithIframe(String inputXpath, String address) {

        // ✅ Try switching through iframes
        int frameCount = driver.findElements(By.tagName("iframe")).size();
        boolean isInsideFrame = false;

        for (int i = 0; i < frameCount; i++) {
            try {
                driver.switchTo().defaultContent();
                driver.switchTo().frame(i);

                if (!driver.findElements(By.xpath(inputXpath)).isEmpty()) {
                    isInsideFrame = true;
                    break;
                }
            } catch (Exception ignored) {}
        }

        if (!isInsideFrame) driver.switchTo().defaultContent();

        // ✅ Type Address
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(inputXpath)));
        input.clear();
        input.sendKeys(address);

        JavascriptExecutor js = (JavascriptExecutor) driver;

        // ✅ Wait suggestions
        By suggestions = By.cssSelector(".pac-item");
        wait.until(ExpectedConditions.visibilityOfElementLocated(suggestions));

        // ✅ First suggestion
        WebElement firstSuggestion = driver.findElement(By.xpath("(//div[contains(@class,'pac-item')])[1]"));

        // ✅ Click via JS
        js.executeScript("arguments[0].click();", firstSuggestion);

        // ✅ Trigger google validation events
        js.executeScript(
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('blur', { bubbles: true }));",
                input
        );

        // ✅ Commit Selection
        input.sendKeys(Keys.TAB);

        driver.switchTo().defaultContent();

        // ✅ Validation
        String finalValue = driver.findElement(By.xpath(inputXpath)).getAttribute("value");
        System.out.println("✅ Final Address Stored = " + finalValue);

        assert finalValue != null;
        if (!finalValue.toLowerCase().contains("ny")) {
            throw new RuntimeException("❌ Address NOT properly selected!");
        }
    }


    // ================== Steps ==================

    @Given("user is on the Internet Page")
    public void user_is_on_the_internet_page() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.get("https://test-umaxx-tv.herokuapp.com/");
    }

    @When("user selects the plan")
    public void user_selects_the_plan() {
        try {
            WebElement toggle = driver.findElement(By.xpath(navToggler));
            if (toggle.isDisplayed()) toggle.click();
        } catch (Exception ignored) {}

        safeClick(navLink2);
        safeClick(plansSection);
    }

    @When("user enters zip code")
    public void user_enters_zip_code() {
        safeSendXpath(coverageCode, "12345");
        safeSendXpath(hboEmail, "test@test.com");
        safeClick(zipSubmit);
    }

    @When("user fills customer details")
    public void user_fills_customer_details() {

        safeSendXpath(order_email, "terrificpravesh@gmail.com");
        safeSendXpath(order_password, "Test@123");
        safeSendXpath(order_phone, "9876543210");
        safeSendXpath(order_first_name, "John");
        safeSendXpath(order_last_name, "Doe");

        // ✅ GOOGLE ADDRESS
        selectAddressWithIframe(order_address, "1 River Rd, Schenectady, NY, USA");
    }

    @When("user selects product")
    public void user_selects_product() {
        safeClick(nokia);
    }

    @When("user accepts agreements")
    public void user_accepts_agreements() {
        safeClick(acceptCheck);
        safeClick(newsletterCheck);
        safeClick(offersCheck);
    }

    @When("user proceeds to pricing")
    public void user_proceeds_to_pricing() {
        safeClick(newOrderComplete);
        safeClick(subscriptionPlanPostOrder);
    }

    @When("user proceeds to payment page")
    public void user_proceeds_to_payment_page() {
        safeClick(makePaymentBtn);
    }

    @When("user enters payment details")
    public void user_enters_payment_details() {
        safeSendId("cardNumber", "4242424242424242");
        safeSendId("expiryDate", "12/30");
        safeSendId("cvv", "123");
        safeSendId("cardholdername", "John Doe");
        safeSendId("billingCountry", "United States");
        safeSendId("billingPostalCode", "10001");
    }

    @Then("order should be submitted successfully")
    public void order_should_be_submitted_successfully() {
        safeClick(completePurchaseBtn);

        WebElement result =
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(text(),'Success') or contains(text(),'Confirmed')]")
                ));

        Assert.assertTrue(
                result.getText().toLowerCase().contains("success") ||
                        result.getText().toLowerCase().contains("confirmed"),
                "Order Failed!"
        );
    }

    @And("user return to homepage")
    public void user_return_to_homepage() {
        safeClick(homeButton);
        driver.quit();
    }
}
