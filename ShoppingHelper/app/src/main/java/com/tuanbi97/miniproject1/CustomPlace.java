package com.tuanbi97.miniproject1;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by User on 6/19/2017.
 */

public class CustomPlace {
    String title;
    LatLng pos;

    CustomPlace(String title, double x, double y) {
        this.title = title;
        pos = new LatLng(x, y);
    }

    public LatLng getPos() {
        return pos;
    }

    public String getTitle() {
        return title;
    }
}
