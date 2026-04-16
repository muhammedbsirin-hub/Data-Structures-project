package data_st_project;

public class Node {
    int value;      
    int row;       
    int col;        
    
  
    Node right;     
    Node down;     

    // constructor
    public Node(int value, int row, int col) {
        this.value = value;
        this.row = row;
        this.col = col;
        this.right = null;
        this.down = null;
    }
}