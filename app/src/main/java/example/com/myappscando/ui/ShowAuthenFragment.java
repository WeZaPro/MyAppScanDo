package example.com.myappscando.ui;

import android.app.Activity;
import android.content.Context;
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
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import example.com.myappscando.R;
import utils.Backable;
import utils.MyModelSaveDb;
import utils.MyModelToPhp;
import utils.authenCallback;

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
    //Callback
    authenCallback listener;

    public ShowAuthenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_show_authen, container, false);
        //Log.d("response","activity_onCreateView ==> "+getActivity());

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

        saveToDatabase(getActivity());
        return v;
    }

    private void saveToDatabase(final Activity activity) {

        try{

            stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_DB,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.d("response","response ==> "+response);
                            //Log.d("response","ShowAuthenFragment ==>response ==> "+activity);
                            Log.d("response","ShowAuthenFragment ==>response ==> "+response);
                            // ตอนแรก getActivity แล้ว error เพราะค่าที่รับมาเป็น null
                            Toast.makeText(activity, "insert complete ..." + response, Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("response","ShowAuthenFragment ==>error ==> "+error);
                    // ตอนแรก getActivity แล้ว error เพราะค่าที่รับมาเป็น null
                    Toast.makeText(activity, "insert error ...please value is emtry "+error, Toast.LENGTH_LONG).show();

                    //test แก้เรื่อง Timeout error ทำให้ save database เบิ้ล 2 ครั้ง
                    if (error instanceof NetworkError) {
                    } else if (error instanceof ServerError) {
                    } else if (error instanceof AuthFailureError) {
                    } else if (error instanceof ParseError) {
                    } else if (error instanceof NoConnectionError) {
                    } else if (error instanceof TimeoutError) {
                        Toast.makeText(getContext(),
                                "Oops. Timeout error!",
                                Toast.LENGTH_LONG).show();
                    }

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    if (getArguments() == null) {
                        Toast.makeText(activity, "Please insert value ", Toast.LENGTH_LONG).show();

                    } else {
                        params.put("fcm_token", myModelToPhp.getToken());
                        params.put("pid", myModelToPhp.getPid());
                        params.put("lat", String.valueOf(myModelToPhp.getLa()));
                        params.put("lon", String.valueOf(myModelToPhp.getLo()));
                        params.put("address", myModelToPhp.getAddresses());
                        params.put("user_id", myModelToPhp.getStr_userid());
                        //Log.d("response","insert data ==> complete");
                    }
                    return params;
                }
            };
        }catch (Exception e){

            Toast.makeText(activity, "Exception error "+e, Toast.LENGTH_LONG).show();
        }

        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(getActivity());
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

        //test goto ShowAuthen2Fragment
        /*ShowAuthen2Fragment showAuthen2Fragment = new ShowAuthen2Fragment();
        Bundle b = new Bundle();
        b.putString("key",myModelToPhp.getPid());
        showAuthen2Fragment.setArguments(b);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentContainer, showAuthen2Fragment)
                .commit();*/

        // send to callback / callback to ShowAuthen2Fragment
        listener.callbackToAuthen2Fragment(myModelToPhp.getPid());

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof authenCallback) {
            listener = (authenCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SampleCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
