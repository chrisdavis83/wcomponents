package com.github.openborders.examples;

import com.github.openborders.Action;
import com.github.openborders.ActionEvent;
import com.github.openborders.Margin;
import com.github.openborders.WAjaxControl;
import com.github.openborders.WCheckBox;
import com.github.openborders.WContainer;
import com.github.openborders.WFieldLayout;
import com.github.openborders.WFieldSet;
import com.github.openborders.WLabel;
import com.github.openborders.WPanel;
import com.github.openborders.WText;

/**
 * This example demonstrates the use of {@link WCheckBox#setActionOnChange(Action)}. The action associated with the
 * checkbox gets executed whenever the checkbox selection changes.
 * 
 * @author Christina Harris
 * @since 1.0.0
 * @author Mark Reeves
 * @since 1.0.0
 */
public class WCheckBoxTriggerActionExample extends WContainer
{
    /** The 'breakfast' check box. */
    private final WCheckBox breakfastCheckbox = new WCheckBox();

    /** The 'lunch' check box. */
    private final WCheckBox lunchCheckbox = new WCheckBox();

    /** The 'dinner' check box. */
    private final WCheckBox dinnerCheckbox = new WCheckBox();

    /** The panel which contains the information text display. */
    private final WPanel infoPanel = new WPanel();

    /**
     * Creates a WCheckBoxTriggerActionExample.
     */
    public WCheckBoxTriggerActionExample()
    {
        WFieldSet fset = new WFieldSet("Choose one or more meal[s]");
        add(fset);
        WFieldLayout flayout = new WFieldLayout(WFieldLayout.LAYOUT_STACKED);
        fset.add(flayout);
        flayout.setLabelWidth(0);
        flayout.addField("Breakfast", breakfastCheckbox);
        flayout.addField("Lunch", lunchCheckbox);
        flayout.addField("Dinner", dinnerCheckbox);
        
        final WText info = new WText();
        add(infoPanel);
        infoPanel.add(info);
        infoPanel.setMargin(new Margin(12, 0, 0, 0));

        breakfastCheckbox.setActionOnChange(new Action()
        {

            public void execute(final ActionEvent event)
            {
                if (breakfastCheckbox.isSelected())
                {
                    info.setText("Breakfast selected");

                }
                else
                {
                    info.setText("Breakfast unselected");
                }

            }

        });

        lunchCheckbox.setActionOnChange(new Action()
        {

            public void execute(final ActionEvent event)
            {
                if (lunchCheckbox.isSelected())
                {
                    info.setText("Lunch selected");

                }
                else
                {
                    info.setText("Lunch unselected");
                }
            }
        });

        dinnerCheckbox.setActionOnChange(new Action()
        {

            public void execute(final ActionEvent event)
            {
                if (dinnerCheckbox.isSelected())
                {
                    info.setText("Dinner selected");

                }
                else
                {
                    info.setText("Dinner unselected");
                }
            }
        });
        
        /* 
         * NOTE: you should not use WCheckBox to submit a form, so if you need to
         * trigger an action on change it should be done using AJAX.
         */
        WAjaxControl ajaxControl = new WAjaxControl(breakfastCheckbox, infoPanel);
        add(ajaxControl);
        ajaxControl = new WAjaxControl(lunchCheckbox, infoPanel);
        add(ajaxControl);
        ajaxControl = new WAjaxControl(dinnerCheckbox, infoPanel);
        add(ajaxControl);

    }

    /**
     * @return the 'breakfast' check box.
     */
    public WCheckBox getBreakfastCheckBox()
    {
        return breakfastCheckbox;
    }

    /**
     * @return the 'lunch' check box.
     */
    public WCheckBox getLunchCheckBox()
    {
        return lunchCheckbox;
    }

    /**
     * @return the 'dinner' check box.
     */
    public WCheckBox getDinnerCheckBox()
    {
        return dinnerCheckbox;
    }

    /**
     * @return the panel which contains the information text.
     */
    public WPanel getInformationTextBox()
    {
        return infoPanel;
    }
}
