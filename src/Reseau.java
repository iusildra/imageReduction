
import java.util.*;

public class Reseau {
    private Graphe g; // g.get(i,j) = capacité de l'arc i -> j (0 si pas d'arc, ou si arc de capacité
                      // 0)
    private int s;
    private int t;

    // ------------------------------------------------------------------
    // ------------------CONSTRUCTEURS ----------------------------------
    // ------------------------------- ----------------------------------

    public Reseau(int nbNoeud, int s, int t) {
        g = new Graphe(nbNoeud);
        this.s = s;
        this.t = t;
    }

    public Reseau(Graphe gg, int s, int t) {
        this.g = gg;
        this.s = s;
        this.t = t;
    }

    public Reseau(Reseau courant) {
        g = new Graphe(courant.g);
        this.s = courant.s;
        this.t = courant.t;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Reseau))
            return false;
        Reseau reseau = (Reseau) o;
        return getS() == reseau.getS() && getT() == reseau.getT() && g.equals(reseau.g);
    }

    @Override
    public int hashCode() {
        return Objects.hash(g, getS(), getT());
    }

    /**
     * Créé un réseau en fonction d'une instance du problème de DebruitageGraphe
     * inst comme spécifié dans le sujet.
     * En particulier, si le graph de inst à n sommets il faudra créer un réseau
     * avec n+2 sommets, et avec s=n et t=n+1.
     * indication : pensez à utiliser le constructeur Graphe(int h, Graphe g), et
     * vous pouvez utiliser Integer.MAX_VALUE pour +infini
     */
    public Reseau(InstanceSegmentationGraphe ins) {
        this.g = new Graphe(ins.getN() + 2, ins.getGraphe());
        this.s = ins.getN();
        this.t = ins.getN() + 1;

        ins.getB().forEach(cell -> {
            this.g.set(cell, s, Integer.MAX_VALUE);
            this.g.set(s, cell, Integer.MAX_VALUE);
        });
        ins.getF().forEach(cell -> {
            this.g.set(cell, t, Integer.MAX_VALUE);
            this.g.set(t, cell, Integer.MAX_VALUE);
        });
    }

    // ------------------------------------------------------------------
    // ------------------ GETTERS, SETTERS, METHODES UTILES et TOSTRING--
    // ------------------------------- ----------------------------------

    public int getN() {
        return g.getN();
    }

    public int getS() {
        return s;
    }

    public int getT() {
        return t;
    }

    public void set(int i, int j, int v) {
        g.set(i, j, v);
    }

    public int get(int i, int j) {
        return g.get(i, j);
    }

    public ArrayList<Integer> getVoisins(int i) {
        return g.getVoisinsSortant(i);
    }

    public ArrayList<Integer> getVoisinsResiduel(int i, Flot f) {
        // retourne tous les sommets j tq
        // soit i->j et arc pas saturé soit j->i et flot > 0
        ArrayList<Integer> sortants = g.getVoisinsSortant(i);
        ArrayList<Integer> entrants = g.getVoisinsEntrants(i);
        ArrayList<Integer> res = new ArrayList<>();
        for (int j : sortants) {
            if (f.getVal(i, j) < g.get(i, j)) {
                res.add(j);
            }
        }

        for (int j : entrants) {
            if (f.getVal(j, i) > 0) {
                if (!res.contains(j)) {// inutile quand ya pas de digons
                    res.add(j);
                }
            }
        }

        return res;
    }

    public String toString() {
        String res = "s : " + s + " t : " + t + "\n" + g;
        return res;
    }

    // ------------------------------------------------------------------
    // ------------------ METHODES POUR MAX FLOT / MIN CUT---------------
    // ------------------------------- ----------------------------------

    private ArrayList<Integer> remonteChemin(int[] pred) {
        ArrayList<Integer> res = new ArrayList<Integer>();
        int c = t;
        while (pred[c] != -1) {
            res.add(0, c);
            c = pred[c];
        }
        res.add(0, c);
        return res;
    }

    /**
     * Cherche un s-t chemin P dans reseau resdiuel de (this,f)
     * si un tel chemin n'existe pas alors retourne (C,null), avec C la composante
     * connexe de s
     * sinon retourne (null,P)
     */
    public Couple<ArrayList<Integer>, ArrayList<Integer>> trouverCheminDansResiduel(Flot f) {
        ArrayList<Integer> avoir = new ArrayList<Integer>();
        ArrayList<Integer> vus = new ArrayList<Integer>();

        int[] pred = new int[g.getN()];
        for (int i = 0; i < pred.length; i++) {
            pred[i] = -1;
        }

        avoir.add(s);
        boolean trouve = false;
        while (!trouve && !avoir.isEmpty()) {
            // avoir est disjoint de vus
            // pour tout i dans vu U avoir, on a un chemin de s -> .. -> i dans prec
            int v = avoir.remove(0);

            vus.add(v);
            if (v == t) {
                trouve = true;
            } else {
                ArrayList<Integer> vois = getVoisinsResiduel(v, f);
                for (int u : vois) {
                    if (!vus.contains(u) && !avoir.contains(u)) {
                        // u est un nouveau sommet
                        avoir.add(0, u);
                        pred[u] = v;
                    }
                }
            }
        }

        if (!trouve) {
            return new Couple<ArrayList<Integer>, ArrayList<Integer>>(vus, null);
        } else {
            return new Couple<ArrayList<Integer>, ArrayList<Integer>>(null, remonteChemin(pred));
        }

    }

    // ---------------------------- AUTRES METHODES --------------------------

    /**
     *
     * this est un réseau quelconque (potentiellement avec digons)
     * 
     * @return un flot maximum, et une coupe minimum
     *         <p>
     *         Applique les étapes de l'algorithme de Ford-Fulkerson vu en cours
     *         pensez à utiliser la méthode "trouverCheminDansResiduel(..)" et
     *         "modifieSelonChemin(..) (dans la classe FLot) qui vous sont fournies
     */
    private Couple<Flot, ArrayList<Integer>> flotMaxCoupeMin() {
        var flux = new Flot(this);
        return flotMaxCoupeMinAux(flux);
    }

    private Couple<Flot, ArrayList<Integer>> flotMaxCoupeMinAux(Flot f) {
        var path = trouverCheminDansResiduel(f);

        if (path.getElement2() == null) // No more path available
            return new Couple<>(f, path.getElement1());

        var minFlux = getMinFlux(path.getElement2(), f);

        f.modifieSelonChemin(path.getElement2(), minFlux);
        return flotMaxCoupeMinAux(f);
    }

    private int getMinFlux(ArrayList<Integer> chemin, Flot f) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < chemin.size() - 1; i++) {
            int val = g.get(chemin.get(i), chemin.get(i + 1));
            if (val < min)
                min = val;
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return min;
    }

    /**
     * On suppose que this est un réseau quelconque (avec peut être des digons)
     *
     * @return une coupe minimum
     */
    public ArrayList<Integer> coupeMin() {

        Reseau r = new Reseau(this);
        Couple<Flot, ArrayList<Integer>> res = r.flotMaxCoupeMin();
        ArrayList<Integer> minCut = res.getElement2();
        return minCut;

    }

    public static void main(String[] args) {

        System.out.println("main Reseau");

    }

}
