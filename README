# TEMA 1 - AA

In cadrul acestei teme am avut de ales dintr-un set de probleme 4 probleme si sa le rezolvam. Problemele alese de mine sunt: Feribot, Stocks, Badgpt, Regele

## 1. Feribot
In aceasta problema am avut de facut impartirea unui convoi de N masini, fiecare cu greutatea ei, in exact K grupuri consecutive, astfel incat suma greutatilor din grupul cel mai incarcat sa fie minima posibil. Pentru rezolvare, am folosit cautarea binara pe valoarea maxima permisa pe un feribot: stabilim limitele low (greutatea celei mai grele masini) si high (suma tuturor greutatilor), apoi iteram pana cand low si high converg. La fiecare pas, alegem un prag mid, simulam cate feriboturi ar fi necesare daca niciunul nu poate depasi mid, si ajustam limitele in consecinta — daca ne trebuie mai mult de K, crestem pragul, altfel il reducem. La final, valoarea comuna low == high reprezinta costul optim maxim al traversarii.

Complexitate: O(N · log S), unde S = (high - low) = diferenta dintre suma tuturor greutatilor si greutatea maxima a unei masini.

## 2. Stocks
In aceasta problema am avut de facut selectia unor actiuni dintre N optiuni diferite, fiecare cu pret curent, minim si maxim estimate pana la sfarsitul anului, astfel incat:

* suma investitiei (currentValue) sa nu depaseasca bugetul B
* pierderea maxima posibila (currentValue - minValue) sa nu depaseasca pragul L
* profitul in cel mai bun caz (maxValue - currentValue) sa fie maxim

Pentru a rezolva, am folosit programare dinamica de tip Rucsac pe trei dimensiuni: dp[i][b][l] = profitul maxim dupa procesarea primelor i actiuni, folosind buget b si inregistrand o pierdere totala l. Initial am setat toate starile la -10000, cu dp[0][0][0]=0. La fiecare pas i, fie nu cumpar actiunea (dp[i][b][l] = dp[i-1][b][l]), fie o cumpar daca b ≥ currentValue[i] si l ≥ risk[i] si actualizez dp[i][b][l] = max(dp[i][b][l], dp[i-1][b-currentValue][l-risk] + profit[i]). La final, trecem prin toate starile dp pentru a gasi profitul maxim.

Complexitate algoritm: O(N · B · L).

## 3. Badgpt
In aceasta problema am avut de facut numararea tuturor sirurilor originale care, dupa comprimarea consecutiva si inlocuirea literelor m → nn si w → uu, ar putea produce un sir comprimat dat (sub forma l1 n1 l2 n2 ...). Fiecare grup l_i n_i indica ca litera l_i apare n_i ori consecutiv, iar m si w sunt ambigue: o secventa nn poate proveni din nn sau m, iar uu din uu sau w.

Solutia foloseste doua observatii:

* Grupurile pentru litere diferite sunt independente (nu exista acelasi caracter in doua grupuri alaturate), deci rezultatul total este produsul numarului de posibilitati pentru fiecare grup.
* Pentru un grup de litera n cu contor n_i, numarul de siruri posibile este Fibonacci(n_i+1), deoarece putem segmenta n_i litere n fie ca nn individuale, fie grupate in m (care produce doi n). La fel pentru u si w.

Astfel, parcurgem fiecare pereche (l_i, n_i):

* Daca l_i == 'n' sau l_i == 'u', inmultim rezultatul curent cu Fibonacci(n_i+1) folosind exponentiere rapida a matricilor pentru calcul in O(log n).
    * -> Algortimul de exponentiere rapid a matricilor l-am luat de aici: https://www.geeksforgeeks.org/matrix-exponentiation/
* Altfel, lasam rezultatul nemodificat.

Complexitatea algortimului este: O(G · log N).

## 4. Regele
In aceasta problema am avut de facut determinarea, pentru fiecare numar X de orase, a necesarului de negustori in cel mai rau caz (maxim) pentru a face acele X orase active, unde un oras este activ daca ambele rute comerciale catre vecinii sai sunt alocate cu un numar de negustori egal cu distanta dintre orase.

Pasi de rezolvare:

1. Calculam vectorul dist al distantelor dintre fiecare pereche de orase adiacente.
2. Folosim programare dinamica bidimensionala pe prefixele oraselor si numarul de orase selectate, cu stari dpPrev[k][stare] si dpCur[k][stare], unde k este numarul de orase alese pana acum, iar stare arata daca ultimul oras din prefix a fost selectat (1) sau nu (0). Actualizam aceste stari cumulandu distantelor (d) in functie de tranzitii, maximizand numarul total de negustori necesari pentru orice selectie de k orase.
3. La final, construim maxWeight[k] = max(dpPrev[k][0], dpPrev[k][1]), reprezentand costul in cel mai rau caz (maxim alocat de negustori) pentru k orase.
4. Pentru fiecare intrebare cu M negustori disponibili, raspundem cu cel mai mare k astfel incat maxWeight[k] ≤ M folosind cautare binara.

Complexitate totala: O(N^2 + Q · log N).