package com.example.nirmawati.praktikum7;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SimpleCursorAdapter aa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView lv = (ListView) findViewById(R.id.lv);
        BookHelper helper = new BookHelper(this);//gunakan class bookhelper yang telah dibuat
        SQLiteDatabase db = helper.getWritableDatabase();

        //query data
        String[] projection = {"_id", "title", "author"};
        Cursor c = db.query("book_entries", projection, null, null, null, null, null);

        aa = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, c, new String[]{"title", "author"},
                new int[]{android.R.id.text1, android.R.id.text2}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lv.setAdapter(aa);

        ArrayList<String> data = new ArrayList<String>();
        c.moveToFirst();

        while (!c.isAfterLast()) {
            String title = c.getString(c.getColumnIndex("title"));
            data.add(title);
            c.moveToNext();
        }
        // lakukan pengecekan
        if (data.isEmpty()) {
            data.add("No Book Entries yet, please add");
        }

        registerForContextMenu(lv);

        //gunakan listview yang tadi dibuat untuk menampilkan data
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, data);
        //lv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            Intent ii = new Intent(this, handle.class);
            startActivity(ii);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_item, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {//untuk meregister fungsi ke menu
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == R.id.context_hapus) {
            delete_book(info.id);
        } else if (item.getItemId() == R.id.context_ubah) {
            update_book(info.id);
        }
        return true;
    }

    private void delete_book(long id) {//function untuk menghapus
        BookHelper helper = new BookHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        int delete = db.delete("book_entries", "_id=?", new String[]{String.valueOf(id)});

        Cursor x = db.query("book_entries", new String[]{"_id", "title", "author"}, null, null, null, null, null);

        aa.changeCursor(x);
        aa.notifyDataSetChanged();
    }

    private void update_book(long id) {//function untuk mengupdate
        BookHelper helper = new BookHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor e = db.query("book_entries",
                new String[]{"title", "author"},
                "_id=?",
                new String[]{String.valueOf(id)},
                null, null, null);
        e.moveToFirst();
        Intent ii = new Intent(this, handle.class);
        ii.putExtra("_id", id);
        ii.putExtra("title", e.getString(e.getColumnIndex("title")));
        ii.putExtra("author", e.getString(e.getColumnIndex("author")));

        startActivity(ii);
    }
}
