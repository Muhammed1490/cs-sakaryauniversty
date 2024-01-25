package Asd;

import java.util.LinkedList;
import java.util.Queue;

public class RR {
	public void processRoundRobin(LinkedList<Proses> queue,KaynakYoneticisi kaynakYoneticisi,int kalanSure) {
		while(!queue.isEmpty()) {
		Proses p = queue.poll();


		if (kaynakYoneticisi.kaynaklariKontrolEt(p) && p.islemSuresi>0) {
			kaynakYoneticisi.kaynaklariTahsisEt(p);
			System.out.println("----------------------------------------------------------");
			System.out.println("Proses Sirasi: " + p.prosesSirasi + "   "  + "Oncelik Degeri :"+p.oncelik);

			//System.out.println("islem suresi:" + p.islemSuresi);
			p.basladi();
			islemiYurut(p);
			p.sonlandi();

			try {
				Thread.sleep(1000); 
			} catch (InterruptedException e) {
			}

			p.setIslemSuresi(p.getIslemSuresi() - 1);

			kalanSure = p.getIslemSuresi();
			
			if (kalanSure <= 0) {
				queue.poll();
			} else {
				p.askiyaAlindi();
				queue.offer(p);
				
			}
			System.out.println("Kalan Surem "+kalanSure);

			kaynakYoneticisi.kaynakBirak(p);

		} else {

			queue.poll();
		}
	}
	}
	
	public  void islemiYurut(Proses p) {

		try {
			for (int i = 0; i < 1; i++) {
				p.herSaniye();
				Thread.sleep(1000);

			}
		} catch (InterruptedException e) {
		}
	}
	
}
