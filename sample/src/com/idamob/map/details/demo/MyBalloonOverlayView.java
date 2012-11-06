package com.idamob.map.details.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.readystatesoftware.mapviewballoons.BalloonOverlayView;

/**
 * 
 * @author zjor
 * @since Jun 29, 2012
 *
 */
public class MyBalloonOverlayView extends BalloonOverlayView<BalloonOverlayItem>{

    private TextView title;
    private TextView snippet;


    public MyBalloonOverlayView(Context context, int balloonBottomOffset) {
        super(context, balloonBottomOffset);
    }

    @Override
    protected void setupView(Context context, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.balloon_overlay, parent);
        title = (TextView) v.findViewById(R.id.balloon_item_title);
        snippet = (TextView) v.findViewById(R.id.balloon_item_snippet);
    }

    @Override
    protected void setBalloonData(BalloonOverlayItem item, ViewGroup parent) {
        if (item.getTitle() != null) {
            title.setVisibility(VISIBLE);
            title.setText(item.getTitle());
        } else {
            title.setText("");
            title.setVisibility(GONE);
        }
        if (item.getSnippet() != null) {
            snippet.setVisibility(VISIBLE);
            snippet.setText(item.getSnippet());
        } else {
            snippet.setText("");
            snippet.setVisibility(GONE);
        }
    }


}
