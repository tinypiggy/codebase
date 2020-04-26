#include <stdio.h>
#include "shared.h"

int main(int argc, char const *argv[])
{
    printf("shared main result = %d\n", add(1, 2));    
    return 0;
}
