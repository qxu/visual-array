package com.github.visualarray.gui;

import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JPanel;

public class VAHolder extends JDialog
{
	private final VisualArray va;
	
	public VAHolder(Window owner, VisualArray va)
	{
		super(owner);
		if(va == null)
			throw new NullPointerException();
		
		this.va = va;
		
		JPanel p = new JPanel();
		p.add(va);
		add(p);
	}
	
	public VisualArray getVisualArray()
	{
		return this.va;
	}
	
	private static final long serialVersionUID = -6480380679773302307L;
}
