package com.company;

/**
 * Interface implémentée par les robots dont on peut obtenir la position
 */
public interface PositionInterrogeable {
    /**
     * Renseigne sur la position courante du robot dans l'axe des X.
     * @return une valeur de 1 à 359 (raisons historiques) ou -1 si impossible à déterminer
     * @version 28 mars 2019
     */
    int getX();

    /**
     * Renseigne sur la position courante du robot dans l'axe des Y.
     * @return une valeur de 1 à 359 (raisons historiques) ou -1 si impossible à déterminer
     */
    int getY();
}
