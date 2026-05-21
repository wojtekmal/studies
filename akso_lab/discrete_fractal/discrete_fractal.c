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

void dfs(char symbol, int height)
{
    if (height == 0)
    {
        putchar(symbol);
        return;
    }

    for (int i = 0; exchange_strings[symbol][i] != 0; i++)
    {
        dfs(exchange_strings[symbol][i], height - 1);
    }
}

int main(int argument_count, char** arguments)
{
    if (argument_count != 2) return 1;

    char* n_string = arguments[1];
    uint32_t n;
    int conversion_result = convert_to_int(n_string, &n);
    if (conversion_result != 0) return 1;

    char* start_string = nullptr;
    getline(&start_string, nullptr, stdin);

    char* exchange_string;

    while (getline(&exchange_string, nullptr, stdin) != -1)
    {
        exchange_strings[exchange_string[0]] = exchange_string + 1;
    }

    for (uint64_t i = 0; start_string[i] != 0; i++)
    {
        dfs(start_string[i], n);
    }
}