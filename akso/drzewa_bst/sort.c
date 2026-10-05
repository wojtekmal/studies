#include <stdlib.h>
#include <stdio.h>

typedef struct Node* Tree;

struct Node
{
    int value;
    Tree left, right;
};

void insert(Tree *treePtr, int x)
{
    if (*treePtr == 0)
    {
        *treePtr = malloc(sizeof(struct Node));
        (*treePtr)->value = x;
        (*treePtr)->left = 0;
        (*treePtr)->right = 0;
        return;
    }
    
    if (x == (*treePtr)->value) return;

    if (x < (*treePtr)->value) insert(&((*treePtr)->left), x);
    if (x > (*treePtr)->value) insert(&((*treePtr)->right), x);
}

void printAll(Tree t)
{
    if (t == 0) return;

    printAll(t->left);
    printf("%d\n", t->value);
    printAll(t->right);
}

void removeAll(Tree t)
{
    if (t == 0) return;
    
    removeAll(t->left);
    removeAll(t->right);
    free(t);
}

int main()
{
    Tree tree = 0;
    int a;

    while (scanf("%d", &a) != EOF)
    {
        insert(&tree, a);
    }

    printAll(tree);
    removeAll(tree);
}