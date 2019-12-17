package com.example.projet_android;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.zip.Inflater;

import static com.example.projet_android.Base_de_donnee.TAG;
import static com.example.projet_android.Fragment_reponse.mtts;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_bas.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_bas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_bas extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    MyContentProvider moncontentprovider ;
    Cursor cursor ;

    Button button_valider ;

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public Button getButton_valider() {
        return button_valider;
    }

    public void setButton_valider(Button button_valider) {
        this.button_valider = button_valider;
    }

    public Button getButton_effacer() {
        return button_effacer;
    }

    public void setButton_effacer(Button button_effacer) {
        this.button_effacer = button_effacer;
    }

    public static Button getTrad() {
        return trad;
    }

    public static void setTrad(Button trad) {
        Fragment_bas.trad = trad;
    }

    public TextView getEd() {
        return ed;
    }

    public void setEd(TextView ed) {
        this.ed = ed;
    }

    Button button_effacer ;
    static Button trad ;
    TextView ed ;

    Button del_cat ;
    Button del_langue ;
    Button del_trad ;

    private OnFragmentInteractionListener mListener;
    private View vue_du_fragment;



    public Fragment_bas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_bas.
     */
    /**
     *
     * ordre d'appel :
     *  OnCreate
     *  OnCreateView
     *  NewInstance
     *  OnCreate
     *  OnCreateView
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_bas newInstance(String param1, String param2) {
        Fragment_bas fragment = new Fragment_bas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        Log.d("d" , "New Instance : " + param1 + " " + param2); // les attributs graphiques on été créé

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        moncontentprovider = new MyContentProvider();
        Log.d("d" ,"onCreate : " + mParam1 + " " + mParam2);

    }

    static Cursor c ;
    static Cursor c_cat ;
    static Cursor res ;
    Button toute_trad ;
    static int bonne_rep = 0 ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vue_du_fragment = inflater.inflate(R.layout.fragment_fragment_bas, container, false);
        button_valider = vue_du_fragment.findViewById(R.id.button);
        button_effacer = vue_du_fragment.findViewById(R.id.button_effacer);
        trad = vue_du_fragment.findViewById(R.id.button_valider);
        ed = vue_du_fragment.findViewById(R.id.textView_reponse) ;

        del_cat = vue_du_fragment.findViewById(R.id.button_del_cat);
        del_langue = vue_du_fragment.findViewById(R.id.button_del_langue);
        del_trad = vue_du_fragment.findViewById(R.id.button_del_trad);
        toute_trad = vue_du_fragment.findViewById(R.id.valide_toute_trad);

        button_valider.setVisibility(View.INVISIBLE);
        button_effacer.setVisibility(View.INVISIBLE);
        del_langue.setVisibility(View.INVISIBLE);
        del_trad.setVisibility(View.INVISIBLE);
        del_cat.setVisibility(View.INVISIBLE);
        toute_trad.setVisibility(View.INVISIBLE);





        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /**
         * Verifie si les options choisies correpondents a une Init BDD :
         */
        if ( mParam1 != null && mParam1.equals("init") && mParam2.equals("bdd")){ // partie correpsondant a l'initailisation de la base de donnée

            /*
            On va masquer les autres fragments
            */

            MainActivity.layout_haut.setVisibility(View.INVISIBLE);
            MainActivity.layout_question.setVisibility(View.INVISIBLE);
            MainActivity.layout_bas.setVisibility(View.INVISIBLE);
            MainActivity.layout_reponse.setVisibility(View.INVISIBLE);


            button_valider.setVisibility(View.VISIBLE);
            button_effacer.setVisibility(View.VISIBLE);
            button_valider.setText("Initialisation de la Base de donnée");
            button_valider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.bdd = Base_de_donnee.getInstance(getContext());
                    Log.d("d" ,"BDD /  name : " + MainActivity.bdd.getDatabaseName() ); // creation Table OK
                    MainActivity.bdd.remplirBDD();

                }
            });
            button_effacer.setText("Cliquer ici pour effacer la BDD");
            button_effacer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.bdd.effacer_bdd();
                }
            });

            del_cat.setVisibility(View.VISIBLE);
            del_cat.setOnClickListener(new View.OnClickListener() {
                private SimpleCursorAdapter sca;

                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                    View view = getLayoutInflater().inflate(R.layout.layout_del_cat, null);


                    final Spinner sp= view.findViewById(R.id.spinner_del_cat);
                    Button button= view.findViewById(R.id.del_categ);

                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_CATGEORIE);
                    Uri uri = builder.build();
                    c = moncontentprovider.query(uri,
                            null
                            ,null,
                            null,
                            null);
                    //Log.d(TAG,"Dans L'acces au données : " + cursor.getColumnName(0 ) + " | " + cursor.getColumnName(1) ) ;
                    c.moveToFirst();
                    String[] fromColumns = new String[] {Base_de_donnee.NOM_CATGEORIE , Base_de_donnee.ID_CATEGORIE};
                    int[] toControlIDs = new int[] {android.R.id.text2};
                    sca = new SimpleCursorAdapter(getContext(), android.R.layout.simple_list_item_2 , c,
                            fromColumns,
                            toControlIDs);
                    sca.setDropDownViewResource(android.R.layout.simple_list_item_2);
                    sp.setAdapter(sca);

                    mBuilder.setView(view);
                    final AlertDialog dialog = mBuilder.create();

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG, "onClick: del cat , position :: " + sp.getSelectedItemPosition());
                            c.moveToFirst();
                            c.move(sp.getSelectedItemPosition()) ;
                            String nom_cat = c.getString(1) ;
                            Uri.Builder builder = new Uri.Builder();
                            builder.scheme("content").authority(Base_de_donnee.authority).appendPath("del_cat").appendPath(nom_cat) ;
                            Uri uri = builder.build();
                            Log.d(TAG, "onClick: Uri == " + uri);
                            long res = moncontentprovider.delete(uri,
                                    null,null
                            );
                            Toast.makeText(getActivity(),"Categorie supprimée ! res== " + res, Toast.LENGTH_LONG) ;


                            dialog.dismiss();

                        }
                    });

                    dialog.show();

                }
            });
            del_langue.setVisibility(View.VISIBLE);
            del_langue.setOnClickListener(new View.OnClickListener() {
                private SimpleCursorAdapter sca;

                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                    View view = getLayoutInflater().inflate(R.layout.layout_del_langue, null);


                    final Spinner sp= view.findViewById(R.id.spinner_del_langue);
                    Button button= view.findViewById(R.id.button_del_langue);

                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_LANGUE);
                    Uri uri = builder.build();
                    final Cursor c = moncontentprovider.query(uri,
                            null
                            ,null,
                            null,
                            null);
                    //Log.d(TAG,"Dans L'acces au données : " + cursor.getColumnName(0 ) + " | " + cursor.getColumnName(1) ) ;
                    c.moveToFirst();
                    String[] fromColumns = new String[] {Base_de_donnee.LANGUE_NOM };
                    int[] toControlIDs = new int[] {android.R.id.text2};
                    sca = new SimpleCursorAdapter(getContext(), android.R.layout.simple_list_item_2 , c,
                            fromColumns,
                            toControlIDs);
                    sca.setDropDownViewResource(android.R.layout.simple_list_item_2);
                    sp.setAdapter(sca);

                    mBuilder.setView(view);
                    final AlertDialog dialog = mBuilder.create();

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG, "onClick: del langue , position :: " + sp.getSelectedItemPosition());
                            String id_langue = String.valueOf(sp.getSelectedItemPosition()+1);
                            Uri.Builder builder = new Uri.Builder();
                            builder.scheme("content").authority(Base_de_donnee.authority).appendPath("del_langue").appendPath(id_langue) ;
                            Uri uri = builder.build();
                            Log.d(TAG, "onClick: Uri == " + uri);
                            long res = moncontentprovider.delete(uri,
                                    null,null
                            );
                            Toast.makeText(getActivity(),"Langue supprimée ! res== " + res, Toast.LENGTH_LONG) ;


                            dialog.dismiss();

                        }
                    });

                    dialog.show();

                }
            });

            // todo : del trad dynamique
            del_trad.setVisibility(View.VISIBLE);
            del_trad.setOnClickListener(new View.OnClickListener() {
                private SimpleCursorAdapter sca;
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                    View view = getLayoutInflater().inflate(R.layout.layout_del_trad, null);
                    final Spinner sp_langue_base = view.findViewById(R.id.spinner_langue_de_base);
                    final Spinner sp_langue_dst = view.findViewById(R.id.spinner_langue_dst);
                    final Spinner sp_categorie_select = view.findViewById(R.id.spinner_categorie_select);
                    final ListView lv = view.findViewById(R.id.liste_view_del_trad);

                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_LANGUE);
                    Uri uri = builder.build();
                    c = moncontentprovider.query(uri,
                            null
                            , null,
                            null,
                            null);
                    //Log.d(TAG,"Dans L'acces au données : " + cursor.getColumnName(0 ) + " | " + cursor.getColumnName(1) ) ;
                    c.moveToFirst();
                    String[] fromColumns = new String[]{Base_de_donnee.LANGUE_NOM};
                    int[] toControlIDs = new int[]{android.R.id.text2};
                    SimpleCursorAdapter sca = new SimpleCursorAdapter(getContext(), android.R.layout.simple_list_item_2, c,
                            fromColumns,
                            toControlIDs);
                    sca.setDropDownViewResource(android.R.layout.simple_list_item_2);

                    sp_langue_base.setAdapter(sca);
                    sp_langue_dst.setAdapter(sca);

                    builder = new Uri.Builder();
                    builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_CATGEORIE);
                    uri = builder.build();
                    c_cat = moncontentprovider.query(uri,
                            null
                            , null,
                            null,
                            null);
                    //Log.d(TAG,"Dans L'acces au données : " + cursor.getColumnName(0 ) + " | " + cursor.getColumnName(1) ) ;
                    c_cat.moveToFirst();
                    fromColumns = new String[]{Base_de_donnee.NOM_CATGEORIE};
                    toControlIDs = new int[]{android.R.id.text2};
                    sca = new SimpleCursorAdapter(getContext(), android.R.layout.simple_list_item_2, c_cat,
                            fromColumns,
                            toControlIDs);
                    sca.setDropDownViewResource(android.R.layout.simple_list_item_2);
                    sp_categorie_select.setAdapter(sca);

                    // todo : .......
                    /**
                     public static final String liste_trad_langueBase_categorie = Base_de_donnee.TABLE_TRAD +  langue1/langue2/catgeorie
                     **/

                    // recup de la langue seletionnée de base et de dst
                    AdapterView.OnItemSelectedListener event = new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if ( c == null || c.getCount() == 0 ) return;
                            int langue_de_base = sp_langue_base.getSelectedItemPosition();
                            c.moveToFirst();
                            c.move(langue_de_base);
                            langue_de_base = Integer.parseInt(c.getString(0));

                            // dst
                            int langue_dst = sp_langue_dst.getSelectedItemPosition();
                            c.moveToFirst();
                            c.move(langue_dst);
                            langue_dst = Integer.parseInt(c.getString(0));

                            // catgeorie
                            c_cat.moveToFirst();
                            c_cat.move(sp_categorie_select.getSelectedItemPosition());
                            String nom_cat = c_cat.getString(1);


                            Uri.Builder builder = new Uri.Builder();

                            builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_TRAD).appendPath(String.valueOf(langue_de_base)).appendPath(String.valueOf(langue_dst))
                                    .appendPath(nom_cat);
                            Uri uri = builder.build();
                            res = moncontentprovider.query(uri,
                                    null
                                    , null,
                                    null,
                                    null);
                            res.moveToFirst();
                            String[] fromColumns = new String[]{"mot_question", "mot_reponse"};
                            int[] toControlIDs = new int[]{R.id.mot_question, R.id.mot_reponse};
                            SimpleCursorAdapter sca_res = new SimpleCursorAdapter(getContext(), R.layout.layout_liste_mot_apprentissage_categorie, res,
                                    fromColumns,
                                    toControlIDs);
                            lv.setAdapter(sca_res);

                            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                    res.moveToFirst();
                                    Log.d(TAG, "onItemLongClick: position dans la liste =" + position);
                                    res.move(position);
                                    Log.d(TAG, "onItemLongClick: res :: " + res.getString(1));


                                    Uri.Builder builder = new Uri.Builder();
                                    builder.scheme("content").authority(Base_de_donnee.authority).appendPath("del_trad").appendPath(res.getString(1))
                                    .appendPath(res.getString(2)).appendPath(res.getString(3)).appendPath(res.getString(4));
                                    Uri uri = builder.build();
                                    Log.d(TAG, "onClick: Uri == " + uri);
                                    long resu = moncontentprovider.delete(uri,
                                            null,null
                                    );
                                    Log.d(TAG, "onItemLongClick: res ::::::: " +resu);
                                    lv.getChildAt(position).setBackgroundColor(Color.RED);

                                    return false;
                                }
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }

                    };

                    sp_langue_base.setOnItemSelectedListener(event);
                    sp_langue_dst.setOnItemSelectedListener(event);
                    sp_categorie_select.setOnItemSelectedListener(event);



                    mBuilder.setView(view);
                    final AlertDialog dialog = mBuilder.create();

                    dialog.show();



                }
            });

        }

        if ( mParam1 != null && mParam1.equals("validetrad") && mParam2.equals("liste")   ){


            MainActivity.layout_bas.setVisibility(View.VISIBLE);
            button_valider.setVisibility(View.INVISIBLE);
            button_effacer.setVisibility(View.INVISIBLE);
            toute_trad.setVisibility(View.VISIBLE);
            MainActivity.layout_bas.setVisibility(View.VISIBLE);
            Log.d(TAG, "onCreateView: Dans le cas valide toutes les trad");

            toute_trad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Cursor c = Fragment_apprentissage_liste.cursor ;
                    c.moveToFirst();
                    bonne_rep = 0 ;

                    ListView lv = Fragment_apprentissage_liste.ma_liste ;

                    for (int i = 0; i < lv.getCount() ; i++) {
                        TextView langue = lv.getChildAt(i).findViewById(R.id.langue) ;
                        TextView mot_base = lv.getChildAt(i).findViewById(R.id.mot_base) ;
                        EditText editext_trad = lv.getChildAt(i).findViewById(R.id.editText_trad) ;


                        Log.d(TAG, "onClick: 1 ; " + c.getString(1));
                        Log.d(TAG, "onClick: 2 ; " + c.getString(2));
                        Log.d(TAG, "onClick: 3 ; " + c.getString(3));
                        Log.d(TAG, "onClick: 4 ; " + c.getString(4));
                        Log.d(TAG, "onClick: mot ; " + mot_base.getText().toString());
                        Log.d(TAG, "onClick: trad ; " + editext_trad.getText().toString());


                        if ( mot_base.getText().toString().equals(c.getString(2)) && editext_trad.getText().toString().equals(c.getString(3))){
                            lv.getChildAt(i).setBackgroundColor(Color.GREEN);
                            bonne_rep += 1 ;

                        }
                        else {
                            lv.getChildAt(i).setBackgroundColor(Color.RED);
                        }
                        if ( !c.moveToNext() ) break ;


                    }

                    if ( bonne_rep == lv.getCount()){
                        Toast.makeText(getContext(),"Toutes les réponses sont correctes !",Toast.LENGTH_LONG);
                    }




                }
            });

        }
        if ( mParam1 != null && mParam1.equals("dictee") ){


            MainActivity.layout_bas.setVisibility(View.VISIBLE);

                button_valider.setVisibility(View.INVISIBLE);

                button_effacer.setVisibility(View.INVISIBLE);
                String mot = MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_MOT_QUESTION) ;
                final String rep = mParam2.toString() ;

                final View view = getLayoutInflater().inflate(R.layout.fragment_fragment_dictee, null);


                trad.setVisibility(View.VISIBLE);
                trad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d(TAG, "onCreateView: Nous somme bien dans fragment bas " + Fragment_dictee.rep.getText().toString() + " " + mParam2);
                    if ( Fragment_dictee.rep.getText().toString().equals(mParam2)){
                        Fragment_dictee.rep.setBackgroundColor(Color.GREEN);

                        Fragment_dictee.suivant.callOnClick();
                    }
                    else {
                        Fragment_dictee.rep.setBackgroundColor(Color.RED);
                        Toast.makeText(getContext(),"Réponse incorect , ré-ecouté le fichier Audio ( ou passer a la question suivante ) !", Toast.LENGTH_LONG) ;
                    }
                }

            });





        }


        if ( mParam1 != null && mParam1.equals("valide") && mParam2.equals("traduction")){ // partie correpsondant a l'initailisation de la base de donnée


            MainActivity.layout_bas.setVisibility(View.VISIBLE);
            Log.d("d" ,"Fragment BAS : valide - trad" ); // creation Table OK

            button_valider.setVisibility(View.INVISIBLE);

            button_effacer.setVisibility(View.INVISIBLE);

            trad.setVisibility(View.VISIBLE);

            trad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Log.d("d" ,"Fragment BAS : Onclick" );

                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_MOT).appendPath(MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_MOT_QUESTION));
                    Uri uri = builder.build();
                    Log.d("d" ,"Fragment BAS : URI " + uri );
                    cursor = moncontentprovider.query(uri,
                            null
                            ,null,
                            null,
                            null);

                    if ( cursor != null && cursor.getCount() != 0) {
                        Log.d(Base_de_donnee.TAG, "Dans Fragment_bas , on a la trad : : " + cursor.getColumnName(0) + " | " + cursor.getColumnName(1) + " | " + cursor.getColumnName(2));
                        Log.d("d", "Fragment BAS : cursor  " + cursor.getCount());
                        Log.d("d", "BundleMotChoisi  " + MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_MOT_QUESTION));
                        cursor.moveToFirst();
                        if (!cursor.getString(2).equals(MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_MOT_QUESTION))) {
                            Log.d("d", "fragment bas , if " + cursor.getCount());
                            String reponse_propose = Fragment_reponse.editText_reponse.getText().toString();
                            if (reponse_propose.toLowerCase().equals(cursor.getString(2).toLowerCase())) {
                                /**
                                 * Reponse correct :)
                                 */

                                trad.setBackgroundColor(Color.GREEN);
                                MainActivity.bundle_de_la_session_en_cours.putInt(MainActivity.BUNDLE_SCORE,MainActivity.bundle_de_la_session_en_cours.getInt(MainActivity.BUNDLE_SCORE)+1);
                                Toast.makeText(getContext(), "Bravo , bonne réponse , question suiavnte !", Toast.LENGTH_LONG).show();
                                // Maj du score
                                Fragment_menu.score.setText("Score : " + MainActivity.bundle_de_la_session_en_cours.getInt(MainActivity.BUNDLE_SCORE));
                                Button passer = Fragment_question.vue_du_frag.findViewById(R.id.button_passer_question) ;
                                if ( passer == null ) Log.d("d", "Rip le boutton passer ....");
                                passer.performClick();
                            } else {
                                /**
                                 * incorrect :s
                                 */
                                trad.setBackgroundColor(Color.RED);
                            }
                        } else {
                            Log.d("d", "fragment bag , else ." + cursor.getCount());
                            String reponse_propose = Fragment_reponse.editText_reponse.getText().toString();
                            if (reponse_propose.toLowerCase().equals(cursor.getString(1).toLowerCase())) {
                                /**
                                 * Reponse correct :)
                                 */
                                trad.setBackgroundColor(Color.GREEN);
                                MainActivity.bundle_de_la_session_en_cours.putInt(MainActivity.BUNDLE_SCORE,MainActivity.bundle_de_la_session_en_cours.getInt(MainActivity.BUNDLE_SCORE)+1);
                                // on incremente le score
                                Fragment_menu.score.setText("Score : " + MainActivity.bundle_de_la_session_en_cours.getInt(MainActivity.BUNDLE_SCORE));
                                Toast.makeText(getContext(), "Bravo , bonne réponse , question suiavnte !", Toast.LENGTH_LONG).show();

                                Button passer = Fragment_question.vue_du_frag.findViewById(R.id.button_passer_question) ;
                                if ( passer == null ) Log.d("d", "Rip le boutton passer ....");
                                passer.performClick();
                            } else {
                                /**
                                 * incorrect :s
                                 */
                                trad.setBackgroundColor(Color.RED);
                            }

                        }

                    }

                }
            });




        }


        return vue_du_fragment;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;


        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if ( mParam1 != null && mParam1 == "valide" && mParam2 == "traduction"){
            MainActivity.fm.popBackStack();
        }
        if ( mParam1 != null && mParam1.equals("validetrad") && mParam2.equals("liste") ){


            MainActivity.fm.popBackStack();

            MainActivity.fm.popBackStack();
            MainActivity.layout_question.setVisibility(View.GONE);
            MainActivity.layout_demarrage.setVisibility(View.VISIBLE);
            MainActivity.layout_reponse.setVisibility(View.GONE);
        }
        if ( mParam1 != null && mParam1.equals("dictee") ) {
            MainActivity.fm.popBackStack();
            MainActivity.fm.popBackStack();
            MainActivity.layout_question.setVisibility(View.GONE);
            MainActivity.layout_demarrage.setVisibility(View.VISIBLE);
            MainActivity.layout_reponse.setVisibility(View.GONE);
        }
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
