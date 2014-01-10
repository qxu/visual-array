package com.github.visualarray.control.components;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.github.visualarray.control.ControlPanel;
import com.github.visualarray.gui.components.VisualArray;

public class HideVisualArrayOnClose implements WindowListener {
	private ControlPanel controlPanel;
	private VisualArray va;

	public HideVisualArrayOnClose(ControlPanel controlPanel, VisualArray va) {
		this.controlPanel = controlPanel;
		this.va = va;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		controlPanel.hideVisualArray(va);
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
