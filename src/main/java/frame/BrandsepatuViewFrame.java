package frame;

import helpers.JasperDataSourceBuilder;
import helpers.Koneksi;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

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
        ubahButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if (barisTerpilih < 0) {
                JOptionPane.showMessageDialog(null, "Pilih Data dulu sanak ai");
                return;
            }
            TableModel tm = viewTable.getModel();
            int kode_brand = Integer.parseInt(tm.getValueAt(barisTerpilih, 0).toString());
            BrandsepatuInputFrame inputFrame = new BrandsepatuInputFrame();
            inputFrame.setId(kode_brand);
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
                BrandsepatuInputFrame inputFrame = new BrandsepatuInputFrame();
                inputFrame.setVisible(true);
            });
            cetakButton.addActionListener(e-> {
                Connection c = Koneksi.getConnection();
                String selectSQL = "SELECT * FROM brandsepatu ";
                Object[][] row;
                try {
                    Statement s = c.createStatement(
                            ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
                    ResultSet rs = s.executeQuery(selectSQL);
                    rs.last();
                    int jumlah = rs.getRow();
                    row = new Object[jumlah][2];
                    int i = 0;
                    rs.beforeFirst();
                    while (rs.next()) {
                        row[i][0] = rs.getInt("kode_brand");
                        row[i][1] = rs.getString("nama");
                        i++;
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    JasperReport jasperReport = JasperCompileManager.compileReport("/Users/mfikr/IdeaProjects/ProjectjavaFikri/src/main/resources/databrandsepatu_report.jrxml");
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, new JasperDataSourceBuilder(row));
                    JasperViewer viewer = new JasperViewer(jasperPrint, false);
                    viewer.setVisible(true);
                } catch (JRException ex) {
                    throw new RuntimeException(ex);
                }
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
                    int kode_barang = Integer.parseInt(tm.getValueAt(barisTerpilih, 0).toString());
                    Connection c = Koneksi.getConnection();
                    String deleteSQL = "DELETE FROM brand WHERE kode_brand = ?";
                    try {
                        PreparedStatement ps = c.prepareStatement(deleteSQL);
                        ps.setInt(1, kode_barang);
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
            setTitle("Data Brand Sepatu");
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
