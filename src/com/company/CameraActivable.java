package com.company;

/**
 * Interface implémentée par les robots équipés d'une caméra
 */
public interface CameraActivable {
    /**
     * Active ou désactive la caméra embarquée
     * @param on true pour activer, false pour désactiver
     * @return true en cas de succées, false si la caméra n'est pas disponible
     * @version 28 mars 2019
     */
    boolean activerCamera(boolean on);
}
