package com.example.androidlananh.ui.base;

/**
 * Created by Ali Asadi on 26/03/2018.
 */
public abstract class BasePresenter<View extends BaseView> {

    protected View view;

    protected BasePresenter(View view) {
        this.view = view;
    }
    protected void onDestroy(){}
    void onDetach() {
        view = null; //avoid memory leak
    }



}