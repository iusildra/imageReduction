
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

public class Main {

	public static Img creerImgLigne() {
		int[][] tab = { { 255 }, { 200 }, { 100 } };
		return new Img(tab);

	}

	public static InstanceSegmentation creerInstanceLigne() {
		ArrayList<Couple<Integer, Integer>> f = new ArrayList<>();
		f.add(new Couple<>(2, 0));
		ArrayList<Couple<Integer, Integer>> b = new ArrayList<>();
		b.add(new Couple<>(0, 0));
		return new InstanceSegmentation(creerImgLigne(), f, b);

	}

	public static Graphe creerPetitGraphe() {
		Graphe g = new Graphe(3);
		g.set(0, 1, 10);
		g.set(1, 2, 20);
		return g;
	}

	public static ArrayList<Integer> testMinCut() {
		var network = new Reseau(5, 0, 4);
		network.set(0, 1, 1);
		network.set(0, 2, 10);
		network.set(1, 2, 6);
		network.set(2, 1, 2);
		network.set(1, 3, 8);
		network.set(2, 4, 2);
		network.set(3, 4, 3);
		return network.coupeMin();
	}

	public static void addPoints(ArrayList<Couple<Integer, Integer>> liste, int i, int j, int size) {
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				liste.add(new Couple<>(i + x, j + y));
			}
		}
	}

	public static void main(String args[]) throws FileNotFoundException, IllegalArgumentException, IOException {
		System.out.println("début Main");

		Img imageFich = new Img("src/main/resources/images/baby_2k.pgm");

		ArrayList<Couple<Integer, Integer>> bbB = new ArrayList<>();
		ArrayList<Couple<Integer, Integer>> bbF = new ArrayList<>();
		int c = 3;

		addPoints(bbB, 0, 0, c);
		addPoints(bbB, 7, 5, c);
		addPoints(bbB, 3, 25, c);
		addPoints(bbB, imageFich.nbColonnes() / 2, 0, c);
		addPoints(bbB, imageFich.nbColonnes() - 5, 11, c);

		addPoints(bbF, imageFich.nbColonnes() / 2, imageFich.nbColonnes() / 2 - 3 * c, c);
		addPoints(bbF, imageFich.nbColonnes() / 2, imageFich.nbColonnes() / 2, c);
		addPoints(bbF, imageFich.nbColonnes() / 2 - 10, imageFich.nbColonnes() / 2 + 3 * c, c);
		addPoints(bbF, imageFich.nbColonnes() / 2 + 5, imageFich.nbColonnes() / 2 + 3 * c, c);
		addPoints(bbF, imageFich.nbColonnes() / 2 + 5, imageFich.nbColonnes() / 2 + 7 * c, c);

		System.out.println(testMinCut());

		InstanceSegmentation isegFich = new InstanceSegmentation(imageFich, bbF, bbB);
		Img resFich = isegFich.creerImageSegmentee();
		resFich.creerImage("src/main/resources/images/outputbaby_2k.pgm");
	}

}
