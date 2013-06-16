package com.github.visualarray.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ResetButton extends JButton implements ActionListener
{
	private final ControlPanel controlPanel;
	
	public ResetButton(ControlPanel controlPanel)
	{
		this.controlPanel = controlPanel;
		setText("reset");
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		controlPanel.reset();
	}
	
	private static final long serialVersionUID = -7778996205946637084L;
}
