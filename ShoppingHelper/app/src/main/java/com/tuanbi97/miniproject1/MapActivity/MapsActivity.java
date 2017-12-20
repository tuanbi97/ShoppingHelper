package com.tuanbi97.miniproject1.MapActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.Pair;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tuanbi97.miniproject1.CustomPlace;
import com.tuanbi97.miniproject1.DataTransfer;
import com.tuanbi97.miniproject1.MainActivity;
import com.tuanbi97.miniproject1.R;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.Math.abs;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener, PlaceFinderListener {

    private GoogleMap mMap;
    private int dw, dh;
    private boolean onMyLocationClick = false;
    private ArrayMap<String, Marker> lmarker;
    private ImageView img;

    private int cDirection = 0;
    private Marker start, end;
    private ProgressDialog progressDialog;
    private List<Polyline> lines = new ArrayList<>();
    private boolean isDirection = false;

    private String dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        init();
    }

    private void init() {
        lmarker = new ArrayMap<String, Marker>();
        initAutocomplete();
        initMarkerMenu();
    }

    private void initMapListener() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (lines != null){
                    for (Polyline line:lines){
                        line.remove();
                    }
                    lines.clear();
                }
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                Projection projection = mMap.getProjection();
                Point clickedPos = projection.toScreenLocation(latLng);

                boolean onMarker = false;
                String markerId = "";
                for (String ID: lmarker.keySet()){
                    Marker marker = lmarker.get(ID);
                    Point markerPos = projection.toScreenLocation(marker.getPosition());
                    if(abs(markerPos.x - clickedPos.x) < 20 && abs(markerPos.y - clickedPos.y) < 60) {
                        onMarker = true;
                        markerId = ID;
                        break;
                    }
                }
                if (onMarker == false){
                    //add marker
                    Marker tmp = addMarker(latLng, "unknown");
                    getMyMarkerTitle(tmp);

                    Toast.makeText(MapsActivity.this, Integer.toString(lmarker.size()), Toast.LENGTH_SHORT).show();
                }
                else{
                    //remove marker
                    lmarker.get(markerId).remove();
                    lmarker.remove(markerId);
                    Toast.makeText(MapsActivity.this, Integer.toString(lmarker.size()), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (isDirection) {
                    cDirection++;
                    if (cDirection == 1) {
                        start = marker;
                        Toast.makeText(MapsActivity.this, start.getTitle(), Toast.LENGTH_SHORT).show();
                    } else if (cDirection == 2) {
                        end = marker;
                        Toast.makeText(MapsActivity.this, end.getTitle(), Toast.LENGTH_SHORT).show();
                        findDirection();
                        cDirection = 0;
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void findDirection() {
        progressDialog = ProgressDialog.show(this, "Please wait", "Finding Direction....", true);
        try {
            new PlaceFinder(this).getPlace(start, end);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void initMarkerMenu() {
        img = (ImageView) findViewById(R.id.btnmarker);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MapsActivity.this, v);
                popup.getMenuInflater().inflate(R.menu.menumarker, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Add")) {
                            Toast.makeText(MapsActivity.this, "Hold on map to add new marker", Toast.LENGTH_SHORT).show();
                        }
                        else
                            if (item.getTitle().equals("Remove")){
                                Toast.makeText(MapsActivity.this, "Hold on marker to remove", Toast.LENGTH_SHORT).show();
                            }
                            else
                                if(item.getTitle().equals("Direction")){
                                    if (lmarker.size() < 2){
                                        Toast.makeText(MapsActivity.this, "Number of markers must be larger or equal 2", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        isDirection = true;
                                        Toast.makeText(MapsActivity.this, "Tap 2 markers to find direction", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    private void initAutocomplete() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.getView().setBackgroundColor(Color.WHITE);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                addMarker(place.getLatLng(), (String) place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("Placefind", "An error occurred: " + status);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        initMapListener();
        dw = Resources.getSystem().getDisplayMetrics().widthPixels;
        dh = Resources.getSystem().getDisplayMetrics().heightPixels;
        getMyLocation();
        initPlaces();
    }

    private void initPlaces() {
        if (DataTransfer.places != null){
            for (CustomPlace item: DataTransfer.places){
                addMarker(item.getPos(), item.getTitle());
            }
        }
        else{
            addMarker(new LatLng(10.762830, 106.682624), "Ho Chi Minh University of Science");
        }
    }

    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                onMyLocationClick = true;
                return false;
            }
        });
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (onMyLocationClick == true) {
                    Projection projection = mMap.getProjection();
                    LatLng geographicalPosition = projection.fromScreenLocation(new Point(dw / 2, dh / 2 - 60));
                    onMyLocationClick = false;
                    addMarker(geographicalPosition, "My Location");
                }
            }
        });
    }

    private Marker addMarker(LatLng position, String title) {
        Marker p = mMap.addMarker(new MarkerOptions().position(position).title(title));
        lmarker.put(p.getId(), p);

        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(position));
        return p;
    }

    public void getMyMarkerTitle(Marker marker) {
        final Marker setTitleMarker = marker;
        final Dialog dialog = new Dialog(MapsActivity.this);
        dialog.setContentView(R.layout.gettitledialog);
        dialog.setTitle("Enter your marker title");
        Button btn = (Button) dialog.findViewById(R.id.btnsettitle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText t = (EditText) dialog.findViewById(R.id.edittext);
                dialogText = t.getText().toString();
                setTitleMarker.setTitle(dialogText);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onDirectionFinderStart() {
        if (lines != null){
            for (Polyline line:lines){
                line.remove();
            }
            lines.clear();
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        lines = new ArrayList<>();
        Integer sz = routes.size();
        for (Route route:routes){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            PolylineOptions polylineOptions = new PolylineOptions()
                    .geodesic(true)
                    .color(Color.BLUE)
                    .width(10);

            for (int i = 0; i < route.points.size()-1; i++){
                polylineOptions.add(route.points.get(i));
            }

            lines.add(mMap.addPolyline(polylineOptions));
        }
        isDirection = false;
    }

    @Override
    public void onPlaceFinderStart() {

    }

    @Override
    public void onPlaceFinderSuccess(List<String> placeIDs) {
        Log.d("placefinder", placeIDs.get(0) + placeIDs.get(1));
        try {
            new DirectionFinder(this, placeIDs.get(0), placeIDs.get(1)).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (DataTransfer.places != null) {
            DataTransfer.places.clear();
            DataTransfer.places = null;
        }
        super.onDestroy();
    }
}
