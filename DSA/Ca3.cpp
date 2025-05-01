#include <bits/stdc++.h>
using namespace std;

// 1 - 2, 1 - 3, 1 - 4, 2 - 4

vector<int> bfs(vector<vector<int>> &adj, int V)
{
    vector<int> ans;
    vector<bool> vis(V + 1, 0);
    queue<int> q;
    q.push(1);
    vis[1] = 1;

    while (!q.empty())
    {
        int node = q.front();
        q.pop();
        ans.push_back(node);

        for (auto it : adj[node])
        {
            if (!vis[it])
            {
                vis[it] = 1;
                q.push(it);
            }
        }
    }

    return ans;
}

int main()
{
    int V = 4;
    vector<vector<int>> adj(V + 1);

    for (int i = 0; i < V; i++)
    {
        int u;
        int v;
        cin >> u >> v;

        adj[u].push_back(v);
        adj[v].push_back(u);
    }

    vector<int> ans = bfs(adj, V);

    for (int i = 0; i < ans.size(); i++)
    {
        cout << ans[i] << " ";
    }

    return 0;
}