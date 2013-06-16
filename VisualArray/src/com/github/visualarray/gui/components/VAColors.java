package com.github.visualarray.gui.components;

import java.awt.Color;

public class VAColors
{
	private static Color SORTED = Color.DARK_GRAY;
	private static Color UN_SORTED = Color.LIGHT_GRAY;
	private static Color PRIMARY_COMPARE = Color.RED;
	private static Color SECONDARY_COMPARE = Color.ORANGE;
	private static Color PRIMARY_SWAP = Color.YELLOW;
	private static Color SECONDARY_SWAP = Color.YELLOW;
	
	public static Color getSorted()
	{
		return SORTED;
	}
	
	public static Color getUnSorted()
	{
		return UN_SORTED;
	}
	
	public static Color getPrimaryCompare()
	{
		return PRIMARY_COMPARE;
	}
	
	public static Color getSecondaryCompare()
	{
		return SECONDARY_COMPARE;
	}
	
	public static Color getPrimarySwap()
	{
		return PRIMARY_SWAP;
	}
	
	public static Color getSecondarySwap()
	{
		return SECONDARY_SWAP;
	}
	
	public static void setSorted(Color c)
	{
		if(c == null)
			throw new NullPointerException();
		SORTED = c;
	}
	
	public static void setUnSorted(Color c)
	{
		if(c == null)
			throw new NullPointerException();
		UN_SORTED = c;
	}
	
	public static void setPrimaryCompare(Color c)
	{
		if(c == null)
			throw new NullPointerException();
		PRIMARY_COMPARE = c;
	}
	
	public static void setSecondaryCompare(Color c)
	{
		if(c == null)
			throw new NullPointerException();
		SECONDARY_COMPARE = c;
	}
	
	public static void setPrimarySwap(Color c)
	{
		if(c == null)
			throw new NullPointerException();
		PRIMARY_SWAP = c;
	}
	
	public static void setSecondarySwap(Color c)
	{
		if(c == null)
			throw new NullPointerException();
		SECONDARY_SWAP = c;
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
