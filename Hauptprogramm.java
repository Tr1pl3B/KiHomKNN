import java.io.File;
import java.io.IOException;


public class Hauptprogramm {

	public static void main(String[] args) throws IOException {		
		
		//1. Trainingsdaten einlesen
		Einlesen.einlesenVorlesungsbeispiele(new File("src/wetter.txt"));
				
		//2. Netz aufbauen
		Perzeptron p = new Perzeptron();
		
		//3. Netz trainieren
		p.trainieren(100001, 0.1);
		
		//4. Netz evaluieren
		p.evaluieren();
		//branch GewichteAktualisieren
	}
}
