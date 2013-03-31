package com.github.visualarray;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.github.visualarray.gui.VisualArray;
import com.github.visualarray.sort.ArrayConditions;
import com.github.visualarray.sort.SortingAlgorithm;
import com.github.visualarray.sort.SortingAlgorithms;

public class Freedom
{
	public static void main(String[] args)
	{
		va.setCompareDelay(20);
		va.setSwapDelay(20);
		test(SortingAlgorithms.HEAP_SORT);
		
	}
	
	static final VisualArray va;
	
	static
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
		
		int[] x = ArrayConditions.COMPLETELY_RANDOM.build(600, 80);
		va = new VisualArray(x, 4, 2);
		
		JFrame frame = new JFrame("Visual Array");
		JPanel panel = new JPanel();
		panel.add(va);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	static void test(final SortingAlgorithm algorithm)
	{
		final Thread sorterThread = new Thread()
		{
			@Override
			public void run()
			{
				algorithm.sort(va);
			}
		};
		
		sorterThread.start();
		
		new Thread()
		{
			@Override
			public void run()
			{
				int delta = 0;
				while(sorterThread.isAlive())
				{
					va.step();
					++delta;
					if(delta % 4 == 0)
					{
						try
						{
							Thread.sleep(1);
						}
						catch(InterruptedException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}.start();
		
		try
		{
			sorterThread.join();
		}
		catch(InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void testAll()
	{
		for(SortingAlgorithm algorithm : SortingAlgorithms.values())
		{
			test(algorithm);
			
			try
			{
				Thread.sleep(1000);
			}
			catch(InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			va.reset();
		}
	}
}
