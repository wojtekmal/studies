#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdlib.h>

int main()
{
    int fd = open("file_four.in", O_RDONLY);
    char* buffer = malloc(38);
    read(fd, buffer, 38);
    printf(buffer);
}