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
            Node newNode = new Node(value, 6, col);
            head = newNode;
            return;
        }
        
        
        Node newNode = new Node(value, 0, col);

        Node current = head;
        Node prevColNode = null; // bir önceki sütundaki düğümü takip etmek için

        //doğru sütuna gitmek için sağa ilerleme 
        while (current != null && current.col < col) {
            prevColNode = current;
            current = current.right;
        }

        
        if (current == null || current.col > col) {
            // sütun boşsa taşı en alt satıra gider
            newNode.row = 6;

            if (prevColNode == null) {
                // eğer liste boş değil ama eklenecek sütun en başa denk geliyorsa col=0 gibi
                // ve headin sütunu bundan büyükse yeni head bu olur
                newNode.right = head;
                head = newNode;
            } else {
                // qraya veya sona ekleme
                newNode.right = prevColNode.right;
                prevColNode.right = newNode;
            }
            return;
        }

        //doğru sütunu bulduğumuzda
        // aynı değere sahip taşı bulma
        Node prevRowNode = null;
        while (current != null) {
            
            if (current.value == value) {
                // eni taş ekleme 
                current.value *= 2;
                return; 
            }
            prevRowNode = current;
            current = current.down;
        }

        
        if (prevRowNode != null && prevRowNode.row > 0) {
            newNode.row = prevRowNode.row - 1;
            newNode.down = prevRowNode; 
            
            //sağ-sol bağlantılarını güncelleme
             if (prevColNode == null) {
                 newNode.right = head;
                 head = newNode;
             } else {
                 newNode.right = prevColNode.right;
                 prevColNode.right = newNode;
             }
        } else {
            // eğer sütun tamamen doluysa oyun bitmiş demektir
            
            System.out.println("Sütun dolu, taş eklenemedi!");
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