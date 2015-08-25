package com.github.openborders.examples; 

import com.github.openborders.Request;
import com.github.openborders.WButton;
import com.github.openborders.WContainer;
import com.github.openborders.WDropdown;
import com.github.openborders.WText;

/** 
 * Demonstrates how the application/portal preference parameters
 * can be accessed from WComponents.  
 * 
 * @author Martin Shevchenko
 */
public class AppPreferenceParameterExample extends WContainer
{
    /** The drop down which used to display the selection. */
    private final WDropdown stateSelector = new WDropdown();
    
    /** A button to reset the selection back to the preferred selection. */
    private final WButton resetButton = new WButton("Reset to preferred selection");

    /**
     * Creates a AppPreferenceParameterExample.
     */
    public AppPreferenceParameterExample()
    {
        add(new WText("State:"));
        add(stateSelector);
        
        add(resetButton);
        
        stateSelector.setOptions(new String[] {null, "ACT", "NSW", "VIC"});
    }
    
    /**
     * Override preparePaintComponent to set the initial selection from the app preferences.
     * The selection is set the first time the example is accessed or when the reset button is used.
     * 
     * @param request the request being responded to.
     */
    @Override
    public void preparePaintComponent(final Request request)
    {
        if (!this.isInitialised() || resetButton.isPressed())
        {
            String preferredState = request.getAppPreferenceParameter("example.preferred.state");
            stateSelector.setSelected(preferredState);
            this.setInitialised(true);
        }
    }
}
