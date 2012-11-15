package com.idamob.map.details.demo;

import android.graphics.drawable.Drawable;

import com.idamobile.map.BalloonItemListOverlay;
import com.idamobile.map.ShadowOverlayExtension;

public class BalloonedOverlay extends BalloonItemListOverlay<OverlayItem> implements ShadowOverlayExtension {

    public BalloonedOverlay(Drawable defaultMarker) {
        super(defaultMarker);
    }

    @Override
    public boolean isShadowEnabled() {
        return true;
    }

}