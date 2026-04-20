package data_st_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Game_menu extends JFrame {

    // ── Grid hücreleri (7 satır × 5 sütun) ──────────────────────────
    private JLabel[][] cells = new JLabel[7][5];

    // ── Preview satırı (atılacak taşı gösterir) ──────────────────────
    private JLabel[] previewCells = new JLabel[5];

    // ── Kontrol paneli bileşenleri ───────────────────────────────────
    private JButton btnPrev, btnNext;
    private JLabel  lblStep;

    // ── Tüm hamlelerin önceden hesaplanmış snapshot'ları ─────────────
    // Her snapshot: int[7][5] değer matrisi
    private ArrayList<int[][]> snapshots   = new ArrayList<>();
    // Her adımdaki preview bilgisi {değer, sütun} (-1 = yok)
    private ArrayList<int[]>   previewData = new ArrayList<>();
    // Birleşen hücre koordinatları (null = birleşme yok)
    private ArrayList<int[]>   mergeCoords = new ArrayList<>();
    // Adım açıklamaları
    private ArrayList<String>  stepLabels  = new ArrayList<>();

    private int currentStep = 0;

    // ── Renk paleti (değere göre) ─────────────────────────────────────
    private Color getBgColor(int val) {
        switch (val) {
            case 2:   return new Color(0xEEE4DA);
            case 4:   return new Color(0xEDE0C8);
            case 8:   return new Color(0xF2B179);
            case 16:  return new Color(0xF59563);
            case 32:  return new Color(0xF67C5F);
            case 64:  return new Color(0xF65E3B);
            case 128: return new Color(0xEDCF72);
            case 256: return new Color(0xEDCC61);
            case 512: return new Color(0xEDC850);
            default:  return new Color(0xCDC1B4); // bos
        }
    }

    private Color getFgColor(int val) {
        return (val <= 4) ? new Color(0x776E65) : Color.WHITE;
    }

    // ── Hamle listesi ─────────────────────────────────────────────────
    private static final int[][] MOVES = {
        {2,0},{2,3},{4,1},{2,2},{4,4},{2,1},{4,4},{8,0},
        {8,0},{32,1},{2,2},{64,2},{16,3},{64,1},{32,2},{16,0},
        {16,4},{32,2},{64,1},{8,3},{4,3},{2,1},{2,3},{2,1},
        {64,2},{32,2},{16,2},{8,2},{8,2},{4,1},{8,1}
    };

    // ─────────────────────────────────────────────────────────────────
    public Game_menu() {
        setTitle("Drop Number Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        getContentPane().setBackground(new Color(0xBBADA0));

        buildUI();
        precomputeSnapshots();
        renderStep(0);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ── Arayüz kurulumu ───────────────────────────────────────────────
    private void buildUI() {

        // ── Üst panel: kontroller ─────────────────────────────────────
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 8));
        topPanel.setBackground(new Color(0xBBADA0));

        btnPrev = makeButton("◀  Geri");
        btnNext = makeButton("Ileri  ▶");
        lblStep = new JLabel("Adim 0 / " + MOVES.length, SwingConstants.CENTER);
        lblStep.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblStep.setForeground(Color.WHITE);
        lblStep.setPreferredSize(new Dimension(160, 32));

        btnPrev.addActionListener(e -> navigate(-1));
        btnNext.addActionListener(e -> navigate(+1));

        topPanel.add(btnPrev);
        topPanel.add(lblStep);
        topPanel.add(btnNext);
        add(topPanel, BorderLayout.NORTH);

        // ── Orta: preview + grid ──────────────────────────────────────
        JPanel centerPanel = new JPanel(new BorderLayout(0, 4));
        centerPanel.setBackground(new Color(0xBBADA0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));

        // Preview satırı
        JPanel previewPanel = new JPanel(new GridLayout(1, 5, 6, 0));
        previewPanel.setBackground(new Color(0xBBADA0));
        previewPanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        for (int j = 0; j < 5; j++) {
            previewCells[j] = makeCell();
            previewPanel.add(previewCells[j]);
        }

        // Grid paneli
        JPanel gridPanel = new JPanel(new GridLayout(7, 5, 6, 6));
        gridPanel.setBackground(new Color(0x9F8F85));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++) {
                cells[i][j] = makeCell();
                gridPanel.add(cells[i][j]);
            }
        }

        centerPanel.add(previewPanel, BorderLayout.NORTH);
        centerPanel.add(gridPanel,    BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JLabel makeCell() {
        JLabel lbl = new JLabel("", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lbl.setPreferredSize(new Dimension(110, 90));
        lbl.setOpaque(true);
        lbl.setBackground(new Color(0xCDC1B4));
        lbl.setBorder(BorderFactory.createLineBorder(new Color(0xBBADA0), 2, true));
        return lbl;
    }

    private JButton makeButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(0x8F7A66));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── İleri/Geri navigasyonu ────────────────────────────────────────
    private void navigate(int dir) {
        int next = currentStep + dir;
        if (next < 0 || next >= snapshots.size()) return;
        currentStep = next;
        renderStep(currentStep);
    }

    // ── Adımı ekrana yansıt ───────────────────────────────────────────
    private void renderStep(int step) {
        int[][] grid    = snapshots.get(step);
        int[]   preview = previewData.get(step);
        int[]   merge   = mergeCoords.get(step);

        // Grid hücreleri
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++) {
                int val = grid[i][j];
                JLabel lbl = cells[i][j];
                if (val == 0) {
                    lbl.setText("");
                    lbl.setBackground(new Color(0xCDC1B4));
                } else {
                    lbl.setText(String.valueOf(val));
                    lbl.setForeground(getFgColor(val));
                    lbl.setBackground(getBgColor(val));
                }
            }
        }

        // Preview satırı
        for (int j = 0; j < 5; j++) {
            previewCells[j].setText("");
            previewCells[j].setBackground(new Color(0xCDC1B4));
        }
        if (preview[0] != -1) {
            int pVal = preview[0];
            int pCol = preview[1];
            previewCells[pCol].setText("[" + pVal + "]");
            previewCells[pCol].setForeground(getFgColor(pVal));
            previewCells[pCol].setBackground(getBgColor(pVal).brighter());
        }

        // Adım etiketi
        lblStep.setText(stepLabels.get(step));

        // Buton durumu
        btnPrev.setEnabled(step > 0);
        btnNext.setEnabled(step < snapshots.size() - 1);

        // Birleşme flash efekti
        if (merge != null) {
            flashCell(merge[0], merge[1]);
        }
    }

    // ── Birleşme flash animasyonu ─────────────────────────────────────
    private void flashCell(int row, int col) {
        JLabel lbl = cells[row][col];
        Color original = lbl.getBackground();
        Timer t = new Timer(80, null);
        final int[] count = {0};
        t.addActionListener(e -> {
            lbl.setBackground(count[0] % 2 == 0 ? Color.WHITE : original);
            count[0]++;
            if (count[0] >= 6) { t.stop(); lbl.setBackground(original); }
        });
        t.start();
    }

    // ── Tüm snapshot'ları önceden hesapla ────────────────────────────
    private void precomputeSnapshots() {
        // Snapshot engine: kendi içinde MultiLinkedList kullanır
        SnapshotEngine engine = new SnapshotEngine();

        // Adım 0: boş grid
        snapshots.add(engine.getGrid());
        previewData.add(new int[]{-1, -1});
        mergeCoords.add(null);
        stepLabels.add("Baslangic");

        for (int i = 0; i < MOVES.length; i++) {
            int val = MOVES[i][0];
            int col = MOVES[i][1];

            // Preview: bu hamleden önce grid + atılacak taş bilgisi
            snapshots.add(engine.getGrid());
            previewData.add(new int[]{val, col});
            mergeCoords.add(null);
            stepLabels.add("Adim " + (i+1) + "/" + MOVES.length
                    + "  →  Sutun " + col + "  :  " + val);

            // Hamleyi uygula, merge bilgisini al
            int[] merged = engine.dropTile(val, col);

            // Sonuç snapshot
            snapshots.add(engine.getGrid());
            previewData.add(new int[]{-1, -1});
            mergeCoords.add(merged); // null ise birleşme yok
            stepLabels.add("Adim " + (i+1) + "/" + MOVES.length + "  [sonuc]");
        }
    }

    // ─────────────────────────────────────────────────────────────────
    //  Snapshot Engine — MultiLinkedList'in int[][] yansımasını tutar
    // ─────────────────────────────────────────────────────────────────
    private static class SnapshotEngine {
        private final MultiLinkedList mll = new MultiLinkedList();

        /** Taşı düşür; birleşme olduysa {row, col} döndür, yoksa null */
        int[] dropTile(int value, int col) {
            boolean hadMerge = false;
            int mergeRow = -1;

            if (mll.getLowestEmptyRow(col) < 0) return null;

            mll.insertTopPublic(value, col);

            boolean merged = true;
            while (merged) {
                merged = false;
                Node colHead = mll.getColumnHead(col);
                if (colHead != null && colHead.down != null
                        && colHead.value == colHead.down.value) {
                    int newVal = colHead.value * 2;
                    Node second = colHead.down;
                    second.value = newVal;
                    second.row   = colHead.row + 1;
                    mll.removeColumnHeadPublic(col, second);
                    hadMerge  = true;
                    mergeRow  = second.row;
                    merged    = true;
                }
            }
            mll.fixRowsPublic(col);

            return hadMerge ? new int[]{mergeRow, col} : null;
        }

        int[][] getGrid() {
            int[][] g = new int[7][5];
            for (int i = 0; i < 7; i++)
                for (int j = 0; j < 5; j++)
                    g[i][j] = mll.getValueAtPublic(i, j);
            return g;
        }
    }

    // ── Ana metot ─────────────────────────────────────────────────────
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new Game_menu());
    }
}
