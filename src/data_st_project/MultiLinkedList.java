package data_st_project;

public class MultiLinkedList {
    Node head; // listenin başı

    //Constructo
    public MultiLinkedList() {
        // başlandıgçta boş olması söylenmiş
        this.head = null; 
    }

    // taşı bırakınca çalışcak
       // Yukarıdan taşı bıraktığımızda çalışacak ana oyun mekaniği (Zincirleme Birleşmeli)
    public void dropTile(int value, int col) {
        // Eğer liste tamamen boşsa, ilk taşı head yapıyoruz ve EN ALTA (6. satıra) düşürüyoruz
        if (head == null) {
            head = new Node(value, 6, col);
            return;
        }

        Node current = head;
        Node prevCol = null;

        // 1. ADIM: İstenilen sütunu bul (Sağa doğru ilerle)
        while (current != null && current.col < col) {
            prevCol = current;
            current = current.right;
        }

        // 2. ADIM: Sütun tamamen boşsa (ilk defa taş atılıyorsa)
        if (current == null || current.col > col) {
            Node newNode = new Node(value, 6, col); // En alta düşer
            if (prevCol == null) {
                // En başa ekleme
                newNode.right = head;
                head = newNode;
            } else {
                // Araya veya sona ekleme
                newNode.right = prevCol.right;
                prevCol.right = newNode;
            }
            return;
        }

        // 3. ADIM: Sütun boş DEĞİL. current şu an o sütunun en üstteki taşı.
        // Gelen taş, en üstteki taşla aynı değere sahip mi? (BİRLEŞME KONTROLÜ)
        if (current.value == value) {
            current.value *= 2; // Çarpıştılar ve üstteki taşın değeri iki katına çıktı

            // ZİNCİRLEME BİRLEŞME (Cascade Merge)
            // Ya yeni oluşan değer, onun altındakiyle de aynıysa?
            while (current.down != null && current.value == current.down.value) {
                // Alttaki taş iki katına çıkar (çünkü üstteki eriyip onun üstüne düşüyor)
                current.down.value *= 2;
                
                // Üstteki taşı (current) aradan çıkarmalıyız. Sütun başlığını current.down yapıyoruz.
                current.down.right = current.right; // Sağ bağı alt taşa devret
                
                if (prevCol == null) {
                    head = current.down;
                } else {
                    prevCol.right = current.down;
                }
                
                // Kontrole bir alt satırdan devam et
                current = current.down; 
            }
        } else {
            // 4. ADIM: Değerler aynı değilse, yeni taşı mevcut taşın üstüne (row - 1) koy
            if (current.row > 0) {
                Node newNode = new Node(value, current.row - 1, col);
                newNode.down = current;
                
                // Sütun başlığı artık newNode oldu, sağ bağları o devralmalı
                newNode.right = current.right;
                current.right = null; // Eski başlığın sağla ilişkisini kesiyoruz (Spagetti olmasın diye)
                
                if (prevCol == null) {
                    head = newNode;
                } else {
                    prevCol.right = newNode;
                }
            } else {
                // Hoca PDF'te sütun dolduğunda oyun biter demişti.
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