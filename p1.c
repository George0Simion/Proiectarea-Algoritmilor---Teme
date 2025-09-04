#include <stdio.h>
#include <stdlib.h>

/* functie de comparare */
int compare(const void *a, const void *b) {
    return (*(int *)a - *(int *)b);
}

int main() {
    FILE *in = fopen("p1.in", "r");
    FILE *out = fopen("p1.out", "w");

    // numarul de noduri
    int N;
    fscanf(in, "%d", &N);

    // vectorul de distante
    int *v = (int *)malloc(N * sizeof(int));
    if (v == NULL) {
        return 1;
    }

    // citim distantele si calculam maximul
    int maxd = 0;
    for (int i = 0; i < N; i++) {
        fscanf(in, "%d", &v[i]);
        if (v[i] > maxd) {
            maxd = v[i];
        }
    }

    // tot timpul primul element din vector trebuie sa fie 0
    if (v[0] != 0) {
        fprintf(out, "-1\n");
        return 0;
    }

    // niciun alt nod nu trebuie sa mai aiba distanta 0
    for (int i = 1; i < N; i++) {
        if (v[i] == 0) {
            fprintf(out, "-1\n");
            return 0;
        }
    }

    // retinem pt fiecare distanta d
    //          primul nod care trebuie sa se afle la distanta d
    int *first = (int *)calloc(N + 1, sizeof(int));
    for (int i = 0; i < N; i++) {
        if (first[v[i]] == 0) {
            // retinem indexul nodului doar daca nu a fost setat anterior
            first[v[i]] = i + 1;
        }
    }

    // verificam consecutivitatea distantelor
    // daca exista un nod la distanta d, trebuie sa exista unul la
    //                  distanta d - 1
    for (int d = 1; d <= maxd; d++) {
        if (first[d] != 0 && first[d-1] == 0) {
            // distanta d apare fata sa existe nod cu distanta d - 1 -> -1
            fprintf(out, "-1\n");
            return 0;
        }
    }

    // nr de muchii = N - 1
    fprintf(out, "%d\n", N - 1);

    // construim muchiile -> pt fiecare nod i + 1 la distanta v[i]
    //      il legam cu primul nod care se afla la distanta v[i] - 1
    for (int i = 1; i < N; i++) {
        fprintf(out, "%d %d\n", first[v[i] - 1], i + 1);
    }

    // eliberam
    free(v);
    free(first);
    fclose(in);
    fclose(out);
    return 0;
}