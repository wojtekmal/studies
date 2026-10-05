#include <bits/stdc++.h>
using namespace std;
#define ll long long

int main()
{
    ll n;
    cin >> n;

    vector<ll> types(n);
    vector<ll> positions(n);

    for (ll i = 0; i < n; i++)
    {
        cin >> types[i] >> positions[i];
    }

    ll global_min = UINT_MAX, global_max = 0;

    for (ll i = 0; i < n; i++)
    {
        for (ll j = i + 1; j < n; j++)
        {
            for (ll k = j + 1; k < n; k++)
            {
                if (types[i] == types[j] || types[i] == types[k] || types[j] == types[k])
                {
                    continue;
                }

                ll first_dist = positions[j] - positions[i];
                ll second_dist = positions[k] - positions[j];

                global_min = min(global_min, max(first_dist, second_dist));
                global_max = max(global_max, min(first_dist, second_dist));
            }
        }
    }

    if (global_min == UINT_MAX) global_min = 0;
    cout << global_min << " " << global_max << "\n";
}