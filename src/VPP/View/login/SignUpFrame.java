package VPP.View.login;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import VPP.database.DBLogin;


public class SignUpFrame extends JFrame {

    public SignUpFrame() {
        initComponents();
    }

    private void initComponents() {
        JPanel bgPanel = new JPanel();
        JPanel paneltrai = new JPanel();
        JPanel rightPanel = new JPanel();
        JLabel lb = new JLabel();
        
        JLabel lb_user = new JLabel();
        JTextField txtUser = new JTextField();
        JLabel lb_mk = new JLabel();
        JPasswordField txtPass = new JPasswordField();
        JLabel xacnhan = new JLabel();
        JPasswordField o_xacnhan = new JPasswordField();
        
        JButton nutdk = new JButton();
        JLabel lblogin = new JLabel();
        JButton nutlogin = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sign Up");
        setResizable(false);
        setSize(800, 500);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        bgPanel.setBackground(new Color(255, 255, 255));
        bgPanel.setLayout(null);
        bgPanel.setBounds(0, 0, 800, 500);

        paneltrai.setBackground(new Color(240, 248, 255));
        paneltrai.setLayout(null);
        paneltrai.setBounds(0, 0, 400, 500);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(255, 240, 245));
        headerPanel.setBounds(0, 0, 400, 80);
        headerPanel.setLayout(null);

        lb.setFont(new Font("Segoe UI", 1, 32));
        lb.setForeground(new Color(222, 120, 255));
        lb.setHorizontalAlignment(SwingConstants.CENTER);
        lb.setText("Create An Account");
        lb.setBounds(0, 20, 400, 40);
        headerPanel.add(lb);

        paneltrai.add(headerPanel);
;
        lb_user.setFont(new Font("Segoe UI", 1, 13));
        lb_user.setForeground(new Color(222, 120, 255));
        lb_user.setHorizontalAlignment(SwingConstants.CENTER);
        lb_user.setText("Username:");
        lb_user.setBounds(50, 110, 300, 20);
        paneltrai.add(lb_user);

        txtUser.setFont(new Font("Segoe UI", 0, 14));
        txtUser.setBounds(50, 130, 300, 40);
        paneltrai.add(txtUser);

        lb_mk.setFont(new Font("Segoe UI", 1, 13));
        lb_mk.setForeground(new Color(222, 120, 255));
        lb_mk.setHorizontalAlignment(SwingConstants.CENTER);
        lb_mk.setText("Password:");
        lb_mk.setBounds(50, 170, 300, 20);
        paneltrai.add(lb_mk);

        txtPass.setFont(new Font("Segoe UI", 0, 14));
        txtPass.setBounds(50, 190, 300, 40);
        paneltrai.add(txtPass);
        
        xacnhan.setFont(new Font("Segoe UI", 1, 13));
        xacnhan.setForeground(new Color(222, 120, 255));
        xacnhan.setHorizontalAlignment(SwingConstants.CENTER);
        xacnhan.setText("Confirm Password");
        xacnhan.setBounds(50, 230, 300, 20);
        paneltrai.add(xacnhan);

        o_xacnhan.setFont(new Font("Segoe UI", 0, 14));
        o_xacnhan.setBounds(50, 250, 300, 40);
        paneltrai.add(o_xacnhan);

        nutdk.setBackground(new Color(186, 104, 200));
        nutdk.setFont(new Font("Segoe UI", 1, 14));
        nutdk.setForeground(new Color(255, 255, 255));
        nutdk.setText("Sign Up");
        nutdk.setBorder(null);
        nutdk.setFocusPainted(false);
        nutdk.setBounds(125, 310, 150, 35);
        paneltrai.add(nutdk);

        lblogin.setFont(new Font("Tahoma", 0, 12));
        lblogin.setForeground(new Color(222, 120, 255));
        lblogin.setHorizontalAlignment(SwingConstants.CENTER);
        lblogin.setText("Already have an account ?");
        lblogin.setBounds(40, 380, 180, 20);
        nutdk.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {

        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());
        String confirm = new String(o_xacnhan.getPassword());

        if (user.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin");
            return;
        }

        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(null, "Mật khẩu xác nhận không khớp");
            return;
        }

        if (DBLogin.checkdk(user)) {
            JOptionPane.showMessageDialog(null, "Tài khoản đã tồn tại");
            return;
        }

        if (DBLogin.chenuser(user, pass)) {
            JOptionPane.showMessageDialog(null, "Đăng ký thành công");
            new LoginFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Đăng ký thất bại");
        }
    }
});

        paneltrai.add(lblogin);

        nutlogin.setFont(new Font("Tahoma", 0, 12));
        nutlogin.setForeground(new Color(222, 120, 255));
        nutlogin.setText("Login here!");
        nutlogin.setBorder(null);
        nutlogin.setContentAreaFilled(false);
        nutlogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nutlogin.setBounds(220, 380, 100, 20);
        nutlogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
        paneltrai.add(nutlogin);

        bgPanel.add(paneltrai);

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