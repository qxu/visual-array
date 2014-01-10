package com.github.visualarray.control.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.github.visualarray.control.ControlPanel;
import com.github.visualarray.util.StringUtil;

public class StartButton extends MultiTextButton implements ActionListener {
	private final ControlPanel controlPanel;
	private State state;

	public StartButton(ControlPanel controlPanel) {
		super();
		this.controlPanel = controlPanel;
		for (State state : State.values()) {
			addText(state.toString());
		}
		setState(State.START);
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (state) {
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
		controlPanel.log(this, e.getActionCommand() + " triggered");
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		setText(state.toString());
		this.state = state;
	}

	public enum State {
		START, PAUSE, RESUME;

		private final String text;

		private State() {
			this.text = StringUtil.capitalize(name().replace('_', ' ')
					.toLowerCase());
		}

		@Override
		public String toString() {
			return text;
		}
	}

	private static final long serialVersionUID = -7366336432486026917L;
}
