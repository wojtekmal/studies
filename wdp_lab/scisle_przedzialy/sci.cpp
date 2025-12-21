#include <bits/stdc++.h>

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
    }

    std::optional<T> minimum_value()
    {
        if (elements.empty()) return std::nullopt;
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
    int64_t first_proportion = sq(a.x_r - a.x_l) * (b.r - b.l + 1);
    int64_t second_proportion = sq(b.x_r - b.x_l) * (a.r - a.l + 1);

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

    std::vector<std::array<int64_t, 2>> points(n);

    for (int i = 0; i < n; i++)
    {
        std::cin >> points[i][0] >> points[i][1];
    }

    MinimumQueue<int64_t> smallest_ys;
    MinimumQueue<int64_t> largest_ys;
    MinimumQueue<Segment> best_segments;
    int64_t r = -1;

    auto can_increment_r = [&]()
    {
        if (r == n + 1) return false;
        int64_t new_y = points[r + 1][1];
        auto smallest_y = smallest_ys.minimum_value();
        auto largest_y = largest_ys.minimum_value();

        if (smallest_y.has_value() && abs(*smallest_y - new_y) > U) return false;
        if (largest_y.has_value() && abs(*largest_y + new_y) > U) return false;
        return true;
    };

    for (int l = 0; l < n; l++)
    {
        if (!can_increment_r()) continue;

        while (can_increment_r())
        {
            r++;
            smallest_ys.insert(r, points[l][1]);
            largest_ys.insert(r, -points[l][1]);
        }

        best_segments.insert(r, Segment{l, r, points[l][0], points[r][0]});
    }
}