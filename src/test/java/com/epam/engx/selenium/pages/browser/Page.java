package com.epam.engx.selenium.pages.browser;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

public abstract class Page extends PageFactory {
    protected final WebDriver driver;
    protected SearchContext searchContext;

    protected Page(WebDriver driver) {
        this.driver = driver;
        initElements(driver, this);
    }

    public Page to() {
        return this;
    }

    public WebDriver driver() {
        return driver;
    }

    protected WebElement $(String selector) {
        return Objects
                .requireNonNullElse(searchContext, driver)
                .findElement(By.cssSelector(selector));
    }

    protected void setSearchContext(SearchContext context) {
        searchContext = context;
    }

    public boolean isElementExist(By by) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5), Duration.ofMillis(500));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void centerTheElement(WebElement element) {
        String scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
                + "var elementTop = arguments[0].getBoundingClientRect().top;"
                + "window.scrollBy(0, elementTop-(viewPortHeight/2));";

        ((JavascriptExecutor) driver).executeScript(scrollElementIntoMiddle, element);
    }
}
