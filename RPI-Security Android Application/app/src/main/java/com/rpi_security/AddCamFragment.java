package com.rpi_security;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mohamed on 18/03/2019.
 *
 * Classe AddCamFragment : Classe permettant l'ajout d'une nouvelle caméra en entrant l'adresse IP et le PORT de la caméra.
 */

public class AddCamFragment extends Fragment
{
    Button buttonSave, buttonTest;
    TextView textViewIP, textViewPort, textViewLabel;
    View RootView;
    WebView webViewTest;
    ProgressBar progressBarWebView;
    boolean webViewSuccess = true;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        RootView = inflater.inflate(R.layout.fragment_add_cam, container, false);
        webViewTest = (WebView) RootView.findViewById(R.id.webViewTest);
        progressBarWebView =  (ProgressBar) RootView.findViewById(R.id.progressBar);
        progressBarWebView.setVisibility(View.GONE);

        OnClickButtonEnregistrer();
        OnClickButtonTest();

        return RootView;
    }

    /**
     * Fonction gérant l'évenement du bouton "Enregistrer". Si les données entrées sont correct, l'addresse IP et le Port sont envoyé à la
     * classe CameraFragment gérant la connexion Socket entre le client Android et le serveur de la Raspberry PI
     */
    void OnClickButtonEnregistrer()
    {
        this.buttonSave = (Button) RootView.findViewById(R.id.buttonSave);

        this.buttonSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //Rabaisser le clavier
                hideKeyboard();

                SharedPreference sharedPreference = new SharedPreference(getContext());

                //Récuperer les info entrés par l'utilisateurs
                textViewLabel = (TextView) RootView.findViewById(R.id.labelTextView);
                textViewIP = (TextView) RootView.findViewById(R.id.adrIpTextView);
                textViewPort = (TextView) RootView.findViewById(R.id.portTexView);
                String textIP = textViewIP.getText().toString();
                String textPort = textViewPort.getText().toString();
                String textLabel = textViewLabel.getText().toString();

                //Vérification de la validité des données entrés par l'utilisateur
                if(textLabel.isEmpty()) {
                    Toast.makeText(getActivity(), "Veuillez entrer un label pour la caméra", Toast.LENGTH_LONG).show();
                    return;
                }
                if(checkIpIsValid(textIP) == false) {
                    Toast.makeText(getActivity(), "Adresse IP incorrect, format attendue  000.000.00.0", Toast.LENGTH_LONG).show();
                    return;
                }
                if(checkPortIsValid(textPort) == false) {
                    Toast.makeText(getActivity(), "Port incorrect : celui-ci doit être compris entre 0 et 65536", Toast.LENGTH_LONG).show();
                    return;
                }

                //Enregistrement des données via SharedPreference
                int camID = sharedPreference.getNbCameraSaved() + 1;    //Récupération du nombre de caméra déja enregistré + 1
                sharedPreference.setNbCameraSaved(camID);               //Actualisation du nombre de caméra enregistré
                sharedPreference.setLabel(camID, textLabel);            //Enregistrement du label
                sharedPreference.setIpAddress(camID, textIP);           //Enregistrement de l'addresse IP
                sharedPreference.setPort(camID, textPort);              //Enregistrement du Port

                //Redirection vers le fragment de présentation des caméras
                ManageCameraFragment fragmentManageCam = new ManageCameraFragment();
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_container, fragmentManageCam)
                        .commit();
            }
        });
    }

    /**
     * Fonction permettant de rabaisser le clavier s'il est ouvert
     */
    void hideKeyboard()
    {
        try {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * Fonction gérant l'évenement du bouton "Test".
     * Permet de tester la connexion à la caméra en affichant le flux streaming de celle-ci dans une WebView.
     */
    void OnClickButtonTest()
    {
        this.buttonTest = (Button) RootView.findViewById(R.id.buttonTest);

        this.buttonTest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Rabaisser le clavier
                hideKeyboard();

                //Tester la validité des données entrées par l'utilisateur
                textViewIP = (TextView) RootView.findViewById(R.id.adrIpTextView);
                textViewPort = (TextView) RootView.findViewById(R.id.portTexView);
                String textIP = textViewIP.getText().toString();
                String textPort = textViewPort.getText().toString();


                if(checkIpIsValid(textIP) == false) {
                    Toast.makeText(getActivity(), "Adresse IP incorrect, format attendue  000.000.00.0", Toast.LENGTH_LONG).show();
                    return;
                }
                if(checkPortIsValid(textPort) == false) {
                    Toast.makeText(getActivity(), "Port incorrect : celui-ci doit être compris entre 0 et 65536", Toast.LENGTH_LONG).show();
                    return;
                }

                //tester la connexion à la caméra dans la WebView
                progressBarWebView.setVisibility(View.VISIBLE);

                webViewTest.setWebViewClient(new WebViewClient() {

                    @Override
                    public void onPageCommitVisible(WebView view, String url) {
                        super.onPageCommitVisible(view, url);

                        progressBarWebView.setVisibility(View.GONE);
                        if(webViewSuccess == true)
                            Toast.makeText(getActivity(), "Connexion Réussie", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage("Connexion échouée, vérifiez la connexion internet ainsi que les informations de connexion")
                                .setPositiveButton(android.R.string.ok, null).create().show();

                        webViewSuccess = false;
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        Log.e("LOG", "PAGE TERMINEE");
                        super.onPageFinished(view, url);
                        progressBarWebView.setVisibility(View.GONE);
                    }
                });

                webViewTest.getSettings().setDisplayZoomControls(false);  // Zoom webview
                webViewTest.getSettings().setUseWideViewPort(true);
                webViewTest.getSettings().setLoadWithOverviewMode(true);
                webViewTest.getSettings().setJavaScriptEnabled(true);
                webViewTest .loadUrl("http://" + textIP + ":" + textPort);
            }
        });
    }

    /**
     * Fonction contrôlant la validité de l'addresse IP entrée par l'utilisateur
     * @param IP Adresse IP (String)
     * @return true si l'addresse IP est correct, et false si incorrecte
     */
    public boolean checkIpIsValid(String IP)
    {
        Matcher checkIP = IP_ADDRESS.matcher(IP);
        if(checkIP.matches())
            return true;
        return false;
    }

    /**
     * Fonction contrôlant la validité du Port entré par l'utilisateur
     * @param PORT Numéro de Port (String)
     * @return true si le port est correct, et false si incorrect
     */
    public boolean checkPortIsValid(String PORT)
    {
        if(PORT.isEmpty())
            return false;

        short portNumber = (short)Integer.parseInt(PORT);

        if(portNumber >= 0 && portNumber <= 65536)
            return true;
        return false;
    }

    /**
     * Fonction contrôlant la validité de l'addresse IP entrée par l'utilisateur
     * @param IP Adresse IP (String)
     * @return
     */
    private static final Pattern IP_ADDRESS
            = Pattern.compile(
            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9]))");
}
