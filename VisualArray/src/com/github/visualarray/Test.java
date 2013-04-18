package com.github.visualarray;

import com.github.visualarray.gui.VAController;

public class Test
{
	public static void main(String[] args)
	{
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
