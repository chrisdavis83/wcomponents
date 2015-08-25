package com.github.openborders.examples; 

import com.github.openborders.examples.InfoDump;
import junit.framework.Assert;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.github.openborders.Environment;

import com.github.openborders.test.selenium.MultiBrowserRunner;
import com.github.openborders.test.selenium.WComponentSeleniumTestCase;

/**
 * Selenium unit tests for {@link InfoDump}.
 * 
 * @author Yiannis Paschalidis 
 * @since 1.0.0
 */
@Category(SeleniumTests.class)
@RunWith(MultiBrowserRunner.class)
public class InfoDump_Test extends WComponentSeleniumTestCase
{
    /**
     * Creates a new InfoDump_Test.
     */
    public InfoDump_Test()
    {
        super(new InfoDump());
    }
    
    @Test
    public void testExample()
    {
        Environment env = getUi().getEnvironment();
        
        // Launch the web browser to the LDE
        WebDriver driver = getDriver();
        
        Assert.assertEquals("Incorrect default text", "", driver.findElement(byWComponentPath("WTextArea")).getText());
        
        driver.findElement(byWComponentPath("WButton[1]")).click();

        String text = driver.findElement(byWComponentPath("WTextArea")).getText();
        Assert.assertTrue("Text should contain dump info", text.contains("WEnvironment"));
        Assert.assertTrue("Incorrect AppId", text.contains("AppId: " + env.getAppId()));

        driver.findElement(byWComponentPath("WButton[0]")).click();
        Assert.assertEquals("Text should have been cleared", "", driver.findElement(byWComponentPath("WTextArea")).getText());
    }    
}
