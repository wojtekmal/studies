#include <stdio.h>
#include <limits.h>
#include <inttypes.h>
#include <unistd.h>

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

const int BUFFER_SIZE = 4096;
char buffer[BUFFER_SIZE];
int buffer_pos = 0;

int read_string(char** pointer_to_set)
{
    size_t allocation_size = 1;
    *pointer_to_set = malloc(allocation_size);

    while (true)
    {
        if (buffer_pos == 0)
        {

        }
        realloc(*pointer_to_set, allocation_size << 1);
        ssize_t read_result = read(0, *pointer_to_set + allocation_size, allocation_size);

    }
}

char* exchange_strings[128];

void dfs(char symbol, int height)
{
    //printf("symbol: %c\n", symbol);
    
    if (height == 0 || exchange_strings[symbol] == nullptr)
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
    size_t dummy;
    int start_size = getline(&start_string, &dummy, stdin);

    int exchange_size;
    char* exchange_string = nullptr;

    while ((exchange_size = getline(&exchange_string, &dummy, stdin)) != -1)
    {
        exchange_string[exchange_size - 1] = 0;
        exchange_strings[exchange_string[0]] = exchange_string + 1;
        exchange_string = nullptr;
    }

    for (uint64_t i = 0; i < start_size - 1; i++)
    {
        dfs(start_string[i], n);
    }

    puts("");
}