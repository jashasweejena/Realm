package com.example.jashasweejena.realm.activity;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.jashasweejena.realm.R;
import com.example.jashasweejena.realm.adapters.BooksAdapter;
import com.example.jashasweejena.realm.adapters.RealmBooksAdapter;
import com.example.jashasweejena.realm.app.Prefs;
import com.example.jashasweejena.realm.model.Book;
import com.example.jashasweejena.realm.realm.RealmController;
import com.nikoyuwono.realmbrowser.RealmBrowser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BooksAdapter adapter;
    private FloatingActionButton fab;
    private Realm realm;
    private LayoutInflater layoutInflater;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recycler);

        //get realm instance
        this.realm = RealmController.with(this).getRealm();

//        Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RealmBrowser realmBrowser = new RealmBrowser();
        realmBrowser.start();
        realmBrowser.showServerAddress(this);


        setupRecycler();


        //This means that setRealmData() will be called iff getPreLoad() returns false.
        //Once setRealmData() is called, it will automatically set boolean PRE_LOAD to true and thus
        //we need not reload the same data again and again, thereby saving memory.
        if (!Prefs.with(this).getPreLoad()) {

            setRealmData();


        }

        //refresh the realm instance
        RealmController.with(this).refresh();

        setRealmAdapter(RealmController.with(this).getBooks());

        Toast.makeText(this, "Press item card to edit, long press to remove", Toast.LENGTH_SHORT).show();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layoutInflater = MainActivity.this.getLayoutInflater();
                View view = layoutInflater.inflate(R.layout.edit_book, null);

                final EditText editTitle = view.findViewById(R.id.editTitle);
                final EditText editAuthor = view.findViewById(R.id.author);
                final EditText editThumbnail = view.findViewById(R.id.thumbnail);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(view)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Book book = new Book();
                                book.setId(RealmController.getInstance().getBooks().size() + 1);
                                book.setTitle(editTitle.getText().toString());
                                book.setAuthor(editAuthor.getText().toString());
                                book.setImageUrl(editThumbnail.getText().toString());

                                if (editTitle.getText() == null || editTitle.getText().toString().equals("") || editTitle.getText().toString().equals(" ")) {

                                    Toast.makeText(MainActivity.this, "Enter title. Entry not saved.", Toast.LENGTH_SHORT).show();

                                } else {

                                    realm.beginTransaction();
                                    realm.copyToRealm(book);
                                    realm.commitTransaction();

                                    adapter.notifyDataSetChanged();

                                    //Scroll to the end of RecyclerView
                                    recyclerView.scrollToPosition(RealmController.getInstance().getBooks().size() - 1);
                                }

                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void setRealmAdapter(RealmResults<Book> books) {

        RealmBooksAdapter realmAdapter = new RealmBooksAdapter(MainActivity.this, books, false);
        //Set the data and tell the RecyclerView to draw
        adapter.setRealmBaseAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }

    public void setupRecycler() {
        //So that changes in content doesn't affect the size of recyclerview and which, in turn, improves performance
        recyclerView.setHasFixedSize(true);

        //use a linearlayoutmanager since the cards are vertically scrollable
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //create an empty adapter and add it to rv
        adapter = new BooksAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    public void setRealmData() {

        ArrayList<Book> books = new ArrayList<>();

        Book book = new Book();
        book.setId(1 + System.currentTimeMillis());
        book.setAuthor("Reto Meier");
        book.setTitle("Android 4 application development");
        book.setImageUrl("https://api.androidhive.info/images/realm/2.png");
        books.add(book);

        book = new Book();
        book.setId(2 + System.currentTimeMillis());
        book.setAuthor("Itzik Ben-Gan");
        book.setTitle("Microsoft SQL Server 2012 T-SQL Fundamentals");
        book.setImageUrl("https://api.androidhive.info/images/realm/2.png");
        books.add(book);

        book = new Book();
        book.setId(3 + System.currentTimeMillis());
        book.setAuthor("Magnus Lie Hetland");
        book.setTitle("Beginning Python: From Novice To Professional Paperback");
        book.setImageUrl("https://api.androidhive.info/images/realm/3.png");
        books.add(book);

        book = new Book();
        book.setId(4 + System.currentTimeMillis());
        book.setAuthor("Chad Fowler");
        book.setTitle("The Passionate Programmer: Creating a Remarkable Career in Software Development");
        book.setImageUrl("https://api.androidhive.info/images/realm/4.png");
        books.add(book);

        book = new Book();
        book.setId(5 + System.currentTimeMillis());
        book.setAuthor("Yashavant Kanetkar");
        book.setTitle("Written Test Questions In C Programming");
        book.setImageUrl("https://api.androidhive.info/images/realm/5.png");
        books.add(book);

        for (Book b : books) {

            realm.beginTransaction();
            realm.copyToRealm(b);
            realm.commitTransaction();
        }

        Prefs.with(this).setPreLoad(true);

    }
}
