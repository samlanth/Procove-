package com.company;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

// Exemple de service PROCOVE
public class ServiceProcove implements Runnable {
    // constantes
    public static final int DELAI_CTR = 30000;
    public static final String NO_VERSION = "1.8";

    // variables de classe
    private static ServiceProcove cPilote; // client en possession du contrôle

    // variables membres
    private Socket mSocket;
    private BufferedReader mReader;
    private PrintWriter mWriter;
    private Robot mRobot;

    // constructeur
    public ServiceProcove(Socket socket, Robot robot)
    {
        mSocket = socket;
        mRobot = robot;

        // fixation d'un délai pour les opérations bloquantes
        try
        {
            mSocket.setSoTimeout(DELAI_CTR);
        } catch (SocketException se)
        {
            System.err.println("Echec a la creation du service\n--> " + se);
        }
    }

    private void deconnecter()
    {
        // pour laisser le temps à l'utilisateur distant de voir la réponse
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException ie)
        {

        }

        // si l'utilisateur est le pilotes on lui enlève le contrôle
        if (cPilote == this)
        {
            mRobot.setPuissance(0); // arrête aussi le robot
            mRobot.setRotation(0);
            cPilote = null;
        }

        // libération des ressources
        try
        {
            // les flux peuvent ne pas avoir été créés (possible selon la façon
            // dont on gère le nombre de connexions)
            if (mReader != null)
            {
                mReader.close();
            }
            if (mWriter != null)
            {
                mWriter.close();
            }

            mSocket.close();
        }
        catch (IOException ioe)
        {
            System.err.println("Erreur a la deconnexion d'un client\n--> " + ioe);
        }
    }

    // méthode principale
    public void run()
    {
        // obtention des flux (le writer est "autoflush")
        try
        {
            mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            mWriter = new PrintWriter(new OutputStreamWriter(mSocket.getOutputStream()), true);

            // boucle de lecture des requêtes
            boolean enService = true;
            while (enService)
            {
                String ligne = null;
                String mots[]; // une ligne est formée de mots

                try
                {
                    ligne = mReader.readLine();
                }
                catch (SocketTimeoutException ste)
                {
                    // délai expiré, s'il s'agit du pilote on lui enlève le contrôle
                    if (cPilote == this)
                    {
                        mRobot.setPuissance(0); // arrête aussi le robot
                        mRobot.setRotation(0);
                        cPilote = null;
                    }
                    continue; // passe au tour de boucle suivant
                }

                if (ligne != null)
                {
                    ligne = ligne.trim();

                    // si la ligne est vide
                    if (ligne.length() == 0)
                    {
                        mWriter.println("ERR 1" + "\r");
                    }
                    else
                    {
                        // l'expression régulière permet d'utiliser n'importe quel nombre
                        // d'espaces et de tabulateurs comme séparateur
                        mots = ligne.split("\\s+");
                        enService = traiterRequete(mots);
                    }
                }
                else
                { // ligne == null
                    enService = false;
                }

            }
        }
        catch (IOException ioe)
        {
            System.err.println("Erreur dans la gestion d'un client\n--> " + ioe);
        }
        // l'écnahge avec le client est terminé
        deconnecter();
    }

    private boolean traiterRequete(String mots[])
    {
        boolean continuer = true;
        String requete = mots[0].toUpperCase();

        switch (requete)
        {
            case "BAS":// non implémenté
                mWriter.println("ERR 5" + "\r");
                break;

            case "BAT":
                if (mRobot instanceof BatterieInterrogeable)
                {
                    if (mots.length != 1)
                    {
                        mWriter.println("ERR 2");
                    }
                    else
                    {
                        BatterieInterrogeable r = (BatterieInterrogeable) mRobot;
                        if (r.getBatterie() >= 0 && r.getBatterie() <= 100)
                        {
                            mWriter.println(r.getBatterie());
                        }
                        else
                        {
                            mWriter.println("ERR 6");
                            System.out.println("Service temporairement non disponible.");
                        }
                    }
                }
                else
                {
                    // Service non implémenté.
                    mWriter.println("ERR 5");
                }
                break;

            case "CAM":

                if (mRobot instanceof CameraActivable)
                {
                    boolean s = false;
                    if (cPilote == null || cPilote != this)
                    {
                        // Le demandeur n'a pas le contrôle du véhicule.
                        mWriter.println("ERR 7" + "\r");
                    }
                    else if (mots.length > 2 || mots.length < 2)
                    {
                        // S'il n'y a pas exactement un paramètre.
                        mWriter.println("ERR 2");
                    }
                    else if (!mots[1].toUpperCase().equals("ON") && !mots[1].toUpperCase().equals("OFF"))
                    {
                        // Le paramètre n'est pas ON ou OFF.
                        mWriter.println("ERR 3");
                    }
                    else
                    {
                        if (mots[1].toUpperCase().equals("ON"))
                        {
                            s = true;
                            CameraActivable r = (CameraActivable) mRobot;
                            if (r.activerCamera(s))
                            {
                                // Activer la Camera
                                mWriter.println("ACK");
                                System.out.println("Camera Activer");
                            }
                            else
                            {
                                // La caméra est temporairement hors service.
                                mWriter.println("ERR 6");
                                System.out.println("Camera non disponible");
                            }
                        }
                        else if (mots[1].toUpperCase().equals("OFF"))
                        {
                            s = false;
                            CameraActivable r = (CameraActivable) mRobot;
                            if (r.activerCamera(s))
                            {
                                // Desactiver la Camera
                                mWriter.println("ACK");
                                System.out.println("Camera Desactiver");
                            }
                            else
                            {
                                // La caméra est temporairement hors service.
                                mWriter.println("ERR 6");
                                System.out.println("Camera non disponible");
                            }
                        }
                    }
                }
                else
                {
                    // Le véhicule n'est pas équipé d'une caméra.
                    mWriter.println("ERR 5");
                }
                break;

            case "COL":
                if (mRobot instanceof CollisionInterrogeable)
                {
                    if (mots.length != 1)
                    {
                        mWriter.println("ERR 2");
                    }
                    else
                    {
                        CollisionInterrogeable r = (CollisionInterrogeable) mRobot;
                        if (r.getCollision() == 1)
                        {
                            mWriter.println("COL 1");
                        }
                        else if (r.getCollision() == 0)
                        {
                            mWriter.println(("COL 0"));
                        }
                        else
                        {
                            mWriter.println("ERR 6");
                            System.out.println("Service temporairement non disponible.");
                        }
                    }
                }
                else
                {
                    // Service non implémenté.
                    mWriter.println("ERR 5");
                }
                break;

            case "CTR":
                if (mots.length > 1)
                {
                    mWriter.println("ERR 2" + "\r");
                }
                else if (cPilote == null)
                {
                    // prend le contrôle
                    cPilote = this;
                    mWriter.println("ACK" + "\r");
                }
                else if (cPilote == this)
                {
                    // a déjà le contrôle
                    mWriter.println("ACK" + "\r");
                }
                else
                {
                    // quelqu'un d'autre a déjà le contrôle
                    mWriter.println("ERR 7" + "\r");
                }
                break;

            case "DEL":
                if (mots.length > 1)
                {
                    mWriter.println("ERR 2" + "\r");
                }
                else
                {
                    // retourne la valeur en secondes
                    mWriter.println("DEL " + (DELAI_CTR / 1000) + "\r");
                }
                break;

            case "DIR":
                if (mRobot instanceof DirectionInterrogeable)
                {
                    if (mots.length != 1)
                    {
                        mWriter.println("ERR 2");
                    }
                    else
                    {
                        DirectionInterrogeable r = (DirectionInterrogeable) mRobot;
                        if (r.getDirection() >= 0 && r.getDirection() <= 3599)
                        {
                            mWriter.println("DIR "+ r.getDirection());
                        }
                        else
                        {
                            mWriter.println("ERR 6");
                            System.out.println("Service temporairement non disponible.");
                        }
                    }
                }
                else
                {
                    mWriter.println("ERR 5");
                }
                break;

            case "FIN":
                if (mots.length > 1)
                {
                    mWriter.println("ERR 2" + "\r");
                }
                else
                {
                    mWriter.println("ACK" + "\r");
                    // valeur retournée à la méthode run()
                    continuer = false;
                }
                break;

            case "LAS":
                if (mRobot instanceof LaserActivable)
                {
                    boolean s = false;
                    if (cPilote == null || cPilote != this)
                    {
                        // Le demandeur n'a pas le contrôle du véhicule.
                        mWriter.println("ERR 7" + "\r");
                    }
                    else if (mots.length > 2 || mots.length < 2)
                    {
                        // S'il n'y a pas exactement un paramètre.
                        mWriter.println("ERR 2");
                    }
                    else if (!mots[1].toUpperCase().equals("ON") && !mots[1].toUpperCase().equals("OFF"))
                    {
                        // Le paramètre n'est pas ON ou OFF.
                        mWriter.println("ERR 3");
                    }
                    else
                    {
                        if (mots[1].toUpperCase().equals("ON"))
                        {
                            s = true;
                            LaserActivable r = (LaserActivable) mRobot;
                            if (r.activerLaser(s))
                            {
                                // Activer le Laser
                                mWriter.println("ACK");
                                System.out.println("Laser Activer");
                            }
                            else
                            {
                                // Le laser est temporairement hors service.
                                mWriter.println("ERR 6");
                                System.out.println("Laser non disponible");
                            }
                        }
                        else if (mots[1].toUpperCase().equals("OFF"))
                        {
                            s = false;
                            LaserActivable r = (LaserActivable) mRobot;
                            if (r.activerLaser(s))
                            {
                                // Desactiver le Laser
                                mWriter.println("ACK");
                                System.out.println("Laser Desactiver");
                            }
                            else
                            {
                                //  	Le laser est temporairement hors service.
                                mWriter.println("ERR 6");
                                System.out.println("Laser non disponible");
                            }
                        }
                    }
                }
                else
                {
                    // Le véhicule n'est pas équipé d'un laser.
                    mWriter.println("ERR 5");
                }
                break;

            case "LST":
                if (mots.length > 1)
                {
                    mWriter.println("ERR 2" + "\r");
                }
                else
                {
                    mWriter.println("LST CTR DEL FIN LST MOT REL ROT SPC SYN VER" + "\r");
                }
                break;

            case "LUM": // non implémenté
                if (mRobot instanceof LumiereActivable)
                {
                    boolean s = false;
                    if (cPilote == null || cPilote != this)
                    {
                        // Le demandeur n'a pas le contrôle du véhicule.
                        mWriter.println("ERR 7" + "\r");
                    }
                    else if (mots.length > 2 || mots.length < 2)
                    {
                        // S'il n'y a pas exactement un paramètre.
                        mWriter.println("ERR 2");
                    }
                    else if (!mots[1].toUpperCase().equals("ON") && !mots[1].toUpperCase().equals("OFF"))
                    {
                        // Le paramètre n'est pas ON ou OFF.
                        mWriter.println("ERR 3");
                    }
                    else
                    {
                        if (mots[1].toUpperCase().equals("ON"))
                        {
                            s = true;
                            LumiereActivable r = (LumiereActivable) mRobot;
                            if (r.activerLumiere(s))
                            {
                                // Activer la Lumiere
                                mWriter.println("ACK");
                                System.out.println("Lumiere Activer");
                            }
                            else
                            {
                                // La Lumiere est temporairement hors service.
                                mWriter.println("ERR 6");
                                System.out.println("Lumiere non disponible");
                            }
                        }
                        else if (mots[1].toUpperCase().equals("OFF"))
                        {
                            s = false;
                            LumiereActivable r = (LumiereActivable) mRobot;
                            if (r.activerLumiere(s))
                            {
                                // Desactiver la lumiere
                                mWriter.println("ACK");
                                System.out.println("Lumiere Desactiver");
                            }
                            else
                            {
                                //  La lumiere est temporairement hors service.
                                mWriter.println("ERR 6");
                                System.out.println("Lumiere non disponible");
                            }
                        }
                    }
                }
                else
                {
                    // Le véhicule n'est pas équipé d'une lumiere.
                    mWriter.println("ERR 5");
                }
                break;

            case "MOT":
                if (mots.length != 2)
                {
                    mWriter.println("ERR 2" + "\r");
                }
                else
                {
                    try
                    {
                        int n = Integer.parseInt(mots[1]);
                        if (n < -100 || n > 100)
                        {
                            mWriter.println("ERR 4" + "\r");
                        }
                        else if (cPilote == null || cPilote != this)
                        {
                            // n'a pas le contrôle
                            mWriter.println("ERR 7" + "\r");
                        }
                        else
                        {
                            // est en possession du contrôle
                            mRobot.setPuissance(n);
                            mRobot.setRotation(0);
                            mWriter.println("ACK" + "\r");
                        }
                    }
                    catch (NumberFormatException nfe)
                    {
                        mWriter.println("ERR 3" + "\r");
                    }
                }
                break;

            case "NAO": // non implémenté
                mWriter.println("ERR 5" + "\r");
                break;

            case "POS": // non implémenté
                if (mRobot instanceof PositionInterrogeable)
                {
                    if (mots.length != 1)
                    {
                        mWriter.println("ERR 2");
                    }
                    else
                    {
                        PositionInterrogeable r = (PositionInterrogeable) mRobot;
                        if (r.getX() >= 1 && r.getX() <= 359 && r.getY() >= 1 && r.getY() <= 359)
                        {
                            mWriter.println("POS "+ r.getX() +" "+ r.getY());
                        }
                        else
                        {
                            mWriter.println("ERR 6");
                            System.out.println("Service temporairement non disponible.");
                        }
                    }
                }
                else
                {
                    mWriter.println("ERR 5");
                }
                break;

            case "REL":
                if (mots.length > 1)
                {
                    mWriter.println("ERR 2" + "\r");
                }
                else
                {
                    // s'il s'agit du pilote, arrête d'abord la course du robot
                    if (cPilote == this)
                    {
                        mRobot.setPuissance(0);
                        mRobot.setRotation(0);
                        cPilote = null;
                    }
                    mWriter.println("ACK" + "\r");
                }
                break;

            case "ROT":
                if (mots.length != 2)
                {
                    mWriter.println("ERR 2" + "\r");
                }
                else
                {
                    try
                    {
                        int n = Integer.parseInt(mots[1]);
                        if (n < -100 || n > 100)
                        {
                            mWriter.println("ERR 4" + "\r");
                        }
                        else if (cPilote == null || cPilote != this)
                        {
                            // n'a pas le contrôle
                            mWriter.println("ERR 7" + "\r");
                        }
                        else
                        {
                            // est en possession du contrôle
                            mRobot.setRotation(n);
                            mWriter.println("ACK" + "\r");
                        }
                    }
                    catch (NumberFormatException nfe)
                    {
                        mWriter.println("ERR 3" + "\r");
                    }
                }
                break;

            case "SON": // non implémenté
                if (mRobot instanceof SonActivable)
                {
                    boolean s = false;
                    if (cPilote == null || cPilote != this)
                    {
                        // Le demandeur n'a pas le contrôle du véhicule.
                        mWriter.println("ERR 7" + "\r");
                    }
                    else if (mots.length > 2 || mots.length < 2)
                    {
                        // S'il n'y a pas exactement un paramètre.
                        mWriter.println("ERR 2");
                    }
                    else if (!mots[1].toUpperCase().equals("ON") && !mots[1].toUpperCase().equals("OFF"))
                    {
                        // Le paramètre n'est pas ON ou OFF.
                        mWriter.println("ERR 3");
                    }
                    else
                    {
                        if (mots[1].toUpperCase().equals("ON"))
                        {
                            s = true;
                            SonActivable r = (SonActivable) mRobot;
                            if (r.activerSon(s))
                            {
                                // Activer le Son
                                mWriter.println("ACK");
                                System.out.println("Son Activer");
                            }
                            else
                            {
                                // Le son est temporairement hors service.
                                mWriter.println("ERR 6");
                                System.out.println("Le son non disponible");
                            }
                        }
                        else if (mots[1].toUpperCase().equals("OFF"))
                        {
                            s = false;
                            SonActivable r = (SonActivable) mRobot;
                            if (r.activerSon(s))
                            {
                                // Desactiver le Son
                                mWriter.println("ACK");
                                System.out.println("Le son est Desactiver");
                            }
                            else
                            {
                                // Le Son est temporairement hors service.
                                mWriter.println("ERR 6");
                                System.out.println("Le son est non disponible");
                            }
                        }
                    }
                }
                else
                {
                    // Le véhicule n'est pas équipé de son.
                    mWriter.println("ERR 5");
                }
                break;

            case "SPC":
                if (mots.length > 1)
                {
                    mWriter.println("ERR 2" + "\r");
                }
                else
                {
                    mWriter.println("SPC " + mRobot.getSpecs() + "\r");
                }
                break;

            case "SYN":
                if (mots.length > 1)
                {
                    mWriter.println("ERR 2" + "\r");
                }
                else
                {
                    mWriter.println("ACK" + "\r");
                }
                break;

            case "VER":
                if (mots.length > 1)
                {
                    mWriter.println("ERR 2" + "\r");
                }
                else
                {
                    mWriter.println(NO_VERSION + "\r");
                }
                break;

            case "VIT":
                if (mRobot instanceof VitesseInterrogeable)
                {
                    if (mots.length != 1)
                    {
                        mWriter.println("ERR 2");
                    }
                    else
                    {
                        VitesseInterrogeable r = (VitesseInterrogeable) mRobot;
                        if (r.getVitesse() >= 0 && r.getVitesse() <=100)
                        {
                            mWriter.println("VIT " + r.getVitesse());
                        }
                        else
                        {
                            mWriter.println("ERR 6");
                            System.out.println("Service temporairement non disponible.");
                        }
                    }
                }
                else
                {
                    mWriter.println("ERR 5");
                }
                break;

            default:
                mWriter.println("ERR 1" + "\r");
        }
        return continuer;
    }
}

