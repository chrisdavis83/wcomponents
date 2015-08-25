package com.github.openborders.examples.validation; 

import com.github.openborders.Action;
import com.github.openborders.ActionEvent;
import com.github.openborders.MessageContainer;
import com.github.openborders.WButton;
import com.github.openborders.WCardManager;
import com.github.openborders.WComponent;
import com.github.openborders.WContainer;
import com.github.openborders.WMessageBox;
import com.github.openborders.WMessages;
import com.github.openborders.WPanel;
import com.github.openborders.layout.BorderLayout;
import com.github.openborders.validation.ValidatingAction;
import com.github.openborders.validation.WValidationErrors;

/**
 * This class is a support class used by some of the validation examples as a
 * container for testing the validation logic contained in another WComponent.
 * This class has a button called "validate" to call the validation logic on
 * the contained component, and a box for displaying any errors found. If
 * no validation errors are found then a "success" dialog is displayed.
 * 
 * @author Martin Shevchenko 
 */
public class ValidationContainer extends WPanel implements MessageContainer
{
    /** The WMessages instance used to display messages. */
    private final WMessages messages = new WMessages();
    
    /** A button to trigger form submission (and validation if an action is set). */
    private final WButton validateBtn = new WButton("Validate");
    
    /** The component to validate. */
    private final WComponent componentToValidate;
    
    /** The card manager used to toggle between the success screen and the validaiton component. */
    private final WCardManager cardManager = new WCardManager();
    
    /** The success screen. */
    private final SuccessPanel successPanel = new SuccessPanel();
    
    /** The panel containing the main controls. */
    private final WPanel mainPanel = new WPanel();
    
    /**
     * Creates a ValidationContainer.
     * @param componentToValidate the component to validate.
     */
    public ValidationContainer(final WComponent componentToValidate)
    {
        add(messages);
        add(cardManager);
        
        this.componentToValidate = componentToValidate;
        
        cardManager.add(mainPanel);
        
        setValidatingAction(new ValidatingAction(messages.getValidationErrors(), componentToValidate) 
        {
            @Override
            public void executeOnValid(final ActionEvent event)
            {
                showSuccessDialog();
            }
        });

        WButton resetBtn = new WButton("Reset");
        resetBtn.setAction(new Action() 
        {
            public void execute(final ActionEvent event)
            {
            	messages.reset();
                componentToValidate.reset();
            }
        });

        mainPanel.setDefaultSubmitButton(validateBtn);
        mainPanel.add(componentToValidate);
        
        WPanel buttonPanel = new WPanel(WPanel.Type.FEATURE);
        mainPanel.add(buttonPanel);
        buttonPanel.setMargin(new com.github.openborders.Margin(12, 0, 0, 0));
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(validateBtn,BorderLayout.EAST);
        buttonPanel.add(resetBtn,BorderLayout.WEST);
        
        cardManager.add(successPanel);
    }
    
    /** {@inheritDoc} */
    public WMessages getMessages()
    {
        return messages;
    }
    
    /**
     * @return the component to validate.
     */
    public WComponent getComponentToValidate()
    {
        return componentToValidate;
    }
    
    /**
     * @return the validation errors box.
     */
    public WValidationErrors getErrorsBox()
    {
        return messages.getValidationErrors();
    }
    
    /**
     * Sets the action for the validation button. The action should be a 
     * {@link ValidatingAction} for validation to actually occur.
     * 
     * @param action the button action.
     */
    public void setValidatingAction(final Action action)
    {
        validateBtn.setAction(action);
    }
    
    /**
     * Displays the success dialog.
     */
    public void showSuccessDialog()
    {
        messages.reset();
        cardManager.makeVisible(successPanel);
    }
    
    /**
     * This component is used to display a "success" message
     * when the contained component passes validation.
     */
    private final class SuccessPanel extends WContainer
    {
        /** Creates a SuccessDialog. */
        public SuccessPanel()
        {
            add(new WMessageBox(WMessageBox.SUCCESS, "Everything is valid!"));
            
            WButton okBtn = new WButton("Ok");
            okBtn.setAction(new Action() 
            {
                public void execute(final ActionEvent event)
                {
                    cardManager.makeVisible(mainPanel);
                }
            });
            
            add(okBtn);
        }
    }
}
