package com.github.visualarray.gui;

import java.awt.Color;

public class VAColors
{
	private static Color sorted = Color.DARK_GRAY;
	private static Color unSorted = Color.LIGHT_GRAY;
	private static Color primaryCompare = Color.RED;
	private static Color secondaryCompare = Color.ORANGE;
	private static Color primarySwap = Color.YELLOW;
	private static Color secondarySwap = Color.YELLOW;
	
	public static Color getSorted()
	{
		return sorted;
	}
	
	public static Color getUnSorted()
	{
		return unSorted;
	}
	
	public static Color getPrimaryCompare()
	{
		return primaryCompare;
	}
	
	public static Color getSecondaryCompare()
	{
		return secondaryCompare;
	}
	
	public static Color getPrimarySwap()
	{
		return primarySwap;
	}
	
	public static Color getSecondarySwap()
	{
		return secondarySwap;
	}
	
	public static void setSorted(Color c)
	{
		if(c == null)
			throw new NullPointerException();
		sorted = c;
	}
	
	public static void setUnSorted(Color c)
	{
		if(c == null)
			throw new NullPointerException();
		unSorted = c;
	}
	
	public static void setPrimaryCompare(Color c)
	{
		if(c == null)
			throw new NullPointerException();
		primaryCompare = c;
	}
	
	public static void setSecondaryCompare(Color c)
	{
		if(c == null)
			throw new NullPointerException();
		secondaryCompare = c;
	}
	
	public static void setPrimarySwap(Color c)
	{
		if(c == null)
			throw new NullPointerException();
		primarySwap = c;
	}
	
	public static void setSecondarySwap(Color c)
	{
		if(c == null)
			throw new NullPointerException();
		secondarySwap = c;
	}
	
	public static void setCompare(Color primary, Color secondary)
	{
		setPrimaryCompare(primary);
		setSecondaryCompare(secondary);
	}
	
	public static void setSwap(Color primary, Color secondary)
	{
		setPrimarySwap(primary);
		setSecondarySwap(secondary);
	}
	
	private VAColors()
	{ // no instantiation
	}
}
