package com.example.pierre.topchef.database;

import android.provider.BaseColumns;


public class DatabaseContract {
    private DatabaseContract(){

    }

    public static class ProductsTable implements BaseColumns{
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_NAME_TASTE_ID = "taste_id";
        public static final String COLUMN_NAME_NAME_FR = "name_fr";
        public static final String COLUMN_NAME_NAME_EN = "name_en";
        public static final String COLUMN_NAME_DRAWABLE_ID = "drawable_id";
    }

    public static class TasteTable implements BaseColumns {
        public static final String TABLE_NAME = "tastes";
        public static final String COLUMN_NAME_TASTE_NAME_FR = "taste_fr";
        public static final String COLUMN_NAME_TASTE_NAME_EN = "taste_en";
    }

    public static class GoodAssociationsTable implements BaseColumns {
        public static final String TABLE_NAME = "good_associations";
        public static final String COLUMN_NAME_PRODUCT_ID_1 = "product_id_1";
        public static final String COLUMN_NAME_PRODUCT_ID_2 = "product_id_2";
    }

    public static class BadAssociationsTable implements BaseColumns {
        public static final String TABLE_NAME = "bad_associations";
        public static final String COLUMN_NAME_PRODUCT_ID_1 = "product_id_1";
        public static final String COLUMN_NAME_PRODUCT_ID_2 = "product_id_2";
    }


}

