package com.ensaitechnomobile.metier;


public class DayItem implements Item {

	private final long title;

	public DayItem(long title) {
		this.title = title;
	}

	public long getTitle() {
		return title;
	}

	@Override
	public boolean isSection() {
		return true;
	}

}
