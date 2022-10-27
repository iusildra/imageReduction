
import java.util.ArrayList;

public class Flot {
    private Reseau reseauF;
    private Graphe valeurFlot;
    // pour tout i,j, 0 <= valeurFlot.get(i,j) <= reseauF.get(i,j) (qui représente
    // la capacité de l'arc i->j)
    // et conservation du flot

    // --------------------------------------------------------
    // ---------------------CONTRUCTEUR -----------------------
    // --------------------------------------------------------

    public Flot(Reseau r) {
        reseauF = new Reseau(r);
        valeurFlot = new Graphe(reseauF.getN());

    }

    // ----------------------METHODES------------------------------

    public int getVal(int i, int j) {
        return valeurFlot.get(i, j);
    }

    public void incrementeFlot(int i, int j, int flot) {
        valeurFlot.set(i, j, valeurFlot.get(i, j) + flot);
    }

    /**
     * calcul la valeur du flot (pas utilisée)
     */

    public int getVal() {
        int res = 0;
        int s = reseauF.getS();
        for (int i = 0; i < valeurFlot.getN(); i++) {
            res += valeurFlot.get(s, i);
        }

        return res;
    }

    public String toString() {

        String res = "\n" + valeurFlot;
        res += "\n";

        return res;
    }

    /**
     * plus utilisée car pour accelerer les calculs on ne construit plus le réseau
     * résiduel
     *
     * On suppose que le reseauF est sans digon.
     * 
     * @return le réseau résiduel associé à reseauF et au flot this.
     *         Ce réseau résiduel pourra lui avoir des digons.
     * 
     */
    /*
     * public Reseau créerReseauResiduel() {
     * Reseau residuel = new Reseau(reseauF.getN(), reseauF.getS(), reseauF.getT());
     * int cap = 0;
     * 
     * for (int i = 0; i < valeurFlot[0].length; i++) {
     * for (int j = 0; j < valeurFlot[0].length; j++) {
     * 
     * if (reseauF.get(i,j) != 0) {
     * cap = reseauF.get(i,j) - this.valeurFlot[i][j];
     * residuel.set(i,j, cap);
     * 
     * cap = this.valeurFlot[i][j];
     * residuel.set(j,i, cap);
     * }
     * }
     * }
     * 
     * return residuel;
     * 
     * }
     */

    /**
     *
     * @param chemin  est un chemin augmentant de reseauF pour le flot this
     * @param epsilon > 0 est la capacité min de ce chemin (telle définie dans le
     *                cours)
     *                action : modifie le flot this en ajoutant ou retirant epsilon
     *                comme indiqué dans le cours
     */
    public void modifieSelonChemin(ArrayList<Integer> chemin, int epsilon) {
        for (int i = 0; i < chemin.size() - 1; i++) {
            int cour = chemin.get(i);
            int next = chemin.get(i + 1);
            if (reseauF.get(cour, next) - getVal(cour, next) >= epsilon) {
                incrementeFlot(cour, next, epsilon);
            } else {
                incrementeFlot(next, cour, -epsilon);
            }

        }
    }

}
