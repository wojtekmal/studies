#ifndef WORKI_H
#define WORKI_H

// Mój kod zaczyna się tu.
#include <variant>
struct przedmiot;
struct worek;
struct ListBegin;
struct ListEnd;

// Należy wypełnić
struct przedmiot {
    // Twój kod zaczyna się tu
    std::variant<przedmiot*, worek*, ListBegin*> left;
    std::variant<przedmiot*, worek*, ListEnd*> right;
    ListBegin* begin;
    int index;
    // A kończy tu
};

// Należy wypełnić
struct worek {
    // Twój kod zaczyna się tu
    std::variant<przedmiot*, worek*, ListBegin*> left;
    std::variant<przedmiot*, worek*, ListEnd*> right;
    ListBegin* begin;
    int index;
    int tree_size;
    ListBegin* contents;
    // A kończy tu
};

struct ListBegin
{
    worek* owner;
    std::variant<przedmiot*, worek*, ListEnd*> right;
};

struct ListEnd
{
    std::variant<przedmiot*, worek*, ListBegin*> left;
};

// Nowy przedmiot na biurku
przedmiot *nowy_przedmiot();

// Nowy worek na biurku; otrzymuje kolejny numer, począwszy od 0.
worek *nowy_worek();

// Wkłada przedmiot co do worka gdzie.
// Założenie: co i gdzie leżą na biurku.
void wloz(przedmiot *co, worek *gdzie);

// Wkłada worek co do worka gdzie.
// Założenie: co i gdzie leżą na biurku.
void wloz(worek *co, worek *gdzie);

// Wyjmuje przedmiot p z worka i kładzie na biurku.
// Założenie: Przedmiot p był w worku leżącym na biurku.
void wyjmij(przedmiot *p);

// Wyjmuje worek w z worka i kładzie na biurku.
// Założenie: Worek w był w worku leżącym na biurku.
void wyjmij(worek *w);

// Wynik: numer worka, w którym znajduje się przedmiot p (-1 jeśli na biurku).
int w_ktorym_worku(przedmiot *p);

// Wynik: numer worka, w którym znajduje się worek w (-1 jeśli na biurku).
int w_ktorym_worku(worek *w);

// Wynik: liczba przedmiotów zawartych (bezpośrednio i pośrednio) w worku w
int ile_przedmiotow(worek *w);

// Cała zawartość worka w ląduje na biurku, a wszystko, co poza workiem w
// znajdowało się bezpośrednio na biurku, ląduje wewnątrz worka w.
void na_odwrot(worek *w);

// Kończy i zwalnia pamięć
void gotowe();

#endif
