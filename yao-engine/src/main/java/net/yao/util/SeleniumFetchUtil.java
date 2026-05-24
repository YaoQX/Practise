package net.yao.util;

import net.yao.enums.BizCodeEnum;
import net.yao.enums.SeleniumByEnum;
import net.yao.enums.SeleniumWebDriverEnum;
import net.yao.exception.BizException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SeleniumFetchUtil {

    public static WebDriver getDriver(String driverName) {
        String osName = System.getProperty("os.name");
        SeleniumWebDriverEnum seleniumWebDriverEnum = SeleniumWebDriverEnum.valueOf(driverName);
        return switch (seleniumWebDriverEnum) {
            case CHROME -> {
                ChromeOptions options = new ChromeOptions();
                //--no-sandbox参数表示禁用沙箱模式，以提高浏览器的兼容性和稳定性。
                //--disable-dev-shm-usage参数表示禁用/dev/shm的使用，以避免在某些Linux系统中出现的内存不足问题。
                //--disable-extensions参数表示禁用所有扩展，以防止扩展影响浏览器的性能和稳定性。
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-extensions");

                if (osName.toLowerCase().contains("win")) {
                    System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver\\chromedriver.exe");
                } else {
                    options.addArguments("--headless");
                    System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver");
                }
                yield new org.openqa.selenium.chrome.ChromeDriver();
            }
            default -> {
                throw new BizException(BizCodeEnum.UI_UNSUPPORTED_BROWSER_DRIVER);
            }
        };
    }

    public static WebElement findElement(String locationType, String locationExpress, Long waitTime){
        WebDriver webDriver = SeleniumWebdriverContext.get();

        WebDriverWait webDriverWait = new WebDriverWait(webDriver, Duration.ofMillis(waitTime));
        SeleniumByEnum seleniumByEnum = SeleniumByEnum.valueOf(locationType);
        return switch (seleniumByEnum){
            case XPATH ->  webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locationExpress)));
            case TAG_NAME -> webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.tagName(locationExpress)));
            case ID -> webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id(locationExpress)));
            case NAME -> webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.name(locationExpress)));
            case CLASS_NAME -> webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.className(locationExpress)));
            case CSS_SELECTOR -> webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(locationExpress)));
            case LINK_TEXT -> webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(locationExpress)));
            case PARTIAL_LINK_TEXT -> webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(locationExpress)));
            default -> throw new BizException(BizCodeEnum.UI_UNSUPPORTED_LOCATION_TYPE);
        };
    }

}
