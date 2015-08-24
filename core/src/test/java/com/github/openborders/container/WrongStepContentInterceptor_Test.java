package com.github.openborders.container;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.github.openborders.AbstractWComponentTestCase;
import com.github.openborders.ActionEscape;
import com.github.openborders.ContentEscape;
import com.github.openborders.Environment;
import com.github.openborders.MockContentAccess;
import com.github.openborders.UIContext;
import com.github.openborders.WApplication;
import com.github.openborders.WComponent;
import com.github.openborders.WContent;
import com.github.openborders.WLabel;
import com.github.openborders.WWindow;
import com.github.openborders.WebUtilities;
import com.github.openborders.container.InterceptorComponent;
import com.github.openborders.container.WrongStepContentInterceptor;
import com.github.openborders.servlet.WServlet;
import com.github.openborders.servlet.WebXmlRenderContext;
import com.github.openborders.util.Config;
import com.github.openborders.util.StepCountUtil;
import com.github.openborders.util.SystemException;
import com.github.openborders.util.mock.MockRequest;
import com.github.openborders.util.mock.MockResponse;

/**
 * WrongStepContentComponent_Test - unit tests for {@link WrongStepContentInterceptor}.
 * 
 * @author Yiannis Paschalidis
 * @since 1.0.0
 */
public class WrongStepContentInterceptor_Test extends AbstractWComponentTestCase
{
    @After
    public void resetConfig()
    {
        Config.reset();
    }

    @Test(expected = SystemException.class)
    public void testServiceRequestNoTarget()
    {
        setActiveContext(createUIContext());
        new WrongStepContentInterceptor().serviceRequest(new MockRequest());
    }

    @Test
    public void testServiceRequestNoStepError()
    {
        MyApp app = new MyApp();
        app.setLocked(true);
        MockResponse response = sendContentRequest(app.appContent, 1, 1);
        Assert.assertEquals("Should have returned content", MyContent.CONTENT, new String(response.getOutput()));
    }

    public void testErrorDirectContent()
    {
        Config.getInstance().setProperty(StepCountUtil.STEP_ERROR_URL_PARAMETER_KEY, "http://test.test");

        MyApp app = new MyApp();
        app.setLocked(true);
        MockResponse response = sendContentRequest(app.appContent, 1, 99);
        Assert.assertEquals("Should have returned error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            response.getErrorCode());
    }

    /**
     * Utility method to send a mock request to the application.
     * 
     * @param target the target content.
     * @param clientStep the client-side step count
     * @param serverStep the server-side step count
     * @return the response.
     */
    private MockResponse sendContentRequest(final WComponent target, final int clientStep, final int serverStep)
    {
        UIContext uic = createUIContext();
        uic.setUI(WebUtilities.getTop(target));
        WServlet.WServletEnvironment env = new WServlet.WServletEnvironment("/app", "http://localhost", "");
        env.setStep(serverStep);
        uic.setEnvironment(env);
        setActiveContext(uic);

        MockRequest request = new MockRequest();
        request.setParameter(Environment.TARGET_ID, target.getId());
        request.setParameter(Environment.STEP_VARIABLE, String.valueOf(clientStep));
        MockResponse response = new MockResponse();

        InterceptorComponent interceptor = new WrongStepContentInterceptor();
        interceptor.attachUI(uic.getUI());
        interceptor.attachResponse(response);

        // Handle the request. This will either return the content or a step error
        try
        {
            interceptor.serviceRequest(request);
            interceptor.preparePaint(request);
            interceptor.paint(new WebXmlRenderContext(response.getWriter()));
        }
        catch (ContentEscape escape)
        {
            try
            {
                // Content has been returned
                escape.setRequest(request);
                escape.setResponse(response);
                escape.escape();
            }
            catch (IOException e)
            {
                Assert.fail("Failed to write content");
            }
        }
        catch (ActionEscape ignored)
        {
            // don't care
        }

        // Step error
        return response;
    }

    /** Some mock content for testing. */
    private static final class MyContent extends WContent
    {
        /** The content which will be served up. */
        private static final String CONTENT = "ABC";

        /** Creates the mock content. */
        public MyContent()
        {
            MockContentAccess content = new MockContentAccess();
            content.setMimeType("text/plain");
            content.setBytes(CONTENT.getBytes());
            content.setDescription("content");
            setContentAccess(content);
        }
    }

    /** A test WApplication with content. */
    private static final class MyApp extends WApplication
    {
        /** Some arbitrary content added directly to the test application. */
        private final WContent appContent = new MyContent();

        /** For testing that step errors for content inside a WWindow are handled correctly. */
        private final WWindow window = new WWindow();

        /** Creates a MyApp. */
        public MyApp()
        {
            add(appContent);
            add(window);

            window.setContent(new WLabel("test"));
        }
    }
}
