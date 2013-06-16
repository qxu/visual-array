package com.github.visualarray.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JComponent;

import com.github.visualarray.sort.SortingAlgorithm;


public class VisualArray extends JComponent implements Runnable
{
	private static final double ASPECT_RATIO = 1;

	private double[] initialValues;

	private VASortingLine[] elements;

	private int thickness;
	private int padding;

	private int compareDelay = 1;
	private int swapDelay = 0;
	private volatile int sortedIndexCount;
	private volatile int stepWait;
	private final Object stepLock = new Object();
	
	private SortingAlgorithm sortingAlgorithm;
	
	private ReentrantLock runningLock = new ReentrantLock();
	private volatile Thread sortingThread = null;
	
	private int stepsTriggered = 0;
	private int stepsUsed = 0;
	
	public VisualArray(SortingAlgorithm alg, double[] x, int thickness, int padding)
	{
		if(x == null)
			throw new NullPointerException();

		double[] values = x.clone();
		int len = values.length;

		int scaleFactor = (int)Math.round(
				(thickness * len + padding * (len - 1)) * ASPECT_RATIO);

		int maxLength = 0;
		VASortingLine[] elem = new VASortingLine[len];
		final Color unsorted = VAColors.getUnSorted();
		for(int i = 0; i < len; ++i)
		{
			int lineLength = (int)Math.round(values[i] * scaleFactor);
			if(lineLength > maxLength)
			{
				maxLength = lineLength;
			}

			elem[i] = new VASortingLine(lineLength, unsorted);
		}

		setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		
		Dimension size = new Dimension(maxLength, len * (thickness + padding));
		setPreferredSize(size);
		setSize(size);

		this.initialValues = values;
		this.elements = elem;
		this.thickness = thickness;
		this.padding = padding;
		this.sortedIndexCount = 0;
		this.stepWait = 0;
		this.sortingAlgorithm = alg;
	}
	
	@Override
	public void run()
	{
		if(runningLock.tryLock())
		{
			sortingThread = Thread.currentThread();
			
			SortingAlgorithm sorter = getSortingAlgorithm();
			try
			{
				sorter.sort(this);
			}
			catch(InterruptedException e)
			{
			}
			
			sortingThread = null;
			runningLock.unlock();
		}
		else
		{
			throw new IllegalStateException("Already running");
		}
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;

		int thickness = getThickness();
		int dy = thickness + getPadding();

		int length = getLength();
		for(int i = 0; i < length; ++i)
		{
			VASortingLine line = elements[i];
			g2d.setColor(line.color);
			g2d.fillRect(0, i * dy, line.length, thickness);
		}

		String debug = stepsUsed + "-" + stepsTriggered + " " + sortedIndexCount + "/" + getLength();
		FontMetrics fm = getFontMetrics(g2d.getFont());
		int strWidth = fm.stringWidth(debug);
		int strHeight = fm.getHeight();

		g2d.setColor(Color.BLACK);
		g2d.drawString(debug, getWidth() - strWidth, strHeight);
	}

//	public void setInitialValues(double[] x)
//	{// TODO
//		double[] values = x.clone();
//		int len = values.length;
//		int scaleFactor = (int)((thickness * len + padding * (len - 1)) *
//				ASPECT_RATIO);
//		int maxLength = 0;
//		VASortingLine[] elements = new VASortingLine[len];
//		for(int i = 0; i < len; ++i)
//		{
//			int lineLength = (int)Math.round(values[i] * scaleFactor);
//			elements[i] = new VASortingLine(lineLength);
//			if(lineLength > maxLength)
//			{
//				maxLength = lineLength;
//			}
//		}
//	}

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
			throw new IllegalArgumentException("Negative thickness: "
					+ thickness);

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
		return this.sortedIndexCount == getLength();
	}

	public void markFinished()
	{
		synchronized(this.stepLock)
		{
			if(!isSorted())
				throw new AssertionError("not fully sorted");
			this.stepLock.notifyAll();
		}
	}

	public int getLength()
	{
		return this.elements.length;
	}
	
	public boolean isRunning()
	{
		return sortingThread != null;
	}

	public void step() throws InterruptedException
	{
		++stepsTriggered;
		if(isRunning())
		{
			synchronized(this.stepLock)
			{
				if(this.stepWait <= 0)
				{
					this.stepLock.wait();
				}
	
				if(isSorted())
					return;
	
				--this.stepWait;
	
				if(this.stepWait <= 0)
				{
					this.stepLock.notifyAll();
				}
	
				this.stepLock.wait();
			}
		}
	}

	private void waitSteps(int steps) throws InterruptedException
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

			this.stepLock.wait();

			stepsUsed += steps;
			this.stepLock.notifyAll();
		}
	}
	
	private void checkThreadAccess()
	{
		if(Thread.currentThread() != sortingThread)
			throw new IllegalStateException("Not called from sorting thread");
	}

	public void swap(int index1, int index2) throws InterruptedException
	{
		checkThreadAccess();
		
		VASortingLine primary = elements[index1];
		VASortingLine secondary = elements[index2];

		Color priColor = primary.color;
		Color secColor = secondary.color;

		primary.color = VAColors.getPrimarySwap();
		secondary.color = VAColors.getSecondarySwap();

		repaint();

		waitSteps(this.swapDelay);

		primary.color = priColor;
		secondary.color = secColor;

		VASortingLine[] va = this.elements;
		VASortingLine tmp = va[index1];
		va[index1] = va[index2];
		va[index2] = tmp;

		repaint();
	}

	public int compare(int index1, int index2) throws InterruptedException
	{
		checkThreadAccess();
		
		VASortingLine primary = elements[index1];
		VASortingLine secondary = elements[index2];

		Color priColor = primary.color;
		Color secColor = secondary.color;

		primary.color = VAColors.getPrimarySwap();
		secondary.color = VAColors.getSecondarySwap();

		repaint();

		waitSteps(this.compareDelay);

		primary.color = priColor;
		secondary.color = secColor;

		repaint();
		return Integer.compare(primary.length, secondary.length);
	}

	public void markSortedIndex(int index)
	{
		VASortingLine line = elements[index];
		Color sortedColor = VAColors.getSorted();
		if(!line.color.equals(sortedColor))
		{
			line.color = sortedColor;
			++sortedIndexCount;
		}
	}

	public void unmarkSortedIndex(int index)
	{
		VASortingLine line = elements[index];
		Color unsortedColor = VAColors.getUnSorted();
		if(!line.color.equals(unsortedColor))
		{
			line.color = unsortedColor;
			--sortedIndexCount;
		}
	}

	public void reset()
	{
		int len = getLength();
		int scaleFactor = (int)Math.round((thickness * len + padding
				* (len - 1))
				* ASPECT_RATIO);
		double[] values = this.initialValues;
		int length = values.length;
		this.elements = new VASortingLine[length];
		VASortingLine[] elements = this.elements;
		final Color unsorted = VAColors.getUnSorted();
		for(int i = 0; i < length; ++i)
		{
			int lineLength = (int)Math.round(values[i] * scaleFactor);

			elements[i] = new VASortingLine(lineLength, unsorted);
		}
		this.sortedIndexCount = 0;
		this.stepWait = 0;
		repaint();
	}

	public void setSortingAlgorithm(SortingAlgorithm alg)
	{
		this.sortingAlgorithm = alg;
	}
	
	public SortingAlgorithm getSortingAlgorithm()
	{
		return sortingAlgorithm;
	}

	public String toString()
	{
		return "sorted:" + sortedIndexCount + "/" + getLength();
	}

	private static class VASortingLine
	{
		int length;
		Color color;

		VASortingLine(int length, Color c)
		{
			this.length = length;
			this.color = c;
		}
	}

	private static final long serialVersionUID = -6614080276063645167L;
}
