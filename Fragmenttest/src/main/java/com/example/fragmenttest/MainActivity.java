package com.example.fragmenttest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Fragment fragment1;
    Fragment fragment2;
    FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        fragment1=new BlankFragment();
        fragment2=new BlankFragment1();
        fragmentManager=getSupportFragmentManager();
        FragmentTransaction   fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container,fragment1);
        fragmentTransaction.commitAllowingStateLoss();

    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    public void show1(View view){
        FragmentTransaction   fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment2);
        fragmentTransaction.show(fragment1);
        fragmentTransaction.commitAllowingStateLoss();

    }
    public void show2(View view){
        if(fragment2.isAdded()){
            FragmentTransaction   fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.hide(fragment1);
            fragmentTransaction.show(fragment2);
            fragmentTransaction.commitAllowingStateLoss();
        }else{
            FragmentTransaction   fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.hide(fragment1);
            fragmentTransaction.add(R.id.container,fragment2);
            fragmentTransaction.commitAllowingStateLoss();
        }


    }
}
