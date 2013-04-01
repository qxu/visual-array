package com.github.visualarray.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JComponent;

import com.github.visualarray.sort.SortingArray;

public class VisualArray extends JComponent implements
		SortingArray<VASortingLine>
{
	private final int length;
	private final int[] initialValues;
	private VASortingLine[] elements;
	private int thickness;
	private int padding;
	
	private volatile int sortedIndexCount;
	private int compareDelay;
	private int swapDelay;
	private volatile int stepWait;
	private final Object stepLock = new Object();
	
	public VisualArray(int[] x, int thickness, int padding)
	{
		if(x == null)
			throw new NullPointerException();
		
		int[] values = x.clone();
		int length = x.length;
		
		int max = 0;
		VASortingLine[] elements = new VASortingLine[length];
		for(int i = 0; i < length; ++i)
		{
			int value = values[i];
			elements[i] = new VASortingLine(value);
			if(value > max)
			{
				max = value;
			}
		}
		
		Dimension size = new Dimension(max, length * (thickness + padding));
		setPreferredSize(size);
		setSize(size);
		
		this.length = length;
		this.initialValues = values;
		this.elements = elements;
		this.thickness = thickness;
		this.padding = padding;
		this.sortedIndexCount = 0;
		
		this.setFont(new Font("SansSerif", Font.PLAIN, 10));
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		int thickness = getThickness();
		int dy = thickness + getPadding();
		VASortingLine[] va = this.elements;
		int length = length();
		for(int i = 0; i < length; ++i)
		{
			VASortingLine line = va[i];
			g.setColor(line.getColor());
			g.fillRect(0, i * dy, line.length(), thickness);
		}
		
		g.setColor(Color.BLACK);
		
		String debug = sortedIndexCount + "/" + length();
		FontMetrics fm = getFontMetrics(getFont());
		int strWidth = fm.stringWidth(debug);
		int strHeight = fm.getHeight();
		g.drawString(debug, getWidth() - strWidth, strHeight);
	}
	
	public int getCompareDelay()
	{
		return this.compareDelay;
	}
	
	public void setCompareDelay(int steps)
	{
		if(steps < 0)
			throw new IllegalArgumentException("Negative steps: " + steps);
		
		this.compareDelay = steps;
	}
	
	public int getSwapDelay()
	{
		return this.swapDelay;
	}
	
	public void setSwapDelay(int steps)
	{
		if(steps < 0)
			throw new IllegalArgumentException("Negative steps: " + steps);
		
		this.swapDelay = steps;
	}
	
	public int getThickness()
	{
		return this.thickness;
	}
	
	public void setThickness(int thickness)
	{
		if(thickness < 0)
			throw new IllegalArgumentException(
					"Negative thickness: " + thickness);
		
		this.thickness = thickness;
	}
	
	public int getPadding()
	{
		return this.padding;
	}
	
	public void setPadding(int padding)
	{
		if(padding < 0)
			throw new IllegalArgumentException("Negative padding: " + padding);
		
		this.padding = padding;
	}

	public boolean isSorted()
	{
		synchronized(this.stepLock)
		{
			return this.sortedIndexCount == length();
		}
	}

	@Override
	public void markFinished()
	{
		synchronized(this.stepLock)
		{
			if(!isSorted())
				throw new AssertionError("not fully sorted");
			this.stepLock.notifyAll();
		}
	}
	
	@Override
	public int length()
	{
		return this.length;
	}
	
	@Override
	public VASortingLine get(int index)
	{
		return this.elements[index];
	}

	public void step()
	{
		synchronized(this.stepLock)
		{
			if(isSorted())
				return;
			if(this.stepWait <= 0)
			{
				try
				{
					this.stepLock.wait();
				}
				catch(InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(isSorted())
				return;
			
			--this.stepWait;
			
			if(this.stepWait <= 0)
			{
				this.stepLock.notifyAll();
			}
			
			try
			{
				this.stepLock.wait();
			}
			catch(InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void waitSteps(int steps)
	{
		if(steps < 0)
			throw new IllegalArgumentException("Negative steps: " + steps);
		if(steps == 0)
			return;
		synchronized(this.stepLock)
		{
			if(this.stepWait > 0)
				throw new IllegalStateException("Already waiting for steps");
			
			this.stepWait = steps;
			this.stepLock.notifyAll();
			
			try
			{
				this.stepLock.wait();
			}
			catch(InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.stepLock.notifyAll();
		}
	}
	
	@Override
	public void swap(int index1, int index2)
	{
		VASortingLine primary = get(index1);
		VASortingLine secondary = get(index2);
		
		Color priColor = primary.getColor();
		Color secColor = secondary.getColor();
		
		primary.setColor(VAColors.getPrimarySwap());
		secondary.setColor(VAColors.getSecondarySwap());
		
		repaint();
		
		waitSteps(this.swapDelay);
		
		primary.setColor(priColor);
		secondary.setColor(secColor);
		
		repaint();
		
		VASortingLine[] va = this.elements;
		VASortingLine tmp = va[index1];
		va[index1] = va[index2];
		va[index2] = tmp;
	}
	
	@Override
	public int compare(int index1, int index2)
	{
		VASortingLine primary = get(index1);
		VASortingLine secondary = get(index2);
		
		Color priColor = primary.getColor();
		Color secColor = secondary.getColor();
		
		primary.setColor(VAColors.getPrimaryCompare());
		secondary.setColor(VAColors.getSecondaryCompare());
		
		repaint();
		
		waitSteps(this.compareDelay);
		
		primary.setColor(priColor);
		secondary.setColor(secColor);
		
		repaint();
		
		VASortingLine[] va = this.elements;
		return va[index1].compareTo(va[index2]);
	}
	
	@Override
	public void markSortedIndex(int index)
	{
		VASortingLine line = get(index);
		Color sortedColor = VAColors.getSorted();
		if(!line.getColor().equals(sortedColor))
		{
			line.setColor(sortedColor);
			++sortedIndexCount;
		}
	}
	
	@Override
	public void unmarkSortedIndex(int index)
	{
		VASortingLine line = get(index);
		Color unsortedColor = VAColors.getUnSorted();
		if(!line.getColor().equals(unsortedColor))
		{
			line.setColor(unsortedColor);
			--sortedIndexCount;
		}
	}
	
	@Override
	public void reset()
	{
		int[] values = this.initialValues;
		int length = values.length;
		this.elements = new VASortingLine[length];
		VASortingLine[] elements = this.elements;
		for(int i = 0; i < length; ++i)
		{
			elements[i] = new VASortingLine(values[i]);
		}
	}
	
	@Override
	public String toString()
	{
		return "sorted:" + sortedIndexCount + "/" + length();
	}
	
	private static final long serialVersionUID = -6614080276063645167L;
}
