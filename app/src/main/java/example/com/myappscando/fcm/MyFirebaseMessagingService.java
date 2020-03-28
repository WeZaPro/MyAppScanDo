package example.com.myappscando.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import example.com.myappscando.R;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String ADMIN_CHANNEL_ID = "admin_channel";
    String msg;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check device get message (App เปิดอยู่)
        Log.d("getMsg","GET MESSAGE");

        // click action on notification ##############
        String click_action = remoteMessage.getNotification().getClickAction();

        String imageUri = remoteMessage.getData().get("image");

        //test send image %%%%%%%%
        // เอาไว้ลองใหม่ @@@@@@@
        //Uri imageSendUri = Uri.parse(remoteMessage.getData().get("image"));;


        Bitmap bitmap = getBitmapfromUrl(imageUri);
        /*Uri notificationSoundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.mysound);*/

        // Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        msg = remoteMessage.getData().toString();
        Log.d("check", msg);
        //Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.sirivatana.co.th/"));
        //Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(imageUri));

        // click action on notification ##############
        Intent notificationIntent = new Intent(click_action);

        //test send intent parameter to NotificationActivity
        notificationIntent.putExtra("NotificationMessage", remoteMessage.getNotification().getTitle());

        // เอาไว้ลองใหม่ @@@@@@@
        //notificationIntent.putExtra("imageUri", Uri.parse(imageUri).toString());// test%%%%%%%

        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, uniqueInt, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context
                .NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(bitmap)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setLargeIcon(bitmap)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap))
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                //.setSound(notificationSoundUri)
                .setContentIntent(pendingIntent);
        Log.d("check", pendingIntent.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        notificationManager.notify(notificationID, notificationBuilder.build());


        if (remoteMessage.getNotification() != null) {
        }
    }

    private void handleNow() {
    }

    private void scheduleJob() {
    }

    @Override
    public void onNewToken(String token) {
        Log.d("token", "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        SharedPreferences sharedPref = getSharedPreferences(Constances.MY_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constances.TOKEN, token);
        editor.commit();

        Log.d("token", "send token: " + token);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager) {
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to devicee notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    private Bitmap getBitmapfromUrl(String imageUri) {

        try {
            URL url = new URL(imageUri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
