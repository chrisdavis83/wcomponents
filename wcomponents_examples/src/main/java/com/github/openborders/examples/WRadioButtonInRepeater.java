package com.github.openborders.examples;

import java.util.Arrays;

import com.github.openborders.Action;
import com.github.openborders.ActionEvent;
import com.github.openborders.AjaxTarget;
import com.github.openborders.Margin;
import com.github.openborders.RadioButtonGroup;
import com.github.openborders.Request;
import com.github.openborders.WAjaxControl;
import com.github.openborders.WBeanContainer;
import com.github.openborders.WButton;
import com.github.openborders.WContainer;
import com.github.openborders.WFieldLayout;
import com.github.openborders.WFieldSet;
import com.github.openborders.WHorizontalRule;
import com.github.openborders.WLabel;
import com.github.openborders.WMessages;
import com.github.openborders.WPanel;
import com.github.openborders.WRadioButton;
import com.github.openborders.WRepeater;
import com.github.openborders.WText;
import com.github.openborders.subordinate.builder.SubordinateBuilder;

/**
 * Demonstrate how {@link RadioButtonGroup} and {@link WRadioButton} can be used with a {@link WRepeater}.
 * 
 * @author Jonathan Austin
 * @since 1.0.0
 * @author Mark Reeves
 * @since 1.0.0
 */
public class WRadioButtonInRepeater extends WContainer
{
    /** The radio button group that holds the radio buttons. */
    private final RadioButtonGroup group = new RadioButtonGroup();
    /** The repeater for the radio buttons. */
    private final WRepeater repeater = new WRepeater(new MyComponent(group));
    /** The text of the currently selected value. */
    private final WText text = new WText();

    /**
     * Construct the example.
     */
    public WRadioButtonInRepeater()
    {

        /*
         * NOTE
         * a set of radio buttons represent answers to a question where each radio
         * button is unique and mutually-exclusive response. To create an accessible
         * group of radio buttons there must be some way to supply an adequate
         * 'question'. This is most commonly a WFieldSet or a column in a WTable.
         */
        
        add(group);
        
        WFieldSet fset = new WFieldSet("Select an option");
        add(fset);
        fset.setMargin(new Margin(0, 0, 6, 0));
        fset.add(repeater);
        WFieldLayout layout = new WFieldLayout();
        fset.add(layout);
        layout.addField("Outside Repeater - Option D", group.addRadioButton("D"));
        layout.addField("Outside Repeater - Option E", group.addRadioButton("E"));
        layout.addField("Outside Repeater - Option F", group.addRadioButton("F"));

        
        /*
         * An ajax control should only control items which are placed AFTER the
         * controller (in source order)
         */
        WPanel msgPanel = new WPanel();
        add(msgPanel);

        final WMessages messages = new WMessages();
        msgPanel.add(messages);
        

        group.setActionOnChange(new Action()
        {
            public void execute(final ActionEvent event)
            {
                messages.info("Changed to " + event.getActionCommand());
            }
        });
        
        add(new WButton("submit"));

        WButton button1 = new WButton("target for subordinate for option B");
        add(button1);

        WPanel txtPanel = new WPanel();
        add(txtPanel);

        txtPanel.add(text);

        WAjaxControl ajax = new WAjaxControl(group, new AjaxTarget[] { msgPanel, txtPanel });
        add(ajax);

        SubordinateBuilder builder = new SubordinateBuilder();
        builder.condition().equals(group, "B");
        builder.whenTrue().enable(button1);
        builder.whenFalse().disable(button1);
        add(builder.build());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void preparePaintComponent(final Request request)
    {
        if (!isInitialised())
        {
            repeater.setData(Arrays.asList(new String[] { "A", "B", "C" }));
            setInitialised(true);
        }

        text.setText("Selected: " + (group.getSelectedValue() == null ? "" : group.getSelectedValue().toString()));

    }

    /**
     * A Class that has a radio button that is repeated.
     */
    public static class MyComponent extends WBeanContainer
    {
        /** Label for the radio button. */
        private final WLabel label;

        /**
         * Construct the repeated component.
         * 
         * @param group the radio button group
         */
        public MyComponent(final RadioButtonGroup group)
        {
            WRadioButton radio = group.addRadioButton();

            label = new WLabel("Label", radio);

            add(label);
            add(radio);
            add(new WHorizontalRule());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void preparePaintComponent(final Request request)
        {
            super.preparePaintComponent(request);
            if (!isInitialised())
            {
                label.setText("Option " + (String) getBean());
                setInitialised(true);
            }
        }
    }
}
