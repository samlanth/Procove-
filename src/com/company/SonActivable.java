package com.company;

/**
 * Interface implémentée par les robots équipés d'un avertisseur sonore.
 */
public interface SonActivable {
    /**
     * Active ou désactive l'avertisseur sonore.
     * @param on true pour activer, false pour désactiver
     * @return true en cas de succées, false si l'avertisseur n'est pas disponible
     * @version 28 mars 2019
     */
    boolean activerSon(boolean on);
}
