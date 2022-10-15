
import java.util.*;

public class InstanceSegmentation {

    private Img img;
    private ArrayList<Couple<Integer, Integer>> f;// liste coordonnées des pixels imposés dans le foreground
    private ArrayList<Couple<Integer, Integer>> b;// liste coordonnées des pixels imposés dans le background
    // hypothese b inter f = vide

    // entrée du problème : (img,f,b)
    // sortie du problème : une (b,f) coupe B (qui contient b et pas f)
    // objectif : minimiser c(B)

    public InstanceSegmentation(Img i, ArrayList<Couple<Integer, Integer>> f, ArrayList<Couple<Integer, Integer>> b) {

        img = i;
        this.f = f;
        this.b = b;

    }

    public Img getImg() {
        return img;
    }

    public ArrayList<Couple<Integer, Integer>> resoudre() {
        // calcule une solution optimale de l'instance (Im,f,b) en se réduisant à
        // SemgentationGraphe (pensez à utiliser des méthodes de Img pour les
        // conversions entre numéros de sommets dans le graphe
        // et coordonnées de pixels
        // A COMPLETER
        return null;
    }

    public Img creerImageSegmentee() {
        ArrayList<Couple<Integer, Integer>> B = resoudre();
        return img.appliquerFiltre(B, f, b);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof InstanceSegmentation))
            return false;
        InstanceSegmentation that = (InstanceSegmentation) o;
        return img.equals(that.img) && f.equals(that.f) && b.equals(that.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(img, f, b);
    }

    public String toString() {
        return img.toString() + "\n f : " + f + "\n b : " + b;
    }

    public ArrayList<Couple<Integer, Integer>> getF() {
        return f;
    }

    public ArrayList<Couple<Integer, Integer>> getB() {
        return b;
    }
}
