package com.idamob.map.details.demo;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.idamobile.map.BalloonOverlayExtension.BalloonAdapter;
import com.idamobile.map.OverlayItemBase;

public class DefaultBalloonAdapter implements BalloonAdapter, OnClickListener {

    public static interface OnBalloonClickListener {
        void onBalloonTap(OverlayItemBase item);
    }

    private OnBalloonClickListener balloonClickListener;

    public DefaultBalloonAdapter(OnBalloonClickListener balloonClickListener) {
        this.balloonClickListener = balloonClickListener;
    }

    public void setBalloonClickListener(OnBalloonClickListener balloonClickListener) {
        this.balloonClickListener = balloonClickListener;
    }

    @Override
    public View createView(Context context) {
        return new BalloonOverlayView(context);
    }

    @Override
    public void bindView(Context context, View convertView, OverlayItemBase item) {
        ((BalloonOverlayView) convertView).setData(item);
        convertView.setTag(item);
        convertView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (balloonClickListener != null) {
            balloonClickListener.onBalloonTap((OverlayItemBase) v.getTag());
        }
    }

}
