package com.github.openborders.examples;

import com.github.openborders.WContainer;
import com.github.openborders.WTimeoutWarning;

import com.github.openborders.examples.common.ExplanatoryText;

public class WTimeoutWarningDefaultExample extends WContainer
{

    public WTimeoutWarningDefaultExample()
    {
        add(new ExplanatoryText("This is a demonstration of the 'session timeout' warning. It will display a session timeout message based on the default timeout for the session. A warning message will be displayed before the session ends. It does not actually end your session."));
        add(new WTimeoutWarning());
    }

}
