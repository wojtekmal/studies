#include <bits/stdc++.h>
#include "worki.h"
using namespace std;

worek get_desk()
{
    ListBegin* desk_begin = new ListBegin;
    ListEnd* desk_end = new ListEnd;
    worek desk;

    desk_begin->owner = &desk;
    desk_begin->right = desk_end;
    desk_end->left = desk_begin;

    desk =
    {
        .left = static_cast<ListBegin*>(nullptr),
        .right = new ListEnd,
        .begin = static_cast<ListBegin*>(nullptr),
        .index = -1,
        .tree_size = 0,
        .contents = desk_begin
    };

    return desk;
}

worek desk = get_desk();

int bag_count = 0;
int item_count = 0;

template<typename T>
void insert_loose(worek* bag, T* thing)
{
    auto set_left_to_thing = [&](auto* typed_first_in_list)
    {
        typed_first_in_list->left = thing;
    };

    visit(set_left_to_thing, bag->contents->right);
    thing->right = bag->contents->right;
    bag->contents->right = thing;
    thing->left = bag->contents;
    thing->begin = bag->contents;

    using ThingType = decay_t<T>;
    int diff;
    if constexpr (is_same_v<ThingType, przedmiot>) diff = 1;
    else if constexpr (is_same_v<ThingType, worek>) diff = thing->tree_size;
    bag->tree_size += diff;
    if (bag != &desk) desk.tree_size += diff;
}

template<typename T>
void make_loose(T* thing)
{
    auto unlink_left = [&](auto* typed_left)
    {
        typed_left->right = thing->right;
    };
    visit(unlink_left, thing->left);

    auto unlink_right = [&](auto* typed_right)
    {
        typed_right->left = thing->left;
    };
    visit(unlink_right, thing->right);

    worek* bag = thing->begin->owner;
    using ThingType = decay_t<T>;
    int diff;
    if constexpr (is_same_v<ThingType, przedmiot>) diff = 1;
    else if constexpr (is_same_v<ThingType, worek>) diff = thing->tree_size;
    bag->tree_size -= diff;
    if (bag != &desk) desk.tree_size -= diff;
}

przedmiot *nowy_przedmiot()
{
    przedmiot* item = new przedmiot;
    item->index = item_count++;
    insert_loose(&desk, item);
    //printf("w nowy_przedmiot\n");
    //printf("desk.tree_size: %d\n", desk.tree_size);
    return item;
}

worek *nowy_worek()
{
    worek* bag = new worek;
    bag->index = bag_count++;
    bag->tree_size = 0;

    ListBegin* bag_begin = new ListBegin;
    ListEnd* bag_end = new ListEnd;
    bag_begin->owner = bag;
    bag_begin->right = bag_end;
    bag_end->left = bag_begin;
    bag->contents = bag_begin;

    insert_loose(&desk, bag);
    return bag;
}

void wloz(przedmiot *co, worek *gdzie)
{
    make_loose(co);
    insert_loose(gdzie, co);
}

void wloz(worek *co, worek *gdzie)
{
    make_loose(co);
    insert_loose(gdzie, co);
}

void wyjmij(przedmiot *p)
{
    make_loose(p);
    insert_loose(&desk, p);
}

void wyjmij(worek *w)
{
    make_loose(w);
    insert_loose(&desk, w);
}

int w_ktorym_worku(przedmiot *p)
{
    return p->begin->owner->index;
}

int w_ktorym_worku(worek *w)
{
    return w->begin->owner->index;
}

int ile_przedmiotow(worek *w)
{
    return w->tree_size;
}

void na_odwrot(worek *w)
{
    //printf("w na_odwrot\n");
    //printf("w->tree_size: %d, desk.tree_size: %d\n", w->tree_size, desk.tree_size);
    make_loose(w);
    swap(w->contents, desk.contents);
    swap(w->contents->owner, desk.contents->owner);
    insert_loose(&desk, w);
    w->tree_size = desk.tree_size - w->tree_size;
}

template<typename T>
void recursive_free(T* thing)
{
    using ThingType = decay_t<T>;
    auto free_in_visit = [](auto* pointer_to_free)
    {
        recursive_free(pointer_to_free);
        delete pointer_to_free;
    };

    if constexpr (is_same_v<ThingType, przedmiot> || is_same_v<ThingType, ListBegin>)
    {
        visit(free_in_visit, thing->right);
    }
    else if constexpr (is_same_v<ThingType, worek>)
    {
        visit(free_in_visit, thing->right);
        recursive_free(thing->contents);
        delete thing->contents;
    }
}

void gotowe()
{
    recursive_free(&desk);
}