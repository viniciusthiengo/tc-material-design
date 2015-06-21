package br.com.thiengo.tcmaterialdesign;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewGroup;


public class TransitionActivity_B extends Activity{
    private View mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Slide slide = new Slide();
            slide.setDuration(3000);
            Explode explode = new Explode();
            explode.setDuration(3000);

            /*getWindow().setEnterTransition(slide);
            getWindow().setReturnTransition(explode);*/

            getWindow().setSharedElementEnterTransition( new ChangeBounds() );
            //getWindow().setSharedElementEnterTransition( TransitionInflater.from( this ).inflateTransition(R.transition.transition_shared) );
        }

        setContentView(R.layout.activity_transition_b);

        mButton = findViewById(R.id.button);
    }
}
