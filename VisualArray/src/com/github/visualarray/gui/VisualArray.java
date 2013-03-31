package com.github.visualarray.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.swing.JComponent;

import com.github.visualarray.sort.SortingArray;

public class VisualArray extends JComponent implements SortingArray<VASortingLine>
{
	private final int length;
	private final int[] initialValues;
	private VASortingLine[] elements;
	private int thickness;
	private int padding;
	private int modCount;
	
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
		this.modCount = 0;
	}

	public void step()
	{
		synchronized(stepLock)
		{
			if(this.stepWait <= 0)
			{
				try
				{
					stepLock.wait();
				}
				catch(InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			--this.stepWait;
			

			if(this.stepWait <= 0)
			{
				stepLock.notifyAll();
			}
		}
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
	}

	public int getCompareDelay()
	{
		return compareDelay;
	}

	public void setCompareDelay(int steps)
	{
		if(steps < 0)
			throw new IllegalArgumentException("Negative steps: " + steps);
		
		this.compareDelay = steps;
	}

	public int getSwapDelay()
	{
		return swapDelay;
	}

	public void setSwapDelay(int steps)
	{
		if(steps < 0)
			throw new IllegalArgumentException("Negative steps: " + steps);
		
		this.swapDelay = steps;
	}

	public int getThickness()
	{
		return thickness;
	}

	public void setThickness(int thickness)
	{
		if(thickness < 0)
			throw new IllegalArgumentException("Negative thickness: " + thickness);
		
		this.thickness = thickness;
	}

	public int getPadding()
	{
		return padding;
	}

	public void setPadding(int padding)
	{
		if(padding < 0)
			throw new IllegalArgumentException("Negative padding: " + padding);
		
		this.padding = padding;
	}
	
	@Override
	public int length()
	{
		return length;
	}
	
	@Override
	public VASortingLine get(int index)
	{
		return this.elements[index];
	}
	
	private void waitSteps(int steps)
	{
		if(steps < 0)
			throw new IllegalArgumentException("Negative steps: " + steps);
		
		synchronized(stepLock)
		{
			if(this.stepWait > 0)
				throw new IllegalStateException("Already waiting for steps");
			
			this.stepWait = steps;
			stepLock.notifyAll();
			
			try
			{
				stepLock.wait();
			}
			catch(InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void swap(int index1, int index2)
	{
		synchronized(stepLock)
		{
			++this.modCount;

			VASortingLine primary = get(index1);
			VASortingLine secondary = get(index2);
			
			Color priColor = primary.getColor();
			Color secColor = secondary.getColor();
			
			primary.setColor(VAColors.getPrimarySwap());
			secondary.setColor(VAColors.getSecondarySwap());
			
			repaint();
			
			waitSteps(swapDelay);

			primary.setColor(priColor);
			secondary.setColor(secColor);
			
			repaint();
			
			VASortingLine[] va = this.elements;
			VASortingLine tmp = va[index1];
			va[index1] = va[index2];
			va[index2] = tmp;
		}
	}
	
	@Override
	public int compare(int index1, int index2)
	{
		synchronized(stepLock)
		{
			VASortingLine primary = get(index1);
			VASortingLine secondary = get(index2);
			
			Color priColor = primary.getColor();
			Color secColor = secondary.getColor();
			
			primary.setColor(VAColors.getPrimaryCompare());
			secondary.setColor(VAColors.getSecondaryCompare());
			
			repaint();
			
			waitSteps(compareDelay);
			
			primary.setColor(priColor);
			secondary.setColor(secColor);
			
			repaint();
			
			VASortingLine[] va = this.elements;
			return va[index1].compareTo(va[index2]);
		}
	}

	@Override
	public void markSorted(int index)
	{
		get(index).setColor(VAColors.getSorted());
	}

	@Override
	public void unmarkSorted(int index)
	{
		get(index).setColor(VAColors.getUnSorted());
	}
	
	@Override
	public void reset()
	{
		++this.modCount;
		int[] values = this.initialValues;
		int length = values.length;
		this.elements = new VASortingLine[length];
		VASortingLine[] elements = this.elements;
		for(int i = 0; i < length; ++i)
		{
			elements[i] = new VASortingLine(values[i]);
		}
	}

	public Iterator<VASortingLine> iterator()
	{
		return new VAIterator();
	}

	private class VAIterator implements Iterator<VASortingLine>
	{
		int currentIndex;
		int expectedModCount;
		
		public VAIterator()
		{
			this.currentIndex = 0;
			this.expectedModCount = VisualArray.this.modCount;
		}
		
		@Override
		public boolean hasNext()
		{
			return this.currentIndex < length();
		}
		
		@Override
		public VASortingLine next()
		{
			if(VisualArray.this.modCount != this.expectedModCount)
				throw new ConcurrentModificationException();
			int i = this.currentIndex + 1;
			if(i > length())
				throw new NoSuchElementException();
			this.currentIndex = i;
			return get(i);
		}
		
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
	
	private static final long serialVersionUID = -6614080276063645167L;
}
