package com.tuanbi97.miniproject1.MapActivity;

import java.util.List;

/**
 * Created by User on 6/18/2017.
 */

public interface PlaceFinderListener {
    void onPlaceFinderStart();
    void onPlaceFinderSuccess(List<String> placeIDs);
}
