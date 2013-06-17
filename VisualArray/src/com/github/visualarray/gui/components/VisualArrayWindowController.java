package com.github.visualarray.gui.components;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.JFrame;

public class VisualArrayWindowController 
{
	private JFrame sharedOwnerFrame;
	private Set<VisualArrayWindow> vaDialogSet;
	
	public VisualArrayWindowController()
	{
		sharedOwnerFrame = new JFrame();
		this.vaDialogSet = new LinkedHashSet<>();
	}
	
	public void addVisualArray(VisualArrayWindow dialog, String title)
	{
		vaDialogSet.add(dialog);
		update();
	}
	
	public void removeVisualArray(VisualArrayWindow dialog)
	{
		if(vaDialogSet.remove(dialog))
		{
			update();
		}
	}
	
	public void addAllVisualArrays(Collection<VisualArrayWindow> dialogs)
	{
		vaDialogSet.addAll(dialogs);
	}
	
	public void removeAllVisualArrays()
	{
		if(!vaDialogSet.isEmpty())
		{
			vaDialogSet.clear();
			update();
		}
	}
	
	public Set<VisualArrayWindow> getWindows()
	{
		return Collections.unmodifiableSet(vaDialogSet);
	}
	
	public void update()
	{
		int maxHeight = 0;
		int x = DesktopVars.DESKTOP_X;
		int y = DesktopVars.DESKTOP_Y;
		for(VisualArrayWindow dialog : vaDialogSet)
		{
//			dialog.setResizable(false);
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
