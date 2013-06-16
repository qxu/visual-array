package com.github.visualarray.gui.components;

import java.awt.Window;
import java.util.LinkedHashMap;

import javax.swing.JDialog;


public class DialogController 
{
	private final Window owner;
	
	private LinkedHashMap<VisualArray, VisualArrayDialog> vaDialogMap;
	
	public DialogController(Window owner)
	{
		this.owner = owner;
		this.vaDialogMap = new LinkedHashMap<>();
	}
	
	public void addVisualArray(VisualArray va, String title)
	{
		if(vaDialogMap.containsKey(va))
			throw new IllegalStateException();
		
		VisualArrayDialog dialog = new VisualArrayDialog(owner, va);
		dialog.setDefaultCloseOperation(VisualArrayDialog.DISPOSE_ON_CLOSE);
		dialog.setResizable(false);
		dialog.pack();
		vaDialogMap.put(va, dialog);
		
		updateLocations();
	}
	
	public void removeVisualArray(VisualArray va)
	{
		JDialog dialog = vaDialogMap.get(va);
		dialog.dispose();
		vaDialogMap.remove(va);
		
		updateLocations();
	}
	
	public void removeAll()
	{
		for(JDialog dialog : vaDialogMap.values())
		{
			dialog.dispose();
		}
		vaDialogMap.clear();
	}
	
	public void revalidate()
	{
		updateLocations();
	}
	
	private void updateLocations()
	{
		int maxHeight = 0;
		int x = DesktopVars.DESKTOP_X;
		int y = DesktopVars.DESKTOP_Y;
		for(JDialog dialog : vaDialogMap.values())
		{
			dialog.setResizable(false);
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
			
			if(!dialog.isVisible())
			{
				dialog.setVisible(true);
			}
		}
	}
}
