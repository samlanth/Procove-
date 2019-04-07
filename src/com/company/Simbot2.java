package com.company;

import java.util.Random;

/**
 * Classe qui simule un robot possédant tous les dispositifs prévus par PROCOVE:
 * <ul>
 *     <li>Indicateur de charge (BAT)</li>
 *     <li>Caméra (CAM)</li>
 *     <li>Détecteur de collision (COL)</li>
 *     <li>Boussole (DIR, POS)</li>
 *     <li>Laser (LAS)</li>
 *     <li>Dispositif d'éclairage (LUM)</li>
 *     <li>Avertisseur sonore (SON)</li>
 *     <li>Indicateur de vitesse (VIT)</li>
 * </ul>
 * @version 1.0 (29 mars 2019)
 */
public class Simbot2 extends Robot implements Runnable, BatterieInterrogeable,
        CameraActivable, CollisionInterrogeable, DirectionInterrogeable,
        LaserActivable, LumiereActivable, PositionInterrogeable, SonActivable, VitesseInterrogeable {

    /**
     * Description textuelle du robot
     */
    public static final String SPECS = "Simbot 2 - Simulateur de robot";

    /**
     * Délai d'affichage de l'état du robot dans la sortie standard.
     */
    public static final int DELAI_DEFAUT = 5000;

    private int mDelai;

    // dispositifs désactivés au départ
    private String mEtatCamera  = "OFF";
    private String mEtatLaser   = "OFF";
    private String mEtatLumiere = "OFF";
    private String mEtatSon     = "OFF";

    // valeurs aélatoires
    private int mEtatBatterie  = new Random().nextInt(101);
    private int mEtatCollision = new Random().nextInt(2);
    private int mEtatDirection = new Random().nextInt(3600) - 1;
    private int mEtatX         = new Random().nextInt(359) + 1;
    private int mEtatY         = new Random().nextInt(359) + 1;

    /**
     * Constructeur
     * @param delai Délai de rafraîchissement des informations
     */
    public Simbot2(int delai) {
        this.mDelai = delai;

        // auto-démarrage du thread d'affichage
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

    /**
     * Constructeur par défaut (utilise le délai par défaut)
     */
    public Simbot2() {
        this(DELAI_DEFAUT);
    }

    /**
     * Permet d'obtenir des informations sur le robot (nom, caractéristiques, etc.)
     * @return Description textuelle du robot
     */
    public String getSpecs() {
        return SPECS;
    }

    /**
     * Implémentation de l'interface BatterieInterrogeable
     * @return Pourcentage restant de la capacité totale de la batterie
     */
    public int getBatterie() {
        return mEtatBatterie;
    }

    /**
     * Implémentation de l'interface CameraActivable
     * @return État (allumé ou non) de la caméra embarquée
     */
    public boolean activerCamera(boolean on) {
        // caméra toujour disponible
        if (on) {
            mEtatCamera = "ON";
        } else {
            mEtatCamera = "OFF";
        }
        return true;
    }

    /**
     * Implémentation de l'interface CollisionInterrogeable
     * @return État (enfoncé ou mon) des pare-chocs
     */
    public int getCollision() {
        return mEtatCollision;
    }

    /**
     * Implémentation de l'interface DirectionInterrogeable
     * @return Direction courante
     */
    public int getDirection() {
        return mEtatDirection;
    }

    /**
     * Implémentation de l'interface LaserActivable
     * @param on true pour activer, false pour désactiver
     * @return true si le dispositif était disponible, false autrement
     */
    public boolean activerLaser(boolean on) {
        // laser toujour disponible
        if (on) {
            mEtatLaser = "ON";
        } else {
            mEtatLaser = "OFF";
        }
        return true;
    }

    /**
     * Implémentation de l'interface LumiereActivable
     * @param on true pour activer, false pour désactiver
     * @return true si le dispositif était disponible, false autrement
     */
    public boolean activerLumiere(boolean on) {
        // lumière toujour disponible
        if (on) {
            mEtatLumiere = "ON";
        } else {
            mEtatLumiere = "OFF";
        }
        return true;
    }

    /**
     * Implémentation de l'interface SonActivable
     * @param on true pour activer, false pour désactiver
     * @return true si le dispositif était disponible, false autrement
     */
    public boolean activerSon(boolean on) {
        // son toujour disponible
        if (on) {
            mEtatSon = "ON";
        } else {
            mEtatSon = "OFF";
        }
        return true;
    }

    /**
     * Permet d'obtenir la coordonnée X du robot (implémentation de l'interface
     * PositionInterrogeable)
     * @return Coordonnée en X
     */
    public int getX() {
        return mEtatX;
    }

    /**
     * Permet d'obtenir la coordonnée Y du robot (implémentation de l'interface
     * PositionInterrogeable
     * @return Coordonnée en Y)
     */
    public int getY() {
        return mEtatY;
    }

    /**
     * Implémentation de l'intercafe VitesseInterrogeable
     * @return vitesse effective du robot
     */
    public int getVitesse() {
        return super.getPuissance();
    }

    public void run() {
        System.out.println("Demarrage de Simbot 2 avec affichage aux " + mDelai + " ms");
        while (true) {
            try {
                Thread.sleep(mDelai);
            } catch (InterruptedException ie) {
                System.err.println("Quelqu'un a reveille Simbot!");
            }

            System.out.println("puissance : " + super.mPuissance);
            System.out.println(" rotation : " + super.mRotation);
            System.out.println("        x : " + mEtatX);
            System.out.println("        y : " + mEtatY);
            System.out.println("direction : " + mEtatDirection);
            System.out.println(" batterie : " + mEtatBatterie);
            System.out.println("   caméra : " + mEtatCamera);
            System.out.println("collision : " + mEtatCollision);
            System.out.println("    laser : " + mEtatLaser);
            System.out.println("  lumière : " + mEtatLumiere);
            System.out.println("      son : " + mEtatSon);
            System.out.println("  vitesse : " + Math.abs(super.getPuissance()));
            System.out.println();
        }
    }
}
