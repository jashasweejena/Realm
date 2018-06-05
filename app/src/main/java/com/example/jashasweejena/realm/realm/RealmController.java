package com.example.jashasweejena.realm.realm;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;

import com.example.jashasweejena.realm.model.Book;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {

    private static RealmController instance;
    private final Realm realm;


    public RealmController(Application application) {

        realm = Realm.getDefaultInstance();
    }


    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }

        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }

        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }

        return instance;
    }

    public static RealmController getInstance() {

        return instance;

    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm instance
    public void refresh() {

        realm.refresh();
    }

    //Clear all objects from Book.class
    public void clearAll() {

        realm.beginTransaction();
        realm.clear(Book.class);
        realm.commitTransaction();
    }

    //Find all objects of Book.class
    public RealmResults<Book> getBooks() {

        return realm.where(Book.class).findAll();

    }

    //query a single item with the item id
    public Book getSingleBook(String id) {

        return realm.where(Book.class).equalTo("id", id).findFirst();

    }

    //check if Book.class is empty
    public boolean isEmpty() {

        return !realm.allObjects(Book.class).isEmpty();

    }

    //query example
    public RealmResults<Book> queryedBooksExample() {

        return realm.where(Book.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();
    }


}
