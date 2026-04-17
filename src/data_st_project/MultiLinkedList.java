package data_st_project;

public class MultiLinkedList {
    Node head; // listenin başı

    //Constructo
    public MultiLinkedList() {
        // başlandıgçta boş olması söylenmiş
        this.head = null; 
    }

    // taşı bırakınca çalışcak
    
    public void dropTile(int value, int col) {
        
        if (head == null) {
            head = new Node(value, 6, col);
            return;
        }

        Node current = head;
        Node prevCol = null;

        // sutun bulma
        while (current != null && current.col < col) {
            prevCol = current;
            current = current.right;
        }

        
        if (current == null || current.col > col) {
            Node newNode = new Node(value, 6, col); // en alta düşer
            if (prevCol == null) {
                
                newNode.right = head;
                head = newNode;
            } else {
                
                newNode.right = prevCol.right;
                prevCol.right = newNode;
            }
            return;
        }

        
        if (current.value == value) {
            current.value *= 2; 

            
            while (current.down != null && current.value == current.down.value) {
                
                current.down.value *= 2;
                
               
                current.down.right = current.right;
                
                if (prevCol == null) {
                    head = current.down;
                } else {
                    prevCol.right = current.down;
                }
                
                
                current = current.down; 
            }
        } else {
            // değerler aynı değilse
            if (current.row > 0) {
                Node newNode = new Node(value, current.row - 1, col);
                newNode.down = current;
                
                
                newNode.right = current.right;
                current.right = null; 
                
                if (prevCol == null) {
                    head = newNode;
                } else {
                    prevCol.right = newNode;
                }
            } else {
                
                System.out.println("Oyun Bitti veya Hatalı Hamle: Sütun " + col + " tamamen dolu!");
            }
        }
    }

    // hamle sonrası yazdırma
        public void printGrid() {
        System.out.println("-------------------------");
        // 7 satır
        for (int i = 0; i < 7; i++) {
            // 5 sütun
            for (int j = 0; j < 5; j++) {
                // o kordinat boş mu kontrol
                int val = getValueAt(head, i, j);
                
                if (val == 0) {
                    // boş ise
                    System.out.printf("%5s", "."); 
                } else {
                    // dolu ise
                    System.out.printf("%5d", val); 
                }
            }
            System.out.println(); 
        }
        System.out.println("-------------------------");
    }

    // istenen kordinattaki değeri bulmak için bir method
    private int getValueAt(Node current, int row, int col) {
        // eğer ki node yoksa
        if (current == null) {
            return 0; 
        }
        
        
        if (current.row == row && current.col == col) {
            return current.value;
        }
        
        // bulamadıysak önce listenin sağ kolunu tarama
        int rightSearch = getValueAt(current.right, row, col);
        if (rightSearch != 0) {
            return rightSearch;
        }
        
        // sağda da bulamadıysak aşağı kolunu tarama
        return getValueAt(current.down, row, col);
    }
}