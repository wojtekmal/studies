#include <stdio.h>
#include <limits.h>
#include <inttypes.h>

int convert_to_int(char* n_string, uint32_t* n)
{
    *n = 0;
    
    for (int i = 0; n_string[i] != 0; i++)
    {
        if (n > UINT_MAX / 10u)
    }
}

int main(int argument_count, char** arguments)
{
    if (argument_count != 1) return 1;

    char* n_string = arguments[0];
    uint32_t n;
    int conversion_result = convert_to_int(n_string, &n);
}