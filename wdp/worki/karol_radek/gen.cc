#include <bits/stdc++.h>
#include "worki.h"
using namespace std;

int main() {
      int seed;
      cin >> seed;
      mt19937_64 engine(seed * 69);
      auto random = [&](int b) -> int {
            return (int)((unsigned long long)engine() % b + 1);
      };

      mt19937_64 masks(seed * 2137);
      auto rmask = [&masks]() -> unsigned long long {
            return masks();
      };

      int q;
      cin >> q;
      vector<przedmiot *>items(10);
      vector<worek *>sacks(10);
      for (int i = 0; i < 10; i++) {
            items[i] = nowy_przedmiot();
            sacks[i] = nowy_worek();
      }

      unsigned long long hsh = 0;
      int count = 0;
      while (count < q) {
            worek *rw = sacks[random(9)];
            worek *rw2 = sacks[random(9)];
            while (rw == rw2) {
                  rw = sacks[random(9)];
                  rw2 = sacks[random(9)];
            }
            przedmiot *ri = items[random(9)];
            int op = random(7) + 1;
            unsigned long long res = -2;

            switch (op) {
                  case 1:
                        if (w_ktorym_worku(ri) != -1 || w_ktorym_worku(rw) != -1)
                              goto end;
                        count++;
                        wloz(ri, rw);
                        break;
                  case 2:
                        if (w_ktorym_worku(rw) != -1 || w_ktorym_worku(rw2) != -1)
                              goto end;
                        count++;
                        wloz(rw, rw2);
                        break;
                  case 3:
                        if (w_ktorym_worku(ri) == -1 || w_ktorym_worku(sacks[w_ktorym_worku(ri)]) != -1)
                              goto end;
                        count++;
                        wyjmij(ri);
                        break;
                  case 4:
                        if (w_ktorym_worku(rw) == -1 || w_ktorym_worku(sacks[w_ktorym_worku(rw)]) != -1)
                              goto end;
                        count++;
                        wyjmij(rw);
                        break;
                  case 5:
                        res = w_ktorym_worku(ri);
                        count++;
                        break;
                  case 6:
                        res = w_ktorym_worku(rw);
                        count++;
                        break;
                  case 7:
                        res = ile_przedmiotow(rw);
                        count++;
                        break;
                  case 8:
                        if (w_ktorym_worku(rw) != -1)
                              goto end;
                        count++;
                        na_odwrot(rw);
                  end:;
            }
            hsh += rmask() * (res + 3);
      }
      cout << hsh << "\n";
      gotowe();
}


