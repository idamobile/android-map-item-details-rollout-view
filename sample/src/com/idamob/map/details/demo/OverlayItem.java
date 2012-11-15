package com.idamob.map.details.demo;

import android.graphics.drawable.Drawable;

import com.idamobile.map.IGeoPoint;
import com.idamobile.map.OverlayItemBase;

public class OverlayItem implements OverlayItemBase {

    private Object tag;

    private IGeoPoint point;
    private CharSequence title;
    private CharSequence snippet;
    private Drawable marker;

    public OverlayItem(IGeoPoint point, CharSequence title, CharSequence snippet) {
        this(null, point, title, snippet);
    }

    public OverlayItem(Drawable marker, IGeoPoint point, CharSequence title, CharSequence snippet) {
        this.marker = marker;
        this.point = point;
        this.title = title;
        this.snippet = snippet;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public Object getTag() {
        return tag;
    }

    @Override
    public Drawable getMarker() {
        return marker;
    }

    @Override
    public IGeoPoint getGeoPoint() {
        return point;
    }

    @Override
    public CharSequence getTitle() {
        return title;
    }

    @Override
    public CharSequence getSnippet() {
        return snippet;
    }
}
