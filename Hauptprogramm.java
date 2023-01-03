import java.io.File;
import java.io.IOException;


public class Hauptprogramm {

	public static void main(String[] args) throws IOException {		
		
		//1. Trainingsdaten einlesen
		Einlesen.einlesenVorlesungsbeispiele(new File("data\\wetter.txt"));
				
		//2. Netz aufbauen
		Perzeptron p = new Perzeptron();
		
		//3. Netz trainieren
		p.trainieren(200, 0.01);
		
		//4. Netz evaluieren
		p.evaluieren();
		//hab den data ordner erstellt, damit es auch richtig läuft
	}
}
