package data_st_project;

public class Data_st_Project {

    public static void main(String[] args) {
        MultiLinkedList game = new MultiLinkedList();
        
        System.out.println("0. Sütuna '4' atıyoruz:");
        game.dropTile(4, 0); // En alta (Row 6) 4 düşecek
        game.printGrid();
        
        System.out.println("0. Sütuna '2' atıyoruz:");
        game.dropTile(2, 0); // 4'ün üstüne (Row 5) 2 düşecek
        game.printGrid();
        
        System.out.println("0. Sütuna bir '2' daha atıyoruz (ZİNCİRLEME BİRLEŞME BEKLENİYOR):");
        game.dropTile(2, 0); // 2 ile birleşip 4 olacak, sonra altındaki 4 ile birleşip 8 olacak!
        game.printGrid();
    }
}