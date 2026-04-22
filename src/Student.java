package src;

public class Student {
    private String nis;
    private String name;
    private String address;
    public Student(String nis, String name, String address) {
        this.nis = nis;
        this.name = name;
        this.address = address;
    }
    public String getNis() { return nis; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public void setNis(String nis) { this.nis = nis; }
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public String toString() {
        return nis + "," + name + "," + address;
    }
}
