package com.github.visualarray;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import com.github.visualarray.gui.VisualArray;
import com.github.visualarray.sort.ArrayConditions;
import com.github.visualarray.sort.SortingAlgorithm;
import com.github.visualarray.sort.SortingAlgorithms;

public class Freedom
{
	public static void main(String[] args)
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
		
		int[] x = ArrayConditions.UNIQUELY_RANDOM.build(80, 600);
		VisualArray va = new VisualArray(x, 4, 2);
		addToAJFrame(va, "Visual Array Freedom").setVisible(true);
		
		va.setCompareDelay(20);
		va.setSwapDelay(20);
		test(va, SortingAlgorithms.COMB_SORT);
		
	}
	
	public static JFrame addToAJFrame(Component c, String title)
	{
		JFrame frame = new JFrame(title);
		JPanel panel = new JPanel();
		panel.add(c);
		
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		return frame;
	}
	
	public static void test(final VisualArray va,
			final SortingAlgorithm algorithm)
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
				while(sorterThread.isAlive() && !va.isSorted())
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
		
		System.out.println(va.isSorted());
	}
	
	public static void testAll(final VisualArray va)
	{
		for(SortingAlgorithm algorithm : SortingAlgorithms.values())
		{
			test(va, algorithm);
			
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
