
package data_st_project;


public class Data_st_Project {

   
    public static void main(String[] args) {
        MultiLinkedList game = new MultiLinkedList();
        
        System.out.println("Başlangıç (Boş Tablo):");
        game.printGrid();
        
        System.out.println("\n1. Sütuna '2' atıyoruz:");
        game.dropTile(2, 0);
        game.printGrid();
        
        System.out.println("\n1. Sütuna bir '2' daha atıyoruz (Birleşmeli):");
        game.dropTile(2, 0);
        game.printGrid();
        
        System.out.println("\n3. Sütuna '4' atıyoruz:");
        game.dropTile(4, 2);
        game.printGrid();
    }
    
}
