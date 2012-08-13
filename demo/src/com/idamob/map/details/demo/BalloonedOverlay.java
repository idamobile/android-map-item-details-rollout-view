package com.idamob.map.details.demo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.MapView;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;

public class BalloonedOverlay extends BalloonItemizedOverlay<BalloonOverlayItem> {

    private Context ctx;
    private Drawable marker;
    private List<BalloonOverlayItem> mItems = new ArrayList<BalloonOverlayItem>();

    private BalloonedOverlay(Drawable defaultMarker, MapView mapView, boolean dummy) {
        super(defaultMarker, mapView);
        marker = defaultMarker;
        ctx = mapView.getContext();

        setBalloonBottomOffset(marker.getIntrinsicHeight());

        // To prevent crashing in onTouch on the map
        // http://stackoverflow.com/questions/3755921/problem-with-crash-with-itemizedoverlay
        populate();
    }

    public BalloonedOverlay(Drawable defaultMarker, MapView mapView) {
        this(boundCenterBottom(defaultMarker), mapView, false);
    }

    public BalloonedOverlay(int drawableId, MapView mapView) {
        this(mapView.getResources().getDrawable(drawableId), mapView);
    }

    public void addItem(BalloonOverlayItem overlay) {
        mItems.add(overlay);
        populate();
    }

    public void setItems(List<BalloonOverlayItem> items) {
        mItems.clear();
        mItems.addAll(items);
        populate();
    }

    protected List<BalloonOverlayItem> getItems() {
        return mItems;
    }

    protected Drawable getDefaultMarker() {
        return marker;
    }

    @Override
    protected BalloonOverlayItem createItem(int i) {
        return mItems.get(i);
    }

    @Override
    public int size() {
        return mItems.size();
    }

    @Override
    protected boolean onBalloonTap(int index, BalloonOverlayItem item) {
        if (onBalloonClickListener != null) {
            onBalloonClickListener.onBalloonTap(index, item);
        }

        return true;
    }

    public void clear() {
        mItems.clear();
        setLastFocusedIndex(-1);
        populate();
    }

    public void replace(BalloonOverlayItem item) {
        mItems.clear();
        setLastFocusedIndex(-1);
        addItem(item);
    }

    @Override
    protected BalloonOverlayView<BalloonOverlayItem> createBalloonOverlayView() {
        // use our custom balloon view with our custom overlay item type:
        return new MyBalloonOverlayView(ctx, getBalloonBottomOffset());
    }

    public void setOnBalloonClickListener(OnBalloonClickListener onBalloonClickListener) {
        this.onBalloonClickListener = onBalloonClickListener;
    }

    private OnBalloonClickListener onBalloonClickListener;

    public static interface OnBalloonClickListener {
        void onBalloonTap(int index, BalloonOverlayItem item);
    }

}
