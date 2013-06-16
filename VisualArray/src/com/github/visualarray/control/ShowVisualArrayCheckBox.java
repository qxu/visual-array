package com.github.visualarray.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import com.github.visualarray.gui.components.VisualArray;

public class ShowVisualArrayCheckBox extends JCheckBox implements ActionListener
{
	private ControlPanel controlPanel;
	private VisualArray va;

	public ShowVisualArrayCheckBox(ControlPanel controlPanel, VisualArray va, String text)
	{
		super(text);
		this.controlPanel = controlPanel;
		this.va = va;
		
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(isSelected())
		{
			controlPanel.showVisualArray(va);
		}
		else
		{
			controlPanel.hideVisualArray(va);
		}
		controlPanel.getShowAllCheckBox().updateSelected();
	}
	
	private static final long serialVersionUID = 5625758425155359614L;
}
