package com.github.visualarray.control.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.github.visualarray.control.ControlPanel;

public class ResetButton extends JButton implements ActionListener {
	private final ControlPanel controlPanel;

	public ResetButton(ControlPanel controlPanel) {
		this.controlPanel = controlPanel;
		setText("Reset");
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		controlPanel.reset();
		controlPanel.log(this, e.getActionCommand() + " triggered");
	}

	private static final long serialVersionUID = -7778996205946637084L;
}
