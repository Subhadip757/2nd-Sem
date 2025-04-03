#include <bits/stc++.h>
using namespace std;

class Node
{
public:
    int val;
    Node *next;

    Node(int d)
    {
        this->val = d;
        this->next = NULL;
    }
};

void insertTail(Node *&tail, int val)
{
    Node *temp = new Node(val);
    tail->next = temp;
    tail = tail->next;
}

int main()
{
}