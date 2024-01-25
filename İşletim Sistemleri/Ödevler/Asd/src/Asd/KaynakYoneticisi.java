package Asd;

class KaynakYoneticisi {
    int yaziciSayisi = 2;
    int tarayiciSayisi = 1;
    int modemSayisi = 1;
    int cdSayisi = 2;
    int kullaniciIsleriBellek = 960; // MB cinsinden
    int gercekZamanliBellek = 64;
    
    // Kaynakların kullanılıp kullanılmadığını tutan boolean değişkenler
    boolean[] yazicilar;
    boolean tarayici;
    boolean modem;
    boolean[] cds;
    int kullanilanBellek;
    
    public KaynakYoneticisi() {
        yazicilar = new boolean[yaziciSayisi];
        cds = new boolean[cdSayisi];
        tarayici = false;
        modem = false;
        kullanilanBellek = 0;
    }


    public synchronized void kaynakBirak(Proses p) {
    	if(p.oncelik==0)
    	{
    			gercekZamanliBellek=64;
    	        yaziciSayisi += p.yaziciSayisi;
    	        tarayiciSayisi += p.tarayiciSayisi;
    	        modemSayisi += p.modemSayisi;
    	        cdSayisi += p.cdSayisi;
    	}
    	else
    	{
    		kullaniciIsleriBellek=1024;
	        yaziciSayisi += p.yaziciSayisi;
	        tarayiciSayisi += p.tarayiciSayisi;
	        modemSayisi += p.modemSayisi;
	        cdSayisi += p.cdSayisi;
    	}
       
    }
    
    public synchronized boolean kaynaklariKontrolEt(Proses p) {
    	//BU KISIM GERÇEK ZAMANLI PROSESLER İÇİN 
    	if(p.oncelik == 0 )
    	{
    		if((this.gercekZamanliBellek - p.bellek) < 0 || p.yaziciSayisi >0 ||
  	               p.tarayiciSayisi > 0 ||
  	               p.modemSayisi > 0||
  	               p.cdSayisi > 0 ||
  	               (this.kullaniciIsleriBellek - p.bellek) < 0)
 	        {
 	        	 System.out.println("\u001B[31m"+"HATA!! Gerçek Zamanlı Proses Çok Fazla Kaynak Talep Ediyor - Proses Silindi: "+"\u001B[0m"+p.prosesSirasi);
 	        	 return false;
 	        }
 	        else
 	        	   return true;
    	}
    	//BU KISIM KULLANICI PROSESLERİ İÇİN
    	else
    	{
    		if(p.yaziciSayisi > this.yaziciSayisi ||
 	               p.tarayiciSayisi > this.tarayiciSayisi ||
 	               p.modemSayisi > this.modemSayisi ||
 	               p.cdSayisi > this.cdSayisi ||
 	               (this.kullaniciIsleriBellek - p.bellek) < 0)
 	        {
 	        	System.out.println("\u001B[31m"+"HATA!! Proses Çok Fazla Kaynak Talep Ediyor - Proses Silindi: "+"\u001B[0m"+ p.prosesSirasi);       	   
 	        	 return false;
 	        }
 	        else
 	        	 return p.yaziciSayisi <= this.yaziciSayisi &&
	               p.tarayiciSayisi <= this.tarayiciSayisi &&
	               p.modemSayisi <= this.modemSayisi &&
	               p.cdSayisi <= this.cdSayisi &&
	               (this.kullaniciIsleriBellek - p.bellek) >= 0;
    	}
      
        
    }
    int kalanBellek=0;
    public synchronized void kaynaklariTahsisEt(Proses p) {
    	if(p.oncelik==0)
    	{
    		   if ((this.gercekZamanliBellek - p.bellek) >= 0)
    		   {
    	    	        kalanBellek=0;
    	    	        kalanBellek = this.gercekZamanliBellek - p.bellek;
    	    	       System.out.println("Yazici Sayisi: " + p.yaziciSayisi + " " + "Tarayici Sayisi: " + p.tarayiciSayisi + " " + "Modem Sayisi: " + p.modemSayisi + " " + "CD Sayisi: " + p.cdSayisi + " " + "Kullanilan Bellek: " + p.bellek + " " +"Kalan Bellek: " + kalanBellek );
    	    	       
    	    	       }
    	    	   else
    	    	   {
    	    		   System.out.println("Yeterli Kaynak Yok Kaynakları Yeniden Tahsis Et");
    	    	   }
    	}
    	else
    	{
    		if (p.yaziciSayisi <= this.yaziciSayisi &&
    	    	       p.tarayiciSayisi <= this.tarayiciSayisi &&
    	    	       p.modemSayisi <= this.modemSayisi &&
    	    	       p.cdSayisi <= this.cdSayisi &&
    	    	       p.bellek < kullaniciIsleriBellek )
    			
    		{  
    	    	        kalanBellek=0;
    	    	        kalanBellek = this.kullaniciIsleriBellek - p.bellek;
    	    	        
    	    	        yaziciSayisi -= p.yaziciSayisi;
    	    	        tarayiciSayisi -= p.tarayiciSayisi;
    	    	        modemSayisi -= p.modemSayisi;
    	    	        cdSayisi -= p.cdSayisi;
    	    	        kullaniciIsleriBellek-=p.bellek;
    	    	        
    	    	       System.out.println("Yazici Sayisi: " + p.yaziciSayisi + " " + "Tarayici Sayisi: " + p.tarayiciSayisi + " " + "Modem Sayisi: " + p.modemSayisi + " " + "CD Sayisi: " + p.cdSayisi + " " + "Kullanilan Bellek: " + p.bellek + " " +"Kalan Bellek: " + kalanBellek );
    	    	      
    	    	       }
    	    	   else
    	    	   {
    	    		   System.out.println("Yeterli Kaynak Yok Kaynakları Yeniden Tahsis Et");
    	    	   }
    	}
    		   
    }
    
}