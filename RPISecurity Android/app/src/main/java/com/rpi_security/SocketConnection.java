package com.rpi_security;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Classe SocketConnexion : Classe permettant la création d'un client TCP pour la communication avec le serveur de la Raspberry.
 * Cette classe permet l'envoi de code de commande permettant la gestion des fonctionalités de la caméra
 * (code de commande présent dans la classe "Command")
 */

public class SocketConnection
{
    String IpAdress;
    short portNumber;
    Socket socket;

    /**
     * Constructeur permettant de récupérer l'adresse Ip et le port de connexion
     * @param IP (String) Adresse IP
     * @param PORT (String) Port
     */
    SocketConnection(String IP, String PORT)
    {
        this.IpAdress = IP;
        this.portNumber = (short)(Integer.parseInt(PORT)+1);
    }

    /**
     * Fonction permettant l'envoi d'un code de commande sur le seruveur TCP de la Raspberry
     * @param Command (Integer) Numéro de commande
     */
    public void sendCommandToRPI(int Command)
    {
        NetworkCommunication network = new NetworkCommunication(Command);
        network.execute();
    }

    /**
     * Classe Asynchrone permettant l'éxécution de la connexion au serveur
     */
    public class NetworkCommunication extends AsyncTask<Void, Void, Void>
    {
        String response;
        int CommandRPI;

        NetworkCommunication(int cmd)
        {
            this.CommandRPI = cmd;
        }

        /**
         * Fonction éxécutée en arrière plan, permettant l'envoi du code de commande sur le serveur de la Raspberry
         */
        @Override
        protected Void doInBackground(Void... arg0)
        {
            try
            {
                socket = new Socket(IpAdress, portNumber);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());//preparing the output stream so that broadcasts can be sent
                Log.e("CMD", String.valueOf(CommandRPI));
                dataOutputStream.writeByte(CommandRPI);//write bytes
                dataOutputStream.close();//close output stream

                socket.close();
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }
    }
}
