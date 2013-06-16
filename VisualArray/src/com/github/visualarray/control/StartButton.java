package com.github.visualarray.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartButton extends MultiTextButton implements ActionListener
{
	private final ControlPanel controlPanel;
	private State state;
	
	public StartButton(ControlPanel controlPanel)
	{
		super();
		this.controlPanel = controlPanel;
		for(State state : State.values())
		{
			addText(state.toString());
		}
		setState(State.START);
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch(state)
		{
		case START:
			controlPanel.startSorter();
			break;
		case PAUSE:
			controlPanel.pauseSorter();
			break;
		case RESUME:
			controlPanel.resumeSorter();
			break;
		}
	}
	
	public State getState()
	{
		return state;
	}
	
	public void setState(State state)
	{
		setText(state.toString());
		this.state = state;
	}
	
	public enum State
	{
		START("start"),
		PAUSE("pause"),
		RESUME("resume");
		
		private State(String text)
		{
			this.text = text;
		}
		
		private final String text;
		
		@Override
		public String toString()
		{
			return text;
		}
	}
	
	private static final long serialVersionUID = -7366336432486026917L;
}
