package com.github.bordertech.wcomponents.test.selenium;

import com.github.bordertech.wcomponents.WComponent;
import com.github.bordertech.wcomponents.lde.PlainLauncher;
import com.github.bordertech.wcomponents.util.Config;
import com.github.bordertech.wcomponents.util.ConfigurationProperties;
import org.apache.commons.configuration.Configuration;
import org.junit.After;
import org.junit.Before;

public class SeleniumJettyTestCase extends WComponentSeleniumTestCase {

	private PlainLauncher lde;

	public SeleniumJettyTestCase(WComponent wComponent) {
		Config.getInstance().setProperty("bordertech.wcomponents.lde.component.to.launch", wComponent.getClass().getCanonicalName());
	}

	@Before
	public void startup() throws Exception {
		lde = new PlainLauncher();
		lde.run();
	}


	@After
	public void tearDown() throws InterruptedException {
		lde.stop();
	}

}
