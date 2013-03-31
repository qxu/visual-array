package com.github.visualarray.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;


public class LocationGrid
{
	private final Location[] points;
	private final HashMap<Point, Location> pointMap;
	private final int width;
	private final int height;
	
	public LocationGrid(Dimension d)
	{
		this(d.width, d.height);
	}
	
	public LocationGrid(int width, int height)
	{
		if(width < 0 || height < 0)
			throw new IllegalArgumentException("Illegal negative arguments");
		
		if(width > Vars.maxWidth)
			width = Vars.maxWidth;
		if(height > Vars.maxHeight)
			height = Vars.maxHeight;
		
		this.width = width;
		this.height = height;
		
		int size = (Vars.maxWidth / width) * (Vars.maxHeight / height);
		points = new Location[size];
		pointMap = new HashMap<>(size);
		
		int i = 0;
		int xMax = Vars.maxWidth - width;
		int yMax = Vars.maxHeight - height;
		for(int y = 0; y <= yMax; y += height)
			for(int x = 0; x <= xMax; x += width)
			{
				 Location cur = new Location(x, y);
				 points[i++] = cur;
				 pointMap.put(new Point(x, y), cur);
			}
	}
	
	public boolean isOccupied(int x, int y)
	{
		int xPoint = x / width * width;
		int yPoint = y / height * height;
		
		return pointMap.get(new Point(xPoint, yPoint)).occupied;
	}
	
	public Point getNext()
	{
		for(Location cur : points)
		{
			if(!cur.occupied)
			{
				cur.occupied = true;
				return cur.getLocation();
			}
		}
		return new Point(0, 0);
	}
	
	public void remove(Point p)
	{
		pointMap.get(p).occupied = false;
	}
	
	public void remove(int x, int y)
	{
		pointMap.get(new Point(x, y)).occupied = false;
	}
	
	public void clear()
	{
		for(Location cur : points)
		{
			cur.occupied = false;
		}
	}
	
	private static class Location extends Point
	{
		public boolean occupied = false;
		
		public Location(int x, int y)
		{
			super(x, y);
		}
		
		private static final long serialVersionUID = 7103826698297158192L;
	}
}
