package com.github.visualarray.control.components;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.github.visualarray.control.ControlPanel;
import com.github.visualarray.gui.components.VisualArray;

public class ThicknessPanel extends JPanel {
	private ControlPanel controlPanel;

	public ThicknessPanel(ControlPanel controlPanel, int defaultThickness) {
		this.controlPanel = controlPanel;

		add(new JLabel("Thickness:"));

		JNumberTextField thicknessField = new JNumberTextField(8,
				JNumberTextField.INTEGRAL, false);
		thicknessField.setNumber(defaultThickness);
		thicknessField.getDocument().addDocumentListener(
				new ThicknessFieldDocumentListener());
		add(thicknessField);
	}

	private class ThicknessFieldDocumentListener extends
			AbstractDocumentUpdateListener {
		@Override
		protected void updatePerformed(DocumentEvent e) {
			Document doc = e.getDocument();
			try {
				String text = doc.getText(0, doc.getLength());
				int thickness = Integer.parseInt(text);

				for (VisualArray va : controlPanel.getVisualArrayList()) {
					va.setThickness(thickness);
				}
				controlPanel.reset();
				controlPanel.log(this, "Thickness changed to " + thickness);
			} catch (BadLocationException | NumberFormatException ignore) { // ignore
			}
		}
	}

	private static final long serialVersionUID = 3925362435771732124L;
}
