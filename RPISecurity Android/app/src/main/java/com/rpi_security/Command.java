package com.rpi_security;

/**
 * Created by Mohamed on 22/03/2019.
 *
 * Classe Command : Classe permettant de définir les code de commandes envoyés sur le serveur de la Raspberry, permettant d'effectuer
 * des actions définies
 */

public class Command
{
    public static final int ROT_CAM_UP              = 1;
    public static final int ROT_CAM_DOWN            = 2;
    public static final int ROT_CAM_RIGHT           = 3;
    public static final int ROT_CAM_LEFT            = 4;
    public static final int ACIVATE_ALARM           = 5;
    public static final int DESACTIVATE_ALARM       = 6;
    public static final int ACTIVATE_DETECTION      = 7;
    public static final int DESACTIVATE_DETECTION   = 8;
}
