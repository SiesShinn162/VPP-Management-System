package VPP.View.mainUI;


import VPP.database.DBThongke;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class Thongkepanel extends JPanel {

    private JComboBox<Integer> cbThang;
    private JComboBox<Integer> cbNam;
    private JButton btnThongKe;

    private JTable dtngay;
    private JTable dtthang;
    private JTable spbchay;

    private DefaultTableModel modelNgay;
    private DefaultTableModel modelThang;
    private DefaultTableModel modelBanChay;

    public Thongkepanel() {
        initComponents();
        bamThongKe(); 
    }

    private void initComponents() {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        
        JPanel pnLoc = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));

        pnLoc.add(new JLabel("Tháng:"));
        cbThang = new JComboBox<>();

        for (int i = 1; i <= 12; i++) {
            cbThang.addItem(i);
        }
        pnLoc.add(cbThang);

        pnLoc.add(new JLabel("Năm:"));
        cbNam = new JComboBox<>();

        int namHienTai = LocalDate.now().getYear();
        for (int i = namHienTai - 5; i <= namHienTai + 1; i++) {
            cbNam.addItem(i);
        }
        
        pnLoc.add(cbNam);

        btnThongKe = new JButton("Thống kê");
        btnThongKe.setBackground(new Color(222, 120, 255));
        btnThongKe.setForeground(Color.WHITE);
        pnLoc.add(btnThongKe);

        cbThang.setSelectedItem(LocalDate.now().getMonthValue());
        cbNam.setSelectedItem(namHienTai);

        btnThongKe.addActionListener(e -> bamThongKe());

        
        modelNgay = new DefaultTableModel(new String[]{"Ngày", "Doanh thu"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        dtngay = new JTable(modelNgay);

        modelThang = new DefaultTableModel(new String[]{"Tháng", "Doanh thu"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        dtthang = new JTable(modelThang);

        modelBanChay = new DefaultTableModel(new String[]{"Mã SP", "Tên SP", "Số lượng bán", "Doanh thu"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        spbchay = new JTable(modelBanChay);

        
        JPanel pnCenter = new JPanel();
        pnCenter.setLayout(new BoxLayout(pnCenter, BoxLayout.Y_AXIS));
        pnCenter.add(pnLoc);
        pnCenter.add(Box.createVerticalStrut(10));

        JPanel pnDoanhThu = new JPanel(new GridLayout(1, 2, 12, 12));

        JPanel pnNgay = new JPanel(new BorderLayout());
        pnNgay.setBorder(BorderFactory.createTitledBorder("Doanh thu theo ngày (trong tháng)"));
        pnNgay.add(new JScrollPane(dtngay), BorderLayout.CENTER);

        JPanel pnThang = new JPanel(new BorderLayout());
        pnThang.setBorder(BorderFactory.createTitledBorder("Doanh thu theo tháng (trong năm)"));
        pnThang.add(new JScrollPane(dtthang), BorderLayout.CENTER);
        pnDoanhThu.add(pnNgay);
        pnDoanhThu.add(pnThang);

        JPanel pnBanChay = new JPanel(new BorderLayout());
        pnBanChay.setBorder(BorderFactory.createTitledBorder("Sản phẩm bán chạy (Top 10 trong tháng)"));
        pnBanChay.add(new JScrollPane(spbchay), BorderLayout.CENTER);

        pnCenter.add(pnDoanhThu);
        pnCenter.add(Box.createVerticalStrut(12));
        pnCenter.add(pnBanChay);

        add(pnCenter, BorderLayout.CENTER);
    }

    private void bamThongKe() {
        int thang = (int) cbThang.getSelectedItem();
        int nam = (int) cbNam.getSelectedItem();


        DBThongke.ttdtngay(this, modelNgay, thang, nam);
        DBThongke.ttdtthang(this, modelThang, nam);
        DBThongke.spbanchay(this, modelBanChay, thang, nam);
    }
}
