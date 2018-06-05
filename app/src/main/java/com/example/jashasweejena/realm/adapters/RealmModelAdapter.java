package com.example.jashasweejena.realm.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import io.realm.RealmBaseAdapter;
import io.realm.RealmObject;
import io.realm.RealmResults;

public abstract class RealmModelAdapter<T extends RealmObject> extends RealmBaseAdapter<T>{

    public RealmModelAdapter(Context context, RealmResults realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return null;

    }


}
