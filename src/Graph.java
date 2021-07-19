import java.util.*;

class Graph
{
    int V;
    ArrayList<ArrayList<Integer>> adjListArray;
    HashSet<ArrayList<Integer>> components;
    List<List<Integer>> allPaths;

    // Constructor
    Graph(int V)
    {
        this.V = V; // define the size of array as number of vertices
        adjListArray = new ArrayList<>();
        components = new HashSet<>();
        allPaths = new ArrayList<>();

        // Create a new list for each vertex such that adjacent nodes can be stored
        for (int i = 0; i < V; i++)
            adjListArray.add(i, new ArrayList<>());
    }

    /**
     * @param src source node
     * @param dest destination node
     * Adds an edge to a graph
    **/
    void addEdge(int src, int dest)
    {
        adjListArray.get(src).add(dest);
    }


    /**
     * @param start the starting node
     * @param end the target of the path
     * @return a List of all the paths from the start to end node
    **/
    public List<List<Integer>> findAllPaths(int start, int end)
    {
        boolean[] isVisited = new boolean[V];
        ArrayList<Integer> pathList = new ArrayList<>();
        pathList.add(start);
        findAllPaths(start, end, isVisited, pathList);

        return allPaths;
    }


    /**
     * Find all paths from 'start' to 'end'.
     * @param start start index
     * @param end end index
     * @param isVisited keeps track of vertices in current path
     * @param localPathList stores vertices in the current path
    **/
    public void findAllPaths(Integer start, Integer end, boolean[] isVisited, List<Integer> localPathList)
    {
        if (start.equals(end))
        {
            allPaths.add(new ArrayList<>(localPathList));

            // If match is found, stop
            return;
        }

        isVisited[start] = true;

        for (Integer i : adjListArray.get(start))
        {
            if (!isVisited[i])
            {
                // Store current node in path
                localPathList.add(i);
                findAllPaths(i, end, isVisited, localPathList);

                // Remove current node in path
                localPathList.remove(i);
            }
        }

        // Flag current node
        isVisited[start] = false;
    }

    /**
     * DFS Algorithm
     * @param v current vertex
     * @param visited checks if we've already visited current vertex
     * @param arr collects the vertices into the component
     */
    void DFS(int v, boolean[] visited, ArrayList<Integer> arr)
    {
        // Mark current node as visited
        visited[v] = true;
        arr.add(v);
        System.out.print(v + " ");

        for (int x : adjListArray.get(v))
        {
            if (!visited[x])
                DFS(x, visited, arr);
        }

        components.add(arr);
    }

//    // DFS algorithm
//    void DFS(int vertex) {
//        visited[vertex] = true;
//        System.out.print(vertex + " ");
//
//        Iterator<Integer> ite = adjLists[vertex].listIterator();
//        while (ite.hasNext()) {
//            int adj = ite.next();
//            if (!visited[adj])
//                DFS(adj);
//        }
//    }

    /**
     * @return a list that represents the connected components in the graph
     */
    Collection<ArrayList<Integer>> connectedComponents()
    {
        // Mark all the vertices as not visited
        boolean[] visited = new boolean[V];

        for (int v = 0; v < V; ++v)
        {
            if (!visited[v])
            {
                // Find all reachable vertices from v
                ArrayList<Integer> arr = new ArrayList<>();
                DFS(v, visited, arr);
                System.out.println();
            }
        }
        return components;
    }
}
