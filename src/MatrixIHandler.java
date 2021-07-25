
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class MatrixIHandler implements IHandler
{
    // Data Members
    private Matrix matrix;
    private Index start, end; // Start & End index
    private int row, col;
    int source, target; // Start & End index values
    Graph g;
    int v; // Number of graph nodes
    private ArrayList<ArrayList<Integer>> adjListArray;
    private Index[] fromValueToIndex; // Index of each value
    ArrayList<ArrayList<Index>> allPathsSourceToTarget;

    // Constructor
    public MatrixIHandler()
    {
        this.resetParams();
    }

    /**
     * Changes valued matrix to binary matrix (base form)
     **/
    public void changeNumToBinary()
    {
        for (int i = 0; i < row; i++)
        {
            for (int j = 0; j < col; j++)
            {
                if (matrix.primitiveMatrix[i][j] == -1)
                    matrix.primitiveMatrix[i][j] = 0;
                else
                    matrix.primitiveMatrix[i][j] = 1;
            }
        }
    }

    /**
     * Using Graph.addEdge to add edges for each node
     * @param g a graph
     * @param matrix a matrix
     */
    public void initializeAllEdges(Graph g, Matrix matrix,int type)
    {
        ArrayList<Index> localAdjListArray = new ArrayList<>();

        // For each vertex, get its reachable nodes & add the edges to the graph
        for(int i = 0; i < v; i++)
        {
            localAdjListArray = (ArrayList)matrix.getReachables(fromValueToIndex[i],type);

            for(int j = 0; j < localAdjListArray.size(); j++)
            {
                g.addEdge(i, matrix.primitiveMatrix[localAdjListArray.get(j).getRow()][localAdjListArray.get(j).getColumn()]);
            }
        }
    }

    /**
     * Resets the matrix & the paths data
    **/
    private void resetPaths()
    {
        this.matrix = null;
        this.allPathsSourceToTarget = null;
        this.fromValueToIndex = null;
        this.adjListArray = null;
    }

    /**
     * Resets the inputStream/outputStream parameters
    **/
    private void resetParams()
    {
        this.matrix = null;
        this.start = null;
        this.end = null;
    }

    /**
     * Changes the matrix from Binary to Value
     * 0 becomes -1
     * 1 becomes a number, ordered chromatically to represent each node as a number
     * Initializes fromValueToIndex array
     **/
    public void updateMatrix()
    {
        int numOfVertices = 0;

        for (int i = 0; i < row; i++)
        {
            for (int j = 0; j < col; j++)
            {
                if (matrix.primitiveMatrix[i][j] == 1)
                    matrix.primitiveMatrix[i][j] = numOfVertices++;
                else
                    matrix.primitiveMatrix[i][j] = -1;
            }
        }

        this.v = numOfVertices;
        this.fromValueToIndex = new Index[v];

        for (int i = 0; i < row; i++)
        {
            for (int j = 0; j < col; j++)
            {
                if(matrix.primitiveMatrix[i][j] != -1)
                    fromValueToIndex[matrix.primitiveMatrix[i][j]] = new Index(i, j);
            }
        }
    }

    public Collection<ArrayList<Index>> lightestPaths(int[][] weightsMatrix)
    {
        List<List<Integer>> allPaths = null;
        allPaths = new ArrayList(g.findAllPaths(source, target));

        Iterator itr = allPaths.iterator();
        allPathsSourceToTarget = new ArrayList<>();

        // Get all paths from start to end node
        while (itr.hasNext())
        {
            List<Index> help1 = new ArrayList<>();
            List<Integer> help2;
            help2 = (ArrayList<Integer>) itr.next();
            Iterator itr2 = help2.iterator();

            while (itr2.hasNext())
                help1.add(fromValueToIndex[(Integer) itr2.next()]);

            allPathsSourceToTarget.add((ArrayList<Index>) help1);
        }

        // Calculate the weight of each path from start to end node
        ArrayList<Integer> sumList = new ArrayList<Integer>();
        for(int k = 0; k < allPathsSourceToTarget.size(); k++)
        {
            Iterator itr3 = allPathsSourceToTarget.get(k).iterator();
            Integer sum = 0;

            while (itr3.hasNext())
            {
                Index currIndex = (Index) itr3.next();
                sum += weightsMatrix[currIndex.row][currIndex.column];
            }
            sumList.add(sum);
        }

        Integer min = sumList.get(0);
        for (int i = 1; i < sumList.size(); i++)
        {
            if (sumList.get(i) < min)
            {
                min = sumList.get(i);
            }
        }

        // Store the lightest paths in the list
        Collection<ArrayList<Index>> lightest = new ArrayList<ArrayList<Index>>();
        for (int i = 0; i < sumList.size(); i++)
        {
            if (sumList.get(i) == min)
            {
                lightest.add(allPathsSourceToTarget.get(i));
                //lightest.add(reverseArrayList(allPathsSourceToTarget.get(i)));
            }
        }
        return lightest;
    }

    //Reverse an ArrayList
    public ArrayList<Index> reverseArrayList(ArrayList<Index> alist)
    {
        // Arraylist for storing reversed elements
        ArrayList<Index> revArrayList = new ArrayList<Index>();
        for (int i = alist.size() - 1; i >= 0; i--)
        {
            // Append the elements in reverse order
            revArrayList.add(alist.get(i));
        }
        // Return the reversed arraylist
        return revArrayList;
    }

    /**
     * Looping over all the paths we found at mission2, and sending ths shortest paths among them
     * @return the shortest paths between a given source index to a target
     **/
    public Collection<ArrayList<Index>> shortestPaths()
    {
        if(row > 50 || col > 50)
        {
            System.out.println("Matrix dimensions are too big");
            ArrayList<ArrayList<Index>> result = new ArrayList<>();
            return result;
        }

        // Find all possible paths from start to end
        List<List<Integer>> allPaths = null;
        allPaths = new ArrayList(g.findAllPaths(source, target));

        Iterator itr = allPaths.iterator();
        allPathsSourceToTarget = new ArrayList<>();

        while (itr.hasNext())
        {
            List<Index> help1 = new ArrayList<>();
            List<Integer> help2;

            help2 = (ArrayList<Integer>) itr.next();
            Iterator itr2 = help2.iterator();

            while (itr2.hasNext())
                help1.add(fromValueToIndex[(Integer) itr2.next()]);

            allPathsSourceToTarget.add((ArrayList<Index>) help1);
        }

        // Find & return the shortest paths (can be more than one)
        ArrayList<ArrayList<Index>> shortestPathList = new ArrayList<>();
        int min = -1;

        for(int i=0; i< allPathsSourceToTarget.size(); i++)
        {
            if(min == -1 || allPathsSourceToTarget.get(i).size()<min)
                min = allPathsSourceToTarget.get(i).size();
        }

        System.out.println("allPaths: " + allPathsSourceToTarget);

        for(int i=0; i< allPathsSourceToTarget.size(); i++)
        {
            if(allPathsSourceToTarget.get(i).size() == min)
                shortestPathList.add(new ArrayList(allPathsSourceToTarget.get(i)));
        }

        return shortestPathList;
    }

    /**
     * Finds all of the graph's connected components
     * @return a list of the graph's connected components
     */
    public List<HashSet<Index>> groupsOfOne()
    {
        HashSet<ArrayList<Index>> arr = new HashSet(g.connectedComponents());
        Iterator itr = arr.iterator();
        ArrayList<HashSet<Index>> result = new ArrayList<>();

        while (itr.hasNext())
        {
            HashSet<Index> help1 = new HashSet<>();
            ArrayList<Integer> help2 = new ArrayList<>();

            help2 = (ArrayList<Integer>)itr.next();
            Iterator itr2 = help2.iterator();

            while (itr2.hasNext())
            {
                help1.add(fromValueToIndex[(Integer) itr2.next()]);
            }
            result.add((HashSet<Index>) help1);
        }
        return result;
    }

    /**
     * Checks whether the given index is part of a diagonal
     * @param index
     * @return false if there is a diagonal, else returns true
     */
    public boolean findingDiagonals(Index index){
        int counter1 = 0, counter2 = 0, counter3 = 0, counter4 = 0;
        int extracted = -1;

        try
        {
            extracted = matrix.primitiveMatrix[index.row+1][index.column];
            if(extracted!=-1)
            {
                counter3 += extracted;
                counter4 += extracted;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored) {}

        try
        {
            extracted = matrix.primitiveMatrix[index.row][index.column+1];
            if(extracted!=-1)
            {
                counter2 += extracted;
                counter4 += extracted;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}

        try
        {
            extracted = matrix.primitiveMatrix[index.row-1][index.column];
            if(extracted!=-1)
            {
                counter1 += extracted;
                counter2 += extracted;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}

        try
        {
            extracted = matrix.primitiveMatrix[index.row][index.column-1];
            if(extracted!=-1)
            {
                counter1 += extracted;
                counter3 += extracted;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}

        try
        {
            extracted = matrix.primitiveMatrix[index.row-1][index.column-1];
            if(extracted!=-1)
            {
                counter1 += extracted;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}

        try
        {
            extracted = matrix.primitiveMatrix[index.row+1][index.column-1];
            if(extracted!=-1)
            {
                counter3 += extracted;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}

        try
        {
            extracted = matrix.primitiveMatrix[index.row+1][index.column+1];
            if(extracted!=-1)
            {
                counter4 += extracted;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}

        try
        {
            extracted = matrix.primitiveMatrix[index.row-1][index.column+1];
            if(extracted!=-1)
            {
                counter2 += extracted;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}

        if((counter1 == 2) || (counter2 == 2) || (counter3 == 2) || (counter4 == 2))
            return false;
        else
            return true;
    }

    /**
     * for each component check:
     * if there is a diagonal in one of the indices in the component
     * if the size of the component is 2 or above
     * @param arr of the connected components in the graph
     * @return number of legal submarines in the graph
     **/
    public int submarinesGame(ArrayList<HashSet<Index>> arr)
    {
        int numOfSubs = 0;
        int count = 0;
        boolean flag = true;
        Iterator itr_1 = arr.iterator();

        while (itr_1.hasNext())
        {
            count = 0;
            flag = true;
            HashSet<Index> helper = new HashSet<>();

            helper = (HashSet)itr_1.next();
            Iterator itr_2 = helper.iterator();

            while (itr_2.hasNext())
            {
                count++;
                if(!findingDiagonals((Index)itr_2.next()))
                    flag = false;
            }

            if(flag && count > 1)
                numOfSubs++;
        }
        return numOfSubs;
    }

    @Override
    public void handle(InputStream inClient, OutputStream outClient) throws Exception
    {
        ObjectOutputStream objectOutputStream=new ObjectOutputStream(outClient);
        ObjectInputStream objectInputStream = new ObjectInputStream(inClient);
        this.resetParams();
        boolean work = true;
        while (work)
        {
            switch (objectInputStream.readObject().toString())
            {
                case "stop":
                {
                    work= false;
                    break;
                }
                case "matrix":
                {
                    int[][] primitiveMatrix = (int[][]) objectInputStream.readObject();
                    this.matrix = new Matrix(primitiveMatrix);
                    this.matrix.printMatrix();
                    this.row = matrix.primitiveMatrix.length;
                    this.col = matrix.primitiveMatrix[0].length;

                    updateMatrix();
                    System.out.println("Matrix after update to Valued Matrix");
                    this.matrix.printMatrix();

                    g = new Graph(v);
                    initializeAllEdges(g, matrix,2);
                    System.out.println("Count: " + v);
                    break;
                }
                case "start index":
                {
                    this.start = (Index) objectInputStream.readObject();
                    this.source = matrix.primitiveMatrix[start.row][start.column];
                    break;
                }
                case "end index":
                {
                    this.end = (Index) objectInputStream.readObject();
                    this.target = matrix.primitiveMatrix[end.row][end.column];
                    break;
                }
                case "task 1":
                {
                    List<HashSet<Index>> result = new ArrayList<>();
                    result = groupsOfOne();
                    objectOutputStream.writeObject(result);
                    break;
                }
                case "task 2":
                {
                    List<List<Index>> result = new ArrayList<>();
                    result = (ArrayList) shortestPaths();
                    objectOutputStream.writeObject(result);
                    break;
                }
                case "task 3":
                {
                    ArrayList<HashSet<Index>> arr = new ArrayList<>();
                    arr = (ArrayList) groupsOfOne();
                    changeNumToBinary();
                    int result = submarinesGame(arr);
                    objectOutputStream.writeObject(result);
                    break;
                }
                case "task 4":
                {
                    this.resetPaths();

                    int[][] weightsMatrix = (int[][]) objectInputStream.readObject();
                    row = weightsMatrix.length;
                    col = weightsMatrix[0].length;

                    // Initialize matrix with ones
                    int[][] nodesMatrix = new int[row][col];
                    for(int i = 0; i < row ; i++)
                        for(int j = 0; j < col ; j++)
                            nodesMatrix[i][j] = 1;

                    this.matrix = new Matrix(nodesMatrix);
                    this.matrix.printMatrix();
                    this.source = 3;
                    this.target = 5;

                    updateMatrix();
                    System.out.println("Matrix after update to Valued Matrix");
                    this.matrix.printMatrix();

                    g = new Graph(v);
                    initializeAllEdges(g, matrix,1);
                    System.out.println("Count: " + v);

                    Collection<ArrayList<Index>> lightest = new ArrayList<ArrayList<Index>>(lightestPaths(weightsMatrix));
                    objectOutputStream.writeObject(lightest);
                    break;
                }
            }
        }
    }
}