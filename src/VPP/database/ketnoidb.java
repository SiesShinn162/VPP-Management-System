package VPP.database;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class ketnoidb {

    private static final String URL = "jdbc:mysql://localhost:3306/vppmanager";
    private static final String USER = "root";
    private static final String PASSWORD = "admin"; // sửa mk ở đây nhé

    private static final String URL_SERVER = "jdbc:mysql://localhost:3306/?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Ho_Chi_Minh";

    // ==== THÊM MỚI: tự khởi tạo DB + bảng + dữ liệu mẫu
    static {
        try {
            khoiTaoDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void khoiTaoDB() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection conn = DriverManager.getConnection(URL_SERVER, USER, PASSWORD);
                Statement st = conn.createStatement()) {

            st.execute("CREATE DATABASE IF NOT EXISTS vppmanager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement st = conn.createStatement()) {

            st.execute(
                    "CREATE TABLE IF NOT EXISTS Products (" +
                            "  maSP VARCHAR(20) PRIMARY KEY," +
                            "  tenSP VARCHAR(255)," +
                            "  giaSP DOUBLE," +
                            "  soluongSP INT" +
                            ") ENGINE=InnoDB");

            st.execute(
                    "CREATE TABLE IF NOT EXISTS HoaDon (" +
                            "  maHD VARCHAR(20) PRIMARY KEY," +
                            "  TongTien DOUBLE," +
                            "  ThoiGian DATETIME DEFAULT CURRENT_TIMESTAMP" +
                            ") ENGINE=InnoDB");

            st.execute(
                    "CREATE TABLE IF NOT EXISTS ChiTietHoaDon (" +
                            "  id INT AUTO_INCREMENT PRIMARY KEY," +
                            "  maHD VARCHAR(20) NOT NULL," +
                            "  maSP VARCHAR(20) NOT NULL," +
                            "  tenSP VARCHAR(255) NOT NULL," +
                            "  giaSP DOUBLE NOT NULL," +
                            "  soluongSP INT NOT NULL," +
                            "  ThanhTien DOUBLE NOT NULL," +
                            "  ThoiGian DATETIME NOT NULL," +
                            "  CONSTRAINT fk_cthd_hd FOREIGN KEY (maHD) REFERENCES HoaDon(maHD) " +
                            "    ON DELETE CASCADE ON UPDATE CASCADE," +
                            "  CONSTRAINT fk_cthd_sp FOREIGN KEY (maSP) REFERENCES Products(maSP) " +
                            "    ON UPDATE CASCADE" +
                            ") ENGINE=InnoDB");

        }

        datasample();
    }

    private static void datasample() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);

            int countProducts = 0;
            try (Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM Products")) {
                if (rs.next())
                    countProducts = rs.getInt(1);
            }

            if (countProducts == 0) {
                String[][] products = new String[][] {
                        { "SP001", "Bút bi Thiên Long TL-027", "5000", "100" },
                        { "SP002", "Vở 200 trang Campus", "15000", "60" },
                        { "SP003", "Giấy in A4 Double A", "120000", "30" },
                        { "SP004", "Bìa cứng 2 lá Deli", "8000", "80" },
                        { "SP005", "Kẹp giấy 19mm", "3000", "200" },
                        { "SP006", "Tẩy chì Thiên Long", "2000", "150" },
                        { "SP007", "Thước kẻ 30cm Deli", "7000", "90" },
                        { "SP008", "Bút lông dầu Artline", "10000", "70" },
                        { "SP009", "Sổ tay bìa da A5", "25000", "40" },
                        { "SP010", "Băng keo trong suốt", "6000", "120" },
                        { "SP011", "Kéo cắt giấy 16cm", "15000", "50" },
                        { "SP012", "Bút dạ quang Stabilo", "12000", "110" },
                        { "SP013", "Giấy note màu Post-it", "18000", "75" },
                        { "SP014", "Tập giấy in A4 70gsm", "90000", "35" },
                        { "SP015", "Bút chì gỗ HB", "4000", "180" },
                        { "SP016", "Keo dán giấy UHU", "15000", "65" },
                        { "SP017", "Bảng trắng viết marker", "250000", "20" },
                        { "SP018", "Máy tính bỏ túi Casio", "350000", "15" },
                        { "SP019", "Cặp sách học sinh", "200000", "25" },
                        { "SP020", "Thùng rác văn phòng nhỏ", "50000", "55" }
                };

                String sqlInsertP = "INSERT INTO Products(maSP, tenSP, giaSP, soluongSP) VALUES (?,?,?,?)";
                try (PreparedStatement ps = conn.prepareStatement(sqlInsertP)) {
                    for (String[] p : products) {
                        ps.setString(1, p[0]);
                        ps.setString(2, p[1]);
                        ps.setDouble(3, Double.parseDouble(p[2]));
                        ps.setInt(4, Integer.parseInt(p[3]));
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }

            int countHD = 0;
            try (Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM HoaDon")) {
                if (rs.next())
                    countHD = rs.getInt(1);
            }

            int countCT = 0;
            try (Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM ChiTietHoaDon")) {
                if (rs.next())
                    countCT = rs.getInt(1);
            }

            if (countHD == 0 && countCT == 0) {

                String sqlInsertHD = "INSERT INTO HoaDon(maHD, TongTien, ThoiGian) VALUES (?,?,?)";
                try (PreparedStatement psHD = conn.prepareStatement(sqlInsertHD)) {
                    for (int i = 1; i <= 10; i++) {
                        String maHD = String.format("HD-TEST%02d", i);
                        psHD.setString(1, maHD);
                        psHD.setDouble(2, 0); // sẽ update theo chi tiết
                        psHD.setTimestamp(3,
                                new java.sql.Timestamp(System.currentTimeMillis() - (long) (i) * 3600_000)); // lệch giờ
                                                                                                             // cho đẹp
                        psHD.addBatch();
                    }
                    psHD.executeBatch();
                }

                Object[][] cts = new Object[][] {
                        { "HD-TEST01", "SP001", "Bút bi Thiên Long TL-027", 5000d, 10 },
                        { "HD-TEST01", "SP002", "Vở 200 trang Campus", 15000d, 2 },

                        { "HD-TEST02", "SP003", "Giấy in A4 Double A", 120000d, 1 },
                        { "HD-TEST02", "SP005", "Kẹp giấy 19mm", 3000d, 10 },

                        { "HD-TEST03", "SP007", "Thước kẻ 30cm Deli", 7000d, 3 },
                        { "HD-TEST03", "SP010", "Băng keo trong suốt", 6000d, 5 },

                        { "HD-TEST04", "SP012", "Bút dạ quang Stabilo", 12000d, 4 },
                        { "HD-TEST04", "SP013", "Giấy note màu Post-it", 18000d, 2 },

                        { "HD-TEST05", "SP015", "Bút chì gỗ HB", 4000d, 12 },
                        { "HD-TEST05", "SP006", "Tẩy chì Thiên Long", 2000d, 10 },

                        { "HD-TEST06", "SP011", "Kéo cắt giấy 16cm", 15000d, 2 },
                        { "HD-TEST06", "SP016", "Keo dán giấy UHU", 15000d, 1 },

                        { "HD-TEST07", "SP014", "Tập giấy in A4 70gsm", 90000d, 1 },
                        { "HD-TEST07", "SP004", "Bìa cứng 2 lá Deli", 8000d, 6 },

                        { "HD-TEST08", "SP008", "Bút lông dầu Artline", 10000d, 3 },
                        { "HD-TEST08", "SP009", "Sổ tay bìa da A5", 25000d, 2 },

                        { "HD-TEST09", "SP017", "Bảng trắng viết marker", 250000d, 1 },
                        { "HD-TEST09", "SP020", "Thùng rác văn phòng nhỏ", 50000d, 2 },

                        { "HD-TEST10", "SP018", "Máy tính bỏ túi Casio", 350000d, 1 },
                        { "HD-TEST10", "SP019", "Cặp sách học sinh", 200000d, 1 }
                };

                String sqlInsertCT = "INSERT INTO ChiTietHoaDon (maHD, maSP, tenSP, giaSP, soluongSP, ThanhTien, ThoiGian) "
                        +
                        "VALUES (?,?,?,?,?,?,?)";

                try (PreparedStatement psCT = conn.prepareStatement(sqlInsertCT)) {
                    long now = System.currentTimeMillis();
                    for (int i = 0; i < cts.length; i++) {
                        String maHD = (String) cts[i][0];
                        String maSP = (String) cts[i][1];
                        String tenSP = (String) cts[i][2];
                        double giaSP = (double) cts[i][3];
                        int soLuong = (int) cts[i][4];
                        double thanhTien = giaSP * soLuong;

                        psCT.setString(1, maHD);
                        psCT.setString(2, maSP);
                        psCT.setString(3, tenSP);
                        psCT.setDouble(4, giaSP);
                        psCT.setInt(5, soLuong);
                        psCT.setDouble(6, thanhTien);
                        psCT.setTimestamp(7, new java.sql.Timestamp(now - (long) (i) * 60_000)); // lệch phút
                        psCT.addBatch();
                    }
                    psCT.executeBatch();
                }

                try (Statement st = conn.createStatement()) {
                    st.executeUpdate(
                            "UPDATE HoaDon hd SET TongTien = (" +
                                    "  SELECT COALESCE(SUM(ct.ThanhTien),0) FROM ChiTietHoaDon ct WHERE ct.maHD = hd.maHD"
                                    +
                                    ")");
                }
            }

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}