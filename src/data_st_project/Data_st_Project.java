package data_st_project;

public class Data_st_Project {

    public static void main(String[] args) {

        MultiLinkedList game = new MultiLinkedList();

        int[][] moves = {
            
            {2,  0},  // 1
            {2,  3},  // 2
            {4,  1},  // 3
            {2,  2},  // 4
            {4,  4},  // 5
            {2,  1},  // 6
            {4,  4},  // 7  
            {8,  0},  // 8
            {8,  0},  // 9  
            {32, 1},  // 10
            {2,  2},  // 11 
            {64, 2},  // 12
            {16, 3},  // 13
            {64, 1},  // 14
            {32, 2},  // 15
            {16, 0},  // 16 
            {16, 4},  // 17 
            {32, 2},  // 18 
            {64, 1},  // 19 
            {8,  3},  // 20 
            {4,  3},  // 21 
            {2,  3},  // 22 
            {2,  3},  // 23 
            {2, 1},  // 24 
            {64,  2},  // 25 
            {32, 2},  // 26 
            {16, 2},  // 27 
            {8, 2},  // 28 
            {8,  2},  // 29 
            {4,  1},  // 30 
            {8,  1},  // 31 
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