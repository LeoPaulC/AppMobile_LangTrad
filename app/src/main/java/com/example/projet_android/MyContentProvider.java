package com.example.projet_android;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static com.example.projet_android.Base_de_donnee.TAG;

public class MyContentProvider extends ContentProvider {

    public static final String authority = Base_de_donnee.authority;

    public static final String nom_langue_dapres_id = Base_de_donnee.ID_LANGUE + "/*";
    public static final String recherche_trad_dapres_mot = Base_de_donnee.TABLE_MOT + "/*";
    public static final String liste_mot_langue_Base_catgeorie = Base_de_donnee.TABLE_MOT + "/*/*";
    // Uri = mot/langue/catgeorie
    public static final String liste_trad_langueBase_categorie = Base_de_donnee.TABLE_TRAD + "/*/*";
    // REtourne les couples , (mot,traduction) en accord avec la langue et catgeroie selectionné

    public static final String Ajout_liste_perso_to_bdd = "liste";
    public static final String Delete_liste_perso_to_bdd = "liste/*/*";

    public static final String Maj_img_interne = "img_int/*" ;
                                            // maj img interne / le mot concerné  ( le lien sera dans le content values )
    public static final String Maj_img_ext= "img_ext/*" ;


    private UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    {
        matcher.addURI(authority, Base_de_donnee.CATEGORIE, 1); // pour recuperer toutes les categories de dispo .
        matcher.addURI(authority, Base_de_donnee.TABLE_LANGUE, 2); // pour recuperer toutes les categories de dispo .
        matcher.addURI(authority, Base_de_donnee.TABLE_MOT, 3); // pour recuperer toutes les mot de dispo d'apres une langue et une categorie.
        matcher.addURI(authority, recherche_trad_dapres_mot, 4); // pour recuperer toutes les categories de dispo .
        matcher.addURI(authority, liste_mot_langue_Base_catgeorie, 5);
        matcher.addURI(authority, nom_langue_dapres_id, 7);
        matcher.addURI(authority, Ajout_liste_perso_to_bdd, 10);
        matcher.addURI(authority, Delete_liste_perso_to_bdd, 11);
        matcher.addURI(authority, Maj_img_interne, 12);
        matcher.addURI(authority, Maj_img_ext, 13);

    }

    public MyContentProvider() {
    }


    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = MainActivity.bdd.getReadableDatabase();
        Log.d(TAG, "MyContentProvider : Query ");
        int code = matcher.match(uri);
        System.out.println("CODE : " + code + "Uri : " + uri.toString());

        Log.d(TAG, "MyContentProvider : CODE : " + code + " || Uri : " + uri.toString());
        Cursor cursor = null;
        switch (code) {
            case 1:
                Log.d(TAG, "MyContentProvider : " + '\n' + "Cas RECHERCHE DE CATEGRORIE : ");
                cursor = db.rawQuery("select *,rowid as _id from " + Base_de_donnee.CATEGORIE, null);
                Log.d(TAG, "Nom des Colonnes trouvées : " + cursor.getColumnName(0) + " | " + cursor.getColumnName(1));
                break;
            case 2:
                Log.d(TAG, "MyContentProvider : " + '\n' + "Cas RECHERCHE DE Langues : ");
                cursor = db.rawQuery("select *,rowid as _id from " + Base_de_donnee.TABLE_LANGUE, null);
                Log.d(TAG, "Nom des Colonnes trouvées : " + cursor.getColumnName(0) + " | " + cursor.getColumnName(1));
                break;
            case 3:
                Log.d(TAG, "MyContentProvider : " + '\n' + "Cas RECHERCHE DE Mot d'apres une langue et une catgeorie : ");
                cursor = db.rawQuery("select *,rowid as _id from " + Base_de_donnee.TABLE_MOT + " where " + Base_de_donnee.CATEGORIE + " = ? and " + Base_de_donnee.ID_LANGUE + " = ? or " + Base_de_donnee.ID_LANGUE + " = ? ", new String[]{MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_CATEGORIE), MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE1), MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE2)});

                Log.d(TAG, "Nom des Colonnes trouvées : " + cursor.getColumnName(0) + " | " + cursor.getColumnName(1) + " | " + cursor.getColumnName(2) + " | " + cursor.getColumnName(3));
                break;
            case 4:
                Log.d(TAG, "MyContentProvider : " + '\n' + "Cas RECHERCHE DE Trad d'apres un mot : ");

                // TODO : pour nos test //
                if ( MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE1) == null ){
                    MainActivity.bundle_de_la_session_en_cours.putString(MainActivity.BUNDLE_LANGUE1,"1");
                }
                if ( MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE2) == null){
                    MainActivity.bundle_de_la_session_en_cours.putString(MainActivity.BUNDLE_LANGUE2,"2");
                }
                // TODO fin test
                String le_mot_en_cours;
                le_mot_en_cours = uri.getPathSegments().get(uri.getPathSegments().size() - 1);

                Log.d(TAG, "MyContentProvider : " + '\n' + "Cas 4 :  " + uri + " ;; " + le_mot_en_cours + " ;; " + MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE2));
                //Log.d(Base_de_donnee.TAG , "MyContentProvider : "+ '\n' +"Cas 4 :  select *,rowid as _id from "+Base_de_donnee.TABLE_TRAD + " where mot_question = ? and "+ Base_de_donnee.TABLE_LANGUE + "2 = ? " );
                //cursor = db.rawQuery("select *,rowid as _id from "+Base_de_donnee.TABLE_TRAD /*+ " where mot_question = ? and "+ Base_de_donnee.TABLE_LANGUE + "2 = ? */, new String[]{le_mot_en_cours, MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE2)});
                //cursor = db.rawQuery("select *,rowid as _id from "+Base_de_donnee.TABLE_TRAD , null );
                // recuperer le pays choisie
                cursor = db.rawQuery("select " + Base_de_donnee.LANGUE_NOM + " , rowid as _id from " + Base_de_donnee.TABLE_LANGUE + " where " + Base_de_donnee.ID_LANGUE + " = ? ", new String[]{MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE2)});
                cursor.moveToFirst();
                String pays_choisi = cursor.getString(0);
                // recuperer le pays de base
                cursor = db.rawQuery("select " + Base_de_donnee.LANGUE_NOM + " , rowid as _id from " + Base_de_donnee.TABLE_LANGUE + " where " + Base_de_donnee.ID_LANGUE + " = ? ", new String[]{MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE1)});
                cursor.moveToFirst();
                String pays_base = cursor.getString(0);
                Log.d("d", "!! !!! !! ! ! !! ! !   1 ) " + pays_choisi);

                cursor = db.rawQuery("select *,rowid as _id from " + Base_de_donnee.TABLE_TRAD + " Where mot_question = ? and langue2 = ? and langue1 = ? ", new String[]{le_mot_en_cours, pays_choisi, pays_base});
                /**
                 * Dasn le cas ou la premiere trad a pas matché , on regarde la traduction dans l'autre sens .
                 */

                Log.d("d", "!! !!! !! ! ! !! ! !    " + cursor.getCount() + pays_base + pays_choisi);
                if (cursor.getCount() == 0) {
                    cursor = db.rawQuery("select *,rowid as _id from " + Base_de_donnee.TABLE_TRAD + " Where mot_reponse= ? and langue2 = ? and langue1 = ? ", new String[]{le_mot_en_cours, pays_choisi, pays_base});
                    Log.d("d", "!! !!! !! ! ! !! ! !  2 ) :  " + cursor.getCount() + pays_base + pays_choisi);
                }
                if (cursor.getCount() == 0) {
                    cursor = db.rawQuery("select *,rowid as _id from " + Base_de_donnee.TABLE_TRAD + " Where mot_reponse= ? and langue1 = ? and langue2 = ? ", new String[]{le_mot_en_cours, pays_choisi, pays_base});
                    Log.d("d", "!! !!! !! ! ! !! ! !  3 ) :  " + cursor.getCount() + pays_base + pays_choisi);
                }
                if (cursor.getCount() == 0) {
                    cursor = db.rawQuery("select *,rowid as _id from " + Base_de_donnee.TABLE_TRAD + " Where mot_question= ? and langue1 = ? and langue2 = ? ", new String[]{le_mot_en_cours, pays_choisi, pays_base});
                    Log.d("d", "!! !!! !! ! ! !! ! !  4 ) :  " + cursor.getCount() + pays_base + pays_choisi);
                }
                /*for (int i = 0; i < cursor.getColumnCount(); i++) {

                    Log.d("d", "Liste colonne de Trad , cas 4 : " + cursor.getColumnNames()[i]);
                }

                 */
                //Log.d(Base_de_donnee.TAG , "RES : " + cursor.toString());
                break;

            case 5:
                Log.d(TAG, "MyContentProvider : " + '\n' + "Cas 5 recherche mot , d'apres categorie et langue de base   : ");
                String categorie;
                String langue_de_bas;
                categorie = uri.getPathSegments().get(uri.getPathSegments().size() - 1);
                langue_de_bas = uri.getPathSegments().get(uri.getPathSegments().size() - 2);
                Log.d(TAG, "Fin du Cas 5 recherche mot :" + categorie.toString());

                Log.d(TAG, "Fin du Cas 5 recherche mot :" + langue_de_bas);


                /*cursor = db.rawQuery("select "+Base_de_donnee.ID_LANGUE+" , rowid as _id from "+Base_de_donnee.TABLE_LANGUE +" where " + Base_de_donnee.LANGUE_NOM +" = ? ", new String[]{langue_de_bas} );
                cursor.moveToFirst() ;
                int id_langue_de_base =  cursor.getInt(0) ;
                // on recupere l'id de la langue*/

                cursor = db.rawQuery("select *,rowid as _id from " + Base_de_donnee.TABLE_MOT + " where " + Base_de_donnee.CATEGORIE + " = ? and " + Base_de_donnee.ID_LANGUE + " = ? ", new String[]{categorie, langue_de_bas});
                Log.d(TAG, "MyContentProvider : " + '\n' + "Fin du Cas 5 recherche mot , d'apres categorie et langue de base   cursor.size() : " + cursor.getCount());

                break;
            case 6:
                Log.d(TAG, "MyContentProvider : " + '\n' + "Cas 6 recherche liste de mot , d'apres categorie et langue de base : ");
                String categorie2;
                String langue_de_base2;
                categorie2 = uri.getPathSegments().get(uri.getPathSegments().size() - 1);
                langue_de_base2 = uri.getPathSegments().get(uri.getPathSegments().size() - 2);

                break;
            case 7:
                Log.d(TAG, "query: cas 7 ");
                String id_langue;
                id_langue = uri.getPathSegments().get(uri.getPathSegments().size() - 1);
                cursor = db.rawQuery("select " + Base_de_donnee.LANGUE_NOM + " , rowid as _id from " + Base_de_donnee.TABLE_LANGUE + " where " + Base_de_donnee.ID_LANGUE + " = ? ", new String[]{id_langue});
                break;
            case 10:
                Log.d(TAG, "query: cas 10 ");
                cursor = db.rawQuery("select * , rowid as _id from " + Base_de_donnee.TABLE_LISTE, null);
                break;
            case 11:
                String mot;
                mot = uri.getPathSegments().get(uri.getPathSegments().size() - 1);
                String nom_langue;
                nom_langue = uri.getPathSegments().get(uri.getPathSegments().size() - 2);

                cursor= db.rawQuery("Select * , rowid as _id from " + Base_de_donnee.TABLE_LISTE + " where langue_nom = ? AND mot = ?", new String[]{nom_langue, mot});
                Log.d(TAG, "delete check : " + cursor.getCount());
                break;

            case 12 :
                String Mot;
                Mot = uri.getPathSegments().get(uri.getPathSegments().size() - 1);

                Uri.Builder uri_tmp = new Uri.Builder();
                uri_tmp.scheme("content").authority(authority).appendPath(Base_de_donnee.TABLE_MOT).appendPath(Mot) ;
                Uri urii = uri_tmp.build() ;
                cursor = this.query(urii,null,null,null,null) ;
                cursor.moveToFirst();
                Log.d(TAG, "query: lien interne == " + cursor.getString(5) );
                Log.d(TAG, "query: lien externe == " + cursor.getString(6) );
                break;
            case 13 :
                String Mott;
                Mott = uri.getPathSegments().get(uri.getPathSegments().size() - 1);

                Uri.Builder uri_Tmp = new Uri.Builder();
                uri_Tmp.scheme("content").authority(authority).appendPath(Base_de_donnee.TABLE_MOT).appendPath(Mott) ;
                Uri uri2 = uri_Tmp.build() ;
                cursor = this.query(uri2,null,null,null,null) ;
                cursor.moveToFirst();
                Log.d(TAG, "query: lien externe == " + cursor.getString(6) );
                break;



            default:
                Log.d("Query", "Not implemented yet");
                break;


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
        SQLiteDatabase db = MainActivity.bdd.getWritableDatabase();
        Log.d(TAG, "MyContentProvider : INSERT ");
        int code = matcher.match(uri);
        System.out.println("CODE : " + code + "Uri : " + uri.toString());

        Log.d(TAG, "MyContentProvider INSERT : CODE : " + code + " || Uri : " + uri.toString());
        Cursor cursor = null;
        switch (code) {
            case 10:
                long a = db.insertOrThrow(Base_de_donnee.TABLE_LISTE, null, values);
                Log.d(TAG, "insert: :: " + a);
                break;


            default:
                break;
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = MainActivity.bdd.getWritableDatabase();
        Log.d(TAG, "MyContentProvider : Delete ");
        int code = matcher.match(uri);
        System.out.println("CODE : " + code + "Uri : " + uri.toString());

        Log.d(TAG, "MyContentProvider Delete : CODE : " + code + " || Uri : " + uri.toString());
        Cursor cursor = null;
        switch (code) {
            case 11:
                String mot;
                mot = uri.getPathSegments().get(uri.getPathSegments().size() - 1);
                String nom_langue;
                nom_langue = uri.getPathSegments().get(uri.getPathSegments().size() - 2);

                long a = db.delete(Base_de_donnee.TABLE_LISTE, "langue_nom = ? AND mot = ?", new String[]{nom_langue, mot});
                Log.d(TAG, "delete check : " + a);
                break;
            case 12 :
                String mot2;
                mot2 = uri.getPathSegments().get(uri.getPathSegments().size() - 1);

                Uri.Builder uri_tmp = new Uri.Builder();
                uri_tmp.scheme("content").authority(authority).appendPath(Base_de_donnee.TABLE_MOT).appendPath(mot2) ;
                Uri urii = uri_tmp.build() ;
                Cursor tmp = this.query(urii,null,null,null,null) ;
                tmp.moveToFirst();
                long b = db.delete(Base_de_donnee.TABLE_TRAD, "img_interne = ? ", new String[]{tmp.getString(5)});
                Log.d(TAG, "delete check : " + b);
                break ;

            default:
                break;
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = MainActivity.bdd.getWritableDatabase();
        Log.d(TAG, "MyContentProvider : Update ");
        int code = matcher.match(uri);
        System.out.println("CODE : " + code + "Uri : " + uri.toString());

        Log.d(TAG, "MyContentProvider Update : CODE : " + code + " || Uri : " + uri.toString());
        Cursor cursor = null;
        switch (code) {
            case 12 :
                Uri.Builder uri_tmp = new Uri.Builder();

                if ( MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE1) == null ){
                    MainActivity.bundle_de_la_session_en_cours.putString(MainActivity.BUNDLE_LANGUE1,"1");
                }
                if ( MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE2) == null){
                    MainActivity.bundle_de_la_session_en_cours.putString(MainActivity.BUNDLE_LANGUE2,"2");
                }
                uri_tmp.scheme("content").authority(authority).appendPath(Base_de_donnee.TABLE_MOT).appendPath(values.getAsString("mot")) ;
                Uri urii = uri_tmp.build() ;
                Cursor tmp = this.query(urii,null,null,null,null) ;
                tmp.moveToFirst();
                for ( String s : tmp.getColumnNames())
                    Log.d(TAG, "update: " + s);
                Log.d(TAG, "update: " + values.getAsString("mot"));
                Log.d(TAG, "update: " + values.getAsString("img_interne"));

                ContentValues cv = new ContentValues();
                cv.put("img_interne" , values.getAsString("img_interne"));

                // on verifie d'abord si il existe un lien , sinon inutile de le supprimer ... :)
                if ( tmp.getString(5) != null ) {
                    File file = new File(tmp.getString(5));
                    //on regarde si il existe (test qui est amené a disparaitre, c’était pour être sur)
                    if (file.exists()) {
                        //System.out.println("Fichier trouvé");
                        Log.d(TAG, "update: fichier trouvé .. supprime le precedent ");
                    } else {
                        System.out.println("Fichier non trouvé");
                    }
                    //Suppression du fichier et exception si ce n'est pas le cas
                    if (file.delete() == false) {
                        try {
                            throw new IOException();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // on met à jour la ou les trad qui comporte le mot "mot" dans la question ou la réponse
                int res = db.update(Base_de_donnee.TABLE_TRAD,cv,"mot_question = ? or mot_reponse = ?" , new String[]{values.getAsString("mot"),values.getAsString("mot")});
                Log.d(TAG, "update interne : RES : " + res);
                break;
            case 13 :
                Uri.Builder uri_tmp2 = new Uri.Builder();

                if ( MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE1) == null ){
                    MainActivity.bundle_de_la_session_en_cours.putString(MainActivity.BUNDLE_LANGUE1,"1");
                }
                if ( MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE2) == null){
                    MainActivity.bundle_de_la_session_en_cours.putString(MainActivity.BUNDLE_LANGUE2,"2");
                }
                uri_tmp2.scheme("content").authority(authority).appendPath(Base_de_donnee.TABLE_MOT).appendPath(values.getAsString("mot")) ;
                Uri uri2 = uri_tmp2.build() ;
                Cursor tmp2 = this.query(uri2,null,null,null,null) ;
                tmp2.moveToFirst();
                for ( String s : tmp2.getColumnNames())
                    Log.d(TAG, "update: " + s);
                Log.d(TAG, "update: " + values.getAsString("mot"));
                Log.d(TAG, "update: " + values.getAsString("img_externe"));

                ContentValues cv2 = new ContentValues();
                cv2.put("img_externe" , values.getAsString("img_externe"));

                // on met à jour la ou les trad qui comporte le mot "mot" dans la question ou la réponse
                int res2 = db.update(Base_de_donnee.TABLE_TRAD,cv2,"mot_question = ? or mot_reponse = ?" , new String[]{values.getAsString("mot"),values.getAsString("mot")});
                Log.d(TAG, "update externe :  RES : " + res2);
                break;

            default:
                break;
        }
        return 0;
    }
}
