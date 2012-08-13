package com.idamob.map.details;

import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;
import com.idamob.map.details.ObservableScrollView.ScrollViewListener;

public class MapItemDetailsView extends LinearLayout {

    private MapView mapView;
    private GeoPoint tinyMapCenter;

    private View tinyMapWindow;
    private View content;
    private int tinyMapWindowMinHeight;

    public MapItemDetailsView(MapView mapView, int contentViewlayoutRes) {
        super(mapView.getContext());
        this.mapView = mapView;

        setOrientation(VERTICAL);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.details_view_layout, this, true);

        tinyMapWindow = findViewById(R.id.map_window);
        tinyMapWindowMinHeight = tinyMapWindow.getLayoutParams().height;
        ViewStub contentStub = (ViewStub) findViewById(R.id.content_view_stub);
        contentStub.setLayoutResource(contentViewlayoutRes);
        content = contentStub.inflate();

        ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scroll);
        scrollView.setScrollViewListener(createScrollListener());
    }

    private ScrollViewListener createScrollListener() {
        return new ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                Projection prj = mapView.getProjection();
                Point tinyMapCenterPxCoord = new Point();
                prj.toPixels(tinyMapCenter, tinyMapCenterPxCoord);
                GeoPoint newCenter = prj.fromPixels(tinyMapCenterPxCoord.x, tinyMapCenterPxCoord.y + y / 2);
                mapView.getController().setCenter(newCenter);
            }
        };
    }

    public View getContent() {
        return content;
    }

    public void setTinyMapCenter(GeoPoint tinyMapCenter) {
        this.tinyMapCenter = tinyMapCenter;
    }

    public int correctTinyMapHeight(int displayHeight) {
        int contentMeasuredHeight = content.getMeasuredHeight();
        int calcHeight = displayHeight - contentMeasuredHeight;
        int height = Math.max(calcHeight, tinyMapWindowMinHeight);
        tinyMapWindow.getLayoutParams().height = height;
        tinyMapWindow.requestLayout();
        return height;
    }

    public void setTinyMapWindowClickListener(OnClickListener clickListener) {
        tinyMapWindow.setOnClickListener(clickListener);
    }
}
