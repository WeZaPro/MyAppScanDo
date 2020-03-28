package example.com.myappscando.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import example.com.myappscando.R;
import utils.Backable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import static utils.Constants.ADDRESS;
import static utils.Constants.CONTACT_NO;
import static utils.Constants.EMAIL_ID;
import static utils.Constants.PASSWORD;
import static utils.Constants.SHARED_PREFERENCES_NAME;
import static utils.Constants.USER_ID;
import static utils.Constants.USER_NAME;

public class LoginFragment extends Fragment implements Backable {
    View v;
    TextView textViewLogin;
    SharedPreferences sharedPreferences;
    EditText editTextEmail, editTextPassword;
    Button buttonLogin;
    String str_pwd, str_email;
    RequestQueue requestQueue;
    StringRequest stringRequest;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_login, container, false);
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        initView(v);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_pwd = editTextPassword.getText().toString();
                str_email = editTextEmail.getText().toString();
                SendData(str_email, str_pwd);

            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterFragment registerFragment = new RegisterFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentContainer, registerFragment)
                        .addToBackStack("")
                        .commit();
            }
        });
        return v;
    }

    //************เช็คด้วยกรณียังไม่ได้ Register ให้แจ้ง User ********************
    private void SendData(final String str_email, final String str_pwd) {
        //Toast.makeText(getActivity(),"insert login",Toast.LENGTH_SHORT).show();
        String tag_json_obj = "json_string_req";
        String url_path = "http://192.168.64.2/app_scanme/app_login.php";
        requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest = new StringRequest(Request.Method.POST, url_path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "String:" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<String, String>();

                params.put("password", str_pwd);
                params.put("email", str_email);

                Log.d("check_params", "" + params);
                return params;
            }
        };
        Log.d("check_params", "stringRequest==> " + stringRequest);
        //AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {
        try {
            Log.d("check_response1", "" + response);
            JSONObject jsonObject = new JSONObject(response);
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

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
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
                ScanFragment scanFragment = new ScanFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentContainer, scanFragment)
                        .addToBackStack("")
                        .commit();

                Log.d("sdd", "" + user_id);

            } else {
                Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(),"No data user",Toast.LENGTH_SHORT).show();
            editTextPassword.getText().clear();
            editTextEmail.getText().clear();
        }
    }

    private void initView(View v) {
        textViewLogin = v.findViewById(R.id.textViewLogin);
        editTextEmail = v.findViewById(R.id.editTextEmail);
        editTextPassword = v.findViewById(R.id.editTextPassword);
        buttonLogin = v.findViewById(R.id.buttonLogin);
    }

    @Override
    public boolean onBackPressed() {
        Toast.makeText(getActivity(), "You clicked the back button", Toast.LENGTH_SHORT).show();

        MainFragment mainFragment = new MainFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentContainer, mainFragment)
                .commit();

        return true;
    }
}
