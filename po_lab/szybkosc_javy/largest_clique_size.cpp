#include <iostream>
#include <vector>
#include <cstdint>
#include <bit>
#include <algorithm>
using namespace std;

class LargestCliqueSize
{
private:
    int n, m;
    vector<vector<bool>> adjacent;

    void input()
    {
        cin >> n >> m;
        adjacent = vector<vector<bool>>(n, vector<bool>(n));

        for (int i = 0; i < m; i++)
        {
            int u, v;
            cin >> u >> v;
            adjacent[u][v] = true;
            adjacent[v][u] = true;
        }
    }

    bool check_mask(uint64_t mask)
    {
        for (int i = 0; i < n; i++)
        {
            if (!(mask & (1ULL << i))) continue;
            
            for (int j = i + 1; j < n; j++)
            {
                if (!(mask & (1ULL << j))) continue;
                if (!adjacent[i][j]) return false;
            }
        }

        return true;
    }

public:
    void find_largest_clique_size()
    {
        input();
        int result = 0;

        for (uint64_t mask = 0; mask < 1ULL << n; mask++)
        {
            if (check_mask(mask)) result = max(result, popcount(mask));
        }

        cout << result << "\n";
    }
};

int main()
{
    LargestCliqueSize counter{};
    counter.find_largest_clique_size();
}