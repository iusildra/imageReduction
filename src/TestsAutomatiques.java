import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.*;

class TestsAutomatiques {

    public static void main(String[] args) {

        double note = 0;
        note += runTest(TestsAutomatiques::testCreerImgLigne, "testCreerImgLigne", 1); // publique
        note += runTest(TestsAutomatiques::testCreerInstanceSegmentationLigne, "testCreerInstanceSegmentationLigne", 1); // publique
        note += runTest(TestsAutomatiques::testInstanceSegmentationGraphe1, "testInstanceSegmentationGraphe1", 1); // publique
        note += runTest(TestsAutomatiques::testReseau1, "testReseau1", 2); // publique
        note += runTest(TestsAutomatiques::testSolveInstanceSegmentationGraphe1, "testSolveInstanceSegmentationGraphe1",
                1); // publique
        note += runTest(TestsAutomatiques::testSolveInstanceSegmentation1, "testSolveInstanceSegmentation1", 1); // publique

        System.out.println("fin des tests : note = " + note);

    }

    public static double runTest(Callable<Double> r, String s, int bareme) {
        ExecutorService executorService = Executors.newSingleThreadExecutor(); // si on submit à nouveau sans re créer
                                                                               // ça timeout aussi pour tests suivants
        Future<Double> future = executorService.submit(r);
        double note = 0;
        try {
            // Wait for at most 5 seconds until the result is returned
            note += future.get(1L, TimeUnit.SECONDS) * bareme; // get renvoie entre 0 et 1
            System.out.println("****************************************************");
            System.out.println(s + " terminé, note: " + note + "/" + bareme);
            System.out.println("****************************************************");

        } catch (TimeoutException e) {
            System.out.println("****************************************************");
            System.out.println(s + " timeout");
            System.out.println("****************************************************");
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("****************************************************");
            System.out.println(s + " erreur " + e.getMessage());
            System.out.println("****************************************************");
        }
        executorService.shutdownNow();
        return note;

    }

    private static double testCreerImgLigne() {
        Img im = Main.creerImgLigne();
        if (im.get(0, 0) == 255
                && im.get(1, 0) == 200
                && im.get(2, 0) == 100)
            return 1;
        else {
            return 0;
        }
    }

    private static double testCreerInstanceSegmentationLigne() {
        InstanceSegmentation iseg = Main.creerInstanceLigne();
        Img im = iseg.getImg();
        if (im.get(0, 0) == 255
                && im.get(1, 0) == 200
                && im.get(2, 0) == 100
                && iseg.getF().contains(new Couple<Integer, Integer>(2, 0))
                && iseg.getB().contains(new Couple<Integer, Integer>(0, 0)))
            return 1;
        else {
            return 0;
        }
    }
    /////////////////////////////////////////////////////////////////
    //////// tests Iseg -> ISegGraphe
    /////////////////////////////////////////////////////////////////

    private static double testInstanceSegmentationGraphe1() {
        // test du constructeur InstanceSegmentationGraphe(InstanceSegmentation iseg)
        InstanceSegmentation iseg = Main.creerInstanceLigne();
        InstanceSegmentationGraphe isegG = new InstanceSegmentationGraphe(iseg);
        Graphe g = new Graphe(3);
        int p1 = InstanceSegmentationGraphe.penalite(iseg.getImg().get(0, 0), iseg.getImg().get(1, 0));
        int p2 = InstanceSegmentationGraphe.penalite(iseg.getImg().get(1, 0), iseg.getImg().get(2, 0));
        g.set(0, 1, p1);
        g.set(1, 0, p1);
        g.set(1, 2, p2);
        g.set(2, 1, p2);

        if (isegG.getGraphe().equals(g)) {
            return 1;
        } else {
            return 0;
        }
    }

    /////////////////////////////////////////////////////////////////
    //////// tests IsegGraphe -> Reseau
    /////////////////////////////////////////////////////////////////
    private static double testReseau1() {
        // test du constructeur Reseau (InstanceSegmentationGraphe isegG)

        Graphe g = creerPetitCarre();
        int pixB = 0;
        int pixF = 2;
        ArrayList<Integer> b = new ArrayList<>();
        ArrayList<Integer> f = new ArrayList<>();
        b.add(pixB);
        f.add(pixF);
        InstanceSegmentationGraphe isegG = new InstanceSegmentationGraphe(g, f, b);

        Reseau r = new Reseau(isegG);
        double res = 0;
        int s = r.getS();
        int t = r.getT();
        if (r.getN() == g.getN() + 2
                && s == g.getN()
                && t == g.getN() + 1) {
            res = res + 0.5;
        }
        if ((r.get(s, 0) == Integer.MAX_VALUE)
                && (r.get(s, 2) == 0) && (r.get(0, t) == 0)
                && (r.get(2, t) == Integer.MAX_VALUE)
                && r.get(3, 2) == g.get(3, 2)) {
            res = res + 0.5;
        }
        return res;

    }

    /////////////////////////////////////////////////////////////////
    //////// tests traduction Reseau -> InstanceSegmentationGraphe
    /////////////////////////////////////////////////////////////////

    private static double testSolveInstanceSegmentationGraphe1() {
        InstanceSegmentationGraphe isegG = creerInstanceSegmentationGraphePetitCarre();
        ArrayList<Integer> B = isegG.calculOpt();
        ArrayList<Integer> B2 = new ArrayList<>();
        B2.add(0);
        B2.add(1);
        if (egalEnsembliste(B, B2)) {
            return 1;
        } else {
            return 0;
        }

    }

    /////////////////////////////////////////////////////////////////
    //////// tests traduction InstanceSegmentationGraphe -> InstanceSegmentation
    /////////////////////////////////////////////////////////////////

    private static double testSolveInstanceSegmentation1() {
        InstanceSegmentation iseg = creerInstancePetitCarre();
        ArrayList<Couple<Integer, Integer>> B = iseg.resoudre();
        ArrayList<Couple<Integer, Integer>> B2 = new ArrayList<>();
        B2.add(new Couple<Integer, Integer>(0, 0));
        B2.add(new Couple<Integer, Integer>(1, 0));
        if (egalEnsembliste(B, B2)) {
            return 1;
        } else {
            return 0;
        }

    }

    /////////////////////////////////////////////////////////////////
    //////// méthodes utiles pour les tests
    /////////////////////////////////////////////////////////////////

    public static InstanceSegmentation creerInstancePetitCarre() {
        int[][] data = new int[2][2];
        data[0][0] = 255;
        data[1][0] = 200;
        data[0][1] = 50;
        data[1][1] = 0;
        Img im = new Img(data);

        Couple<Integer, Integer> pixB = new Couple<>(0, 0);
        Couple<Integer, Integer> pixF = new Couple<>(1, 1);
        ArrayList<Couple<Integer, Integer>> b = new ArrayList<>();
        ArrayList<Couple<Integer, Integer>> f = new ArrayList<>();
        b.add(pixB);
        f.add(pixF);
        return new InstanceSegmentation(im, f, b);

    }

    public static boolean egalEnsembliste(ArrayList<?> a1, ArrayList<?> a2) {
        return a1.containsAll(a2) && a2.containsAll(a1);
    }

    private static Graphe creerPetitCarre() {
        int[][] data = new int[2][2];
        data[0][0] = 255;
        data[1][0] = 200;
        data[0][1] = 50;
        data[1][1] = 0;
        // Img im = new Img(data);
        new Img(data);
        int p1 = InstanceSegmentationGraphe.penalite(255, 200);
        int p2 = InstanceSegmentationGraphe.penalite(255, 50);
        int p3 = InstanceSegmentationGraphe.penalite(0, 50);
        int p4 = InstanceSegmentationGraphe.penalite(0, 200);

        Graphe g = new Graphe(4);
        g.set(0, 1, p1);
        g.set(0, 2, p2);
        g.set(1, 0, p1);
        g.set(1, 3, p4);
        g.set(2, 3, p3);
        g.set(2, 0, p2);
        g.set(3, 2, p3);
        g.set(3, 1, p4);
        return g;
    }

    public static InstanceSegmentationGraphe creerInstanceSegmentationGraphePetitCarre() {

        int pixB = 0;
        int pixF = 3;
        ArrayList<Integer> b = new ArrayList<>();
        ArrayList<Integer> f = new ArrayList<>();
        b.add(pixB);
        f.add(pixF);
        Graphe g = creerPetitCarre();
        InstanceSegmentationGraphe isegG = new InstanceSegmentationGraphe(g, f, b);
        return isegG;
    }

}
