package com.github.openborders.examples;

import java.math.BigDecimal;
import java.util.List;

import com.github.openborders.ActionEvent;
import com.github.openborders.Request;
import com.github.openborders.WButton;
import com.github.openborders.WContainer;
import com.github.openborders.WField;
import com.github.openborders.WFieldLayout;
import com.github.openborders.WLabel;
import com.github.openborders.WNumberField;
import com.github.openborders.WTimeoutWarning;
import com.github.openborders.subordinate.Disable;
import com.github.openborders.subordinate.Enable;
import com.github.openborders.subordinate.Match;
import com.github.openborders.subordinate.Rule;
import com.github.openborders.subordinate.WSubordinateControl;
import com.github.openborders.validation.Diagnostic;
import com.github.openborders.validation.ValidatingAction;
import com.github.openborders.validation.WValidationErrors;

/**
 * This example is shows how WTimeoutWarning can be configured to show a warning at different times before the timeout
 * period and how the timeout period itself can be manipulated.
 * 
 * The timeout warning will not appear if the timeout period is not set or is set to 0. The warning must be set to
 * a period less then the timeout and defaults to 20 seconds. 
 *
 * @author Mark Reeves
 * @since 1.0.0
 */
public class WTimeoutWarningOptionsExample extends WContainer
{
    /** The absolute minimum warning period is fixed in all themes as the WCAG 2.0 minimum requirement of 20 seconds. */
    private static final int MIN_WARN = 20;
    /** The timeout must be greater than a theme-determined value otherwise the warning may not be able to be rendered.
     * This is never less than 30 seconds but may be longer. If your example doesn't work - check your theme.*/
    private static final int MIN_EXPIRY = 60;
    
    /** The timeout warning */
    private WTimeoutWarning warning = new WTimeoutWarning(0);
    
    /** a field to set the timeout period */
    private final WNumberField timeoutNumberField = new WNumberField();
    /** a field to set the warning period */
    private final WNumberField warnAtNumberField = new WNumberField();
    
    private final WButton saveButton = new WButton("Set timeout config");

    private final WValidationErrors errors = new WValidationErrors();
    public WTimeoutWarningOptionsExample()
    {
        add(warning);
        add(errors);
        
        final WFieldLayout layout = new WFieldLayout();
        add(layout);
        layout.setLabelWidth(25);

        timeoutNumberField.setMinValue(MIN_EXPIRY);
        timeoutNumberField.setNumber(MIN_EXPIRY);
        timeoutNumberField.setMandatory(true);
        
        WField timeoutField = layout.addField("Demonstration Timeout Period", timeoutNumberField);
        timeoutField.getLabel().setHint("Must be at least " + String.valueOf(MIN_EXPIRY));

        warnAtNumberField.setMinValue(MIN_WARN);
        WField warnField = layout.addField("Demonstration Warning Period", warnAtNumberField);
        warnField.getLabel().setHint("Must be at least "+String.valueOf(MIN_WARN)+" and less than the value of the demonstration timeout period");
        
        layout.addField((WLabel) null, saveButton);
        
        WSubordinateControl control = new WSubordinateControl();
        add(control);
        Rule rule = new Rule(new Match(timeoutNumberField, "\\d+"));
        rule.addActionOnFalse(new Disable(saveButton));
        rule.addActionOnTrue(new Enable(saveButton));
        control.addRule(rule);
        
        
        saveButton.setAction(new ValidatingAction(errors, layout)
        {
            public void validate(final List<Diagnostic> diags)
            {
                super.validate( diags);
                
                BigDecimal warnValue = warnAtNumberField.getValue();
                BigDecimal timeoutValue = timeoutNumberField.getValue();
                
                if(warnValue != null)
                {
                    if(warnValue.compareTo(timeoutValue) != -1)
                    {
                        diags.add(createErrorDiagnostic(warnAtNumberField,"The warning period must be less than the timeout period."));
                    }
                }
            }
            
            
            @Override
            public void executeOnValid(ActionEvent event)
            {
                BigDecimal timeout = timeoutNumberField.getNumber();
                if(timeout != null)
                {
                    warning.setTimeoutPeriod(timeout.intValue());
                }
                else
                {
                    warning.setTimeoutPeriod(0);
                }
                BigDecimal warn = warnAtNumberField.getNumber();
                if(warn != null)
                {
                    warning.setWarningPeriod(warn.intValue());
                }
            }
        });
    }
}
