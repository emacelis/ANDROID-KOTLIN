package com.realstatey.emmateamappnotes;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;


public class MyFirebaseMessagingService  extends FirebaseMessagingService {
private static final  String TAG="FCM";


public void onMessageRecived(RemoteMessage remoteMessage){
    Log.e(TAG,remoteMessage.getFrom());
    openActivity(remoteMessage);
    }

    public void openActivity(RemoteMessage remoteMessage){
    //aqui inicia el tema de la  notificación
        Intent intent=new Intent(this,NotificationActivity.class);
        intent.putExtra("title",remoteMessage.getNotification().getTitle());
        PendingIntent pendingIntent=PendingIntent.getActivity(
                this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT );
                //aqui sig siendo el inicia del tema de la  notificación

                //ahora se define como va a quedar el tema de la notificación

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        //el BUILDER es el manager de las NOTIFICACIONES
        builder.setSmallIcon(R.drawable.com_facebook_button_icon)
                .setContentTitle("Titulo Notificacion")
                .setContentIntent(pendingIntent);
            //CONFIGURACION DEL ICONO

        //MANEJO DE LA NOTIFICACION
        NotificationManager notificationManager=(NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);// trabajar con el servicio de notiicación
        notificationManager.notify("TAG",10,builder.build());
    }

}
