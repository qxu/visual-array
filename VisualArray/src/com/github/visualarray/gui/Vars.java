package com.github.visualarray.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

public final class Vars
{
	private final static int SCREEN_WIDTH;
	private final static int SCREEN_HEIGHT;
	
	public static int getScreenWidth()
	{
		return SCREEN_WIDTH;
	}

	public static int getScreenHeight()
	{
		return SCREEN_HEIGHT;
	}
	
	static
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		SCREEN_WIDTH = (int)screenSize.getWidth();
		SCREEN_HEIGHT = (int)screenSize.getHeight();
	}
	
	private Vars()
	{ // no instantiation
	}
}
