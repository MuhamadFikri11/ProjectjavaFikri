package frame;

import helpers.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class BrandsepatuViewFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JScrollPane viewScrollPane;
    private JPanel buttonPanel;
    private JTextField cariTextField;
    private JButton cariButton;
    private JTable viewTable;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;
    public BrandsepatuViewFrame(){
            tutupButton.addActionListener(e -> {
                dispose();
            });

            batalButton.addActionListener(e -> {
                isiTable();
            });

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowActivated(WindowEvent e) {
                    isiTable();
                }
            });

            tambahButton.addActionListener(e -> {
                BrandsepatuViewFrame inputFrame = new BrandsepatuViewFrame();
                inputFrame.setVisible(true);
            });

            cariButton.addActionListener(e -> {
                Connection c = Koneksi.getConnection();
                String keyword = "%" + cariTextField.getText() + "%";
                String seachSQL = "SELECT * FROM brandsepatu WHERE nama like ?";
                try {
                    PreparedStatement ps = c.prepareStatement(seachSQL);
                    ps.setString(1, keyword);
                    ResultSet rs = ps.executeQuery();
                    DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                    dtm.setRowCount(0);
                    Object[] row = new Object[2];
                    while (rs.next()) {
                        row[0] = rs.getInt("kode_brand");
                        row[1] = rs.getString("nama");
                        dtm.addRow(row);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });

            hapusButton.addActionListener(e -> {
                int barisTerpilih = viewTable.getSelectedRow();
                if (barisTerpilih < 0) {
                    JOptionPane.showMessageDialog(null, "Pilih data dulu sanak");
                    return;
                }
                int pilihan = JOptionPane.showConfirmDialog(null,
                        "Yakin sanak mau hapus?",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION
                );
                if (pilihan == 0) {
                    TableModel tm = viewTable.getModel();
                    int id = Integer.parseInt(tm.getValueAt(barisTerpilih, 0).toString());
                    Connection c = Koneksi.getConnection();
                    String deleteSQL = "DELETE FROM brand WHERE kode_brand = ?";
                    try {
                        PreparedStatement ps = c.prepareStatement(deleteSQL);
                        ps.setInt(1, id);
                        ps.executeUpdate();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });



            isiTable();
            init();
        }

        public void init() {
            setContentPane(mainPanel);
            setTitle("Data Brandsepatu");
            pack();
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
        }

        public void isiTable() {
            Connection c = Koneksi.getConnection();
            String selectSQL = "SELECT * FROM brandsepatu";
            try {
                Statement s = c.createStatement();
                ResultSet rs = s.executeQuery(selectSQL);
                String header[] = {"kode_brand", "Nama Brand"};
                DefaultTableModel dtm = new DefaultTableModel(header, 0);
                viewTable.setModel(dtm);
                Object[] row = new Object[2];
                while (rs.next()) {
                    row[0] = rs.getInt("kode_brand");
                    row[1] = rs.getString("nama");
                    dtm.addRow(row);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
}