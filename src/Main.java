package src;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class Main {
    // Atribut
    private static ArrayList<String> data = new ArrayList<String>();
    private final static File lokasiFile = new File("./src/data/dataSiswa.csv");
    private final static File dirFile = new File("./src/data");

    // Method
    public static void save() {
        if (!dirFile.exists()) dirFile.mkdir();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(lokasiFile))) {

            for (int i = 0; i < data.size(); i++) {
                String[] pisah = data.get(i).trim().split(";");
                bw.write(String.join(",", pisah));

                if (i != data.size() - 1) {
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void load() {
        if (!dirFile.exists()) dirFile.mkdir();
        if (!lokasiFile.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(lokasiFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] split = line.trim().split(",");
                data.add(String.join(";", split));
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args){
    }
}
