package VPP.View.mainUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import VPP.database.ketnoidb;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat; 

public class Hoadonpanel extends javax.swing.JPanel {

    private JTable tbHoaDon;
    private DefaultTableModel modelHoaDon;
    private DecimalFormat df = new DecimalFormat("#,###");

    public Hoadonpanel() {
        this.setLayout(new BorderLayout());

        String[] columns = {"Mã Hóa Đơn", "Tổng Tiền","Thoi Gian"};
        modelHoaDon = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        tbHoaDon = new JTable(modelHoaDon);
        tbHoaDon.getTableHeader().setReorderingAllowed(false);
        tbHoaDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.add(new JScrollPane(tbHoaDon), BorderLayout.CENTER);

        JPanel pl  = new JPanel(new FlowLayout());
        JButton xemchitiet = new JButton("Xem Chi Tiết");
        xemchitiet.setBackground(new Color( 222,120,255));
        xemchitiet.setForeground(Color.WHITE);
        JButton xoa = new JButton("Xóa Hóa Đơn");
        xoa.setBackground(new Color( 222,120,255));
        xoa.setForeground(Color.WHITE); 
        JButton timkiem = new JButton("Tìm Kiếm");
        timkiem.setBackground(new Color( 222,120,255));
        timkiem.setForeground(Color.WHITE);
        JButton lammoi = new JButton("Làm Mới");
        lammoi.setBackground(new Color( 222,120,255));
        lammoi.setForeground(Color.WHITE);

        pl.add(xemchitiet);
        pl.add(xoa);
        pl.add(timkiem);
        pl.add(lammoi);

        this.add(pl, BorderLayout.SOUTH);

        // --- SỰ KIỆN ---
        lammoi.addActionListener(e -> loadHoaDonList());
        xemchitiet.addActionListener(e -> xemChiTietHoaDon());
        xoa.addActionListener(e -> xoaHoaDon());
        timkiem.addActionListener(e->timkiem());

        // Load dữ liệu lần đầu
        loadHoaDonList();
    }

    private void loadHoaDonList() {
        modelHoaDon.setRowCount(0);
        String sql = "SELECT * FROM HoaDon ORDER BY maHD DESC";

        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int tongtien = rs.getInt("TongTien");
                modelHoaDon.addRow(new Object[]{
                        rs.getString("maHD"),
                        df.format(tongtien),
                        rs.getDate("ThoiGian")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách hóa đơn: " + e.getMessage());
        }
    }

    private void xemChiTietHoaDon() {
        int k= tbHoaDon.getSelectedRow();
        if (k== -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để xem!");
            return;
        }

        String maHDS = (String) modelHoaDon.getValueAt(k, 0);

        // Tạo Dialog hiển thị chi tiết
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết hóa đơn #" + maHDS, true);
        dialog.setSize(600, 400);
        dialog.setLayout(new BorderLayout());

        DefaultTableModel modelCT = new DefaultTableModel(new String[]{"Mã HD","Mã SP", "Tên SP", "Đơn Giá", "Số Lượng", "Thành Tiền","Thời Gian"}, 0);
        JTable tbCT = new JTable(modelCT);

        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHD = ?";
        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, maHDS);
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                double gia = rs.getDouble("giaSP");
                int sl = rs.getInt("soluongSP");
                double thanhTien = gia * sl;

                modelCT.addRow(new Object[]{
                        rs.getString("maHD"),
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        df.format(gia),
                        sl,
                        df.format(thanhTien),
                        rs.getDate("ThoiGian")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dialog.add(new JScrollPane(tbCT), BorderLayout.CENTER);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void xoaHoaDon() {
        int k = tbHoaDon.getSelectedRow();
        if (k == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để xóa!");
            return;
        }

        String maHD = (String) modelHoaDon.getValueAt(k, 0);
        JDialog xoahd = new JDialog((Frame) null, " Xóa Hóa Đơn", true);
        xoahd.setSize(300, 200);
        xoahd.setLayout(new BorderLayout());
        JLabel lb =new  JLabel("Bạn có chắc muốn xóa hóa đơn có mã: "+maHD+" không?");
        xoahd.add(lb, BorderLayout.CENTER);
        JPanel pl = new JPanel();
        JButton co = new JButton("Có");
        JButton khong= new JButton("Không");
        pl.add(co);
        pl.add(khong);
        xoahd.add(pl, BorderLayout.SOUTH);
        co.addActionListener(ev -> {
            try{
                String sql = "DELETE FROM chitiethoadon WHERE maHD= ?";
                String sqll = "DELETE FROM hoadon WHERE maHD= ?";
                Connection conn = ketnoidb.getConnection();
                PreparedStatement r = conn.prepareStatement(sql);
                PreparedStatement r1 = conn.prepareStatement(sqll);
                r.setString(1,maHD);
                r.executeUpdate();
                r1.setString(1,maHD);
                r1.executeUpdate();
                JOptionPane.showMessageDialog(xoahd,"Xóa Sản phẩm thành công!");
                xoahd.dispose();
                loadHoaDonList();
            }
            catch(Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(xoahd, "Lỗi khi sửa!");
            }

        });
        khong.addActionListener(ev -> xoahd.dispose());
        xoahd.setLocationRelativeTo(this);
        xoahd.setVisible(true);

    }


    private void timkiem(){
        JDialog  timhd = new JDialog((Frame) null, "Tìm Hóa Đơn", true);
        timhd.setSize(300, 200);
        timhd.setLayout(new BorderLayout());
        JLabel lb = new JLabel(" Nhập Mã Hóa Đơn Cần Tìm");
        JTextField tf = new JTextField();
        timhd.add (lb, BorderLayout.NORTH);
        timhd.add(tf, BorderLayout.CENTER);
        JPanel pl = new JPanel(new FlowLayout());
        JButton tim = new  JButton("Tìm kiếm");
        JButton huy = new JButton("Hủy");
        pl.add(tim);
        pl.add(huy);
        timhd.add(pl, BorderLayout.SOUTH);
        tim.addActionListener(e->{
            try{
                String sql= "SELECT * FROM  hoadon WHERE maHD = ?";
                Connection conn = ketnoidb.getConnection();
                PreparedStatement rs = conn.prepareStatement(sql);
                rs.setString(1,tf.getText());
                ResultSet a = rs.executeQuery();

                modelHoaDon.setRowCount(0);
                while (a.next()) {
                    modelHoaDon.addRow(new Object[]{
                            a.getString("maHD"),
                            df.format(a.getDouble("tongtien")),
                    });
                }
                if(modelHoaDon.getRowCount()==0){
                    JOptionPane.showMessageDialog(timhd,"Không tìm thấy hóa đơn");
                }
                timhd.dispose();


            }catch(Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(timhd, "Lỗi khi tìm hoá đơn");
            }

        });
        huy.addActionListener(ev -> timhd.dispose());
        timhd.setLocationRelativeTo(this);
        timhd.setVisible(true);

    }
}

