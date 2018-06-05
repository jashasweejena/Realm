package com.example.jashasweejena.realm.adapters;

import android.content.Context;

import com.example.jashasweejena.realm.model.Book;

import io.realm.RealmResults;

public class RealmBooksAdapter extends RealmModelAdapter<Book> {

    public RealmBooksAdapter(Context context, RealmResults realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);

    }


}
