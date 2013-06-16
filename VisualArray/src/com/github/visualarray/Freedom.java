package com.github.visualarray;

import java.util.concurrent.Executors;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.github.visualarray.gui.components.VisualArray;
import com.github.visualarray.gui.components.VisualArrayDialog;
import com.github.visualarray.gui.control.VAController;
import com.github.visualarray.sort.ArrayConditions;
import com.github.visualarray.sort.SortingAlgorithms;

public class Freedom
{
	public static void main(String[] args) throws InterruptedException
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
		
		final VisualArray va = new VisualArray(ArrayConditions.UNIQUELY_RANDOM.build(100), 1, 1);
		VisualArrayDialog dialog = new VisualArrayDialog(null, va);
		
		dialog.setDefaultCloseOperation(VisualArrayDialog.DISPOSE_ON_CLOSE);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
		
		Executors.newSingleThreadExecutor().submit(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					SortingAlgorithms.QUICK_SORT.sort(va);
				}
				catch(InterruptedException e)
				{
				}
			}
		});
		while(!va.isSorted())
		{
			va.step();
			Thread.sleep(10);
		}
		System.out.println("done.");
	}
}
