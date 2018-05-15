package com.example.pierre.topchef.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pierre.topchef.R;
import com.example.pierre.topchef.database.DatabaseContract;
import com.example.pierre.topchef.database.DatabaseHelper;
import com.example.pierre.topchef.metier.Product;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {


    private String language;
    private DatabaseHelper myDbHelper;
    private ArrayList<String> PRODUCTS;
    ArrayAdapter<String> arrayAdapter;
    private int nbLines = 0;
    private ArrayList<Product> products;
    int width, height;


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
               searchMatches();
            }
        });

        nbLines = 0;
        products = new ArrayList<>();
        myDbHelper = new DatabaseHelper(this);
        setLanguageFrench();
        getScreenDimensions();

        Button b = findViewById(R.id.buttonfr);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLanguageFrench();
            }
        });

        b = findViewById(R.id.buttonen);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLanguageEnglish();
            }
        });

        initDb();

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select products."+ DatabaseContract.ProductsTable.COLUMN_NAME_NAME_FR +" from products;"
                , null);

        PRODUCTS = new ArrayList<>();

        while (cursor.moveToNext()) {
            PRODUCTS.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_FR)));
        }

        cursor.close();
        db.close();

        addAnotherProduct();

    }

    private void setLanguageFrench() {
        this.language = "FRENCH";
        Button b = findViewById(R.id.buttonfr);
        b.getBackground(). setColorFilter(Color.rgb(66, 244, 152), PorterDuff.Mode.SRC_IN);
        b = findViewById(R.id.buttonen);
        b.getBackground(). setColorFilter(Color.rgb(214, 214, 214), PorterDuff.Mode.SRC_IN);

        LinearLayout ll = findViewById(R.id.scrollVertical);
        int nbLines = ll.getChildCount();
        // TODO use Product Objects

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select products."+ DatabaseContract.ProductsTable.COLUMN_NAME_NAME_FR +" from products;"
                , null);

        PRODUCTS = new ArrayList<>();

        while (cursor.moveToNext()) {
            PRODUCTS.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_FR)));
        }

        cursor.close();
        db.close();

        for (int i = 0; i < nbLines; i++) {
            System.out.println(i);
            LinearLayout line = (LinearLayout) ll.getChildAt(i);
            AutoCompleteTextView et = (AutoCompleteTextView) line.getChildAt(0);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, PRODUCTS);
            et.setAdapter(arrayAdapter);
        }
    }

    private void setLanguageEnglish() {
        this.language = "ENGLISH";
        Button b = findViewById(R.id.buttonen);
        b.getBackground(). setColorFilter(Color.rgb(66, 244, 152), PorterDuff.Mode.SRC_IN);
        b = findViewById(R.id.buttonfr);
        b.getBackground(). setColorFilter(Color.rgb(214, 214, 214), PorterDuff.Mode.SRC_IN);
        LinearLayout ll = findViewById(R.id.scrollVertical);
        int nbLines = ll.getChildCount();
        // TODO use Product Objects

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select products."+ DatabaseContract.ProductsTable.COLUMN_NAME_NAME_EN +" from products;"
                , null);

        PRODUCTS = new ArrayList<>();

        while (cursor.moveToNext()) {
            PRODUCTS.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_EN)));
        }

        cursor.close();
        db.close();

        for (int i = 0; i < nbLines; i++) {
            System.out.println(i);
            LinearLayout line = (LinearLayout) ll.getChildAt(i);
            AutoCompleteTextView et = (AutoCompleteTextView) line.getChildAt(0);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, PRODUCTS);
            et.setAdapter(arrayAdapter);
        }
    }

    private void addAnotherProduct() {
        LinearLayout ll = findViewById(R.id.scrollVertical);
        LinearLayout line = new LinearLayout(this);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        line.setId(nbLines);
        nbLines ++;

        ll.addView(line);

        Button bt = new Button(this);
        bt.setText("Add another product");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAnotherProduct();
            }
        });

        AutoCompleteTextView et = new AutoCompleteTextView(this);
        et.setHint("Enter the product name");

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, PRODUCTS);
        et.setAdapter(arrayAdapter);


        final int currentLine = nbLines - 1;

        et.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LinearLayout line = findViewById(currentLine);
                AutoCompleteTextView et = (AutoCompleteTextView) line.getChildAt(0);

                Product prod = MatchesDisplay.getProduct(et.getText().toString(), language, myDbHelper);
                products.add(prod);
                line.removeViewAt(0);
                TextView textView = new TextView(MainMenu.this);
                textView.setText(prod.getName());
                line.addView(textView, 0);
                ImageView img = new ImageView(MainMenu.this);
                img.setImageDrawable(ResourcesCompat.getDrawable(getResources(), prod.getPicture_id(), null));
                LinearLayout.LayoutParams params;// = new LinearLayout.LayoutParams(width/4, height/4);
                //img.setLayoutParams(params);
                line.addView(img, 0);
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                line.setLayoutParams(params);
            }
        });

        line.addView(et);
        line.addView(bt);
        et.requestFocus();
    }

    private void searchMatches() {
        Intent intent = new Intent(this, MatchesDisplay.class);
        Boolean hasOnlyProducts = true;

        LinearLayout ll = (LinearLayout) findViewById(R.id.scrollVertical);

        for (int i = 0; i < nbLines; i++) {
            LinearLayout line = (LinearLayout) ll.getChildAt(0);
            if (line.getChildAt(0) instanceof AutoCompleteTextView) {
                AutoCompleteTextView tv = (AutoCompleteTextView) line.getChildAt(0);
            }
        }


        ArrayList<Integer> ids = new ArrayList<>();
        for (Product prod : products) {
            ids.add(prod.getId());
        }
        intent.putExtra("productIds", ids);
        intent.putExtra("language", language);
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
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_DRAWABLE_ID, R.drawable.index);

            db.insert(DatabaseContract.ProductsTable.TABLE_NAME, null, values);

            values = new ContentValues();
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_FR, "chocolat blanc");
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_EN, "white chocolate");
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_TASTE_ID, 1);
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_DRAWABLE_ID, R.drawable.index);

            db.insert(DatabaseContract.ProductsTable.TABLE_NAME, null, values);

            values = new ContentValues();
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_FR, "menthe");
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_EN, "mint");
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_TASTE_ID, 2);
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_DRAWABLE_ID, R.drawable.index);

            db.insert(DatabaseContract.ProductsTable.TABLE_NAME, null, values);
            values = new ContentValues();
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_FR, "crème fouettée");
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_EN, "whipped cream");
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_TASTE_ID, 1);
            values.put(DatabaseContract.ProductsTable.COLUMN_NAME_DRAWABLE_ID, R.drawable.index);

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

    private void getScreenDimensions() {
        Display disp = getWindowManager().getDefaultDisplay();
        width = disp.getWidth();
        height = disp.getHeight();
    }
}
