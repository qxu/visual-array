package com.github.visualarray;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.github.visualarray.control.ControlPanel;
import com.github.visualarray.gui.components.DesktopVars;

public class Freedom
{
	private JFrame frame;
	private ControlPanel panel;
	
	public Freedom()
	{
		frame = new JFrame("Visual Array");
		initControlPanel();
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher()
		{
			@Override
			public boolean dispatchKeyEvent(KeyEvent e)
			{
				if(e.getID() == KeyEvent.KEY_PRESSED)
				{
					if(e.getKeyCode() == KeyEvent.VK_F5)
					{
						initControlPanel();
					}
				}
				return false;
			}
		});
	}
	
	private void initControlPanel()
	{
		if(panel != null)
		{
			frame.remove(panel);
		}
		panel = new ControlPanel(frame);
		frame.add(panel);
		frame.setResizable(false);
		frame.pack();
		int cfXPos = DesktopVars.DESKTOP_X_MAX - frame.getWidth();
		int cfYPos = DesktopVars.DESKTOP_Y_MAX - frame.getHeight();
		frame.setLocation(cfXPos, cfYPos);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1)
		{
		}
		
		new Freedom();
	}
}
