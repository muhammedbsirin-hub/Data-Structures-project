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
        goToStep(0); // Başlangıç durumunu yükle
 
        setSize(600, 750);
        setLocationRelativeTo(null);
        setVisible(true);
    }
 
    private void buildUI() {
        // Üst panel (İleri/Geri butonları)
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.DARK_GRAY);
 
        btnPrev = new JButton("Geri");
        btnNext = new JButton("İleri");
        lblStep = new JLabel("Başlangıç", SwingConstants.CENTER);
        lblStep.setForeground(Color.WHITE);
        lblStep.setFont(new Font("Arial", Font.BOLD, 16));
        
        btnPrev.addActionListener(e -> goToStep(currentStep - 1));
        btnNext.addActionListener(e -> goToStep(currentStep + 1));
 
        topPanel.add(btnPrev);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(lblStep);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(btnNext);
        add(topPanel, BorderLayout.NORTH);
 
        // Orta panel (Önizleme ve Oyun Alanı)
        JPanel centerPanel = new JPanel(new BorderLayout());
        
        // Önizleme (Preview) Paneli
        JPanel previewPanel = new JPanel(new GridLayout(1, 5));
        for (int j = 0; j < 5; j++) {
            previewCells[j] = makeCell();
            previewCells[j].setBorder(BorderFactory.createMatteBorder(0, 1, 3, 1, Color.RED)); // Altını çizgiyle belli edelim
            previewPanel.add(previewCells[j]);
        }
        
        // Oyun Tablosu (Grid)
        JPanel gridPanel = new JPanel(new GridLayout(7, 5));
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++) {
                cells[i][j] = makeCell();
                gridPanel.add(cells[i][j]);
            }
        }
 
        centerPanel.add(previewPanel, BorderLayout.NORTH);
        centerPanel.add(gridPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }
 
    private JLabel makeCell() {
        JLabel lbl = new JLabel("", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 28));
        lbl.setOpaque(true);
        lbl.setBackground(Color.GRAY);
        lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return lbl;
    }

    // --- ARRAY KULLANMADAN İLERİ/GERİ YAPMA MANTIĞI ---
    private void goToStep(int targetStep) {
        if (targetStep < 0 || targetStep > MOVES.length) return;
        
        currentStep = targetStep;
        
        // Her adım değiştirdiğimizde listeyi sıfırlıyoruz. 
        // Böylece geçmişi array'de tutmamıza GEREK KALMIYOR.
        gameList = new MultiLinkedList();
        
        // Sıfırdan, hedef adıma kadar olan tüm hamleleri yeniden hesapla
        for (int i = 0; i < currentStep; i++) {
            gameList.dropTile(MOVES[i][0], MOVES[i][1]);
        }
        
        updateUIState();
    }
 
    private void updateUIState() {
        // 1. Oyun alanını MultiLinkedList üzerinden güncelle
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
        
        // 2. Önizleme (Gelecek Taş) Panelini Güncelle
        for (int j = 0; j < 5; j++) {
            previewCells[j].setText("");
            previewCells[j].setBackground(Color.DARK_GRAY);
        }
        
        if (currentStep < MOVES.length) {
            int nextVal = MOVES[currentStep][0];
            int nextCol = MOVES[currentStep][1];
            
            previewCells[nextCol].setText("[" + nextVal + "]");
            previewCells[nextCol].setForeground(Color.WHITE);
            previewCells[nextCol].setBackground(Color.BLACK); // Düşecek taş belirgin olsun
            lblStep.setText("Adım " + currentStep + " (Sıradaki: " + nextVal + ")");
        } else {
            lblStep.setText("Oyun Bitti!");
        }
        
        // Buton durumlarını ayarla
        btnPrev.setEnabled(currentStep > 0);
        btnNext.setEnabled(currentStep < MOVES.length);
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