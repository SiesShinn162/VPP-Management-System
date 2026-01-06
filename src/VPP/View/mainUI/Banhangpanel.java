package VPP.View.mainUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import VPP.database.ketnoidb;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

class ProductItem {
    private String maSP;
    private String tenSP;

    public ProductItem(String maSP, String tenSP) {
        this.maSP = maSP;
        this.tenSP = tenSP;
    }

    public String getMaSP() { return maSP; }
    public String getTenSP() { return tenSP; }

    @Override
    public String toString() {
        return maSP + "-" + tenSP;
    }
}

public class Banhangpanel extends javax.swing.JPanel {

    private JTable tbGiohang;
    private DefaultTableModel DTbGiohang;
    private JComboBox<ProductItem> cbSanpham;
    private JTextField tfSoluong;
    private JLabel lbTongtien;
    private double Tongtien = 0;
    private DecimalFormat df = new DecimalFormat("#,###");

    public Banhangpanel() {
        this.setLayout(new BorderLayout(10, 10));

        // Khu vực nhập liệu (NORTH)
        JPanel pnlInput = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cbSanpham = new JComboBox<>();

        // Cập nhật lại danh sách mỗi khi mở combo
        cbSanpham.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {
                loadProductsToCombo();
            }
            @Override
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {}
            @Override
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {}
        });

        tfSoluong = new JTextField(5);
        JButton btnAdd = new JButton("Thêm vào giỏ");
        btnAdd.setBackground(new Color(0, 153, 76));
        btnAdd.setForeground(Color.WHITE);

        JButton btnRemove = new JButton("Xóa khỏi giỏ");
        btnRemove.setBackground(new Color(204, 0, 0));
        btnRemove.setForeground(Color.WHITE);

        pnlInput.add(new JLabel("Sản phẩm:"));
        pnlInput.add(cbSanpham);
        pnlInput.add(new JLabel("Số lượng:"));
        pnlInput.add(tfSoluong);
        pnlInput.add(btnAdd);
        pnlInput.add(btnRemove);

        // Giỏ hàng (CENTER)
        String[] columns = {"Mã SP", "Tên SP", "Số lượng", "Đơn giá", "Thành tiền"};
        DTbGiohang = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tbGiohang = new JTable(DTbGiohang);
        tbGiohang.getTableHeader().setReorderingAllowed(false);
        tbGiohang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Thanh toán (SOUTH)
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lbTongtien = new JLabel("Tổng tiền: 0 VNĐ");
        lbTongtien.setFont(new Font("Arial", Font.BOLD, 16));

        JButton btnThanhtoan = new JButton("Thanh toán & Xuất dữ liệu hóa đơn");
        btnThanhtoan.setPreferredSize(new Dimension(280, 40));
        btnThanhtoan.setBackground(new Color(222,120,255));
        btnThanhtoan.setForeground(Color.WHITE);

        pnlFooter.add(lbTongtien);
        pnlFooter.add(Box.createHorizontalStrut(20));
        pnlFooter.add(btnThanhtoan);

        add(pnlInput, BorderLayout.NORTH);
        add(new JScrollPane(tbGiohang), BorderLayout.CENTER);
        add(pnlFooter, BorderLayout.SOUTH);

        // Sự kiện
        btnAdd.addActionListener(e -> addToCart());
        btnRemove.addActionListener(e -> removeFromCart());
        btnThanhtoan.addActionListener(e -> processPayment());

        loadProductsToCombo();
    }

    private void loadProductsToCombo() {
        ProductItem currentSelection = (ProductItem) cbSanpham.getSelectedItem();
        cbSanpham.removeAllItems();
        try (Connection conn = ketnoidb.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT maSP, tenSP FROM Products")) {

            while (rs.next()) {
                cbSanpham.addItem(new ProductItem(rs.getString("maSP"), rs.getString("tenSP")));
            }

            // Giữ lại lựa chọn cũ nếu còn tồn tại
            if (currentSelection != null) {
                for (int i = 0; i < cbSanpham.getItemCount(); i++) {
                    if (cbSanpham.getItemAt(i).getMaSP().equals(currentSelection.getMaSP())) {
                        cbSanpham.setSelectedIndex(i);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addToCart() {
        ProductItem selectedItem = (ProductItem) cbSanpham.getSelectedItem();
        if (selectedItem == null) return;

        String maSP_selected = selectedItem.getMaSP();
        String tenSP_selected = selectedItem.getTenSP();

        int qty;
        try {
            qty = Integer.parseInt(tfSoluong.getText().trim());
            if (qty <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng nguyên dương!");
            return;
        }

        try (Connection conn = ketnoidb.getConnection()) {
            conn.setAutoCommit(false);

            // 1. Kiểm tra tồn kho
            String sqlCheck = "SELECT giaSP, soluongSP FROM Products WHERE maSP = ?";
            PreparedStatement pstCheck = conn.prepareStatement(sqlCheck);
            pstCheck.setString(1, maSP_selected);
            ResultSet rs = pstCheck.executeQuery();

            if (rs.next()) {
                int stock = rs.getInt("soluongSP");
                double price = rs.getDouble("giaSP");

                if (qty > stock) {
                    JOptionPane.showMessageDialog(this, "Số lượng tồn kho không đủ! (Còn lại: " + stock + ")");
                    conn.rollback();
                    return;
                }

                // 2. Trừ kho trong database
                String sqlUpdateStock = "UPDATE Products SET soluongSP = soluongSP - ? WHERE maSP = ?";
                PreparedStatement pstUp = conn.prepareStatement(sqlUpdateStock);
                pstUp.setInt(1, qty);
                pstUp.setString(2, maSP_selected);
                pstUp.executeUpdate();

                // 3. Cập nhật JTable (Giỏ hàng)
                double subTotal = price * qty;
                DTbGiohang.addRow(new Object[]{maSP_selected, tenSP_selected, qty, price, subTotal});

                Tongtien += subTotal;
                updateTotalLabel();

                conn.commit();
                tfSoluong.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm vào giỏ hàng: " + e.getMessage());
        }
    }

    private void removeFromCart() {
        int selectedRow = tbGiohang.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm trong giỏ hàng để xóa!");
            return;
        }

        String maSP = DTbGiohang.getValueAt(selectedRow, 0).toString();
        int qty = Integer.parseInt(DTbGiohang.getValueAt(selectedRow, 2).toString());
        double subTotal = Double.parseDouble(DTbGiohang.getValueAt(selectedRow, 4).toString());

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = ketnoidb.getConnection()) {
                // Cộng trả lại số lượng vào kho
                String sqlUpdateStock = "UPDATE Products SET soluongSP = soluongSP + ? WHERE maSP = ?";
                PreparedStatement pst = conn.prepareStatement(sqlUpdateStock);
                pst.setInt(1, qty);
                pst.setString(2, maSP);
                pst.executeUpdate();

                // Cập nhật giao diện
                Tongtien -= subTotal;
                updateTotalLabel();
                DTbGiohang.removeRow(selectedRow);

                JOptionPane.showMessageDialog(this, "Đã xóa và hoàn trả số lượng vào kho!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi hoàn trả kho: " + e.getMessage());
            }
        }
    }

    private void processPayment() {
        if (DTbGiohang.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng trống!");
            return;
        }

        try (Connection conn = ketnoidb.getConnection()) {
            conn.setAutoCommit(false);

            Timestamp now = new Timestamp(System.currentTimeMillis());
            String uniqueID = UUID.randomUUID().toString();
            String maHD = "HD-" + uniqueID.substring(0, 8).toUpperCase();

            // 1. Lưu hóa đơn tổng
            String sqlHD = "INSERT INTO HoaDon VALUES (?,?,?)";
            PreparedStatement pstHD = conn.prepareStatement(sqlHD, Statement.RETURN_GENERATED_KEYS);
            pstHD.setString(1,maHD);
            pstHD.setDouble(2, Tongtien);
            pstHD.setTimestamp(3, now);
            pstHD.executeUpdate();



            // 2. Lưu chi tiết hóa đơn sử dụng Batch để tối ưu
            String sqlCT = "INSERT INTO ChiTietHoaDon (maHD, maSP, tenSP, giaSP, soluongSP,ThanhTien, ThoiGian) VALUES (?, ?, ?, ?, ?, ?,?)";
            PreparedStatement pstCT = conn.prepareStatement(sqlCT);

            for (int i = 0; i < DTbGiohang.getRowCount(); i++) {
                double ThanhTien = Double.parseDouble(DTbGiohang.getValueAt(i, 4).toString());

                pstCT.setString(1, maHD);
                pstCT.setString(2, DTbGiohang.getValueAt(i, 0).toString());
                pstCT.setString(3, DTbGiohang.getValueAt(i, 1).toString());
                pstCT.setDouble(4, Double.parseDouble(DTbGiohang.getValueAt(i, 3).toString()));
                pstCT.setInt(5, Integer.parseInt(DTbGiohang.getValueAt(i, 2).toString()));
                pstCT.setDouble(6, ThanhTien);
                pstCT.setTimestamp(7, now);
                pstCT.addBatch();
            }
            pstCT.executeBatch();

            conn.commit();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            JOptionPane.showMessageDialog(this, "Thanh toán thành công!\n"
                    + "Mã hóa đơn: " + maHD + "\n"
                    + "Ngày lập: " + sdf.format(now));

            clearCart();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi thanh toán: " + e.getMessage());
        }
    }

    private void updateTotalLabel() {
        lbTongtien.setText("Tổng tiền: " + df.format(Tongtien) + " VNĐ");
    }

    private void clearCart() {
        DTbGiohang.setRowCount(0);
        Tongtien = 0;
        updateTotalLabel();
        tfSoluong.setText("");
    }
}

    


