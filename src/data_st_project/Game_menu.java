package data_st_project;
 
import javax.swing.*;
import java.awt.*;
 
public class Game_menu extends JFrame {
 
    
    private JLabel[][] cells = new JLabel[7][5];
    private JLabel[] previewCells = new JLabel[5]; // gelecek taşı gösterecek satır
 
    private JButton btnPrev, btnNext;
    private JLabel  lblStep;
 
    
    private MultiLinkedList gameList;
    private int currentStep = 0;//kaçıncı satırdayım
 
    //test hamleleri array olarak tutmak zorundayım
    private static final int[][] MOVES = {
            {2,  0}, {2,  3}, {4,  1}, {2,  2}, {4,  4}, 
            {2,  1}, {4,  4}, {8,  0}, {8,  0}, {32, 1}, 
            {2,  2}, {64, 2}, {16, 3}, {64, 1}, {32, 2}, 
            {16, 0}, {16, 4}, {32, 2}, {64, 1}, {8,  3}, 
            {4,  3}, {2,  3}, {2,  3}, {2, 1},  {64, 2}, 
            {32, 2}, {16, 2}, {8, 2},  {8,  2}, {4,  1}, 
            {8,  1}
    };
 
    public Game_menu() {
        setTitle("Drop Number Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
 
        buildUI();
        goToStep(0); // başlangıç durumunu yükler
 
        setSize(600, 750);
        setLocationRelativeTo(null);
        setVisible(true);
    }
 
    private void buildUI() {
        // üst panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.DARK_GRAY);
 
        btnPrev = new JButton("Geri");
        btnNext = new JButton("İleri");
        lblStep = new JLabel("Başlangıç", SwingConstants.CENTER);
        lblStep.setForeground(Color.WHITE);//renklendirme ve font kısımlarında yapay zekadan destek aldım
        lblStep.setFont(new Font("Arial", Font.BOLD, 16));
        
        btnPrev.addActionListener(e -> goToStep(currentStep - 1));
        btnNext.addActionListener(e -> goToStep(currentStep + 1));
 
        topPanel.add(btnPrev);//buton ekleme en üste
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(lblStep);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(btnNext);
        add(topPanel, BorderLayout.NORTH);
 
        // orta panel oyun alanı
        JPanel centerPanel = new JPanel(new BorderLayout());
        
        // önizleme paneli
        JPanel previewPanel = new JPanel(new GridLayout(1, 5));
        for (int j = 0; j < 5; j++) {
            previewCells[j] = makeCell();
            previewCells[j].setBorder(BorderFactory.createMatteBorder(0, 1, 3, 1, Color.RED)); // belirgin olsun diye altını kırmızı çizgi yaptım
            previewPanel.add(previewCells[j]);
        }
        
        // oyun tablosu grid
        JPanel gridPanel = new JPanel(new GridLayout(7, 5));
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++) {
                cells[i][j] = makeCell();
                gridPanel.add(cells[i][j]);
            }
        }
 
        centerPanel.add(previewPanel, BorderLayout.NORTH);//panelleri yerleştirme
        centerPanel.add(gridPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }
 
    private JLabel makeCell() {//label oluşturma ve onların fontu fln
        JLabel lbl = new JLabel("", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 28));
        lbl.setOpaque(true);
        lbl.setBackground(Color.GRAY);
        lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return lbl;
    }

   
    private void goToStep(int targetStep) {
        if (targetStep < 0 || targetStep > MOVES.length) return;
        
        currentStep = targetStep;
        
        // her adım değiştirdiğimizde listeyi sıfırlıyoruz
        // böylece geçmişi arrayde tutmuyoruz
        gameList = new MultiLinkedList();
        
        // sıfırdan aynı adımları yapıyoruz
        for (int i = 0; i < currentStep; i++) {
            gameList.dropTile(MOVES[i][0], MOVES[i][1]);
        }
        
        updateUIState();
    }
 
    private void updateUIState() {
        // oyun alanını MultiLinkedList üzerinden güncelliyoruz
        for (int r = 0; r < 7; r++) {
            for (int c = 0; c < 5; c++) {
                int val = gameList.getValueAt(r, c);
                JLabel lbl = cells[r][c];
                
                if (val == 0) {
                    lbl.setText("");
                    lbl.setBackground(Color.GRAY);
                } else {
                    lbl.setText(String.valueOf(val));
                    lbl.setForeground(Color.BLACK);
                    lbl.setBackground(getBgColor(val));
                }
            }
        }
        
        // önizleme panelini güncelleme
        for (int j = 0; j < 5; j++) {
            previewCells[j].setText("");
            previewCells[j].setBackground(Color.DARK_GRAY);
        }
        
        if (currentStep < MOVES.length) {
            int nextVal = MOVES[currentStep][0];
            int nextCol = MOVES[currentStep][1];
            
            previewCells[nextCol].setText("[" + nextVal + "]");
            previewCells[nextCol].setForeground(Color.WHITE);
            previewCells[nextCol].setBackground(Color.BLACK); // düşecek taş belirgin olsun diye ekledim
            lblStep.setText("Adım " + currentStep + " (Sıradaki: " + nextVal + ")");
        } else { 
            lblStep.setText("Oyun Bitti!");
    
    // oyun bitti mesajı 
        if (currentStep == MOVES.length) {
            JOptionPane.showMessageDialog( this,"Tüm hamleler tamamlandı!\nOyun sona erdi.", "Oyun Bitti!",JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
    }
        }
        
        
        btnPrev.setEnabled(currentStep > 0);//en baştaysak önceki butonunu pasif yapıyoruz     
        btnNext.setEnabled(currentStep < MOVES.length);//en sondaysak da sonraki butonunu pasif yaparız
    }
 
    private Color getBgColor(int val) {
        switch (val) {
            case 2:   return Color.WHITE;
            case 4:   return Color.LIGHT_GRAY;
            case 8:   return Color.CYAN;
            case 16:  return Color.YELLOW;
            case 32:  return Color.ORANGE;
            case 64:  return Color.MAGENTA;
            case 128: return Color.PINK;
            case 256: return Color.RED;
            case 512: return Color.GREEN;
            default:  return Color.GRAY;
        }
    }
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Game_menu());
    }
}