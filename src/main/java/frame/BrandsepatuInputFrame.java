package frame;

import helpers.Koneksi;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BrandsepatuInputFrame  extends JFrame{
    private JPanel mainPanel;
    private JTextField namaTextField;
    private JTextField kodebrandTextField;
    private JPanel buttonPanel;
    private JButton batalButton;
    private JButton simpanButton;

    public BrandsepatuInputFrame() {
        batalButton.addActionListener(e -> {
            dispose();
        });

        simpanButton.addActionListener(e -> {
            String nama = namaTextField.getText();
            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
                String insertSQL = "INSERT INTO brandsepatu VALUES (NULL, ?)";
                ps = c.prepareStatement(insertSQL);
                ps.setString(1, nama);
                ps.executeUpdate();
                dispose();

            } catch (SQLException ex){
                throw new RuntimeException(ex);
            }
        });
        init();
    }

    public void init(){
        setContentPane(mainPanel);
        setTitle("Input Brandsepatu");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
