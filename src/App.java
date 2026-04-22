package src;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import src.entities.Student;
import src.exceptions.DataNotFoundException;
import src.exceptions.DuplicateDataException;
import src.repositories.StudentRepository;

public class App {
    private JFrame frame;
    private JPanel panel1;
    private DefaultTableModel table1;
    private JTextField tfId;
    private JTextField tfNama;
    private JTextField tfAlamat;

    private JButton btnCreate;
    private JButton btnDelete;
    private JButton btnUpdate;
    private JButton btnSearch;
    private JButton btnClear;

    StudentRepository repo = new StudentRepository();

    public App(){
        frame = new JFrame("Data Siswa SMP");
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        // Tabel
        c.gridx = 0; c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 2;
        c.gridheight = 4;
        c.weightx = 0.6;
        c.weighty = 1;
        c.insets = new Insets(7,7,7,7);
        table1 = new DefaultTableModel(new String[]{"NIS", "Nama", "Alamat"}, 0);

        JTable view1 = new JTable(table1);
        JScrollPane scroll = new JScrollPane(view1);
        panel1.add(scroll, c);

        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.3;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;

        // ID
        c.gridx = 2;
        c.gridy = 0;
        panel1.add(new JLabel("ID"), c);

        c.gridx = 3;
        tfId = new JTextField(10);
        panel1.add(tfId, c);

        // NAMA
        c.gridx = 2;
        c.gridy = 1;
        panel1.add(new JLabel("Nama"), c);

        c.gridx = 3;
        tfNama = new JTextField(10);
        panel1.add(tfNama, c);

        // ALAMAT
        c.gridx = 2;
        c.gridy = 2;
        panel1.add(new JLabel("Alamat"), c);

        c.gridx = 3;
        tfAlamat = new JTextField(10);
        panel1.add(tfAlamat, c);


        // BUTTON BAR (BAWAH FULL)
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 4;   // full lebar
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 0;

        JPanel btnPanel = new JPanel(new GridLayout(1, 5, 5, 5));

        btnCreate = new JButton("Create");
        btnDelete = new JButton("Delete");
        btnUpdate = new JButton("Update");
        btnSearch = new JButton("Search");
        btnClear = new JButton("Clear");

        btnPanel.add(btnCreate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnSearch);
        btnPanel.add(btnClear);

        panel1.add(btnPanel, c);

        frame.add(panel1);

        // Load data ke tabel saat aplikasi dimulai
        loadTable();

        // ===== EVENT HANDLERS =====
        btnCreate.addActionListener(e -> tambahData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> searchData());

        // Mouse listener untuk mengisi form saat baris tabel diklik
        view1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view1.getSelectedRow();
                if (row != -1) {
                    tfId.setText(table1.getValueAt(row, 0).toString());
                    tfNama.setText(table1.getValueAt(row, 1).toString());
                    tfAlamat.setText(table1.getValueAt(row, 2).toString());
                }
            }
        });

        frame.setVisible(true);
    }

    private void loadTable() {
        table1.setRowCount(0);

        for (Student s : repo.findAll()) {
            table1.addRow(new Object[]{s.getNis(), s.getNama(), s.getAlamat()});
        }
    }

    private void tambahData() {
        try {
            String nis = tfId.getText();
            String nama = tfNama.getText();
            String alamat = tfAlamat.getText();

            // ID wajib diisi
            if (nis.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "NIS wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                tfId.requestFocus();
                return;
            }

            // Nama wajib diisi
            if (nama.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Nama wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                tfNama.requestFocus();
                return;
            }

            // Alamat opsional (boleh kosong)
            // Jika alamat kosong, bisa diisi dengan string kosong atau default value
            if (alamat.trim().isEmpty()) {
                alamat = "-"; // atau bisa juga alamat = "";
            }

            Student s = new Student(nis, nama, alamat);

            repo.create(s);
            loadTable();
            clearForm();
            JOptionPane.showMessageDialog(frame, "Data berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

        } catch (DuplicateDataException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            tfId.requestFocus();
        }
    }

    private void updateData() {
        try {
            String nis = tfId.getText().trim();

            // Validasi NIS tidak boleh kosong
            if (nis.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Masukkan NIS data yang akan diupdate!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                tfId.requestFocus();
                return;
            }

            // Ambil data lama (bisa null jika tidak ditemukan)
            Student existingStudent = repo.findById(nis);

            // Jika nama kosong, gunakan nama lama
            String namaBaru = tfNama.getText().trim();
            if (namaBaru.isEmpty() && existingStudent != null) {
                namaBaru = existingStudent.getNama();
            }

            // Jika alamat kosong, gunakan alamat lama
            String alamatBaru = tfAlamat.getText().trim();
            if (alamatBaru.isEmpty() && existingStudent != null) {
                alamatBaru = existingStudent.getAlamat().replace("\"", "");
            }

            Student newData = new Student(nis, namaBaru, alamatBaru);

            repo.update(nis, newData);
            loadTable();
            clearForm();
            JOptionPane.showMessageDialog(frame, "Data berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

        } catch (DataNotFoundException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            tfId.requestFocus();
        }
    }

    // ===== DELETE =====
    private void deleteData() {
        try {
            String nis = tfId.getText();
            if (nis.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Pilih data yang akan dihapus!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Yakin ingin menghapus data dengan NIS " + nis + "?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                repo.delete(nis);
                loadTable();
                clearForm();
                JOptionPane.showMessageDialog(frame, "Data berhasil dihapus!");
            }
        } catch (DataNotFoundException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage());
        }
    }

    // ===== SEARCH =====
    private void searchData() {
        String nis = tfId.getText();
        if (nis.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Masukkan NIS yang akan dicari!");
            return;
        }


        Student student = repo.findById(nis);
        if (student != null) {
            tfNama.setText(student.getNama());
            tfAlamat.setText(student.getAlamat());
            JOptionPane.showMessageDialog(frame, "Data ditemukan!");
        }

        clearForm();
    }

    // ===== CLEAR FORM =====
    private void clearForm() {
        tfId.setText("");
        tfNama.setText("");
        tfAlamat.setText("");
        tfId.requestFocus();
    }

}