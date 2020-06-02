package com.parkinncharge.parkinncharge.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.SphericalUtil;
import com.google.maps.errors.ApiException;
import com.parkinncharge.parkinncharge.MarkerCardAdapter;
import com.parkinncharge.parkinncharge.MarkerInfo;
import com.parkinncharge.parkinncharge.R;
import com.parkinncharge.parkinncharge.scan_in;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private HomeViewModel homeViewModel;
    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    FusedLocationProviderClient fusedLocationClient;
    MapView mMapView;
    FirebaseFirestore db;
    Button reached_button;
    public static String marker_name;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> autoAdapter;
    ArrayList<String> autoArrayList;
    LatLng userLoc;//= new ArrayList<String>();
    LatLng usercurLoc;
    MarkerCardAdapter markerCardAdapter;
    final int CAMERA_REQUEST_CODE = 101, LOCATION_REQUEST_CODE = 100;
    ArrayList<MarkerInfo> marker;
    RecyclerView recList;
    ViewGroup cont;
    static Activity activity;
    FirebaseUser user;
    String uid;
    private String TAG = "HomeFragment";
    View root;
    public static String bid, progress, location;

    @Override


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        activity = getActivity();
        root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        db = FirebaseFirestore.getInstance();
        db.collection("booking").document(uid).collection("let out").whereEqualTo("type", "PRIVATE PARKING").whereEqualTo("status", "Active").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Reached");
                                QuerySnapshot snap = task.getResult();
                                if (snap.isEmpty())
                                    addmarkers();
                                    //root = inflater.inflate(R.layout.fragment_home, container, false);
                                else {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        bid = document.getId();

                                        progress = (String) document.get("progress");
                                        location = (String) document.get("marker");
                                        switch (progress) {
                                            case "booked":
                                                Bundle bundle = new Bundle();
                                                bundle.putString("booking_id", bid);
                                                bundle.putString("marker", location);
                                                Toast.makeText(getActivity(), "You already have a booking", Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, bid + "");
                                                //scan_in scan=new scan_in();
                                                //scan.setArguments(bundle);
                                                //container.removeAllViews();
                                                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,scan).commit();
                                                //Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.ongoing_booking);
                                                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.scan_in, bundle);
                                                break;
                                            case "scanned":
                                                Toast.makeText(getActivity(), "You already have a booking in progress", Toast.LENGTH_SHORT).show();
                                                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.ongoing_booking);
                                                break;
                                            default:
                                                root = inflater.inflate(R.layout.fragment_home, container, false);
                                        }
                                        break;
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    }
                });

        root = inflater.inflate(R.layout.fragment_home, container, false);
        //mMap.addMarker(new MarkerOptions().position(userLoc).title("Your Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.home)));
        cont = container;
        recList = (RecyclerView) root.findViewById(R.id.marker_recycler_view);
        //recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        marker = new ArrayList<>();


        //db= FirebaseFirestore.getInstance();
        //reached_button=(Button) root.findViewById(R.id.reached_button);
        autoCompleteTextView = root.findViewById(R.id.autoCompleteTextView);
        autoArrayList = new ArrayList<String>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Button Pressed", Toast.LENGTH_SHORT).show();
                Log.d("Adress", "Pressed");
                autoCompleteText();
            }
        });


        return root;


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMyLocationEnabled(true);


        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object
                                    Log.d("Location fetched", "");
                                    usercurLoc = new LatLng(location.getLatitude(), location.getLongitude());
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(usercurLoc, 15));
                                }
                            }
                        });
            }
        });

        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(getActivity(),
                                100);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });










            /*locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    //LatLng userLoc=new LatLng(location.getLatitude(),location.getLongitude());
                    // mMap.addMarker(new MarkerOptions().position(userLoc).title("Your Location").snippet("Hi Hello").icon(BitmapDescriptorFactory.fromResource(R.drawable.home)));


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
            locationManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastknownLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            userLoc=new LatLng(lastknownLocation.getLatitude(),lastknownLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLoc, 15));*/

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //reached_button.setVisibility(View.VISIBLE);
                location = marker.getTitle();
                mMap.setPadding(0, 0, 0, 150);
                return false;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                reached_button.setVisibility(View.INVISIBLE);
            }
        });
            /*reached_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent scanner_intent = new Intent(getActivity(), ScannerActivity.class);
                    scanner_intent.putExtra("Title", marker_name);
                    //scanner_intent.putExtra("booking_id",order_id);
                    Toast.makeText(getActivity(), "Passed:" + marker_name, Toast.LENGTH_SHORT).show();
                    startActivity(scanner_intent);
                }
            });*/


    }

    private void addmarkers() {

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLoc,15));
        //Log.d("Reached","here");
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Hello", document.getId() + " => " + document.getData());
                                String name = document.getId();
                                Double latitude_park = Double.parseDouble(document.getString("Latitude"));
                                Double longitude_park = Double.parseDouble(document.getString("Longitude"));
                                LatLng marker_latlng = new LatLng(latitude_park, longitude_park);
                                if (SphericalUtil.computeDistanceBetween(usercurLoc, marker_latlng) < 4000) {
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitude_park, longitude_park)).title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.home)));
                                    marker.add(new MarkerInfo(name, SphericalUtil.computeDistanceBetween(usercurLoc, marker_latlng) / 1000));


                                }
                            }
                            markerCardAdapter = new MarkerCardAdapter(getActivity(), marker);
                            recList.setAdapter(markerCardAdapter);
                        } else {
                            Log.d("Hello", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void autoCompleteText() {
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(getActivity(), "Text Changed", Toast.LENGTH_SHORT).show();

                Toast.makeText(getActivity(), s + "", Toast.LENGTH_SHORT).show();
                // Create a new Places client instance.
                PlacesClient placesClient = Places.createClient(getActivity());
                AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
                // Create a RectangularBounds object.
                RectangularBounds bounds = RectangularBounds.newInstance(
                        new LatLng(-33.880490, 151.184363), //dummy lat/lng
                        new LatLng(-33.858754, 151.229596));
                // Use the builder to create a FindAutocompletePredictionsRequest.
                FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                        // Call either setLocationBias() OR setLocationRestriction().
                        .setLocationBias(bounds)
                        //.setLocationRestriction(bounds)
                        .setCountry("IN")//Nigeria
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build();


                placesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
                    StringBuilder mResult = new StringBuilder();
                    //Toast.makeText(getActivity(), "Reached Here", Toast.LENGTH_SHORT).show();
                    for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                        mResult.append(" ").append(prediction.getFullText(null) + "\n");
                        //Log.i("Hello", prediction.getPlaceId());
                        //Log.i("Hello", prediction.getPrimaryText(null).toString());
                        //Toast.makeText(getActivity(), prediction.getPrimaryText(null) + "-" + prediction.getSecondaryText(null), Toast.LENGTH_SHORT).show();
                        autoArrayList.add(prediction.getFullText(null).toString());

                    }
                    Log.d("List:", "" + autoArrayList.toString());
                    autoAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, autoArrayList);
                    autoCompleteTextView.setAdapter(autoAdapter);
                    // mSearchResult.setText(String.valueOf(mResult));
                }).addOnFailureListener((exception) -> {
                    Log.e("Hello", "Place not found: ");

                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e("Hello1", "Place not found: " + apiException.getMessage());
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public static void paymentOnActivityResult(int requestCode, int resultCode, @Nullable Intent data, Bundle bundle) {
        //super.onActivityResult(requestCode, resultCode, data);


    }

    //@Override

}