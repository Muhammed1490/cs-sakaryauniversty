package Asd;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Queue;

public class Asd {
    public static void main(String[] args) {
        System.out.println("Program basliyor. Kaynak yoneticisi ve planlayici baslatiliyor.");

        KaynakYoneticisi kaynakYoneticisi = new KaynakYoneticisi();
        Scheduler scheduler = new Scheduler(kaynakYoneticisi);

        System.out.println("Proseslerin okunmasi icin 'giris.txt' dosyasi aciliyor.");
        scheduler.readFile("giris.txt");

        //System.out.println("Proseslerin yonetimi ve planlanmasi basliyor.");
   
        

        System.out.println("Tum surecler tamamlandi. Planlayici sonlandiriliyor.");
    }
}
