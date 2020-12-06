package com.daveclay.midi.realtime.ui.util;

public class EnumSelector<E extends Enum> extends AutoComplete {

	private Class<E> type;

	public EnumSelector(Class<E> type) {
		super(type.getEnumConstants());
		setEditable(true);
		this.type = type;
	}

	public E getValue() {
		return (E) Enum.valueOf(type, getSelectedItem().toString());
	}
}
