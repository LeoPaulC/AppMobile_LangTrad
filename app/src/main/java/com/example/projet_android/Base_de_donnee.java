package com.example.projet_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class Base_de_donnee extends SQLiteOpenHelper {

    public final static int VERSION = 6;
    public final static String DB_NAME = "Base de donnee Projet Mobile";
    public final static String TAG = "d" ;


    public final static String authority = "fr.android.projet";

    /**
     * Table MOT :
     */
    public final static String TABLE_MOT = "mot";
    public final static String ID_MOT = "id_mot";
    public final static String ID_LANGUE = "id_langue";
    public final static String CONTENU = "contenu";
    public final static String CATEGORIE = "categorie";
    public final static String ID_DICO = "id_dico";

    /**
     * Table DICTIONNAIRE :
     */
    public final static String TABLE_DICTIONNAIRE = "dictionnaire";
    // ---  ID Dico ----
    // ---  ID Langue ----

    /**
     * Table LANGUE :
     */
    public final static String TABLE_LANGUE = "langue";
    //public final static String ID_LANGUE = "id_mot";
    public final static String LANGUE_NOM = "langue_nom";
    public final static String LANGUE_PAYS = "langue_pays";

    /**
     * TABLE TRADUCTION
     */
    public final static String TABLE_TRAD = "traduction";
    public final static String ID_TRAD = "id_trad" ;
    /**
     * mot1 , langue 1 , langue 2  ,mot 2
     */

    /**
     * TABLE CATEGORIE
     */
    public final static String TABLE_CATGEORIE= "categorie";
    public final static String ID_CATEGORIE = "id_categorie";
    public final static String NOM_CATGEORIE = "nom_CATEGORIE";


    public final static String CREATE_CATEGORIE= "create table " + TABLE_CATGEORIE + "(" +
            ID_CATEGORIE + " integer primary key AUTOINCREMENT, " +
            NOM_CATGEORIE + " string );";



    public final static String CREATE_DICO = "create table " + TABLE_DICTIONNAIRE + "(" +
            ID_DICO + " integer primary key AUTOINCREMENT, " +
            ID_LANGUE + " integer, " +
            "FOREIGN KEY(" + ID_LANGUE + ") REFERENCES " + TABLE_LANGUE + "(" + ID_LANGUE + ") );"; // joindre ID_LANGUE de dico à Langue


    public final static String CREATE_MOT = "create table " + TABLE_MOT + "(" +
            ID_MOT + " integer primary key AUTOINCREMENT, " +
            ID_LANGUE + " integer, " +
            CONTENU + " string," +
            CATEGORIE + " string," +
            ID_DICO + " integer ," +
            "FOREIGN KEY(" + ID_LANGUE + ") REFERENCES " + TABLE_LANGUE + "(" + ID_LANGUE + ")," +
            "FOREIGN KEY(" + CATEGORIE + ") REFERENCES " + TABLE_CATGEORIE + "(" + NOM_CATGEORIE + ")," +
            "FOREIGN KEY(" + ID_DICO + ") REFERENCES " + TABLE_DICTIONNAIRE + "(" + ID_DICO + ")" + ");"; // joindre ID_LANGUE du mot à Langue et Id_dico

    public final static String CREATE_LANGUE= "create table " + TABLE_LANGUE + "(" +
            ID_LANGUE + " integer primary key AUTOINCREMENT, " +
            LANGUE_NOM + " string, " +
            LANGUE_PAYS + " string );"; // joindre ID_LANGUE de dico à Langue

    public final static String CREATE_TRAD= "create table " + TABLE_TRAD + "(" +
            ID_TRAD + " integer primary key AUTOINCREMENT, " +
            "mot_question String, " +
            "mot_reponse String, " +
            TABLE_LANGUE +"1 String , " +
            TABLE_LANGUE +"2 String , " +
            "FOREIGN KEY(mot_question) REFERENCES " + TABLE_MOT + "(" + CONTENU + ")," +
            "FOREIGN KEY(mot_reponse) REFERENCES " + TABLE_MOT + "(" + CONTENU + ")," +
            "FOREIGN KEY(" + TABLE_LANGUE + "1) REFERENCES " + TABLE_LANGUE + "(" + LANGUE_NOM + ")," +
            "FOREIGN KEY(" + TABLE_LANGUE + "2) REFERENCES " + TABLE_LANGUE + "(" + LANGUE_NOM + ")" + ");";




    /**
    public final static String CREATE_STAT = "create table " + TABLE_STAT + "(" +
            COLONNE_PAYS + " string references geo, " +
            COLONNE_ANNEE + " integer, " +
            COLONNE_POPULATION + " integer," +
            "primary key(" + COLONNE_PAYS + ", " + COLONNE_ANNEE + "));";

     */

    private static Base_de_donnee ourInstance;

    public static Base_de_donnee getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new Base_de_donnee(context);
        return ourInstance;
    }

    public Base_de_donnee(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CATEGORIE);
        db.execSQL(CREATE_LANGUE);
        db.execSQL(CREATE_DICO);
        db.execSQL(CREATE_MOT);
        db.execSQL(CREATE_TRAD);
        Log.d("d","Création base de données terminée.................................................................................................................................") ;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("drop table if exists " + TABLE_CATGEORIE);
            db.execSQL("drop table if exists " + TABLE_LANGUE);
            db.execSQL("drop table if exists " + TABLE_DICTIONNAIRE);
            db.execSQL("drop table if exists " + TABLE_MOT);
            db.execSQL("drop table if exists " + TABLE_TRAD);
            onCreate(db);
        }
    }


    public void remplirBDD(){

        /**
         * A changer pour remplir la BDD a l'initialisation
         */
        /*
        ContentValues row = new ContentValues();
        row.put(ID_LANGUE, "1");
        row.put(CONTENU, "Bonjour");
        row.put(CATEGORIE, "Mot Usuel");
        row.put(ID_DICO, "1" );
        SQLiteDatabase bd = this.getWritableDatabase();
        long res = bd.insertOrThrow(TABLE_MOT, null, row);
        Log.d("d","Res de l'insertion : " + res ) ;

        Log.d("d","Insert dans la BDD ... a verifier ") ;

        affiche_res_requette(new String []{"*"}, new String[]{TABLE_MOT},null);
        */
        SQLiteDatabase bd = this.getWritableDatabase();

        ContentValues row = new ContentValues();
        row.put(NOM_CATGEORIE, "Mot Usuel");
        long res = bd.insertOrThrow(TABLE_CATGEORIE, null, row);

        row = new ContentValues();
        row.put(NOM_CATGEORIE, "Nature");
        res = bd.insertOrThrow(TABLE_CATGEORIE, null, row);

        row = new ContentValues();
        row.put(NOM_CATGEORIE, "Vehicule");
        res = bd.insertOrThrow(TABLE_CATGEORIE, null, row);

        row = new ContentValues();
        row.put(NOM_CATGEORIE, "Elements");
        res = bd.insertOrThrow(TABLE_CATGEORIE, null, row);

        row = new ContentValues();
        row.put(NOM_CATGEORIE, "Social");
        res = bd.insertOrThrow(TABLE_CATGEORIE, null, row);

        row = new ContentValues();
        row.put(NOM_CATGEORIE, "Animaux");
        res = bd.insertOrThrow(TABLE_CATGEORIE, null, row);


        /**
         * Langue
         */
        row = new ContentValues();
        row.put(LANGUE_NOM, "Francais");
        res = bd.insertOrThrow(TABLE_LANGUE, null, row);
        row = new ContentValues();
        row.put(LANGUE_NOM, "Anglais");
        res = bd.insertOrThrow(TABLE_LANGUE, null, row);
        row = new ContentValues();
        row.put(LANGUE_NOM, "Espagnol");
        res = bd.insertOrThrow(TABLE_LANGUE, null, row);
        row = new ContentValues();
        row.put(LANGUE_NOM, "Allemands");
        res = bd.insertOrThrow(TABLE_LANGUE, null, row);

        /**
         * MOT
         */
        row = new ContentValues();
        row.put(CONTENU, "Bonjour");
        row.put(ID_LANGUE, 1 );
        row.put(CATEGORIE, "Mot Usuel" );
        res = bd.insertOrThrow(TABLE_MOT, null, row);

        row = new ContentValues();
        row.put(CONTENU, "Hello");
        row.put(ID_LANGUE, 2 );
        row.put(CATEGORIE, "Mot Usuel" );
        res = bd.insertOrThrow(TABLE_MOT, null, row);

        row = new ContentValues();
        row.put(CONTENU, "Salut");
        row.put(ID_LANGUE, 1 );
        row.put(CATEGORIE, "Mot Usuel" );
        res = bd.insertOrThrow(TABLE_MOT, null, row);

        row = new ContentValues();
        row.put(CONTENU, "Cacahuete");
        row.put(ID_LANGUE, 1 );
        row.put(CATEGORIE, "Mot Usuel" );
        res = bd.insertOrThrow(TABLE_MOT, null, row);
        row = new ContentValues();
        row.put(CONTENU, "Elephant");
        row.put(ID_LANGUE, 1 );
        row.put(CATEGORIE, "Animaux" );
        res = bd.insertOrThrow(TABLE_MOT, null, row);
        row = new ContentValues();
        row.put(CONTENU, "Elephant");
        row.put(ID_LANGUE, 1 );
        row.put(CATEGORIE, "Mot Usuel" );
        res = bd.insertOrThrow(TABLE_MOT, null, row);
        row = new ContentValues();
        row.put(CONTENU, "Pigeon");
        row.put(ID_LANGUE, 1 );
        row.put(CATEGORIE, "Animaux" );
        res = bd.insertOrThrow(TABLE_MOT, null, row);
        row = new ContentValues();
        row.put(CONTENU, "Stylo");
        row.put(ID_LANGUE, 1 );
        row.put(CATEGORIE, "Mot Usuel" );
        res = bd.insertOrThrow(TABLE_MOT, null, row);
        row = new ContentValues();
        row.put(CONTENU, "Crayon");
        row.put(ID_LANGUE, 1 );
        row.put(CATEGORIE, "Mot Usuel" );
        res = bd.insertOrThrow(TABLE_MOT, null, row);
        row = new ContentValues();
        row.put(CONTENU, "Ordinateur");
        row.put(ID_LANGUE, 1 );
        row.put(CATEGORIE, "Mot Usuel" );
        res = bd.insertOrThrow(TABLE_MOT, null, row);
        row = new ContentValues();
        row.put(CONTENU, "Master");
        row.put(ID_LANGUE, 1 );
        row.put(CATEGORIE, "Mot Usuel" );
        res = bd.insertOrThrow(TABLE_MOT, null, row);

        /**
         * TRADuction
         */
        row = new ContentValues();
        row.put(TABLE_LANGUE+"1", "Francais");
        row.put(TABLE_LANGUE+"2", "Anglais");
        row.put("mot_question", "Bonjour" );
        row.put("mot_reponse", "Hello" );
        res = bd.insertOrThrow(TABLE_TRAD, null, row);

        row = new ContentValues();
        row.put(TABLE_LANGUE+"1", "Francais");
        row.put(TABLE_LANGUE+"2", "Anglais");
        row.put("mot_question", "Ordinateur" );
        row.put("mot_reponse", "Computeur" );
        res = bd.insertOrThrow(TABLE_TRAD, null, row);


        affiche_res_requette(new String []{"*"}, new String[]{TABLE_LANGUE},null);

        }


    public Cursor affiche_res_requette( String[] colonne_du_Select , String[] colonne_du_From , String[] where_clause ){
        /**
         * a retirer car remplacer par un cursorLoader
         */
        SQLiteDatabase bd = this.getReadableDatabase();

        Cursor c = bd.query(colonne_du_From[0], colonne_du_Select , null, null, null,
                null, null);
        c.moveToFirst();
        //Log.d("d","BDD :: count : " + c.getColumnCount() ) ;
        do{
            //Log.d("d","BDD :: _nom_categorie : " +  c.getString(1));
            //Log.d("d","BDD :: _id_categorie: " +  c.getInt(0));
        } while (c.moveToNext());
        return c ;
    }


    public void effacer_bdd(){
        /**
         * A garder pour vider la BDD
         */
        SQLiteDatabase bd = this.getWritableDatabase();
        bd.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE name = '"+TABLE_MOT+"';");
        bd.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE name = '"+TABLE_CATGEORIE+"';");
        bd.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE name = '"+TABLE_DICTIONNAIRE+"';");
        bd.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE name = '"+TABLE_LANGUE+"';");
        bd.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE name = '"+TABLE_TRAD+"';");
        bd.delete(TABLE_MOT,null,null);
        bd.delete(TABLE_CATGEORIE,null,null);
        bd.delete(TABLE_DICTIONNAIRE,null,null);
        bd.delete(TABLE_LANGUE,null,null);
        bd.delete(TABLE_TRAD,null,null);
        Log.d(TAG, "effacer_bdd: fait . Remise à 0 de tous les auto increments.");
    }




    /**
     * Nous avons reussis a recuperer des informations de la base de données.
     */
}
