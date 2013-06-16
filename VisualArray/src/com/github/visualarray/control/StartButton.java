package com.github.visualarray.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class StartButton extends JButton implements ActionListener
{
	private final ControlPanel controlPanel;
	private State state;
	
	public StartButton(ControlPanel controlPanel)
	{
		this.controlPanel = controlPanel;
		setState(State.START);
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch(state)
		{
		case START:
			controlPanel.start();
			setState(State.PAUSE);
			break;
		case PAUSE:
			controlPanel.pause();
			setState(State.RESUME);
			break;
		case RESUME:
			controlPanel.resume();
			setState(State.PAUSE);
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
