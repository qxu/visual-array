package com.github.visualarray;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.visualarray.gui.WindowPositioning;
import com.github.visualarray.gui.VisualArray;
import com.github.visualarray.gui.VisualArrayClock;
import com.github.visualarray.gui.WindowPositioning.Corner;
import com.github.visualarray.sort.ArrayConditions;
import com.github.visualarray.sort.SortingAlgorithm;
import com.github.visualarray.sort.SortingAlgorithms;

public class Fr999
{
	private static int framesCreated = 0;
	private static int xOffset = 0;
	private static int yOffset = 0;
	
	private static final JFrame master = new JFrame("visual array");
	
	public static void main(String[] args)
	{
		master.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		master.pack();
		WindowPositioning.positionRelativeTo(Corner.BOTTOM_RIGHT, master, -master.getWidth(), -master.getHeight());
		master.setResizable(false);
		master.setVisible(true);
		
		for(int i = 0; i < 1; ++i)
		{
			int[] x = ArrayConditions.UNIQUELY_RANDOM.build(200, 600);
			VisualArrayClock clock = new VisualArrayClock(4 * 1000000);
			clockWork(clock, x, SortingAlgorithms.QUICK_SORT, SortingAlgorithms.SHELL_SORT);
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
	
	private static void clockWork(VisualArrayClock clock, int[] x, SortingAlgorithm... algs)
	{
		for(final SortingAlgorithm sa : algs)
		{
			final VisualArray va = new VisualArray(x, 2, 1);
			va.setSwapDelay(1);
			va.setCompareDelay(1);
			JDialog window = new JDialog(master, Integer.toString(++framesCreated));
			JPanel panel = new JPanel();
			panel.add(va);
			
			window.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			window.add(panel);
			window.pack();
			
			WindowPositioning.positionInDesktop(window, xOffset, yOffset);
			
			int width = window.getWidth();
			if(xOffset + width > WindowPositioning.getDesktopWidth())
			{
				xOffset = 0;
				yOffset += window.getHeight();
				if(yOffset > WindowPositioning.getDesktopHeight())
				{
					yOffset = 0;
				}
			}
			else
			{
				xOffset += width;
			}
			
			window.setFocusableWindowState(false);
			window.setResizable(false);
			window.setVisible(true);
		
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					sa.sort(va);
				}
			}, "sorter " + framesCreated).start();
			clock.add(va);
		}
	}
}
