package com.example.projet_android;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


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
    Button button_effacer ;
    Button trad ;
    TextView ed ;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vue_du_fragment = inflater.inflate(R.layout.fragment_fragment_bas, container, false);
        button_valider = vue_du_fragment.findViewById(R.id.button);
        button_effacer = vue_du_fragment.findViewById(R.id.button_effacer);
        trad = vue_du_fragment.findViewById(R.id.button_valider);

        ed = vue_du_fragment.findViewById(R.id.textView_reponse) ;


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /**
         * Verifie si les options choisies correpondents a une Init BDD :
         */
        if ( mParam1 != null && mParam1.equals("init") && mParam2.equals("bdd")){ // partie correpsondant a l'initailisation de la base de donnée
            button_valider.setText("Initialisation de la Base de donnée ( sans remplissage )");
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
        }

        if ( mParam1 != null && mParam1.equals("valide") && mParam2.equals("traduction")){ // partie correpsondant a l'initailisation de la base de donnée
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

                    Log.d(Base_de_donnee.TAG,"Dans Fragment_bas , on a la trad : : " + cursor.getColumnName(0 ) + " | " + cursor.getColumnName(1) + " | " + cursor.getColumnName(2) ) ;
                    Log.d("d" ,"Fragment BAS : cursor  " + cursor.getCount() );
                    Log.d("d" ,"BundleMotChoisi  " + MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_MOT_QUESTION) );
                    cursor.moveToFirst();
                    if (!cursor.getString(2).equals(MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_MOT_QUESTION))) {
                        Log.d("d" ,"fragment bas , if " + cursor.getCount() );
                        /*Toast.makeText(getContext(),
                                ">> : " + cursor.getString(2) ,
                                Toast.LENGTH_SHORT).show();

                         */

                        String reponse_propose = Fragment_reponse.editText_reponse.getText().toString() ;
                        if ( reponse_propose.toLowerCase().equals(cursor.getString(2).toLowerCase())){
                            /**
                             * Reponse correct :)
                             */
                            trad.setBackgroundColor(Color.GREEN);
                        }
                        else{
                            /**
                             * incorrect :s
                             */
                            trad.setBackgroundColor(Color.RED);
                        }
                    }
                    else {
                        Log.d("d" ,"fragment bag , else ." + cursor.getCount() );
                        /*Toast.makeText(getContext(),
                                ">> : " + cursor.getString(1) ,
                                Toast.LENGTH_SHORT).show();

                         */
                        EditText ed = vue_du_fragment.findViewById(R.id.textView_reponse) ;
                        String reponse_propose = ed.getText().toString() ;
                        if ( reponse_propose.toLowerCase().equals(cursor.getString(1).toLowerCase())){
                            /**
                             * Reponse correct :)
                             */
                            trad.setBackgroundColor(Color.GREEN);
                        }
                        else{
                            /**
                             * incorrect :s
                             */
                            trad.setBackgroundColor(Color.RED);
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
