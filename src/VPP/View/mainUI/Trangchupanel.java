package VPP.View.mainUI;

import java.awt.*;
import java.sql.Connection;
import java.text.DecimalFormat;

import javax.swing.*;

import javax.swing.table.DefaultTableModel;

import VPP.database.DBTrangchu;
import VPP.database.ketnoidb;

public class Trangchupanel extends JPanel {

    private JLabel lbDTN;
    private JLabel lbDTT;
    private JLabel lbDTThang;
    private JLabel lbTongHD;

    private JTable tbProducts;
    private DefaultTableModel modelProducts;

    private final DecimalFormat dfMoney = new DecimalFormat("#,###");

    public Trangchupanel() {
        initUI();
        loadStat();
        loadspbang();
    }

    private void initUI() {
        this.setLayout(new BorderLayout(20, 20));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel o_card = new JPanel(new GridLayout(1, 4, 20, 0));
        o_card.setPreferredSize(new Dimension(0, 120));

        lbDTN = taoLabelSo("0");
        lbDTT = taoLabelSo("0");
        lbDTThang = taoLabelSo("0");
        lbTongHD = taoLabelSo("0");

        o_card.add(createStatCard("Doanh Thu Hôm Nay", lbDTN, new Color(255, 255, 204)));
        o_card.add(createStatCard("Doanh Thu Tuần Này", lbDTT, new Color(255, 204, 204)));
        o_card.add(createStatCard("Doanh Thu Tháng Này", lbDTThang, new Color(204, 255, 153)));
        o_card.add(createStatCard("Tổng Số Hoá đơn", lbTongHD, new Color(204, 255, 204)));

        this.add(o_card, BorderLayout.NORTH);

        modelProducts = new DefaultTableModel(new String[] { "Mã SP", "Tên SP", "Số Lượng", "Giá" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tbProducts = new JTable(modelProducts);
        tbProducts.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tbProducts);
        this.add(scroll, BorderLayout.CENTER);
    }

    private JLabel taoLabelSo(String text) {
        JLabel lb = new JLabel(text);
        lb.setFont(new Font("Segoe UI Black", Font.BOLD, 24));
        lb.setForeground(new Color(222, 120, 255));
        lb.setHorizontalAlignment(SwingConstants.CENTER);
        return lb;
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color bg) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bg);
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void loadStat() {
        Connection conn = null;
        try {
            conn = ketnoidb.getConnection();
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Không thể kết nối database!");
                return;
            }

            String time = "ThoiGian";

            long tongHoaDon = DBTrangchu.csumHD(conn);

            double[] doanhThu = new double[3];
            DBTrangchu.laytkhd(conn, time, doanhThu);

            lbDTN.setText(dfMoney.format(doanhThu[0]) + " VNĐ");
            lbDTT.setText(dfMoney.format(doanhThu[1]) + " VNĐ");
            lbDTThang.setText(dfMoney.format(doanhThu[2]) + " VNĐ");

            lbTongHD.setText(String.valueOf(tongHoaDon));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể tải dashboard: " + e.getMessage());
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception ex) {
            }
        }
    }

    private void loadspbang() {
        try {

            DBTrangchu.doDuLieuProducts(modelProducts);

            for (int i = 0; i < modelProducts.getRowCount(); i++) {
                Object giaObj = modelProducts.getValueAt(i, 3);
                if (giaObj != null) {
                    long gia = Long.parseLong(giaObj.toString());
                    modelProducts.setValueAt(dfMoney.format(gia), i, 3);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi load Products: " + e.getMessage());
        }
    }

    public void refreshDashboard() {
        loadStat();
        loadspbang();
    }
}
