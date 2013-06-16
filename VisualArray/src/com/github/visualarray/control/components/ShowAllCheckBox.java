package com.github.visualarray.control.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JCheckBox;

import com.github.visualarray.control.ControlPanel;
import com.github.visualarray.gui.components.VisualArray;

public class ShowAllCheckBox extends JCheckBox implements ActionListener
{
	private ControlPanel controlPanel;

	public ShowAllCheckBox(ControlPanel controlPanel)
	{
		super("Show All");
		this.controlPanel = controlPanel;

		addActionListener(this);
	}

	public void updateSelected()
	{
		boolean shouldBeSelected = true;
		for(ShowVisualArrayCheckBox checkBox : controlPanel
				.getVisualArrayCheckBoxMap().values())
		{
			if(!checkBox.isSelected())
			{
				shouldBeSelected = false;
			}
		}
		setSelected(shouldBeSelected);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(isSelected())
		{
			for(Map.Entry<VisualArray, ShowVisualArrayCheckBox> entry : controlPanel
					.getVisualArrayCheckBoxMap().entrySet())
			{
				controlPanel.showVisualArray(entry.getKey());
				entry.getValue().setSelected(true);
			}
		}
		else
		{
			for(Map.Entry<VisualArray, ShowVisualArrayCheckBox> entry : controlPanel
					.getVisualArrayCheckBoxMap().entrySet())
			{
				controlPanel.hideVisualArray(entry.getKey());
				entry.getValue().setSelected(false);
			}
		}
	}

	private static final long serialVersionUID = 866229596665325648L;
}
