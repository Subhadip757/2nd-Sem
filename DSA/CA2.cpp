#include <bits/stdc++.h>
using namespace std;

class Queue
{
public:
    vector<string> arr;
    int size;
    int front, rear;

    Queue()
    {
        size = 1000001;
        arr.resize(size, "");
        front = rear = -1;
    }

    void push(string val)
    {
        if (rear == size)
        {
            cout << "Queue is full" << endl;
        }
        else if (isEmpty())
        {
            front = rear = 0;
            arr[rear] = val;
            rear++;
        }
        else
        {
            arr[rear] = val;
            rear++;
        }
    }

    void pop()
    {
        if (isEmpty())
        {
            return;
        }
        arr[front] = "";
        front++;
        if (front == rear)
        {
            front = rear = -1;
        }
    }

    string getFront()
    {
        if (front == -1)
        {
            return "Underflow";
        }
        else
        {
            return arr[front];
        }
    }

    bool isEmpty()
    {
        return front == -1;
    }
};

int main()
{

    Queue q;
    q.push("Gandor");
    q.push("Elric");
    q.push("Merio");
    q.push("Zara");
    q.push("Nyra");

    cout << "First Element: " << q.getFront() << endl;

    while (!q.isEmpty())
    {
        string ans = q.getFront();
        q.pop();

        cout << ans << " ";
    }
}