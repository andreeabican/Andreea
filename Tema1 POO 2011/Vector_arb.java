
public class Vector_arb {
	Arbore[] f;
	
	/**
	 * 
	 * @param n - numarul de elemente pe care o sa-l aiba vectorul cu elemente de tip Arbore
	 */
	Vector_arb(int n) {
		f = new Arbore[n];
	}
	/**
	 * 
	 * @param lungime - numarul de elemente ale vectorului de frecventa
	 * @return intoarce arborele Huffman construit pe  baza vectorului de frecventa
	 */
	public Arbore huffman(int lungime) {
		//construirea arborelui Huffman
				int l = lungime;
				int i;
				Arbore arbore = new Arbore();
				//System.out.println(l);
				while (l>=0) {
					//initializare arbore stang si arbore drept
					Arbore a1 = new Arbore(f[0].n, f[0].c, f[0].s, f[0].d, f[0].p);
					Arbore a2 = new Arbore(f[1].n, f[1].c, f[1].s, f[1].d, f[1].p);
					//crearea arborelui parinte, care ar ca fii arborele stang si arborele drept
					Arbore parinte = new Arbore(f[0].n + f[1].n, f[0].c + f[1].c, a1, a2, null);
					//Cautare loc de inserare in vectorul de frecventa a noului element format din reuninunea celor doua elemente
					for (i=0; i<l; i++) {
						if (parinte.n <= f[i].n) break;
					}
					
					//Adaugare parinte in vectorul de frecventa, in cazul in care se adauga pe pozitia 0
					if (i<=2) {
						f[0] = parinte;
						//Deplasarea elementelor spre stanga cu o pozitie
						for (int j=1; j<l-1; j++) {
							f[j] = f[j+1];
						}
						lungime--;
						l--;
					}
					else
						//adaugarea la sfarsit a elementului nou creat
						if (i==l) {
							
							for (int j=0; j<l-2; j++) {
								f[j] = f[j+2];
							}
							f[l-2] = parinte;
							l--;
							lungime--;
							if (l==2) l=0;
						}
					
					//inserare in vectorul de frecventa, in interior
						else {
						if (i>=2) {
							//Deplasare spre stanga cu doua pozitii a elementelor de la stanga pozitiei unde vrem sa innseram
							for (int j=0; j<i-2; j++) {
								f[j] = f[j+2];
							}
							//inserarea noului element
							f[i-2] = parinte;
							//Deplasarea spre stanga cu o pozitie a elementelor de la dreapta pozitiei unde am inserat
							for (int j=i-1; j<l-1; j++) {
								f[j] = f[j+1];
								}
							l--;
							lungime--;
							
						}
						}
				arbore = parinte;
				}
				return arbore;
	}

}
