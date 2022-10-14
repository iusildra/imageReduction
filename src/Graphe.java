
import java.util.ArrayList;
import java.util.Objects;

public class Graphe{
    private ArrayList<ArrayList<Couple<Integer,Integer>>> l;
    //liste adj : dans l.get(i) il y a tous les successeurs de i avec la capacité des arcs sortants
    //par ex, si on a l'arc i->j de capacité c, alors dans la liste l.get(i) on trouvera le couple (j,c)


    private ArrayList<ArrayList<Couple<Integer,Integer>>> pred; //liste adj : pred dans l.get(i) arcs j->i
    //on maintient l'invariant que si (j,c) est dans l.get(i), alors (i,c) est dans pred.get(j)

    //le graphe ne contient aucune boucle
    //les capacités sont non nulles

    ////////////////////////////////////////////////////////////
    // constructeurs et méthodes de base
    ////////////////////////////////////////////////////////////

    public Graphe(int n){
        l = new ArrayList<>();
        for(int i = 0;i<n;i++){
            l.add(new ArrayList<>());
        }
        pred = new ArrayList<>();
        for(int i = 0;i<n;i++){
            pred.add(new ArrayList<>());
        }
    }

    public Graphe(Graphe g){
        l = new ArrayList<>();
        for(int i = 0;i<g.getN();i++){
            l.add(new ArrayList<>());
            for(Couple<Integer,Integer> c:g.l.get(i)) {
                l.get(i).add(new Couple<>(c));
            }
        }
        pred = new ArrayList<>();
        for(int i = 0;i<g.getN();i++){
            pred.add(new ArrayList<>());
            pred.get(i).addAll(g.pred.get(i));
        }

    }


    public Graphe(int n, Graphe g){

            //prérequis n >= g.getN();
            //action : créer le graph this à n sommets, puis y ajoute tous les arcs de g

        this(n);
        for(int i = 0;i<g.getN();i++){
            l.get(i).addAll(g.l.get(i));
        }
        for(int i = 0;i<g.getN();i++){
            pred.get(i).addAll(g.pred.get(i));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Graphe)) return false;
        Graphe graphe = (Graphe) o;
        if (graphe.getN()!=getN()) return false;

        boolean ok = true;
        int i = 0;
        while(i < getN() && ok){
            ok = l.get(i).containsAll(graphe.l.get(i)) && graphe.l.get(i).containsAll(l.get(i))  && pred.get(i).containsAll(graphe.pred.get(i)) && graphe.pred.get(i).containsAll(pred.get(i)) ;
            i++;
        }
        return ok;
    }

    @Override
    public int hashCode() {
        return Objects.hash(l, pred);
    }



    public int getValMaxArc(){
        int max = 0;
        for(ArrayList<Couple<Integer,Integer>> liste : l){
            for(Couple<Integer,Integer> c : liste){
                max =Math.max(max,c.getElement2());
            }
        }
        return max;
    }

    public int getN(){
        return l.size();
    }
    public String toString(){
        String res = "\n successeurs :\n";
        for(int i = 0;i<l.size();i++){
            res+="l["+i+"] : " + l.get(i)+"\n";
        }
	res+="\n predecesseurs :\n";
	for(int i = 0;i<pred.size();i++){
            res+="pred["+i+"] : " + pred.get(i)+"\n";
        }

        return res;
    }


    ////////////////////////////////////////////////////////////
    // méthodes relatives aux voisins
    ////////////////////////////////////////////////////////////


    /**
     *
     * @param i
     * @param j
     * @return null si pas d'arc i->j, et le couple c encodant l'arc sinon
     */
    private Couple<Integer,Integer> isVoisinSortantAux(int i, int j){
        Couple<Integer,Integer> res;
        for(Couple<Integer,Integer> c: l.get(i)){
            if(c.getElement1()==j){
                return c;
            }
        }
        return null;
    }
    private Couple<Integer,Integer> isVoisinEntrantAux(int i, int j){
        Couple<Integer,Integer> res;
        for(Couple<Integer,Integer> c: pred.get(j)){
            if(c.getElement1()==i){
                return c;
            }
        }
        return null;
    }



    public void set(int i, int j, int v){

        if(i!=j) {
            Couple<Integer, Integer> c = isVoisinSortantAux(i, j);
            if (c != null) { //alors il y avait arc i->j de capacité non nulle
                //et l'arc est encodé 2 fois, il faut mettre à jour sa capacité partout
                Couple<Integer, Integer> c2 = isVoisinEntrantAux(i, j);
                assert(c2!=null);
                if (v != 0) {
                    c.setElement2(v);
                    c2.setElement2(v);
                } else {
                    int card = l.get(i).size();
                    l.get(i).remove(c);
                    assert (l.get(i).size() == card - 1);

                    int cardpred = pred.get(j).size();
                    pred.get(j).remove(c2);
                    assert (pred.get(j).size() == cardpred - 1);

                }
            } else {
                assert(isVoisinEntrantAux(i,j)==null);
                if (v != 0) {
                    l.get(i).add(new Couple<>(j, v));
                    pred.get(j).add(new Couple<>(i, v));
                }
            }
        }
    }

    

    public int get(int i, int j) {

        Couple<Integer, Integer> c = isVoisinSortantAux(i, j);
        if (c != null) {
            return c.getElement2();
        } else {
            return 0;
        }
    }


    public ArrayList<Integer> getVoisinsSortant(int i){
        ArrayList<Integer> res = new ArrayList<>();
        for(Couple<Integer,Integer> c: l.get(i)){
            res.add(c.getElement1());
        }
        return res;
    }

    public ArrayList<Integer> getVoisinsEntrants(int i){
        ArrayList<Integer> res = new ArrayList<>();
        for(Couple<Integer,Integer> c: pred.get(i)){
            res.add(c.getElement1());
        }
        return res;
    }
}
