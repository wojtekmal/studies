#include <bits/stdc++.h>

struct worek;

std::deque<worek *> worek_pointers;
worek *desk;

struct worek
{
    inline static int count = -1;
    worek **owner, **me;
    int item_count = 0, id;

    worek()
    {
        id = count++;
        me = new (worek *){this};
        worek_pointers.push_back(this);
    }
};


struct przedmiot
{
    worek **owner;
};

std::deque<przedmiot *> item_pointers;

void make_desk()
{
    desk = new worek;
    desk->owner = desk->me;
}

// Nowy worek na biurku; otrzymuje kolejny numer, począwszy od 0.
worek *nowy_worek()
{
    if (desk == NULL)
        make_desk();

    worek *item = new worek;
    item->owner = desk->me;
    return item;
}

// Nowy przedmiot na biurku
przedmiot *nowy_przedmiot()
{
    if (desk == NULL)
        make_desk();

    przedmiot *item = new przedmiot;
    item->owner = desk->me;
    item_pointers.push_back(item);
    return item;
}

// Wkłada worek co do worka gdzie.
// Założenie: co i gdzie leżą na biurku.
void wloz(worek *co, worek *gdzie)
{
    assert(co != gdzie);
    co->owner = gdzie->me;
    gdzie->item_count += co->item_count;
}

// Wkłada przedmiot co do worka gdzie.
// Założenie: co i gdzie leżą na biurku.
void wloz(przedmiot *co, worek *gdzie)
{
    co->owner = gdzie->me;
    gdzie->item_count++;
}

// Wyjmuje worek w z worka i kładzie na biurku.
// Założenie: Worek w był w worku leżącym na biurku.
void wyjmij(worek *w)
{
    (*w->owner)->item_count -= w->item_count;
    w->owner = desk->me;
}

// Wyjmuje przedmiot p z worka i kładzie na biurku.
// Założenie: Przedmiot p był w worku leżącym na biurku.
void wyjmij(przedmiot *p)
{
    (*p->owner)->item_count -= 1;
    p->owner = desk->me;
}

// Wynik: numer worka, w którym znajduje się worek w (-1 jeśli na biurku).
int w_ktorym_worku(worek *w)
{
    return (*w->owner)->id;
}

// Wynik: numer worka, w którym znajduje się przedmiot p (-1 jeśli na biurku).
int w_ktorym_worku(przedmiot *p)
{
    return (*p->owner)->id;
}

// Wynik: liczba przedmiotów zawartych (bezpośrednio i pośrednio) w worku w
int ile_przedmiotow(worek *w)
{
    return w->item_count;
}

// Cała zawartość worka w ląduje na biurku, a wszystko, co poza workiem w
// znajdowało się bezpośrednio na biurku, ląduje wewnątrz worka w.
void na_odwrot(worek *w)
{
    std::swap(*(w->me), *(desk->me));
    std::swap(w->me, desk->me);
    w->item_count = (int)item_pointers.size() - w->item_count;
    w->owner = desk->me;
}

// Kończy i zwalnia pamięć
void gotowe()
{
    for (auto p : item_pointers)
        delete (p);
    for (auto w : worek_pointers)
        delete (w->me),
            delete (w);
}
