package example.com.myappscando.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import example.com.myappscando.R;
import utils.Backable;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoDataFragment extends Fragment implements Backable {

    View v;

    public NoDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_no_data, container, false);
        return v;
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
