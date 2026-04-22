package src.repositories;

import src.exceptions.DataNotFoundException;
import src.exceptions.DuplicateDataException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import src.entities.Student;
import src.interfaces.Repository;


public class StudentRepository implements Repository<Student, String> {
    private final static File dirFile = new File("./src/data");
    private final static File lokasiFile = new File("./src/data/students.csv");
    private ArrayList<Student> datas = new ArrayList<>();

    public StudentRepository() {
        // buat folder jika belum ada
        if (!dirFile.exists()) dirFile.mkdirs();
        // buat file jika belum ada
        if (!lokasiFile.exists()) {
            try {
                lokasiFile.createNewFile();
            } catch (IOException e) {
                System.out.println("Gagal membuat file CSV: " + e.getMessage());
            }
        }
        
        // ambil data dari file
        loadFromFile();
    }

    // load data dari file ke dalam variabel datas
    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(lokasiFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                // memastikan data tidak kurang
                if (data.length == 3) {
                    String alamat = data[2].replace("\"", "");
                    datas.add(new Student(data[0], data[1], alamat));
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file CSV: " + e.getMessage());
        }
    }

    private void saveToFile(List<Student> students) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(lokasiFile))) {
            for(int i=0; i<students.size(); i++){
                Student current = students.get(i);
                String line = String.format("%s,%s,%s", current.getId(), current.getNama(), current.getAlamat());
                writer.write(line);

                if(i != students.size()-1){
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal menyimpan ke file: " + e.getMessage());
        }
    }

    @Override
    public void create(Student student) throws DuplicateDataException {
        if (findById(student.getId()) != null){
            throw new DuplicateDataException("Gagal: Student dengan ID " + student.getId() + " sudah terdaftar!");
        }
        datas.add(student);
        saveToFile(datas);
    }

    @Override
    public List<Student> findAll() {
        return datas;
    }

    @Override
    public Student findById(String id) {
        return datas.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public void update(String nim, Student newData) throws DataNotFoundException {
        if (findById(nim) == null){
            throw new DataNotFoundException("Gagal: Student dengan ID " + nim + " tidak ditemukan.");
        }
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getId().equals(nim)) {
                datas.set(i, newData);
                break;
            }
        }
        saveToFile(datas);
    }

    @Override
    public void delete(String nim) throws DataNotFoundException {
        if (findById(nim) == null){
            throw new DataNotFoundException("Gagal: Student dengan ID " + nim + " tidak ditemukan.");
        }
        datas.removeIf(s -> s.getId().equals(nim));
        saveToFile(datas);
    }
}