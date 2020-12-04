package com.daveclay.midi.realtime.ui.util;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;

public class AutoComplete extends JComboBox implements JComboBox.KeySelectionManager {

	private String searchFor;
	private long lap;
	private boolean enableAddingValues;

	public class CBDocument extends PlainDocument {
		public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
			if (str == null) return;
			super.insertString(offset, str, a);
			if (!isPopupVisible() && str.length() != 0) fireActionEvent();
		}
	}

	public AutoComplete(Object[] items) {
		super(items);
		lap = new java.util.Date().getTime();
		setKeySelectionManager(this);
		if (getEditor() != null) {
			JTextField tf = (JTextField) getEditor().getEditorComponent();
			if (tf != null) {
				tf.setDocument(new CBDocument());
				addActionListener(new MyActionListener());
			}
		}
	}

	public void setEnableAddingValues(boolean enableAddingValues) {
		this.enableAddingValues = enableAddingValues;
	}

	private int lastFoundIndex = -1;

	public int selectionForKey(char aKey, ComboBoxModel aModel) {
		long now = new java.util.Date().getTime();
		if (searchFor != null && aKey == KeyEvent.VK_BACK_SPACE && searchFor.length() > 0) {
			searchFor = searchFor.substring(0, searchFor.length() - 1);
		} else {
			//	System.out.println(lap);
			// Kam nie hier vorbei.
			if (lap + 1000 < now)
				searchFor = "" + aKey;
			else
				searchFor = searchFor + aKey;
		}
		lap = now;
		String current;
		for (int i = 0; i < aModel.getSize(); i++) {
			current = aModel.getElementAt(i).toString().toLowerCase();
			if (current.toLowerCase().startsWith(searchFor.toLowerCase())) {
				lastFoundIndex = i;
				return i;
			}
		}
		return lastFoundIndex;
	}

	public void fireActionEvent() {
		super.fireActionEvent();
	}

	public static void main(String arg[]) {
		JFrame f = new JFrame("AutoCompleteComboBox");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(200, 300);
		Container cp = f.getContentPane();
		cp.setLayout(null);
		//String[] names= {"Beate", "Claudia", "Fjodor", "Fred", "Friedrich",	"Fritz", "Frodo", "Hermann", "Willi"};
		Locale[] locales = Locale.getAvailableLocales();//
		AutoComplete cBox = new AutoComplete(locales);
		cBox.setEnableAddingValues(true);
		cBox.setBounds(50, 50, 100, 21);
		cBox.setEditable(true);
		cp.add(cBox);
		f.setVisible(true);
	}

	private class MyActionListener implements ActionListener {

		private String lastMatch;
		private int matchedStartIndex = -1;
		private int matchedEndIndex = -1;

		public void actionPerformed(ActionEvent evt) {
			JTextField tf = (JTextField) getEditor().getEditorComponent();
			String text = tf.getText();
			ComboBoxModel aModel = getModel();
			for (int i = 0; i < aModel.getSize(); i++) {
				String current = aModel.getElementAt(i).toString();
				if (current.toLowerCase().startsWith(text.toLowerCase())) {
					lastMatch = current;
					tf.setText(current);
					matchedStartIndex = text.length();
					matchedEndIndex = current.length();
					break;
				}
			}

			if (lastMatch == null) {
				tf.setText(text);
			} else {
				tf.setText(lastMatch);
			}

			if (matchedEndIndex > 0) {
				tf.setSelectionStart(matchedStartIndex);
				tf.setSelectionEnd(matchedEndIndex);
			} else {
				tf.setCaretPosition(text.length());
			}

			if (enableAddingValues) {
				lastMatch = null;
				matchedStartIndex = -1;
				matchedEndIndex = -1;
			}
		}
	}
}

