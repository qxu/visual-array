package com.github.visualarray.control.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.github.visualarray.control.ControlPanel;

public class StopButton extends JButton implements ActionListener {
	private final ControlPanel controlPanel;

	public StopButton(ControlPanel controlPanel) {
		super("Stop");
		this.controlPanel = controlPanel;
		setEnabled(false);
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		controlPanel.stopSorter();
		controlPanel.log(this, e.getActionCommand() + " triggered");
	}

	private static final long serialVersionUID = -2501639915044373239L;
}
