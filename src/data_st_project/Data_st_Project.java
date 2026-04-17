package data_st_project;

public class Data_st_Project {

    public static void main(String[] args) {
        MultiLinkedList game = new MultiLinkedList();
        
        System.out.println("-------------------------");
        System.out.println("OYUN BAŞLIYOR (BOŞ TABLO)");
        game.printGrid();

        
        System.out.println("\nAdım 1: 0. Sütuna '2' atılıyor.");
        game.dropTile(2, 0);
        game.printGrid();

        System.out.println("\nAdım 2: 3. Sütuna '2' atılıyor.");
        game.dropTile(2, 3);
        game.printGrid();

        System.out.println("\nAdım 3: 1. Sütuna '4' atılıyor.");
        game.dropTile(4, 1);
        game.printGrid();

        System.out.println("\nAdım 4: 2. Sütuna '2' atılıyor.");
        game.dropTile(2, 2);
        game.printGrid();

        System.out.println("\nAdım 5: 4. Sütuna '4' atılıyor.");
        game.dropTile(4, 4);
        game.printGrid();

        System.out.println("\nAdım 6: 1. Sütuna '2' atılıyor.");
        game.dropTile(2, 1);
        game.printGrid();

        System.out.println("\nAdım 7: 4. Sütuna '4' atılıyor.");
        game.dropTile(4, 4); 
        game.printGrid();

        System.out.println("\nAdım 8: 0. Sütuna '8' atılıyor.");
        game.dropTile(8, 0); 
        game.printGrid();

        System.out.println("\nAdım 9: 0. Sütuna '8' atılıyor.");
        game.dropTile(8, 0);
        game.printGrid();

        System.out.println("\nAdım 10: 1. Sütuna '32' atılıyor.");
        game.dropTile(32, 1);
        game.printGrid();

        System.out.println("\nAdım 11: 2. Sütuna '2' atılıyor.");
        game.dropTile(2, 2);
        game.printGrid();

        System.out.println("\nAdım 12: 2. Sütuna '64' atılıyor.");
        game.dropTile(64, 2);
        game.printGrid();

        System.out.println("\nAdım 13: 3. Sütuna '16' atılıyor.");
        game.dropTile(16, 3);
        game.printGrid();

        System.out.println("\nAdım 14: 1. Sütuna '64' atılıyor.");
        game.dropTile(64, 1);
        game.printGrid();

        System.out.println("\nAdım 15: 2. Sütuna '32' atılıyor.");
        game.dropTile(32, 2); 
        game.printGrid();

        System.out.println("\nAdım 16: 0. Sütuna '16' atılıyor.");
        game.dropTile(16, 0); 
        game.printGrid();

        
        
        
        
        
        System.out.println("\nTest Senaryosu Başarıyla Tamamlandı!");
    }
}