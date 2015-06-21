package br.com.thiengo.tcmaterialdesign.interfaces;

import android.view.View;

/**
 * Created by viniciusthiengo on 4/5/15.
 */
public interface RecyclerViewOnClickListenerHack {
    public void onClickListener(View view, int position);
    public void onLongPressClickListener(View view, int position);
}
