package com.rpi_security;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Classe SharedPreference :
 */
public class SharedPreference
{

    Context context;
    android.content.SharedPreferences prefs;
    android.content.SharedPreferences.Editor editor;

    /**
     * Constructeur
     */
    public SharedPreference(Context context)
    {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
    }

    /**
     * Fonction qui retourne le nombre de caméra stockée
     * @return
     */
    public int getNbCameraSaved() {
        return prefs.getInt("NB_CAMERA_SAVED", 0);
    }

    /**
     * Fonction qui sauvegarde le nombre de caméra sauvegardés
     * @return
     */
    public void setNbCameraSaved(int nbCam) {
        editor.putInt("NB_CAMERA_SAVED", nbCam).commit();
    }


    /**
     * Fonction qui retourne l'adresse IP si elle existe, sinon null
     * @return
     */
    public String getLabel(int camID) {
        return prefs.getString("LABEL"+camID, "null");
    }

    /**
     * Fonction qui sauvegarde l'addresse IP
     * @return
     */
    public void setLabel(int camID, String label) {
        editor.putString("LABEL"+camID, label).commit();
    }


    /**
     * Fonction qui retourne l'adresse IP si elle existe, sinon null
     * @return
     */
    public String getIpAddress(int camID) {
        return prefs.getString("IP"+camID, "null");
    }

    /**
     * Fonction qui sauvegarde l'addresse IP
     * @return
     */
    public void setIpAddress(int camID, String ip) {
        editor.putString("IP"+camID, ip).commit();
    }


    /**
     * Fonction qui retourne le Port si il existe, sinon null
     * @return
     */
    public String getPort(int camID) {
        return prefs.getString("PORT"+camID, "null");
    }

    /**
     * Fonction qui sauvegarde le Port
     * @return
     */
    public void setPort(int camID, String port) {
        editor.putString("PORT"+camID, port).commit();
    }


    /**
     * Fonction qui supprime une caméra
     * @return
     */
    public void clearData()
    {
        editor.clear().commit();
    }

    /**
     * Fonction permettant de supprimer une caméra enregistré
     * @param camId (ID de la caméra)
     */
    public void removeCamera(int camId)
    {
        editor.remove("LABEL"+camId);
        editor.remove("IP"+camId);
        editor.remove("PORT"+camId);
    }
}
