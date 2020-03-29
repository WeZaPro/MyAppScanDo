package example.com.myappscando.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import example.com.myappscando.R;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static utils.Constants.SHARED_PREFERENCES_NAME;
import static utils.Constants.USER_ID;


public class SplashFragment extends Fragment {

    SharedPreferences sharedPreferences;
    String user_id;
    View v;

    public SplashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if(savedInstanceState == null){
            //v =  inflater.inflate(R.layout.fragment_splash, container, false);
            v = inflater.inflate(R.layout.fragment_splash,container,false);

            sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            user_id = sharedPreferences.getString(USER_ID, "");
            load();
        }

        return v;
    }

    private void load() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!user_id.equals("") ) {
                    ScanFragment scanFragment = new ScanFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contentContainer,scanFragment)
                            //.addToBackStack("")
                            .commit();

                } else {
                    LoginFragment loginFragment = new LoginFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contentContainer,loginFragment)
                            //.addToBackStack("")
                            .commit();
                }
            }
        }, 2000);
    }
}
