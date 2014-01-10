package com.github.visualarray.control.components;

import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.github.visualarray.control.ControlPanel;
import com.github.visualarray.sort.ArrayBuilder;

public class ArrayBuilderPanel extends JPanel {
	private ControlPanel controlPanel;

	public ArrayBuilderPanel(ControlPanel controlPanel,
			ArrayBuilder[] builders, ArrayBuilder defaultBuilder) {
		super();
		this.controlPanel = controlPanel;

		add(new ArrayBuilderSelector(builders, defaultBuilder));
	}

	private class ArrayBuilderSelector extends JComboBox<ArrayBuilder> {
		public ArrayBuilderSelector(ArrayBuilder[] builders,
				ArrayBuilder defaultBuilder) {
			super(builders);
			setSelectedItem(defaultBuilder);
			addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			controlPanel.setArrayBuilder((ArrayBuilder) getSelectedItem());
		}

		private static final long serialVersionUID = -8634730661231028268L;
	}

	private static final long serialVersionUID = -4498044192148600328L;
}
