

import java.util.*;
import java.io.*;

public class Img {

    private int[][] data;  //data[i][j] représente le pixel(i,j) avec i numero colonne et j num ligne avec ligne 0 en haut, et colonne 0 à gauche
    private int hauteur;
    private int largeur;




    /**
     *
     * @param nomFichier : adresse d'un fichier pgm, ex "fichier.pgm"
     */
    public Img(String nomFichier){
        try {
            Pixmap pm = new Pixmap(nomFichier);
            data = new int[pm.getW()][pm.getH()];
            for (int i = 0; i < pm.getW(); i++) {
                for (int j = 0; j < pm.getH(); j++) {

                    data[i][j] = pm.get(i, j);
                }
            }
            setHL();
        }catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Img(int[][] tab){
        data = new int[tab.length][tab[0].length];
        for(int i=0;i<tab.length;i++){
            for(int j=0; j<tab[0].length;j++){
                data[i][j] = tab[i][j];
            }
        }
        setHL();
    }

    public Img(Img i){
        this(i.data);
    }



    private void setHL(){
        hauteur = data[0].length;
        largeur = data.length;
    }


    public int get(int i, int j){
        return data[i][j];
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Img)) return false;
        Img img = (Img) o;
        return hauteur == img.hauteur && largeur == img.largeur && Arrays.deepEquals(data, img.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(hauteur, largeur);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    /************************************************************/
    /* méthodes relatives aux conversions de cooredonées de pixels */
    /************************************************************/

    public int calculIndice(int i, int j){
        return largeur*j+i;
    }

    public Couple<Integer,Integer> calculCoord(int x){
        int j = x/largeur;
        int i  = x%largeur;
        Couple<Integer,Integer>  res = new Couple<Integer,Integer>(i,j);
        return res;
    }

    /**
     *
     * @param l : liste de sommets
     * @return les coordonnées de ces sommets
     */
    public ArrayList<Couple<Integer,Integer>> calculFiltre(ArrayList<Integer> l)
    {
        ArrayList<Couple<Integer,Integer>> res = new ArrayList<Couple<Integer,Integer>>();
        for (Integer x : l){
            res.add(calculCoord(x));
        }
        return res;
    }


    /**
     param : B : liste des pixels à mettre en back ground, f et b : entrée du problème de InstanceSegmentation

     effet
     créer une nouvelle image (et ne modifie pas this), dans laquelle on
     -met en blanc (255) les pixels de b et en gris clair (200) les pixels de B \ b (le background devient donc gris clair, sauf les pixels de $b$ qui sont blancs)
     -met en noir (0) les pixels de f, et laisse l'image intacte pour les pixels de (P \ B) \ f$ (le forground reste intact, sauf les pixels
de f qui sont noirs)



     */
    public Img appliquerFiltre(ArrayList<Couple<Integer,Integer>> B, ArrayList<Couple<Integer,Integer>>f, ArrayList<Couple<Integer,Integer>>b) {
    	//A COMPLETER
	
 	return null;
    }


    /************************************************************/
    /* fin méthodes relatives aux conversions de cooredonées de pixels */
    /************************************************************/


    public String toString(){
        String str = "";
        for (int j = 0; j < nbLignes(); j++) {
            for (int i = 0; i < nbColonnes(); i++) {
                str = str + "\t" + data[i][j];
            }
            str = str + "\n";
        }
        return str;
    }

    public int nbColonnes(){
        return largeur;
    }

    public int nbLignes(){
        return hauteur;
    }


    /**
     * Convertie l'image en un nouveau ficher pgm
     * @param nomFile : nom du nouveau ficher
     */
    public void creerImage(String nomFile) {
        Pixmap pm = new Pixmap(this.nbColonnes(), this.nbLignes());
        pm.setData();
        for (int i = 0; i < nbColonnes(); i++) {
            for (int j = 0; j < nbLignes(); j++) {
                pm.set(i, j, data[i][j]);
            }
        }
        pm.write(nomFile);
    }


}
