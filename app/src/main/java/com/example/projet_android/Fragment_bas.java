package com.example.projet_android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    Button button_valider ;
    Button button_effacer ;

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
        Log.d("d" ,"onCreate : " + mParam1 + " " + mParam2);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vue_du_fragment = inflater.inflate(R.layout.fragment_fragment_bas, container, false);
        button_valider = vue_du_fragment.findViewById(R.id.button);
        button_effacer = vue_du_fragment.findViewById(R.id.button_effacer);


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
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
