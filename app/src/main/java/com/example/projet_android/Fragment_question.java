package com.example.projet_android;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.projet_android.Base_de_donnee.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_question.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_question#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_question extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1; // on va se servir de ce paramettre pour stocker la question
    private String mParam2;

    MyContentProvider moncontentprovider ;

    CursorLoader mycursorLoad;

    public TextView getTexview_question() {
        return texview_question;
    }

    public void setTexview_question(TextView texview_question) {
        this.texview_question = texview_question;
    }

    public TextView getText_view_de_la_question() {
        return text_view_de_la_question;
    }

    public void setText_view_de_la_question(TextView text_view_de_la_question) {
        this.text_view_de_la_question = text_view_de_la_question;
    }

    public Button getButton_passer_la_question() {
        return button_passer_la_question;
    }

    public void setButton_passer_la_question(Button button_passer_la_question) {
        this.button_passer_la_question = button_passer_la_question;
    }

    public Button getValider() {
        return valider;
    }

    public void setValider(Button valider) {
        this.valider = valider;
    }

    static Cursor cursor ;
    static Cursor cursor2 ;

    TextView texview_question ;




    private OnFragmentInteractionListener mListener;

    TextView text_view_de_la_question ;
    Button button_passer_la_question ;
    Button valider ;
    Spinner spinner_de_choix_categorie;
    GridView grid_view_de_la_liste_de_mot ;
    static View vue_du_frag;
    SimpleCursorAdapter sca ;
    public static ListView mon_recycler_view ;

    public Fragment_question() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_question.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_question newInstance(String param1, String param2) {
        Fragment_question fragment = new Fragment_question();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /**
         * Attribut visuel de la classe :
         */
        vue_du_frag = inflater.inflate(R.layout.fragment_question, container, false);
        text_view_de_la_question = vue_du_frag.findViewById(R.id.Textview_question) ;
        text_view_de_la_question.setText(mParam1);
        valider = vue_du_frag.findViewById(R.id.button_valider) ;

        Log.d(TAG, "Fragment Question : OnCreateView .");


        /**
         * On affiche un mot au hasard parmis la liste de dispo pour la categorie  .
         */
        if ( mParam1 != null && mParam1 == "affiche" && mParam2 == Base_de_donnee.TABLE_MOT){

            String catgeorie_en_cours = MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_CATEGORIE) ;
            String langue_en_cours = MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE2) ;

            MainActivity.layout_reponse.setVisibility(View.VISIBLE);

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_MOT);
            Uri uri = builder.build();
            cursor = moncontentprovider.query(uri,
                    null
                    ,null,
                    null,
                    null);
            texview_question = vue_du_frag.findViewById(R.id.Textview_question);
            texview_question.setText("Traduire ce mot : ");
            Log.d(TAG,"Dans Fragment Question : " + cursor.getColumnName(0 ) + " | " + cursor.getColumnName(1) + " | " + cursor.getColumnName(2) + " | " + cursor.getColumnName(3)) ;

            cursor.moveToFirst();
            MainActivity.bundle_de_la_session_en_cours.putString(MainActivity.BUNDLE_MOT_QUESTION, cursor.getString(2));
            texview_question.setText("Traduire ce mot : " + cursor.getString(2));

            button_passer_la_question = vue_du_frag.findViewById(R.id.button_passer_question);
            button_passer_la_question.setVisibility(View.VISIBLE);
            button_passer_la_question.setText("Passer cette Question");
            button_passer_la_question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( cursor.moveToNext() ){
                        MainActivity.bundle_de_la_session_en_cours.putString(MainActivity.BUNDLE_MOT_QUESTION, cursor.getString(2));
                        texview_question.setText("Traduire ce mot : " + cursor.getString(2));
                        Fragment_bas.trad.setBackgroundColor(Color.rgb(255 , 235 , 59));
                        // 255 , 235 , 59

                    }
                }
            });

            MainActivity.ChargeFragmentDansEmplacement_Bas(Fragment_bas.newInstance("valide","traduction"),"valider choix");
            MainActivity.ChargeFragmentDansEmplacement_Reponse(Fragment_reponse.newInstance("affiche","reponse"),"traduction");



            Log.d(TAG, "Fragment Question: OnCreateView terminé , Question en place .");
        }
        if ( mParam1 != null && mParam1.equals("apprentissage") && mParam2.equals("mot a mot") ){
            /**
             * Dans le cas d'un apprentissage par categories et d'apres la langue choisie precedement dans le menu de demarrage
             */
            spinner_de_choix_categorie = vue_du_frag.findViewById(R.id.spinner_liste_categorie) ;
            grid_view_de_la_liste_de_mot = vue_du_frag.findViewById(R.id.gridview_liste_mot);

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_CATGEORIE);
            Uri uri = builder.build();
            cursor = moncontentprovider.query(uri,
                    null
                    ,null,
                    null,
                    null);

            Log.d(TAG,"Dans Fragment question apprentissage : " + cursor.getColumnName(0 ) + " | " + cursor.getColumnName(1) ) ;

            Log.d(TAG,"cursor count  !" + cursor.getCount());
            cursor.moveToFirst();
            String[] fromColumns = new String[] {Base_de_donnee.NOM_CATGEORIE };
            int[] toControlIDs = new int[] {android.R.id.text1};
            sca = new SimpleCursorAdapter(getContext(), android.R.layout.simple_list_item_1 , cursor,
                    fromColumns,
                    toControlIDs);
            spinner_de_choix_categorie.setAdapter(sca);

            spinner_de_choix_categorie.setVisibility(View.VISIBLE);
            spinner_de_choix_categorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(TAG,"Reussis !");
                    MainActivity.layout_reponse.setVisibility(View.VISIBLE);

                    Uri.Builder builder = new Uri.Builder();
                    cursor.moveToFirst();
                    cursor.move(position);
                    String categorie = cursor.getString(1);
                    //view.toString()
                    builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_MOT).appendPath(MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE1)).appendPath(categorie);
                    Uri uri = builder.build();
                    cursor2 = moncontentprovider.query(uri,
                            null
                            ,null,
                            null,
                            null);

                    Log.d(TAG,"Dans Fragment question apprentissage : " + cursor2.getColumnName(0 ) + " | " + cursor2.getColumnName(1) ) ;

                    cursor2.moveToFirst();
                    String[] fromColumns = new String[] {Base_de_donnee.CONTENU };
                    int[] toControlIDs = new int[] {android.R.id.text2};
                    sca = new SimpleCursorAdapter(getContext(), android.R.layout.simple_list_item_2 , cursor2,
                            fromColumns,
                            toControlIDs);
                    grid_view_de_la_liste_de_mot.setAdapter(sca);
                    Log.d("d","\n\n\n On colorie le mot : " + grid_view_de_la_liste_de_mot.getCount()) ;

                    grid_view_de_la_liste_de_mot.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            /**
                             * On restant appuyé , on va ajouté les mots a notre liste perso .
                             */
                            String langue ;
                            cursor2.moveToFirst();
                            cursor2.move(position);
                            String mot_selectionne_long = cursor2.getString(2) ;
                            Log.d("d","Vous etes resté appuyé lg sur " + mot_selectionne_long +" position = " + position) ;


                            /**
                             * Ajouter a la liste lors d'un clique long , et lors de l'initialisation , verifier les enfants de la liste et colorier ceux present .
                             * voir pour chaque chlds .
                             */

                            if ( !MainActivity.liste_de_mot.contains(mot_selectionne_long)){
                                Boolean add = MainActivity.liste_de_mot.add(mot_selectionne_long) ;
                                Log.d(TAG, "onItemLongClick: add = " + add);
                                grid_view_de_la_liste_de_mot.getChildAt(position).setBackgroundColor(Color.GREEN);
                            }
                            else {
                                Boolean remove = MainActivity.liste_de_mot.remove(mot_selectionne_long);
                                Log.d(TAG, "onItemLongClick: remove = " + remove);
                                grid_view_de_la_liste_de_mot.getChildAt(position).setBackgroundColor(Color.WHITE);
                            }

                            /**
                             * Implementer l'ajout dans notre liste perso , faire une page pour l'afficher aussi .
                             */
                            return false;
                        }
                    });
                    grid_view_de_la_liste_de_mot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            /**
                             * On ne peut recreer de fragment a chaque click .
                             */
                            cursor2.moveToFirst();
                            cursor2.move(position);
                            String mot_selectionne = cursor2.getString(2) ;
                            MainActivity.ChargeFragmentDansEmplacement_Reponse(Fragment_reponse.newInstance("trad",mot_selectionne),"trad");
                        }
                    });

                    grid_view_de_la_liste_de_mot.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            grid_view_de_la_liste_de_mot.removeOnLayoutChangeListener(this);
                            Log.d("d","Liste_completée , getcount " + grid_view_de_la_liste_de_mot.getCount() + " getchildcount() = " + grid_view_de_la_liste_de_mot.getChildCount()) ;
                            for (int i = 0; i < grid_view_de_la_liste_de_mot.getCount(); i++) {
                                cursor2.moveToFirst();
                                cursor2.move(i);
                                String mot_a_remplir = cursor2.getString(2) ;
                                if ( MainActivity.liste_de_mot.contains(mot_a_remplir)) {
                                    Log.d("d","\n\n\n On colorie le mot : " + mot_a_remplir + " i =  " + i) ;
                                    //grid_view_de_la_liste_de_mot.getChildAt(i).setBackgroundColor(Color.GREEN);
                                    //grid_view_de_la_liste_de_mot.getChildAt(i).setElevation(1);
                                    grid_view_de_la_liste_de_mot.getChildAt(i).setBackgroundColor(Color.GREEN);
                                    /**
                                     * Voir ça demain !
                                     */
                                }


                            }
                        }
                    });

                    grid_view_de_la_liste_de_mot.setVisibility(View.VISIBLE);


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });

            Maj_liste_user_avec_liste_affiche();







        }

        return vue_du_frag;

    }

    /**
     * Voir ça plus tard
     */

    public void Maj_liste_user_avec_liste_affiche(){

        Log.d("d","\n\n\n nb child: " + grid_view_de_la_liste_de_mot.getChildCount() + " , count()  =  " + grid_view_de_la_liste_de_mot.getCount()) ;
        for (int i = 0; i < grid_view_de_la_liste_de_mot.getCount(); i++) {
            cursor2.moveToFirst();
            cursor2.move(i);
            String mot_a_remplir = cursor2.getString(2) ;
            if ( MainActivity.liste_de_mot.contains(mot_a_remplir)) {
                Log.d("d","\n\n\n On colorie le mot : " + mot_a_remplir + " i =  " + i) ;
                //grid_view_de_la_liste_de_mot.getChildAt(i).setBackgroundColor(Color.GREEN);
                //grid_view_de_la_liste_de_mot.getChildAt(i).setElevation(1);
                grid_view_de_la_liste_de_mot.getChildAt(i).setBackgroundColor(Color.RED);
                /**
                 * Voir ça demain !
                 */
            }


        }
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
        if ( mParam1 != null && mParam1.equals("apprentissage") && mParam2.equals("mot a mot") ){
            MainActivity.layout_question.setVisibility(View.INVISIBLE);
            MainActivity.layout_demarrage.setVisibility(View.VISIBLE);
            MainActivity.layout_reponse.setVisibility(View.INVISIBLE);
        }
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
