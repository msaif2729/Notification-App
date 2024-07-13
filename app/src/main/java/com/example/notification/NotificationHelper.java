    package com.example.notification;

    import android.app.NotificationChannel;
    import android.app.NotificationManager;
    import android.app.PendingIntent;
    import android.content.Context;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.graphics.Bitmap;
    import android.graphics.Color;
    import android.graphics.drawable.Drawable;
    import android.net.Uri;
    import android.util.Base64;
    import android.util.Log;

    import androidx.core.app.ActivityCompat;
    import androidx.core.app.NotificationCompat;
    import androidx.core.app.NotificationManagerCompat;

    import com.squareup.picasso.Picasso;
    import com.squareup.picasso.Target;

    import java.io.ByteArrayOutputStream;

    public class NotificationHelper {

        private static final String CHANNEL_ID = "notify";

        public static void createChannel(Context context) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null && notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Notify", NotificationManager.IMPORTANCE_DEFAULT);
                    channel.enableVibration(true);
                    channel.setDescription("This is to notify");
                    channel.enableLights(true);
                    channel.setLightColor(Color.RED);

                    notificationManager.createNotificationChannel(channel);
                }
            }
        }

        public static void sendNotification(Context context, String title, String message, Uri filename) {




            Intent intent = new Intent(context, MainActivity2.class);
            intent.putExtra("title", title);
            intent.putExtra("mssg", message);
            intent.putExtra("image", String.valueOf(filename));

//            Log.d("Mohammed Saif", filename);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                    .setSmallIcon(R.mipmap.ic_launcher);

            Picasso.get().load(filename).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(bitmap));
                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    notificationManagerCompat.notify((int) System.currentTimeMillis(), builder.build());
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });




        }


    }
