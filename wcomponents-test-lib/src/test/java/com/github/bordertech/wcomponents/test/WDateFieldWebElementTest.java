package com.github.bordertech.wcomponents.test;

import com.github.bordertech.wcomponents.test.components.WDateFieldUI;
import com.github.bordertech.wcomponents.test.selenium.by.ByButtonText;
import com.github.bordertech.wcomponents.test.selenium.by.ByLabel;
import com.github.bordertech.wcomponents.test.selenium.MultiBrowserRunner;
import com.github.bordertech.wcomponents.test.selenium.SeleniumJettyTestCase;
import com.github.bordertech.wcomponents.test.selenium.driver.SeleniumWComponentsWebDriver;
import com.github.bordertech.wcomponents.test.selenium.element.SeleniumWButtonWebElement;
import com.github.bordertech.wcomponents.test.selenium.element.SeleniumWDateFieldWebElement;
import com.github.bordertech.wcomponents.test.selenium.element.SeleniumWFieldIndicatorWebElement;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MultiBrowserRunner.class)
public class WDateFieldWebElementTest extends SeleniumJettyTestCase {


	/**
	 * Constructor...
	 */
	public WDateFieldWebElementTest() {
		super(new WDateFieldUI());
	}

	@Test
	public void testDateFieldUsingStandard() {
		SeleniumWComponentsWebDriver driver = getDriver();

		SeleniumWButtonWebElement validatingButton = driver.findWButton(new ByButtonText("Validate button", false, true));

		validatingButton.click();

		SeleniumWDateFieldWebElement dateField = driver.findWDateField(new ByLabel("this is a standard datefield", false));

		SeleniumWFieldIndicatorWebElement message = dateField.getFieldIndicatorMessage();
		assertEquals(message.getText(), "this is a standard datefield must be completed.");

		dateField.sendKeys("18102018");
	}

	@Test
	public void testDateFieldUsingReadonly() {
		SeleniumWComponentsWebDriver driver = getDriver();

		SeleniumWButtonWebElement validatingButton = driver.findWButton(new ByButtonText("Validate button", false, true));

		validatingButton.click();

		SeleniumWDateFieldWebElement dateField = driver.findWDateField(new ByLabel("this is a readonly datefield", false));

		SeleniumWFieldIndicatorWebElement message = dateField.getFieldIndicatorMessage();
		assertNull(message);

	}
}