
import java.util.ArrayList;
import java.util.Objects;

public class InstanceSegmentationGraphe {
    private Graphe g;
    // g est un graphe orienté contenant des valeur de pénalités sur chaque arc

    private ArrayList<Integer> f = new ArrayList<>();
    private ArrayList<Integer> b = new ArrayList<>();
    // hypothèse : b inter f = vide

    // entrée du problème : (g,f,b)
    // sortie du problème : une (b,f) coupe B (avec B contenant b et pas f)
    // objectif : minimiser c(B), où c est la valeur de la coupe

    public static int penalite(int nivx, int nivy) {
        // calcule la penatlie entre deux niveaux de gris
        int v = (int) Math.abs(nivx - nivy);

        if (v <= 10)
            return 1000;
        if (v <= 35)
            return 100;
        if (v <= 90)
            return 10;
        if (v <= 210)
            return 1;
        return 0; // 0 <= .. <= 40
    }

    /*
     * fonction citée dans les articles, qui semble marcher moins bien car, pour le
     * fichier baby par ex,
     * va favoriser une coupe horizontale en plein milieu d'une zone monochrome, car
     * le cout
     * de cette partie est de L*penalitémax, alors que la bonne coupe qui longerait
     * le contour serait
     * de cout L2*penalitépetite (avec L2 longueur du contour), mais penalitemax est
     * seulement egal à penalitepetit*2, donc L2 > 2*L,
     * on garde cette mauvaise coupe!
     * public static int penalite(int nivx, int nivy){
     * double square = (nivx-nivy)*(nivx-nivy);
     * double f = (-square)/(2*sigma*sigma);
     * return (int) (100*Math.exp(f));
     * }
     */

    public Graphe getGraphe() {
        return g;
    }

    public InstanceSegmentationGraphe(Graphe g, ArrayList<Integer> f, ArrayList<Integer> b) {
        this.g = g;
        this.f = f;
        this.b = b;
    }

    public InstanceSegmentationGraphe(InstanceSegmentation isegm) {
        var img = isegm.getImg();
        var lines = img.nbLignes();
        var cols = img.nbColonnes();
        var size = lines * cols;

        this.g = new Graphe(size);

        isegm.getF().forEach(x -> this.f.add(img.calculIndice(x.getElement1(), x.getElement2())));
        isegm.getB().forEach(x -> this.b.add(img.calculIndice(x.getElement1(), x.getElement2())));

        for (int i = 0; i < size - 1; i++) {
            var cell = img.calculCoord(i);

            if (cell.getElement2() < lines - 1) {
                var greyDiff = penalite(getGrey(img, i), getGrey(img, i + cols));
                this.g.set(i, i + cols, greyDiff);
                this.g.set(i + cols, i, greyDiff);
            }

            if (cell.getElement1() < cols - 1) {
                var greyDiff = penalite(getGrey(img, i), getGrey(img, i + 1));
                this.g.set(i, i + 1, greyDiff);
                this.g.set(i + 1, i, greyDiff);
            }
        }
    }

    private static int getGrey(Img img, int cellNb) {
        var cell = img.calculCoord(cellNb);
        return img.get(cell.getElement1(), cell.getElement2());
    }

    public int getN() {
        return g.getN();
    }

    public int getValArc(int i, int j) {
        return g.get(i, j);
    }

    /**
     * calcule une solution optimale en se réduisant à un problème de minCut sur les
     * réseaux comme indiqué dans le sujet.
     *
     */
    public ArrayList<Integer> calculOpt() {
        var network = new Reseau(this);
        var minCut = network.coupeMin();
        minCut.remove(0);
        return minCut;
    }

    public String toString() {
        String str = g.toString();
        str += "\n";
        str += "\n b : " + b + "\n \n f :" + f;
        return str;
    }

    public ArrayList<Integer> getF() {
        return f;
    }

    public ArrayList<Integer> getB() {
        return b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof InstanceSegmentationGraphe))
            return false;
        InstanceSegmentationGraphe that = (InstanceSegmentationGraphe) o;
        return Objects.equals(g, that.g) && Objects.equals(getF(), that.getF()) && Objects.equals(getB(), that.getB());
    }

    @Override
    public int hashCode() {
        return Objects.hash(g, getF(), getB());
    }
}
