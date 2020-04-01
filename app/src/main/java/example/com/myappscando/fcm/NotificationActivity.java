package example.com.myappscando.fcm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import example.com.myappscando.R;
import example.com.myappscando.ui.MainActivity;
import example.com.myappscando.ui.MainFragment;
import utils.Backable;


public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Toast.makeText(this, "You clicked the back button", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        fileList();
    }
}
