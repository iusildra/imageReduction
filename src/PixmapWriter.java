
import java.io.*;

//code importé de https://www.enseignement.polytechnique.fr/informatique/profs/Philippe.Chassignet/PGM/pgm_java.html
//et légèrement modifié


class PixmapWriter extends FileOutputStream {

  public PixmapWriter(String fileName) throws IOException {
    super(fileName);
  }

  public void put(String data) throws IOException {
    write(data.getBytes());
  }

}


