package com.example.pierre.topchef.activities;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pierre.topchef.R;
import com.example.pierre.topchef.database.DatabaseContract;
import com.example.pierre.topchef.database.DatabaseHelper;
import com.example.pierre.topchef.metier.Product;

import java.util.ArrayList;

public class MatchesDisplay extends AppCompatActivity {

    private DatabaseHelper myDbHelper;
    private ArrayList<Product> productsIn;
    private ArrayList<Product> productsMatch;
    private ArrayList<Product> productsNoMatch;
    int width, height;
    String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_display);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productsIn = new ArrayList<Product>();
        productsMatch = new ArrayList<Product>();
        productsNoMatch = new ArrayList<Product>();
        myDbHelper = new DatabaseHelper(this);

        getScreenDimensions();
        fillProducts();
        displayGoodAssociations();

    }

    private void fillProducts() {
        /*
        Fill productsMatch with a list of products that work well with the products supplied to the activity
         */

        language = this.getIntent().getStringExtra("language");
        ArrayList<Integer> ids = this.getIntent().getIntegerArrayListExtra("productIds");

        for (Integer id : ids) {
            Product prod = getProduct(id);
            productsIn.add(prod);
        }

        for (Product product : productsIn) {
            System.out.println("on va rechercher les associations d'un produit");
            ArrayList<Product> goodAssociations = getGoodAssociations(product);
            for (Product producttmp : goodAssociations) {
                productsMatch.add(producttmp);
                System.out.println("bonne association ajoutée à productsMatch");
            }

        }

        System.out.println("productsMatch");
        for (Product product : productsMatch) {
            System.out.println(product.toString());
        }

        for (Product product : productsIn) {
            System.out.println("on va rechercher les mauvaises associations du produit " + product.toString());
            ArrayList<Product> badAssociations = getBadAssociations(product);
            System.out.println("On a récupéré " + badAssociations.size() + " bad associations");
            for (Product producttmp : badAssociations) {
                productsNoMatch.add(producttmp);
                System.out.println("On veut enlever de productsMatch : " + producttmp.toString());
                if (productsMatch.contains(producttmp)) {
                    productsMatch.remove(producttmp);
                }
            }
        }

        System.out.println("productsMatch");
        for (Product product : productsMatch) {
            System.out.println(product.toString());
        }



    }

    private Product getProduct(String name) {

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = null;
        if (language.equals("FRENCH")) {
            cursor = db.rawQuery(
                    "select products."+DatabaseContract.ProductsTable._ID + " from products where products."+DatabaseContract.ProductsTable.COLUMN_NAME_NAME_FR+" = '" +name+ "';"
                    , null);
        }
        else if (language.equals("ENGLISH")){
            cursor = db.rawQuery(
                    "select products."+DatabaseContract.ProductsTable._ID + " from products where products."+DatabaseContract.ProductsTable.COLUMN_NAME_NAME_EN+" = '" +name+ "';"
                    , null);
        }

        int idPin=-1;
        while (cursor.moveToNext()) {
            idPin = cursor.getInt(
                    cursor.getColumnIndexOrThrow(DatabaseContract.ProductsTable._ID)
            );
        }

        cursor.close();
        db.close();
        return new Product(idPin, name);
    }

    private Product getProduct(int id) {

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = null;
        if (language.equals("FRENCH")) {
            cursor = db.rawQuery(
                    "select products."+DatabaseContract.ProductsTable._ID + ", products."+DatabaseContract.ProductsTable.COLUMN_NAME_NAME_FR+" from products where products."+DatabaseContract.ProductsTable._ID+" = " +id+ ";"
                    , null);
        }
        else if (language.equals("ENGLISH")){
            cursor = db.rawQuery(
                    "select products."+DatabaseContract.ProductsTable._ID + ", products."+DatabaseContract.ProductsTable.COLUMN_NAME_NAME_EN+" from products where products."+DatabaseContract.ProductsTable._ID+" = '" +id+ "';"
                    , null);
        }

        int idPin=-1;
        String name="";
        while (cursor.moveToNext()) {
            idPin = cursor.getInt(
                    cursor.getColumnIndexOrThrow(DatabaseContract.ProductsTable._ID)
            );
            if (language.equals("FRENCH")) {
                name = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_FR)
                );
            } else if (language.equals("ENGLISH")){
                name = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_EN)
                );
            }
        }

        cursor.close();
        db.close();
        return new Product(idPin, name);
    }

    public static Product getProduct(String name, String language, DatabaseHelper myDbHelper) {

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = null;
        if (language.equals("FRENCH")) {
            cursor = db.rawQuery(
                    "select products."+DatabaseContract.ProductsTable._ID + " from products where products."+DatabaseContract.ProductsTable.COLUMN_NAME_NAME_FR+" = '" +name+ "';"
                    , null);
        }
        else if (language.equals("ENGLISH")){
            cursor = db.rawQuery(
                    "select products."+DatabaseContract.ProductsTable._ID + " from products where products."+DatabaseContract.ProductsTable.COLUMN_NAME_NAME_EN+" = '" +name+ "';"
                    , null);
        }

        int idPin=-1;
        while (cursor.moveToNext()) {
            idPin = cursor.getInt(
                    cursor.getColumnIndexOrThrow(DatabaseContract.ProductsTable._ID)
            );
        }

        cursor.close();
        db.close();
        return new Product(idPin, name);
    }

    private ArrayList<Product> getGoodAssociations(Product pin) {

        SQLiteDatabase db = myDbHelper.getReadableDatabase();

        Cursor cursor1 = db.rawQuery(
                "select products._id, products.name_fr " +
                        "from products inner join good_associations on products._id = good_associations.product_id_2 " +
                        "where good_associations.product_id_1 = " + pin.getId() + ";"
                , null);

        ArrayList<Product> ret = new ArrayList<>();
        while (cursor1.moveToNext()) {
            int id = cursor1.getInt(
                    cursor1.getColumnIndexOrThrow(DatabaseContract.ProductsTable._ID)
            );
            String name="";
            if (language.equals("FRENCH")) {
                name = cursor1.getString(
                        cursor1.getColumnIndexOrThrow(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_FR)
                );
            }
            else  if (language.equals("ENGLISH")) {
                name = cursor1.getString(
                        cursor1.getColumnIndexOrThrow(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_EN)
                );
            }
            String taste = "";
            if (language.equals("FRENCH")) {
                Cursor cursor2 = db.rawQuery(
                        "select tastes." + DatabaseContract.TasteTable.COLUMN_NAME_TASTE_NAME_FR + " " +
                                "from products inner join tastes on products.taste_id = tastes._id " +
                                "where products._id = " + id + ";"
                        , null);
                while (cursor2.moveToNext()) {
                    taste = cursor2.getString(
                            cursor2.getColumnIndexOrThrow(DatabaseContract.TasteTable.COLUMN_NAME_TASTE_NAME_FR)
                    );
                }
                cursor2.close();
            } else if (language.equals("ENGLISH")) {
                Cursor cursor2 = db.rawQuery(
                        "select tastes." + DatabaseContract.TasteTable.COLUMN_NAME_TASTE_NAME_EN + " " +
                                "from products inner join tastes on products.taste_id = tastes._id " +
                                "where products._id = " + id + ";"
                        , null);
                while (cursor2.moveToNext()) {
                    taste = cursor2.getString(
                            cursor2.getColumnIndexOrThrow(DatabaseContract.TasteTable.COLUMN_NAME_TASTE_NAME_EN)
                    );
                }
                cursor2.close();
            }
            ret.add(new Product(id, name, taste));
        }

        cursor1.close();
        db.close();
        return ret;
    }

    private ArrayList<Product> getBadAssociations(Product pin) {

        SQLiteDatabase db = myDbHelper.getReadableDatabase();

        Cursor cursor1 = db.rawQuery(
                "select products._id, products.name_fr " +
                        "from products inner join bad_associations on products._id = bad_associations.product_id_2 " +
                        "where bad_associations.product_id_1 = " + pin.getId() + ";"
                , null);

        System.out.println("Nb de mauvaises associations trouvées " + cursor1.getCount());

        ArrayList<Product> ret = new ArrayList<>();
        while (cursor1.moveToNext()) {
            int id = cursor1.getInt(
                    cursor1.getColumnIndexOrThrow(DatabaseContract.ProductsTable._ID)
            );
            String name="";
            if (language.equals("FRENCH")) {
                name = cursor1.getString(
                        cursor1.getColumnIndexOrThrow(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_FR)
                );
            }
            else  if (language.equals("ENGLISH")) {
                name = cursor1.getString(
                        cursor1.getColumnIndexOrThrow(DatabaseContract.ProductsTable.COLUMN_NAME_NAME_EN)
                );
            }
            String taste = "";
            if (language.equals("FRENCH")) {
                Cursor cursor2 = db.rawQuery(
                        "select tastes." + DatabaseContract.TasteTable.COLUMN_NAME_TASTE_NAME_FR + " " +
                                "from products inner join tastes on products.taste_id = tastes._id " +
                                "where products._id = " + id + ";"
                        , null);
                while (cursor2.moveToNext()) {
                    taste = cursor2.getString(
                            cursor2.getColumnIndexOrThrow(DatabaseContract.TasteTable.COLUMN_NAME_TASTE_NAME_FR)
                    );
                }
                cursor2.close();
            } else if (language.equals("ENGLISH")) {
                Cursor cursor2 = db.rawQuery(
                        "select tastes." + DatabaseContract.TasteTable.COLUMN_NAME_TASTE_NAME_EN + " " +
                                "from products inner join tastes on products.taste_id = tastes._id " +
                                "where products._id = " + id + ";"
                        , null);
                while (cursor2.moveToNext()) {
                    taste = cursor2.getString(
                            cursor2.getColumnIndexOrThrow(DatabaseContract.TasteTable.COLUMN_NAME_TASTE_NAME_EN)
                    );
                }
                cursor2.close();
            }
            ret.add(new Product(id, name, taste));
        }

        cursor1.close();
        db.close();
        return ret;
    }

    private void displayGoodAssociations() {
        clearDisplay();
        NestedScrollView nsv = findViewById(R.id.nsv);
        LinearLayout verticalLayout = (LinearLayout) nsv.getChildAt(0);
        if (productsMatch.size() == 0) {
            TextView text = new TextView(this);
            text.setText("No match found");
            verticalLayout.addView(text);
        } else {
            for (Product prod : productsMatch) {

                ImageView img = new ImageView(this);
                LinearLayout.LayoutParams params;
                switch (prod.getId()) {
                    case 1 :
                        img.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.index, null));
                        params = new LinearLayout.LayoutParams(width/3,height/6);
                        img.setLayoutParams(params);
                        break;
                    case 2 :
                        img.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.index, null));
                        params = new LinearLayout.LayoutParams(width/3,height/6);
                        img.setLayoutParams(params);
                        break;
                    case 3 :
                        img.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.index, null));
                        params = new LinearLayout.LayoutParams(width/3,height/6);
                        img.setLayoutParams(params);
                        break;
                    case 4 :
                        img.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.index, null));
                        params = new LinearLayout.LayoutParams(width/3,height/6);
                        img.setLayoutParams(params);
                        break;
                    default :
                        img.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.index, null));
                        params = new LinearLayout.LayoutParams(width/3,height/6);
                        img.setLayoutParams(params);
                        break;
                }
                TextView matchName = new TextView(this);
                matchName.setText(prod.getName());
                TextView matchTaste = new TextView(this);
                matchTaste.setText(prod.getTaste());
                LinearLayout horizontalLayout = new LinearLayout(this);
                horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                horizontalLayout.addView(img);
                horizontalLayout.addView(matchName);
                horizontalLayout.addView(matchTaste);
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                horizontalLayout.setLayoutParams(params);
                verticalLayout.addView(horizontalLayout);
            }
        }

        if (productsNoMatch.size() > 0) {
            // We want a way to display the bad associations

            Button toBadAssoc = new Button(this);
            toBadAssoc.setText("Display bad associations");
            toBadAssoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    displayBadAssociations();
                }
            });
            verticalLayout.addView(toBadAssoc);
        }


    }

    private void displayBadAssociations() {
        clearDisplay();
        NestedScrollView nsv = findViewById(R.id.nsv);
        LinearLayout verticalLayout = (LinearLayout) nsv.getChildAt(0);

        for (Product product : productsNoMatch) {
            ImageView img = new ImageView(this);
            LinearLayout.LayoutParams params;
            switch (product.getId()) {
                case 1 :
                    img.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.index, null));
                    params = new LinearLayout.LayoutParams(width/3,height/6);
                    img.setLayoutParams(params);
                    break;
                case 2 :
                    img.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.index, null));
                    params = new LinearLayout.LayoutParams(width/3,height/6);
                    img.setLayoutParams(params);
                    break;
                case 3 :
                    img.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.index, null));
                    params = new LinearLayout.LayoutParams(width/3,height/6);
                    img.setLayoutParams(params);
                    break;
                case 4 :
                    img.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.index, null));
                    params = new LinearLayout.LayoutParams(width/3,height/6);
                    img.setLayoutParams(params);
                    break;
                default :
                    img.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.index, null));
                    params = new LinearLayout.LayoutParams(width/3,height/6);
                    img.setLayoutParams(params);
                    break;
            }
            TextView matchName = new TextView(this);
            matchName.setText(product.getName());
            TextView matchTaste = new TextView(this);
            matchTaste.setText(product.getTaste());
            LinearLayout horizontalLayout = new LinearLayout(this);
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            horizontalLayout.addView(img);
            horizontalLayout.addView(matchName);
            horizontalLayout.addView(matchTaste);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            horizontalLayout.setLayoutParams(params);
            verticalLayout.addView(horizontalLayout);
        }

        Button toGoodAssoc = new Button(this);
        toGoodAssoc.setText("Display good associations");
        toGoodAssoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayGoodAssociations();
            }
        });
        verticalLayout.addView(toGoodAssoc);

    }

    private void clearDisplay() {
        NestedScrollView nsv = findViewById(R.id.nsv);
        nsv.removeAllViews();
        LinearLayout verticalLayout = new LinearLayout(this);
        verticalLayout.setOrientation(LinearLayout.VERTICAL);
        nsv.addView(verticalLayout);
    }

    private void getScreenDimensions() {
        Display disp = getWindowManager().getDefaultDisplay();
        width = disp.getWidth();
        height = disp.getHeight();
    }
}
