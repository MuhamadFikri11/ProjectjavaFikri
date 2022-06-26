package frame;

import helpers.ComboBoxItem;
import helpers.Koneksi;

import javax.swing.*;
import java.sql.*;

public class SepatuInputFrame extends JFrame {
    private JPanel mainPanel;
    private JTextField kodeTextField;
    private JTextField namaTextField;
    private JPanel buttonPanel;
    private JButton batalButton;
    private JButton simpanButton;
    private JComboBox brandComboBox;
    private JRadioButton sepatuFutsalRadioButton;
    private JRadioButton sepatuRunningRadioButton;
    private JRadioButton sepatuSekolahRadioButton;

    private ButtonGroup jenisButtonGrup;

    private int kode;
    public void setId(int kode){
        this.kode = kode;
    }

    public SepatuInputFrame() {
        batalButton.addActionListener(e -> {
            dispose();
        });

        simpanButton.addActionListener(e -> {
                    String nama = namaTextField.getText();
                    if (nama.equals("")) {
                        JOptionPane.showMessageDialog(null, "Isi Nama Sepatu", "Validasi kata kunci kosong", JOptionPane.WARNING_MESSAGE);
                        namaTextField.requestFocus();
                        return;
                    }
            ComboBoxItem item = (ComboBoxItem) brandComboBox.getSelectedItem();
            int kodeBrand = item.getValue();
            if (kodeBrand == 0) {
                JOptionPane.showMessageDialog(null, "Pilih Brand", "Validasi ComboBox", JOptionPane.WARNING_MESSAGE);
                brandComboBox.requestFocus();
                return;
            }
            String jenis = "";
            if(sepatuFutsalRadioButton.isSelected()){
                jenis = "Sepatu Futsal";
            } else if (sepatuRunningRadioButton.isSelected()) {
                jenis = "Sepatu Running";
            } else if (sepatuSekolahRadioButton.isSelected()) {
                jenis = "Sepatu Sekolah";
            }
            else{
                JOptionPane.showMessageDialog(null,"Pilih Jenis Sepatu","Validasi Kosong",JOptionPane.WARNING_MESSAGE);
                return;
            }
            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
                if (kode == 0) {
                    String cekSQL = "SELECT * FROM sepatu WHERE nama= ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Data sama sudah ada");
                    } else {
                        String insertSQL = "INSERT INTO sepatu (kode,nama,kode_brand,jenis) " + "VALUES (NULL, ?,?, ?)";
                        ps = c.prepareStatement(insertSQL);
                        ps.setString(1, nama);
                        ps.setInt(2,kodeBrand);
                        ps.setString(3,jenis);
                        ps.executeUpdate();
                        dispose();
                    }

                } else {
                    String cekSQL = "SELECT * FROM sepatu WHERE nama= ? AND kode != ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, kode);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Data sama sudah ada");
                    } else {
                        String updateSQL = "UPDATE sepatu SET nama= ?, kode_brand = ?, jenis = ?" + "WHERE kode= ?";
                        ps = c.prepareStatement(updateSQL);
                        ps.setString(1, nama);
                        ps.setInt(2, kodeBrand);
                        ps.setString(3, jenis);
                        ps.setInt(4, kode);
                        ps.executeUpdate();
                        dispose();
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        kustomisasiKomponen();
        init();
    }

    public void isiKomponen() {
        Connection c = Koneksi.getConnection();
        String findSQL = "SELECT * FROM sepatu WHERE kode= ?";
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, kode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                kodeTextField.setText(String.valueOf(rs.getInt("kode")));
                namaTextField.setText(rs.getString("nama"));
                int kodeBrand = rs.getInt("kode_brand");
                for (int i = 0; i < brandComboBox.getItemCount(); i++) {
                    brandComboBox.setSelectedIndex(i);
                    ComboBoxItem item = (ComboBoxItem) brandComboBox.getSelectedItem();
                    if ( kodeBrand ==item.getValue()){
                        break;
                    }
                }
                String jenis = rs.getString( "jenis");
                if(jenis !=null){
                    if(jenis.equals("Sepatu Futsal")){
                        sepatuFutsalRadioButton.setSelected(true);
                    }else if(jenis.equals("Sepatu Running")){
                        sepatuRunningRadioButton.setSelected(true);
                    } else if (jenis.equals("Sepatu Sekolah")) {
                        sepatuSekolahRadioButton.setSelected(true);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void kustomisasiKomponen(){
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM brandsepatu ORDER BY nama";
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            brandComboBox.addItem(new ComboBoxItem(0,"Pilih Brand"));
            while (rs.next()){
                brandComboBox.addItem(new ComboBoxItem(rs.getInt("kode_brand"),rs.getString("nama")));
            }
        } catch (SQLException ex){
            throw new RuntimeException(ex);
  }
        jenisButtonGrup = new ButtonGroup();
        jenisButtonGrup.add(sepatuFutsalRadioButton);
        jenisButtonGrup.add(sepatuRunningRadioButton);
        jenisButtonGrup.add(sepatuSekolahRadioButton);
}
    public void init(){
        setContentPane(mainPanel);
        setTitle("Input Sepatu");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
