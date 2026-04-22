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
            Student s = new Student(
                nis,
                txtNama.getText(),
                txtAlamat.getText()
            );
    
            repo.create(s);
            loadTable();
            clearForm();
        } catch (DuplicateDataException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ===== UPDATE =====
    private void updateData() {
        try {
            String nis = txtNis.getText();
            Student newData = new Student(
                nis,
                txtNama.getText(),
                txtAlamat.getText()
            );
            repo.update(nis, newData);
            loadTable();
        } catch (DataNotFoundException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ===== DELETE =====
    private void deleteData() {
        try {
            String nis = txtNis.getText();
            repo.delete(nis);
            loadTable();
            clearForm();
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
