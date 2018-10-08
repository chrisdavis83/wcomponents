package com.github.bordertech.wcomponents.test.components;

import com.github.bordertech.wcomponents.ActionEvent;
import com.github.bordertech.wcomponents.Message;
import com.github.bordertech.wcomponents.WApplication;
import com.github.bordertech.wcomponents.WButton;
import com.github.bordertech.wcomponents.WComponent;
import com.github.bordertech.wcomponents.WFieldLayout;
import com.github.bordertech.wcomponents.WMessages;
import com.github.bordertech.wcomponents.WMultiFileWidget;
import com.github.bordertech.wcomponents.WPanel;
import com.github.bordertech.wcomponents.validation.ValidatingAction;
import static com.github.bordertech.wcomponents.WFieldLayout.LAYOUT_FLAT;

public class WMultiFileWidgetUI extends WApplication {

	private WMessages messages = new WMessages();
	private WPanel panel = new WPanel();
	private WMultiFileWidget wMultiFileWidget = new WMultiFileWidget();

	public WMultiFileWidgetUI() {
		panel.add(messages);
		WFieldLayout fieldLayout = new WFieldLayout(LAYOUT_FLAT);
		fieldLayout.setTitle("this is a bunch of fields");
		fieldLayout.addField("this is a textupload file widget field", wMultiFileWidget);
		wMultiFileWidget.setMandatory(true);

		panel.add(fieldLayout);
		add(createWButton());
		add(panel);
	}

	private WComponent createWButton() {
		WButton button = new WButton("Validate button");
		button.setAction(new ValidatingAction(messages.getValidationErrors(), panel) {
			@Override
			public void executeOnValid(ActionEvent event) {
				messages.addMessage(new Message(Message.INFO_MESSAGE, "Success"));
			}
		});

		return button;
	}

	public WMultiFileWidget getwMultiFileWidget() {
		return wMultiFileWidget;
	}
}