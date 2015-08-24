package com.github.openborders.validation; 

import junit.framework.Assert;

import org.junit.Test;

import com.github.openborders.UIContext;
import com.github.openborders.UIContextImpl;
import com.github.openborders.WComponent;
import com.github.openborders.WLabel;
import com.github.openborders.WTextField;
import com.github.openborders.validation.Diagnostic;
import com.github.openborders.validation.DiagnosticImpl;

/**
 * DiagnosticImpl_Test - unit tests for {@link DiagnosticImpl}.
 * 
 * @author Yiannis Paschalidis
 * @since 1.0.0
 */
public class DiagnosticImpl_Test
{
    @Test
    public void testGetSeverity()
    {
        final int severity = Diagnostic.INFO;
        DiagnosticImpl diag = new DiagnosticImpl(severity, new WTextField(), "dummy");
        Assert.assertEquals("Incorrect severity", severity, diag.getSeverity());
    }
    
    @Test
    public void testGetContext()
    {
        final UIContext uic = new UIContextImpl();
        DiagnosticImpl diag = new DiagnosticImpl(Diagnostic.INFO, uic, new WTextField(), "dummy");
        Assert.assertSame("Incorrect UI context", uic, diag.getContext());
    }    
    
    @Test
    public void testGetComponent()
    {
        final WComponent component = new WTextField();
        DiagnosticImpl diag = new DiagnosticImpl(Diagnostic.INFO, component, "dummy");
        Assert.assertSame("Incorrect component", component, diag.getComponent());
    }
    
    @Test
    public void testGetDescription()
    {
        final UIContext uic = new UIContextImpl();
        final WTextField input = new WTextField();
        final String noArgsMessage = "The field is required";
        final String fieldArgMessage = "The field ''{0}'' is required";
        
        // Test with no formatting
        DiagnosticImpl diag = new DiagnosticImpl(Diagnostic.INFO, uic, input, noArgsMessage);
        Assert.assertEquals("Incorrect description text", noArgsMessage, diag.getDescription());
        
        // Test with formatting, but missing label text should default to empty String
        diag = new DiagnosticImpl(Diagnostic.INFO, uic, input, fieldArgMessage, input);
        Assert.assertEquals("Incorrect description text", "The field '' is required", diag.getDescription());
        
        // Test with formatting with accessible text set
        input.setAccessibleText("a");
        Assert.assertEquals("Incorrect description text", "The field 'a' is required", diag.getDescription());
        
        // Test with label set
        WLabel label = new WLabel("bc", input);
        Assert.assertEquals("Incorrect description text", "The field 'bc' is required", diag.getDescription());
        
        // Test with label set, with a colon at the end
        label.setText("def:");
        Assert.assertEquals("Incorrect description text", "The field 'def' is required", diag.getDescription());
    }
}
