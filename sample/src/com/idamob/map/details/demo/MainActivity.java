package com.idamob.map.details.demo;

import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.idamob.map.details.MapItemDetailsController;
import com.idamob.map.details.MapItemDetailsView;
import com.idamob.map.details.demo.DefaultBalloonAdapter.OnBalloonClickListener;
import com.idamobile.map.IGeoPoint;
import com.idamobile.map.MapViewBase;
import com.idamobile.map.MapViewWrapper;
import com.idamobile.map.OverlayItemBase;
import com.idamobile.map.google.UniversalGeoPoint;

public class MainActivity extends MapActivity {

    private MapItemDetailsController detailsController;
    private MapItemDetailsView detailsView;
    private MapViewBase mapView;

    private IGeoPoint location;
    private BalloonedOverlay overlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = MapViewWrapper.getInstance(this).wrap(findViewById(R.id.map));

        overlay = new BalloonedOverlay(getResources().getDrawable(R.drawable.map_atm_marker));
        overlay.initWithAdapter(new DefaultBalloonAdapter(new OnBalloonClickListener() {
            @Override
            public void onBalloonTap(OverlayItemBase item) {
                overlay.getBalloonController().hideBalloon(true);
                detailsController.show(location);
            }
        }));
        mapView.addOverlay(overlay);

        double lat = 55.4;
        double lng = 37.5;
        location = new UniversalGeoPoint((int) (lat * 1e6), (int) (lng * 1e6));
        overlay.addItem(new OverlayItem(location, getString(R.string.location_title),
                getString(R.string.location_address)));

        mapView.getController().animateTo(location);

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
