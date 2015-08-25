package com.github.openborders.examples;

import com.github.openborders.Action;
import com.github.openborders.ActionEvent;
import com.github.openborders.Margin;
import com.github.openborders.WButton;
import com.github.openborders.WDropdown;
import com.github.openborders.WFieldLayout;
import com.github.openborders.WHeading;
import com.github.openborders.WPanel;
import com.github.openborders.WStyledText;
import com.github.openborders.WTextArea;

/**
 * Demonstrate the {@link WStyledText} configuration options.
 * 
 * @author Jonathan Austin
 * @since 1.0.0
 */
public class WStyledTextOptionsExample extends WPanel
{

    /**
     * Construct the example.
     */
    public WStyledTextOptionsExample()
    {

        add(new WHeading(WHeading.MAJOR, "WStyledText Options"));

        WFieldLayout layout = new WFieldLayout();
        layout.setLabelWidth(30);
        add(layout);

        final WDropdown type = new WDropdown(WStyledText.Type.values());
        layout.addField("Type", type);

        final WDropdown mode = new WDropdown(WStyledText.WhitespaceMode.values());
        layout.addField("Whitespace mode", mode);

        final WTextArea text = new WTextArea();
        text.setRows(5);
        text.setText("Some text");
        layout.addField("Text", text).setInputWidth(100);

        final WStyledText styled = new WStyledText();

        WButton button = new WButton("apply");
        add(button);

        button.setAction(new Action()
        {
            public void execute(final ActionEvent event)
            {
                styled.reset();
                styled.setType((WStyledText.Type) type.getSelected());
                styled.setWhitespaceMode((WStyledText.WhitespaceMode) mode.getSelected());
                styled.setText(text.getText());
            }
        });

        add(new WHeading(WHeading.MAJOR, "Styled Text"));

        WPanel panel = new WPanel(WPanel.Type.BOX);
        panel.setMargin(new Margin(20));
        add(panel);

        panel.add(styled);

        setDefaultSubmitButton(button);
    }

}
