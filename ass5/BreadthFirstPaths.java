import java.util.Stack;

import Queue.Queue;

public class BreadthFirstPaths {
    private boolean[] marked;
    private int[] edgeTo;
    private final int s;

    public BreadthFirstPaths(Graph G,int s)
    {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        this.s = s;
        bfs(G,s);
    }
    private void bfs(Graph G,int s)
    {
        Queue<Integer> queue = new Queue<Integer>();
        marked[s] = true;
        queue.enqueue(s);
        while(!queue.isEmpty())
        {
            int v = queue.dequeue();//从队列中删除改顶点
            for(int w:G.adj(v))//对该顶点相邻的所有顶点进行遍历，标记为访问过，同时加入队列
            {
                edgeTo[w] = v;
                marked[w] = true;
                queue.enqueue(w);
            }
        }
    }
    public boolean hasPathTo(int v){
        return marked[v];
    }
    public Iterable<Integer> pathTo(int v)
    {
        Stack<Integer> path = new Stack<Integer>();
        while(edgeTo[v] != s)//同上一篇，通过该数组往上回溯
        {
            path.push(v);
            v = edgeTo[v];
        }
        path.push(s);
        return path;
    }
}
