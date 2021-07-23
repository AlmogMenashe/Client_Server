public class Test 
{
    public static void main(String[] args) 
    {
        int v = 0;
        int numOfVertices = 1;
        Index[] fromValueToIndex;
        int[][] inputMatrix =
                {
                        {-1,0,0,-1},
                        {1,0,100,0},
                        {0,5,0,-7},
                };

        int row = inputMatrix.length;
        int col = inputMatrix[0].length;
        
        
        
        for (int i = 0; i < row; i++)
        {
            for (int j = 0; j < col; j++)
            {
                if (inputMatrix[i][j] != 0)
                    inputMatrix[i][j] = numOfVertices++;
            }
        }

        v = numOfVertices;
        fromValueToIndex = new Index[v];

        for (int i = 0; i < row; i++)
        {
            for (int j = 0; j < col; j++)
            {
                if(inputMatrix[i][j] != 0)
                    fromValueToIndex[inputMatrix[i][j]] = new Index(i, j);
            }
        }

        for (int i = 0; i < row; i++)
        {
            for (int j = 0; j < col; j++)
            {
                System.out.println(inputMatrix[i][j]);
            }
        }
    }
}
