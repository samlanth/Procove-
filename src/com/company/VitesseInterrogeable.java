package com.company;

/**
 * Interface implémentée par les robots dont on peut obtenir la vitesse
 */
public interface VitesseInterrogeable {
    /**
     * Renseigne sur la vitesse courante du robot.
     * @return valeur entière (unités non déterminées)
     * @version 28 mars 2019
     */
    int getVitesse();
}
