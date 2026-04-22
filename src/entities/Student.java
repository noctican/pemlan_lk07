package src.entities;
import java.io.Serializable;
import src.interfaces.DataRepository;

public class Student implements Serializable, DataRepository<String> {
    private String nis, nama, alamat;

    // Constructor
    public Student(String nis, String nama, String alamat) {
        this.nis = nis;
        this.nama = nama;
        this.alamat = "\"" + alamat + "\"";
    }
    
    // Untuk mengambil ID berupa NIS(digunakan di repository)
    @Override
    public String getId() { 
        return nis; 
    }
    
    // Getter
    public String getNis() { 
        return nis; 
    }
    public String getNama() { 
        return nama; 
    }
    public String getAlamat() { 
        return alamat.replace("\"", "");
    }
    
    // Setter
    public void setNis(String nis) { 
        this.nis = nis; 
    }
    public void setNama(String nama) { 
        this.nama = nama; 
    }
    public void setAlamat(String alamat) { 
        this.alamat = alamat; 
    }

    // Untuk menampilkan data siswa dalam bentuk string yang mudah dibaca
    @Override
    public String toString() {
        return String.format("NIS: %s | Nama: %s | Alamat: %s", nis, nama, alamat);
    }
}
