package com.github.visualarray.gui;

import java.awt.Color;
import java.util.Random;

public class Colors
{
	final static Color[] colors = {Color.BLACK, Color.BLUE, Color.CYAN,
			Color.DARK_GRAY, Color.GRAY, Color.GREEN, Color.LIGHT_GRAY,
			Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.WHITE,
			Color.YELLOW,};

	private static final Random rand = new Random();
	
	public static Color randomColor()
	{
		int index = rand.nextInt(13);
		return colors[index];
	}

	public static String toString(Color c)
	{
		if(c.equals(Color.DARK_GRAY))
			return "DARK GRAY";
		if(c.equals(Color.LIGHT_GRAY))
			return "LIGHT GRAY";
		if(c.equals(Color.RED))
			return "RED";
		if(c.equals(Color.ORANGE))
			return "ORANGE";
		return c.toString();
	}
}
