package com.example.fragmenttest;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment1 extends Fragment {


    public BlankFragment1() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    /**
     * 使用setRetainInstance方法
     * <p>
     * 设置该方法为true后，可以让fragment在activity被重建时保持实例不变。
     *
     * @param savedInstanceState
     * @Override public void onCreate(@Nullable Bundle savedInstanceState) {
     * super.onCreate(savedInstanceState);
     * setRetainInstance(true);
     * }
     * 此方法设置后会让activity在重建时的fragment生命周期与activity生命周期产生一些差别。差别如下：
     * <p>
     * onDestroy将不会被调用（但onDetach方法会，因为fragment将会先从当前activity中分离）
     * onCreate将因为fragment没有被重新创建而不会被调用
     * onAttach和onActivityCreated还将会被调用
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank_fragment1, container, false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }
}
