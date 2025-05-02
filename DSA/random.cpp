#include <bits/stdc++.h>
using namespace std;

void dfs(int node, vector<vector<int>> &adj, int V, vector<bool> &vis, vector<int> &ans){
    vis[node] = 1;
    ans.push_back(node);

    for(auto it : adj[node]){
        if(!vis[it]){
            dfs(it, adj, V, vis, ans);
        }
    }
}

int main()
{
    int V = 5;
    vector<vector<int>> adj(V + 1);

    for (int i = 0; i <= V; i++)
    {
        int u;
        int v;
        cin >> u >> v;

        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    vector<int> ans;
    vector<bool> vis(V + 1, 0);

    dfs(0, adj, V, vis, ans);

    for (int i = 0; i < ans.size(); i++)
    {
        cout << ans[i] << " ";
    }
    return 0;
}