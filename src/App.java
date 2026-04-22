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
        frame.setVisible(true);
    }
}
