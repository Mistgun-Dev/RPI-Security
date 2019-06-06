package com.rpi_security;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Classe ManageCameraFragment : Classe permettant l'affichage des caméras enregistrés dans l'application
 * Enregistrement des caméras éfféctués par SharedPreference en stockant l'addresse IP, le Port et le libélé de la caméra
 */
public class ManageCameraFragment extends Fragment {

    View RootView;
    private ListView listViewCam;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        RootView = inflater.inflate(R.layout.fragment_manage_cam, container, false);
        listViewCam = RootView.findViewById(R.id.listViewCam);

        floatActionButton();
        loadSavedCamera();
        eventOnClickListView();

        return RootView;
    }

    void floatActionButton()
    {
        FloatingActionButton fab = RootView.findViewById(R.id.addNewCam);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCamFragment fragmentAddCam = new AddCamFragment();
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_container, fragmentAddCam)
                        .commit();
            }
        });
    }

    /**
     * Fonction permettant de charger les caméras enregistrés en mémoire, et les listé dans une ListView
     */
    void loadSavedCamera()
    {
        SharedPreference sharedPreference = new SharedPreference(getContext());

        int nbCam;
        nbCam = sharedPreference.getNbCameraSaved(); //Récupération du nombre de caméra stocké en mémoire

        //Création de la ArrayList qui nous permettra de remplir la listView
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
        //On déclare la HashMap qui contiendra les informations pour un item
        HashMap<String, String> map;
        //Création d'une HashMap pour insérer les informations du premier item de notre listView
        map = new HashMap<String, String>();

        //Si aucune caméra enregistré, on affiche un TextView et on return
        TextView textViewCam = RootView.findViewById(R.id.textViewCam);
        if(nbCam == 0)
        {
            textViewCam.setText("Aucune caméra enregistrée");
            textViewCam.setVisibility(View.VISIBLE);
            return;
        }
        else
            textViewCam.setVisibility(View.INVISIBLE);


        for(int camID=1 ; camID<=nbCam ; camID++)
        {
            String storedLabel = sharedPreference.getLabel(camID);
            String storedIP = sharedPreference.getIpAddress(camID);
            String storedPort = sharedPreference.getPort(camID);

            if(storedLabel.equals("null") || storedIP.equals("null") || storedPort.equals("null"))
            {
                continue;
            }
            else
            {
                map = new HashMap<String, String>();
                map.put("titre", storedLabel);
                map.put("description", storedIP+":"+storedPort);
                map.put("ip", storedIP);
                map.put("port", storedPort);
                map.put("img", String.valueOf(R.mipmap.outline_videocam_black_24));
                listItem.add(map);
            }
        }

        //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
        SimpleAdapter mSchedule = new SimpleAdapter (getActivity(), listItem, R.layout.display_item_listview,
                new String[] {"img", "titre", "description", "ip", "port"}, new int[] {R.id.imgItem, R.id.labelCam, R.id.descriptionCam});

        //On attribue à notre listView l'adapter que l'on vient de créer
        listViewCam.setAdapter(mSchedule);

    }

    /**
     * Fonction permettant la gestion des évenements de click sur les item de la listView (un click sur un item permet
     * la redirection vers le fragment de connexion de la caméra en question, et passant le port et l'addresse IP comme argument
     * au fragment cible
     */
    void eventOnClickListView()
    {
        //Enfin on met un écouteur d'évènement sur notre listView
        listViewCam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                //on récupère la HashMap contenant les infos de notre item (titre, description, img)
                HashMap<String, String> map = (HashMap<String, String>) listViewCam.getItemAtPosition(position);

                CameraFragment fragmentCamera = new CameraFragment();
                Bundle arguments = new Bundle();
                arguments.putString( "IP", map.get("ip"));
                arguments.putString( "PORT", map.get("port"));
                fragmentCamera.setArguments(arguments);
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_container, fragmentCamera)
                        .commit();
            }
        });
    }
}
