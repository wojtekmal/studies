#include <stdio.h>
#include <stdlib.h>

int main()
{
    int n;
    scanf("%d", &n);
    int *tab = malloc((unsigned int) n * sizeof(int));
    int *lens = malloc((unsigned int) n * sizeof(int));
    int maxx = 0;

    for (int i = 0; i < n; i++)
    {
        scanf("%d", &tab[i]);
        lens[i] = 1;
        int pos = i - 1;

        while (pos >= 0)
        {
            if (tab[pos] != 0 && tab[i] % tab[pos] == 0)
            {
                lens[i] += lens[pos];
                pos -= lens[pos];
            }
            else
            {
                break;
            }
        }

        maxx = maxx > lens[i] ? maxx : lens[i];
    }

    printf("%d\n", maxx);
}