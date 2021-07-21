import java.util.*;

class Graph
{
    // Data Members
    int V; // Number of vertices
    ArrayList<ArrayList<Integer>> adjListArray; // A list of lists that holds each vertex and its adjacent vertices
    HashSet<ArrayList<Integer>> components; // A hashset of lists that holds the graph's connected components and their vertices
    List<List<Integer>> allPaths; // A list of lists that holds each vertex and its paths to all other vertices

    // Constructor
    Graph(int V)
    {
        this.V = V;
        adjListArray = new ArrayList<>();
        components = new HashSet<>();
        allPaths = new ArrayList<>();

        // Create A list of V empty lists (basic graph vertices information)
        for (int i = 0; i < V; i++)
            adjListArray.add(i, new ArrayList<>());
    }

    /**
     * @param src source node
     * @param dest destination node
     * Adds an edge between two vertices
     * An edge means that there is a direct path from a vertex to another vertex
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
     * @param start start index
     * @param end end index
     * @param isVisited keeps track of vertices in current path
     * @param localPathList stores vertices in the current path
     * Find all paths from start to end node
     */
    public void findAllPaths(Integer start, Integer end, boolean[] isVisited, List<Integer> localPathList)
    {
        // If the start node is also the end node, add a new list to the paths list
        if (start.equals(end))
        {
            allPaths.add(new ArrayList<>(localPathList));
            return;
        }

        // Flag current node as visited
        isVisited[start] = true;

        // For each adjacent node (from current node)
        // If current node was not yet visited, add the node to the path list
        // Recursive call from an adjacent node
        // Remove current node from path list
        for (Integer i : adjListArray.get(start))
        {
            if (!isVisited[i])
            {
                // Store current node in the path
                localPathList.add(i);
                findAllPaths(i, end, isVisited, localPathList);

                // Remove current node from the path
                localPathList.remove(i);
            }
        }

        // Flag current node as not visited
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
        // Flag current node as visited and add to the list
        visited[v] = true;
        arr.add(v);
        System.out.print(v + " ");

        // For each adjacent node to the current node, call DFS recursively if not already visited
        for (int x : adjListArray.get(v))
        {
            if (!visited[x])
                DFS(x, visited, arr);
        }

        // Add the current component (list) to the components hashset
        components.add(arr);
    }

    /**
     * @return a list that represents the connected components in the graph
     **/
    Collection<ArrayList<Integer>> connectedComponents()
    {
        // Flag all the vertices as not visited (default value = false)
        boolean[] visited = new boolean[V];

        // For each vertex, call DFS if not already visited
        for (int v = 0; v < V; ++v)
        {
            if (!visited[v])
            {
                ArrayList<Integer> arr = new ArrayList<>();
                DFS(v, visited, arr);
                System.out.println();
            }
        }

        return components;
    }
}
