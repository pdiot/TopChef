package com.example.pierre.topchef.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.pierre.topchef.R;
import com.example.pierre.topchef.database.DatabaseContract;
import com.example.pierre.topchef.database.DatabaseHelper;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {


    private DatabaseHelper myDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        myDbHelper = new DatabaseHelper(this);


        LinearLayout ll = findViewById(R.id.scrollVertical);
        LinearLayout line = new LinearLayout(this);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        ll.addView(line);

        Button bt = new Button(this);
        bt.setText("Add another product");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAnotherProduct();
            }
        });

        EditText et = new EditText(this);
        et.setHint("Enter the product name");

        line.addView(et);
        line.addView(bt);

        View v = findViewById(R.id.searchButton);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMatches();
            }
        });

        initDb();
    }

    private void addAnotherProduct() {
        LinearLayout ll = findViewById(R.id.scrollVertical);
        LinearLayout line = new LinearLayout(this);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        ll.addView(line);

        Button bt = new Button(this);
        bt.setText("Add another product");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAnotherProduct();
            }
        });

        EditText et = new EditText(this);
        et.setHint("Enter the product name");

        line.addView(et);
        line.addView(bt);
        et.requestFocus();
    }

    private void searchMatches() {
        Intent intent = new Intent(this, MatchesDisplay.class);

        LinearLayout ll = findViewById(R.id.scrollVertical);
        int nbLines = ll.getChildCount();
        // TODO use Product Objects
        ArrayList<String> products = new ArrayList<String>();

        for (int i = 0; i < nbLines; i++) {
            System.out.println(i);
            LinearLayout line = (LinearLayout) ll.getChildAt(i);
            EditText et = (EditText) line.getChildAt(0);
            if (et==null) {
                System.out.println("line(" + i + " est null");
            }
            else {
                System.out.println(et.toString());
            }
            products.add(et.getText().toString());
        }
        intent.putExtra("productList", products);
        startActivity(intent);
    }

    private void initDb() {

        SQLiteDatabase db = myDbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from products", null);
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_FR, "banane");
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_EN, "banana");
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_TASTE_ID, 1);

            db.insert(DatabaseContract.ProductsTable.TABLE_NAME, null, values);

            values = new ContentValues();
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_FR, "chocolat blanc");
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_EN, "white chocolate");
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_TASTE_ID, 1);

            db.insert(DatabaseContract.ProductsTable.TABLE_NAME, null, values);

            values = new ContentValues();
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_FR, "menthe");
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_EN, "mint");
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_TASTE_ID, 2);

            db.insert(DatabaseContract.ProductsTable.TABLE_NAME, null, values);
            values = new ContentValues();
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_FR, "creme fouettee");
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_EN, "whipped cream");
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_TASTE_ID, 1);

            db.insert(DatabaseContract.ProductsTable.TABLE_NAME, null, values);
            System.out.println(cursor.getCount() + " products in base");

        } else {
            System.out.println(cursor.getCount() + " products in base");
        }

        cursor = db.rawQuery("select * from tastes", null);
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.TasteTable.COLUMN_NAME_TASTE_NAME_FR, "sucré");
            values.put(DatabaseContract.TasteTable.COLUMN_NAME_TASTE_NAME_EN, "sweet");

            db.insert(DatabaseContract.TasteTable.TABLE_NAME, null, values);

            values = new ContentValues();
            values.put(DatabaseContract.TasteTable.COLUMN_NAME_TASTE_NAME_FR, "acide");
            values.put(DatabaseContract.TasteTable.COLUMN_NAME_TASTE_NAME_EN, "acidic");

            db.insert(DatabaseContract.TasteTable.TABLE_NAME, null, values);
            System.out.println(cursor.getCount() + " tastes in base");
        } else {
            System.out.println(cursor.getCount() + " tastes in base");
        }

        cursor = db.rawQuery("select * from good_associations", null);
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.GoodAssociationsTable.COLUMN_NAME_PRODUCT_ID_1, 1);
            values.put(DatabaseContract.GoodAssociationsTable.COLUMN_NAME_PRODUCT_ID_2, 2);

            db.insert(DatabaseContract.GoodAssociationsTable.TABLE_NAME, null, values);
            values = new ContentValues();
            values.put(DatabaseContract.GoodAssociationsTable.COLUMN_NAME_PRODUCT_ID_1, 1);
            values.put(DatabaseContract.GoodAssociationsTable.COLUMN_NAME_PRODUCT_ID_2, 4);

            db.insert(DatabaseContract.GoodAssociationsTable.TABLE_NAME, null, values);
            values = new ContentValues();
            values.put(DatabaseContract.GoodAssociationsTable.COLUMN_NAME_PRODUCT_ID_1, 2);
            values.put(DatabaseContract.GoodAssociationsTable.COLUMN_NAME_PRODUCT_ID_2, 3);

            db.insert(DatabaseContract.GoodAssociationsTable.TABLE_NAME, null, values);
            System.out.println(cursor.getCount() + " good associations in base");
        }
        else {
            System.out.println(cursor.getCount() + " good associations in base");
        }

        cursor = db.rawQuery("select * from bad_associations", null);
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.BadAssociationsTable.COLUMN_NAME_PRODUCT_ID_1, 1);
            values.put(DatabaseContract.BadAssociationsTable.COLUMN_NAME_PRODUCT_ID_2, 3);

            db.insert(DatabaseContract.BadAssociationsTable.TABLE_NAME, null, values);

            System.out.println(cursor.getCount() + " bad associations in base");
        }
        else {
            System.out.println(cursor.getCount() + " bad associations in base");
        }


    }
}
