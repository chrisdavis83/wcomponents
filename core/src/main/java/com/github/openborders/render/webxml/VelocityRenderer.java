package com.github.openborders.render.webxml;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.github.openborders.AbstractWComponent;
import com.github.openborders.Container;
import com.github.openborders.RenderContext;
import com.github.openborders.Renderer;
import com.github.openborders.UIContext;
import com.github.openborders.UIContextHolder;
import com.github.openborders.WComponent;
import com.github.openborders.servlet.WebXmlRenderContext;
import com.github.openborders.util.Config;
import com.github.openborders.util.SystemException;
import com.github.openborders.velocity.VelocityEngineFactory;
import com.github.openborders.velocity.VelocityProperties;
import com.github.openborders.velocity.VelocityWriter;

/**
 * Renders WComponents using a Velocity template.
 *
 * Children of the WComponent being rendered are placed into the VelocityContext for use by the velocity template.
 * If a child is added to the parent with a tag of "xyz", then the child's rendered output will be placed into the
 * VelocityContext under the variable "$xyz".
 * <p>
 * There are some other standard variables that are available in the VelocityContext:
 * <ul>
 *   <li> $children is a list of the rendered output of all children, in order.</li>
 *   <li> $this is a reference to the WComponent itself.</li>
 *   <li> $context is a reference to the VelocityContext itself.</li>
 *   <li> If a child's tag ends in "_list", then instead of adding the child's rendered output to the context, the
 *      output is placed in a List which is added to the context. This means that several children can use the
 *      same tag (ending in "_list") and the template can iterate over a list structure to render them.</li>
 * </ul>
 *
 * The Velocity engine can be configured to work in one of two modes:
 * <ol>
 *   <li> Source mode - by setting the <code>wcomponent.velocity.fileTemplatesDir</code> parameter to
 *      point to the location of the source tree (eg the src directory), the Velocity engine
 *      will read the templates directly from the source. It will not cache them.</li>
 *   <li> Classpath mode - by setting the <code>wcomponent.velocity.fileTemplatesDir</code> parameter
 *      to null or empty, the Velocity engine will read the templates from the classpath and
 *      will cache them.</li>
 * </ol>
 *
 * @author James Gifford
 * @since 1.0.0
 */
public final class VelocityRenderer implements Renderer
{
    private static final String NO_TEMPLATE_LAYOUT = "com/github/openborders/velocity/NoTemplateLayout.vm";

    /** The logger instance for this class. */
    private static final Log log = LogFactory.getLog(VelocityRenderer.class);

    /** Parameter names ending with this suffix will be treated as lists. */ 
    public static final String LIST_SUFFIX = "_list";

    /** The URL of the velocity template to use when rendering. */
    @Deprecated
    private final String url;

    /**
     * Creates a VelocityRenderer where the template is determined during rendering.
     */
    public VelocityRenderer()
    {
        url = null;
    }

    /**
     * Creates a VelocityRenderer with a pre-defined template.
     * @param templateUrl the URL of the velocity template to use when rendering
     * 
     * @deprecated do not construct velocity renderers with specific templates
     */
    @Deprecated
    public VelocityRenderer(final String templateUrl)
    {
        url = templateUrl;
        
        if (log.isDebugEnabled())
        {
            log.debug("VelocityLayout url : " + url);
        }
    }

    /**
     * @return the URL of the velocity template to use when rendering.
     * @deprecated Velocity renderers will not necessarily have a template.
     */
    @Deprecated
    public String getUrl()
    {
        return url;
    }

    /**
     * Paints the component in HTML using the Velocity Template.
     * 
     * @param component the component to paint.
     * @param context the context to send the XML output to.
     */
    public void render(final WComponent component, final RenderContext context)
    {
        PrintWriter out = ((WebXmlRenderContext) context).getWriter();
        
        // If we are debugging the layout, write markers so that the html
        // designer can see where templates start and end.
        boolean debugLayout = Config.getInstance().getBoolean("wcomponent.velocity.debugLayout", false);
        
        if (debugLayout)
        {
            String templateUrl = url;
            
            if (url == null && component instanceof AbstractWComponent)
            {
                templateUrl = ((AbstractWComponent) component).getTemplate();
            }
            
            out.println("<!-- Start " + templateUrl + " -->");
            paintXml(component, out);
            out.println("<!-- End   " + templateUrl + " -->");
        }
        else
        {
            paintXml(component, out);
        }
    }
    
    /**
     * Paints the component in XML using the Velocity Template.
     * 
     * @param component the component to paint.
     * @param writer the writer to send the HTML output to.
     */
    public void paintXml(final WComponent component, final Writer writer)
    {
        if (log.isDebugEnabled())
        {
            log.debug("paintXml called for component class " + component.getClass());
        }
        
        String templateText = null;
        
        if (component instanceof AbstractWComponent)
        {
            AbstractWComponent abstractComp = ((AbstractWComponent) component);
            templateText = abstractComp.getTemplateMarkUp();
        }
        
        try
        {
            Map<String, WComponent> componentsByKey = new HashMap<String, WComponent>();
            VelocityContext context = new VelocityContext();
            fillContext(component, context, componentsByKey);
            
            VelocityWriter velocityWriter = new VelocityWriter(writer, componentsByKey, UIContextHolder.getCurrent());
            
            if (templateText != null)
            {
                VelocityEngine engine = VelocityEngineFactory.getVelocityEngine();
                engine.evaluate(context, velocityWriter, component.getClass().getSimpleName(), templateText);
            }
            else
            {
                Template template = getTemplate(component);
                
                if (template == null)
                {
                    log.warn("VelocityRenderer invoked for a component with no template: " + component.getClass().getName());
                }
                else
                {
                    template.merge(context, velocityWriter);
                }
            }
            
            velocityWriter.close();

            if (component instanceof VelocityProperties)
            {
                ((VelocityProperties) component).mapUsed();
            }
        }
        catch (ResourceNotFoundException rnfe)
        {
            log.error("Could not find template '" + url + "' for component " + component.getClass().getName(), rnfe);
        }
        catch (ParseErrorException pee)
        {
            // syntax error : problem parsing the template
            log.error("Parse problems", pee);
        }
        catch (MethodInvocationException mie)
        {
            // something invoked in the template
            // threw an exception
            Throwable wrapped = mie.getWrappedThrowable();

            log.error("Problems with velocity", mie);

            if (wrapped != null)
            {
                log.error("Wrapped exception...", wrapped);
            }
        }
        catch (Exception e)
        {
            log.error("Problems with velocity", e);
        }
    }

    /**
     * Fills the given velocity context with data from the component which is being rendered.
     * A map of components is also built up, in order to support deferred rendering.
     * 
     * @param component the current component being rendered.
     * @param context the velocity context to modify.
     * @param componentsByKey a map to store components for deferred rendering.
     */
    private void fillContext(final WComponent component, 
                               final VelocityContext context, final Map<String, WComponent> componentsByKey)
    {
        // Also make the component available under the "this" key.
        context.put("this", component);
        
        // Make the UIContext available under the "uicontext" key.
        UIContext uic = UIContextHolder.getCurrent();
        context.put("uicontext", uic);
        context.put("uic", uic);

        if (component instanceof VelocityProperties)
        {
            Map<?, ?> map = ((VelocityProperties) component).getVelocityMap();
            
            for (Map.Entry<?, ?> entry : map.entrySet())
            {
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                context.put(key, value);
            }
        }
        
        if (log.isDebugEnabled())
        {
            log.debug("Handling children");
        }
        
        // As well as going into their own named slots, visible children are also
        // placed into a list called children
        ArrayList<String> children = new ArrayList<String>();

        if (component instanceof Container)
        {
            Container container = (Container) component;
            
            for (int i = 0; i < container.getChildCount(); i++)
            {
                WComponent child = container.getChildAt(i);
                String tag = child.getTag();
                
                if (tag != null || child.isVisible())
                {
                    // The key needs to be something which would never be output by a Velocity template.
                    String key = "<VelocityLayout" + child.getId() + "/>";
                    componentsByKey.put(key, child);
                    
                    if (tag != null)
                    {
                        if (log.isDebugEnabled())
                        {
                            log.debug("Adding child " + tag + " to context");
                        }
                        
                        addToContext(context, tag, key);
                    }

                    if (child.isVisible())
                    {
                        children.add(key);
                    }
                }
            }

            context.put("children", children);
        }

        // Put the context in the context
        context.put("context", context);
    }
    
    /**
     * Adds a name/value pair to the Velocity context.
     * If the name parameter ends with {@link #LIST_SUFFIX}
     * 
     * @param context the context to add to.
     * @param name the name
     * @param value the value
     */
    private void addToContext(final VelocityContext context, final String name, final Object value)
    {
        if (name.endsWith(LIST_SUFFIX))
        {
            // We want to use lists
            Object already = context.get(name);
            if (already != null && !(already instanceof List))
            {
                throw new SystemException("VelocityContext contained " + already + " instead of List under " + name);
            }
            
            List list = (List) context.get(name);
            
            if (list == null)
            {
                list = new ArrayList();
                context.put(name, list);
            }
            
            list.add(value);
        }
        else
        {
            context.put(name, value);
        }
    }

    /**
     * Paints the component in HTML using the NoTemplateLayout.
     * 
     * @param component the component to paint.
     * @param writer the writer to send the HTML output to.
     * 
     * @deprecated Unused. Will be removed in the next major release.
     */
    @Deprecated
    protected void noTemplatePaintHtml(final WComponent component, final Writer writer)
    {
        try
        {
            writer.write("<!-- Start " + url + " not found -->\n");
            new VelocityRenderer(NO_TEMPLATE_LAYOUT).paintXml(component, writer);
            writer.write("<!-- End " + url + " (template not found) -->\n");
        }
        catch (IOException e)
        {
            log.error("Failed to paint component", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "VelocityLayout: " + url;
    }
    
    /**
     * Retrieves the Velocity template for the given component.
     * @param component the component to retrieve the template for.
     * @return the template for the given component, or null if there is no template.
     */
    private Template getTemplate(final WComponent component)
    {
        String templateUrl = url;
        
        if (templateUrl == null && component instanceof AbstractWComponent)
        {
            templateUrl = ((AbstractWComponent) component).getTemplate();
        }

        if (templateUrl != null)
        {
            try
            {
                return VelocityEngineFactory.getVelocityEngine().getTemplate(templateUrl);
            }
            catch (Exception ex)
            {
                // If the resource is not available (eg if the template does not
                // exist), paint using a default layout and inform the user
                // of what's going on in the html comments.
                log.warn("Could not open " + templateUrl, ex);
                
                try
                {
                    return VelocityEngineFactory.getVelocityEngine().getTemplate(NO_TEMPLATE_LAYOUT);
                }
                catch (Exception e)
                {
                    log.error("Failed to read no template layout", e);
                }
            }
        }
        
        return null;
    }
}
