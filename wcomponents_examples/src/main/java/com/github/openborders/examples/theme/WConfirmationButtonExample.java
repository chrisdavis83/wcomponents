package com.github.openborders.examples.theme;

import com.github.openborders.Action;
import com.github.openborders.ActionEvent;
import com.github.openborders.WButton;
import com.github.openborders.WCancelButton;
import com.github.openborders.WConfirmationButton;
import com.github.openborders.WContainer;
import com.github.openborders.WFieldLayout;
import com.github.openborders.WLabel;
import com.github.openborders.WPanel;
import com.github.openborders.WTextField;
import com.github.openborders.layout.FlowLayout;

/**
 * <p>An example showing use of a {@link WConfirmationButton}. Rendered as both a button and a link</p>
 * <p>This component is a specialised version of a {@link WButton} that
 * provides additional client-side functionality commonly associated with a
 * "cancel" button.</p>
 *
 * <p>When a user presses the button, it displays a confirmation prompt
 * before posting the form to the server.</p>
 *
 * @author Martin Shevchenko
 * @since 1.0.0
 */
public class WConfirmationButtonExample extends WContainer
{
    /** text field. */
    private final WTextField text;

    /**
     * Creates a WConfirmationButtonExample.
     */
    public WConfirmationButtonExample()
    {
        WFieldLayout layout = new WFieldLayout();
        layout.setLabelWidth(25);
        add(layout);

        text = new WTextField();
        layout.addField("Enter some text",text);

        WConfirmationButton clear = new WConfirmationButton("Clear");
        clear.setMessage("Are you really really sure?");

        WConfirmationButton clearLink = new WConfirmationButton("Clear");
        clearLink.setRenderAsLink(true);

        WPanel buttonPanel = new WPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT,6,0, FlowLayout.ContentAlignment.BASELINE));
        buttonPanel.add(clear);
        buttonPanel.add(clearLink);
        layout.addField((WLabel)null, buttonPanel);

        WConfirmationButton ieConfirmButton = new WConfirmationButton("IE Test Confirm");
        ieConfirmButton.setMessage("This should not appear. If it does IE is broken");
        ieConfirmButton.setDisabled(true);
        buttonPanel.add(ieConfirmButton);
        text.setDefaultSubmitButton(ieConfirmButton);

        Action clearAction = new Action()
        {
            public void execute(final ActionEvent event)
            {
                doClearText();
            }
        };

        clear.setAction(clearAction);
        clearLink.setAction(clearAction);
    }

    /**
     * Clears the contents of the text field.
     */
    public void doClearText()
    {
        text.setText(null);
    }
}
