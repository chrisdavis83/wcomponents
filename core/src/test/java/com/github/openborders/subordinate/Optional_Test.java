package com.github.openborders.subordinate;

import junit.framework.Assert;

import org.junit.Test;

import com.github.openborders.AbstractWComponent;
import com.github.openborders.AbstractWComponentTestCase;
import com.github.openborders.SubordinateTarget;
import com.github.openborders.WLabel;
import com.github.openborders.subordinate.AbstractAction;
import com.github.openborders.subordinate.Optional;

/**
 * Test cases for {@link Optional}.
 * 
 * @author Jonathan Austin
 * @since 1.0.0
 */
public class Optional_Test extends AbstractWComponentTestCase
{
    @Test
    public void testConstructor()
    {
        SubordinateTarget target = new MyTarget();
        Optional action = new Optional(target);

        Assert.assertEquals("Value for Optional should be false", Boolean.FALSE, action.getValue());
        Assert.assertEquals("Target for Optional should be the target", target, action.getTarget());
    }

    @Test
    public void testActionType()
    {
        Optional action = new Optional(new MyTarget());
        Assert.assertEquals("Incorrect Action Type", AbstractAction.ActionType.OPTIONAL, action.getActionType());
    }

    @Test
    public void testToString()
    {
        MyTarget target = new MyTarget();

        Optional action = new Optional(target);
        Assert.assertEquals("Incorrect toString for action", "set MyTarget optional", action.toString());

        WLabel label = new WLabel("test label", target);
        Assert.assertEquals("Incorrect toString for action with a label", "set " + label.getText() + " optional", action.toString());
    }

    /**
     * Test component that implements the SubordinateTarget interface.
     */
    private static class MyTarget extends AbstractWComponent implements SubordinateTarget
    {
    }
}
