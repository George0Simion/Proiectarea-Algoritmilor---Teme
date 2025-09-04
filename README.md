# Tema 3

# Problemă

## Abordare

Pornim de la ideea ca, daca avem un graf neorientat cu N noduri si pornim un BFS din nodul 1, distantele de la nodul 1 la celelalte noduri trebuie sa coincida cu vectorul v = [v1, v2, ..., vN] dat. Cum pentru orice BFS pe graf conex cu N noduri si fara muchii inutile avem exact N-1 muchii, solutia se va baza pe construirea unui arbore in care fiecare nod i, cu distanta vi, se va conecta la un nod parinte aflat la distanta vi-1.

1. **Validări inițiale**  
   - Verificam ca v1 = 0 (distanta de la nodul 1 la sine insusi este zero)  
   - Ne asiguram ca niciun alt vi nu este zero
2. **Găsirea primului reprezentant pentru fiecare distanță**  
   Cream un tablou first[d] care retine indexul primului nod care apare la distanta d. Astfel, first[0] = 1, iar pentru i = 2…N daca first[v[i]] nu este setat, il initializam cu 
3. **Verificarea consecutivității nivelurilor**  
   Pentru fiecare d de la 1 pana la valoarea maxima din v, daca exista first[d] dar nu exista first[d-1], raspunsul este -1.
4. **Construirea efectivă a muchiilor**  
   Pentru fiecare nod i de la 2 la N, scriem muchia "first[v[i]-1] i".

## Implementare

1. Deschidem fișierele `p1.in` și `p1.out`.  
2. Citim \(N\) și apoi vectorul de distanțe `v[0..N-1]`, ținând în același timp evidența celui mai mare nivel (`maxd`).  
3. Verificăm condițiile de validitate:
   - `v[0] == 0`  
   - pentru orice `i > 0`, `v[i] != 0`  
   Dacă oricare dintre aceste condiții eșuează, scriem `-1` și terminăm.
4. Alocăm un tablou `first` de dimensiune `N+1`, inițializat cu 0.  
5. Parcurgem din nou vectorul `v` și pentru fiecare poziție `i` (0-based) setăm, dacă nu e deja setat, `first[v[i]] = i+1`.  
6. Verificăm consecutivitatea distantelor: pentru fiecare `d` între 1 și `maxd`, dacă `first[d] != 0` dar `first[d-1] == 0`, rezultatul este `-1`.  
7. Dacă toate validările trec, scriem în ieșire numărul de muchii (`N-1`), apoi pentru fiecare nod `i` de la 2 la `N` afișăm muchia:


# Problemă

## Abordare

Trebuie să găsim, într-o matrice de dimensiuni \(N \times M\), cea mai mare zonă conexă (în sensul vecinătății pe verticală sau orizontală) astfel încât, în acea zonă, diferența dintre valoarea maximă și valoarea minimă să fie cel mult \(K\). O metodă directă este să considerăm fiecare celulă \((i,j)\) ca punct de plecare și să extindem o regiune printr-o parcurgere în lățime (BFS), menținând pe parcurs valorile curente de minim și maxim găsite. Pornind de la \((i,j)\), inițial avem \( \text{min} = \text{max} = c_{i,j} \); apoi, pentru fiecare vecin neexplorat, calculăm noile extreme \(\min' = \min(\text{min}, c_{\text{vecin}})\) și \(\max' = \max(\text{max}, c_{\text{vecin}})\). Dacă \(\max' - \min' \le K\), putem include celula în regiune și continuăm BFS; altfel, o sărim. În final, comparăm dimensiunea regiunii obținute cu cea mai mare găsită până atunci.

## Implementare

1. **Citirea datelor**  
   - Deschidem `p2.in` și citim valorile \(N\), \(M\) şi \(K\).  
   - Construim matricea `mat[N][M]` citind liniile următoare.

2. **Parcurgerea tuturor celulelor**  
   - Pentru fiecare poziție \((i,j)\) din matrice:
     1. Inițializăm o coadă `Queue<int[]>` și un tablou `visited[N][M]`, marcând \((i,j)\) ca vizitat.  
     2. Setăm `min = mat[i][j]`, `max = mat[i][j]` și `count = 1`.  
     3. Începem BFS: scoatem coordonatele curente, verificăm cei patru vecini valizi (sus, jos, stânga, dreapta).  
     4. Pentru fiecare vecin nevizitat, calculăm `newMin` și `newMax`. Dacă `newMax - newMin <= K`, marcăm vecinul vizitat, îl adăugăm în coadă, actualizăm `min`, `max` și incrementăm `count`.  
     5. Continuăm până când coada se golește; `count` va conține aria zonei pornind din \((i,j)\).
   - Actualizăm rezultatul global `res = max(res, count)`.

3. **Scrierea rezultatului**  
   - După ce am explorat toate punctele de start, scriem `res` în `p2.out` și închidem fișierele.

## Complexitate

- **Pentru fiecare celulă** \((i,j)\) dintre cele \(N\times M\):  
  - Inițializăm și golim coada de BFS.  
  - În cel mai rău caz, BFS-ul va explora toate cele \(N\times M\) celule (dacă toate pot intra în regiune), procesând fiecare vecin de patru ori constant.  
  - Costul unui BFS este \(O(N \times M)\).

- Cum pornim BFS de la fiecare dintre cele \(N * M\) celule, timpul total ajunge la
  O((N×M)^2) (BFS din fiecare celula)

- **Spațial**: folosim un tablou `visited[N][M]` și o coadă care în cel mai rău caz poate conține \(O(NM)\) elemente, plus matricea de intrare — deci O(N×M) (matrice de vizitat)

# Problemă 3

## Abordare

Robin Hood trebuie să parcurgă un lac de buşteni plutitori, într-un interval de timp T, pentru a ajunge la Maid Marian, consumând cea mai mică energie posibilă. Fiecare buştean se mişcă pe o linie dreaptă (verticală sau orizontală), iar poziţia fiecărui capăt la momentul \(t\) se poate determina prin asumarea unei serii de deplasări prefixate. La fiecare pas de timp, Robin are la dispoziţie trei tipuri de acţiuni — să stea pe loc (cost \(E_1\)), să facă un pas către un vecin pe acelaşi buştean (cost \(E_2\)) sau să sară pe un alt buştean intersectant (cost \(E_3\)). Fiecare stare a lui Robin poate fi codificată printr-o triplă \((t,\,i,\,o)\), unde:
- \(t\) este timpul curent (de la \(0\) la \(T\));  
- \(i\) este indicele buşteanului pe care se află (de la \(1\) la \(N\));  
- \(o\) este offset-ul de la capătul de start al buştanului (de la \(0\) la lungimea lui).

Pornim de la starea iniţială \((0,\,1,\,0)\) cu energie consumată \(0\) şi aplicăm Dijkstra pe graful implicit al acestor stări, având muchiile ponderate cu costurile celor trei acţiuni. Când ajungem într-o stare în care Robin se află în coordonatele lui Maid Marian, ne oprim şi reconstruim calea.

## Implementare

1. **Citirea şi preprocesarea datelor**  
   - Citim \(T\) şi \(N\), apoi coordonatele lui Maid Marian \((x_M,y_M)\) şi costurile \(E_1,E_2,E_3\).  
   - Pentru fiecare buştean citim coordonatele capătelor \((x_i^{start},y_i^{start})\), \((x_i^{end},y_i^{end})\) şi şirul de direcţii de lungime \(T\).  
   - Creăm un obiect `LogInfo` care, la iniţializare, calculează două tablouri `deplasariX[t]` şi `deplasariY[t]` pentru poziţia capătului de start la fiecare pas de timp.

2. **Structura de date pentru Dijkstra**  
   - Construim un tablou tridimensional `dist[t][i][o]`, iniţializat cu valori infinite, de dimensiuni \((T+1) \times N \times (L_i+1)\), unde \(L_i\) este lungimea buştanului \(i\).  
   - Folosim un `PriorityQueue<StareNod>` ordonat după energia consumată, unde fiecare nod stochează \((energie, t, i, o, parinte, actiune)\).

3. **Executarea algoritmului**  
   - Pornim din starea \((0,1,0)\) cu `dist[0][1][0] = 0`.  
   - Cât timp coada nu e goală, extragem starea cu energia minimă; dacă aceasta corespunde coordonatelor lui Maid Marian, o salvăm ca soluţie şi oprim.  
   - În caz contrar, pentru pasul de timp următor \(t+1\), generăm cele trei tipuri de tranziţii:
     1. **H (sta pe loc)**: acelaşi buştean, acelaşi offset, cost \(E_1\).  
     2. **N/S/E/V (pas pe buştean)**: offset\(\pm1\) pe acelaşi buştean, cost \(E_2\), dacă rămâne valid (în interiorul buştanului).  
     3. **J j (salt pe buşteanul \(j\))**: pentru fiecare buştean intersectant în poziţia curentă, salt la acelaşi punct de intersecţie, cost \(E_3\).
   - Relaxăm muchiile şi, dacă găsim un cost mai mic, actualizăm `dist` şi adăugăm noua stare în coadă.

4. **Reconstruirea traiectoriei**  
   - Din starea finală, urmăm legăturile `parinte` înapoi până la starea iniţială, apoi inversăm lista de acţiuni pentru a obţine paşii lui Robin.

## Complexitate

- **Număr de stări**: pentru fiecare timp \(t\in[0,T]\), fiecare buştean \(i\in[1,N]\) şi fiecare offset \(o\in[0,L_i]\), avem o stare — în total  
  \[
    V = \sum_{i=1}^N (T+1)\,(L_i+1) \;=\; O\bigl(T \times N \times L\bigr),
  \]
  unde \(L=\max_i L_i\).

- **Muchii per stare**:  
  - 1 tranziţie “H”  
  - până la 2 tranziţii de pas (N/S sau E/V)  
  - până la \(N-1\) tranziţii “J” (salturi către celelalte buşteni)  
  → \(O(N)\) per stare.

- **Dijkstra cu coadă de priorităţi** are cost aproximativ  
 O(T×N×L×log(T×N×L)) (Dijkstra cu coada prioritara)

- **Spaţial**: stocăm tabloul `dist` (\(O(TNL)\)) şi coada de priorităţi (\(O(V)\)).

O(T×N×L) (stocarea distanta minima pentru fiecare stare)

