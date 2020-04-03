package example.com.myappscando.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import example.com.myappscando.R;
import utils.Backable;
import utils.ExampleItem;
import utils.MyAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainFragment extends Fragment implements MyAdapter.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener , Backable {

    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_CREATOR = "creatorName";
    public static final String EXTRA_LIKES = "likeCount";

    FloatingActionButton fab;
    RecyclerView _myRecyclerView;
    MyAdapter myAdapter;
    ArrayList<ExampleItem> mExampleList;
    RequestQueue mRequestQueue;

    SwipeRefreshLayout swipeRefreshLayout;
    View v;


    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(v == null){
            v= inflater.inflate(R.layout.fragment_main, container, false);

            fab = v.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SplashFragment splashFragment = new SplashFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contentContainer, splashFragment)
                            .addToBackStack("")
                            .commit();
                }
            });

            setMyRvLayout(v);
            mExampleList = new ArrayList<>();
            swipeRefreshLayout.setOnRefreshListener(this);

            mRequestQueue = Volley.newRequestQueue(getActivity());

            getData();
            //parseJSON();

        }
        return v;
    }

    private void getData() {
        mExampleList.clear(); // เคลียร์ข้อมูลเดิม ไม่งั้นจะซ้อนกับข้อมูลเก่า
        swipeRefreshLayout.setRefreshing(true); // Start Refress
        parseJSON();
    }

    private void parseJSON() {
        //String url = "http://192.168.64.2/test_json/sample_json_feed.php";
        String url = "http://192.168.64.2/app_scanme/sample_json_feed.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("products");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String creatorName = hit.getString("user");
                                String imageUrl = hit.getString("webformatURL");
                                int likeCount = hit.getInt("likes");

                                mExampleList.add(new ExampleItem(imageUrl, creatorName, likeCount));
                            }

                            myAdapter = new MyAdapter(getActivity(), mExampleList);
                            _myRecyclerView.setAdapter(myAdapter);
                            swipeRefreshLayout.setRefreshing(false); // Stop Refress
                            myAdapter.setOnItemClickListener(MainFragment.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }

    private void setMyRvLayout(View view) {
        _myRecyclerView = view.findViewById(R.id.recyclerView);
        _myRecyclerView.setHasFixedSize(true);
        // _myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Layout Horizontal
        //_myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));

        //Layout Grid
        int numberOfColumns = 2;
        _myRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));


        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
    }

    @Override
    public void onItemClick(int position) {

        ExampleItem item = new ExampleItem();
        item.setmCreator(mExampleList.get(position).getCreator());
        item.setmImageUrl(mExampleList.get(position).getImageUrl());
        item.setmLikes(mExampleList.get(position).getLikeCount());

        DetailProductFragment detailProductFragment = new DetailProductFragment();
        Bundle b = new Bundle();
        b.putParcelable("key",item);
        detailProductFragment.setArguments(b);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentContainer, detailProductFragment)
                .addToBackStack("")
                .commit();

        /*Intent detailIntent = new Intent(this, DetailActivity.class);
        ExampleItem clickedItem = mExampleList.get(position);

        detailIntent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
        detailIntent.putExtra(EXTRA_CREATOR, clickedItem.getCreator());
        detailIntent.putExtra(EXTRA_LIKES, clickedItem.getLikeCount());

        startActivity(detailIntent);*/
        //Toast.makeText(getActivity(),"Click Item position "+position,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRefresh() {
        getData();
        Toast.makeText(getActivity(),"Refresh...",Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onBackPressed() {
        Toast.makeText(getActivity(), "finish app... ", Toast.LENGTH_SHORT).show();

        getActivity().finish();

        return true;
    }
}
