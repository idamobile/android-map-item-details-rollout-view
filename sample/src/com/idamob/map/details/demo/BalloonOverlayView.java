package com.idamob.map.details.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.idamobile.map.OverlayItemBase;

public class BalloonOverlayView extends FrameLayout {

    private LinearLayout layout;
    private TextView title;
    private TextView snippet;

    public BalloonOverlayView(Context context) {
        super(context);

        layout = new LimitLinearLayout(context);
        layout.setVisibility(VISIBLE);

        setupView(context, layout);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.NO_GRAVITY;

        addView(layout, params);

    }

    protected void setupView(Context context, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.balloon_overlay, parent);
        title = (TextView) v.findViewById(R.id.balloon_item_title);
        snippet = (TextView) v.findViewById(R.id.balloon_item_snippet);
    }

    public void setData(OverlayItemBase item) {
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

    private class LimitLinearLayout extends LinearLayout {

        private static final int MAX_WIDTH_DP = 280;

        final float SCALE = getContext().getResources().getDisplayMetrics().density;

        public LimitLinearLayout(Context context) {
            super(context);
        }

        public LimitLinearLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int mode = MeasureSpec.getMode(widthMeasureSpec);
            int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
            int adjustedMaxWidth = (int)(MAX_WIDTH_DP * SCALE + 0.5f);
            int adjustedWidth = Math.min(measuredWidth, adjustedMaxWidth);
            int adjustedWidthMeasureSpec = MeasureSpec.makeMeasureSpec(adjustedWidth, mode);
            super.onMeasure(adjustedWidthMeasureSpec, heightMeasureSpec);
        }
    }

}
