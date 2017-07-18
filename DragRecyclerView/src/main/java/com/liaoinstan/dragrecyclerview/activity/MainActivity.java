package com.liaoinstan.dragrecyclerview.activity;

import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.liaoinstan.dragrecyclerview.R;
import com.liaoinstan.dragrecyclerview.fragment.MainFragment;
import com.liaoinstan.dragrecyclerview.fragment.MyGridFragment;
import com.liaoinstan.dragrecyclerview.fragment.MyListFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        if (savedInstanceState==null){
//            MainFragment mainFragment = new MainFragment();
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.fragment,mainFragment)
//                    .commit();
//        }
        Fragment fragment = null;
        fragment = new MyGridFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId()){
            case R.id.list:
                fragment = new MyListFragment();
                break;
            case R.id.grid:
                fragment = new MyGridFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}
