package Asd;

import java.util.Queue;

public class FCFS {

	
	 public void executeTask(Queue<Proses> kuyruk,KaynakYoneticisi kaynakYoneticisi) {
		while (!kuyruk.isEmpty()) {

			Proses p = kuyruk.poll();
			System.out.println("Proses Sirasi: " + p.prosesSirasi);

			kaynakYoneticisi.kaynaklariTahsisEt(p);
			p.basladi();
			islemiYurut(p);
			p.sonlandi();
			kaynakYoneticisi.kaynakBirak(p);

		}
	}
	
	public  void islemiYurut(Proses p) {

		try {
			for (int i = 0; i < p.islemSuresi; i++) {
				p.herSaniye();
				Thread.sleep(1000);

			}
		} catch (InterruptedException e) {
		}
	}
}
