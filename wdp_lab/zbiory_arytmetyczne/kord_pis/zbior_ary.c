#include<stdlib.h>
#include <stdio.h>
#include <stdlib.h> // Zawiera qsort
#include "zbior_ary.h"

int diff = -1; // globalna zmienna przechowująca wartość q, -1 oznacza, że nie została jeszcze ustawiona

// Funkcja zwracająca wartość modulo diff, obsługująca również liczby ujemne
int mod(int x)
{
    return ((x % diff) + diff) % diff;
}

// Konstruktor zbioru ze zbioru arytmetycznego
zbior_ary ciag_arytmetyczny(int a, int q, int b)
{
    zbior_ary result;
    result.mn = (long long*)malloc(sizeof(long long));
    result.mx = (long long*)malloc(sizeof(long long));
    result.size = 1;
    diff = q;
    result.mn[0] = a;
    result.mx[0] = b;
    return result;
}

// Konstruktor zbioru zawierającego jeden element
zbior_ary singleton(int a)
{
    zbior_ary result;
    result.mn = (long long*)malloc(sizeof(long long));
    result.mx = (long long*)malloc(sizeof(long long));
    result.size = 1;
    result.mn[0] = a;
    result.mx[0] = a;
    return result;
}

// Implementacja funkcji suma
zbior_ary suma(zbior_ary A, zbior_ary B)
{
    zbior_ary result;
    result.mn = (long long*)malloc(sizeof(long long) * (A.size + B.size));
    result.mx = (long long*)malloc(sizeof(long long) * (A.size + B.size));
    result.size = 0;

    unsigned int A_index = 0;
    unsigned int B_index = 0;

    //rozważamy elementy(ciągi) w zbiorach A i B w kolejności rosnącej lewych krańców (A.mn i B.mn)
    //Dopuki pozostały elementy do rozważenia w zbiorze A
    while (A_index<A.size && B_index<B.size)
    {
        //Dopuki obecnie rozważane elementy w zbiorach A i B mają różne wartości modulo q
        while(A_index<A.size && B_index<B.size && mod(A.mn[A_index]) != mod(B.mn[B_index]))
        {
            //Skoro różne modulo, to obecne elementy się nie pokrywają, więc dodajemy mniejszy z nich do wyniku
            if (mod(A.mn[A_index]) < mod(B.mn[B_index]))
            {
                result.mn[result.size] = A.mn[A_index];
                result.mx[result.size] = A.mx[A_index];
                A_index++;
                result.size++;
            }
            else
            {
                result.mn[result.size] = B.mn[B_index];
                result.mx[result.size] = B.mx[B_index];
                B_index++;
                result.size++;
            }
        }

        if(!(A_index<A.size && B_index<B.size))break;//Sprawdzenie, czy nie skończyły się elementy
        long long current_diff = mod(A.mn[A_index]);//obecnie rozważane modulo
        long long mn = (A.mn[A_index] < B.mn[B_index]) ? A.mn[A_index] : B.mn[B_index];//początek obecnie rozważanego ciągu w wyniku (skoro jest suma, to kilka ciągów może się nakładać tworząc jeden, więc musimy zapisać jego początek - bierzemy mniejszy lewy kraniec)

        //Obecnie rozważane elementy w zbiorach A i B mają te same wartości modulo q
        while (A_index<A.size && B_index<B.size && mod(A.mn[A_index]) == mod(B.mn[B_index]))
        {
            //gdy obecnie rozważane odcinki są rozłączne
            if (A.mx[A_index]+diff < B.mn[B_index])
            {
                //odcinki są rozłączne, i A ma mniejszy lewy koniecc, więc dodajemy obecny element A do wyniku
                result.mn[result.size] = mn;
                result.mx[result.size] = A.mx[A_index];
                A_index++;
                result.size++;
                //ustawiamy początek nowego odcinka na lewy koniec kolejnego elementu w A lub B, w zależności od tego, który jest mniejszy
                mn = B.mn[B_index];
                if(A_index<A.size && mod(A.mn[A_index])==current_diff && B.mn[B_index] > A.mn[A_index]) mn = A.mn[A_index];//sprawdzenie, czy kolejny element w A ma to samo modulo i czy on wogule istnieje
            }
            else if (A.mn[A_index] > B.mx[B_index]+diff)
            {
                //odcinki są rozłączne, i B ma mniejszy lewy koniecc, więc dodajemy obecny element B do wyniku
                result.mn[result.size] = mn;
                result.mx[result.size] = B.mx[B_index];
                B_index++;
                result.size++;
                //ustawiamy początek nowego odcinka na lewy koniec kolejnego elementu w A lub B, w zależności od tego, który jest mniejszy
                mn = A.mn[A_index];
                if(B_index<B.size && mod(B.mn[B_index])==current_diff && A.mn[A_index] > B.mn[B_index]) mn = B.mn[B_index];//sprawdzenie, czy kolejny element w B ma to samo modulo i czy on wogule istnieje
            }
            //gdy obecnie rozważane odcinki się nakładają:
            else
            {
                //aktualizujemy prawy koniec obecnego odcinka w wyniku - przechodzimy do kolejnego elementu w A lub B, w zależności od tego, który ma większy prawy koniec
                if(A.mx[A_index]>B.mx[B_index])B_index++;
                else A_index++;
            }
        }
        //Zamykamy obecny odcinek w wyniku
        if(A_index<A.size && mod(A.mn[A_index]) == current_diff)
        {
            result.mn[result.size] = mn;
            result.mx[result.size] = A.mx[A_index];
            A_index++;
            result.size++;
        }
        else if(B_index<B.size && mod(B.mn[B_index]) == current_diff)
        {
            result.mn[result.size] = mn;
            result.mx[result.size] = B.mx[B_index];
            B_index++;
            result.size++;
        }
    }
    //jeśli zostały jakies elementy w A, a w B już nie, to je dodajemy
    while (A_index<A.size)
    {
        result.mn[result.size] = A.mn[A_index];
        result.mx[result.size] = A.mx[A_index];
        A_index++;
        result.size++;
    }
    //jeśli zostały jakies elementy w B, a w A już nie, to je dodajemy
    while (B_index<B.size)
    {
        result.mn[result.size] = B.mn[B_index];
        result.mx[result.size] = B.mx[B_index];
        B_index++;
        result.size++;
    }    
    return result;
}


zbior_ary roznica(zbior_ary A, zbior_ary B)
{
    zbior_ary result;
    result.mn = (long long*)malloc(sizeof(long long) * (A.size + B.size));
    result.mx = (long long*)malloc(sizeof(long long) * (A.size + B.size));
    result.size = 0;

    unsigned int A_index = 0;
    unsigned int B_index = 0;

    //rozważamy elementy(ciągi) w zbiorach A i B w kolejności rosnącej lewych krańców (A.mn i B.mn)
    //Dopuki pozostały elementy do rozważenia w zbiorze A i B
    while (A_index<A.size && B_index<B.size)
    {
        //Dopuki obecnie rozważane elementy w zbiorach A i B mają różne wartości modulo q
        while(A_index<A.size && B_index<B.size && mod(A.mn[A_index]) != mod(B.mn[B_index]))
        {
            if (mod(A.mn[A_index]) < mod(B.mn[B_index]))
            {
                result.mn[result.size] = A.mn[A_index];
                result.mx[result.size] = A.mx[A_index];
                A_index++;
                result.size++;
            }
            else
            {
                B_index++;
            }
        }


        if(!(A_index<A.size && B_index<B.size))break;//Sprawdzenie, czy nie skończyły się elementy
        int current_diff = mod(A.mn[A_index]);

        int mn = A.mn[A_index];//początek obecnie rozważanego ciągu w A
        //Dopuki obecnie rozważane elementy w zbiorach A i B mają te same wartości. 
        //Różnice elementów o tym samym modulo możemy rozważać jak różnice zbioru odcinków na prostej
        while (A_index<A.size && B_index<B.size && mod(A.mn[A_index]) == mod(B.mn[B_index]))
        {
            //gdy obecnie rozważane odcinki są rozłączne
            if (A.mx[A_index] < B.mn[B_index])//gdy A.mx jest mniejszy niż B.mn, odcniki są rozłączne, więc dodajemy obecny element A do wyniku
            {
                result.mn[result.size] = mn;
                result.mx[result.size] = A.mx[A_index];
                A_index++;
                if(A_index<A.size)mn = A.mn[A_index];
                result.size++;
            }
            else if (A.mn[A_index] > B.mx[B_index])//gdy A.mn jest większy niż B.mx, odcniki są rozłączne, więć pomijamy obecny element z B
            {
                B_index++;
            }
            //gdy obecnie rozważane odcinki się nakładają:
            else if(mn>=B.mn[B_index])
            {
                if(A.mx[A_index]>B.mx[B_index])
                {
                    // element z B pokrywa początek (lewy kraniec) elementu z A, wiec "ucinamy" go do B.mx+q:
                    mn = B.mx[B_index]+diff; 
                    B_index++;
                }
                else
                {
                    A_index++; // element z B pokrywa w całości element z A
                    if(A_index<A.size)mn = A.mn[A_index];
                }
            }
            else
            {
                // element z B nie pokrywa początku (lewego krańca) elementu z A, więc dodajemy odcinek [A.mn - B.mn-q):
                result.mn[result.size] = mn; 
                result.mx[result.size] = B.mn[B_index]-diff; 
                result.size++;
                if(A.mx[A_index]>B.mx[B_index])
                {
                    mn = B.mx[B_index]+diff;// element z B nie pokrywa prawego końca elementu z A, więc zawiera się w nim - więc ucinamy element z A do B.mx
                    B_index++;
                }
                else
                {
                    A_index++; // element z B pokrywa prawy koniec, więc przerobiliśmy cały element z A
                    if(A_index<A.size)mn = A.mn[A_index];
                }
            }
            
        }
        //powyższa pętla kończy się, gdy skończą się elementy o indexie current_diff w A albo B, więc należy rozważyć pozostałe elementy w drugim zbiorze
        //rozważamy tylko case, gdy zostały elementy w A, bo to różnica, więc elementy z B możemy pominąć
        while(A_index<A.size && mod(A.mn[A_index]) == current_diff)
        {
            result.mn[result.size] = mn;
            result.mx[result.size] = A.mx[A_index];
            A_index++;
            if(A_index<A.size)mn = A.mn[A_index];
            result.size++;
        }
    }

    //jeśli zostały jakies elementy w A, a w B już nie, to je dodajemy
    while (A_index<A.size)
    {
        result.mn[result.size] = A.mn[A_index];
        result.mx[result.size] = A.mx[A_index];
        A_index++;
        result.size++;
    }
    //jeśli zostały jakies elementy w B, a w A już nie, to je pomijamy
    return result;
}

zbior_ary iloczyn(zbior_ary A, zbior_ary B)
{
    //A ∩ B = A \ (A \ B)
    return (roznica(A, roznica(A, B)));
}

bool nalezy(zbior_ary A, int b)
{
    if(A.size==0) return false;
    long long lo=0;
    long long hi=A.size-1;//binary searsh
    while (hi-lo>1)
    {
        long long mid = (lo+hi)/2;
        //najpierw szukamy elementów równych modulo q
        if(mod(A.mn[mid]) < mod(b))
        {
            lo=mid;
            continue;
        }
        if(mod(A.mn[mid]) > mod(b))
        {
            hi=mid;
            continue;
        }
        //jeśli A[mid] = b mod q, to szukamy największego lewego końca ciągu w A, który jest mniejszy/równy b
        if(A.mn[mid]<b) 
        {
            lo=mid;
            continue;
        }
        else if(A.mn[mid]>b)
        {
            hi=mid;
            continue;
        }
        else return true;//jeśli A.mn[mid]=b
    }
    //sprawdzamy, czy któryś z ciągów hi lub lo zawiera b
    return ((mod(A.mn[lo]) == mod(b) && A.mn[lo]<=b && A.mx[lo]>=b)||(mod(A.mn[hi]) == mod(b) && A.mn[hi]<=b && A.mx[hi]>=b));//sprawdzamy, czy któryś z ciągów hi lub lo zawiera b
}

unsigned moc(zbior_ary A)
{
    unsigned result=0;
    for (int i = 0; i < A.size; i++)
    {
        //dodajemy liczbę elementów w i-tym ciągu arytmetycznym
        result+=(A.mx[i]-A.mn[i])/diff+1;
    }
    return result;
}

unsigned ary(zbior_ary A)
{
    //liczba ciągów arytmetycznych w zbiorze A - to po prostu rozmiar tablicy mn lub mx, ponieważ zawsze trzymamy zbiór w optymalnej postaci względem ilości ciągów arytmetycznych
    return A.size;
}

int cmpfunc (const void * a, const void * b)
{
    if(*(int*)a > *(int*)b) return 1;
    if(*(int*)a < *(int*)b) return -1;
    return 0;
}

void print(zbior_ary A)
{
    int* tmp = (int*)malloc(sizeof(int) * moc(A));
    int index = 0;
    for(int i=0;i<A.size;i++)
    {
        for(int val=A.mn[i];val<=A.mx[i];val+=diff)
        {
            tmp[index++] = val;
        }
    }
    qsort(tmp, index, sizeof(int), cmpfunc);
    for(int i=0;i<index;i++)
    {
        printf("%d ", tmp[i]);
    }
    free(tmp);
    printf("\n");
}