package com.example.projet_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

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

    private ListView ma_listeview_liste_mot;
    private TextView titre_liste;
    private WebView webview_image;


    static Cursor cursor;

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

        if (mParam1 != null & mParam1.equals("affiche") && mParam2.equals("liste")) {
            // dans ce cas là on affiche la liste personnelle

            MainActivity.layout_question.setVisibility(View.INVISIBLE);
            MainActivity.layout_reponse.setVisibility(View.INVISIBLE);

            titre_liste = vue_du_fragment.findViewById(R.id.textView_tite_liste);
            titre_liste.setText(R.string.titre_liste);
            ma_listeview_liste_mot = vue_du_fragment.findViewById(R.id.listeview_mot_perso);

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
            String[] fromColumns = new String[]{"langue_nom", "mot", "trad"};
            int[] toControlIDs = new int[]{R.id.langue_nom, R.id.mot, R.id.trad};
            SimpleCursorAdapter sca;
            sca = new SimpleCursorAdapter(getContext(), R.layout.layout_liste_view_liste, cursor,
                    fromColumns,
                    toControlIDs);

            ma_listeview_liste_mot.setAdapter(sca);

            ma_listeview_liste_mot.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    cursor.moveToFirst();
                    cursor.move(position);


                    Log.d(TAG, "onItemLongClick: position " + position);
                    Log.d(TAG, "onItemLongClick: cursor :" + cursor.getCount());

                    ArrayList<String> notre_liste_a_effacer;
                    notre_liste_a_effacer = new ArrayList<>();
                    notre_liste_a_effacer.add(cursor.getString(1));
                    notre_liste_a_effacer.add(cursor.getString(2));
                    notre_liste_a_effacer.add(cursor.getString(3));

                    Log.d(TAG, "onItemLongClick: " + notre_liste_a_effacer.toString());

                    MainActivity.liste_de_mot.remove(notre_liste_a_effacer);

                    MyContentProvider myContentProvider_delete = new MyContentProvider();
                    Uri.Builder builder_liste_delete = new Uri.Builder();
                    builder_liste_delete.scheme("content").authority(Base_de_donnee.authority).appendPath("liste").appendPath(cursor.getString(1)).appendPath(cursor.getString(2));
                    Uri uri_liste_delete = builder_liste_delete.build();
                    myContentProvider_delete.delete(uri_liste_delete,
                            null
                            , null);
                    ma_listeview_liste_mot.getChildAt(position).setBackgroundColor(Color.RED);

                    return false;
                }
            });

            ma_listeview_liste_mot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final ImageView image = new ImageView(getContext());
                    //FileInputStream f =  new FileInputStream(String.format("%s/downloadfile-6.jpg", getContext().getFilesDir().toString())) ;
                    BufferedInputStream buf = null;
                    Log.d(TAG, "coucou");
                    try {
                        TextView tv = view.findViewById(R.id.mot);
                        String mot = tv.getText().toString();
                        Log.d(TAG, "onItemClick: mot == " + mot);
                        FileInputStream in = new FileInputStream(Environment.getExternalStorageDirectory() + "/Images/" + mot + ".jpg");
                        buf = new BufferedInputStream(in);
                        Bitmap bitmap = BitmapFactory.decodeStream(buf);
                        image.setImageBitmap(bitmap);
                        new AlertDialog.Builder(getContext())
                                .setTitle("Image associée :")
                                .setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        /**
                                         *      https://www.google.com/search?hl=fr&biw=1870&bih=919&tbm=isch&sxsrf=ACYBGNQevJnnHnyN_4qXYAPXdH1p4cqxIg%3A1574949813744&sa=1&ei=tdPfXbGLLYTnxgOQh6vwDw&q=fraise&oq=fraise&gs_l=img.3..0i67l6j0j0i67j0l2.1461.2033..2141...2.0..0.68.257.5......0....1..gws-wiz-img.....10..35i362i39j35i39.Q2ovIvlBHrk&ved=0ahUKEwjxub-hiY3mAhWEs3EKHZDDCv4Q4dUDCAY&uact=5#imgrc=gzJYbbw5cVkXPM:
                                         */
                                        dialog.dismiss();
                                    }
                                }).setView(image).setIcon(R.drawable.voiture).show();

                    } catch (FileNotFoundException e) {
                        Log.d(TAG, "fichier image non trouvé ...");
                        e.printStackTrace();
                        new AlertDialog.Builder(getContext())
                                .setTitle("Image associée : Aucune image trouvée ...")
                                .setPositiveButton("Aller dans la gestion de BDD pour ajouter une image", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                    }
                                }).show();

                    }


                }


            });
        }


        return vue_du_fragment;
    }

                    // TODO: Rename method, update argument and hook method into UI event
            public void onButtonPressed (Uri uri){
                if (mListener != null) {
                    mListener.onFragmentInteraction(uri);
                }
            }

            @Override
            public void onAttach (Context context){
                super.onAttach(context);
                if (context instanceof OnFragmentInteractionListener) {
                    mListener = (OnFragmentInteractionListener) context;
                } else {
                    throw new RuntimeException(context.toString()
                            + " must implement OnFragmentInteractionListener");
                }
            }

            @Override
            public void onDetach () {
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
