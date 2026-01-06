package VPP.View.login;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import VPP.database.DBLogin;
import VPP.View.mainUI.MainUI;
public class LoginFrame extends JFrame {

    public LoginFrame() {
        initComponents();
    }

    private void initComponents() {
        JPanel bgPanel = new JPanel();
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        JLabel lbchao = new JLabel();
        JLabel lbuser = new JLabel();
        JTextField o_user = new JTextField();
        JLabel lbmk = new JLabel();
        JPasswordField o_matkhau = new JPasswordField();
        JButton nutlogin = new JButton();
        JLabel lbdangki = new JLabel();
        JButton lbdangki2 = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setResizable(false);
        setSize(800, 500);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        bgPanel.setBackground(new Color(255, 255, 255));
        bgPanel.setLayout(null);
        bgPanel.setBounds(0, 0, 800, 500);

        leftPanel.setBackground(new Color(240, 248, 255));
        leftPanel.setLayout(null);
        leftPanel.setBounds(0, 0, 400, 500);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(255, 240, 245));
        headerPanel.setBounds(0, 0, 400, 100);
        headerPanel.setLayout(null);

        lbchao.setFont(new Font("Segoe UI", 1, 32));
        lbchao.setForeground(new Color(222, 120, 255));
        lbchao.setHorizontalAlignment(SwingConstants.CENTER);
        lbchao.setText("Welcome Back!");
        lbchao.setBounds(0, 30, 400, 40);
        headerPanel.add(lbchao);

        leftPanel.add(headerPanel);

        lbuser.setFont(new Font("Segoe UI", 1, 14));
        lbuser.setForeground(new Color(222, 120, 255));
        lbuser.setHorizontalAlignment(SwingConstants.CENTER);
        lbuser.setText("User");
        lbuser.setBounds(50, 160, 300, 20);
        leftPanel.add(lbuser);

        o_user.setFont(new Font("Segoe UI", 0, 14));
        o_user.setBounds(50, 185, 300, 40);
        leftPanel.add(o_user);

        lbmk.setFont(new Font("Segoe UI", 1, 14));
        lbmk.setForeground(new Color(222, 120, 255));
        lbmk.setHorizontalAlignment(SwingConstants.CENTER);
        lbmk.setText("Password");
        lbmk.setBounds(50, 230, 300, 20);
        leftPanel.add(lbmk);

        o_matkhau.setFont(new Font("Segoe UI", 0, 14));
        o_matkhau.setBounds(50, 255, 300, 40);
        leftPanel.add(o_matkhau);

        nutlogin.setBackground(new Color(186, 104, 200));
        nutlogin.setFont(new Font("Segoe UI", 1, 14));
        nutlogin.setForeground(new Color(255, 255, 255));
        nutlogin.setText("Login");
        nutlogin.setBorder(null);
        nutlogin.setFocusPainted(false);
        nutlogin.setBounds(125, 320, 150, 35);
        nutlogin.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
    
        String user = o_user.getText().trim();
        String pass = new String(o_matkhau.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin");
            return;
        }

        if (DBLogin.checkdn(user, pass)) {
            JOptionPane.showMessageDialog(null, "Đăng nhập thành công");
            new MainUI().setVisible(true);
            dispose(); 
            
        } else {
            JOptionPane.showMessageDialog(null, "Sai tài khoản hoặc mật khẩu");
        }
    }
});
        leftPanel.add(nutlogin);

        lbdangki.setFont(new Font("Tahoma", 0, 12));
        lbdangki.setForeground(new Color(222, 120, 255));
        lbdangki.setHorizontalAlignment(SwingConstants.CENTER);
        lbdangki.setText("Dont have an account ?");
        lbdangki.setBounds(50, 380, 180, 20);
        leftPanel.add(lbdangki);

        lbdangki2.setFont(new Font("Tahoma", 0, 12));
        lbdangki2.setForeground(new Color(222, 120, 255));
        lbdangki2.setText("Sign up here!");
        lbdangki2.setBorder(null);
        lbdangki2.setContentAreaFilled(false);
        lbdangki2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbdangki2.setBounds(230, 380, 100, 20);
        lbdangki2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignUpFrame().setVisible(true);
                dispose();
            }
        });
        leftPanel.add(lbdangki2);

        bgPanel.add(leftPanel);

        rightPanel.setLayout(null);
        rightPanel.setBounds(400, 0, 400, 500);

        JLabel lblImage = new JLabel();
        try {
            java.net.URL imgUrl = LoginFrame.class.getResource("/VPP/image/abc.jpg");
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image img = icon.getImage().getScaledInstance(400, 500, Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(img));
            } else {
                throw new java.io.FileNotFoundException("thieu anh (abc.jpg)");
            }
        } catch (Exception e) {
            lblImage.setText("thieu anh (abc.jpg)");
            lblImage.setHorizontalAlignment(SwingConstants.CENTER);
            lblImage.setOpaque(true);
            lblImage.setBackground(new Color(50, 50, 150));
            lblImage.setForeground(Color.WHITE);
        }
        lblImage.setBounds(0, 0, 400, 500);
        rightPanel.add(lblImage);

        bgPanel.add(rightPanel);

        getContentPane().add(bgPanel);
    } 
}