package com.github.visualarray;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.github.visualarray.gui.control.VAController;

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
		
		new VAController();
		
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
