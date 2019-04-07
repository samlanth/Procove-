package com.company;
/**
 * Classe mère de tous les robots.
 * @version 1.0
 */
public abstract class Robot {
    // pourcentage de la puissance maximale utilisée
    protected int mPuissance = 0;
    // pourcentage de la rotation maximale appliquée
    protected int mRotation = 0;

    /**
     * Obtient de l'information sur le robot.
     * @return une description textuelle des caractéristiques du robot
     */
    public abstract String getSpecs();

    /**
     * Règle la puissance utilisée par le robot.
     * @param puissance pourcentage de la puissance disponible
     */
    public void setPuissance(int puissance) {
        mPuissance = puissance;
    }

    /**
     * Règle la rotation appliquée au robot.
     * @param rotation pourcentage de la rotation maximale
     */
    public void setRotation(int rotation) {
        mRotation = rotation;
    }

    /**
     * Obtient la puissance utilisée par le robot.
     * @return pourcentage de la puissance disponible utilisée
     */
    protected int getPuissance() {
        return mPuissance;
    }

    /**
     * Obtient la rotation appliquée au robot.
     * @return pourcentage de la rotation maximale appliquée
     */
    protected int getRotation() {
        return mRotation;
    }
}
