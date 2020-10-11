package com.example.giris.drdroid.fragments.finddoctorsfragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.giris.drdroid.R;
import com.example.giris.drdroid.dummy.Transfer;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A fragment that launches other parts of the demo application.
 */
public class DoctorMapFragment extends Fragment {

    private static ArrayList<Marker> data;
    MapView mMapView;
    String latitude = "hi", longitude = "hi";
    private LocationManager mLocationManager;

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            Transfer.latitude = location.getLatitude();
            Transfer.longitude = location.getLongitude();
            latitude = location.getLatitude() + "";
            longitude = location.getLongitude() + "";


            // create marker
            MarkerOptions marker = new MarkerOptions().position(
                    new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))).title("You are here.");

            // Changing marker icon
            marker.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

            // adding marker
            googleMap.addMarker(marker);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_map, container,
                false);

        mLocationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return null;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1,
                1, mLocationListener);


        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        // latitude and longitude
        if(latitude.equals("hi")) latitude = "12.934202";
        if(longitude.equals("hi")) longitude = "77.534425";



        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))).title("You are here.");

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        // adding marker
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        data = new ArrayList<Marker>();

        SharedPreferences prefs = getActivity().getSharedPreferences("URL", getActivity().MODE_PRIVATE);
        String url = prefs.getString("URL", "nat");

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(true);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url + "/doctors", new AsyncHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                pDialog.hide();
                try {
                    JSONObject json = new JSONObject(new String(response));
                    JSONArray data2 = json.getJSONArray("data");
                    int i;
                    for (i = 0; i < data2.length(); i++) {
                        JSONObject obj = data2.getJSONObject(i);
                        String latitude = obj.getString("Latitude");
                        String longitude = obj.getString("Longitude");
                        String name = obj.getString("FirstName") + " " + obj.getString("LastName");
                        String special = obj.getString("Specialization");
                        if (!latitude.toLowerCase().equals("none") && !longitude.toLowerCase().equals("none ")) {
                            Log.e("HAI",latitude);
                            Marker marker = googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)))
                                    .title(name)
                                    .snippet(special));
                            data.add(marker);
                        }

                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker m : data) {
                    builder.include(m.getPosition());
                }
                LatLngBounds bounds = builder.build();
                int padding = 0; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                googleMap.animateCamera(cu);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                pDialog.hide();
                Log.e("DOCTOR", e.toString());
            }
        });

        // Perform any camera updates here
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}