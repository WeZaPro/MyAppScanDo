package example.com.myappscando.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import example.com.myappscando.R;
import utils.Backable;
import utils.MyModelSaveDb;
import utils.MyModelToPhp;

// get data from ScanFragment + save to database + goto AhowAuthen2Fragment
public class ShowAuthenFragment extends Fragment implements Backable {

    //local
    View v;
    String qrcode,address,str_userid,token;
    double la,lo;

    // add to php database
    MyModelToPhp myModelToPhp;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    String URL_SAVE_DB = "http://192.168.64.2/app_scanme/scanme.php";


    public ShowAuthenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_show_authen, container, false);

        myModelToPhp = new MyModelToPhp();

        if (getArguments() != null){
            qrcode = getArguments().getString("qrcode");
            la = getArguments().getDouble("la1");
            lo = getArguments().getDouble("lo1");
            address = getArguments().getString("address1");
            str_userid = getArguments().getString("str_userid");
            token = getArguments().getString("token");
        }

        myModelToPhp.setPid(qrcode);
        myModelToPhp.setLa(la);
        myModelToPhp.setLo(lo);
        myModelToPhp.setAddresses(address);
        myModelToPhp.setStr_userid(str_userid);
        myModelToPhp.setToken(token);

        saveToDatabase();
        return v;
    }

    private void saveToDatabase() {

        stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_DB,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getActivity(), "insert complete ..." + response, Toast.LENGTH_LONG).show();
                        //tvTempPid.getText().clear();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "insert error ...please value is emtry "+error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                if (getArguments() == null) {
                    Toast.makeText(getActivity(), "Please insert value ", Toast.LENGTH_LONG).show();
                } else {
                    params.put("fcm_token", myModelToPhp.getToken());
                    params.put("pid", myModelToPhp.getPid());
                    params.put("lat", String.valueOf(myModelToPhp.getLa()));
                    params.put("lon", String.valueOf(myModelToPhp.getLo()));
                    params.put("address", myModelToPhp.getAddresses());
                    params.put("user_id", myModelToPhp.getStr_userid());

                    Log.d("check123", "token  => " + myModelToPhp.getToken());
                    Log.d("check123", "pid  => " + myModelToPhp.getPid());
                    Log.d("check123", "lat  => " + String.valueOf(myModelToPhp.getLa()));
                    Log.d("check123", "lon  => " + String.valueOf(myModelToPhp.getLo()));
                    Log.d("check123", "address  => " + myModelToPhp.getAddresses());
                    Log.d("check123", "user_id  => " + myModelToPhp.getStr_userid());
                }
                return params;
            }
        };
        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(getActivity());
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

        //test goto ShowAuthen2Fragment
        ShowAuthen2Fragment showAuthen2Fragment = new ShowAuthen2Fragment();
        Bundle b = new Bundle();
        b.putString("key",myModelToPhp.getPid());
        showAuthen2Fragment.setArguments(b);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentContainer, showAuthen2Fragment)
                .commit();


    }

   @Override
    public boolean onBackPressed() {
        Toast.makeText(getActivity(), "You clicked the back button", Toast.LENGTH_SHORT).show();

        ScanFragment scanFragment = new ScanFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentContainer, scanFragment)
                .commit();
        return true;
    }

}
