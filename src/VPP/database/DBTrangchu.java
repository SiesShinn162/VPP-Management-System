package VPP.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;

public class DBTrangchu {
    public static double layGiaTri(Connection conn, String sql) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0;
    }

    public static void laytkhd(Connection conn, String time, double[] dt) throws Exception {
        double totalRevenue = layGiaTri(conn, "SELECT COALESCE(SUM(TongTien), 0) AS total FROM HoaDon");

        double doanhThuHomNay = totalRevenue;
        double doanhThuTuan = totalRevenue;
        double doanhThuThang = totalRevenue;

        if (time != null && time.trim().length() > 0) {
            String sqlToday = "SELECT COALESCE(SUM(TongTien), 0) AS total "
                    + "FROM HoaDon WHERE DATE(" + time + ") = CURDATE()";

            String sqlWeek = "SELECT COALESCE(SUM(TongTien), 0) AS total "
                    + "FROM HoaDon WHERE YEARWEEK(DATE(" + time + "), 1) = YEARWEEK(CURDATE(), 1)";

            String sqlMonth = "SELECT COALESCE(SUM(TongTien), 0) AS total "
                    + "FROM HoaDon WHERE YEAR(DATE(" + time + ")) = YEAR(CURDATE()) "
                    + "AND MONTH(DATE(" + time + ")) = MONTH(CURDATE())";

            doanhThuHomNay = layGiaTri(conn, sqlToday);
            doanhThuTuan = layGiaTri(conn, sqlWeek);
            doanhThuThang = layGiaTri(conn, sqlMonth);
        }

        dt[0] = doanhThuHomNay;
        dt[1] = doanhThuTuan;
        dt[2] = doanhThuThang;
    }

    public static long csumHD(Connection conn) throws Exception {
        return (long) layGiaTri(conn, "SELECT COUNT(*) AS total FROM HoaDon");
    }

    public static void doDuLieuProducts(DefaultTableModel model) throws Exception {
        model.setRowCount(0);

        String sql = "SELECT maSP, tenSP, soluongSP, giaSP FROM Products ORDER BY maSP";

        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String ma = rs.getString("maSP");
                String ten = rs.getString("tenSP");
                int soLuong = rs.getInt("soluongSP");
                long gia = rs.getLong("giaSP"); 

                model.addRow(new Object[]{ma, ten, soLuong, gia});
            }
        }
    }
}
