import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Regele {
	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(new FileReader("regele.in"));
		PrintWriter out = new PrintWriter("regele.out");

		// citim orasele
		int n = in.nextInt();
		long[] coord = new long[n];
		for (int i = 0; i < n; i++) {
			coord[i] = in.nextLong();
		}

		// citim negustorii
		int q = in.nextInt();
		long[] negustori = new long[q];
		for (int i = 0; i < q; i++) {
			negustori[i] = in.nextLong();
		}

		// calculam distantele intre orase
		long[] dist = new long[n - 1];
		for (int i = 0; i < n - 1; i++) {
			dist[i] = coord[i + 1] - coord[i];
		}

		// dpPrev = valoarea maxima pentru orasele anterioare, 1 = ultimul selectat, 0 = nu
		// dpCur = valoarea maxima pentru orasele curente, 1 = ultimul selectat, 0 = nu
		long[][] dpPrev = new long[n + 1][2];
		long[][] dpCur  = new long[n + 1][2];
		for (int k = 0; k <= n; k++) {
			dpPrev[k][0] = -1;
			dpPrev[k][1] = -1;
		}
		dpPrev[0][0] = 0;
		dpPrev[1][1] = 0;

		// iteram prin fiecare distanta
		for (int i = 1; i < n; i++) {
			long d = dist[i - 1];

			// resetam starile curente
			for (int k = 0; k <= n; k++) {
				dpCur[k][0] = -1;
				dpCur[k][1] = -1;
			}

			// pt fiecare oras selectat pana acum
			for (int select = 0; select <= i; select++) {
				for (int prevSelect = 0; prevSelect < 2; prevSelect++) {
					long currVal = dpPrev[select][prevSelect];

					if (currVal < 0) {
						continue;
					}

					// daca nu selectam orasul, selectia ramane aceeasi, ultimul devine neactiv
					// daca ultimul era activ, adaugam distanta
					int newCountA = select;
					long ok = 0;
					if (prevSelect == 1) {
						ok = d;
					}
					dpCur[newCountA][0] = Math.max(dpCur[newCountA][0], currVal + ok);

					// daca selectam orasul, selectia creste cu 1, ultimul devine activ,
					// si adaugam distanta
					dpCur[select + 1][1] = Math.max(dpCur[select + 1][1], currVal + d);
				}
			}

			long[][] tmp = dpPrev;
			dpPrev = dpCur;
			dpCur  = tmp;
		}

		// extragem cea mai mare greutate pentru fiecare numar de orase selectate
		long[] maxWeight = new long[n + 1];
		for (int k = 0; k <= n; k++) {
			maxWeight[k] = Math.max(dpPrev[k][0], dpPrev[k][1]);
		}

		// Pt fiecare negustor, cautam cel mai mare x cu maxWeight[x] <= buget
		for (long neg : negustori) {
			int low = 0, high = n;
			while (low < high) {
				int mid = (low + high + 1) >>> 1;
				if (maxWeight[mid] <= neg) {
					low = mid;
				} else {
					high = mid - 1;
				}
			}
			out.println(low);
		}

		in.close();
		out.close();
	}
}
