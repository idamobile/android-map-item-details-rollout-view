package com.idamob.map.details.demo;

import android.os.Bundle;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.idamob.map.details.MapItemDetailsController;
import com.idamob.map.details.MapItemDetailsView;
import com.idamob.map.details.demo.BalloonedOverlay.OnBalloonClickListener;
import com.readystatesoftware.maps.OnSingleTapListener;
import com.readystatesoftware.maps.TapControlledMapView;

public class MainActivity extends MapActivity {

    private MapItemDetailsController detailsController;
    private MapItemDetailsView detailsView;
    private TapControlledMapView mapView;

    private GeoPoint location;
    private BalloonedOverlay overlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = (TapControlledMapView) findViewById(R.id.map);

        overlay = new BalloonedOverlay(R.drawable.map_atm_marker, mapView);
        mapView.getOverlays().add(overlay);

        double lat = 55.4;
        double lng = 37.5;
        location = new GeoPoint((int) (lat * 1e6), (int) (lng * 1e6));
        overlay.addItem(new BalloonOverlayItem(location, getString(R.string.location_title), getString(R.string.location_address)));
        overlay.setOnBalloonClickListener(new OnBalloonClickListener() {

            @Override
            public void onBalloonTap(int index, BalloonOverlayItem item) {
                overlay.hideAllBalloons();
                detailsController.show(location);
            }
        });

        mapView.getController().animateTo(location);
        mapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public boolean onSingleTap(MotionEvent e) {
                overlay.hideAllBalloons();
                return true;
            }
        });


        detailsView = new MapItemDetailsView(mapView, R.layout.popup_content);
        detailsController = new MapItemDetailsController(detailsView, mapView);
    }

    public boolean hidePopup() {
        if (detailsController != null && detailsController.isShowing()) {
            detailsController.hide();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (!hidePopup()) {
            super.onBackPressed();
        }
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

}
