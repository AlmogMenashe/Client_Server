import java.util.*;

public class BFS {

    public List<List<Index>> allPathsToDestanation(Matrix matrix, Index source, Index dest){
        List<List<Index>> result = new ArrayList<>();
        Set<Index> finished = new HashSet<>();
        Queue<List<Index>> queue = new LinkedList<>();
        queue.add(Collections.singletonList(source));

        while(!queue.isEmpty()){
            List<Index> path = queue.poll();
            Index lastIndex = path.get(path.size()-1);

            if(lastIndex.equals(dest)){
                result.add(new ArrayList<>(path));
            } else{
                finished.add(lastIndex);
                List<Index> reachableIndices = (List<Index>) matrix.getReachables(lastIndex);
                //System.out.println(reachableIndices);
                for(Index neighbor : reachableIndices){
                    if(!finished.contains(neighbor)){
                        List<Index> list = new ArrayList<>(path);
                        list.add(neighbor);
                        queue.add(list);
                    }
                }
            }

        }
        filterPaths(result);
        return result;
    }

    public void filterPaths(List<List<Index>> result){
        List<Index> singleFiltered = result.get(0);
        int minSize = singleFiltered.size();
        result.removeIf(singleArray -> singleArray.size() > minSize);
    }

    public static void main(String[] args) {
        int[][] source = {
                {1, 1, 1},
                {1, 0, 1},
                {1, 1, 1}
        };
        Matrix matrix = new Matrix(source);
        BFS bfsLogic = new BFS();
        System.out.println(bfsLogic.allPathsToDestanation(matrix, new Index(0,0),new Index(2,2)));
    }
}
