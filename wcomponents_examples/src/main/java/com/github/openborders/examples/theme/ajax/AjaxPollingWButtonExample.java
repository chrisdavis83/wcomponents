package com.github.openborders.examples.theme.ajax;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.openborders.Action;
import com.github.openborders.ActionEvent;
import com.github.openborders.BeanProvider;
import com.github.openborders.BeanProviderBound;
import com.github.openborders.Request;
import com.github.openborders.WAjaxControl;
import com.github.openborders.WAjaxPollingRegion;
import com.github.openborders.WButton;
import com.github.openborders.WContainer;
import com.github.openborders.WPanel;
import com.github.openborders.WText;
import com.github.openborders.layout.FlowLayout;
import com.github.openborders.layout.FlowLayout.Alignment;
import com.github.openborders.util.Util;

/**
 * <p>This example use of a {@link WAjaxControl} in conjunction with a 
 * {@link WAjaxPollingRegion} to produce an AJAX polling effect. </p>
 * 
 * <p>The {@link WAjaxControl} initiates the {@link WAjaxPollingRegion} and makes a service call. the polling region 
 * polls every second until the service returns.  The service will return after 5 seconds. </p>
 * 
 * @author Yiannis Paschalidis
 * @since 1.0.0
 */
public class AjaxPollingWButtonExample extends WPanel
{
    /** Polling interval in milliseconds. For a real app, do not set this as low as in this example. */
    private static final int POLL_INTERVAL = 1000;

    /** Emulated service invocation time in milliseconds. */
    private static final int SERVICE_TIME = 5000;
    
    /** The logger instance for this class. */
    private static final Log log = LogFactory.getLog(AjaxPollingWButtonExample.class);
    
    /** The polling region. */
    private final WAjaxPollingRegion poller = new WAjaxPollingRegion(POLL_INTERVAL);
    
    /** Cache key for the data being read. */
    private static final String DATA_KEY = "someDummyCacheKey"; 
    
    private final MyDataComponent myDataComponent = new MyDataComponent();

    /** Creates a AjaxPollingWButtonExample. */
    public AjaxPollingWButtonExample()
    {
        setLayout(new FlowLayout(Alignment.VERTICAL, 0, 5));
        
        WButton pollBtn = new WButton("Invoke service");
        add(pollBtn);
        
        WAjaxControl ajaxControl = new WAjaxControl(pollBtn, poller);
        poller.add(myDataComponent);
        myDataComponent.setVisible(false);
        
        add(poller);
        
        pollBtn.setAction(new Action()
        {
            public void execute(final ActionEvent event)
            {
                myDataComponent.setVisible(true);
                fakeServiceCall();
            }
        });

        // make the image change an ajax request
        add(ajaxControl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void preparePaintComponent(final Request request)
    {
        super.preparePaintComponent(request);
        
        if (Cache.getCache().get(DATA_KEY) != null)
        {
            poller.disablePoll();
        }
    }
    
    /**
     * Fakes a service call, using the WorkManager for threading.
     */
    private void fakeServiceCall()
    {
        poller.enablePoll();
        Cache.getCache().invalidate(DATA_KEY);
        
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(SERVICE_TIME);
                    Cache.getCache().put(DATA_KEY, "SUCCESS!");
                }
                catch (InterruptedException e)
                {
                    log.error("Timed out calling service", e);
                    Cache.getCache().put(DATA_KEY, "Timed out!");
                }
            }
        }.start();
    }
    
    /**
     * An example component that displays the service result from the cache.
     * @author Yiannis Paschalidis 
     */
    private static final class MyDataComponent extends WContainer
    {
        /**
         * Creates a MyDataComponent.
         */
        public MyDataComponent()
        {
            add(new WText("Service result: "));            
            WText display = new WText();
            add(display);
            
            display.setBeanProvider(new BeanProvider()
            {
                public Object getBean(final BeanProviderBound beanProviderBound)
                {
                    String result = (String) Cache.getCache().get(DATA_KEY);
                    
                    if (!Util.empty(result))
                    {
                        return result;
                    }
                    else if (result == null)
                    {
                        return "Loading...";
                    }
        
                    return "";
                }
            });
        }
    }

    /**
     * A dummy application cache implementation.
     */
    private static final class Cache
    {
        /** Singleton instance. */
        private static final Cache instance = new Cache();
        
        /** Backing cache map. */
        private final Map<Object, Object> map = new HashMap<Object, Object>();
        
        /** @return the cache instance. */
        public static Cache getCache()
        {
            return instance;
        }
        
        /**
         * Retrieves an item from the cache.
         * 
         * @param key the cache key.
         * @return the item corresponding to the given key, or null if not found.
         */
        public Object get(final Object key)
        {
            return map.get(key);
        }
        
        /**
         * Adds an item to the cache.
         * 
         * @param key the cache key.
         * @param value the value to cache.
         */
        public void put(final Object key, final Object value)
        {
            map.put(key, value);
        }
        
        /**
         * Removes an item from the cache.
         * 
         * @param key the cache key.
         */
        public void invalidate(final Object key)
        {
            map.remove(key);
        }
    }
}
