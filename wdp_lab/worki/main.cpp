#include "worki.h"
#include <assert.h>
#include <vector>
#include <typeinfo>
#include <bits/stdc++.h>

using namespace std;

template<typename T>
void recursive_print(T* thing)
{
    auto print_in_visit = [](auto* pointer_to_free)
    {
        recursive_print(pointer_to_free);
    };

    using ThingType = decay_t<T>;
    printf("start of type: %s, address: %p\n", typeid(ThingType).name(), (void*) thing);

    if constexpr (is_same_v<ThingType, przedmiot>)
    {
        printf("item id: %d\n", thing->index);
        printf("parent id: %d\n", w_ktorym_worku(thing));
        visit(print_in_visit, thing->right);
    }
    else if constexpr (is_same_v<ThingType, ListBegin>)
    {
        visit(print_in_visit, thing->right);
    }
    else if constexpr (is_same_v<ThingType, worek>)
    {
        printf("bag id: %d\n", thing->index);
        printf("parent id: %d\n", w_ktorym_worku(thing));
        printf("bag tree_size: %d\n", thing->tree_size);
        visit(print_in_visit, thing->right);
        recursive_print(thing->contents);
    }

    printf("end of type: %s, address: %p\n", typeid(ThingType).name(), (void*) thing);
}

int main(){
    int n = 1;
    vector<przedmiot*> przedmioty;
    vector<worek*> worki;

    for (int i = 0; i < n; ++i){
        przedmioty.push_back(nowy_przedmiot());
        worki.push_back(nowy_worek());
        wloz(przedmioty[i], worki[i]);
        if (i > 0)
            wloz(worki[i - 1], worki[i]);
    }

    for (int i = 0; i < n; ++i){
        assert(w_ktorym_worku(przedmioty[i]) == i);
        assert(w_ktorym_worku(worki[i]) == (i < n - 1 ? i + 1 : -1));
        assert(ile_przedmiotow(worki[i]) == i + 1);
    }

    for (int i = n - 1; i >= 0; --i){
        if (i < n - 1)
            wyjmij(worki[i]);
        wyjmij(przedmioty[i]);
    }

    for (int i = 0; i < n; ++i){
        assert(w_ktorym_worku(przedmioty[i]) == -1);
        assert(w_ktorym_worku(worki[i]) == -1);
        assert(ile_przedmiotow(worki[i]) == 0);
    }

    //recursive_print(worki[0]);
    //printf("\n");

    na_odwrot(worki[0]);

    //recursive_print(worki[0]);

    assert(w_ktorym_worku(przedmioty[0]) == 0);
    assert(w_ktorym_worku(worki[0]) == -1);
    assert(ile_przedmiotow(worki[0]) == n);
    for (int i = 1; i < n; ++i){
        assert(w_ktorym_worku(przedmioty[i]) == 0);
        assert(w_ktorym_worku(worki[i]) == 0);
        assert(ile_przedmiotow(worki[i]) == 0);
    }

    gotowe();

    return 0;
}
