#include <bits/stdc++.h>
using namespace std;

struct Edge
{
    int u, v;
};

int main()
{
    mt19937 rng{};
    int n;
    cin >> n;
    vector<Edge> edges;

    for (int i = 0; i < n; i++)
    {
        for (int j = i + 1; j < n; j++)
        {
            if (rng() % 2) continue;
            edges.push_back({i, j});
        }
    }

    cout << n << " " << edges.size() << "\n";

    for (const Edge& e : edges)
    {
        cout << e.u << " " << e.v << "\n";
    }
}