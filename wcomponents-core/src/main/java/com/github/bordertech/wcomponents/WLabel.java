package com.github.bordertech.wcomponents;

import com.github.bordertech.wcomponents.util.HtmlSanitizerUtil;
import com.github.bordertech.wcomponents.util.I18nUtilities;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Objects;

/**
 * <p>
 * The WLabel component is used to display a textual label for an input field. A WLabel is associated with an input
 * field provided in the labels constructor, or via the {@link #setForComponent(WComponent) setForComponent} method.</p>
 *
 * @author James Gifford
 * @since 1.0.0
 */
public class WLabel extends AbstractMutableContainer implements AjaxTarget {

	/**
	 * Creates a new WLabel. Sets the label text, access key and the component that label is for.
	 *
	 * @param text The label text.
	 * @param accessKey The access key for the label.
	 * @param forComponent The component that this label is for.
	 */
	public WLabel(final String text, final char accessKey, final WComponent forComponent) {
		this(text, forComponent);
		getComponentModel().accessKey = accessKey;
	}

	/**
	 * Creates a new WLabel with the specified text and component that the label is for.
	 *
	 * @param text the label text.
	 * @param forComponent the component that this label is for.
	 */
	public WLabel(final String text, final WComponent forComponent) {
		this(text);
		getComponentModel().forComponent = forComponent;

		if (forComponent instanceof AbstractWComponent) {
			((AbstractWComponent) forComponent).setLabel(this);
		}
	}

	/**
	 * Creates a new WLabel with the specified text and access key.
	 *
	 * @param text The label text.
	 * @param accessKey The access key for the label's <code>forComponent</code>.
	 */
	public WLabel(final String text, final char accessKey) {
		this(text);
		getComponentModel().accessKey = accessKey;
	}

	/**
	 * Creates a new WLabel with the specified text.
	 *
	 * @param text The label text.
	 */
	public WLabel(final String text) {
		getComponentModel().text = text;
	}

	/**
	 * Creates an empty WLabel.
	 */
	public WLabel() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		if (getIdName() == null && getForComponent() != null && getForComponent().getIdName() != null) {
			return getForComponent().getId() + ID_FRAMEWORK_ASSIGNED_SEPERATOR + "lbl";
		}
		return super.getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(final WComponent component) {
		super.add(component);
		if (component instanceof Input) {
			setForComponent(component);
		}
	}

	// ================================
	// Attributes
	/**
	 * @return the label text.
	 */
	public String getText() {
		String text = I18nUtilities.format(null, getComponentModel().text);
		if (!isEncodeText() && isSanitizeOnOutput()) {
			return sanitizeOutputText(text);
		}
		return text;
	}

	/**
	 * Sets the label's text.
	 *
	 * @param text the label text, using {@link MessageFormat} syntax.
	 * @param args optional arguments for the message format string.
	 */
	public void setText(final String text, final Serializable... args) {
		Serializable currText = getComponentModel().text;
		Serializable textToBeSet = I18nUtilities.asMessage(text, args);

		if (!Objects.equals(textToBeSet, currText)) {
			getOrCreateComponentModel().text = textToBeSet;
		}
	}

	/**
	 * Sets the label "hint" text, which can be used to provide additional information to the user.
	 *
	 * @param hint the hint text, using {@link MessageFormat} syntax.
	 * @param args optional arguments for the message format string.
	 */
	public void setHint(final String hint, final Serializable... args) {
		Serializable currHint = getComponentModel().hint;
		Serializable hintToBeSet = I18nUtilities.asMessage(hint, args);

		if (!Objects.equals(hintToBeSet, currHint)) {
			getOrCreateComponentModel().hint = hintToBeSet;
		}
	}

	/**
	 * @return the label hint text.
	 */
	public String getHint() {
		return I18nUtilities.format(null, getComponentModel().hint);
	}

	/**
	 * Sets the component that this label is associated with.
	 *
	 * @param forComponent the associated component.
	 */
	public void setForComponent(final WComponent forComponent) {
		getOrCreateComponentModel().forComponent = forComponent;

		if (forComponent instanceof AbstractWComponent) {
			((AbstractWComponent) forComponent).setLabel(this);
		}
	}

	/**
	 * @return the component that this label is associated with.
	 */
	public WComponent getForComponent() {
		return getComponentModel().forComponent;
	}

	/**
	 * @return the id of the component that this label is for.
	 */
	public String getLabelFor() {
		WComponent forComponent = getComponentModel().forComponent;

		if (forComponent == null) {
			return null;
		}

		return forComponent.getId();
	}

	// ================================
	// Access key
	/**
	 * <p>
	 * Set the key (in combination with Alt) that should give focus to this label. The action that occurs when the label
	 * is focussed depends on the type of component this label is for (the <code>forComponent</code>). For example,
	 * WTextField will focus the cursor in the text input, WCheckBox will toggle the check box selection. Access keys
	 * are not case sensitive.
	 * </p>
	 * <p>
	 * Note: Setting the access key also sets the shared title of the component to indicate the access key combination.
	 * </p>
	 *
	 * @param accesskey The key (in combination with the Alt key) that activates this label.
	 * @since 1.0.0
	 */
	public void setAccessKey(final char accesskey) {
		getOrCreateComponentModel().accessKey = accesskey;
	}

	/**
	 * The access key is a shortcut key that will focus the label when used in combination with the Alt key.
	 *
	 * @return The key that in combination with Alt will focus this label.
	 * @since 1.0.0
	 */
	public char getAccessKey() {
		return getComponentModel().accessKey;
	}

	/**
	 * Returns the accesskey character as a String. If the character is not a letter or digit then <code>null</code> is
	 * returned.
	 *
	 * @return The accesskey character as a String (may be <code>null</code>).
	 */
	public String getAccessKeyAsString() {
		char accessKey = getComponentModel().accessKey;

		if (Character.isLetterOrDigit(accessKey)) {
			return String.valueOf(accessKey);
		}

		return null;
	}

	/**
	 * Indicates whether the heading text needs to be encoded.
	 *
	 * @return true if the text needs to be encoded, false if not.
	 */
	public boolean isEncodeText() {
		return isFlagSet(ComponentModel.ENCODE_TEXT_FLAG);
	}

	/**
	 * <p>
	 * Sets whether the heading text needs to be encoded.</p>
	 *
	 * <p>
	 * When setting <code>encodeText</code> to <code>false</code>, it then becomes the responsibility of the application
	 * to ensure that the text does not contain any characters which need to be escaped.</p>
	 *
	 * @param encodeText true if the text needs to be encode, false if not.
	 */
	public void setEncodeText(final boolean encodeText) {
		setFlag(ComponentModel.ENCODE_TEXT_FLAG, encodeText);
	}

	/**
	 * @return a String representation of this component, for debugging purposes.
	 */
	@Override
	public String toString() {
		String text = getText();
		text = text == null ? "null" : ('"' + text + '"');
		return toString(text, 1, 1);
	}

	/**
	 * Pass true if you need to run the HTML sanitizer on <em>any</em> output. This is only needed if the text is not
	 * encoded as other cases the output will be XML encoded.
	 *
	 * @param sanitize true if output sanitization is required.
	 */
	public void setSanitizeOnOutput(final boolean sanitize) {
		if (sanitize != isSanitizeOnOutput()) {
			getOrCreateComponentModel().sanitizeOnOutput = sanitize;
		}
	}

	/**
	 * @return true if this text area is to be sanitized on output.
	 */
	public boolean isSanitizeOnOutput() {
		return getComponentModel().sanitizeOnOutput;
	}

	/**
	 * @param text the output text to sanitize
	 * @return the sanitized text
	 */
	protected String sanitizeOutputText(final String text) {
		return HtmlSanitizerUtil.sanitizeOutputText(text);
	}

	// --------------------------------
	// Extrinsic state management
	/**
	 * Creates a new model appropriate for this component.
	 *
	 * @return a new {@link LabelModel}.
	 */
	@Override
	protected LabelModel newComponentModel() {
		return new LabelModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override // For type safety only
	protected LabelModel getComponentModel() {
		return (LabelModel) super.getComponentModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override // For type safety only
	protected LabelModel getOrCreateComponentModel() {
		return (LabelModel) super.getOrCreateComponentModel();
	}

	/**
	 * Holds the extrinsic state information of a WLabel.
	 */
	public static class LabelModel extends ComponentModel {

		/**
		 * The label text.
		 */
		private Serializable text;

		/**
		 * Provides further hint text to the user, in addition to the label text.
		 */
		private Serializable hint;

		/**
		 * The component which this label is for.
		 */
		private WComponent forComponent;

		/**
		 * The key shortcut that activates the label's <code>forComponent</code>.
		 */
		private char accessKey = '\0';

		/**
		 * Indicates if the label should be HTML sanitized. This only needs to be true if the content is HTML
		 * <strong>and</strong> of unknown provenance.
		 */
		private boolean sanitizeOnOutput;
	}
}
