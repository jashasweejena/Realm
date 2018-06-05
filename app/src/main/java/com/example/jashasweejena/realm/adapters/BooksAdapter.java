package com.example.jashasweejena.realm.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jashasweejena.realm.R;
import com.example.jashasweejena.realm.app.Prefs;
import com.example.jashasweejena.realm.model.Book;
import com.example.jashasweejena.realm.realm.RealmController;

import io.realm.Realm;
import io.realm.RealmResults;

public class BooksAdapter extends RealmRecyclerViewAdapter<Book> {

    final Context context;
    private Realm realm;
    private LayoutInflater inflater;


    public BooksAdapter(Context context) {

        this.context = context;
    }


    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        realm = RealmController.getInstance().getRealm();

        //get the object
        final Book book = getItem(position);

        //cast the generic viewholder to our specific one
        final CardViewHolder cardViewHolder = (CardViewHolder) holder;

        //complete the formalities
        cardViewHolder.textTitle.setText(book.getTitle());
        cardViewHolder.textAuthor.setText(book.getAuthor());
        cardViewHolder.textDesc.setText(book.getDescription());

        //load the background image
        if(book.getImageUrl() != null) {

            Glide.with(context)
                    .asBitmap()
                    .load(book.getImageUrl().replace("https", "http"))
                    .apply(new RequestOptions()
                    .fitCenter())
                    .into(cardViewHolder.imageBg);

        }

        //remove single match from realm
        cardViewHolder.card.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                RealmResults<Book> results = realm.where(Book.class).findAll();

                //Get the book title to show it in toast message
                Book b = results.get(position);
                String title = b.getTitle();

                //All changes must happen in a transaction
                realm.beginTransaction();

                //remove single match
                results.remove(position);
                realm.commitTransaction();

                if(results.size() == 0) {
                    Prefs.with(context).setPreLoad(false);
                }

                notifyDataSetChanged();

                Toast.makeText(context, title + " is removed from Realm", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //update single match from realm
        cardViewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View content = inflater.inflate(R.layout.edit_book, null);
                final EditText editTitle = content.findViewById(R.id.editTitle);
                final EditText editAuthor = content.findViewById(R.id.author);
                final EditText editThumbnail = content.findViewById(R.id.thumbnail);

                final RealmResults<Book> results = realm.where(Book.class).findAll();

                editTitle.setText(results.get(position).getTitle());
                editAuthor.setText(results.get(position).getAuthor());
                editThumbnail.setText(results.get(position).getImageUrl());

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(content)
                        .setTitle("Edit Book")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Book book = results.get(position);

                                realm.beginTransaction();

                                book.setTitle(editTitle.getText().toString());
                                book.setAuthor(editAuthor.getText().toString());
                                book.setImageUrl(editThumbnail.getText().toString());

                                realm.copyToRealm(book);
                                realm.commitTransaction();
                                
                                notifyDataSetChanged();
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

    @Override
    public int getItemCount() {
        if(getRealmAdapter() != null)
        {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        public CardView card;
        public TextView textTitle;
        public TextView textAuthor;
        public TextView textDesc;
        public ImageView imageBg;

        public CardViewHolder(View view) {

            super(view);

            card = view.findViewById(R.id.card_books);
            textTitle = view.findViewById(R.id.text_books_title);
            textAuthor = view.findViewById(R.id.text_books_author);
            textDesc =  view.findViewById(R.id.text_books_description);
            imageBg = view.findViewById(R.id.img_thumbnail);
        }


    }
}
