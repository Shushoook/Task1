package com.epam.engx.selenium.pages.browser;

import com.paulhammant.ngwebdriver.NgWebDriver;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.openqa.selenium.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class Browser implements SearchContext {
    private final WebDriver driver;
    private final NgWebDriver ngDriver;
    private final BidiMap<String, com.epam.engx.selenium.pages.browser.Page> windows;

    public Browser(WebDriver driver) {
        this.driver = driver;
        this.ngDriver = new NgWebDriver((JavascriptExecutor) driver);
        this.windows = new DualHashBidiMap<>();
        windows.put(driver.getWindowHandle(), null);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().fullscreen();
    }

    public static Browser create() {
        return new Browser(new com.epam.engx.selenium.pages.browser.DriverSupplier().get());
    }

    public void quit() {
        driver.manage().deleteAllCookies();
        driver.quit();
    }

    public <T extends com.epam.engx.selenium.pages.browser.Page> T switchTo(T page) {
        driver.switchTo().window(windows.inverseBidiMap().get(page));
        return page;
    }

    public <T extends com.epam.engx.selenium.pages.browser.Page> T switchTo(Class<T> pageClass) {
        var pageEntry = windows.entrySet().stream()
                .filter(entry -> entry.getValue().getClass() == pageClass)
                .findFirst();

        pageEntry.map(Map.Entry::getKey)
                .ifPresent(driver.switchTo()::window);

        return pageEntry
                .map(Map.Entry::getValue)
                .map(pageClass::cast)
                .orElseThrow();
    }

    public <T extends com.epam.engx.selenium.pages.browser.Page> T go(com.epam.engx.selenium.pages.browser.PageConstructor<T> pageConstructor) {
        var page = pageConstructor.apply(driver).to();
        windows.put(driver.getWindowHandle(), page);
        return (T) page;
    }

    public Browser addTab() {
        driver.switchTo().newWindow(WindowType.TAB);
        windows.put(driver.getWindowHandle(), null);
        return this;
    }

    public String title() {
        return driver().getTitle();
    }

    public WebDriver driver() {
        return driver;
    }

    public NgWebDriver ngDriver() {
        return ngDriver;
    }

    BidiMap<String, com.epam.engx.selenium.pages.browser.Page> tabs() {
        return windows;
    }

    @Override
    public List<WebElement> findElements(By by) {
        return driver.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return driver.findElement(by);
    }
}
