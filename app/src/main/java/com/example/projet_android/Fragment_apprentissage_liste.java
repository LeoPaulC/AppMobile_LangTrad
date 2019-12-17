package com.example.projet_android;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;

import static com.example.projet_android.Base_de_donnee.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_apprentissage_liste.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_apprentissage_liste#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_apprentissage_liste extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View vue_du_fragment;
    private View vue_layout;

    public Fragment_apprentissage_liste() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_apprentissage_liste.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_apprentissage_liste newInstance(String param1, String param2) {
        Fragment_apprentissage_liste fragment = new Fragment_apprentissage_liste();
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
    }

    TextView mot ;
    EditText trad ;
    Button audio ;
    static ListView ma_liste ;
    static Cursor cursor ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vue_du_fragment = inflater.inflate(R.layout.fragment_fragment_apprentissage_liste, container, false);
        vue_layout = inflater.inflate(R.layout.layout_quizz_liste,container,false);

        if ( mParam1 != null && mParam1.equals("affiche") && mParam2.equals("liste")){

            MainActivity.layout_question.setMinimumHeight(600);
            MainActivity.layout_reponse.setMinimumHeight(0);

            ma_liste = vue_du_fragment.findViewById(R.id.listeview_liste);
            mot = vue_layout.findViewById(R.id.mot_base);
            trad = vue_layout.findViewById(R.id.editText_trad);
            TextView masque = vue_layout.findViewById(R.id.trad_masque);
            Button audiob = vue_layout.findViewById(R.id.audio);

            MyContentProvider myContentProvider = new MyContentProvider();
            Uri.Builder builder_liste = new Uri.Builder();
            builder_liste.scheme("content").authority(Base_de_donnee.authority).appendPath("liste");
            Uri uri_liste = builder_liste.build();
            cursor = myContentProvider.query(uri_liste,
                    null
                    , null,
                    null,
                    null);

            cursor.moveToFirst();
            String[] fromColumns = new String[]{ "mot","langue_nom","trad"};
            int[] toControlIDs = new int[]{R.id.mot_base,R.id.langue,R.id.trad_masque};
            SimpleCursorAdapter sca;
            sca = new SimpleCursorAdapter(getContext(), R.layout.layout_quizz_liste, cursor,
                    fromColumns,
                    toControlIDs);

            ma_liste.setAdapter(sca);



            
            MainActivity.ChargeFragmentDansEmplacement_Bas(Fragment_bas.newInstance("validetrad","liste"),"valide_liste");

        }
        return vue_du_fragment ;
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
        MainActivity.layout_question.setMinimumHeight(340);
        MainActivity.layout_reponse.setMinimumHeight(120);
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
