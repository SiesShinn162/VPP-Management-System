package VPP.View.mainUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import VPP.database.ketnoidb;


public class Sanphampanel extends javax.swing.JPanel {

    JTable table;
    DefaultTableModel model;
    public Sanphampanel() {
        this.setLayout(new BorderLayout());
        JPanel panel = new JPanel(new BorderLayout());
        model = new DefaultTableModel(
                new String[]{"Mã SP", "Tên SP", "Giá", "Số lượng"}, 0
        );
        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        this.add(new JScrollPane(table), BorderLayout.CENTER);


        JPanel nut = new JPanel(new FlowLayout());
            
        JButton them = new JButton("Thêm");
        them.setBackground(new Color(222, 120, 255));
        them.setForeground(Color.WHITE);
        
        JButton sua = new  JButton("Sửa");
        sua.setBackground(new Color(222, 120, 255));
        sua.setForeground(Color.WHITE);
        
        JButton xoa = new JButton("Xóa");
        xoa.setBackground(new Color(222, 120, 255));
        xoa.setForeground(Color.WHITE);
        
        JButton tim = new JButton("Tìm Kiếm");
        tim.setBackground(new Color(222, 120, 255));
        tim.setForeground(Color.WHITE);
        
        JButton lammoi = new JButton("Làm mới");
        lammoi.setBackground(new Color(222, 120, 255));
        lammoi.setForeground(Color.WHITE);
        nut.add(them);
        nut.add(sua);
        nut.add(xoa);
        nut.add(tim);
        nut.add(lammoi);
        this.add(nut, BorderLayout.SOUTH);

        loadTable();
        them.addActionListener(e->themsp());
        sua.addActionListener(e->suaSP());
        xoa.addActionListener(e->xoasp());
        tim.addActionListener(e->timsp());
        lammoi.addActionListener(e->loadTable());


    }
    public void loadTable() {
        model.setRowCount(0);
        try {
            String sql = "SELECT * FROM Products";
            Connection conn = ketnoidb.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        rs.getInt("giaSP"),
                        rs.getInt("soluongSP")
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void themsp(){

        JDialog dialog = new JDialog((Frame) null, "Thêm sản phẩm", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new GridLayout(5, 2, 5, 5));

        JTextField maField = new JTextField();
        JTextField tenField = new JTextField();
        JTextField giaField = new JTextField();
        JTextField soLuongField = new JTextField();

        JButton btnOK = new JButton("OK");
        JButton btnCancel = new JButton("Hủy");

        dialog.add(new JLabel("Mã SP:"));
        dialog.add(maField);
        dialog.add(new JLabel("Tên SP:"));
        dialog.add(tenField);
        dialog.add(new JLabel("Giá:"));
        dialog.add(giaField);
        dialog.add(new JLabel("Số lượng:"));
        dialog.add(soLuongField);
        dialog.add(btnOK);
        dialog.add(btnCancel);

        btnOK.addActionListener(ev -> {
            try {
                String sql = "INSERT INTO Products(maSP, tenSP, giaSP, soluongSP) VALUES(?,?,?,?)";
                Connection conn = ketnoidb.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, maField.getText());
                ps.setString(2, tenField.getText());
                ps.setInt(3, Integer.parseInt(giaField.getText()));
                ps.setInt(4, Integer.parseInt(soLuongField.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(dialog, "Thêm thành công!");
                dialog.dispose();
                loadTable();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Lỗi khi thêm!");
            }
        });

        btnCancel.addActionListener(ev -> dialog.dispose());

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);


    }
    public void suaSP() {
        int k = table.getSelectedRow();
        if (k == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm để sửa!");
            return;
        }

        String maSP = (String) model.getValueAt(k, 0);
        String tenSP = (String) model.getValueAt(k, 1);
        int giaSP = (int) model.getValueAt(k, 2);
        int soLuongSP = (int) model.getValueAt(k, 3);

        // Tạo dialog
        JDialog dialog = new JDialog((Frame) null, "Sửa sản phẩm", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new GridLayout(5, 2, 5, 5));

        JTextField maField = new JTextField(maSP);
        JTextField tenField = new JTextField(tenSP);
        JTextField giaField = new JTextField(String.valueOf(giaSP));
        JTextField soLuongField = new JTextField(String.valueOf(soLuongSP));

        JButton btnOK = new JButton("OK");
        JButton btnCancel = new JButton("Hủy");

        dialog.add(new JLabel("Mã SP:"));
        dialog.add(maField);
        dialog.add(new JLabel("Tên SP:"));
        dialog.add(tenField);
        dialog.add(new JLabel("Giá:"));
        dialog.add(giaField);
        dialog.add(new JLabel("Số lượng:"));
        dialog.add(soLuongField);
        dialog.add(btnOK);
        dialog.add(btnCancel);

        btnOK.addActionListener(ev -> {
            try {
                String sql = "UPDATE Products SET tenSP=?, giaSP=?, soluongSP=? WHERE maSP=?";
                Connection conn = ketnoidb.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, tenField.getText());
                ps.setInt(2, Integer.parseInt(giaField.getText()));
                ps.setInt(3, Integer.parseInt(soLuongField.getText()));
                ps.setString(4, maField.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(dialog, "Sửa thành công!");
                dialog.dispose();
                loadTable();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Lỗi khi sửa!");
            }
        });

        btnCancel.addActionListener(ev -> dialog.dispose());

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public void xoasp(){
        int k = table.getSelectedRow();
        if(k==-1){
            JOptionPane.showMessageDialog(this,"Chọn một sản phẩm để xóa!");
            return;
        }
        String maspp =(String) model.getValueAt(k,0);
        JDialog xoaspp = new JDialog((Frame) null, " Xóa Sản phẩm", true);
        xoaspp.setSize(300, 200);
        xoaspp.setLayout(new BorderLayout());
        JLabel lb =new  JLabel("Bạn có chắc muốn xóa sản phẩm có mã: "+maspp+" không?");
        xoaspp.add(lb, BorderLayout.CENTER);
        JPanel pl = new JPanel();
        JButton co = new JButton("Có");
        JButton khong= new JButton("Không");
        pl.add(co);
        pl.add(khong);
        xoaspp.add(pl, BorderLayout.SOUTH);
        co.addActionListener(ev -> {
            try{
                String sqll = "DELETE FROM Products WHERE maSP=?";
                Connection conn = ketnoidb.getConnection();
                PreparedStatement r = conn.prepareStatement(sqll);
                r.setString(1,maspp);
                r.executeUpdate();
                JOptionPane.showMessageDialog(xoaspp,"Xóa Sản phẩm thành công!");
                xoaspp.dispose();
                loadTable();
            }
            catch(Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(xoaspp, "Lỗi khi sửa!");
            }

        });
        khong.addActionListener(ev -> xoaspp.dispose());
        xoaspp.setLocationRelativeTo(this);
        xoaspp.setVisible(true);


    }
    public void timsp(){
        JDialog  timsp = new JDialog((Frame) null, "Timsp", true);
        timsp.setSize(300, 200);
        timsp.setLayout(new BorderLayout());
        JLabel lb = new JLabel(" Nhập tên sản phẩm cần tìm");
        JTextField tf = new JTextField();
        timsp.add (lb, BorderLayout.NORTH);
        timsp.add(tf, BorderLayout.CENTER);
        JPanel pl = new JPanel(new FlowLayout());
        JButton tim = new  JButton("Tìm kiếm");
        JButton huy = new JButton("Hủy");
        pl.add(tim);
        pl.add(huy);
        timsp.add(pl, BorderLayout.SOUTH);
        tim.addActionListener(ev -> {
            try{
                String sql= "SELECT * FROM  Products WHERE tenSP LIKE ?";
                Connection conn = ketnoidb.getConnection();
                PreparedStatement r = conn.prepareStatement(sql);
                r.setString(1,"%"+tf.getText()+"%");
                ResultSet rs = r.executeQuery();
                model.setRowCount(0);

                while(rs.next()){model.addRow(new Object[]{
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        rs.getInt("giaSP"),
                        rs.getInt("soluongSP")
                });}
                if(model.getRowCount()==0){
                    JOptionPane.showMessageDialog(timsp,"Không tìm thấy san phẩm");
                }
                timsp.dispose();


            }
            catch(Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(timsp, "Lỗi khi tìm sản phẩm");
            }
        });
        huy.addActionListener(ev -> timsp.dispose());
        timsp.setLocationRelativeTo(this);
        timsp.setVisible(true);

    }
}