package frame;

import helpers.Koneksi;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BrandsepatuInputFrame  extends JFrame{
    private JPanel mainPanel;
    private JTextField namaTextField;
    private JTextField kodebrandTextField;
    private JPanel buttonPanel;
    private JButton batalButton;
    private JButton simpanButton;
    private int kode_brand;
    public void setId(int kode_brand)
    {this.kode_brand = kode_brand;}

    public BrandsepatuInputFrame() {
        batalButton.addActionListener(e -> {
            dispose();
        });

        simpanButton.addActionListener(e ->{
            String nama = namaTextField.getText();
            if (nama.equals("")){
                JOptionPane.showMessageDialog(null,"Isi kata kunci pencarian","Validasi kata kunci kosong",JOptionPane.WARNING_MESSAGE);
                namaTextField.requestFocus();
                return;
            }
            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
                if (kode_brand == 0) {
                    String cekSQL = "SELECT * FROM brandsepatu WHERE nama= ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Data sama sudah ada");
                    } else {
                        String insertSQL = "INSERT INTO brandsepatu VALUES (NULL, ?)";
                        ps = c.prepareStatement(insertSQL);
                        ps.setString(1, nama);
                        ps.executeUpdate();
                        dispose();
                    }

                } else {
                    String cekSQL = "SELECT * FROM brandsepatu WHERE nama= ? AND kode_brand != ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, kode_brand);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Data sama sudah ada");
                    } else {
                        String updateSQL = "UPDATE brandsepatu SET nama= ? WHERE kode_brand= ?";
                        ps = c.prepareStatement(updateSQL);
                        ps.setString(1, nama);
                        ps.setInt(2, kode_brand);
                        ps.executeUpdate();
                        dispose();
                    }
                }
                } catch(SQLException ex){
                    throw new RuntimeException(ex);
                }
        });
        init();
    }
    public void isiKomponen(){
        Connection c = Koneksi.getConnection();
        String findSQL = "SELECT * FROM brandsepatu WHERE kode_brand= ?";
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1,kode_brand);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                kodebrandTextField.setText(String.valueOf(rs.getInt("kode_brand")));
                namaTextField.setText(rs.getString("nama"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void init(){
        setContentPane(mainPanel);
        setTitle("Input Brandsepatu");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
