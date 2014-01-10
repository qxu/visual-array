package com.github.visualarray;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.github.visualarray.control.ControlPanel;
import com.github.visualarray.gui.components.DesktopVars;

public class Freedom {
	private JFrame frame;

	public Freedom() {
		initControlFrame();

		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(new KeyEventDispatcher() {
					@Override
					public boolean dispatchKeyEvent(KeyEvent e) {
						if (e.getID() == KeyEvent.KEY_PRESSED) {
							if (e.getKeyCode() == KeyEvent.VK_F5) {
								frame.dispose();
								frame = null;
								initControlFrame();
							}
						}
						return false;
					}
				});
	}

	private void initControlFrame() {
		frame = new JFrame("Visual Array");
		ControlPanel panel = new ControlPanel(frame);
		frame.add(panel);
		frame.setResizable(false);
		frame.pack();
		int cfXPos = DesktopVars.DESKTOP_X_MAX - frame.getWidth() - 5;
		int cfYPos = DesktopVars.DESKTOP_Y_MAX - frame.getHeight() - 5;
		frame.setLocation(cfXPos, cfYPos);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
		}

		new Freedom();
	}
}
