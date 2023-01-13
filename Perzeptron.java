import java.util.ArrayList;

public class Perzeptron {
    double[] w; 	   //Gewichte, fuer jeden Eingang ein Gewicht (incl. BIAS-GEWICHT)
    int[] reihenfolge; //zufaellige Reihenfolge fuer die Auswahl von Mustern
	
	Perzeptron(){		
		w           = new double[9];
		reihenfolge = new int[Einlesen.m]; 
		for(int i=0;i<reihenfolge.length;i++) 
			reihenfolge[i] = i;
	}
	
	
	public void trainieren(int epochen, double alphaStart) {
		double alpha  = alphaStart;
		
		//Initialisierung der Gewichte
		for(int i=0;i<w.length;i++) {
			w[i] = Math.random();
			if(Math.random()<0.5)
				w[i]*=-1.0;
		}
		
		//Trainieren der Gewichte
		for(int epoche=0;epoche<epochen;epoche++) {
			musterStochastischAuswaehlen();
			int anzahlfehler = 0;
			for(int i=0;i<reihenfolge.length;i++) {//fuer jedes Muster m
				int musterNr = reihenfolge[i];
				ArrayList<Schicht> schichtListe = outBerechnen(Einlesen.x[musterNr]);
				schichtListe = fehlerBerechnen(schichtListe);
				gewichteAnpassen(alpha, schichtListe);
				
			}
			System.out.println("Epoche: " + epoche + " Fehler: " + anzahlfehler);
			if(anzahlfehler == 0)break;		
		}
	}
	
	public ArrayList<Schicht> fehlerBerechnen(ArrayList<Schicht> schichtListe) {
		Schicht schicht;
		for(int schichtNr = schichtListe.size()-1; schichtNr > 0; schichtNr--){
			schicht = schichtListe.get(schichtNr);
			if(schicht.knoteListe.size() > 1){
				for(int knotenNr = 1; knotenNr < schicht.knoteListe.size(); knotenNr++) {
					double in = schicht.knoteListe.get(knotenNr).in;
					int offset = 1;
					double weight = w[w.length - 1 - offset];
					offset--;
					double deltaParent = schichtListe.get(schichtNr+1).knoteListe.get(0).delta;
					double delta = abgeleiteterSigi(in) * (weight*deltaParent);
					schichtListe.get(schichtNr).knoteListe.get(knotenNr).delta = delta;
				}
			} else {
				for(int knotenNr = 0; knotenNr < schicht.knoteListe.size(); knotenNr++) {
					double in = schicht.knoteListe.get(knotenNr).in;
					double out = schicht.knoteListe.get(knotenNr).out;
					double delta = abgeleiteterSigi(in)*(1-out);
					schichtListe.get(schichtNr).knoteListe.get(knotenNr).delta = delta;
				}
			}

		}
		return schichtListe;
	}
	
	public ArrayList<Schicht> outBerechnen(double[] xWerte) {
		ArrayList<Schicht> schichtListe = new ArrayList<>();
		Schicht schicht = new Schicht();
		schicht.knoteListe.add(new Knoten(1, 1));
		schicht.knoteListe.add(new Knoten(xWerte[0]));
		schicht.knoteListe.add(new Knoten(xWerte[1]));
		schichtListe.add(schicht);
		schicht = new Schicht();
		schicht.knoteListe.add(new Knoten(1, 1));
		for(int knoten = 0; knoten < 2; knoten++){
			double in = 0.0;
			for(int altKnoten = 0; altKnoten < 3; altKnoten ++){
				in += schichtListe.get(0).knoteListe.get(altKnoten).in * w[knoten*3+altKnoten];
			}
			double out = outBerechnenMitSig(in);
			schicht.knoteListe.add(new Knoten(in, out));
		}
		schichtListe.add(schicht);
		schicht = new Schicht();
		double in = 0.0;
		for(int altKnoten = 0; altKnoten < 3; altKnoten ++){
			in += schichtListe.get(0).knoteListe.get(altKnoten).in * w[2*3+altKnoten];
		}
		double out = outBerechnenMitSig(in);
		schicht.knoteListe.add(new Knoten(in, out));
		schichtListe.add(schicht);

		return schichtListe;
	}
	
	
	public void musterStochastischAuswaehlen() {
		for(int i=0;i<reihenfolge.length;i++) {
			int j = (int)(reihenfolge.length*Math.random());
			int a = reihenfolge[i];
			int b = reihenfolge[j];
			reihenfolge[i] = b;
			reihenfolge[j] = a;
		}
	}
	
	public void gewichteAnpassen(double alpha, ArrayList<Schicht> schichtListe) {
		//Uebungsaufgabe 1
		//Hier euren Sourcecode einfuegen
		int gewichtNr = w.length-1;
		for (int schichtNr = schichtListe.size()-1; schichtNr > 0; schichtNr--){
			int limit = (schichtNr == schichtListe.size()-1) ? 0 : 1;
			for (int knotenNr = schichtListe.get(schichtNr).knoteListe.size()-1; knotenNr >= limit; knotenNr--) {
				for (int tochterKnoten = schichtListe.get(schichtNr-1).knoteListe.size()-1; tochterKnoten >= 0; tochterKnoten--) {
					double out = schichtListe.get(schichtNr - 1).knoteListe.get(tochterKnoten).out;
					double delta = schichtListe.get(schichtNr).knoteListe.get(knotenNr).delta;
					w[gewichtNr] = w[gewichtNr] + alpha * out * delta;
					gewichtNr--;
				}

			}
		}
	}
	
	public double skalarProdukt(double[] w, double[] x) {
		//Uebungsaufgabe 2
		double result = 0.0;
		//Hier euren Sourcecode einfuegen
		for(int i = 0; i < w.length; i++){
			result = w[i] * x[i] + result;
		}

		return result;
	}

	
	private int aktivierungsFunktionSchwellwert(double x) {
		if(x<0)return 0;
		else   return 1;
	}	
	
	public void evaluieren() {
		double[] x = new double[3];
		for(int z=100;z>=0;z=z-1) {
			for(int s=0;s<=100;s=s+1) {
				x[0] = 1.;
				x[1] = (double)(s/100.);
				x[2] = (double)(z/100.);
				ArrayList<Schicht> schichtListe = outBerechnen(x);
				int out = aktivierungsFunktionSchwellwert(schichtListe.get(2).knoteListe.get(0).out);
				//if(z==90 && s==20)
				System.out.print(out);
			}
			System.out.println();
		}
	}

	//Die out berechnen Funktion mit dem Sigmoid
	public double outBerechnenMitSig(double in){
		// out(x) = sig(in(x))
		// sig (x) = 1/(1+e^(-x))
		double sig = 1/(1+Math.exp(-in));
		return sig;
	}

	//albleitung sig e^x/((e^x+1)^2)
	public double abgeleiteterSigi(double in) {
		double abgSigi = Math.exp(in)/(Math.pow((Math.exp(in) + 1), 2));
		return abgSigi;
	}
}
