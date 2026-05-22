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

int read_string(char** result_string)
{
    size_t allocation_size = 1;
    *result_string = malloc(allocation_size);
    if (result_string == nullptr) return 1;

    int result_size = 0;
    int eof_pos = BUFFER_SIZE;

    while (true)
    {
        if (buffer_pos == 0)
        {
            ssize_t read_result = read(0, buffer, BUFFER_SIZE);
            
            if (read_result == -1)
            {
                free(*result_string);
                return 1;
            }

            eof_pos = read_result;
        }

        if (result_size == allocation_size)
        {
            *result_string = realloc(*result_string, allocation_size << 1);
            if (result_string == nullptr) return 1;
        }

        if (buffer_pos == eof_pos)
        {
            (*result_string)[result_size] = 0;
            return 2;
        }
        else if (buffer[buffer_pos] == '\n')
        {
            (*result_string)[result_size] = 0;
            buffer_pos = (buffer_pos + 1) % BUFFER_SIZE;
            return 0;
        }
        else if (buffer[buffer_pos] < 33 || buffer[buffer_pos] > 126)
        {
            free(*result_string);
            return 1;
        }
        else
        {
            (*result_string)[result_size] = buffer[buffer_pos];
            buffer_pos = (buffer_pos + 1) % BUFFER_SIZE;
            result_size++;
        }
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

    bool eof_reached = false;

    char* start_string;
    int read_start_result = read_string(&start_string);
    if (read_start_result == 1) return 1;
    else if (read_start_result == 2) eof_reached = true;

    while (true)
    {
        char* exchange_string;
        int read_exchange_result = read_string(&exchange_string);

        exchange_strings[exchange_string[0]] = exchange_string + 1;
        exchange_string = nullptr;
    }

    for (uint64_t i = 0; i < start_size - 1; i++)
    {
        dfs(start_string[i], n);
    }

    puts("");
}