package com.mohdeva.learn.tasker;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class location extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng final_latlng;
    ImageButton ib;
    Double lat;
    Double longi,saved_lati,saved_longi;
    int taskid,issaved;
    String nameString;
    DBController controller=new DBController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Recieve data from intent
        Intent pit=getIntent();
        taskid=pit.getIntExtra("taskid",-1);
        issaved=pit.getIntExtra("Issaved",0);
//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//
//            if(extras == null) {
//               nameString=" ";
//            } else {
//                nameString=extras.getString("Datacont");
//            }
//        }
//        else {
//            nameString = (String) savedInstanceState.getSerializable("Datacont");
//        }
        setContentView(R.layout.activity_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onMapReady(GoogleMap googleMap) {
        ib=(ImageButton)findViewById(R.id.confirm);
        ib.setVisibility(View.INVISIBLE);
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            mMap.setMyLocationEnabled(false);
        }
        else
            mMap.setMyLocationEnabled(true);
        Toast.makeText(getApplicationContext()," "+taskid,Toast.LENGTH_SHORT).show();
        if(issaved==1)
        {

            Cursor cursor=controller.getlocationdata(taskid);
            if(cursor.getCount()>=1){
                String[] result=new String[2];
                if(cursor.moveToFirst()) {
                    for (int i=0;i<2;i++) {
                        result[i] = (cursor.getString(i));
                    }
                }
                saved_longi=Double.parseDouble(result[0]);
                saved_lati=Double.parseDouble(result[1]);
                mMap.addMarker(new MarkerOptions().position(new LatLng(saved_lati,saved_longi)).title(saved_lati+":"+saved_longi));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(saved_lati,saved_longi), 18.0f));
            }
            else
                Toast.makeText(getApplicationContext(),"Error Occured "+cursor.getCount(),Toast.LENGTH_SHORT).show();

        }
        else {
            PlaceAutocompleteFragment places = (PlaceAutocompleteFragment)
                    getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE).setCountry("IN")
                    .build();
            places.setFilter(typeFilter);
            places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {

                    ib.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), place.getName(), Toast.LENGTH_SHORT).show();
                    String loc = place.getName().toString();
                    List<Address> addressList = null;
                    mMap.clear();
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        addressList = geocoder.getFromLocationName(loc, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final Address address = addressList.get(0);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), 18.0f));
                    mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                        @Override
                        public void onMapLongClick(final LatLng point) {
                            ib.setVisibility(View.VISIBLE);
                            mMap.clear();
                            final_latlng = point;
                            lat = final_latlng.latitude;
                            longi = final_latlng.longitude;
                            mMap.addMarker(new MarkerOptions().position(final_latlng).title(lat + ":" + longi));

                        }

                    });
                }

                @Override
                public void onError(Status status) {

                    Toast.makeText(getApplicationContext(), status.toString(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
    public void setlatlong(View view)
    {
//        taskid=controller.getId(nameString);
        boolean isdone=controller.insertLocation(taskid,lat,longi);
        if(isdone) {
            finishAffinity();
            Toast.makeText(getApplicationContext(), "Data Inserted", Toast.LENGTH_SHORT).show();
            Intent tolist = new Intent(location.this, Todo.class);
            startActivity(tolist);
        }
        else
            Toast.makeText(getApplicationContext(),"Data Isn't Inserted",Toast.LENGTH_SHORT).show();


    }
    public void onBackPressed()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(location.this);
        // Setting Dialog Title
        alertDialog.setTitle("Discard Changes..");
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to discard the changes?");
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                // Write your code here to invoke YES event
                Toast.makeText(location.this, "Discarded", Toast.LENGTH_SHORT).show();
                controller.updatetype(taskid,null);
                finishAffinity();
                Intent main=new Intent(location.this,Todo.class);
                startActivity(main);
            }
        });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //invoke NO event
                Toast.makeText(getApplicationContext(), "Okay ", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }
}
