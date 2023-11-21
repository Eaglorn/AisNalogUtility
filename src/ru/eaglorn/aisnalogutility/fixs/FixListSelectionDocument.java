package ru.eaglorn.aisnalogutility.fixs;

import java.text.MessageFormat;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class FixListSelectionDocument extends PlainDocument implements ListSelectionListener {

	private static final long serialVersionUID = 2610351175473201734L;

	private String delim;
	private MessageFormat elementFormat;

	public FixListSelectionDocument() {
		this(new MessageFormat("{0}"), "\n");
	}

	public FixListSelectionDocument(MessageFormat elementFormat, String delim) {
		this.elementFormat = elementFormat;
		this.delim = delim;
	}

	private void formatElement(Object element, StringBuilder textBuilder, int i) {
		String formatted = elementFormat.format(new Object[] { element });
		textBuilder.append(formatted);
		textBuilder.append(delim);
	}

	private void setText(String text) {
		try {
			remove(0, getLength());
			insertString(0, text, null);
		} catch (BadLocationException e) {
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		JList<?> list = (JList<?>) e.getSource();
		ListModel<?> model = list.getModel();

		ListSelectionModel listSelectionModel = list.getSelectionModel();

		int minSelectionIndex = listSelectionModel.getMinSelectionIndex();
		int maxSelectionIndex = listSelectionModel.getMaxSelectionIndex();

		StringBuilder textBuilder = new StringBuilder();

		for (int i = minSelectionIndex; i <= maxSelectionIndex; i++) {
			if (listSelectionModel.isSelectedIndex(i)) {
				Object elementAt = model.getElementAt(i);
				formatElement(elementAt, textBuilder, i);
			}
		}

		setText(textBuilder.toString());
	}
}