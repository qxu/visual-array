package com.github.visualarray.control;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.github.visualarray.gui.JNumberTextField;

public class DelayPanel extends JPanel
{
	private final ControlPanel controlPanel;

	public DelayPanel(ControlPanel controlPanel, double defaultDelay)
	{
		this.controlPanel = controlPanel;
		
		add(new JLabel("step delay (ms):"));
		
		JNumberTextField delayField = new JNumberTextField(8,
				JNumberTextField.DECIMAL, false);
		delayField.setNumber(defaultDelay);
		delayField.getDocument().addDocumentListener(
				new DelayFieldDocumentListener());
		add(delayField);
	}

	private class DelayFieldDocumentListener extends
			AbstractDocumentUpdateListener
	{
		@Override
		protected void updatePerformed(DocumentEvent e)
		{
			Document doc = e.getDocument();
			try
			{
				String text = doc.getText(0, doc.getLength());
				double delay = Double.parseDouble(text);
				Sorter sorter = controlPanel.getSorter();
				sorter.setStepDelay((long)(delay * 1000000));
			}
			catch(BadLocationException | NumberFormatException ignore)
			{ // ignore
			}
		}
	}

	private static final long serialVersionUID = 5450824356844477905L;
}
