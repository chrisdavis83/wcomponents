package com.github.openborders.examples.theme;

import java.util.Calendar;
import java.util.Date;

import com.github.openborders.Action;
import com.github.openborders.ActionEvent;
import com.github.openborders.WButton;
import com.github.openborders.WFieldLayout;
import com.github.openborders.WFieldSet;
import com.github.openborders.WHeading;
import com.github.openborders.WHorizontalRule;
import com.github.openborders.WPanel;
import com.github.openborders.WPartialDateField;
import com.github.openborders.WTextField;
import com.github.openborders.layout.FlowLayout;
import com.github.openborders.layout.FlowLayout.Alignment;
import com.github.openborders.subordinate.And;
import com.github.openborders.subordinate.Disable;
import com.github.openborders.subordinate.Enable;
import com.github.openborders.subordinate.Equal;
import com.github.openborders.subordinate.Match;
import com.github.openborders.subordinate.Rule;
import com.github.openborders.subordinate.WSubordinateControl;

/**
 * An example showing use of a {@link WPartialDateField}.
 *
 * @author Ming Gao
 */
public class WPartialDateFieldExample extends WPanel
{
    private final WPartialDateField dateField = new WPartialDateField();

    private final WTextField text = new WTextField();
    private final WTextField day = new WTextField();
    private final WTextField month = new WTextField();
    private final WTextField year = new WTextField();

    /**
     * Creates a WPartialDateFieldExample.
     */
    public WPartialDateFieldExample()
    {
        WFieldLayout layout = new WFieldLayout();
        layout.setLabelWidth(30);
        text.setDisabled(true);
        text.setColumns(50);

        layout.addField("Enter any part(s) of a date", dateField);
        layout.addField("Text from entered date", text);
        add(layout);

        WButton copyBtn = new WButton("Copy date text");
        WButton btnGetDay = new WButton("Copy the day of month");
        WButton btnGetMonth = new WButton("Copy month value");
        WButton btnGetYear = new WButton("Copy year value");
        WButton btnGetDate = new WButton("Copy the Java Date");

        WPanel buttonPanel = new WPanel(WPanel.Type.FEATURE);
        buttonPanel.setLayout(new FlowLayout(Alignment.LEFT, 6, 0));
        buttonPanel.setMargin(new com.github.openborders.Margin(6,0,24,0));
        buttonPanel.add(copyBtn);
        buttonPanel.add(btnGetDay);
        buttonPanel.add(btnGetMonth);
        buttonPanel.add(btnGetYear);
        buttonPanel.add(btnGetDate);
        add(buttonPanel);

        WFieldSet setDateFieldSet= new WFieldSet("Set some or all of the date");
        add(setDateFieldSet);
        setDateFieldSet.setFrameType(WFieldSet.FrameType.NO_BORDER);
        WFieldLayout componentPanel = new WFieldLayout();
        componentPanel.setLabelWidth(30);
        componentPanel.addField("Day", day);
        componentPanel.addField("Month", month);
        componentPanel.addField("Year", year);
        setDateFieldSet.add(componentPanel);


        WPanel setDateButtonPanel = new WPanel(WPanel.Type.FEATURE);
        add(setDateButtonPanel);
        setDateButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT,6,0));
        setDateButtonPanel.setMargin(new com.github.openborders.Margin(6,0,0,0));

        final WButton btnSetDMY = new WButton("Set the day month year");
        setDateButtonPanel.add(btnSetDMY);

        WButton btnSetDate = new WButton("Set the Java Date(Today)");
        setDateButtonPanel.add(btnSetDate);

        add(new WHorizontalRule());
        add(new WHeading(WHeading.MAJOR, "Other properties"));
        layout = new WFieldLayout();
        layout.setLabelWidth(30);
        add(layout);
        WPartialDateField pdfPropField = new WPartialDateField();
        pdfPropField.setDisabled(true);
        layout.addField("Disabled partial date field", pdfPropField);
        pdfPropField = new WPartialDateField();
        pdfPropField.setDisabled(true);
        pdfPropField.setPartialDate(null, 7, 2015);
        layout.addField("Disabled partial date field with content", pdfPropField);
        pdfPropField = new WPartialDateField();
        pdfPropField.setMandatory(true);
        layout.addField("Mandatory partial date field", pdfPropField);
        pdfPropField = new WPartialDateField();
        pdfPropField.setToolTip("Any part of the date is fine");
        layout.addField("Partial date field with toolTip", pdfPropField);
        
        add(new WHorizontalRule());
        add(new WHeading(WHeading.MAJOR, "Read-only"));
        WPartialDateField roDate = new WPartialDateField();
        roDate.setReadOnly(true);
        layout = new WFieldLayout();
        add(layout);
        layout.addField("read-only no date", roDate);
        roDate = new WPartialDateField();
        roDate.setReadOnly(true);
        roDate.setDate(new Date());
        layout.addField("read-only today", roDate);
        roDate = new WPartialDateField();
        roDate.setReadOnly(true);
        roDate.setPartialDate(null, 11, 2013);
        layout.addField("read-only November 2013", roDate);
        roDate = new WPartialDateField();
        roDate.setReadOnly(true);
        roDate.setPartialDate(13, null, 2013);
        layout.addField("read-only 13th unknown month 2013", roDate);
        roDate = new WPartialDateField();
        roDate.setReadOnly(true);
        roDate.setPartialDate(13, 07, null);
        layout.addField("read-only 13th July", roDate);

        copyBtn.setAction(new Action()
        {
            public void execute(final ActionEvent event)
            {
                text.setText(dateField.getText());
            }
        });

        btnGetDay.setAction(new Action()
        {
            public void execute(final ActionEvent event)
            {
                Integer value = dateField.getDay();
                text.setText(value == null ? null : value.toString());
            }
        });

        btnGetMonth.setAction(new Action()
        {
            public void execute(final ActionEvent event)
            {
                Integer value = dateField.getMonth();
                text.setText(value == null ? null : value.toString());
            }
        });

        btnGetYear.setAction(new Action()
        {
            public void execute(final ActionEvent event)
            {
                Integer value = dateField.getYear();
                text.setText(value == null ? null : value.toString());
            }
        });

        btnGetDate.setAction(new Action()
        {
            public void execute(final ActionEvent event)
            {
                if (dateField.getDate() != null)
                {
                    text.setText(dateField.getDate().toString());
                }
                else
                {
                    text.setText("");
                }
            }
        });

        btnSetDMY.setAction(new Action()
        {
            public void execute(final ActionEvent event)
            {
                dateField.setPartialDate(parseInt(day.getText()), parseInt(month.getText()), parseInt(year.getText()));
            }
        });

        btnSetDate.setAction(new Action()
        {
            public void execute(final ActionEvent event)
            {
//                Calendar now = Calendar.getInstance();
//                dateField.setPartialDate(now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.MONTH) + 1, now.get(Calendar.YEAR));
                dateField.setDate(new Date());
            }
        });
    }

    /**
     * Attempts to parse an integer string.
     * @param text the text to parse
     * @return the parsed text, or null if it couldn't be parsed.
     */
    private Integer parseInt(final String text)
    {
        try
        {
            return new Integer(text);
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }
}
