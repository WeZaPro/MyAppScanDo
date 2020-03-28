package example.com.myappscando.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import example.com.myappscando.R;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static utils.Constants.ADDRESS;
import static utils.Constants.CONTACT_NO;
import static utils.Constants.EMAIL_ID;
import static utils.Constants.PASSWORD;
import static utils.Constants.SHARED_PREFERENCES_NAME;
import static utils.Constants.USER_ID;
import static utils.Constants.USER_NAME;

public class RegisterFragment extends Fragment {

    EditText names, pwds, addresses, emails, phones;
    Button btn_submit;
    String str_name, str_pwd, str_address, str_email, str_phone;
    RequestQueue requestQueue;
    String HttpUrl = "http://192.168.64.2/app_scanme/register_login.php";
    View v;
    TextView go_login;

    public RegisterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (v == null) {
            v = inflater.inflate(R.layout.fragment_register, container, false);
            initView(v);

        }
        return v;
    }

    private void initView(View v) {
        names = v.findViewById(R.id.e_name);
        pwds = v.findViewById(R.id.e_pwd);
        addresses = v.findViewById(R.id.e_address);
        emails = v.findViewById(R.id.e_email);
        phones = v.findViewById(R.id.e_phone);
        btn_submit = v.findViewById(R.id.submit_btn);
        go_login = v.findViewById(R.id.go_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        go_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Todo Goto Login Fragment
                LoginFragment loginFragment = new LoginFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentContainer,loginFragment)
                        .addToBackStack("")
                        .commit();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {

                    str_name = names.getText().toString().trim();
                    str_pwd = pwds.getText().toString().trim();
                    str_address = addresses.getText().toString().trim();
                    str_email = emails.getText().toString().trim();
                    str_phone = phones.getText().toString().trim();

                    if(TextUtils.isEmpty(str_name)){
                        names.setError("Email is Required.");
                        return;
                    }
                    if(TextUtils.isEmpty(str_email)){
                        emails.setError("Email is Required.");
                        return;
                    }
                    if(TextUtils.isEmpty(str_pwd)){
                        pwds.setError("Email is Required.");
                        return;
                    }
                    if(str_pwd.length() < 6){
                        pwds.setError("Password Must be >= 6 Characters");
                        return;
                    }

                    FormData();

                } else {

                    Toast.makeText(getActivity(), "You are not connected to Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void FormData() {

        //String tag_json_obj = "json_string_req";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        showJason(ServerResponse);

                        // Todo Goto Scan Fragment
                        ScanFragment scanFragment = new ScanFragment();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.contentContainer,scanFragment)
                                .addToBackStack("")
                                .commit();

                        //Toast.makeText(getActivity(), ServerResponse, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("name", str_name);
                params.put("password", str_pwd);
                params.put("address", str_address);
                params.put("email", str_email);
                params.put("phone", str_phone);

                Log.d("_POST_PARAMS", "" + params);

                return params;
            }
        };

        //AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(getActivity());

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }

    private void showJason(String serverResponse) {
        try {

            Log.d("check_response1", "" + serverResponse);

            JSONObject jsonObject = new JSONObject(serverResponse);

            // Response ที่ส่งมาจาก php
            //$response=array("response"=> $UserDetails);
            //echo json_encode($response);

            String check = jsonObject.getString("response");

            JSONObject innerObj = new JSONObject(check);

            String user_id = innerObj.getString("user_id");
            String name = innerObj.getString("name");
            String phone = innerObj.getString("phone");
            String email = innerObj.getString("email");
            String address = innerObj.getString("address");

            Log.d("check_response", "" + check);
            Log.d("email", "" + email);

            if (!check.equalsIgnoreCase("faliure")) {

                SharedPreferences sharedPreferences = getActivity()
                        .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(USER_ID, user_id);
                editor.putString(PASSWORD, "");
                editor.putString(USER_NAME, name);
                editor.putString(CONTACT_NO, phone);
                editor.putString(EMAIL_ID, email);
                editor.putString(ADDRESS, address);
                editor.commit();

                Toast.makeText(getActivity(), "saved_successfully", Toast.LENGTH_SHORT).show();

                // Todo Goto Scan Fragment
                Toast.makeText(getActivity(),"to scan fragment",Toast.LENGTH_SHORT).show();
                ScanFragment scanFragment = new ScanFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentContainer,scanFragment)
                        .addToBackStack("")
                        .commit();

                Log.d("sdd", "" + user_id);

            } else {
                Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}
