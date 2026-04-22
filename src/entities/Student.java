package entities;

import interfaces.DataRepository;
import java.io.Serializable;


public class Student implements Serializable, DataRepository<String> {
    private String nis, nama, alamat;

    public Student(String nis, String nama, String alamat) {
        this.nis = nis;
        this.nama = nama;
        this.alamat = alamat;
    }

    @Override
    public String getId() { return nis; }

    public String getNis() { return nis; }
    public String getNama() { return nama; }
    public String getAlamat() { return alamat; }
    public void setNis(String nis) { this.nis = nis; }
    public void setNama(String nama) { this.nama = nama; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    @Override
    public String toString() {
        return String.format("NIS: %s | Nama: %s | Alamat: %s", nis, nama, alamat);
    }
}
