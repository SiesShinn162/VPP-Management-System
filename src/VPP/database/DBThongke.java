package VPP.database;

import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DBThongke {

    private static final DecimalFormat dinhDangTien = new DecimalFormat("#,###");

    
    public static void ttdtngay(Component giaoDien, DefaultTableModel modelNgay, int thang, int nam) {
        modelNgay.setRowCount(0);

        String sql =
                "SELECT DATE(ThoiGian) AS Ngay, COALESCE(SUM(TongTien), 0) AS DoanhThu " +
                "FROM HoaDon " +
                "WHERE MONTH(ThoiGian) = ? AND YEAR(ThoiGian) = ? " +
                "GROUP BY DATE(ThoiGian) " +
                "ORDER BY Ngay";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ketnoidb.getConnection();
            if (conn == null) {
                JOptionPane.showMessageDialog(giaoDien, "Không kết nối được database!");
                return;
            }

            ps = conn.prepareStatement(sql);
            ps.setInt(1, thang);
            ps.setInt(2, nam);

            rs = ps.executeQuery();
            while (rs.next()) {
                String ngay = rs.getString("Ngay");
                long doanhThu = rs.getLong("DoanhThu");

                modelNgay.addRow(new Object[]{ngay, dinhDangTien.format(doanhThu)});
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(giaoDien, "Lỗi thống kê theo ngày: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ex) {}
            try { if (ps != null) ps.close(); } catch (Exception ex) {}
            try { if (conn != null) conn.close(); } catch (Exception ex) {}
        }
    }



    public static void ttdtthang(Component giaoDien, DefaultTableModel modelThang, int nam) {
        modelThang.setRowCount(0);

        String sql =
                "SELECT MONTH(ThoiGian) AS Thang, COALESCE(SUM(TongTien), 0) AS DoanhThu " +
                "FROM HoaDon " +
                "WHERE YEAR(ThoiGian) = ? " +
                "GROUP BY MONTH(ThoiGian) " +
                "ORDER BY Thang";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ketnoidb.getConnection();
            if (conn == null) {
                JOptionPane.showMessageDialog(giaoDien, "Không kết nối được database!");
                return;
            }

            ps = conn.prepareStatement(sql);
            ps.setInt(1, nam);

            rs = ps.executeQuery();
            while (rs.next()) {
                int thang = rs.getInt("Thang");
                long doanhThu = rs.getLong("DoanhThu");

                modelThang.addRow(new Object[]{thang, dinhDangTien.format(doanhThu)});
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(giaoDien, "Lỗi thống kê theo tháng: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ex) {}
            try { if (ps != null) ps.close(); } catch (Exception ex) {}
            try { if (conn != null) conn.close(); } catch (Exception ex) {}
        }
    }

    
    public static void spbanchay(Component giaoDien, DefaultTableModel modelBanChay, int thang, int nam) {
        modelBanChay.setRowCount(0);

        String sql =
                "SELECT maSP, tenSP, " +
                "       COALESCE(SUM(soluongSP), 0) AS SoLuongBan, " +
                "       COALESCE(SUM(giaSP * soluongSP), 0) AS DoanhThu " +
                "FROM ChiTietHoaDon " +
                "WHERE MONTH(ThoiGian) = ? AND YEAR(ThoiGian) = ? " +
                "GROUP BY maSP, tenSP " +
                "ORDER BY SoLuongBan DESC " +
                "LIMIT 10";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ketnoidb.getConnection();
            if (conn == null) {
                JOptionPane.showMessageDialog(giaoDien, "Không kết nối được database!");
                return;
            }

            ps = conn.prepareStatement(sql);
            ps.setInt(1, thang);
            ps.setInt(2, nam);

            rs = ps.executeQuery();
            while (rs.next()) {
                String maSP = rs.getString("maSP");
                String tenSP = rs.getString("tenSP");
                int soLuongBan = rs.getInt("SoLuongBan");
                long doanhThu = rs.getLong("DoanhThu");

                modelBanChay.addRow(new Object[]{
                        maSP, tenSP, soLuongBan, dinhDangTien.format(doanhThu)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(giaoDien, "Lỗi thống kê bán chạy: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ex) {}
            try { if (ps != null) ps.close(); } catch (Exception ex) {}
            try { if (conn != null) conn.close(); } catch (Exception ex) {}
        }
    }
}
