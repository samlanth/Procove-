package com.company;

/**
 * Interface implémentée par les robots équipés d'un laser
 */
public interface LaserActivable {
    /**
     * Active ou désactive la laser embarqué
     * @param on true pour activer, false pour désactiver
     * @return true en cas de succées, false si le laser n'est pas disponible
     * @version 28 mars 2019
     */
    boolean activerLaser(boolean on);
}
