//code importé de https://www.enseignement.polytechnique.fr/informatique/profs/Philippe.Chassignet/PGM/pgm_java.html

import java.io.*;

public class Pixmap {

  private static String MAGIC_PGM = "P5\n";

  private final int width;
  private final int height;
  private final int size;

  private byte[] data;

  public Pixmap(int w, int h) {
    width = w;
    height = h;
    size = width * height;
  }

  Pixmap(String fileName, String magic) throws IOException {
    PixmapReader reader = new PixmapReader(fileName);
    if (!reader.matchKey(magic)) {
      reader.close();
      throw new IOException(fileName + " : wrong magic number");
    }
    reader.skipComment('#');
    width = reader.getInt();
    height = reader.getInt();
    size = width * height;
    reader.skipLine();
    reader.skipComment('#');
    reader.skipLine();

    data = reader.loadData(size);
    reader.close();
  }

  public Pixmap(String fileName) throws IOException {
    this(fileName, MAGIC_PGM);
  }

  public int getW() {
    return width;
  }

  public int getH() {
    return height;
  }

  final void write(String fileName, String magic, byte[] buffer) {
    try {
      PixmapWriter writer = new PixmapWriter(fileName);
      writer.put(magic);
      writer.put("#\n");
      writer.put(width + " " + height + "\n255\n");
      writer.write(buffer);
      writer.close();
      System.err.println("'" + fileName + "' wrote");
    } catch (IOException e) {
      System.err.println("can't write '" + fileName + "'");
    }
  }

  /*
   * public abstract byte[] getBytes();
   */
  public void write(String fileName) {
    write(fileName, MAGIC_PGM, data);
  }

  public static int intValue(byte b) {
    if (b < 0)
      return b + 256;
    else
      return b;
  }

  public static byte byteValue(int v) {
    // 0 <= v <= 255
    if (v > 128)
      return (byte) (v - 256);
    else
      return (byte) v;
  }

  public int get(int i, int j) {
    // retourne entier entre 0 (blanc) et 255 (noir)
    return intValue(data[j * getW() + i]);
  }

  public void set(int i, int j, int v) {
    assert ((v >= 0) && (v <= 255));// retourne entier entre 0 (blanc) et 255 (noir)
    data[j * getW() + i] = byteValue(v);
  }

  public void setData() {
    data = new byte[size];
  }

}
