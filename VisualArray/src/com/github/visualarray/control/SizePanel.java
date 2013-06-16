package com.github.visualarray.control;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.github.visualarray.gui.JNumberTextField;
import com.github.visualarray.gui.components.VisualArray;

public class SizePanel extends JPanel
{
	private final ControlPanel controlPanel;

	public SizePanel(ControlPanel controlPanel, int defaultSize)
	{
		this.controlPanel = controlPanel;
		add(new JLabel("size:"));

		JNumberTextField sizeField = new JNumberTextField(8,
				JNumberTextField.NUMERIC, false);
		sizeField.setNumber(defaultSize);
		sizeField.getDocument().addDocumentListener(
				new SizeFieldDocumentListener());
		add(sizeField);
	}

	private class SizeFieldDocumentListener extends
			AbstractDocumentUpdateListener
	{
		@Override
		protected void updatePerformed(DocumentEvent e)
		{
			Document doc = e.getDocument();
			try
			{
				String text = doc.getText(0, doc.getLength());
				int size = Integer.parseInt(text);

				double[] values = controlPanel.getArrayBuilder().build(size);
				for(VisualArray va : controlPanel.getVisualArrayList())
				{
					va.setInitialValues(values);
				}
				controlPanel.reset();
			}
			catch(BadLocationException | NumberFormatException ignore)
			{ // ignore
			}
		}
	}

	private static final long serialVersionUID = -6755964296026667619L;
}
