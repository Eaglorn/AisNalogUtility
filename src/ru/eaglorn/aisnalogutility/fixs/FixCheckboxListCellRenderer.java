package ru.eaglorn.aisnalogutility.fixs;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class FixCheckboxListCellRenderer<E> extends JCheckBox implements ListCellRenderer<E> {

	private static final long serialVersionUID = 5422800800423430296L;

	public FixCheckboxListCellRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected,
			boolean cellHasFocus) {
		setComponentOrientation(list.getComponentOrientation());

		setFont(list.getFont());
		setText(String.valueOf(value));

		setBackground(list.getBackground());
		setForeground(list.getForeground());

		setSelected(isSelected);
		setEnabled(list.isEnabled());

		return this;
	}
}
