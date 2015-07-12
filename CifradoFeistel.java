import java.util.*;
import static java.lang.System.out;
class CifradoFeistel{

      GeneraClave gc;
      long val;
      static final long[] keysStatic=new long[]{ 0x245F987L,0x34FDA5L,0xA199890L,0xAAC4567L,0xFF673412L,0x45A678L, 0x99875AACCL};
      CifradoFeistel(GeneraClave gc,long val){
          this.gc=gc;
          this.val=val;
      }

      CifradoFeistel(long valu){
        this.val=valu;
          gc= new GeneraClave(){
            public long clave(long t,long k){
              return (((t%k)|0xACF00)&t ^ val);
            }
          };
      }
      
      long intercambia(long c){
        return Long.rotateRight(c,32);
      }

      long aplicaIzquierda(long v,long clave){
        return (v ^ (clave<<32L));
      }
      
      long codDerecha(long t, long k){
        return gc.clave(t&0xFFFFFFFFL,k);
      }

      long cifraBloque(long b){
        return this.cifraBloque(b,keysStatic);
      }
      
      long [] invierteArreglo(long[] arr){
        long [] t=arr.clone();
        for (int i=0,j=t.length-1;i<j;i++,j--){
            long aux=t[j];
            t[j]=t[i];
            t[i]=aux;
        }
        return t;
      }

      long descifraBloque(long b){
        long[] keys=invierteArreglo(keysStatic);
        return this.cifraBloque(b,keys);
      }

      long descifraBloque(long b,long[] keys){
        keys=invierteArreglo(keys);
        return this.cifraBloque(b,keys);
      }
      long cifraBloque(long b,long[] keys){
        if (keys==null)
           throw new IllegalArgumentException();
        for (long z:keys){
            long auxDer=codDerecha(b,z);
            long auxIzq=aplicaIzquierda(b&0xFFFFFFFF00000000L,auxDer);
            b=intercambia((b&0xFFFFFFFFL)|auxIzq);
        }
        b=intercambia(b);
        return b;
      }

      public static void main(String [] args){
      }
}