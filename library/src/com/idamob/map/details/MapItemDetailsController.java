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

public class MapItemDetailsController {

    public interface OnHideListener {
        void onMapItemDetailsHide();
    }

    private MapView mapView;
    private MapItemDetailsView detailsView;

    private GeoPoint savedMapCenter;
    private int savedMapZoom;
    private boolean restoreLastMapPosition;

    private OnHideListener hideListener;

    private boolean zoomControllersEnabled;
    private GeoPoint itemPosition;

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

    public void setRestoreLastMapPosition(boolean enabled) {
        this.restoreLastMapPosition = enabled;
    }

    public boolean isRestoreLastMapPosition() {
        return restoreLastMapPosition;
    }

    protected Context getContext() {
        return mapView.getContext();
    }

    public MapView getMapView() {
        return mapView;
    }

    public void show(GeoPoint forLocation) {
        this.itemPosition = forLocation;
        saveCurrentMapState();

        detailsView.setVisibility(View.VISIBLE);
        detailsView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.grow_from_bottom));
        int widthPx = mapView.getWidth();

        detailsView.measure(MeasureSpec.makeMeasureSpec(widthPx, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int heightPx = detailsView.correctTinyMapHeight(mapView.getHeight());

        int tinyMapCenterPxCoordX = widthPx / 2;
        int tinyMapCenterPxCoordY = heightPx / 2;
        Point itemPxCoord = convertGeoPoint(forLocation);

        int dx = tinyMapCenterPxCoordX - itemPxCoord.x;
        int dy = tinyMapCenterPxCoordY - itemPxCoord.y;

        Point mapCenterPxCoord = convertGeoPoint(mapView.getMapCenter());

        int newX = mapCenterPxCoord.x - dx;
        int newY = mapCenterPxCoord.y - dy;

        final GeoPoint tinyMapCenter = convertScreenPoint(new Point(newX, newY));
        mapView.getController().setCenter(mapView.getMapCenter());
        mapView.post(new Runnable() {
            @Override
            public void run() {
                mapView.getController().animateTo(tinyMapCenter);
            }
        });
        zoomControllersEnabled = mapView.getZoomButtonsController() != null
                && mapView.getZoomButtonsController().isVisible();
        if (zoomControllersEnabled) {
            mapView.getZoomButtonsController().setVisible(false);
        }
        detailsView.setTinyMapCenter(tinyMapCenter);
    }

    public Point convertGeoPoint(GeoPoint geoPoint) {
        Point result = new Point();
        mapView.getProjection().toPixels(geoPoint, result);
        return result;
    }

    public GeoPoint convertScreenPoint(Point point) {
        return mapView.getProjection().fromPixels(point.x, point.y);
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
        if (restoreLastMapPosition) {
            restoreSavedMapState();
        } else {
            moveToItemPosition();
        }
        if (zoomControllersEnabled) {
            mapView.getZoomButtonsController().setVisible(true);
        }
        if (hideListener != null) {
            hideListener.onMapItemDetailsHide();
        }
    }

    private void moveToItemPosition() {
        if (itemPosition != null) {
            mapView.getController().animateTo(itemPosition);
            mapView.getController().setZoom(savedMapZoom);
            itemPosition = null;
        }
    }

    public boolean isShowing() {
        return detailsView.getVisibility() == View.VISIBLE;
    }
}
