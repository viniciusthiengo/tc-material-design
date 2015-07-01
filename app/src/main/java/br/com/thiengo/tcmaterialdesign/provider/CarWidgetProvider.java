package br.com.thiengo.tcmaterialdesign.provider;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import javax.crypto.Mac;

import br.com.thiengo.tcmaterialdesign.MainActivity;
import br.com.thiengo.tcmaterialdesign.R;
import br.com.thiengo.tcmaterialdesign.domain.Car;
import br.com.thiengo.tcmaterialdesign.service.CarWidgetService;

/**
 * Created by viniciusthiengo on 6/28/15.
 */
public class CarWidgetProvider extends AppWidgetProvider {
    public static final String LOAD_CARS = "br.com.thiengo.tcmaterialdesign.provider.LOAD_CARS";
    public static final String FILTER_CAR = "br.com.thiengo.tcmaterialdesign.provider.FILTER_CAR";
    public static final String FILTER_CAR_ITEM = "br.com.thiengo.tcmaterialdesign.provider.FILTER_CAR_ITEM";


    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        if( intent != null ){
            if( intent.getAction().equalsIgnoreCase( LOAD_CARS ) ){
                int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

                if( appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID ){
                    //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_collection);
                    onUpdate(context, appWidgetManager, new int[]{appWidgetId});
                }
            }
            else if( intent.getAction().equalsIgnoreCase( FILTER_CAR ) ){
                String urlPhoto = intent.getStringExtra(FILTER_CAR_ITEM);

                Intent it = new Intent(context, MainActivity.class);
                it.putExtra(FILTER_CAR_ITEM, urlPhoto);
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(it);
            }
        }

        super.onReceive(context, intent);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for(int i = 0; i < appWidgetIds.length; i++){
            Intent itService = new Intent(context, CarWidgetService.class);
            itService.putExtra( AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i] );

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_collection);
            views.setRemoteAdapter(R.id.lv_collection, itService);
            views.setEmptyView(R.id.lv_collection, R.id.tv_loading);

            Intent itLoadCars = new Intent(context, CarWidgetProvider.class);
            itLoadCars.setAction( LOAD_CARS );
            itLoadCars.putExtra( AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i] );
            PendingIntent piLoadCars = PendingIntent.getBroadcast(context, 0, itLoadCars, 0);
            views.setOnClickPendingIntent( R.id.iv_update_collection, piLoadCars);

            Intent itOpen = new Intent(context, MainActivity.class);
            PendingIntent piOpen = PendingIntent.getActivity(context, 0, itOpen, 0);
            views.setOnClickPendingIntent( R.id.iv_open_app, piOpen);

            Intent itFilterCar = new Intent(context, CarWidgetProvider.class);
            itFilterCar.setAction( FILTER_CAR );
            PendingIntent piFilterCar = PendingIntent.getBroadcast(context, 0, itFilterCar, 0);
            views.setPendingIntentTemplate(R.id.lv_collection, piFilterCar);

            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.lv_collection);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
