package com.epam.engx.selenium.pages.gcpc.component;

import org.openqa.selenium.WebElement;

import java.util.function.Predicate;

record OptionPredicate(String text) implements Predicate<WebElement> {

    @Override
    public boolean test(WebElement element) {
        return element
                .getAttribute("textContent")
                .strip()
                .toLowerCase()
                .startsWith(text().toLowerCase());
    }
}
