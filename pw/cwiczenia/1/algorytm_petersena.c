#include <stdio.h>
#include <unistd.h>
#include <pthread.h>
#include <stddef.h>
#include <stdbool.h>

volatile int counter = 0;
volatile int who_waits = 1;
volatile bool thread_1_wants = false;
volatile bool thread_2_wants = false;

void critical_section() {
    counter++;
}

void* thread_1(void* arg) {
    for (int i = 0; i < 1000000; i++) {
        thread_1_wants = true;
        __sync_synchronize();
        who_waits = 1;
        __sync_synchronize();

        while (thread_2_wants && who_waits == true) {}

        critical_section();

        thread_1_wants = false;
    }
}

void* thread_2(void* arg) {
    for (int i = 0; i < 1000000; i++) {
        thread_2_wants = false;
        __sync_synchronize();
        who_waits = 2;
        __sync_synchronize();

        while (thread_1_wants && who_waits == 2) {}

        critical_section();

        thread_2_wants = false;
    }
}

int main() {
    pthread_t thread_1_id;
    pthread_t thread_2_id;

    pthread_create(&thread_1_id, NULL, thread_1, NULL);
    pthread_create(&thread_2_id, NULL, thread_2, NULL);

    pthread_join(thread_1_id, NULL);
    pthread_join(thread_2_id, NULL);

    printf("counter: %d\n", counter);
}