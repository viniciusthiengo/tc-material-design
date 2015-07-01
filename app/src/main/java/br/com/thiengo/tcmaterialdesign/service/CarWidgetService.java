package br.com.thiengo.tcmaterialdesign.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import br.com.thiengo.tcmaterialdesign.adapters.CarWidgetFactoryAdapter;

/**
 * Created by viniciusthiengo on 6/28/15.
 */
public class CarWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new CarWidgetFactoryAdapter(this, intent);
    }
}
