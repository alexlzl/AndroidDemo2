package com.gome.friendcircle.activity;

import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v7.app.AppCompatActivity;
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
        super.onBackPressed();
    }
}
