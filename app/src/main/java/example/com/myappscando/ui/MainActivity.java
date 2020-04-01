package example.com.myappscando.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import example.com.myappscando.R;
import utils.Backable;
import utils.MyModelSaveDb;
import utils.OnActivityResultDataChanged;
import utils.OnScanQrButtonClicked;
import utils.authenCallback;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import static example.com.myappscando.ui.ScanFragment.myModelSaveDb;
import static utils.Constants.SHARED_PREFERENCES_NAME;

public class MainActivity extends AppCompatActivity implements OnScanQrButtonClicked, authenCallback {

    //Callback
    public static OnActivityResultDataChanged mOnActivityResultDataChanged;

    public static void setOnActivityResultDataChanged(OnActivityResultDataChanged listener) {
        mOnActivityResultDataChanged = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MainFragment mainFragment = new MainFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contentContainer, mainFragment)
                    //.addToBackStack("")
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "QR Code Scan Cancelled", Toast.LENGTH_SHORT).show();
            }
            {
                mOnActivityResultDataChanged.onDataReceived(result.getContents());
                gotoGetDataFragment(result.getContents(), myModelSaveDb());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void gotoGetDataFragment(String contents, MyModelSaveDb myModelSaveDb) {
        //Toast.makeText(this, "Goto  getDataFragment*************: "+contents,Toast.LENGTH_SHORT).show();
        ShowAuthenFragment showAuthenFragment = new ShowAuthenFragment();
        Bundle b = new Bundle();
        b.putString("qrcode", contents);
        b.putDouble("la1", myModelSaveDb.getLa());
        b.putDouble("lo1", myModelSaveDb.getLo());
        b.putString("address1", myModelSaveDb.getAddresses());
        b.putString("str_userid", myModelSaveDb.getStr_userid());
        b.putString("token", myModelSaveDb.getToken());
        showAuthenFragment.setArguments(b);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentContainer, showAuthenFragment)
                //.addToBackStack("")\
                .commitAllowingStateLoss();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "Click logout ", Toast.LENGTH_SHORT).show();
            logoutMethod();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // เวลากด back ที่ Login fragment แล้ว fragment ซ้อนกัน
    @Override
    public void onBackPressed() {
        boolean handled = false;
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof Backable) {
                handled = ((Backable) fragment).onBackPressed();
            }
        }
        if (!handled) super.onBackPressed();
    }

    private void logoutMethod() {
        SharedPreferences sharedpreferences = getApplicationContext()
                .getApplicationContext()
                .getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();

        LoginFragment loginFragment = new LoginFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentContainer, loginFragment)
                .commit();
    }

    @Override
    public void triggerScanQr() {
        System.out.println("Fragment Just Triggered MainActivity");
        /*IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();*/

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan something");
        integrator.setCameraId(0);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    @Override
    public void callbackToAuthen2Fragment(String pid,String token) {
        ShowAuthen2Fragment showAuthen2Fragment = new ShowAuthen2Fragment();
        Bundle b = new Bundle();
        b.putString("key", pid);
        b.putString("token", token);
        showAuthen2Fragment.setArguments(b);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentContainer, showAuthen2Fragment)
                .commit();
    }
}
