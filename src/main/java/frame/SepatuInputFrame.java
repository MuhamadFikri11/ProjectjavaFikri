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

    private JButton ubahButton;

    private  JButton cariButton;
    private JComboBox brandComboBox;
    private JRadioButton sepatuFutsalRadioButton;
    private JRadioButton sepatuRunningRadioButton;
    private JRadioButton sepatuSekolahRadioButton;
    private JTextField hargaTextField;
    private JRadioButton uk39RadioButton;
    private JRadioButton uk40RadioButton;
    private JRadioButton uk41RadioButton;
    private JRadioButton uk42RadioButton;
    private JRadioButton putihRadioButton;
    private JRadioButton hitamRadioButton;
    private JRadioButton merahRadioButton;
    private JRadioButton biruRadioButton;

    private ButtonGroup jenisButtonGrup;
    private ButtonGroup ukuranButtonGrup;
    private ButtonGroup warnaButtonGrup;

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
                    if (sepatuFutsalRadioButton.isSelected()) {
                        jenis = "Sepatu Futsal";
                    } else if (sepatuRunningRadioButton.isSelected()) {
                        jenis = "Sepatu Running";
                    } else if (sepatuSekolahRadioButton.isSelected()) {
                        jenis = "Sepatu Sekolah";
                    } else {
                        JOptionPane.showMessageDialog(null, "Pilih Jenis Sepatu", "Validasi Kosong", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
            String ukuran = "";
            if(uk39RadioButton.isSelected()){
                ukuran = "39";
            } else if (uk40RadioButton.isSelected()) {
                ukuran = "40";
            } else if (uk41RadioButton.isSelected()) {
                ukuran = "41";
            } else if (uk42RadioButton.isSelected()) {
                ukuran = "42";
            } else{
                JOptionPane.showMessageDialog(null,"Pilih Ukuran Sepatu","Validasi Kosong",JOptionPane.WARNING_MESSAGE);
                return;
                }
            String warna = "";
            if(putihRadioButton.isSelected()){
                warna = "Putih";
            } else if (hitamRadioButton.isSelected()) {
                warna = "Hitam";
            } else if (merahRadioButton.isSelected()) {
                warna = "Merah";
            } else if (biruRadioButton.isSelected()) {
                warna = "Biru";

            } else{
                JOptionPane.showMessageDialog(null,"Pilih Warna Sepatu","Validasi Kosong",JOptionPane.WARNING_MESSAGE);
                return;
            }
            String harga = hargaTextField.getText();
            if (harga.equals("")) {
                JOptionPane.showMessageDialog(null, "Isi Harga Sepatu", "Validasi kata kunci kosong", JOptionPane.WARNING_MESSAGE);
                hargaTextField.requestFocus();
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
                        String insertSQL = "INSERT INTO sepatu (kode, nama, kode_brand, jenis, ukuran, warna, harga) " +
                                "VALUES (NULL,?,?,?,?,?,?)";
                        ps = c.prepareStatement(insertSQL);
                        ps.setString(1, nama);
                        ps.setInt(2,kodeBrand);
                        ps.setString(3,jenis);
                        ps.setString(4,ukuran);
                        ps.setString(5,warna);
                        ps.setString(6,harga);
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
                        String updateSQL = "UPDATE sepatu SET nama= ?, kode_brand = ?, jenis = ?, ukuran = ?, warna = ?, harga = ?" + "WHERE kode= ?";
                        ps = c.prepareStatement(updateSQL);
                        ps.setString(1, nama);
                        ps.setInt(2, kodeBrand);
                        ps.setString(3, jenis);
                        ps.setString(4, ukuran);
                        ps.setString(5, warna);
                        ps.setString(6, harga);

                        ps.setInt(7, kode);
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
                hargaTextField.setText(rs.getString("harga"));
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
                    String ukuran = rs.getString( "ukuran");
                    if(ukuran !=null) {
                        if (ukuran.equals("39")) {
                            uk39RadioButton.setSelected(true);
                        } else if (ukuran.equals("40")) {
                            uk40RadioButton.setSelected(true);
                        } else if (ukuran.equals("41")) {
                            uk41RadioButton.setSelected(true);
                        } else if (ukuran.equals("42")) {
                            uk42RadioButton.setSelected(true);
                        }
                        String warna = rs.getString( "warna");
                        if(warna !=null) {
                            if (warna.equals("Putih")) {
                                putihRadioButton.setSelected(true);
                            } else if (warna.equals("Hitam")) {
                                hitamRadioButton.setSelected(true);
                            } else if (warna.equals("Merah")) {
                                merahRadioButton.setSelected(true);
                            } else if (warna.equals("Biru")) {
                                biruRadioButton.setSelected(true);
                            }
                        }
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

        ukuranButtonGrup = new ButtonGroup();
        ukuranButtonGrup.add(uk39RadioButton);
        ukuranButtonGrup.add(uk40RadioButton);
        ukuranButtonGrup.add(uk41RadioButton);
        ukuranButtonGrup.add(uk42RadioButton);

        warnaButtonGrup = new ButtonGroup();
        warnaButtonGrup.add(putihRadioButton);
        warnaButtonGrup.add(hitamRadioButton);
        warnaButtonGrup.add(merahRadioButton);
        warnaButtonGrup.add(biruRadioButton);
}
    public void init(){
        setContentPane(mainPanel);
        setTitle("Input Sepatu");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
