class BufferFeistel{
      public static long str4ToLong(String c){
        long l=0;
        int t=c.length();
        t=t>4? 4 : t;
        for (int z=0;z<t;z++){
            l|=Long.rotateLeft((long)c.charAt(z),16*z);
        }
        return l;
      }
      public static String longToStr4(long l){
        StringBuilder res= new StringBuilder();
        for (int z=0;z<4;z++){
            char c=(char)(Long.rotateRight(l,16*z)&0xFFFF);
            res.append(c);
        }
        return res.toString();
      }

      public static String encripta(String c,long l){
        CifradoFeistel cf = new CifradoFeistel(l);
        StringBuilder res= new StringBuilder();
        for (int z=0,tam=c.length();z<tam;z+=4){
          long cifrar=str4ToLong(c.substring(z,(z+4<tam)? z+4 : tam));
          long cs=cf.cifraBloque(cifrar);
          res.append(longToStr4(cs));
        }
        return res.toString();
      }
      
      public static String desencripta(String c,long l){
        CifradoFeistel cf = new CifradoFeistel(l);
        StringBuilder res= new StringBuilder();
        for (int z=0,tam=c.length();z<tam;z+=4){
          long cifrar=str4ToLong(c.substring(z,(z+4<tam)? z+4 : tam));
          long cs=cf.descifraBloque(cifrar);
          res.append(longToStr4(cs));
        }
        return res.toString();
      }
}