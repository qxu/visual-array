package com.github.visualarray.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class StopButton extends JButton implements ActionListener
{
	private final ControlPanel controlPanel;
	
	public StopButton(ControlPanel controlPanel)
	{
		super("stop");
		this.controlPanel = controlPanel;
		setEnabled(false);
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		controlPanel.stopSorter();
	}
	
	private static final long serialVersionUID = -2501639915044373239L;
}
