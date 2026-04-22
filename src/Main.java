package src;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import src.entities.Student;
import src.exceptions.DataNotFoundException;
import src.exceptions.DuplicateDataException;
import src.repositories.StudentRepository;

public class Main extends JFrame {

    // Atribut GUI
    private JTextField txtNis, txtNama, txtAlamat;
    private JTable table;
    private DefaultTableModel model;
    
    // Objek Repository
    private StudentRepository repo = new StudentRepository();

    public Main() {
        setTitle("Aplikasi Data Siswa");
        setSize(750, 500); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        
        setLayout(new BorderLayout(15, 15));
        
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Untuk judul
        JLabel title = new JLabel("MANAJEMEN DATA SISWA", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // Untuk panel di kiri (Form & Tombol) 
        JPanel panelKiri = new JPanel(new BorderLayout(0, 20));
        panelKiri.setPreferredSize(new Dimension(280, 0)); 

        // FORM 
        JPanel form = new JPanel(new GridLayout(3, 2, 5, 10));

        txtNis = new JTextField();
        txtNama = new JTextField();
        txtAlamat = new JTextField();

        form.add(new JLabel("NIS:"));
        form.add(txtNis);
        form.add(new JLabel("Nama:"));
        form.add(txtNama);
        form.add(new JLabel("Alamat:"));
        form.add(txtAlamat);

        panelKiri.add(form, BorderLayout.NORTH);

        // Untuk Button
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        JButton btnTambah = new JButton("Tambah");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        btnPanel.add(btnTambah);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        panelKiri.add(btnPanel, BorderLayout.CENTER);
        
        // Masukkan panel kiri ke frame utama
        add(panelKiri, BorderLayout.WEST);

        // Table
        model = new DefaultTableModel(new String[]{"NIS", "Nama", "Alamat"}, 0);
        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadTable();

        // Event
        btnTambah.addActionListener(e -> tambahData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());
        btnClear.addActionListener(e -> clearForm());

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                txtNis.setText(model.getValueAt(row, 0).toString());
                txtNama.setText(model.getValueAt(row, 1).toString());
                txtAlamat.setText(model.getValueAt(row, 2).toString());
            }
        });
    }

    // ===== LOAD TABLE =====
    private void loadTable() {
        model.setRowCount(0);

        for (Student s : repo.findAll()) {
            model.addRow(new Object[]{
                s.getNis(),
                s.getNama(),
                s.getAlamat()
            });
        }
    }

    // ===== CREATE =====
    private void tambahData() {
        try {
            String nis = txtNis.getText();
            String nama = txtNama.getText();
            String alamat = txtAlamat.getText();

            if (nis.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "NIS wajib diisi!");
                txtNis.requestFocus();
                return;
            }

            if (nama.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama wajib diisi!");
                txtNama.requestFocus();
                return;
            }

            if (alamat.trim().isEmpty()) {
                alamat = "";
            }

            Student s = new Student(nis, nama, alamat);

            repo.create(s);
            loadTable();
            clearForm();

            JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");

        } catch (DuplicateDataException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            txtNis.requestFocus();
        }
    }

    // ===== UPDATE =====
    private void updateData() {
        try {
            String nis = txtNis.getText().trim();

            if (nis.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Masukkan NIS!");
                txtNis.requestFocus();
                return;
            }

            Student existing = repo.findById(nis);

            String namaBaru = txtNama.getText().trim();
            if (namaBaru.isEmpty() && existing != null) {
                namaBaru = existing.getNama();
            }

            String alamatBaru = txtAlamat.getText().trim();
            if (alamatBaru.isEmpty() && existing != null) {
                alamatBaru = existing.getAlamat();
            }

            Student newData = new Student(nis, namaBaru, alamatBaru);

            repo.update(nis, newData);
            loadTable();
            clearForm();

            JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");

        } catch (DataNotFoundException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            txtNis.requestFocus();
        }
    }

    // ===== DELETE =====
    private void deleteData() {
        try {
            String nis = txtNis.getText();

            if (nis.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih data dulu!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Yakin hapus data NIS " + nis + "?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                repo.delete(nis);
                loadTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
            }

        } catch (DataNotFoundException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void clearForm() { 
        txtNis.setText("");
        txtNama.setText("");
        txtAlamat.setText("");
    }

    public static void main(String[] args){
        new Main().setVisible(true);
    }
}
