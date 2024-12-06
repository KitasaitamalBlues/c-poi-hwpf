#include "graalvm/doc.h"
#include "read_doc.h"
#include <cstdlib>
#include <cstring>
#include <iostream>
#include <stdio.h>
#include <stdlib.h>

// #include "java/libenvmap.h"
extern "C"
{
    EXPORT_SYMBOL char *read_doc(char *f);
}
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

    auto res1 = read_doc(const_cast<char *>(f));

    std::cout << res1;
    std::cout << "end";
}

void *mem_doc_addr = 0;
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

        int length = strlen(res);

        char *res_copy = new char[length + 1]{};

        memcpy(res_copy, res, length);

        graal_tear_down_isolate(thread);
        if (mem_doc_addr != nullptr)
        {
            std::free(mem_doc_addr);
            mem_doc_addr = nullptr;
        }
        return res_copy;
    }
}