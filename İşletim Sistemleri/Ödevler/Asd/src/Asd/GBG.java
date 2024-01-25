package Asd;

import java.util.LinkedList;
import java.util.Queue;

public class GBG {
	KaynakYoneticisi kaynakYoneticisi = new KaynakYoneticisi();
	public void GBK(Queue<Proses> userProcessQueue,LinkedList<Proses> userOneProcessQueue,LinkedList<Proses> userTwoProcessQueue,LinkedList<Proses> userThreeProcessQueue,int kalanSure,int gecenZaman) {
		RR r=new RR();
		Proses p = userProcessQueue.poll();
		switch (p.oncelik) {
		case 1:
			userOneProcessQueue.addFirst(p);
			processQueue(userOneProcessQueue, 2,userTwoProcessQueue,userThreeProcessQueue,gecenZaman);
			break;
		case 2:
			userTwoProcessQueue.addFirst(p);
			processQueue(userTwoProcessQueue, 3,userTwoProcessQueue,userThreeProcessQueue,gecenZaman);
			break;
		case 3:
			userThreeProcessQueue.addFirst(p);
			r.processRoundRobin(userThreeProcessQueue,kaynakYoneticisi,kalanSure);
			break;
		}
	}
	
	public void processQueue(Queue<Proses> queue, int sonrakiOncelik,Queue<Proses> userTwoProcessQueue,Queue<Proses> userThreeProcessQueue,int gecenZaman) {
		Proses p = queue.poll();
		
		if(gecenZaman-p.varisZamani >20) {
			System.out.println("Zaman Aşımı (20 Saniye) "+p.prosesSirasi);
			
		}
		System.out.println("----------------------------------------------------------");

		System.out.println("Proses Sirasi: " + p.prosesSirasi + "   "  + "Oncelik Degeri :"+p.oncelik);

		p.basladi();
		kaynakYoneticisi.kaynaklariTahsisEt(p);
		kaynakYoneticisi.kaynakBirak(p);
		p.herSaniye();
		p.sonlandi();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		p.islemSuresi--;
		if (p.islemSuresi >= 2) {
			p.oncelik = sonrakiOncelik;
			switch (sonrakiOncelik) {
			case 2:
				userTwoProcessQueue.add(p);
				break;
			case 3:
				userThreeProcessQueue.add(p);
				break;
			}
		}

	}
}
