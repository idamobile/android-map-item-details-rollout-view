package com.idamob.map.details;

import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.idamobile.map.IGeoPoint;
import com.idamobile.map.MapViewBase;

public class MapItemDetailsController {

    public interface OnHideListener {
        void onMapItemDetailsHide();
    }

    private MapViewBase mapView;
    private MapItemDetailsView detailsView;

    private IGeoPoint savedMapCenter;
    private int savedMapZoom;
    private boolean restoreLastMapPosition;

    private OnHideListener hideListener;

    private boolean zoomControllersEnabled;
    private IGeoPoint itemPosition;

    public MapItemDetailsController(MapItemDetailsView detailsView, MapViewBase mapView) {
        this.detailsView = detailsView;
        this.mapView = mapView;

        detailsView.setTinyMapWindowClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

        ViewGroup group = (ViewGroup) mapView.getView().getParent();
        group.addView(this.detailsView, group.indexOfChild(mapView.getView()) + 1, mapView.getView().getLayoutParams());
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

    public MapViewBase getMapViewBase() {
        return mapView;
    }

    public void show(IGeoPoint forLocation) {
        this.itemPosition = forLocation;
        saveCurrentMapState();

        detailsView.setVisibility(View.VISIBLE);
        detailsView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.grow_from_bottom));
        int widthPx = mapView.getView().getWidth();

        detailsView.measure(MeasureSpec.makeMeasureSpec(widthPx, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int heightPx = detailsView.correctTinyMapHeight(mapView.getView().getHeight());

        int tinyMapCenterPxCoordX = widthPx / 2;
        int tinyMapCenterPxCoordY = heightPx / 2;
        Point itemPxCoord = mapView.convertGeoPoint(forLocation);

        int dx = tinyMapCenterPxCoordX - itemPxCoord.x;
        int dy = tinyMapCenterPxCoordY - itemPxCoord.y;

        Point mapCenterPxCoord = mapView.convertGeoPoint(mapView.getController().getMapCenter());

        int newX = mapCenterPxCoord.x - dx;
        int newY = mapCenterPxCoord.y - dy;

        final IGeoPoint tinyMapCenter = mapView.convertScreenPoint(new Point(newX, newY));
        mapView.getController().setMapCenter(mapView.getController().getMapCenter());
        mapView.getView().post(new Runnable() {
            @Override
            public void run() {
                mapView.getController().animateTo(tinyMapCenter);
            }
        });
        zoomControllersEnabled = mapView.hasZoomController() && mapView.isZoomControllerVisible();
        if (zoomControllersEnabled) {
            mapView.setZoomControllerVisible(false);
        }
        detailsView.setTinyMapCenter(tinyMapCenter);
    }

    private void saveCurrentMapState() {
        savedMapCenter = mapView.getController().getMapCenter();
        savedMapZoom = mapView.getController().getZoomLevel();
    }

    private void restoreSavedMapState() {
        if (savedMapCenter != null) {
            mapView.getController().animateTo(savedMapCenter);
            mapView.getController().setZoomLevel(savedMapZoom);
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
            mapView.setZoomControllerVisible(true);
        }
        if (hideListener != null) {
            hideListener.onMapItemDetailsHide();
        }
    }

    private void moveToItemPosition() {
        if (itemPosition != null) {
            mapView.getController().animateTo(itemPosition);
            mapView.getController().setZoomLevel(savedMapZoom);
            itemPosition = null;
        }
    }

    public boolean isShowing() {
        return detailsView.getVisibility() == View.VISIBLE;
    }
}
