package com.github.visualarray.gui;

import java.awt.Color;
import java.util.LinkedList;

public class Gradient
{
	private final int len;
	
	private final LinkedList<ColorIndex> colors = new LinkedList<>();
	
	public Gradient(Color... colors)
	{
		if(colors == null)
			throw new IllegalArgumentException("null colors");
		this.len = colors.length;
		if(this.len == 0)
			throw new IllegalArgumentException("empty colors");
		
		float index = 0F, div = 1F / (this.len - 1);
		for(Color c : colors)
		{
			this.colors.add(new ColorIndex(index, c));
			index += div;
		}
	}
	
	public Color get(final float index)
	{
		if(index < 0F || index > 1F)
			throw new IllegalArgumentException("Illegal index " + index);
		
		ColorIndex prev = null;
		ColorIndex next = null;
		for(ColorIndex c : this.colors)
		{
			next = c;
			if(index < c.index)
			{
				break;
			}
			prev = c;
		}
		
		final float shadeIndex = (index - prev.index) / (next.index - prev.index);
		
		final int red = (int)(prev.r + shadeIndex * (next.r - prev.r));
		final int green = (int)(prev.g + shadeIndex * (next.g - prev.g));
		final int blue = (int)(prev.b + shadeIndex * (next.b - prev.b));
		final int alpha = (int)(prev.a + shadeIndex * (next.a - prev.a));
		
		return new Color(red, green, blue, alpha);
	}
	
	private final static class ColorIndex
	{
		final float index;
		final int r;
		final int g;
		final int b;
		final int a;
		
		public ColorIndex(float index, Color c)
		{
			this.index = index;
			this.r = c.getRed();
			this.g = c.getGreen();
			this.b = c.getBlue();
			this.a = c.getAlpha();
		}
		
		@Override
		public String toString()
		{
			return this.index + "@[" + this.r + ", " + this.g + ", " + this.b + ", " + this.a + "]";
		}
	}
}
