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

public class ShowAuthen2Fragment extends Fragment implements Backable {

    // get from database
    String URL_GET_DB = "http://192.168.64.2/app_scanme/get_data_from_Scan.php";
    RequestQueue queue;
    StringRequest stringRequest;
    //local
    TextView tvProductId,tvProductName,tvProductCount;
    ImageView image_view;
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
        tvProductCount = v.findViewById(R.id.tvProductCount);

        if(getArguments() != null){
            String pid = getArguments().getString("key");
            Log.d("pid","PID==>"+pid);
            sendGetRequest(pid);
        }
        return v;
    }

    private void sendGetRequest(final String pid_code) {

        stringRequest=new StringRequest(Request.Method.POST, URL_GET_DB,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //get_response_text.setText("Data : "+response);
                        Log.d("response","ShowAuthen2 Fragment ==>response ==> "+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                    /*get_response_text.setText("name :"+jsonObject.getString("name")+"\n");
                    get_response_text.append("email :"+jsonObject.getString("email")+"\n");
                    get_response_text.append("address :"+jsonObject.getString("address")+"\n");*/
                            tvProductName.setText("product_name : "+jsonObject.getString("product_name"));
                            imageUrl = jsonObject.getString("product_image");
                            tvProductCount.setText("AUTHEN : "+jsonObject.getString("count_row"));
                            tvProductId.setText("Product id : "+jsonObject.getString("pid"));

                    /*Picasso.get().load(imageUrl)
                            .fit()
                            .centerInside()
                            //.centerCrop(-18)
                            //.centerCrop(0)
                            .into(image_view);*/
                            new LoadImagefromUrl( ).execute( image_view, imageUrl );

                        }
                        catch (Exception e){
                            e.printStackTrace();
                            //get_response_text.setText("Failed to Parse Json");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //get_response_text.setText("Data : Response Failed");
                Log.d("response","ShowAuthen2 Fragment ==>error ==> "+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("pid", pid_code);

                return params;
            }
        };

        queue= Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }
    //// load image url
    private class LoadImagefromUrl extends AsyncTask< Object, Void, Bitmap> {
        ImageView ivPreview = null;

        @Override
        protected Bitmap doInBackground( Object... params ) {
            this.ivPreview = (ImageView) params[0];
            String url = (String) params[1];
            System.out.println(url);
            return loadBitmap( url );
        }

        @Override
        protected void onPostExecute( Bitmap result ) {
            super.onPostExecute( result );
            ivPreview.setImageBitmap( result );
        }
    }

    public Bitmap loadBitmap( String url ) {
        URL newurl = null;
        Bitmap bitmap = null;
        try {
            newurl = new URL( url );
            bitmap = BitmapFactory.decodeStream( newurl.openConnection( ).getInputStream( ) );
        } catch ( MalformedURLException e ) {
            e.printStackTrace( );
        } catch ( IOException e ) {

            e.printStackTrace( );
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
