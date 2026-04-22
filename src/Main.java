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
    // Atribut dipake di StudentRepository
    // private static ArrayList<String> data = new ArrayList<String>();
    // private final static File lokasiFile = new File("./src/data/dataSiswa.csv");
    // private final static File dirFile = new File("./src/data");

    // Atribut GUI
    private JTextField txtNis, txtNama, txtAlamat;
    private JTable table;
    private DefaultTableModel model;
    //Objek Repository
    private StudentRepository repo = new StudentRepository();

    public Main() {
        setTitle("Aplikasi Data Siswa");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== JUDUL =====
        JLabel title = new JLabel("DATA SISWA", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // ===== FORM =====
        JPanel form = new JPanel(new GridLayout(3,2));

        txtNis = new JTextField();
        txtNama = new JTextField();
        txtAlamat = new JTextField();

        form.add(new JLabel("NIS"));
        form.add(txtNis);
        form.add(new JLabel("Nama"));
        form.add(txtNama);
        form.add(new JLabel("Alamat"));
        form.add(txtAlamat);

        add(form, BorderLayout.WEST);

        // ===== BUTTON =====
        JPanel btnPanel = new JPanel(new GridLayout(4,1));

        JButton btnTambah = new JButton("Tambah");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        btnPanel.add(btnTambah);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        add(btnPanel, BorderLayout.EAST);

        // ===== TABLE =====
        model = new DefaultTableModel(new String[]{"NIS","Nama","Alamat"}, 0);
        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadTable();

        // ===== EVENT =====
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

    private void clearForm() { //buat ngehapus field nya pas udah input atau pas klik clear
        txtNis.setText("");
        txtNama.setText("");
        txtAlamat.setText("");
    }

    // // Method
    // public static void save() {
    //     if (!dirFile.exists()) dirFile.mkdir();

    //     try (BufferedWriter bw = new BufferedWriter(new FileWriter(lokasiFile))) {

    //         for (int i = 0; i < data.size(); i++) {
    //             String[] pisah = data.get(i).trim().split(";");
    //             bw.write(String.join(",", pisah));

    //             if (i != data.size() - 1) {
    //                 bw.newLine();
    //             }
    //         }
    //     } catch (IOException e) {
    //         System.out.println(e.getMessage());
    //     }
    // }

    // public static void load() {
    //     if (!dirFile.exists()) dirFile.mkdir();
    //     if (!lokasiFile.exists()) return;

    //     try (BufferedReader br = new BufferedReader(new FileReader(lokasiFile))) {
    //         String line;

    //         while ((line = br.readLine()) != null) {
    //             String[] split = line.trim().split(",");
    //             data.add(String.join(";", split));
    //         }

    //     } catch (IOException e) {
    //         System.out.println(e.getMessage());
    //     }
    // }

    public static void main(String[] args){
        new Main().setVisible(true);
    }
}
