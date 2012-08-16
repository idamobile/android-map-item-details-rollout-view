package com.idamob.map.details;

import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

public class MapItemDetailsController {

    public interface OnHideListener {
        void onMapItemDetailsHide();
    }

    private MapView mapView;
    private MapItemDetailsView detailsView;

    private GeoPoint savedMapCenter;
    private int savedMapZoom;

    private OnHideListener hideListener;

    public MapItemDetailsController(MapItemDetailsView detailsView, MapView mapView) {
        this.detailsView = detailsView;
        this.mapView = mapView;

        detailsView.setTinyMapWindowClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

        ViewGroup group = (ViewGroup) mapView.getParent();
        group.addView(this.detailsView, group.indexOfChild(mapView) + 1, mapView.getLayoutParams());
        this.detailsView.setVisibility(View.GONE);
    }

    public void setHideListener(OnHideListener hideListener) {
        this.hideListener = hideListener;
    }

    protected Context getContext() {
        return mapView.getContext();
    }

    public void show(GeoPoint forLocation) {
        saveCurrentMapState();

        detailsView.setVisibility(View.VISIBLE);
        detailsView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.grow_from_bottom));
        int widthPx = mapView.getWidth();

        detailsView.measure(MeasureSpec.makeMeasureSpec(widthPx, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int heightPx = detailsView.correctTinyMapHeight(mapView.getHeight());

        Projection prj = mapView.getProjection();
        int tinyMapCenterPxCoordX = widthPx / 2;
        int tinyMapCenterPxCoordY = heightPx / 2;
        Point itemPxCoord = new Point();
        prj.toPixels(forLocation, itemPxCoord);

        int dx = tinyMapCenterPxCoordX - itemPxCoord.x;
        int dy = tinyMapCenterPxCoordY - itemPxCoord.y;

        Point mapCenterPxCoord = new Point();
        prj.toPixels(mapView.getMapCenter(), mapCenterPxCoord);

        int newX = mapCenterPxCoord.x - dx;
        int newY = mapCenterPxCoord.y - dy;

        GeoPoint tinyMapCenter = prj.fromPixels(newX, newY);
        mapView.getController().animateTo(tinyMapCenter);
        mapView.getZoomButtonsController().setVisible(false);
        detailsView.setTinyMapCenter(tinyMapCenter);
    }

    private void saveCurrentMapState() {
        savedMapCenter = mapView.getMapCenter();
        savedMapZoom = mapView.getZoomLevel();
    }

    private void restoreSavedMapState() {
        if (savedMapCenter != null) {
            mapView.getController().animateTo(savedMapCenter);
            mapView.getController().setZoom(savedMapZoom);
            savedMapCenter = null;
        }
    }

    public void hide() {
        this.detailsView.setVisibility(View.GONE);
        detailsView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shrink_to_bottom));
        restoreSavedMapState();
        mapView.getZoomButtonsController().setVisible(true);
        if (hideListener != null) {
            hideListener.onMapItemDetailsHide();
        }
    }

    public boolean isShowing() {
        return detailsView.getVisibility() == View.VISIBLE;
    }
}
