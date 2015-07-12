import java.io.*;

class Encriptar{
  public static void setColorPos(Imagen.Color c,short pColor, short pByte,short valor){
    int v=((valor&1)<<pByte);
    int masc=~(1<<pByte);
    switch (pColor){
           case 0:
                c.setRed((short)((c.getRed()&masc)|v));
           break;
           case 1:
                c.setBlue((short)((c.getBlue()&masc|v)));
           break;
           case 2:
                c.setGreen((short)((c.getGreen()&masc|v)));
           break;
    }
  }
  
  
  public static int getColorPos(Imagen.Color c,short pColor, short pByte){
    switch (pColor){
           case 0:
                return ((c.getRed()>>pByte)&1);
           case 1:
                return((c.getBlue()>>pByte)&1);
           case 2:
                return((c.getGreen()>>pByte)&1);
    }
    return 0;
  }

  public static class BufferEncriptar{
    int val,caracter,tam;
    String texto;
    BufferEncriptar(String texto){
      this.texto=texto;
      val=0;
      caracter=0;
      tam=texto.length();
    }
    boolean hasNextBit(){
      if (caracter<tam)
         return true;
      return false;
    }
    int nextBit(){
      int v;
      v=((texto.charAt(caracter))>>val)&1;
      val++;
      if (val>15){
         caracter++;
         val=0;
      }
      return v;
    }
  }

  public static class BufferDesencriptar{
    int val;
    char caracter;
    StringBuilder texto;
    BufferDesencriptar(){
      val=0;
      texto= new StringBuilder();
      caracter=0;
    }
    public String toString(){
      return texto.toString();
    }
    public void limpia(){
      texto.delete(0,texto.length());
    }
    void addNextBit(int b){
      int v;
      caracter=(char)(caracter|(b<<val));
      val++;
      if (val>15){
        val=0;
        texto.append(caracter);
        caracter=0;
      }
    }
  }

  public static String encripta (String texto, Imagen imgTextoEncriptado,int clave) throws LimitExcededException, IOException{
     texto=Comprime.comprime(texto);
     String textoEncriptado=BufferFeistel.encripta(BufferFeistel.longToStr4(texto.length())+texto,clave);
     BufferEncriptar bf=new BufferEncriptar(textoEncriptado);
     int ancho,alto,i=0,j=0,c=0;
     ancho=imgTextoEncriptado.getAncho();
     alto=imgTextoEncriptado.getAlto();
     BufferAvance ba = new  BufferAvance(ancho,alto);
     while (bf.hasNextBit()){
       short s=(short)bf.nextBit();
       ba.next();
      // System.out.println(ba.getI()+"  "+ba.getJ()+"  "+(short)ba.getColor());
       setColorPos(imgTextoEncriptado.getColorPixel(ba.getI(),ba.getJ()),(short)ba.getColor(),(short)0,s);
     }

     return textoEncriptado;
  }

  public static String desencripta(Imagen imgTextoEncriptado,int clave) throws IOException{
     String des="";
     int tamOr=0;
     try{
     BufferDesencriptar bf=new BufferDesencriptar();
     int ancho,alto,i=0,j=0,c=0;
     ancho=imgTextoEncriptado.getAncho();
     alto=imgTextoEncriptado.getAlto();
     BufferAvance ba = new  BufferAvance(ancho,alto);
     for (i=1;i<65;i++){
       ba.next();
       int t=getColorPos(imgTextoEncriptado.getColorPixel(ba.getI(),ba.getJ()),(short)ba.getColor(),(short)0);
       bf.addNextBit(t);
     }
     long tam=BufferFeistel.str4ToLong(bf.toString());
     CifradoFeistel feis= new CifradoFeistel(clave);
     tam=feis.descifraBloque(tam);
     bf.limpia();
     tamOr=(int)tam;
     tam=(tam*16)+65;  //cantidad de bits por caracter más el long del tamaño
     for (;i<tam;i++){                     //corregir para hacer que abarque toda la imagen...
       ba.next();
       int t=getColorPos(imgTextoEncriptado.getColorPixel(ba.getI(),ba.getJ()),(short)ba.getColor(),(short)0);
       bf.addNextBit(t);
     }
     des=BufferFeistel.desencripta(bf.toString(),clave);
  }catch(LimitExcededException erd){
  }
     return Comprime.descomprime(des.substring(0,tamOr));
  }

}