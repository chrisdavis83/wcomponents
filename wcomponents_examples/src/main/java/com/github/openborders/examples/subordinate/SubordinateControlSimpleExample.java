package com.github.openborders.examples.subordinate;

import com.github.openborders.Margin;
import com.github.openborders.WCheckBox;
import com.github.openborders.WContainer;
import com.github.openborders.WField;
import com.github.openborders.WFieldLayout;
import com.github.openborders.WFieldSet;
import com.github.openborders.WTextField;
import com.github.openborders.subordinate.Equal;
import com.github.openborders.subordinate.Hide;
import com.github.openborders.subordinate.Rule;
import com.github.openborders.subordinate.Show;
import com.github.openborders.subordinate.WSubordinateControl;

/**
 * A simple example of SubordinateControl usage.
 * 
 * @author Martin Shevchenko
 * @since 1.0.0
 */
public class SubordinateControlSimpleExample extends WContainer
{
    /**
     * Creates a SubordinateControlSimpleExample.
     */
    public SubordinateControlSimpleExample()
    {
        WFieldLayout layout = new WFieldLayout(WFieldLayout.LAYOUT_STACKED);
        layout.setLabelWidth(0);
        
        WFieldSet fieldset = new WFieldSet("select any that apply");
        add(fieldset);
        fieldset.add(layout);
        fieldset.setMargin(new Margin(0, 0, 12, 0));
        
        WCheckBox extraInfoRequired = new WCheckBox();
        layout.addField("Extra information required", extraInfoRequired);
        WField extraField = layout.addField("Extra information", new WTextField());
        
        WCheckBox otherCheckBox = new WCheckBox();
        layout.addField("More information required", otherCheckBox);
        WField otherField = layout.addField("More information",  new WTextField());
        
        WSubordinateControl control = new WSubordinateControl();
        add(control);
        
        Rule rule = new Rule();
        rule.setCondition(new Equal(extraInfoRequired, Boolean.TRUE.toString()));
        rule.addActionOnTrue(new Show(extraField));
        rule.addActionOnFalse(new Hide(extraField));
        control.addRule(rule);
        rule = new Rule();
        rule.setCondition(new Equal(otherCheckBox, Boolean.TRUE.toString()));
        rule.addActionOnTrue(new Show(otherField));
        rule.addActionOnFalse(new Hide(otherField));
        control.addRule(rule);
    }
}
