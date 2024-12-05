#include "graalvm/libdoc.h"
#include "read_doc.h"
#include <iostream>
#include <stdio.h>
#include <stdlib.h>

// #include "java/libenvmap.h"

int main(int argc, char **argv)
{
    const char *f = "C:/Users/hua/Desktop/人员信息表.doc";
    graal_isolate_t *isolate = NULL;
    graal_isolatethread_t *thread = NULL;

    if (graal_create_isolate(NULL, &isolate, &thread) != 0)
    {
        fprintf(stderr, "initialization error\n");
        return 1;
    }
    char *res = readDOC(thread, const_cast<char *>(f));
    std::cout << res;

    graal_tear_down_isolate(thread);
}

extern "C"
{
    EXPORT_SYMBOL char *read_doc(char *f)
    {
        graal_isolate_t *isolate = NULL;
        graal_isolatethread_t *thread = NULL;

        if (graal_create_isolate(NULL, &isolate, &thread) != 0)
        {
            fprintf(stderr, "initialization error\n");
            return const_cast<char *>("initialization error\n");
        }
        auto res = readDOC(thread, f);

        graal_tear_down_isolate(thread);

        return res;
    }
}