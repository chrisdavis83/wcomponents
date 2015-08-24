package com.github.openborders.render.webxml; 

import com.github.openborders.Renderer;
import com.github.openborders.WComponent;
import com.github.openborders.WProgressBar;
import com.github.openborders.XmlStringBuilder;
import com.github.openborders.WProgressBar.ProgressBarType;
import com.github.openborders.WProgressBar.UnitType;
import com.github.openborders.servlet.WebXmlRenderContext;
import com.github.openborders.util.Util;

/**
 * {@link Renderer} for the {@link WProgressBar} component.
 * 
 * @author Yiannis Paschalidis
 * @since 1.0.0
 */
final class WProgressBarRenderer extends AbstractWebXmlRenderer
{
    /**
     * Paints the given WProgressBar.
     * 
     * @param component the WProgressBar to paint.
     * @param renderContext the RenderContext to paint to.
     */
    @Override
    public void doRender(final WComponent component, final WebXmlRenderContext renderContext)
    {
        WProgressBar progressBar = (WProgressBar) component;
        XmlStringBuilder xml = renderContext.getWriter();
        
        xml.appendTagOpen("ui:progressBar");
        xml.appendAttribute("id", component.getId());
        xml.appendOptionalAttribute("track", component.isTracking(), "true");
        xml.appendOptionalAttribute("hidden", progressBar.isHidden(), "true");
        xml.appendAttribute("value", progressBar.getValue());
        xml.appendAttribute("max", progressBar.getMax());
        xml.appendOptionalAttribute("toolTip", progressBar.getToolTip());
        xml.appendOptionalAttribute("accessibleText", progressBar.getAccessibleText());
        
        ProgressBarType type = progressBar.getProgressBarType();
        xml.appendOptionalAttribute("type", type == ProgressBarType.NORMAL ? null : "small");

        UnitType unitType = progressBar.getUnitType();
        xml.appendOptionalAttribute("output", unitType == UnitType.FRACTION ? null : "percent");
        
        String text = progressBar.getText();
        
        if (Util.empty(text))
        {
            xml.appendEnd();
        }
        else
        {
            xml.appendClose();
            xml.appendEscaped(text);
            xml.appendEndTag("ui:progressBar");
        }
    }
}
