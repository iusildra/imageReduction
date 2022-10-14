
import java.util.ArrayList;
import java.util.Objects;

public class InstanceSegmentationGraphe {
    private Graphe g;
    //g est un graphe orienté contenant des valeur de pénalités sur chaque arc

    private ArrayList<Integer> f;
    private ArrayList<Integer> b;
    //hypothèse : b inter f = vide


    //entrée du problème : (g,f,b)
    //sortie du problème : une (b,f) coupe  B (avec B contenant b et pas f)
    //objectif : minimiser c(B), où c est la valeur de la coupe 


    public static int penalite(int nivx, int nivy){
	//calcule la penatlie entre deux niveaux de gris
        int v = (int)Math.abs(nivx-nivy);

        if(v <= 10) return 1000;
        if(v <= 35) return 100;
        if(v <= 90) return 10;
        if(v <= 210) return 1;
        return 0; //0 <= .. <= 40
    }




/*
    fonction citée dans les articles, qui semble marcher moins bien car, pour le fichier baby par ex,
    va favoriser une coupe horizontale en plein milieu d'une zone monochrome, car le cout
    de cette partie est de L*penalitémax, alors que la bonne coupe qui longerait le contour serait
    de cout L2*penalitépetite (avec L2 longueur du contour), mais penalitemax est seulement egal à penalitepetit*2, donc L2 > 2*L,
    on garde cette mauvaise coupe!
    public static int penalite(int nivx, int nivy){
        double square = (nivx-nivy)*(nivx-nivy);
        double f = (-square)/(2*sigma*sigma);
        return (int) (100*Math.exp(f));
    }
*/


    public Graphe getGraphe(){
        return g;
    }

    public InstanceSegmentationGraphe(Graphe g, ArrayList<Integer>f, ArrayList<Integer>b){
        this.g=g;
        this.f=f;
        this.b=b;
    }

    public InstanceSegmentationGraphe(InstanceSegmentation isegm) {
        //A COMPLETER

        // construit un graphe tel que
        // - pour chaque sommets u et v du graphe (où u correspond au pixel (i,j,), et v correspond à un pixel voisin (i',j')) de x, on ait l'arc u->v
        // de capacité égale à la pénalité entre les niveaux de gris de (i,j) et (i',j')
        // - b et f contient les indices de sommets correspondants aux pixels de im.f et im.g

        /*
          si im est une image 3x3, va créer un graphe à 9 sommets, avec la numérotation
	     0 1 2
	     3 4 5
	     6 7 8

	    et par exemple les arcs sortants du sommet 3 sont les arcs 3->0, 3->4, et 3->6
	    */
       
    

    }

    public int getN(){
	    return g.getN();
    }

    public int getValArc(int i, int j){
	return g.get(i,j);
    }
    
   

    /**
     * calcule une solution optimale en se réduisant à un problème de minCut sur les réseaux comme indiqué dans le sujet.
     *
     */
    public ArrayList<Integer> calculOpt(){
        //A COMPLETER
        return null;
    }


    public String toString() {
        String str = g.toString();
	    str += "\n";
        str += "\n b : " + b + "\n \n f :" + f ;
        return str;
    }

    public ArrayList<Integer> getF(){
        return f;
    }
    public ArrayList<Integer> getB(){
        return b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstanceSegmentationGraphe)) return false;
        InstanceSegmentationGraphe that = (InstanceSegmentationGraphe) o;
        return Objects.equals(g, that.g) && Objects.equals(getF(), that.getF()) && Objects.equals(getB(), that.getB());
    }

    @Override
    public int hashCode() {
        return Objects.hash(g, getF(), getB());
    }
}
