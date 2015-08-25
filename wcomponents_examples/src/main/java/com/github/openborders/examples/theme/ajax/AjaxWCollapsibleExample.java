package com.github.openborders.examples.theme.ajax;

import com.github.openborders.CollapsibleGroup;
import com.github.openborders.WCollapsible;
import com.github.openborders.WCollapsibleToggle;
import com.github.openborders.WContainer;
import com.github.openborders.WHeading;
import com.github.openborders.WText;

/**
 * Example of using AJAX collapsibles. To load the content via AJAX, the collapsibles are
 * created using the {@link WCollapsible.CollapsibleMode#LAZY LAZY} collapsible mode.
 * 
 * @author Christina Harris
 * @since 1.0.0
 */
public class AjaxWCollapsibleExample extends WContainer
{
    /**
     * Creates an AjaxWCollapsibleExample.
     */
    public AjaxWCollapsibleExample()
    {
        add(new WHeading(WHeading.MAJOR, "Client Side Toggle and Collapsibles with Ajax Content."));
        add(new CollapsibleExample(true));

        add(new WHeading(WHeading.MAJOR, "Server Side Toggle and Collapsibles with Ajax Content."));
        add(new CollapsibleExample(false));
    }

    /**
     * A component which contains a collapsible with some AJAX content.
     */
    private static final class CollapsibleExample extends WContainer
    {
        /**
         * Creates a CollapsibleExample.
         * @param clientSide true for client-side collapsible, false for server-side.
         */
        public CollapsibleExample(final boolean clientSide)
        {

            CollapsibleGroup group = new CollapsibleGroup();
            WCollapsibleToggle wct = new WCollapsibleToggle(clientSide);
            wct.setGroup(group);
            add(wct);

            WText component1 = new WText("Here is some text that is collapsible via ajax.");

            WCollapsible collapsible1 = new WCollapsible(component1,
                                                         "Client Side Ajax Collapsible - initially collapsed",
                                                         WCollapsible.CollapsibleMode.LAZY, group);
            add(collapsible1);

            WText component2 = new WText("Here is some more text that is collapsible via ajax.");

            WCollapsible collapsible2 = new WCollapsible(component2,
                                                         "Client Side Ajax Collapsible - initially expanded",
                                                         WCollapsible.CollapsibleMode.LAZY, group);
            collapsible2.setCollapsed(false);
            add(collapsible2);
        }
    }
}
