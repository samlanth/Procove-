package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// Exemple de serveur PROCOVE
public class ServeurProcove
{
    // constantes
    public static final int PORT_PROCOVE = 51000;

    // variables membres
    private int mPort;
    private Robot mRobot;
    private ServerSocket mSocServeur;
    private int NBR_MAX_USER = 3;
    // Constructeur
    public ServeurProcove(int port, Robot robot)
    {
        mPort = port;
        mRobot = robot;

        try
        {
            mSocServeur = new ServerSocket(mPort);
        } catch (IOException ioe)
        {
            System.err.println("Echec au lancement du serveur\n--> " + ioe);
            System.exit(1);
        }
    }
    // Méthode principale
    public void servir()
    {
        System.out.println("Serveur PROCOVE en ligne au port " + mPort);

        boolean enService = true;
        while (enService)
        {
            try
            {
                if (Thread.activeCount()-2 < NBR_MAX_USER)
                {
                    Socket soc = mSocServeur.accept();
                    new Thread(new ServiceProcove(soc, mRobot)).start();
                }
            }
            catch (IOException ioe)
            {
                System.err.println("Erreur du serveur\n--> " + ioe);
                System.exit(1);
            }
        }
    }
    // Point d'entrée de l'application
    public static void main(String args[])
    {
        int port = PORT_PROCOVE;
        if (args.length <= 1)
        {
            System.err.println("Vous devez specifier le port TCP et la classe du robot");
        }
        else if (args.length > 2)
        {
            System.err.println("Trop de parametres");
            System.exit(1);
        }
        else
        {
            // création d'une instance de la classe dont le nom est passé en paramètre
            // au programme (rappel: toutes les classes dérivent de Robot)
            Robot robot = null;
            try
            {
                port = Integer.parseInt(args[0]);
                Class<?> classe = Class.forName("com.company." + args[1]);
                Object objet = classe.newInstance();
                robot = (Robot)objet;
            }
            catch (NumberFormatException nfe)
            {
                System.err.println("Le parametre n'est pas un nombre");
                System.exit(1);
            }
            catch (ClassNotFoundException cnfe)
            {
                System.err.println("Classe " + args[1] + " introuvable");
                System.exit(1);
            }
            catch (ReflectiveOperationException roe)
            {
                // récupère les autres exceptions reliées
                System.err.println("Probleme au chargement de la classe " + args[1]);
                System.err.println(roe.getMessage());
                System.exit(1);
            }
            // création et lancement du serveur
            ServeurProcove serveur = new ServeurProcove(port, robot);
            serveur.servir();
        }
    }
}


