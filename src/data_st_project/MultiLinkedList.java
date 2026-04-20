package data_st_project;

public class MultiLinkedList {
    Node head;

    public MultiLinkedList() {
        this.head = null;
    }

    public Node getColumnHead(int col) {
        Node cur = head;
        while (cur != null) {
            if (cur.col == col) return cur;
            if (cur.col > col)  return null;
            cur = cur.right;
        }
        return null;
    }

    public int getLowestEmptyRow(int col) {
        Node cur = getColumnHead(col);
        int count = 0;
        while (cur != null) { count++; cur = cur.down; }
        return 6 - count;
    }

    public void dropTile(int value, int col) {
        if (getLowestEmptyRow(col) < 0) {
            System.out.println("Oyun Bitti: Sutun " + col + " tamamen dolu!");
            return;
        }
        insertTop(value, col);
        boolean merged = true;
        while (merged) {
            merged = false;
            Node colHead = getColumnHead(col);
            if (colHead != null && colHead.down != null
                    && colHead.value == colHead.down.value) {
                int mergeVal = colHead.value;
                int newVal   = mergeVal * 2;
                Node second  = colHead.down;
                second.value = newVal;
                second.row   = colHead.row + 1;
                removeColumnHead(col, second);
                System.out.println("  --> Birlesme! " + mergeVal + "+" + mergeVal + "=" + newVal + " (sutun " + col + ")");
                printGridInternal(null, -1);
                merged = true;
            }
        }
        fixRows(col);
    }

    public void insertTopPublic(int value, int col) { insertTop(value, col); }
    public void removeColumnHeadPublic(int col, Node newH) { removeColumnHead(col, newH); }
    public void fixRowsPublic(int col) { fixRows(col); }
    public int  getValueAtPublic(int row, int col)  { return getValueAt(row, col); }

    void insertTop(int value, int col) {
        int newRow = getLowestEmptyRow(col);
        Node newNode = new Node(value, newRow, col);
        Node cur = head;
        Node prev = null;
        while (cur != null && cur.col < col) { prev = cur; cur = cur.right; }
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

    void removeColumnHead(int col, Node newHead) {
        Node cur = head;
        Node prev = null;
        while (cur != null && cur.col < col) { prev = cur; cur = cur.right; }
        newHead.right = cur.right;
        if (prev == null) head = newHead;
        else              prev.right = newHead;
    }

    void fixRows(int col) {
        Node cur = getColumnHead(col);
        int count = 0;
        Node tmp = cur;
        while (tmp != null) { count++; tmp = tmp.down; }
        int startRow = 6 - count + 1;
        tmp = cur;
        while (tmp != null) { tmp.row = startRow++; tmp = tmp.down; }
    }

    public void printGrid(int droppingValue, int droppingCol) {
        printGridInternal(droppingValue == -1 ? null : droppingValue, droppingCol);
    }

    void printGridInternal(Integer droppingValue, int droppingCol) {
        System.out.println("-------------------------");
        System.out.print("  ");
        for (int j = 0; j < 5; j++) {
            if (droppingValue != null && j == droppingCol)
                System.out.printf("%5s", "[" + droppingValue + "]");
            else System.out.printf("%5s", " ");
        }
        System.out.println("  <- atilacak tas");
        System.out.print("  ");
        for (int j = 0; j < 5; j++) {
            if (droppingValue != null && j == droppingCol)
                System.out.printf("%5s", "v");
            else System.out.printf("%5s", " ");
        }
        System.out.println();
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

    private int getValueAt(int row, int col) {
        Node colHead = getColumnHead(col);
        while (colHead != null) {
            if (colHead.row == row) return colHead.value;
            colHead = colHead.down;
        }
        return 0;
    }
}
