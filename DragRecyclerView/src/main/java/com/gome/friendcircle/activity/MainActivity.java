package com.gome.friendcircle.activity;

import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.gome.app.R;
import com.gome.friendcircle.fragment.RecyclerViewFragment;

public class MainActivity extends AppCompatActivity {
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);
        fragment = new RecyclerViewFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }


    @Override
    public void onBackPressed() {
        ((RecyclerViewFragment) fragment).windowViewManager.removeFloatView();
        Log.e("tag", "onBackPressed");
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ((RecyclerViewFragment) fragment).windowViewManager.removeFloatView();
        Log.e("tag", "onStop");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus){
            Log.e("tag", "onWindowFocusChanged");
            ((RecyclerViewFragment) fragment).hideWindowView();
        }
    }
}
