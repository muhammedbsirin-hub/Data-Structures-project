package data_st_project;

public class MultiLinkedList {
    Node head;

    public MultiLinkedList() {
        this.head = null;
    }

    // ─────────────────────────────────────────
    //  YARDIMCI: Belirtilen sutunun en ustundeki
    //  node'u dondur (head pointer'i olan node)
    // ─────────────────────────────────────────
    private Node getColumnHead(int col) {
        Node cur = head;
        while (cur != null) {
            if (cur.col == col) return cur;
            if (cur.col > col)  return null;
            cur = cur.right;
        }
        return null;
    }

    // ─────────────────────────────────────────
    //  Sutundaki en alt dolu satiri bul
    //  (tasin oturacagi satirin bir ustu = bos)
    // ─────────────────────────────────────────
    private int getLowestEmptyRow(int col) {
        // Grid 7 satir (0-6), tas en alta (6) duser.
        // Sutunda kac tas var?
        Node cur = getColumnHead(col);
        int count = 0;
        while (cur != null) { count++; cur = cur.down; }
        // Taslar alta hizalanir: dolu satir sayisi = count
        // Bos olan en alt satir = 6 - count
        return 6 - count;
    }

    // ─────────────────────────────────────────
    //  Tasi birak + merge + grid yazdir
    // ─────────────────────────────────────────
    public void dropTile(int value, int col) {

        // Sutun dolu mu?
        if (getLowestEmptyRow(col) < 0) {
            System.out.println("Oyun Bitti: Sutun " + col + " tamamen dolu!");
            return;
        }

        // 1) Yeni tasi sutunun en ustune ekle
        insertTop(value, col);

        // 2) Cascade merge: altinda ayni deger varsa birlestir
        boolean merged = true;
        while (merged) {
            merged = false;
            Node colHead = getColumnHead(col);
            if (colHead != null && colHead.down != null
                    && colHead.value == colHead.down.value) {

                // Birlesme gerceklesiyor
                int mergeVal = colHead.value; // birlesmeden onceki deger
                int newVal   = mergeVal * 2;

                Node second = colHead.down;
                second.value = newVal;
                second.row   = colHead.row + 1;

                removeColumnHead(col, second);

                // Merge sonrasi grid yazdir
                System.out.println("  --> Birlesme! " + mergeVal + "+" + mergeVal + "=" + newVal + " (sutun " + col + ")");
                printGridInternal(null, -1);

                merged = true; // tekrar kontrol et
            }
        }

        // Merge sonrasi satir numaralarini duzelt
        fixRows(col);
    }

    
    private void insertTop(int value, int col) {
        int newRow = getLowestEmptyRow(col); // tasin oturacagi satir

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
            else              prev.right = newNode;
        } else {
            
            newNode.down  = cur;
            newNode.right = cur.right;
            cur.right = null;
            if (prev == null) head = newNode;
            else              prev.right = newNode;
        }
    }

    // ─────────────────────────────────────────
    //  Sutun head'ini kaldir, yeni head = second
    // ─────────────────────────────────────────
    private void removeColumnHead(int col, Node newHead) {
        Node cur = head;
        Node prev = null;
        while (cur != null && cur.col < col) {
            prev = cur;
            cur = cur.right;
        }
        // cur = eski head
        newHead.right = cur.right;
        if (prev == null) head = newHead;
        else              prev.right = newHead;
    }

    // ─────────────────────────────────────────
    //  Sutundaki taslarin row numaralarini duzelt
    //  (taslar her zaman alta hizali)
    // ─────────────────────────────────────────
    private void fixRows(int col) {
        Node cur = getColumnHead(col);
        int count = 0;
        Node tmp = cur;
        while (tmp != null) { count++; tmp = tmp.down; }

        int startRow = 6 - count + 1;
        tmp = cur;
        while (tmp != null) {
            tmp.row = startRow++;
            tmp = tmp.down;
        }
    }

    // ─────────────────────────────────────────
    //  PREVIEW satiri ile grid yazdir
    //  droppingValue / droppingCol = atilacak tas
    //  (-1 gecilirse preview satiri bos)
    // ─────────────────────────────────────────
    public void printGrid(int droppingValue, int droppingCol) {
        printGridInternal(droppingValue == -1 ? null : droppingValue,
                          droppingCol);
    }

    private void printGridInternal(Integer droppingValue, int droppingCol) {
        System.out.println("-------------------------");

        // Preview satiri (satir -1)
        System.out.print("  "); // kucuk girinti
        for (int j = 0; j < 5; j++) {
            if (droppingValue != null && j == droppingCol) {
                System.out.printf("%5s", "[" + droppingValue + "]");
            } else {
                System.out.printf("%5s", " ");
            }
        }
        System.out.println("  <- atilacak tas");

        // Ok satiri
        System.out.print("  ");
        for (int j = 0; j < 5; j++) {
            if (droppingValue != null && j == droppingCol) {
                System.out.printf("%5s", "v");
            } else {
                System.out.printf("%5s", " ");
            }
        }
        System.out.println();

        // 7 satir (0-6)
        for (int i = 0; i < 7; i++) {
            System.out.print("  ");
            for (int j = 0; j < 5; j++) {
                int val = getValueAt(i, j);
                if (val == 0) System.out.printf("%5s", ".");
                else          System.out.printf("%5d", val);
            }
            System.out.println();
        }
        System.out.println("-------------------------");
    }

    // ─────────────────────────────────────────
    //  (row, col) degerini bul
    // ─────────────────────────────────────────
    private int getValueAt(int row, int col) {
        Node colHead = getColumnHead(col);
        while (colHead != null) {
            if (colHead.row == row) return colHead.value;
            colHead = colHead.down;
        }
        return 0;
    }
}
