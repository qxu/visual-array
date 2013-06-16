package com.github.visualarray.gui.components;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.JDialog;


public class VisualArrayDialog extends JDialog
{
	private VisualArray component;
	
	public VisualArrayDialog(Window owner, VisualArray va)
	{
		super(owner, va.getSortingAlgorithm().toString());
		setLayout(new BorderLayout());
		
		this.component = va;
		if(va != null)
		{
			add(va, BorderLayout.CENTER);
		}
	}
	
	public VisualArray getVisualArray()
	{
		return component;
	}
	
	private static final long serialVersionUID = -8602225285527978238L;
}
