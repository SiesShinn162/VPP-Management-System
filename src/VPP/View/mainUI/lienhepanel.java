package VPP.View.mainUI;


import javax.swing.*;
import java.awt.*;

public class lienhepanel extends javax.swing.JPanel {

    public lienhepanel() {
        this.setLayout(new BorderLayout(20, 20));
        initComponents();
    }

    private void initComponents() {
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Liên Hệ & Hỗ Trợ", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.add(title, BorderLayout.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridLayout(1, 2, 20, 0));
        content.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Gửi phản hồi nhanh"));

        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField subjectField = new JTextField();
        JTextArea messageArea = new JTextArea(6, 20);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        fieldsPanel.add(new JLabel("Họ tên:"));
        fieldsPanel.add(nameField);
        fieldsPanel.add(new JLabel("Email:"));
        fieldsPanel.add(emailField);
        fieldsPanel.add(new JLabel("Tiêu đề:"));
        fieldsPanel.add(subjectField);
        fieldsPanel.add(new JLabel("Nội dung:"));
        fieldsPanel.add(new JScrollPane(messageArea));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btngui = new JButton("Gửi");
        btngui.setBackground(new Color(222, 120, 255));
        btngui.setForeground(Color.WHITE);
        JButton clearButton = new JButton("Xoá Nhanh");   
        clearButton.setBackground(new Color(222, 120, 255));
        clearButton.setForeground(Color.WHITE);
        buttonsPanel.add(clearButton);
        buttonsPanel.add(btngui);

        clearButton.addActionListener(e -> {
            nameField.setText("");
            emailField.setText("");
            subjectField.setText("");
            messageArea.setText("");
        });

        btngui.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String subject = subjectField.getText().trim();
            String message = messageArea.getText().trim();

            if (name.isEmpty() || email.isEmpty() || subject.isEmpty() || message.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin trước khi gửi.");
                return;
            }
            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(this, "Email không hợp lệ.");
                return;
            }

            JOptionPane.showMessageDialog(this, "Cảm ơn bạn đã liên hệ! Chúng tôi sẽ phản hồi sớm.");
            nameField.setText("");
            emailField.setText("");
            subjectField.setText("");
            messageArea.setText("");
        });

        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        formPanel.add(buttonsPanel, BorderLayout.SOUTH);

        content.add(formPanel);
        add(content, BorderLayout.CENTER);
    }
}
