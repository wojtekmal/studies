#include <iostream>
#include <queue>
#include <vector>
#include <array>
#include <cassert>

template<typename T>
struct MinimumQueue
{
    struct Element
    {
        int64_t index;
        T value;
    };

    std::deque<Element> elements;

    void move_start(int64_t new_start)
    {
        while (!elements.empty() && elements.front().index < new_start)
        {
            elements.pop_front();
        }
    }

    void insert(int64_t index, T value)
    {
        while (!elements.empty() && value < elements.back().value)
        {
            elements.pop_back();
        }

        elements.push_back(Element{index, value});
    }

    bool empty()
    {
        return elements.empty();
    }

    T minimum_value()
    {
        return elements.front().value;
    }
};

struct Segment
{
    int64_t l, r, x_l, x_r;
};

int64_t sq(int64_t a)
{
    return a * a;
}

bool operator<(Segment a, Segment b)
{
    __int128_t first_proportion = (__int128_t) sq(a.x_r - a.x_l) * (b.r - b.l + 1);
    __int128_t second_proportion = (__int128_t) sq(b.x_r - b.x_l) * (a.r - a.l + 1);

    if (first_proportion == second_proportion)
    {
        return a.l < b.l;
    }

    return first_proportion > second_proportion;
}

int main()
{
    int64_t n, U;
    std::cin >> n >> U;

    std::vector<std::array<int64_t, 2>> points(n + 1);
    for (int i = 1; i <= n; i++) std::cin >> points[i][0] >> points[i][1];

    MinimumQueue<int64_t> smallest_ys;
    MinimumQueue<int64_t> largest_ys;
    MinimumQueue<Segment> best_segments;
    int64_t r = 0;

    auto can_increment_r = [&]()
    {
        if (r == n) return false;
        int64_t new_y = points[r + 1][1];

        if (smallest_ys.empty()) return true;
        int64_t smallest_y = smallest_ys.minimum_value();
        int64_t largest_y = largest_ys.minimum_value();

        if (std::abs(smallest_y - new_y) > U) return false;
        if (std::abs(largest_y + new_y) > U) return false;
        return true;
    };

    for (int64_t l = 1; l <= n; l++)
    {
        bool r_was_incremented = false;

        while (can_increment_r())
        {
            r_was_incremented = true;
            r++;
            smallest_ys.insert(r, points[r][1]);
            largest_ys.insert(r, -points[r][1]);
        }
        
        if (r_was_incremented)
        {
            best_segments.insert(r, Segment{l, r, points[l][0], points[r][0]});
        }

        assert(!best_segments.empty());
        auto best_segment = best_segments.minimum_value();
        std::cout << best_segment.l << " " << best_segment.r << "\n";

        smallest_ys.move_start(l + 1);
        largest_ys.move_start(l + 1);
        best_segments.move_start(l + 1);
    }
}