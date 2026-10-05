#include <stdlib.h>
#include <math.h>
#include <stdbool.h>
#include <stdio.h>
typedef unsigned int uint;

double eps = 0.000001;

typedef struct
{
    double x, y;
} Point;

typedef struct
{
    char type;

    // Used by rectangle and folded cards.
    Point p_1, p_2;

    // Used by circle cards.
    Point center;
    double radius;

    // Used by folded cards.
    uint prev;
} Card;

double dist(Point p_1, Point p_2)
{
    return sqrt(pow(p_1.x - p_2.x, 2) + pow(p_1.y - p_2.y, 2));
}

Point get_reflection(Point point, Point p_1, Point p_2)
{
    // Calculates the dot product of p_2 - p_1 and point - p_1. Makes use of
    // the formula ab = |a||b|sin(alpha). |a|sin(alpha)/|b|, where a = p_2-p_1
    // and b = point-p_1, happens to be the proportion (proj-p_1)/(p_2-p_1),
    // where proj is the projection of point onto the line passing through p_1
    // and p_2.
    Point axis_vector = {p_2.x - p_1.x, p_2.y - p_1.y};
    double axis_vector_len = dist(p_1, p_2);
    Point p_1_to_point_vector = {point.x - p_1.x, point.y - p_1.y};

    double dot_product = axis_vector.x * p_1_to_point_vector.x +
        axis_vector.y * p_1_to_point_vector.y;
    
    double projection_ratio = dot_product / pow(axis_vector_len, 2);
    Point projection = {p_1.x + axis_vector.x * projection_ratio,
        p_1.y + axis_vector.y * projection_ratio};
    
    return (Point) {2 * projection.x - point.x, 2 * projection.y - point.y};
}

bool is_on_folded_side(Point point, Point p_1, Point p_2)
{
    // Calculates the signed area of the triangle with verticies point, p_1,
    // p_2 using the shoelace formula. Returns true if point is on the side
    // that was folded over.
    double half_area = p_1.x * p_2.y + p_2.x * point.y + point.x * p_1.y
        - p_1.y * p_2.x - p_2.y * point.x - point.y * p_1.x;
    return half_area < 0;
}

int how_many_layers(Card* cards, uint card_index, Point quest)
{
    Card card = cards[card_index];

    if (card.type == 'P')
    {
        return (card.p_1.x < quest.x + eps && quest.x < card.p_2.x + eps &&
                card.p_1.y < quest.y + eps && quest.y < card.p_2.y + eps) ? 1 : 0;
    }
    if (card.type == 'K')
    {
        return (dist(card.center, quest) < card.radius + eps) ? 1 : 0;
    }
    else
    {
        Point reflection = get_reflection(quest, card.p_1, card.p_2);
        
        if (dist(reflection, quest) < eps)
        {
            return how_many_layers(cards, card.prev, quest);
        }
        else if (is_on_folded_side(quest, card.p_1, card.p_2))
        {
            return 0;
        }
        else
        {
            return how_many_layers(cards, card.prev, quest) +
                how_many_layers(cards, card.prev, reflection);
        }
    }
}

int main()
{
    uint n, q;
    scanf("%u %u", &n, &q);

    Card* cards = malloc(n * sizeof(Card));

    for (uint i = 0; i < n; i++)
    {
        scanf(" %c", &cards[i].type);

        if (cards[i].type == 'P')
        {
            Point p_1, p_2;
            scanf("%lf %lf %lf %lf", &p_1.x, &p_1.y, &p_2.x, &p_2.y);
            cards[i].p_1 = p_1;
            cards[i].p_2 = p_2;
        }
        else if (cards[i].type == 'K')
        {
            Point center;
            double radius;
            scanf("%lf %lf %lf", &center.x, &center.y, &radius);
            cards[i].center = center;
            cards[i].radius = radius;
        }
        else
        {
            uint prev;
            Point p_1, p_2;
            scanf("%u %lf %lf %lf %lf", &prev, &p_1.x, &p_1.y, &p_2.x, &p_2.y);
            cards[i].prev = prev - 1;
            cards[i].p_1 = p_1;
            cards[i].p_2 = p_2;
        }
    }

    for (uint i = 0; i < q; i++)
    {
        Point quest;
        uint card_index;
        scanf("%u %lf %lf", &card_index, &quest.x, &quest.y);

        printf("%d\n", how_many_layers(cards, card_index - 1, quest));
    }
}