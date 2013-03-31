package com.github.visualarray.gui;

import java.awt.Color;
import java.util.Arrays;

public class VASortingLine implements Comparable<VASortingLine>
{
	private final int length;
	private Color color;
	
	public VASortingLine(int length)
	{
		if(length < 0)
			throw new IllegalArgumentException("Negative length " + length);
		this.length = length;
		this.color = VAColors.getUnSorted();
	}
	
	public int length()
	{
		return this.length;
	}
	
	public void setColor(Color c)
	{
		this.color = c;
	}
	
	public Color getColor()
	{
		return this.color;
	}
	
	@Override
	public String toString()
	{
		char[] buf = new char[length()];
		Arrays.fill(buf, '_');
		return String.valueOf(buf);
	}
	
	@Override
	public int compareTo(VASortingLine other)
	{
		return Integer.compare(length(), other.length());
	}
}
