package com.github.visualarray.control.components;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public abstract class AbstractDocumentUpdateListener implements
		DocumentListener {
	@Override
	public void insertUpdate(DocumentEvent e) {
		updatePerformed(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		updatePerformed(e);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		updatePerformed(e);
	}

	protected abstract void updatePerformed(DocumentEvent e);
}
