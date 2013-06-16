package com.github.visualarray.gui.components;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class DialogController 
{
	private Set<VisualArrayDialog> vaDialogSet;
	
	public DialogController()
	{
		this.vaDialogSet = new LinkedHashSet<>();
	}
	
	public void addVisualArrayDialog(VisualArrayDialog dialog, String title)
	{
		vaDialogSet.add(dialog);
		updateLocations();
	}
	
	public void removeVisualArrayDialog(VisualArrayDialog dialog)
	{
		if(vaDialogSet.remove(dialog))
		{
			updateLocations();
		}
	}
	
	public void addAll(Collection<VisualArrayDialog> dialogs)
	{
		vaDialogSet.addAll(dialogs);
	}
	
	public void removeAll()
	{
		if(!vaDialogSet.isEmpty())
		{
			vaDialogSet.clear();
			updateLocations();
		}
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
		for(VisualArrayDialog dialog : vaDialogSet)
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
