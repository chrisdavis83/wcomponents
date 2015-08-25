package com.github.openborders.examples; 

import com.github.openborders.examples.TextDuplicator_VelocityImpl;
import junit.framework.Assert;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.github.openborders.Container;

import com.github.openborders.test.selenium.MultiBrowserRunner;
import com.github.openborders.test.selenium.WComponentSeleniumTestCase;

/**
 * Selenium unit tests for {@link TextDuplicator_VelocityImpl}.
 * 
 * @author Yiannis Paschalidis 
 * @since 1.0.0
 */
@Category(SeleniumTests.class)
@RunWith(MultiBrowserRunner.class)
public class TextDuplicator_VelocityImpl_Test extends WComponentSeleniumTestCase
{
    /** Creates a TextDuplicator_Test_SeleniumImpl. */
    public TextDuplicator_VelocityImpl_Test()
    {
        super(new TextDuplicator_VelocityImpl());
    }

    @Test
    public void testDuplicator()
    {
        // Launch the web browser to the LDE
        WebDriver driver = getDriver();

        // Enter some text and use the duplicate button
        String inputFieldName = ((Container) getUi()).getChildAt(1).getId();
        driver.findElement(By.name(inputFieldName)).sendKeys("dummy");
        driver.findElement(By.xpath("//button[text()='Duplicate']")).click();
        Assert.assertEquals("Incorrect text field text after duplicate", "dummydummy", 
                     driver.findElement(By.xpath("//input[@type='text']")).getAttribute("value"));
        
        // Clear the text
        driver.findElement(By.xpath("//button[text()='Clear']")).click();        
        Assert.assertEquals("Incorrect text field text after clear", "", 
                     driver.findElement(By.xpath("//input[@type='text']")).getAttribute("value"));
    }
}
