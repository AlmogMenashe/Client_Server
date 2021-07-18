
import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // In order to request something over TCP from a server, we need a port number and an IP address
        Socket socket = new Socket("127.0.0.1",8010);
        // socket is an abstraction of 2-way data pipe
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        // use decorators
        ObjectInputStream fromServer = new ObjectInputStream(inputStream);
        ObjectOutputStream toServer = new ObjectOutputStream(outputStream);

        int[][] source = {
                {1,0,0},
                {1,0,1},
                {0,1,1},
        };
//        int[][] source = {
//                {1,0,0,1,1},
//                {1,0,0,1,1},
//                {1,0,0,1,1},
//        };



        toServer.writeObject("matrix");
        // according to protocol, after "matrix" string, send 2d int array
        toServer.writeObject(source);

        toServer.writeObject("start index");
        // according to protocol, after "matrix" string, send 2d int array
        toServer.writeObject(new Index(0,0));

        toServer.writeObject("end index");
        // according to protocol, after "matrix" string, send 2d int array
        toServer.writeObject(new Index(2,2));

        toServer.writeObject("TaskOne");
        toServer.writeObject(new Index(1,1));

        Collection<Index> adjacentIndices = new ArrayList<>((Collection<Index>)fromServer.readObject());
        System.out.println("Neighbors: " + adjacentIndices);

        //Task 1 - Find all groups with index 1 (with the diagonals)
        toServer.writeObject("Mission1");
//        toServer.writeObject(new Index(1,1));
        Collection<Index> connectedComponents = new ArrayList<>((Collection<Index>)fromServer.readObject());
        System.out.println("Task 1 - Find all groups with index 1 (with the diagonals) : " + connectedComponents);

        //Task 3 - The submarine game
        toServer.writeObject("Mission3");
//        toServer.writeObject(new Index(1,1));
        int numberOfSubs = (int)fromServer.readObject();
        System.out.println("Task 3 - The submarine game : Number of legal Subs:" + numberOfSubs);

        //Task 4 - Finding extremely easy routes
        toServer.writeObject("Mission4");
//        toServer.writeObject(new Index(1,1));
        List<List<Integer>> allPaths = new ArrayList<>((ArrayList)fromServer.readObject());
        System.out.println("Task 4 - Finding extremely easy routes :" + allPaths);

        //Task 2 - Find the shortest routes from source to destination
        toServer.writeObject("Mission2");
//        toServer.writeObject(new Index(1,1));
        List<List<Integer>> allPaths2 = new ArrayList<>((ArrayList)fromServer.readObject());
        System.out.println("Task 2 - Find the shortest routes from source to destination :" + allPaths2);




        toServer.writeObject("stop");
        fromServer.close();
        toServer.close();
        socket.close();
        System.out.println("All streams are closed");

    }
}
