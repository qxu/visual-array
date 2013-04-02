package com.github.visualarray.gui;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;

public final class WindowPositioning
{
	private final static Rectangle DESKTOP_BOUNDS;
	
	public static int getDesktopWidth()
	{
		return (int)DESKTOP_BOUNDS.getWidth();
	}
	
	public static int getDesktopHeight()
	{
		return (int)DESKTOP_BOUNDS.getHeight();
	}
	
	public static void positionInDesktop(Window w, int x, int y)
	{
		positionRelativeTo(Corner.TOP_LEFT, w, x, y);
	}
	
	public static enum Corner
	{
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT;
	}
	
	public static int getDesktopTop()
	{
		return (int)DESKTOP_BOUNDS.getY();
	}
	
	public static int getDesktopBottom()
	{
		return getDesktopTop() + (int)DESKTOP_BOUNDS.getHeight();
	}
	
	public static int getDesktopLeft()
	{
		return (int)DESKTOP_BOUNDS.getX();
	}
	
	public static int getDesktopRight()
	{
		return getDesktopLeft() + (int)DESKTOP_BOUNDS.getWidth();
	}
	
	public static void positionRelativeTo(Corner c, Window w, int x, int y)
	{
		switch(c)
		{
			case TOP_LEFT:
			{
				w.setLocation(getDesktopLeft() + x, getDesktopTop() + y);
				return;
			}
			case TOP_RIGHT:
			{
				w.setLocation(getDesktopRight() + x, getDesktopTop() + y);
				return;
			}
			case BOTTOM_LEFT:
			{
				w.setLocation(getDesktopLeft() + x, getDesktopBottom() + y);
				return;
			}
			case BOTTOM_RIGHT:
			{
				w.setLocation(getDesktopRight() + x, getDesktopBottom() + y);
				return;
			}
		}
	}
	
	static
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		DESKTOP_BOUNDS = ge.getMaximumWindowBounds();
	}
	
	private WindowPositioning()
	{ // no instantiation
	}
}
