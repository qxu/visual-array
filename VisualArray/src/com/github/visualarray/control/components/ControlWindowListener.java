package com.github.visualarray.control.components;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.github.visualarray.control.ControlPanel;

public class ControlWindowListener implements WindowListener {
	private ControlPanel controlPanel;

	public ControlWindowListener(ControlPanel controlPanel) {
		this.controlPanel = controlPanel;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		controlPanel.dispose();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		controlPanel.dispose();
	}

	@Override
	public void windowIconified(WindowEvent e) {
		controlPanel.getWindowController().setVisible(false);
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		controlPanel.getWindowController().setVisible(true);
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
		controlPanel.getWindowController().setVisible(true);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

}
