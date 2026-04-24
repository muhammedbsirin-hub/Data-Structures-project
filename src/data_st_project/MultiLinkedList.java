package data_st_project;

public class MultiLinkedList {
    public Node head;

    public MultiLinkedList() {
        this.head = null;
    }

    // hangi sütunda olduğumuzu buluyoruz
    public Node getColumnHead(int col) {
        Node cur = head;
        while (cur != null) {
            if (cur.col == col) return cur;
            if (cur.col > col)  return null;
            cur = cur.right;
        }
        return null;
    }

    // bulduğumuz sütunda ki ilk boş olan yere iniyoruz 
    public int getLowestEmptyRow(int col) {
        Node cur = getColumnHead(col);
        int count = 0;
        while (cur != null) {
            count++;
            cur = cur.down;
        }
        return 6 - count;//mesela count 5 çıkarsa 1. label boş demek
    }

    
    public void dropTile(int value, int col) {
        if (getLowestEmptyRow(col) < 0) {
            System.out.println("Sutun " + col + " tamamen dolu!");
            return;
        }

        insertTop(value, col);

        boolean merged = true;
        while (merged) {
            merged = false;
            Node colHead = getColumnHead(col);
            if (colHead != null && colHead.down != null && colHead.value == colHead.down.value) {//zincirleme olayının olduğu kıssım
                
                int newVal = colHead.value * 2;
                Node second = colHead.down;
                second.value = newVal;
                second.row = colHead.row + 1;

                removeColumnHead(col, second);
                System.out.println("Birlesme! Yeni tas: " + newVal);//hangi birleşmeler oldu görmek için gerekli değil çok

                merged = true;
            }
        }
        fixRows(col);
    }

    // yeni tasi listenin basina ekle
    public void insertTop(int value, int col) {
        int newRow = getLowestEmptyRow(col);
        Node newNode = new Node(value, newRow, col);

        Node cur = head;
        Node prev = null;

        while (cur != null && cur.col < col) {
            prev = cur;
            cur = cur.right;
        }

        if (cur == null || cur.col > col) {
            newNode.right = cur;
            if (prev == null) head = newNode;
            else prev.right = newNode;
        } else {
            newNode.down = cur;
            newNode.right = cur.right;
            cur.right = null;
            if (prev == null) head = newNode;
            else prev.right = newNode;
        }
    }

    // birlesme sonrasi en ustteki eski tasi sil
    public void removeColumnHead(int col, Node newHead) {
        Node cur = head;
        Node prev = null;
        while (cur != null && cur.col < col) {
            prev = cur;
            cur = cur.right;
        }
        newHead.right = cur.right;
        if (prev == null) head = newHead;
        else prev.right = newHead;
    }

    // satir numaralarini guncelleme
    public void fixRows(int col) {
        Node cur = getColumnHead(col);
        int count = 0;
        Node tmp = cur;
        while (tmp != null) { 
            count++; 
            tmp = tmp.down; 
        }

        int startRow = 6 - count + 1;
        tmp = cur;
        while (tmp != null) {
            tmp.row = startRow++;
            tmp = tmp.down;
        }
    }

    // 
    public void printGrid(int droppingValue, int droppingCol) {
        System.out.println("-------------------------");
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++) {
                int val = getValueAt(i, j);
                if (val == 0) {
                    System.out.print(".\t"); // bos yerlere nokta
                } else {
                    System.out.print(val + "\t"); 
                }
            }
            System.out.println();
        }
        System.out.println("-------------------------");
    }

    // koordinattaki degeri bul
    public int getValueAt(int row, int col) {
        Node colHead = getColumnHead(col);
        while (colHead != null) {
            if (colHead.row == row) return colHead.value;
            colHead = colHead.down;
        }
        return 0; // bos hucre
    }
}