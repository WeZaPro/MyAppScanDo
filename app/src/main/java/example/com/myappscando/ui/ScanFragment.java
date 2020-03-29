package example.com.myappscando.ui;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import example.com.myappscando.R;
import utils.Backable;
import utils.Constants;
import utils.MyModelSaveDb;
import utils.OnActivityResultDataChanged;
import utils.OnScanQrButtonClicked;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static utils.Constants.SHARED_PREFERENCES_NAME;
import static utils.Constants.USER_ID;
import static utils.Constants.USER_NAME;

// get scan qr code + get all data to send to ShowAuthenFragment

public class ScanFragment extends Fragment implements Backable, OnActivityResultDataChanged {
    View v;
    Button btn_scan;
    //Google location
    FusedLocationProviderClient client;
    Geocoder geocoder;
    //local
    //public double la, lo;
    public String ADDRESS;
    List<Address> addresses;
    //local save db
    public static double la1, lo1;
    public static String address1;
    public static String names, str_userid,token;
    // sharePreference
    SharedPreferences sharedPreferences, sharedPref;
    // interface Qrcode
    private OnScanQrButtonClicked mOnScanQrButtonClickedListener;

    public ScanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_scan, container, false);
        btn_scan = v.findViewById(R.id.btn_scan);

        // location
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        getLocationAddress();
        // Share Login
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        names = sharedPreferences.getString(USER_NAME, "");
        // เก็บค่า user id ไว้ที่ตัวแปล str_userid
        str_userid = sharedPreferences.getString(USER_ID, "");
        // Share Token
        sharedPref = getActivity().getSharedPreferences(Constants.MY_PREFS, Context.MODE_PRIVATE);
        token = sharedPref.getString(Constants.TOKEN, "");

        //callback
        MainActivity.setOnActivityResultDataChanged((this));
        //googlemap
        requestPermissionGooglemap();
        return v;
    }

    public void getLocationAddress() {
        /*client = LocationServices.getFusedLocationProviderClient(getActivity());*/
        client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //addAddress(location);
                /*List<Address> addresses;*/
                geocoder = new Geocoder(getActivity(), Locale.getDefault());
                // Sep Address
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    address1 = addresses.get(0).getAddressLine(0);
            /*String city = addresses.get(0).getLocality();
            String tambon = addresses.get(0).getSubLocality();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            String amphoe = addresses.get(0).getSubAdminArea();*/

                    la1 = location.getLatitude();
                    lo1 = location.getLongitude();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnScanQrButtonClickedListener.triggerScanQr();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnScanQrButtonClickedListener = (OnScanQrButtonClicked) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnScanQrButtonClickedListener = null;
    }

    @Override
    public boolean onBackPressed() {
        Toast.makeText(getActivity(), "finish app... ", Toast.LENGTH_SHORT).show();
        getActivity().finish();
        return true;
    }


    @Override
    public void onDataReceived(String data) {
        //get data to show in MainFragment
        //Toast.makeText(getActivity(),"FM-Scan Data is : "+data,Toast.LENGTH_SHORT).show();
    }

    // request permission google map
    private void requestPermissionGooglemap() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    public static MyModelSaveDb myModelSaveDb() { // ส่งเป็น Model
        MyModelSaveDb saveToDb = new MyModelSaveDb();
        saveToDb.setLa(la1);
        saveToDb.setLo(lo1);
        saveToDb.setAddresses(address1);
        saveToDb.setStr_userid(str_userid);
        saveToDb.setToken(token);
        return saveToDb;
    }

}
