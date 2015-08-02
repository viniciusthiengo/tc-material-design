package br.com.thiengo.tcmaterialdesign;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


public class TransitionActivity_A extends Activity implements View.OnClickListener {
    private ViewGroup mViewGroup;
    private View mRedBox, mGreenBox, mBlueBox, mBlackBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS );
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS );

            TransitionInflater inflater = TransitionInflater.from( this );
            Transition transition = null; //inflater.inflateTransition(R.transition.transition_a);

            Slide slide = new Slide();
            slide.setDuration(3000);

            /*getWindow().setExitTransition(transition);
            getWindow().setReenterTransition(slide);*/

            //getWindow().setSharedElementExitTransition(new ChangeTransform());
            //getWindow().setSharedElementExitTransition(  inflater.inflateTransition(R.transition.transition_shared)  );
            //getWindow().getSharedElementReenterTransition(  );
        }

        setContentView(R.layout.activity_transition_a);

        mViewGroup = (ViewGroup) findViewById(R.id.layout_root_view);
        mViewGroup.setOnClickListener(this);

        mRedBox = findViewById(R.id.red_box);
        mGreenBox = findViewById(R.id.green_box);
        mBlueBox = findViewById(R.id.blue_box);
        mBlackBox = findViewById(R.id.black_box);
    }

    @Override
    public void onClick(View v) {
        Intent it = new Intent( this, TransitionActivity_B.class );

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair.create(mBlackBox, "button_shared"));

            startActivity( it, options.toBundle() );
        }
        else{
            startActivity(it);
        }

        /*TransitionInflater inflater = TransitionInflater.from( this );
        Transition transition = inflater.inflateTransition( R.transition.transition_a );

        TransitionManager.beginDelayedTransition( mViewGroup , transition);
        getWindow().setTransitionBackgroundFadeDuration(3000);
        toggleVisibility( mRedBox, mGreenBox, mBlueBox, mBlackBox );*/
    }

    public static void toggleVisibility(View... views){
        for(View view : views){
            view.setVisibility( view.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE );
        }
    }
}
