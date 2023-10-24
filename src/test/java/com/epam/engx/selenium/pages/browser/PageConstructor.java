package com.epam.engx.selenium.pages.browser;

import org.openqa.selenium.WebDriver;

import java.util.function.Function;

@FunctionalInterface
public interface PageConstructor<T extends com.epam.engx.selenium.pages.browser.Page> extends Function<WebDriver, T> {
}
