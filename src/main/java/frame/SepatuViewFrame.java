package frame;

import helpers.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class SepatuViewFrame extends JFrame{
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JPanel buttonPanel;
    private JScrollPane viewScrollPane;
    private JTextField cariTextField;
    private JButton cariButton;
    private JTable viewTable;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;

    public SepatuViewFrame(){
        tutupButton.addActionListener(e -> {
            dispose();
        });

        batalButton.addActionListener(e -> {
            isiTable();
        });
        ubahButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if (barisTerpilih < 0) {
                JOptionPane.showMessageDialog(null, "Pilih Data dulu sanak ai");
                return;
            }
            TableModel tm = viewTable.getModel();
            int kode = Integer.parseInt(tm.getValueAt(barisTerpilih, 0).toString());
            SepatuInputFrame inputFrame = new SepatuInputFrame();
            inputFrame.setId(kode);
            inputFrame.isiKomponen();
            inputFrame.setVisible(true);
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                isiTable();
            }
        });

        tambahButton.addActionListener(e -> {
            SepatuInputFrame inputFrame = new SepatuInputFrame();
            inputFrame.setVisible(true);
        });

        cariButton.addActionListener(e -> {
            Connection c = Koneksi.getConnection();
            String keyword = "%" + cariTextField.getText() + "%";
            String seachSQL = "SELECT * FROM sepatu WHERE nama like ?";
            try {
                PreparedStatement ps = c.prepareStatement(seachSQL);
                ps.setString(1, keyword);
                ResultSet rs = ps.executeQuery();
                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                Object[] row = new Object[2];
                while (rs.next()) {
                    row[0] = rs.getInt("kode");
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
                int kode = Integer.parseInt(tm.getValueAt(barisTerpilih, 0).toString());
                Connection c = Koneksi.getConnection();
                String deleteSQL = "DELETE FROM brand WHERE kode = ?";
                try {
                    PreparedStatement ps = c.prepareStatement(deleteSQL);
                    ps.setInt(1, kode);
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
        setTitle("Data Sepatu");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiTable() {
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM sepatu";
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            String header[] = {"kode", "Nama Sepatu"};
            DefaultTableModel dtm = new DefaultTableModel(header, 0);
            viewTable.setModel(dtm);
            Object[] row = new Object[2];
            while (rs.next()) {
                row[0] = rs.getInt("kode");
                row[1] = rs.getString("nama");
                dtm.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
