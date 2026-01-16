#include <vector>
#include <iostream>
#include <unordered_set>
#include <queue>
#include <numeric>
using namespace std;
typedef unsigned long long ull;

struct Solve
{
    struct State
    {
        vector<ull> volumes;
        int cost;
    };

    int n;
    vector<ull> cups;
    vector<ull> targets;
    unordered_set<ull> hashes;
    queue<State> states;

    int check_easy()
    {
        for (int i = 1; i < n; i++) if (cups[i] != cups[0]) return 0;
        int full_count = 0;

        for (int i = 0; i < n; i++)
        {
            if (targets[i] != 0 && targets[i] != cups[i]) return -1;
            if (targets[i] == cups[i] && cups[i] > 0) full_count++;
        }

        return full_count;
    }

    bool check_easy_2()
    {
        ull unit = cups[0];
        for (int i = 1; i < n; i++) unit = gcd(unit, cups[i]);
        if (unit == 0) return false;
        for (int i = 0; i < n; i++) if (targets[i] % unit != 0) return true;
        return false;
    }

    ull hash_vector(const vector<ull>& nums)
    {
        ull result = 0;
        for (ull num : nums) result = result * 1000'000'007 + num;
        return result;
    }

    void consider_state(State state)
    {
        ull hash = hash_vector(state.volumes);

        if (!hashes.count(hash))
        {
            states.push(state);
            hashes.insert(hash);
        }
    }

    void process_state()
    {
        vector<ull> volumes = states.front().volumes;
        int cost = states.front().cost;
        states.pop();

        for (int i = 0; i < n; i++)
        {
            vector<ull> new_volumes = volumes;
            new_volumes[i] = 0;
            consider_state({new_volumes, cost + 1});
        }

        for (int i = 0; i < n; i++)
        {
            vector<ull> new_volumes = volumes;
            new_volumes[i] = cups[i];
            consider_state({new_volumes, cost + 1});
        }

        for (int i = 0; i < n; i++) 
        {
            for(int j = 0; j < n; j++)
            {
                ull to_pour = min(volumes[i], cups[j] - volumes[j]);
                vector<ull> new_volumes = volumes;
                new_volumes[i] -= to_pour;
                new_volumes[j] += to_pour;
                consider_state({new_volumes, cost + 1});
            }
        }
    }

    void run()
    {
        cin >> n;
        cups = vector<ull>(n);
        targets = vector<ull>(n);
        for (int i = 0; i < n; i++) cin >> cups[i] >> targets[i];

        if (check_easy())
        {
            cout << check_easy() << "\n";
            return;
        }

        if (check_easy_2())
        {
            cout << "-1\n";
            return;
        }

        State start_state{vector<ull>(n), 0};
        hashes.insert(hash_vector(start_state.volumes));
        states.push(start_state);

        while (!states.empty())
        {
            if (states.front().volumes == targets)
            {
                cout << states.front().cost << "\n";
                return;
            }

            process_state();
        }

        cout << "-1\n";
    }
};

int main()
{
    Solve solve;
    solve.run();
}