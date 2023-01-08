
public class Perzeptron {
    double[] w; 	   //Gewichte, fuer jeden Eingang ein Gewicht (incl. BIAS-GEWICHT)
    int[] reihenfolge; //zufaellige Reihenfolge fuer die Auswahl von Mustern
	
	Perzeptron(){		
		w           = new double[Einlesen.n];
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
				int out      = outBerechnen(Einlesen.x[musterNr]);
				int delta    = fehlerBerechnen(Einlesen.y[musterNr], out);
				gewichteAnpassen(delta, alpha, Einlesen.x[musterNr]);
				anzahlfehler+=Math.abs(delta);
			}
			System.out.println("Epoche: " + epoche + " Fehler: " + anzahlfehler);
			if(anzahlfehler == 0)break;		
		}
	}
	
	public int fehlerBerechnen(int y, int out) {
		return y-out;
	}
	
	public int outBerechnen(double[] xWerte) {
		double in = skalarProdukt(w, xWerte);
		int out   = aktivierungsFunktionSchwellwert(in);
		return out;
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
	
	
	public void gewichteAnpassen(double delta, double alpha, double[] x) {
		//Uebungsaufgabe 1
		//Hier euren Sourcecode einfuegen
		for(int i = 0; i < w.length; i++){
			//System.out.println(i+" alpha:"+alpha+" gewicht:"+w[i]+" x[i]:"+x[i]+" delta:"+delta+" vor aktualisierung");
			w[i] = w[i] + alpha * x[i] * delta;
			//System.out.println(i+" "+w[i] + " nach aktualisierung");
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
				int out = outBerechnen(x);
				//if(z==90 && s==20)
				System.out.print(out);
			}
			System.out.println();
		}
	}

	//Die out berechnen Funktion mit dem Sigmoid
	public double outBerechnenMitSig(double[] xWerte){
		double in = skalarProdukt(w, xWerte);
		// out(x) = sig(in(x))
		// sig (x) = 1/(1+e^(-x))
		double sig = 1/(1+Math.exp(-in));
		return sig;
	}

		
		
}
