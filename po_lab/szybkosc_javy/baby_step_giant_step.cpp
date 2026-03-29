#include <iostream>
#include <cmath>
#include <unordered_map>
using namespace std;

int64_t discreet_log(int64_t num, int64_t base, int64_t mod)
{
    // Find the smallest exponent p such that base^p % mod == num % mod or
    // return -1 if it doesn't exist. Works for mod that fits in int.

    int64_t sqrt_pow = ceil(sqrt(mod));
    unordered_map<int64_t, int64_t> baby_steps;
    int64_t base_to_sqrt_pow = 1;

    for (int64_t i = 0; i < sqrt_pow; i++)
    {
        base_to_sqrt_pow = base_to_sqrt_pow * base % mod;
    }

    for (int64_t i = 0, baby_step = num; i < sqrt_pow; i++)
    {
        // If baby_step already occurs in the hashmap, then it's value is
        // overwritten, thanks to which we find the smallest exponent later.
        baby_steps[baby_step] = i;
        baby_step = baby_step * base % mod;
    }

    for (int64_t i = 1, giant_step = base_to_sqrt_pow; i <= sqrt_pow; i++)
    {
        if (baby_steps.count(giant_step))
        {
            // The order in which we iterate over the giant steps finds the
            // smaller exponents first.
            return i * sqrt_pow - baby_steps[giant_step];
        }
    }

    return -1;
}

int main()
{
    int64_t base, mod, num_of_log_calculations;
    cin >> base >> mod >> num_of_log_calculations;
    int64_t check_sum = 0;

    for (int64_t i = 1; i <= num_of_log_calculations; i++)
    {
        check_sum += discreet_log(i, base, mod);
    }

    cout << check_sum << "\n";
}