package data_st_project;

public class Data_st_Project {

    public static void main(String[] args) {

        MultiLinkedList game = new MultiLinkedList();

        int[][] moves = {
    
    {2, 0},
    {2, 3},
    {4, 1},
    {2, 2},
    {4, 4},
    {2, 1},
    {4, 4}, 
    {8, 0}, 

   
    {8, 0},
    {32, 1},
    {2, 2},
    {64, 2},
    {16, 3},
    {64, 1},
    {32, 2},
    {16, 0},
    {16, 4}, 
    {32, 2}, 

    
    {64, 1}, 
    {8, 3},
    {4, 3},
    {2, 1},  
    {2, 3},
    {2, 1},  
    {64, 2},
    {32, 2},

    
    {16, 2}, 
    {8, 2},
    {8, 2},
    {4, 1},  
    {8, 1}   
};
       

        System.out.println("=========================");
        System.out.println("  OYUN BASLIYOR (BOS)   ");
        game.printGrid(-1, -1);

        for (int i = 0; i < moves.length; i++) {
            int value = moves[i][0];
            int col   = moves[i][1];

            System.out.println("\nAdim " + (i + 1) + ": " + col
                    + ". Sutuna '" + value + "' atiliyor.");

            
            game.printGrid(value, col);

            
            game.dropTile(value, col);

            
            System.out.println("  [Adim " + (i + 1) + " sonucu]");
            game.printGrid(-1, -1);
        }

        System.out.println("\n=========================");
        System.out.println("Test Senaryosu Tamamlandi!");
    }
}
