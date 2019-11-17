package com.example.projet_android;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;

public class MyContentProvider extends ContentProvider {

    public static final String authority = Base_de_donnee.authority ;

    private UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    {
        matcher.addURI(authority,Base_de_donnee.CATEGORIE,1); // pour recuperer toutes les categories de dispo .

    }
    public MyContentProvider() {}


    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = MainActivity.bdd.getReadableDatabase();
        Log.d(Base_de_donnee.TAG , "MyContentProvider : Query ");
        int code = matcher.match(uri);
        System.out.println("CODE : " + code + "Uri : " + uri.toString());

        Log.d(Base_de_donnee.TAG , "MyContentProvider : CODE : " + code + " || Uri : " + uri.toString());
        Cursor cursor = null;
        switch (code) {
            case 1:
                Log.d(Base_de_donnee.TAG , "MyContentProvider : "+ '\n' +"Cas RECHERCHE DE CATEGRORIE : ");
                cursor = db.rawQuery("select *,rowid as _id from "+Base_de_donnee.CATEGORIE, null);
                Log.d(Base_de_donnee.TAG , "Nom des Colonnes trouvées : " + cursor.getColumnName(0) + " | " + cursor.getColumnName(1));
                break;
            default:
                Log.d("Query", "Not implemented yet");


        }
        //throw new UnsupportedOperationException("Not yet implemented");
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
