#include <stdio.h>
#include <stdlib.h>

#define max(a, b) ((a) > (b) ? (a) : (b))

int main() {
    FILE *in = fopen("stocks.in", "r");
    FILE *out = fopen("stocks.out", "w");

    int N, B, L;
    fscanf(in, "%d %d %d", &N, &B, &L);
    int currValue[N], minValue[N], maxValue[N];
    for (int i = 0; i < N; i++) {
        fscanf(in, "%d %d %d", &currValue[i], &minValue[i], &maxValue[i]);
    }

    int ***dp = malloc((N + 1) * sizeof(int **));
    for (int i = 0; i <= N; i++) {
        dp[i] = malloc((B + 1) * sizeof(int *));
        for (int j = 0; j <= B; j++) {
            dp[i][j] = malloc((L + 1) * sizeof(int));
        }
    }

    /* dp pentru profitul pt fiecare obiect pe toate
        bugetele si loss ul worst case */

    for (int i = 0; i <= N; i++) {
        for (int j = 0; j <= B; j++) {
            for (int k = 0; k <= L; k++) {
                dp[i][j][k] = -10000;
            }
        }
    }
    dp[0][0][0] = 0;

    /* pt fiecare stock */
    for (int i = 1; i <= N; i++) {
        int curr = currValue[i - 1];
        int risk = currValue[i - 1] - minValue[i - 1];
        int profit = maxValue[i - 1] - currValue[i - 1];

        /* toate posibilitatile de buge si risk care avem voie sa l luam */
        for (int b = 0; b <= B; b++) {
            for (int l = 0; l <= L; l++) {
                /* nu luam stockul curent */
                dp[i][b][l] = dp[i - 1][b][l];

                /* luam stockul curent */
                if (b >= curr && l >= risk) {
                    dp[i][b][l] = max(dp[i][b][l],
                                        dp[i - 1][b - curr][l - risk] + profit);
                }
            }
        }
    }

    /* bugetul maxim dintre toate riskurile si bugetele */
    int maxProfit = 0;
    for (int i = 0; i <= B; i++) {
        for (int j = 0; j <= L; j++) {
            if (dp[N][i][j] > maxProfit) {
                maxProfit = dp[N][i][j];
            }
        }
    }

    fprintf(out, "%d\n", maxProfit);

    return 0;
}
