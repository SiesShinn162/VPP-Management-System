package VPP.View.mainUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import VPP.View.login.LoginFrame;

public class MainUI extends javax.swing.JFrame {

    private JPanel pnhientai;
    private JPanel contentPanel;
    private Trangchupanel dashboardPanel;


    private JPanel nut_trangchu;
    private JPanel nut_sp;
    private JPanel nut_pos;
    private JPanel nut_hdon;
    private JPanel nut_thongke;
    private JPanel nut_about;

    public MainUI() {
        initComponents();
        pnhientai = contentPanel;

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public void chuyenPanel(JPanel panelMoi) {
        if (panelMoi != null) {
            pnhientai.removeAll();
            pnhientai.setLayout(new BorderLayout());
            pnhientai.add(panelMoi, BorderLayout.CENTER);
            pnhientai.revalidate();
            pnhientai.repaint();
        }
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("VPP By 5ThangLuoi Team");

        getContentPane().setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(222, 120, 255));
        topPanel.setPreferredSize(new Dimension(100, 60)); // Chiều cao 60px
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));

        JPanel topLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        topLeft.setOpaque(false);

        JLabel lblAvatar = new JLabel();
        try {
            lblAvatar.setIcon(new ImageIcon(
                    getClass().getResource("/VPP/image/account_circle_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24.png")));
        } catch (Exception e) {
        }

        JLabel lblHello = new JLabel("Chào, Admin");
        lblHello.setForeground(Color.WHITE);
        lblHello.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        topLeft.add(lblAvatar);
        topLeft.add(lblHello);

        JLabel lblTitle = new JLabel("Phần Mềm Quản Lí Bán Đồ Văn Phòng Phẩm V1", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI Light", Font.PLAIN, 20));

        topPanel.add(topLeft, BorderLayout.WEST);
        topPanel.add(lblTitle, BorderLayout.CENTER);

        //  Nút Đăng xuất 
        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        topRight.setOpaque(false);

        JPanel pnDangXuat = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        pnDangXuat.setOpaque(true);
        pnDangXuat.setBackground(new Color(222, 120, 255));
        pnDangXuat.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel lblDangXuat = new JLabel("Đăng xuất");
        lblDangXuat.setForeground(Color.WHITE);
        lblDangXuat.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        try {
            lblDangXuat.setIcon(new ImageIcon(getClass().getResource(
                    "/VPP/image/logout_32dp_E3E3E3_FILL0_wght700_GRAD0_opsz40.png")));
        } catch (Exception ex) {
            
        }
        // event nút đăng xuất
        pnDangXuat.add(lblDangXuat);
        pnDangXuat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                pnDangXuat.setCursor(new Cursor(Cursor.HAND_CURSOR));
                pnDangXuat.setBackground(new Color(192, 192, 192)); // giống hover sidebar
            }

            @Override
            public void mouseExited(MouseEvent e) {
                pnDangXuat.setBackground(new Color(222, 120, 255));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int chon = JOptionPane.showConfirmDialog(
                        MainUI.this,
                        "Bạn có chắc muốn đăng xuất không?",
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION);

                if (chon == JOptionPane.YES_OPTION) {
                    new LoginFrame().setVisible(true);
                    MainUI.this.dispose();
                }
            }
        });
        // add nút đăng xuất vào top panel
        topRight.add(pnDangXuat);
        topPanel.add(topRight, BorderLayout.EAST);

        JPanel sidePanel = new JPanel();
        sidePanel.setBackground(new Color(222, 120, 255));
        sidePanel.setPreferredSize(new Dimension(220, 0)); // Rộng 220px, cao tự động
        sidePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.WHITE));
        sidePanel.setLayout(new GridLayout(10, 1, 0, 5));

        nut_trangchu = createSideButton("Trang Chủ",
                "/VPP/image/home_app_logo_32dp_E3E3E3_FILL0_wght400_GRAD0_opsz40.png");
        nut_sp = createSideButton("QL Sản Phẩm", "/VPP/image/package_2_32dp_E3E3E3_FILL0_wght400_GRAD0_opsz40.png");
        nut_pos = createSideButton("Bán Hàng (POS)",
                "/VPP/image/shopping_bag_32dp_E3E3E3_FILL0_wght400_GRAD0_opsz40.png");
        nut_hdon = createSideButton("QL Hoá Đơn", "/VPP/image/receipt_32dp_E3E3E3_FILL0_wght400_GRAD0_opsz40.png");
        nut_thongke = createSideButton("Thống Kê",
                "/VPP/image/leaderboard_32dp_E3E3E3_FILL0_wght500_GRAD200_opsz40.png");
        nut_about = createSideButton("Liên Hệ", "/VPP/image/info_32dp_E3E3E3_FILL0_wght400_GRAD0_opsz40.png");


        // sự kiện chuyển panel
        nut_trangchu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dashboardPanel.refreshDashboard();
                chuyenPanel(dashboardPanel);
            }
        });
        nut_sp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chuyenPanel(new Sanphampanel());
            }
        });
        nut_pos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chuyenPanel(new Banhangpanel());
            }
        });
        nut_hdon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chuyenPanel(new Hoadonpanel());
            }
        });
        nut_thongke.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chuyenPanel(new Thongkepanel());
            }
        });
        nut_about.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chuyenPanel(new lienhepanel());
            }
        });

        // add nút vào side panel
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        sidePanel.add(spacer);
        sidePanel.add(nut_trangchu);
        sidePanel.add(nut_sp);
        sidePanel.add(nut_pos);
        sidePanel.add(nut_hdon);
        sidePanel.add(nut_thongke);
        sidePanel.add(nut_about);

        sidePanel.add(new JLabel());
        sidePanel.add(new JLabel());

        contentPanel = new JPanel(new BorderLayout());
        dashboardPanel = new Trangchupanel();
        contentPanel.add(dashboardPanel, BorderLayout.CENTER);

        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(sidePanel, BorderLayout.WEST);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createSideButton(String text, String iconPath) {
        JPanel btn = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        btn.setBackground(new Color(222, 120, 255));
        btn.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));

        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lbl.setForeground(Color.WHITE);

        try {
            lbl.setIcon(new ImageIcon(getClass().getResource(iconPath)));
        } catch (Exception e) {
            
        }

        btn.add(lbl);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setOpaque(true);
                btn.setBackground(new Color(192, 192, 192));
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(222, 120, 255));
            }
        });

        return btn;
    }

}