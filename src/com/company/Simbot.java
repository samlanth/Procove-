package com.company;

/**
 * Classe qui simule le coportement d'un robot de base. Conçue pour le test des
 * applications qui utilisent PROCOVE.
 * @version 1.0
 * @deprecated <span style="color:red">Cette classe devrait être remplacée par Simbot2</span>
 */
public class Simbot extends Robot implements Runnable {
    /**
     * Constante qui décrit les caractéristiques du robot
     */
    private static final String SPECS = "Simbot 1.0 - Simulateur de robot";

    /**
     * Constante qui représente la valeur par défaut du délai entre les
     * affichages de l'état du robot.
     */
    public static final int DELAI_DEFAUT = 5000;

    // délai entre les affichages de l'état du robot.
    private int mDelai;

    /**
     * Constructeur permettant d'indiquer le délai d'affichage. Inclut
     * l'auto-démarrage du thread d'affichage.
     * @param delai délai
     */
    public Simbot(int delai) {
        this.mDelai = delai;

        // auto-démarrage
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

    /**
     * Constructeur par défaut. Utilise le délai par défaut.
     */
    public Simbot() {
        this(DELAI_DEFAUT);
    }

    /**
     * Obtient de l'information sur le robot.
     * @return une description textuelle des caractéristiques du robot
     */
    public String getSpecs() {
        return SPECS;
    }

    /**
     * Boucle d'affichage de l'état du robot. NE DOIT PAS ÊTRE APPELÉE
     * DIRECTEMENT.
     */
    public void run() {
        System.out.println("Demarrage de Simbot avec affichage aux " + mDelai + " ms");
        while (true) {
            try {
                Thread.sleep(mDelai);
            } catch (InterruptedException ie) {
                System.err.println("Quelqu'un a reveille Simbot!");
            }
            System.out.printf("[M = %3d] [R = %3d]\n", super.mPuissance, super.mRotation);
        }
    }
}

