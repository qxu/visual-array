package com.github.visualarray.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.github.visualarray.sort.ArrayConditions;
import com.github.visualarray.sort.SortingAlgorithms;

public class VisualArrayClock implements Runnable
{
	private List<VisualArray> elements;
	private long stepDelayNanos;
	private NanoSleeper sleeper = new NanoSleeper();
	
	private VisualArray waitingOn;
	
	public VisualArrayClock(long stepDelayNanos)
	{
		if(stepDelayNanos < 0)
			throw new IllegalArgumentException("negative delay");
		
		this.stepDelayNanos = stepDelayNanos;
		this.elements = new ArrayList<>();
	}
	
	public void addAll(Collection<? extends VisualArray> va)
	{
		synchronized(this.elements)
		{
			this.elements.addAll(va);
		}
	}
	
	public void add(VisualArray va)
	{
		synchronized(this.elements)
		{
			this.elements.add(va);
		}
	}
	
	public VisualArray waitingOn()
	{
		return this.waitingOn;
	}
	
	@Override
	public void run()
	{
		List<VisualArray> vaList = this.elements;
		synchronized(vaList)
		{
			while(!vaList.isEmpty())
			{
				this.sleeper.start(this.stepDelayNanos);
				
				for(Iterator<VisualArray> i = vaList.iterator(); i.hasNext();)
				{
					VisualArray va = i.next();
					
					waitingOn = va;
					va.step();
					waitingOn = null;
					
					if(va.isSorted())
					{
						System.out.println("done");
						i.remove();
						System.out.println("size:" + vaList.size());
					}
				}
				
				if(vaList.isEmpty())
					break;
				
				try
				{
					this.sleeper.join();
				}
				catch(InterruptedException e)
				{
					// TODO
					return;
				}
			}
		}
	}
	
	private static int framesCreated = 0;
	private static int xOffset = 0;
	private static int yOffset = 0;
	
	private static final JFrame master = new JFrame();
	private static VisualArray[] createVAs(int n)
	{
		int[] x = ArrayConditions.UNIQUELY_RANDOM.build(20, 60);
		VisualArray[] vas = new VisualArray[n];
		for(int i = 0; i < n; ++i)
		{
			final VisualArray va = new VisualArray(x, 2, 1);
			va.setSwapDelay(0);
			va.setCompareDelay(1);
			JDialog window = new JDialog(master, Integer.toString(++framesCreated));
			JPanel panel = new JPanel();
			panel.add(va);
			
			window.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			window.add(panel);
			window.pack();
			
			int xEnd = xOffset + window.getWidth();
			if(xEnd > Vars.getScreenWidth())
			{
				xOffset = 0;
				yOffset += window.getHeight();
				window.setLocation(xOffset, yOffset);
			}
			else
			{
				window.setLocation(xOffset, yOffset);
				xOffset = xEnd;
			}
			
			window.setVisible(true);
			
			vas[i] = va;
		}
		return vas;
	}
	
	public static void main(String[] args)
	{
		master.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		master.setVisible(true);
		for(int i = 0; i < 20; ++i)
		{
			VisualArrayClock clock = new VisualArrayClock(20 * 1000000);
			
			final VisualArray[] va = createVAs(1);
			
			clock.addAll(Arrays.asList(va));
	
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					SortingAlgorithms.COCKTAIL_SORT.sort(va[0]);
				}
			}, "sorter 1").start();
			
//			new Thread(new Runnable()
//			{
//				@Override
//				public void run()
//				{
//					SortingAlgorithms.BUBBLE_SORT.sort(va2);
//				}
//			}, "sorter 2").start();
//			
//			new Thread(new Runnable()
//			{
//				@Override
//				public void run()
//				{
//					SortingAlgorithms.INSERTION_SORT.sort(va3);
//				}
//			}, "sorter 3").start();
			
			Thread t = new Thread(clock, "visual array");
			t.start();
			
			while(t.isAlive())
			{
				try
				{
					Thread.sleep(200);
				}
				catch(InterruptedException e)
				{
				}
			}
		}
		System.exit(0);
	}
}
