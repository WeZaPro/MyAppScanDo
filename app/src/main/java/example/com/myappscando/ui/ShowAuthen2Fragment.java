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
import com.android.volley.DefaultRetryPolicy;
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

import example.com.myappscando.R;
import utils.Backable;

public class ShowAuthen2Fragment extends Fragment implements Backable {

    // get from database
    String URL_GET_DB = "http://192.168.64.2/app_scanme/get_data_from_Scan.php";
    RequestQueue queue;
    StringRequest stringRequest;
    //local
    TextView tvProductId, tvProductName, tvProductCount;
    ImageView image_view,mv_authen;
    String imageUrl;
    View v;

    public ShowAuthen2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_show_authen2, container, false);
        tvProductId = v.findViewById(R.id.tvProductId);
        tvProductName = v.findViewById(R.id.tvProductName);
        image_view = v.findViewById(R.id.image_view);
        mv_authen = v.findViewById(R.id.mv_authen);
        tvProductCount = v.findViewById(R.id.tvProductCount);

        if (getArguments() != null) {
            String pid = getArguments().getString("key");
            String token = getArguments().getString("token");
            Log.d("chk", "PID==>" + pid);
            Log.d("chk", "TOKEN==>" + token);
            sendGetRequest(pid, token);
        }
        return v;
    }

    private void sendGetRequest(final String pid_code, final String token) {
        Log.d("response2", "ShowAuthen2 Fragment ==>PID ==> " + pid_code);

        stringRequest = new StringRequest(Request.Method.POST, URL_GET_DB,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //get_response_text.setText("Data : "+response);
                        //Log.d("response2","ShowAuthen2 Fragment ==>response ==> "+response);
                        /*if(response.isEmpty()){
                            Log.d("response2","NoData++++++");
                        }*/

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String productName = jsonObject.getString("product_name");

                            if (jsonObject.isNull("product_name")) {
                                Log.d("response2", "null data===>" + productName);
                                Toast.makeText(getActivity(), "data is null " + productName, Toast.LENGTH_SHORT).show();

                                // goto NoData Fragment
                                NoDataFragment noDataFragment = new NoDataFragment();
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.contentContainer, noDataFragment)
                                        .commit();
                            } else {
                                tvProductName.setText("product_name : " + productName);
                                imageUrl = jsonObject.getString("product_image");
                                tvProductCount.setText("AUTHEN : " + jsonObject.getString("count_row"));
                                tvProductId.setText("Product id : " + jsonObject.getString("pid"));

                                new LoadImagefromUrl().execute(image_view, imageUrl);

                                String authen = jsonObject.getString("count_row");
                                if(authen.equals("REGISTER-AUTHENTIC")){
                                    Log.d("AUTHEN","AUTHENTIC==> "+authen);
                                    mv_authen.setImageResource(R.drawable.ic_launcher_authentic);
                                }else{
                                    Log.d("AUTHEN","COPY==> "+authen);
                                    mv_authen.setImageResource(R.drawable.ic_launcher_copy);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //get_response_text.setText("Failed to Parse Json");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //get_response_text.setText("Data : Response Failed");
                Log.d("response2", "ShowAuthen2 Fragment ==>error ==> " + error);

                //test แก้เรื่อง Timeout error ทำให้ save database เบิ้ล 2 ครั้ง
                //Todo test edit TimeoutError ==>1
                if (error instanceof NetworkError) {
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    /*Toast.makeText(getActivity(),
                            "Oops. Timeout error!",
                            Toast.LENGTH_LONG).show();*/
                    Log.d("response2", "ShowAuthen2 Fragment ==>error in if else ==> " + error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("pid", pid_code);
                params.put("fcm_token", token);

                return params;
            }
        };

        //Todo test edit TimeoutError ==>2
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }

    //// load image url
    private class LoadImagefromUrl extends AsyncTask<Object, Void, Bitmap> {
        ImageView ivPreview = null;

        @Override
        protected Bitmap doInBackground(Object... params) {
            this.ivPreview = (ImageView) params[0];
            String url = (String) params[1];
            System.out.println(url);
            return loadBitmap(url);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            ivPreview.setImageBitmap(result);
        }
    }

    public Bitmap loadBitmap(String url) {
        URL newurl = null;
        Bitmap bitmap = null;
        try {
            newurl = new URL(url);
            bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return bitmap;
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
