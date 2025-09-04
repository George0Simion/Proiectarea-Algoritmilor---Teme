import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class P3 {
	/**
	 * Clasa LogInfo stocheaza toate informatiile despre un log:
	 * - pozitia initiala a capetelor (xStart,yStart), (xEnd,yEnd)
	 * - sirul de directii în care se misca busteanul la fiecare pas de timp
	 * - vectorii deplasariX, deplasariY care permit calculul pozitiei busteanului la un moment t
	 * - daca busteanul e orientat vertical sau orizontal si lungimea lui
	 */
	static class LogInfo {
		int xStart, yStart;
		int xEnd, yEnd;
		String directii;       // de exemplu "NNESV..."
		int[] deplasariX, deplasariY;
		boolean esteVertical;  // true daca busteanul este orientat vertical
		int lungime;           // numarul de unitati între cele doua capete

		public LogInfo(int xStart, int yStart, int xEnd, int yEnd, String directii) {
			this.xStart = xStart;
			this.yStart = yStart;
			this.xEnd = xEnd;
			this.yEnd = yEnd;
			this.directii = directii;
			this.esteVertical = (xStart == xEnd);
			this.lungime = esteVertical
					? (yEnd - yStart)
					: (xEnd - xStart);
			calculeazaDeplasari();
		}

		/**
		 * Populeaza deplasariX[t] si deplasariY[t], astfel incat:
		 *   pozitia capatului de start la
		 *   				timpul t = (xStart + deplasariX[t], yStart + deplasariY[t])
		 * 		directiile posibile: N, S, E, V
		 */
		private void calculeazaDeplasari() {
			int T = directii.length();
			deplasariX = new int[T + 1];
			deplasariY = new int[T + 1];

			// la t = 0 nu ne miscam
			deplasariX[0] = 0;
			deplasariY[0] = 0;

			// pentru fiecare pas de timp, adaugam deplasarea corespunzatoare
			for (int t = 1; t <= T; t++) {
				char d = directii.charAt(t - 1);
				deplasariX[t] = deplasariX[t - 1];
				deplasariY[t] = deplasariY[t - 1];
				switch (d) {
					case 'N':
						deplasariY[t]++;
						break;
					case 'S':
						deplasariY[t]--;
						break;
					case 'E':
						deplasariX[t]++;
						break;
					case 'V':
						deplasariX[t]--;
						break;
					default:
						break;
				}
			}
		}
	}

	/**
	 * Clasa StareNod reprezinta un nod în spatiul de stare al lui Robin Hood:
	 * - energieConsumata = energia totala consumata pâna în acest punct
	 * - pasTimp = pasul de timp la care se afla Robin
	 * - indexLog = indicele busteanului pe care se afla
	 * - offsetLog = deplasarea de la capatul de start al busteanului
	 * - parinte = referinta catre starea anterioara (pentru reconstructia traiectoriei)
	 * - actiuneExecutata = actiunea („H”, „N”, „S”, „E”, „V” sau „J j”)
	 * Implementam Comparable pentru a folosi PriorityQueue ordonat dupa energia minima.
	 */
	static class StareNod implements Comparable<StareNod> {
		int energieConsumata;
		int pasTimp;
		int indexLog;
		int offsetLog;
		StareNod parinte;
		String actiuneExecutata;

		public StareNod(int energie, int timp, int idxBustean, int offset,
						StareNod parinte, String actiune) {
			this.energieConsumata = energie;
			this.pasTimp = timp;
			this.indexLog = idxBustean;
			this.offsetLog = offset;
			this.parinte = parinte;
			this.actiuneExecutata = actiune;
		}

		@Override
		public int compareTo(StareNod altaStare) {
			// ordonam dupa energia consumata: prioritate starii cu energie mai mica
			return Integer.compare(this.energieConsumata, altaStare.energieConsumata);
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader in  = new BufferedReader(new FileReader("p3.in"));
		BufferedWriter out = new BufferedWriter(new FileWriter("p3.out"));

		// citim timpul disponibil lui robin si numarul de busteni
		String[] tokens = in.readLine().split(" ");
		final int timpDisponibil = Integer.parseInt(tokens[0]);
		final int numarBusteani = Integer.parseInt(tokens[1]);

		// coordonatele lui Maid Marian
		tokens = in.readLine().split(" ");
		int xMaidMarian = Integer.parseInt(tokens[0]);
		int yMaidMarian = Integer.parseInt(tokens[1]);

		// costurile de energie pentru Robin:
		tokens = in.readLine().split(" ");
		int E1 = Integer.parseInt(tokens[0]);
		int E2 = Integer.parseInt(tokens[1]);
		int E3 = Integer.parseInt(tokens[2]);

		// pozitiile capetelor fiecarui bustean
		int[][] coordLogs = new int[numarBusteani][4];
		for (int i = 0; i < numarBusteani; i++) {
			tokens = in.readLine().split(" ");
			for (int j = 0; j < 4; j++) {
				coordLogs[i][j] = Integer.parseInt(tokens[j]);
			}
		}

		// directiile bustenilor
		String[] dirLogs = new String[numarBusteani];
		for (int i = 0; i < numarBusteani; i++) {
			dirLogs[i] = in.readLine().trim();
		}

		// initializam clasa de informatii despre busteni
		List<LogInfo> logs = new ArrayList<>();
		for (int i = 0; i < numarBusteani; i++) {
			int[] capete = coordLogs[i];
			logs.add(new LogInfo(
					capete[0], capete[1],
					capete[2], capete[3],
					dirLogs[i]));
		}

		// vectorul de distante -> energia minima pe care robin o consuma ca sa ajunga in
		//						starea curenta -> la timpul t, busteanul i, si offset-ul
		//						d de la startul busteanului
		// dist[pasTimp][indexLog][offsetLog]
		int[][][] dist = new int[timpDisponibil + 1][numarBusteani][];
		for (int i = 0; i < numarBusteani; i++) {
			int lungimeLog = logs.get(i).lungime;
			for (int t = 0; t <= timpDisponibil; t++) {
				dist[t][i] = new int[lungimeLog + 1];
				Arrays.fill(dist[t][i], Integer.MAX_VALUE);
			}
		}

		/* incepem algoritmul lui Dijkstra */

		PriorityQueue<StareNod> que = new PriorityQueue<>();
		// adaugam starea initiala: t=0, bustean 1 cu index 0, offset=0, energie=0
		dist[0][0][0] = 0;
		que.add(new StareNod(0, 0, 0, 0, null, null));

		StareNod solutie = null;

		while (!que.isEmpty()) {
			// scoatem din coada
			StareNod curenta = que.poll();

			// daca am gasit o cale mai buna deja, ignoram
			if (curenta.energieConsumata
					> dist[curenta.pasTimp][curenta.indexLog][curenta.offsetLog]) {
				continue;
			}

			// calculam poztia lui robin in starea curenta
			LogInfo logCurent = logs.get(curenta.indexLog);
			int xRobin = logCurent.xStart + logCurent.deplasariX[curenta.pasTimp];
			int yRobin = logCurent.yStart + logCurent.deplasariY[curenta.pasTimp];

			// adaugam offset-ul pe lungimea logului in functie de orientare
			if (logCurent.esteVertical) {
				yRobin += curenta.offsetLog;
			} else {
				xRobin += curenta.offsetLog;
			}

			//  daca Robin a ajuns la Maid Marian -> oprim Dijkstra
			if (xRobin == xMaidMarian && yRobin == yMaidMarian) {
				solutie = curenta;
				break;
			}

			// daca am epuizat timpul disponibil -> oprim
			if (curenta.pasTimp >= timpDisponibil) {
				continue;
			}

			/* adaugam urmatorul pas in coada -> cele 3 actiuni pe care poate sa le faca  */

			int pasUrmator = curenta.pasTimp + 1;

			// actiunea H: stat pe loc -> cost E1
			int energieH = curenta.energieConsumata + E1;
			if (energieH < dist[pasUrmator][curenta.indexLog][curenta.offsetLog]) {
				dist[pasUrmator][curenta.indexLog][curenta.offsetLog] = energieH;
				que.add(new StareNod(
						energieH, pasUrmator,
						curenta.indexLog,
						curenta.offsetLog,
						curenta, "H"));
			}

			// miscare pe bustean, updatam offset-ul -> cost E2
			if (logCurent.esteVertical) {
				// vertical: merge N sau S
				for (int delta = -1; delta <= 1; delta += 2) {
					int nouOffset = curenta.offsetLog + delta;
					if (nouOffset < 0 || nouOffset > logCurent.lungime) {
						continue;
					}

					char directiePas = (delta == 1 ? 'N' : 'S');
					int energiePas = curenta.energieConsumata + E2;

					// daca muchia are un cost mai mic o relaxam si adaugam noua stare in coada
					if (energiePas < dist[pasUrmator][curenta.indexLog][nouOffset]) {
						dist[pasUrmator][curenta.indexLog][nouOffset] = energiePas;
						que.add(new StareNod(
								energiePas, pasUrmator,
								curenta.indexLog,
								nouOffset,
								curenta, String.valueOf(directiePas)));
					}
				}
			} else {
				// orizontal: E sau V
				for (int delta = -1; delta <= 1; delta += 2) {
					int nouOffset = curenta.offsetLog + delta;
					if (nouOffset < 0 || nouOffset > logCurent.lungime) {
						continue;
					}

					char directiePas = (delta == 1 ? 'E' : 'V');
					int energiePas = curenta.energieConsumata + E2;

					// daca muchia are un cost mai mic o relaxam si adaugam noua stare in coada
					if (energiePas < dist[pasUrmator][curenta.indexLog][nouOffset]) {
						dist[pasUrmator][curenta.indexLog][nouOffset] = energiePas;
						que.add(new StareNod(
								energiePas, pasUrmator,
								curenta.indexLog,
								nouOffset,
								curenta, String.valueOf(directiePas)));
					}
				}
			}

			// actiunea J: salt pe alt bustean -> cost E3
			for (int j = 0; j < logs.size(); j++) {
				if (j == curenta.indexLog) {
					continue;
				}
				LogInfo nextLog = logs.get(j);

				// coord capetelor logului j la timpul curent
				int xStartAlt = nextLog.xStart + nextLog.deplasariX[curenta.pasTimp];
				int yStartAlt = nextLog.yStart + nextLog.deplasariY[curenta.pasTimp];
				int xEndAlt = nextLog.xEnd + nextLog.deplasariX[curenta.pasTimp];
				int yEndAlt = nextLog.yEnd + nextLog.deplasariY[curenta.pasTimp];

				// verificam daca coord lui robin se afla pe busteanul j
				boolean intersecteaza;
				if (nextLog.esteVertical) {
					// pt log vertical, x este fix si y trebuie sa fie intre capete
					intersecteaza = (xRobin == xStartAlt
							&& yRobin >= yStartAlt
							&& yRobin <= yEndAlt);
				} else {
					// pt log orizontal, y este fix si x trebuie sa fie intre capete
					intersecteaza = (yRobin == yStartAlt
							&& xRobin >= xStartAlt
							&& xRobin <= xEndAlt);
				}
				if (!intersecteaza) {
					continue;
				}

				// calculam distanta de la inceputul logului pana la punctul de salt
				int offsetLaSalt;
				if (nextLog.esteVertical) {
					offsetLaSalt = yRobin - yStartAlt;
				} else {
					offsetLaSalt = xRobin - xStartAlt;
				}
				// nu putem sarii daca offset ul este in afara logului
				if (offsetLaSalt < 0 || offsetLaSalt > nextLog.lungime) {
					continue;
				}

				int energieJ = curenta.energieConsumata + E3;
				if (energieJ < dist[pasUrmator][j][offsetLaSalt]) {
					dist[pasUrmator][j][offsetLaSalt] = energieJ;
					// notam actiunea "J index bustean 1-based>"
					que.add(new StareNod(
							energieJ, pasUrmator, j, offsetLaSalt,
							curenta, "J " + (j + 1)));
				}
			}
		}

		// afisare
		if (solutie == null) {
			// nu exista cale pâna la dest în timpul disponibil
			out.write("-1\n");
			
		} else {
			// afisam energia minima necesara
			out.write(solutie.energieConsumata + "\n");
			
			// reconstruim lista de actiuni
			List<String> listaActiuni = new ArrayList<>();
			for (StareNod s = solutie; s.parinte != null; s = s.parinte) {
				listaActiuni.add(s.actiuneExecutata);
			}
			Collections.reverse(listaActiuni);

			out.write(listaActiuni.size() + "\n");
			for (String act : listaActiuni) {
				out.write(act + "\n");
			}
		}

		in.close();
		out.close();
	}
}
