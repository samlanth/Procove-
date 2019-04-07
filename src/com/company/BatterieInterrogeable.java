package com.company;

/**
 * Interface implémentée par les robots dont on peut obtenir l'état de la batterie
 */
public interface BatterieInterrogeable {
    /**
     * Renseigne sur l'état de la batterie qui alimente le système de propulsion du robot.
     * @return pourcentage restant de la charge totale ou -1 si impossible à déterminer
     * @version 28 mars 2019
     */
    int getBatterie();
}
