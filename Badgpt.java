import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Badgpt {
	static final long mod = 1000000007;

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader("badgpt.in"));
		BufferedWriter out = new BufferedWriter(new FileWriter("badgpt.out"));

		// citim inputul
		String input = in.readLine();

		long res = 1;
		int i = 0;
		while (i < input.length()) {
			// citim caracterul curent
			char c = input.charAt(i);
			i++;

			// citim numarul care urmeaza
			long count = 0;
			while (i < input.length() && Character.isDigit(input.charAt(i))) {
				count = count * 10 + input.charAt(i) - '0';
				i++;
			}

			// pt caracterele 'c' si 'n', folosim o DP de tip Fibonacci
			if (c == 'n' || c == 'u') {
				res = (res * fibo(count + 1)) % 1000000007;
			}
		}

		out.write(res + "\n");

		out.close();
		in.close();
	}

	// functie care ridica matricea F la puterea p folosind exponentierea rapida
	static void power(long[][] F, long p) {
		if (p == 0 || p == 1) {
			return;
		}

		long[][] M = {
			{1, 1},
			{1, 0}
		};

		power(F, p / 2); // recursiv ridicam matricea la puterea p/2
		multiply(F, F);

		if (p % 2 != 0) { // daca p este impar, inmultim cu matricea M
			multiply(F, M);
		}
	}

	// functie care returneaza n-ul termen Fibonacci folosind exponentierea de matrice
	static long fibo(long n) {
		if (n == 0) {
			return 0;
		}

		long[][] F = {
			{1, 1},
			{1, 0}
		};

		power(F, n - 1);
		return F[0][0];
	}

	// functie care inmulteste doua matrici 2x2
	static void multiply(long[][] mat1, long[][] mat2) {
		final long x = (mat1[0][0] * mat2[0][0] + mat1[0][1] * mat2[1][0]) % mod;
		final long y = (mat1[0][0] * mat2[0][1] + mat1[0][1] * mat2[1][1]) % mod;
		final long z = (mat1[1][0] * mat2[0][0] + mat1[1][1] * mat2[1][0]) % mod;
		final long w = (mat1[1][0] * mat2[0][1] + mat1[1][1] * mat2[1][1]) % mod;

		mat1[0][0] = x;
		mat1[0][1] = y;
		mat1[1][0] = z;
		mat1[1][1] = w;
	}
}
