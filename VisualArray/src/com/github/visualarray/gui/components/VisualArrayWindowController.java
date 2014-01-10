package com.github.visualarray.gui.components;

import java.awt.Window;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JFrame;

import com.github.visualarray.control.ControlPanel;
import com.github.visualarray.control.components.HideVisualArrayOnClose;

public class VisualArrayWindowController {
	private ControlPanel controlPanel;
	private Window sharedOwner;
	private Map<VisualArray, VisualArrayWindow> vaWindowMap;

	public VisualArrayWindowController(ControlPanel controlPanel) {
		this.controlPanel = controlPanel;
		sharedOwner = new JFrame();
		this.vaWindowMap = new LinkedHashMap<>();
	}

	public boolean containsVisualArray(VisualArray va) {
		return vaWindowMap.containsKey(va);
	}

	public void addVisualArray(VisualArray va) {
		VisualArrayWindow window = new VisualArrayWindow(sharedOwner,
				controlPanel, va);
		window.pack();
		window.addWindowListener(new HideVisualArrayOnClose(controlPanel, va));

		vaWindowMap.put(va, window);
		update();
	}

	public void removeVisualArray(VisualArray va) {
		VisualArrayWindow window = vaWindowMap.remove(va);
		if (window != null) {
			window.setVisible(false);
			window.dispose();
			update();
		}
	}

	public void addAllVisualArrays(Collection<VisualArray> vaCollection) {
		for (VisualArray va : vaCollection) {
			addVisualArray(va);
		}
	}

	public void removeAllVisualArrays() {
		if (!vaWindowMap.isEmpty()) {
			for (VisualArrayWindow window : vaWindowMap.values()) {
				window.setVisible(false);
				window.dispose();
			}
			vaWindowMap.clear();
			update();
		}
	}

	public void setVisible(boolean b) {
		for (VisualArrayWindow window : vaWindowMap.values()) {
			window.setVisible(b);
		}
		controlPanel.getOwner().toFront();
	}

	public void dispose() {
		removeAllVisualArrays();
		sharedOwner.dispose();
	}

	public void update() {
		int maxHeight = 0;
		int x = DesktopVars.DESKTOP_X;
		int y = DesktopVars.DESKTOP_Y;
		for (VisualArrayWindow window : vaWindowMap.values()) {
			window.pack();

			int width = window.getWidth();
			int nextX = x + width;

			if (nextX > DesktopVars.DESKTOP_X_MAX) {
				x = 0;
				nextX = width;

				int nextY = y + maxHeight;
				y = nextY < DesktopVars.DESKTOP_Y_MAX ? nextY : 0;

				maxHeight = 0;
			}

			window.setLocation(x, y);
			x = nextX;
			int height = window.getHeight();
			if (height > maxHeight) {
				maxHeight = height;
			}

			if (!window.isVisible()) {
				window.setVisible(true);
				controlPanel.getOwner().toFront();
			}
		}
	}
}
