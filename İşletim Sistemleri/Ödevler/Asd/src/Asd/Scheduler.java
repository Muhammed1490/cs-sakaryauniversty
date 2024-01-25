package Asd;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;

class Scheduler {
	Queue<Proses> realTimeQueue;
	Queue<Proses> userProcessQueue;
	LinkedList<Proses> beklemeKuyrugu;
	LinkedList<Proses> userThreeProcessQueue;
	LinkedList<Proses> userTwoProcessQueue;
	LinkedList<Proses> userOneProcessQueue;
	List<Proses> prosesListesi;
	FCFS fcfs = new FCFS();
	GBG gbg = new GBG();
	int kalanSure;

	int denemeSayisi = 0;
	int quantum;
	private final int QUANTUM = 1000; // Örnek kuantum süresi
	KaynakYoneticisi kaynakYoneticisi;

	public Scheduler(KaynakYoneticisi kaynakYoneticisi) {
		realTimeQueue = new LinkedList<>();
		userProcessQueue = new LinkedList<>();
		userOneProcessQueue = new LinkedList<>();
		userTwoProcessQueue = new LinkedList<>();
		userThreeProcessQueue = new LinkedList<>();
		beklemeKuyrugu = new LinkedList<>();
		prosesListesi = new ArrayList<>();

		this.kaynakYoneticisi = kaynakYoneticisi;
		quantum = 1;
	}

	public void readFile(String filename) {
		int prosesSirasi = 0;
		try {
			File file = new File(filename);
			Scanner scanner = new Scanner(file);
			System.out.println("Gercek Zamanli Prosesler FCFS Algoritmasina Gore Calismaya Basliyor.");

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] parts = line.split(", ");
				int varisZamani = Integer.parseInt(parts[0]);
				int oncelik = Integer.parseInt(parts[1]);
				int islemSuresi = Integer.parseInt(parts[2]);
				int bellek = Integer.parseInt(parts[3]);
				int yaziciSayisi = Integer.parseInt(parts[4]);
				int tarayiciSayisi = Integer.parseInt(parts[5]);
				int modemSayisi = Integer.parseInt(parts[6]);
				int cdSayisi = Integer.parseInt(parts[7]);

				Proses proses = new Proses(varisZamani, oncelik, islemSuresi, bellek, yaziciSayisi, tarayiciSayisi,
						modemSayisi, cdSayisi, prosesSirasi);

				// Gerçek Zamanlı Prosesleri ve Normal Kullanıcı Proseslerini ayır
				prosesListesi.add(proses);
				prosesSirasi++;

			}
			prosesKontrol(prosesListesi);
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("Dosya bulunamadı: " + e.getMessage());
		}
	}

	public void prosesKontrol(List<Proses> prosesListesi) {

		int gecenZaman = 0;

		while (!prosesListesi.isEmpty()) {
			List<Proses> mevcutProsesler = new ArrayList<>();
			for (Proses p : prosesListesi) {
				if (p.varisZamani <= gecenZaman) {
					
					mevcutProsesler.add(p);
				}
			}
			
			ArrayList<Proses> silinecekler = new ArrayList<>();
			for(Proses p : mevcutProsesler) {
				if(!kaynakYoneticisi.kaynaklariKontrolEt(p)) {
					System.out.println("Silindi");
					silinecekler.add(p);
				}
				else if(gecenZaman - p.varisZamani >20) {
					System.out.println("\u001B[31m"+"20 saniye zaman aşımı "+"\u001B[0m"+p.prosesSirasi);
					silinecekler.add(p);
				}
			}
			
			prosesListesi.removeAll(silinecekler);
			mevcutProsesler.removeAll(silinecekler);

			Proses secilenProses = null;
			for (Proses p : mevcutProsesler) {
				if (secilenProses == null || p.oncelik < secilenProses.oncelik) {
					secilenProses = p;
				}
			}

			if (secilenProses != null) {
				if (secilenProses.oncelik == 0) {
					if (kaynakYoneticisi.kaynaklariKontrolEt(secilenProses)) {
						System.out.println("----------------------------------------------------------");
						realTimeQueue.add(secilenProses);
						fcfs.executeTask(realTimeQueue,kaynakYoneticisi);
						gecenZaman += secilenProses.islemSuresi;
						prosesListesi.remove(secilenProses);
					} else {
						prosesListesi.remove(secilenProses);
					}
				} else {
					if (kaynakYoneticisi.kaynaklariKontrolEt(secilenProses)) {
						//System.out.println("----------------------------------------------------------");
						userProcessQueue.add(secilenProses);

						gbg.GBK(userProcessQueue,userOneProcessQueue,userTwoProcessQueue,userThreeProcessQueue,kalanSure,gecenZaman);
						gecenZaman++;
						//secilenProses.islemSuresi--;
						if (secilenProses.islemSuresi <= 0) {
							prosesListesi.remove(secilenProses);}
//						} else {
//							prosesListesi.remove(secilenProses);
//							//secilenProses.varisZamani+=gecenZaman;
//							//System.out.println(secilenProses.varisZamani);
//							prosesListesi.add(secilenProses);
//						}
					} else {
						prosesListesi.remove(secilenProses);
					}

				}
			} else {
				// Hiç proses işlenemezse
				gecenZaman++;

			}
		}
	}

//	public void FCFS() {
//		while (!realTimeQueue.isEmpty()) {
//
//			Proses p = realTimeQueue.poll();
//			System.out.println("Proses Sirasi: " + p.prosesSirasi);
//
//			kaynakYoneticisi.kaynaklariTahsisEt(p);
//			p.basladi();
//			islemiYurut(p);
//			p.sonlandi();
//			kaynakYoneticisi.kaynakBirak(p);
//
//		}
//	}

//	public void GBK() {
//
//		Proses p = userProcessQueue.poll();
//		switch (p.oncelik) {
//		case 1:
//			userOneProcessQueue.add(p);
//			processQueue(userOneProcessQueue, 2);
//			break;
//		case 2:
//			userTwoProcessQueue.add(p);
//			processQueue(userTwoProcessQueue, 3);
//			break;
//		case 3:
//			userThreeProcessQueue.add(p);
//			processRoundRobin(userThreeProcessQueue);
//			break;
//		}
//	}

	private void processQueue(Queue<Proses> queue, int sonrakiOncelik) {

		Proses p = queue.poll();
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


//	private void processRoundRobin(Queue<Proses> queue) {
//
//		Proses p = queue.poll();
//
//		System.out.println("Proses Sirasi: " + p.prosesSirasi + "   "  + "Oncelik Degeri :"+p.oncelik);
//
//		if (kaynakYoneticisi.kaynaklariKontrolEt(p)) {
//			kaynakYoneticisi.kaynaklariTahsisEt(p);
//			System.out.println("islem suresi:" + p.islemSuresi);
//			p.basladi();
//
//			try {
//				Thread.sleep(1000); 
//			} catch (InterruptedException e) {
//			}
//
//			p.setIslemSuresi(p.getIslemSuresi() - 1);
//
//			kalanSure = p.getIslemSuresi();
//
//			if (kalanSure <= 0) {
//				queue.poll();
//			} else {
//				p.askiyaAlindi();
//				queue.offer(p);
//			}
//
//			kaynakYoneticisi.kaynakBirak(p);
//
//		} else {
//
//			queue.poll();
//		}
//	}

	private void islemiYurut(Proses p) {

		try {
			for (int i = 0; i < p.islemSuresi; i++) {
				p.herSaniye();
				Thread.sleep(1000);

			}
		} catch (InterruptedException e) {
		}
	}

}
