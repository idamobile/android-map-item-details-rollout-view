package com.idamob.map.details.demo;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class BalloonOverlayItem extends OverlayItem {

	private boolean mHideButton;

	public BalloonOverlayItem(GeoPoint point, String title, String snippet) {
	    super(point, title, snippet);
	}

	public BalloonOverlayItem(GeoPoint point, String title, String snippet, boolean hideButton) {
		super(point, title, snippet);
		mHideButton = hideButton;
	}

	public boolean shouldHideButton() {
		return mHideButton;
	}
}
