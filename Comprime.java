import java.util.zip.*;
import java.io.*;

class Comprime{
  static  int tam;
  public static String comprime(String s)throws IOException{
    ByteArrayOutputStream os= new ByteArrayOutputStream(s.length());
    GZIPOutputStream out = new GZIPOutputStream(os);
    byte[] bt= s.getBytes();
    out.write(bt);
    out.finish();
    out.close();
    return byteArray2String(os.toByteArray());
  }

   public static String byteArray2String(byte[] b){
     tam=b.length;
    StringBuilder build= new StringBuilder(b.length/2+ (b.length%2));
     for (int z=0,p=0;z<b.length;z+=2,p++){
      int aux;
      if ((z+1)<b.length){
         aux=((int)(b[z]&0xFF)) | ((int)((b[z+1]&0xFF)<<8));
      }else
         aux=b[z];
      build.append((char) aux);
    }
    return build.toString();
  }

  public static byte[] String2byteArray(String a){
    byte [] r=new byte[tam];
    for (int z=0,p=0;p<a.length();z+=2,p++){
      int aux=a.charAt(p);
      r[z]=(byte)(aux&0xFF);
      if ((z+1)<tam)
         r[z+1]=(byte)(aux>>8);
    }
    return r;
  }
  

  public static String descomprime(String s)throws IOException{
    byte[] bt= String2byteArray(s);
    ByteArrayInputStream os= new ByteArrayInputStream(bt);
    GZIPInputStream in = new GZIPInputStream(os);
    StringBuilder build= new StringBuilder(bt.length);
    int arr;
    while((arr=in.read())!=-1)
       build.append((char) arr);
    in.close();
    return build.toString();
  }
  public static void main(String []args) throws IOException{
     String cad1="The class String includes methods for examining individual characters of the sequence, for comparing strings, for searching strings, for extracting substrings, and for creating a copy of a string with all characters translated to uppercase or to lowercase. Case mapping is based on the Unicode Standard version specified by the Character class. The Java language provides special support for the string concatenation operator ( + ), and for conversion of other objects to strings. String concatenation is implemented through the StringBuilder(or StringBuffer) class and its append method. String conversions are implemented through the method toString, defined by Object and inherited by all classes in Java. For additional information on string concatenation and conversion, see Gosling, Joy, and Steele, The Java Language Specification. Unless otherwise noted, passing a null argument to a constructor or method in this class will cause a NullPointerException to be thrown. A String represents a string in the UTF-16 format in which supplementary characters are represented by surrogate pairs (see the section Unicode Character Representations in the Character class for more information). Index values refer to char code units, so a supplementary character uses two positions in a String. ";
     String cad2=comprime(cad1);
     System.out.println(cad1.length()+" "+cad2.length());
     System.out.println(cad2);
     System.out.println(descomprime(cad2));

  }
}