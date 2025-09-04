#include <stdio.h>
#include <stdlib.h>

int cmp(const void *a, const void *b) {
    return (*(int *)a - *(int *)b);
}

int main() {
    FILE *in = fopen("feribot.in", "r");
    FILE *out = fopen("feribot.out", "w");

    /* citire */
    int N, K;
    fscanf(in, "%d %d", &N, &K);
    long long *masini = malloc(N * sizeof(long long));
    for (int i = 0; i < N; i++) {
        fscanf(in, "%lld", &masini[i]);
    }

    /* calculam greutatea minima a unei masini si suma maxima a greutatile 
        foloosim aceste rezultate pentru cautarea binara */
    long long low = 0, high = 0;
    for (int i = 0; i < N; i++) {
        if (masini[i] > low) {
            low = masini[i];
        }
        high += masini[i];
    }

    /* 
    cautare binara 
        -> low = greutatea minima a unei masini
        -> high = suma greutatilor masinilor
        -> mid = greutatea maxima a unei masini
        -> used = numarul de masini folosite
        -> count = greutatea curenta a masinilor
    */
    while (low < high) {
        long long mid = low + (high - low) / 2;

        int used = 1;
        long long count = 0;
        for (int i = 0; i < N; i++) {
            if (count + masini[i] <= mid) {
                // masina i incape in greutatea maxima
                count += masini[i];

            } else {
                // masina i nu incape in greutatea maxima
                used++;
                count = masini[i];
            }
        }

        if (used > K) {
            /* daca numarul de masini folosite este mai mare decat K,
                cautam o greutate mai mare */
            low = mid + 1;

        } else {
            /* daca numarul de masini folosite este mai mic decat K,
                cautam o greutate mai mica */
            high = mid;
        }
    }

    fprintf(out, "%lld\n", low);

    free(masini);
    fclose(in);
    fclose(out);
    return 0;
}
