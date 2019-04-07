package com.company;

/**
 * Interface implémentée par les robots dont on peut obtenir l'état des pare-chocs
 * @version 1.1 (29 mars 2019)
 */
public interface CollisionInterrogeable {
    /**
     * Renseigne sur l'état des pare-chocs du robot
     * @return un nombre positif si au moins un pare-choc est enfoncé, 0 si le robot
     * n'est pas en état de collision, -1 si l'information n'est pas disponible
     *
     * <span style="color:red">Attention, cette méthode retournait dans sa version précédente
     * un booléen</span>
     */
    int getCollision();
}
