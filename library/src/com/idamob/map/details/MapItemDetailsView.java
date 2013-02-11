package com.idamob.map.details;

import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import com.idamob.map.details.ObservableScrollView.ScrollViewListener;
import com.idamobile.map.IGeoPoint;
import com.idamobile.map.MapViewBase;

public class MapItemDetailsView extends LinearLayout {

    private MapViewBase mapView;
    private IGeoPoint tinyMapCenter;
    private IGeoPoint itemPosition;

    private View tinyMapWindow;
    private View content;
    private int tinyMapWindowMinHeight;
    private final ObservableScrollView scrollView;

    public MapItemDetailsView(MapViewBase mapView, int contentViewlayoutRes) {
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

        scrollView = (ObservableScrollView) findViewById(R.id.scroll);
        scrollView.setScrollViewListener(createScrollListener());
    }

    private ScrollViewListener createScrollListener() {
        return new ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                updateMapViewPosition(y);
            }
        };
    }

    private void updateMapViewPosition(int y) {
        Point tinyMapCenterPxCoord = mapView.convertGeoPoint(tinyMapCenter);
        IGeoPoint newCenter = mapView.convertScreenPoint(
                new Point(tinyMapCenterPxCoord.x, tinyMapCenterPxCoord.y + y / 2));
        mapView.getController().setMapCenter(newCenter);
    }

    public View getContent() {
        return content;
    }

    public void resetTinyMapCenter() {
        tinyMapCenter = null;
    }

    public IGeoPoint getTinyMapCenter() {
        return tinyMapCenter;
    }

    public void setItemPosition(IGeoPoint itemPosition) {
        this.itemPosition = itemPosition;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthPx = MeasureSpec.getSize(widthMeasureSpec);
        int heightPx = MeasureSpec.getSize(heightMeasureSpec);
        content.measure(MeasureSpec.makeMeasureSpec(widthPx, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        correctTinyMapHeight(heightPx);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void correctTinyMapHeight(int displayHeight) {
        int contentMeasuredHeight = content.getMeasuredHeight();
        int calcHeight = displayHeight - contentMeasuredHeight;
        int height = Math.max(calcHeight, tinyMapWindowMinHeight);
        if (tinyMapWindow.getLayoutParams().height != height) {
            tinyMapWindow.getLayoutParams().height = height;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        post(new Runnable() {
            @Override
            public void run() {
                updateTinyMapCenter();
            }
        });
    }

    public void updateTinyMapCenter() {
        if (itemPosition != null) {
            int tinyMapCenterPxCoordX = content.getMeasuredWidth() / 2;
            int tinyMapCenterPxCoordY = tinyMapWindow.getLayoutParams().height / 2;
            Point itemPxCoord = mapView.convertGeoPoint(itemPosition);

            int dx = tinyMapCenterPxCoordX - itemPxCoord.x;
            int dy = tinyMapCenterPxCoordY - itemPxCoord.y;

            Point mapCenterPxCoord = mapView.convertGeoPoint(mapView.getController().getMapCenter());

            int newX = mapCenterPxCoord.x - dx;
            int newY = mapCenterPxCoord.y - dy;

            IGeoPoint point = mapView.convertScreenPoint(new Point(newX, newY));
            if (tinyMapCenter == null
                    || (tinyMapCenter.getLat() != point.getLat() && tinyMapCenter.getLng() != point.getLng())) {
                boolean shouldUpdateMapPosition = tinyMapCenter != null;
                tinyMapCenter = point;
                if (shouldUpdateMapPosition) {
                    updateMapViewPosition(scrollView.getScrollY());
                }
            }
        }
    }

    public void setTinyMapWindowClickListener(OnClickListener clickListener) {
        tinyMapWindow.setOnClickListener(clickListener);
    }
}
