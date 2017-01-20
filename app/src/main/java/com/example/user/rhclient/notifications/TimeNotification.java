package com.example.user.rhclient.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.user.rhclient.R;
import com.example.user.rhclient.api.ApiFactory;
import com.example.user.rhclient.helper.Product;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TimeNotification extends BroadcastReceiver {

    private final int NOTIFICATION_ID = 1;

    private NotificationManager mNotificationManager;
    private String mCurrentCategory;
    private int mSize;


    @Override
    public void onReceive(final Context context, Intent intent) {

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mCurrentCategory = intent.getStringExtra("currCategory");
        mSize = intent.getIntExtra("size", 0);

        ApiFactory.createApi().getProducts(mCurrentCategory).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful() || response.body() == null){
                    return;
                }

                JsonArray array = response.body().getAsJsonArray("posts");

                int typeOfNotification = array.size() - mSize;

                switch (typeOfNotification){
                    case 0: break;
                    case 1: {

                        JsonObject object = array.get(array.size() - 1).getAsJsonObject();

                        JsonObject thumbnail = object.getAsJsonObject("thumbnail");
                        String url = thumbnail.get("image_url").getAsString();

                        String title = object.get("name").getAsString();
                        String desc = object.get("tagline").getAsString();

                        NotificationCompat.Builder builder = new NotificationCompat
                                .Builder(context)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("New post!")
                                .setContentText(title.toUpperCase() + "\n" + desc);

                        Notification notification = builder.build();

                        notification.flags |= Notification.FLAG_AUTO_CANCEL;

                        mNotificationManager.notify(NOTIFICATION_ID, notification);
                        break;
                    }
                    default: {

                        NotificationCompat.Builder builder = new NotificationCompat
                                .Builder(context)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("New posts!")
                                .setContentText("New " + typeOfNotification + " posts!");


                        Notification notification = builder.build();

                        notification.flags |= Notification.FLAG_AUTO_CANCEL;

                        mNotificationManager.notify(NOTIFICATION_ID, notification);
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

}
