package com.github.visualarray.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
	
	private static final int[] x = ArrayConditions.UNIQUELY_RANDOM.build(20, 160);
	private static int framesCreated = 0;
	private static int xOffset = 0;
	private static VisualArray createVA()
	{
		final VisualArray va = new VisualArray(x, 4, 2);
		va.setSwapDelay(0);
		va.setCompareDelay(1);
		JFrame frame = new JFrame(Integer.toString(++framesCreated));
		JPanel panel = new JPanel();
		panel.add(va);
		
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setLocation(xOffset, 0);
		xOffset += frame.getWidth();
		frame.setVisible(true);
		return va;
	}
	
	public static void main(String[] args)
	{
		VisualArrayClock clock = new VisualArrayClock(0 * 1000000);
		
		final VisualArray va1 = createVA();
		final VisualArray va2 = createVA();
		final VisualArray va3 = createVA();
		
		clock.add(va1);
		clock.add(va2);
		clock.add(va3);
		
		try
		{
			Thread.sleep(2000);
		}
		catch(InterruptedException e)
		{
		}

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				SortingAlgorithms.BUBBLE_SORT.sort(va1);
			}
		}, "sorter 1").start();
		
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				SortingAlgorithms.QUICK_SORT.sort(va2);
			}
		}, "sorter 2").start();
		
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				SortingAlgorithms.HEAP_SORT.sort(va3);
			}
		}, "sorter 3").start();
		
		Thread t = new Thread(clock, "visual array");
		t.start();
		
		while(t.isAlive())
		{
			System.out.println("clock running... " + clock.waitingOn());
			try
			{
				Thread.sleep(1000);
			}
			catch(InterruptedException e)
			{
			}
		}
	}
}
