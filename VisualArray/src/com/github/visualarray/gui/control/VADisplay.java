package com.github.visualarray.gui.control;

import java.awt.Window;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;

import com.github.visualarray.gui.components.VisualArray;

public class VADisplay 
{
	private final Window owner;
	private Map<VisualArray, JDialog> vaHolders;
	
	public VADisplay(Window owner)
	{
		this.owner = owner;
		this.vaHolders = new HashMap<>();
	}
	
	public void addVisualArray(VisualArray va, String title)
	{
		if(vaHolders.containsKey(va))
			throw new IllegalStateException();
		
		JDialog dialog = new JDialog(owner, title);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.add(va);
		vaHolders.put(va, dialog);
		updateLocations();
	}
	
	public void removeVisualArray(VisualArray va)
	{
		JDialog dialog = vaHolders.get(va);
		dialog.dispose();
		vaHolders.remove(va);
		updateLocations();
	}
	
	public void removeAll()
	{
		for(JDialog dialog : vaHolders.values())
		{
			dialog.dispose();
		}
		vaHolders.clear();
	}
	
	private void updateLocations()
	{
		int maxHeight = 0;
		int x = DesktopVars.DESKTOP_X;
		int y = DesktopVars.DESKTOP_Y;
		for(JDialog dialog : vaHolders.values())
		{
			dialog.pack();
			
			int width = dialog.getWidth();
			int nextX = x + width;
			
			if(nextX > DesktopVars.DESKTOP_X_MAX)
			{
				x = 0;
				nextX = width;
				
				int nextY = y + maxHeight;
				y = nextY < DesktopVars.DESKTOP_Y_MAX ? nextY : 0;
				
				maxHeight = 0;
			}
			
			dialog.setLocation(x, y);
			x = nextX;
			int height = dialog.getHeight();
			if(height > maxHeight)
			{
				maxHeight = height;
			}
			
			dialog.setVisible(true);
		}
	}
}
