package com.example.projet_android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioFormat;
import android.net.Uri;
import android.net.rtp.AudioCodec;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import static com.example.projet_android.Base_de_donnee.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_reponse.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_reponse#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_reponse extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Cursor cursor;
    MyContentProvider moncontentprovider;
    static ProgressBar pb_exit;
    static int progress = 0;
    private static int compteur = 0;

    private OnFragmentInteractionListener mListener;
    private View vue_du_fragment;

    public static EditText getEditText_reponse() {
        return editText_reponse;
    }

    public static void setEditText_reponse(EditText editText_reponse) {
        Fragment_reponse.editText_reponse = editText_reponse;
    }

    static EditText editText_reponse;
    Button affiche_image;
    Button audio;
    static TextToSpeech mtts;

    public Fragment_reponse() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_reponse.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_reponse newInstance(String param1, String param2) {
        Fragment_reponse fragment = new Fragment_reponse();
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
        affiche_image = new Button(getContext());
        audio = new Button(getContext());
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        vue_du_fragment = inflater.inflate(R.layout.fragment_reponse, container, false);


        if (mParam1 != null && mParam1 == "affiche" && mParam2 == "reponse") {

            editText_reponse = ((EditText) vue_du_fragment.findViewById(R.id.textView_reponse));
            affiche_image = vue_du_fragment.findViewById(R.id.button_affiche_image);
            audio = vue_du_fragment.findViewById(R.id.audio);

            editText_reponse.setVisibility(View.VISIBLE);
            affiche_image.setVisibility(View.VISIBLE);
            audio.setVisibility(View.VISIBLE);

            mtts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int res = mtts.setLanguage(Locale.GERMAN);
                        if (res == TextToSpeech.LANG_MISSING_DATA) {
                            Log.d(TAG, "onInit: OUps , langue non prise en compte...");
                        }
                    }

                }
            });

            audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = Fragment_question.mot_en_cours;
                    mtts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
            });



            affiche_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ImageView image = new ImageView(getContext());
                    Uri.Builder builder = new Uri.Builder();
                    Log.d("d", "Fragment ma liste perso : mot == " + mParam2);
                    // on recherche le lien de l'image interne
                    builder.scheme("content").authority(Base_de_donnee.authority).appendPath("img_int").appendPath(Fragment_question.mot_en_cours);
                    Uri uri = builder.build();
                    Log.d("d", "Fragment ma liste perso  : URI " + uri);
                    MyContentProvider moncontentprovider = new MyContentProvider();
                    cursor = moncontentprovider.query(uri,
                            null
                            , null,
                            null,
                            null);
                    cursor.moveToFirst();


                    if (cursor != null) {
                        Log.d(TAG, "onItemClick: ma liste perso , lien == " + cursor.getString(5));
                        if (cursor.getString(5) != null && !cursor.getString(5).equals("vide")) {
                            try {
                                FileInputStream in = new FileInputStream(cursor.getString(5));
                                BufferedInputStream buf = new BufferedInputStream(in);
                                Bitmap bitmap = BitmapFactory.decodeStream(buf);
                                image.setImageBitmap(bitmap);
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Image associée ( interne ) :")
                                        .setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).setNegativeButton("Supprimer Image interne ?", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        File file = new File(cursor.getString(5));
                                        //on regarde si il existe (test qui est amené a disparaitre, c’était pour être sur)
                                        if (file.exists()) {
                                            Log.d(TAG, "onClick: Fichier trouvé");
                                            // on va supprimer la reference de la BDD en mettant à jour le champs img_interne à ""


                                            Uri.Builder uri_tmp = new Uri.Builder();
                                            uri_tmp.scheme("content").authority(Base_de_donnee.authority).appendPath("img_int").appendPath(Fragment_question.mot_en_cours);
                                            Uri urii = uri_tmp.build();
                                            MyContentProvider myContentProvider = new MyContentProvider();
                                            ContentValues cv = new ContentValues();

                                            cv.put("mot", Fragment_question.mot_en_cours);
                                            cv.put("img_interne", "vide");
                                            int tmp = myContentProvider.update(urii, cv, null, null);
                                        } else {
                                            Log.d(TAG, "onClick: Fichier non trouvé");
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
                                }).setView(image).show();
                                cursor.moveToFirst();

                            } catch (FileNotFoundException e) {
                                Log.d(TAG, "fichier image non trouvé ...");

                            }
                        } else {
                            Log.d(TAG, "onItemClick: Image interne non trouvé , on cherche le lien externe ");
                            if (cursor.getString(6) != null) {
                                Log.d(TAG, "onItemClick: on tente l'ouverture du lien ... " + cursor.getString(6));

                                WebView wv = new WebView(getContext());
                                wv.loadUrl(cursor.getString(6));
                                wv.setWebViewClient(new WebViewClient() {
                                    @Override
                                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                        view.loadUrl(url);
                                        return true;
                                    }
                                });
                                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                alert.setTitle("Image externe ... :");
                                alert.setView(wv);
                                alert.setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                                alert.show();

                            } else {
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Image associée : Aucune image trouvée ...")
                                        .setPositiveButton("Aller dans la gestion de BDD pour ajouter une image ( else )", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                            }
                                        }).show();
                            }
                        }

                    }

                }
            });

        }
        if (mParam1 != null && mParam1.equals("trad") && mParam2 != null) { // affichier al traduction d'apres un mot slectionné

            affiche_image = vue_du_fragment.findViewById(R.id.button_affiche_image);
            audio = vue_du_fragment.findViewById(R.id.audio);
            audio.setVisibility(View.VISIBLE);
            affiche_image.setVisibility(View.VISIBLE);
            editText_reponse = ((EditText) vue_du_fragment.findViewById(R.id.textView_reponse));
            editText_reponse.setEnabled(false);
            editText_reponse.setVisibility(View.VISIBLE);
            Uri.Builder builder = new Uri.Builder();
            Log.d("d", "Fragment Reponse : mot a traduire :" + mParam2);
            builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_MOT).appendPath(mParam2);
            Uri uri = builder.build();
            Log.d("d", "Fragment Reponse : URI " + uri);
            cursor = moncontentprovider.query(uri,
                    null
                    , null,
                    null,
                    null);
            cursor.moveToFirst();
            if (cursor != null) {
                if (cursor.getString(1).equals(mParam2)) {
                    // cas ou la traduction colle avec le mot choisi
                    editText_reponse.setText(cursor.getString(2));

                } else {
                    // le cas ou c'est la reponse de la trad qui match avec le mot selectionne
                    editText_reponse.setText(cursor.getString(1));
                }


                mtts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            int res = mtts.setLanguage(Locale.GERMAN);
                            if (res == TextToSpeech.LANG_MISSING_DATA) {
                                Log.d(TAG, "onInit: OUps , langue non prise en compte...");
                            }
                        }

                    }
                });

                audio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = editText_reponse.getText().toString();
                        mtts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    }
                });

                affiche_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ImageView image = new ImageView(getContext());
                        Uri.Builder builder = new Uri.Builder();
                        Log.d("d", "Fragment ma liste perso : mot == " + mParam2);
                        // on recherche le lien de l'image interne
                        builder.scheme("content").authority(Base_de_donnee.authority).appendPath("img_int").appendPath(mParam2);
                        Uri uri = builder.build();
                        Log.d("d", "Fragment ma liste perso  : URI " + uri);
                        MyContentProvider moncontentprovider = new MyContentProvider();
                        cursor = moncontentprovider.query(uri,
                                null
                                , null,
                                null,
                                null);
                        cursor.moveToFirst();


                        if (cursor != null) {
                            Log.d(TAG, "onItemClick: ma liste perso , lien == " + cursor.getString(5));
                            if (cursor.getString(5) != null && !cursor.getString(5).equals("vide")) {
                                try {
                                    FileInputStream in = new FileInputStream(cursor.getString(5));
                                    BufferedInputStream buf = new BufferedInputStream(in);
                                    Bitmap bitmap = BitmapFactory.decodeStream(buf);
                                    image.setImageBitmap(bitmap);
                                    new AlertDialog.Builder(getContext())
                                            .setTitle("Image associée ( interne ) :")
                                            .setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).setNegativeButton("Supprimer Image interne ?", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            File file = new File(cursor.getString(5));
                                            //on regarde si il existe (test qui est amené a disparaitre, c’était pour être sur)
                                            if (file.exists()) {
                                                Log.d(TAG, "onClick: Fichier trouvé");
                                                // on va supprimer la reference de la BDD en mettant à jour le champs img_interne à ""


                                                Uri.Builder uri_tmp = new Uri.Builder();
                                                uri_tmp.scheme("content").authority(Base_de_donnee.authority).appendPath("img_int").appendPath(mParam2);
                                                Uri urii = uri_tmp.build();
                                                MyContentProvider myContentProvider = new MyContentProvider();
                                                ContentValues cv = new ContentValues();

                                                cv.put("mot", mParam2);
                                                cv.put("img_interne", "vide");
                                                int tmp = myContentProvider.update(urii, cv, null, null);
                                            } else {
                                                Log.d(TAG, "onClick: Fichier non trouvé");
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
                                    }).setView(image).show();
                                    cursor.moveToFirst();

                                } catch (FileNotFoundException e) {
                                    Log.d(TAG, "fichier image non trouvé ...");

                                }
                            } else {
                                Log.d(TAG, "onItemClick: Image interne non trouvé , on cherche le lien externe ");
                                if (cursor.getString(6) != null) {
                                    Log.d(TAG, "onItemClick: on tente l'ouverture du lien ... " + cursor.getString(6));

                                    WebView wv = new WebView(getContext());
                                    wv.loadUrl(cursor.getString(6));
                                    wv.setWebViewClient(new WebViewClient() {
                                        @Override
                                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                            view.loadUrl(url);
                                            return true;
                                        }
                                    });
                                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                    alert.setTitle("Image externe ... :");
                                    alert.setView(wv);
                                    alert.setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alert.show();

                                } else {
                                    new AlertDialog.Builder(getContext())
                                            .setTitle("Image associée : Aucune image trouvée ...")
                                            .setPositiveButton("Aller dans la gestion de BDD pour ajouter une image ( else )", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                }
                            }

                        }

                    }
                });
            }


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
        if (mParam1 != null && mParam1 == "affiche" && mParam2 == "reponse") {
            MainActivity.fm.popBackStack();
        }
        if (mParam1 != null && mParam1 == "trad" && mParam2 != null) {
            for (Fragment a : MainActivity.fm.getFragments()) {
                // on retire de la liste tous les fragment créé pour la réponse
                if (a instanceof Fragment_reponse)
                    MainActivity.fm.popBackStack();
                MainActivity.layout_reponse.setVisibility(View.INVISIBLE);
            }
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
