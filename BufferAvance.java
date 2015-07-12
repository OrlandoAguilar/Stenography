import static java.lang.Math.*;
class BufferAvance{
      int ancho,alto, i[], j[], iAct,jAct ,c ,pix;
      float angle;
      
      BufferAvance(int ancho,int alto){
        this.ancho=ancho;
        this.alto=alto;
        i=new int[3];
        j=new int[3];
      }

      int nextColor(){
        angle++;
        return (int) abs(sin(angle)*8) %3;
      }
      int nextAvance(){
        return (int)abs((sin(++angle)*cos(++angle/10.0f)*20.0f) % 6)+1;
      }
      void next() throws LimitExcededException{
        c=nextColor();
        i[c]+=nextAvance();
        if (i[c]>=ancho){
          j[c]++;
          i[c]=i[c] % ancho;
          if (j[c]>=alto)
             throw new LimitExcededException();

        }
        iAct=i[c];
        jAct=j[c];
        pix=nextColor();
      }
      int getColor(){
        return c;
      }
      int getI(){
        return iAct;
      }
      int getJ(){
        return jAct;
      }
      int getPix(){
        return pix;
      }
}