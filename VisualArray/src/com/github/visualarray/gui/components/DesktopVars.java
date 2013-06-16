package com.github.visualarray.gui.components;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

public class DesktopVars
{
	public static final int DESKTOP_WIDTH;
	public static final int DESKTOP_HEIGHT;
	public static final int DESKTOP_X;
	public static final int DESKTOP_Y;
	public static final int DESKTOP_X_MAX;
	public static final int DESKTOP_Y_MAX;

	private static final Rectangle DESKTOP_BOUNDS;
	
	public static Rectangle getBounds()
	{
		return new Rectangle(DESKTOP_BOUNDS);
	}
	
	private DesktopVars()
	{ // no instantiation
	}
	
	static
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle desktopBounds = ge.getMaximumWindowBounds();
		DESKTOP_WIDTH = desktopBounds.width;
		DESKTOP_HEIGHT = desktopBounds.height;
		DESKTOP_X = desktopBounds.x;
		DESKTOP_Y = desktopBounds.y;
		DESKTOP_X_MAX = DESKTOP_X + DESKTOP_WIDTH;
		DESKTOP_Y_MAX = DESKTOP_Y + DESKTOP_HEIGHT;
		DESKTOP_BOUNDS = new Rectangle(desktopBounds);
	}
}
