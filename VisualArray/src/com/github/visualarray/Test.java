package com.github.visualarray;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.github.visualarray.control.ControlPanel;
import com.github.visualarray.gui.components.DesktopVars;

public class Test
{
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
		

		JFrame cf = new JFrame("Visual Array");

		ControlPanel vas = new ControlPanel(cf);
		
		cf.add(vas);
		cf.pack();
		int cfXPos = DesktopVars.DESKTOP_X_MAX - cf.getWidth();
		int cfYPos = DesktopVars.DESKTOP_Y_MAX - cf.getHeight();
		cf.setLocation(cfXPos, cfYPos);
		cf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		cf.setVisible(true);
		
		try
		{
			Thread.sleep(60000);
		}
		catch(InterruptedException e)
		{
		}
		System.exit(0);
	}
}
