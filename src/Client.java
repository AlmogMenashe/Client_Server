import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client
{
    public static void main(String[] args) throws IOException, ClassNotFoundException
    {
        // Initialize Socket IP & Port
        Socket socket = new Socket("127.0.0.1",8010);

        // Initialize stream pipe
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        ObjectInputStream fromServer = new ObjectInputStream(inputStream);
        ObjectOutputStream toServer = new ObjectOutputStream(outputStream);

        // Input matrix
        int[][] inputMatrix =
        {
                {1,0,0},
                {1,0,1},
                {0,1,1},
        };

//        int[][] inputMatrix2 = {
//                {100, 100, 100},
//                {500, 900, 300}
//        };

        // If input is "matrix", the server expects a 2D Array of int
        toServer.writeObject("matrix");
        toServer.writeObject(inputMatrix);

        // If input is "start index", the server expects an Index
        toServer.writeObject("start index");
        toServer.writeObject(new Index(0,0));

        // If input is "end index", the server expects an Index
        toServer.writeObject("end index");
        toServer.writeObject(new Index(2,2));

        toServer.writeObject("TaskOne");
        toServer.writeObject(new Index(1,1));
//
        Collection<Index> adjacentIndices = new ArrayList<>((Collection<Index>)fromServer.readObject());
        System.out.println("Neighbors: " + adjacentIndices);

        /**
            The server will execute the tasks given as input
            For example: if the input is "task 1", the server will execute task 1
        **/

        // Task 1: Find all groups with index 1
        toServer.writeObject("task 1");
        Collection<Index> groupsOfOne = new ArrayList<>((Collection<Index>)fromServer.readObject());

        // Task 2: Find the shortest routes from source to destination
        toServer.writeObject("task 2");
        List<List<Integer>> shortestPaths = new ArrayList<>((ArrayList)fromServer.readObject());

        // Task 3: The submarine game
        toServer.writeObject("task 3");
        int legalSubmarines = (int)fromServer.readObject();


//        //Short route
//        toServer.writeObject("matrixshortroute");
//        toServer.writeObject(inputMatrix2);
//        // Task 4: Finding extremely easy routes
//        toServer.writeObject("task 4");
//        List<List<Integer>> easyAllPaths = new ArrayList<>((ArrayList)fromServer.readObject());



        System.out.println("Task 1: Find all groups with index 1 (with the diagonals) : " + groupsOfOne);
        System.out.println("Task 2: Find the shortest routes from source to destination :" + shortestPaths);
        System.out.println("Task 3: The submarine game : Number of legal Subs:" + legalSubmarines);
//        System.out.println("Task 4: Finding extremely easy routes :" + easyAllPaths);

        toServer.writeObject("stop");
        fromServer.close();
        toServer.close();
        socket.close();
        System.out.println("All streams are closed");

    }
}
