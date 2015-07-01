package br.com.thiengo.tcmaterialdesign.adapters;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.FrameLayout;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import br.com.thiengo.tcmaterialdesign.MainActivity;
import br.com.thiengo.tcmaterialdesign.R;
import br.com.thiengo.tcmaterialdesign.domain.Car;
import br.com.thiengo.tcmaterialdesign.extras.DataUrl;
import br.com.thiengo.tcmaterialdesign.provider.CarWidgetProvider;

/**
 * Created by viniciusthiengo on 6/28/15.
 */
public class CarWidgetFactoryAdapter implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private List<Car> mList;
    private int size;


    public CarWidgetFactoryAdapter(Context context, Intent intent){
        mContext = context;

        float scale = mContext.getResources().getDisplayMetrics().density;
        size = (int)( 50 * scale + 0.5f );
    }


    @Override
    public void onCreate() {
        mList = new MainActivity().getSetCarList(10);
    }

    @Override
    public void onDataSetChanged() {
        Collections.shuffle(mList, new Random());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.appwidget_item);
        views.setTextViewText(R.id.tv_model, mList.get(position).getModel() );
        views.setTextViewText(R.id.tv_brand, mList.get(position).getBrand() );

        try {
            Bitmap myBitmap = Glide.with(mContext.getApplicationContext())
                    .load(DataUrl.getUrlCustom(mList.get(position).getUrlPhoto(), size))
                    .asBitmap()
                    .centerCrop()
                    .into(size, size)
                    .get();

            views.setImageViewBitmap(R.id.iv_car, myBitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Intent itFilter = new Intent();
        itFilter.putExtra(CarWidgetProvider.FILTER_CAR_ITEM, mList.get(position).getUrlPhoto());
        views.setOnClickFillInIntent(R.id.rl_container, itFilter);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
