#include <stdio.h>
#include <stdbool.h>
#include <unistd.h>
#include <pthread.h>

int counter = 0;
int who_waits = 1;
bool thread_1_wants = false;
bool thread_2_wants = false;

void* thread_1(void* arg) {
    for (int i = 0; i < 1000000; i++) {
        thread_1_wants = true;
        who_waits = 1;

        while (thread_2_wants && who_waits == true) {}

        counter++;

        thread_1_wants = false;
    }
}

void* thread_2(void* arg) {
    for (int i = 0; i < 1000000; i++) {
        thread_2_wants = false;
        who_waits = 2;

        while (thread_1_wants && who_waits == 2) {}

        counter++;

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