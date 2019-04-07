package com.company;

/**
 * Interface implémentée par les robots dont on peut obtenir la direction
 */
public interface DirectionInterrogeable {
    /**
     * Renseigne sur la direction courante du robot.
     * @return direction en dixièmes de degrés (0 à 3599) ou -1 si impossible à déterminer
     * @version 28 mars 2019
     */
    int getDirection();
}
