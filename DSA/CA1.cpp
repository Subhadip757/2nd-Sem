#include <iostream>
#include <stack>
using namespace std;

void level1(stack<int> st)
{
    while (!st.empty())
    {
        int elem = st.top();
        st.pop();

        cout << elem << " ";
    }
    cout << endl;
}

int maxStack(stack<int> st)
{
    int maxElem = INT_MIN;

    while (!st.empty())
    {
        int elem = st.top();
        st.pop();

        if (elem > maxElem)
        {
            maxElem = elem;
        }
    }
    return maxElem;
}

int main()
{
    stack<int> st;

    st.push(6);
    st.push(2);
    st.push(9);
    st.push(3);
    st.push(5);

    level1(st);

    cout << maxStack(st);
}
