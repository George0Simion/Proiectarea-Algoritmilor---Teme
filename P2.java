import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.LinkedList;
import java.util.Queue;

public class P2 {
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader("p2.in"));
		BufferedWriter out = new BufferedWriter(new FileWriter("p2.out"));

		// citim prima linie: N (linii), M (coloane), K (diferenţa maximă permisă)
		String[] firstLine = in.readLine().split(" ");
		int N = Integer.parseInt(firstLine[0]);
		int M = Integer.parseInt(firstLine[1]);
		int K = Integer.parseInt(firstLine[2]);
		int[][] mat  = new int[N][M];

		// citim matricea de valori
		for (int i = 0; i < N; i++) {
			String[] line = in.readLine().split(" ");
			for (int j = 0; j < M; j++) {
				mat[i][j] = Integer.parseInt(line[j]);
			}
		}

		// definim cele 4 directii
		int[][] directions = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

		/* incepem BFS */

		// coada pt bfs
		Queue<int[]> que = new LinkedList<>();
		int res = 0;

		// facem bfs de la fiecare celula din matrice
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				// bagam elementul curent in coada
				que.offer(new int[]{i, j});

				// minimul si maximul curent
				int min = mat[i][j], max = mat[i][j] + K, count = 1;

				// vectorul de vizitati
				int[][] visited = new int[N][M];
				visited[i][j] = 1;

				// BFS
				while (!que.isEmpty()) {
					// scoatem din coada
					int[] cur = que.poll();

					// pt cele 4 directii
					for (int[] dir : directions) {
						// calculam noile coordonate
						int new_x = cur[0] + dir[0];
						int new_y = cur[1] + dir[1];

						// verificam daca sunt intre limitele matricei
						if (new_x < 0 || new_x >= N || new_y < 0 || new_y >= M) {
							continue;
						}

						// sarim peste celulele deja vizitate
						if (visited[new_x][new_y] == 1) {
							continue;
						}

						// caluclam noul min si max
						int newMin = Math.min(mat[new_x][new_y], min);
						int newMax = Math.max(mat[new_x][new_y], max);

						// verificam daca noile min max sunt in limita K
						if (newMax - newMin <= K) {
							// maracam ca vizitat si adaugam in coada
							que.offer(new int[]{new_x, new_y});
							visited[new_x][new_y] = 1;

							// updatam min max
							min = newMin;
							max = newMax;

							// numarul celula
							count++;
						}
					}
				}

				// aria maxima gasita
				res = Math.max(res, count);
			}
		}

		// afisam
		out.write(res + "\n");

		in.close();
		out.close();
	}
}
