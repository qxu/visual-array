package com.github.visualarray.control.components;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.github.visualarray.control.ControlPanel;
import com.github.visualarray.gui.components.VisualArray;

public class PaddingPanel extends JPanel
{
	private ControlPanel controlPanel;

	public PaddingPanel(ControlPanel controlPanel, int defaultPadding)
	{
		this.controlPanel = controlPanel;

		add(new JLabel("Padding:"));

		JNumberTextField paddingField = new JNumberTextField(8,
				JNumberTextField.INTEGRAL, false);
		paddingField.setNumber(defaultPadding);
		paddingField.getDocument().addDocumentListener(
				new PaddingFieldDocumentListener());
		add(paddingField);
	}

	private class PaddingFieldDocumentListener extends
			AbstractDocumentUpdateListener
	{
		@Override
		protected void updatePerformed(DocumentEvent e)
		{
			Document doc = e.getDocument();
			try
			{
				String text = doc.getText(0, doc.getLength());
				int padding = Integer.parseInt(text);

				for(VisualArray va : controlPanel.getVisualArrayList())
				{
					va.setPadding(padding);
				}
				controlPanel.reset();
				controlPanel.log(this, "Padding changed to " +  padding);
			}
			catch(BadLocationException | NumberFormatException ignore)
			{ // ignore
			}
		}
	}

	private static final long serialVersionUID = 3558691930742832688L;
}
