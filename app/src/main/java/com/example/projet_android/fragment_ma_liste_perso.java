package com.example.projet_android;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;

import static com.example.projet_android.Base_de_donnee.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_ma_liste_perso.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_ma_liste_perso#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_ma_liste_perso extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View vue_du_fragment;

    private ListView ma_listeview_liste_mot ;


    public fragment_ma_liste_perso() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_ma_liste_perso.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_ma_liste_perso newInstance(String param1, String param2) {
        fragment_ma_liste_perso fragment = new fragment_ma_liste_perso();
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
        ma_listeview_liste_mot = new ListView(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vue_du_fragment = inflater.inflate(R.layout.fragment_fragment_ma_liste_perso, container, false);

        if ( mParam1 != null & mParam1.equals("affiche") && mParam2.equals("liste")){
            // dans ce cas là on affiche la liste personnelle
            ma_listeview_liste_mot = vue_du_fragment.findViewById(R.id.listeview_mot_perso);

            Cursor cursor ;
            MyContentProvider myContentProvider = new MyContentProvider();
            Uri.Builder builder_liste = new Uri.Builder();
            builder_liste.scheme("content").authority(Base_de_donnee.authority).appendPath("liste");
            Uri uri_liste = builder_liste.build();
            cursor = myContentProvider.query(uri_liste,
                    null
                    ,null,
                    null,
                    null);

            cursor.moveToFirst();
            String[] fromColumns = new String[] {"langue_nom","mot","trad"};
            int[] toControlIDs = new int[] {R.id.langue_nom , R.id.mot, R.id.trad };
            SimpleCursorAdapter sca ;
            sca = new SimpleCursorAdapter(getContext(), R.layout.layout_liste_view_liste , cursor,
                    fromColumns,
                    toControlIDs);

            ma_listeview_liste_mot.setAdapter(sca);

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
