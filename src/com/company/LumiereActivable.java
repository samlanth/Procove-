package com.company;

/**
 * Interface implémentée par les robots équipés d'un dispositif d'éclairage
 */
public interface LumiereActivable {
    /**
     * Active ou désactive le dispositif d'éclairage embarquée
     * @param on true pour activer, false pour désactiver
     * @return true en cas de succées, false si le dispositif d'éclairage n'est pas disponible
     * @version 28 mars 2019
     */
    boolean activerLumiere(boolean on);
}
