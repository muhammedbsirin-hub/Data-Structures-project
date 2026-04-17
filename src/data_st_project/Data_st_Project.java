package data_st_project;

public class Data_st_Project {

    public static void main(String[] args) {

        MultiLinkedList game = new MultiLinkedList();

        // Hocanin istedigi sirayla: {deger, sutun}
        int[][] moves = {
            {2,  0},   // Adim 1
            {2,  3},   // Adim 2
            {4,  1},   // Adim 3
            {2,  2},   // Adim 4
            {4,  4},   // Adim 5
            {2,  1},   // Adim 6
            {4,  4},   // Adim 7
            {8,  0},   // Adim 8
            {8,  0},   // Adim 9
            {32, 1},   // Adim 10
            {2,  2},   // Adim 11
            {64, 2},   // Adim 12
            {16, 3},   // Adim 13
            {64, 1},   // Adim 14
            {32, 2},   // Adim 15
            {16, 0},   // Adim 16
        };

        System.out.println("=========================");
        System.out.println("  OYUN BASLIYOR (BOS)   ");
        game.printGrid(-1, -1);

        for (int i = 0; i < moves.length; i++) {
            int value = moves[i][0];
            int col   = moves[i][1];

            System.out.println("\nAdim " + (i + 1) + ": " + col
                    + ". Sutuna '" + value + "' atiliyor.");

            // Atilmadan once preview goster
            game.printGrid(value, col);

            // Tasi birak (merge varsa kendi icinde yazdirir)
            game.dropTile(value, col);

            // Adim sonu final hali
            System.out.println("  [Adim " + (i + 1) + " sonucu]");
            game.printGrid(-1, -1);
        }

        System.out.println("\n=========================");
        System.out.println("Test Senaryosu Tamamlandi!");
    }
}
