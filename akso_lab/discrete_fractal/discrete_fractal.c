#include <stdio.h>
#include <limits.h>
#include <inttypes.h>

int convert_to_int(char* n_string, uint32_t* n)
{
    *n = 0;
    
    for (int i = 0; n_string[i] != 0; i++)
    {
        uint32_t digit = n_string[i] - '0';
        if (digit > 9) return 1;

        if (*n > (UINT_MAX - digit) / 10u) return 1;

        *n = *n * 10 + digit;
    }

    return 0;
}

char* exchange_strings[128];

int main(int argument_count, char** arguments)
{
    if (argument_count != 1) return 1;

    char* n_string = arguments[0];
    uint32_t n;
    int conversion_result = convert_to_int(n_string, &n);
    if (conversion_result != 0) return 1;

    char* start_string = nullptr;
    getline(&start_string, nullptr, 0);

    char* exchange_string;

    while (getline(&exchange_string, nullptr, 0) != -1)
    {
        exchange_strings[exchange_string[0]] = exchange_string + 1;
    }

    for (uint64_t i = 0; start_string[i] != 0; i++)
    {
        dfs(start_string[i]);
    }
}