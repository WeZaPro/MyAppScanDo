package example.com.myappscando.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import example.com.myappscando.R;
import utils.ExampleItem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DetailProductFragment extends Fragment {

    View v;
    TextView textViewCreator;//,textViewLike;
    ImageView imageView;
    ExampleItem exampleItem;
    public DetailProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.fragment_detail_product, container, false);
        textViewCreator = v.findViewById(R.id.textViewCreator);
        //textViewLike = v.findViewById(R.id.textViewLike);
        imageView = v.findViewById(R.id.imageView);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments() != null){
            exampleItem = getArguments().getParcelable("key");
            textViewCreator.setText("Creator : "+exampleItem.getCreator());
            //textViewLike.setText("Like : "+exampleItem.getLikeCount());

            Picasso.get().load(exampleItem.getImageUrl()).fit()
                    //.centerInside()
                    //.centerCrop(-18)
                    .centerCrop(0)
                    .into(imageView);

            //Toast.makeText(getActivity(),"Data get is : "+exampleItem.getLikeCount(),Toast.LENGTH_SHORT).show();

        }
    }
}
