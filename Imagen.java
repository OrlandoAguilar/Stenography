
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;

public class Imagen implements Cloneable
{
    //atributos
    private Color bitmap[][];
    private int ancho;
    private int alto;

    public Object clone(){
           return  new Imagen (bitmap,ancho,alto);
    }

    //clase color
    public static class Color{
      short rojo, verde, azul;
      public static final Color BLACK, WHITE;
      static{
         BLACK= new Color(0);
         WHITE= new Color(0xFFFFFF);
      }
      public Color(int c){
        azul=(short)(c&0x0000FF);
        verde=(short)((c&0x00FF00)>>8);
        rojo=(short)((c&0xFF0000)>>16);
      }
      public Color(int rojo, int verde, int azul){
        this.azul=(short)azul;
        this.verde=(short)verde;
        this.rojo=(short)rojo;
      }
      public int getRGB( ){
           return rojo<<16|verde<<8|azul;
      }
      int getRed(){
        return rojo;
      }
      int getBlue(){
        return azul;
      }
      int getGreen(){
        return verde;
      }
      void setRed(short r){
        rojo=r;
      }
      void setBlue(short b){
        azul=b;
      }
      void setGreen(short g){
        verde=g;
      }
    }

    private Imagen(Color bitmap[][], int ancho, int alto )
    {
        this.ancho=ancho;
        this.alto=alto;
        this.bitmap = new Color[ alto ][ ancho ];
        for( int i = 0; i < alto; i++ )
            for( int j = 0; j < ancho; j++ )
            {
                this.bitmap[ i ][ j ] = new Color( bitmap[i][j].getRGB() );
            }
    }
    public Imagen( String archivo ) throws IOException
    {
        cargarImagen( archivo );
    }
    public Imagen( File archivo ) throws IOException
    {
        cargarImagen( archivo );
    }


    public Color getColorPixel( int x, int y )
    {
        if( x >= ancho || y >= alto )
            return null;
        else
            return bitmap[ y ][ x ];
    }
    
    public void setColorPixel( int x, int y ,Color c)
    {
            bitmap[ y ][ x ]=c;
    }
    
     public void setColorPixel( int x, int y ,int c)
    {
            bitmap[ y ][ x ]=new Color(c);
    }


    public int getAlto( )
    {
        return alto;
    }


    public int getAncho( )
    {
        return ancho;
    }


    private void cargarImagen( String nombreArchivo ) throws IOException
    {
        File archivo = new File( nombreArchivo );
        BufferedImage bmp;

        try
        {
            bmp = ImageIO.read( archivo );
        }
        catch( IOException e )
        {
            throw new IOException( "No se encuentra la imagen" );
        }

        ancho = bmp.getWidth( );
        alto = bmp.getHeight( );
        bitmap = new Color[ alto ][ ancho ];
        for( int i = 0; i < alto; i++ )
            for( int j = 0; j < ancho; j++ )
            {
                bitmap[ i ][ j ] = new Color( bmp.getRGB( j, i ) );
            }
    }
    
     private void cargarImagen( File archivo ) throws IOException
    {
        BufferedImage bmp;

        try
        {
            bmp = ImageIO.read( archivo );
        }
        catch( IOException e )
        {
            throw new IOException( "No se encuentra la imagen" );
        }

        ancho = bmp.getWidth( );
        alto = bmp.getHeight( );
        bitmap = new Color[ alto ][ ancho ];
        for( int i = 0; i < alto; i++ )
            for( int j = 0; j < ancho; j++ )
            {
                bitmap[ i ][ j ] = new Color( bmp.getRGB( j, i ) );
            }
    }

    /**
     * Retorna el mapa de bits como una BufferdImage
     * @return imagen como objeto BufferedImage
     */
    public BufferedImage getImageBuffer( )
    {
        BufferedImage imagen = new BufferedImage( ancho, alto, BufferedImage.TYPE_INT_RGB );
        for( int i = 0; i < alto; i++ )
            for( int j = 0; j < ancho; j++ )
            {
                imagen.setRGB( j, i, bitmap[ i ][ j ].getRGB( ) );
            }
        return imagen;
    }

    /**
     * Negativo de la imagen: El negativo se calcula cambiando cada componente RGB, tomando el valor absoluto de restarle al componente 255.
     */
    public void convertirNegativo( )
    {
        //Recorre la matriz y calcula los componentes del nuevo color
        for( int i = 0; i < alto; i++ )
            for( int j = 0; j < ancho; j++ )
            {
                Color colorViejo = bitmap[ i ][ j ];
                int nuevoR = Math.abs( colorViejo.getRed( ) - 255 );
                int nuevoG = Math.abs( colorViejo.getGreen( ) - 255 );
                int nuevoB = Math.abs( colorViejo.getBlue( ) - 255 );
                bitmap[ i ][ j ] = new Color( nuevoR, nuevoG, nuevoB );
            }
    }

    /**
     * Reflejar imagen: Consiste en intercambiar las columnas enteras de la imagen, de las finales a la iniciales
     */
    public void reflejarImagen( )
    {
        //Recorre la matriz hasta la mitad para intercambiar los colores de la columna
        for( int i = 0; i < alto; i++ )
            for( int j = 0; j < ancho / 2; j++ )
            {
                Color temporal = bitmap[ i ][ j ];
                bitmap[ i ][ j ] = bitmap[ i ][ ancho - 1 - j ];
                bitmap[ i ][ ancho - 1 - j ] = temporal;
            }
    }

    /**
     * Binarización: Consiste en llevar cada píxel de una imagen a negro o blanco. Para ello se requiere un umbral: si el color del píxel está por encima o igual se lleva a
     * blanco y si está por debajo se lleva a negro.
     * @param umbral Umbral para la binarización.
     */
    public void binarizarImagen( double umbral )
    {
        //Recorre la matriz de la imagen. Aquellos puntos con color menor o
        // igual al umbral los lleva a blanco y los mayores al negro.
        for( int i = 0; i < alto; i++ )
            for( int j = 0; j < ancho; j++ )
            {
                Color pixel = bitmap[ i ][ j ];
                int promedio = ( pixel.getBlue( ) + pixel.getGreen( ) + pixel.getRed( ) ) / 3;
                if( promedio < umbral )
                    bitmap[ i ][ j ] = Color.BLACK;
                else
                    bitmap[ i ][ j ] = Color.WHITE;
            }
    }

    /**
     * Pixelamiento: Consiste en dividir la imagen en pequeñas regiones de píxeles y para cada una de esas regiones cambiar el color de los píxeles al color promedio de dicha
     * región. En este ejemplo, la región se dimensiona con los divisores más pequeños del ancho y el alto de la imagen
     */
    public void pixelarImagen( )
    {
        //Los píxeles son divisores de las dimensiones de la imagen
        int anchoPixel = menorDivisorMayorAUno( ancho );
        int altoPixel = menorDivisorMayorAUno( alto );

        //Recorre la matriz por regiones para modificarla
        for( int x = 0; x < ancho; x += anchoPixel )
        {
            for( int y = 0; y < alto; y += altoPixel )
            {
                //Obtiene el color medio de la región
                Color colorPromedio = colorPromedio( x, y, x + anchoPixel - 1, y + altoPixel - 1 );
                //Cambia el color de la región al promedio
                cambiarColorRegion( colorPromedio, x, y, x + anchoPixel - 1, y + altoPixel - 1 );
            }
        }
    }

    /**
     * Escala de grises: Para ello promedia los componentes de cada píxel y crea un nuevo color donde cada componente (RGB) tiene el valor de dicho promedio
     */
    public void convertirAGrises( )
    {
        //Recorre la matriz de la imagen. para pasarla a gris.
        for( int i = 0; i < alto; i++ )
            for( int j = 0; j < ancho; j++ )
            {
                int rgbGris = ( bitmap[ i ][ j ].getBlue( ) + bitmap[ i ][ j ].getGreen( ) + bitmap[ i ][ j ].getRed( ) ) / 3;
                bitmap[ i ][ j ] = new Color( rgbGris, rgbGris, rgbGris );
            }
    }

    /**
     * Convolución: Opera la imagen con la matriz de convolución dada por el usuario
     * @param convolucion Matriz cuadrada de dimensión impar. convolucion != null.
     * @param dimension Dimensión de la matriz de convolución. dimension es válido para el contenido de la matriz.
     */
    public void aplicarOperadorConvolucion( double[][] convolucion, int dimension )
    {
        //Obtiene una copia de la imagen original, pero con un marco
        //de píxeles negros para operar fácilmente las esquinas de la imagen
        //con la matriz de convolución
        Color copiaBorde[][] = copiarConBorde( dimension / 2 );

        //Calcula la suma de los factores de convolución
        double sumaConvolucion = 0;
        for( int i = 0; i < dimension; i++ )
            for( int j = 0; j < dimension; j++ )
                sumaConvolucion += convolucion[ i ][ j ];

        //Recorre la matriz de píxeles para cambiar la imagen
        for( int i = 0; i < alto; i++ )
            for( int j = 0; j < ancho; j++ )
            {
                //Para cada píxel realiza el cálculo recorriendo la matriz de convolución
                double sumaRed = 0;
                double sumaGreen = 0;
                double sumaBlue = 0;

                //La división se hace en la mayoría de los casos (excepto en los bordes)
                //Restando sobre la suma de los factores de convolución
                double divisor = sumaConvolucion;

                //La suma se hace con los píxeles de la imagen original
                for( int k = -dimension / 2; k <= dimension / 2; k++ )
                    for( int l = -dimension / 2; l <= dimension / 2; l++ )
                    {
                        sumaRed += convolucion[ k + dimension / 2 ][ l + dimension / 2 ] * copiaBorde[ i + k + dimension / 2 ][ j + l + dimension / 2 ].getRed( );
                        sumaGreen += convolucion[ k + dimension / 2 ][ l + dimension / 2 ] * copiaBorde[ i + k + dimension / 2 ][ j + l + dimension / 2 ].getGreen( );
                        sumaBlue += convolucion[ k + dimension / 2 ][ l + dimension / 2 ] * copiaBorde[ i + k + dimension / 2 ][ j + l + dimension / 2 ].getBlue( );

                        //Si es un píxel del borde no cuenta para el divisor
                        if( i + l < 0 || i + l > alto || j + k < 0 || j + k > ancho )
                            divisor -= convolucion[ k + dimension / 2 ][ l + dimension / 2 ];
                    }

                if( divisor > 0 )
                {
                    sumaRed /= divisor;
                    sumaGreen /= divisor;
                    sumaBlue /= divisor;

                    if( sumaRed > 255 )
                        sumaRed = 255;
                    else if( sumaRed < 0 )
                        sumaRed = 0;

                    if( sumaGreen > 255 )
                        sumaGreen = 255;
                    else if( sumaGreen < 0 )
                        sumaGreen = 0;

                    if( sumaBlue > 255 )
                        sumaBlue = 255;
                    else if( sumaBlue < 0 )
                        sumaBlue = 0;

                    //Cambia el píxel en la imagen
                    bitmap[ i ][ j ] = new Color( ( int )sumaRed, ( int )sumaGreen, ( int )sumaBlue );
                }
                else
                {
                    if( sumaRed > 255 )
                        sumaRed = 255;
                    else if( sumaRed < 0 )
                        sumaRed = 0;

                    if( sumaGreen > 255 )
                        sumaGreen = 255;
                    else if( sumaGreen < 0 )
                        sumaGreen = 0;

                    if( sumaBlue > 255 )
                        sumaBlue = 255;
                    else if( sumaBlue < 0 )
                        sumaBlue = 0;

                    //Cambia el píxel en la imagen
                    bitmap[ i ][ j ] = new Color( ( int )sumaRed, ( int )sumaGreen, ( int )sumaBlue );
                }
            }
    }

    /**
     * Retorna el color promedio de la imagen
     * @return color promedio de toda la imagen
     */
    public Color colorPromedio( )
    {
        return colorPromedio( 0, 0, ancho - 1, alto - 1 );
    }

    /**
     * Busca el color promedio de la región de la imagen El color promedio es formado por los promedios de rojos, verdes y azules de cada píxel
     * @param xInicial Coordenada x del píxel de inicio.
     * @param yInicial Coordenada y del píxel de inicio.
     * @param xFinal Coordenada x del píxel final.
     * @param yFinal Coordenada y del píxel final.
     * @return Color promedio de la región.
     */
    private Color colorPromedio( int xInicial, int yInicial, int xFinal, int yFinal )
    {
        int valorMedioRojo = 0, valorMedioVerde = 0, valorMedioAzul = 0;
        int totalPixeles = ( xFinal - xInicial + 1 ) * ( yFinal - yInicial + 1 );

        //Recorre la región para promediar los componentes de los colores
        for( int i = yInicial; i <= yFinal; i++ )
            for( int j = xInicial; j <= xFinal; j++ )
            {
                valorMedioRojo += bitmap[ i ][ j ].getRed( );
                valorMedioVerde += bitmap[ i ][ j ].getGreen( );
                valorMedioAzul += bitmap[ i ][ j ].getBlue( );
            }

        valorMedioRojo /= totalPixeles;
        valorMedioVerde /= totalPixeles;
        valorMedioAzul /= totalPixeles;
        return new Color( valorMedioRojo, valorMedioVerde, valorMedioAzul );
    }

    /**
     * Calcula el menor divisor del número dado que sea mayor a 1.
     * @param numero al que se le buscará el divisor.
     * @return menor divisor mayor a uno del número
     */
    private int menorDivisorMayorAUno( int numero )
    {
        boolean encontrado = false;
        int divisor = 2;

        //Si el número es par el divisor menor es 2
        if( numero % divisor == 0 )
            return divisor;

        else
        {
            //Si el número es impar le busca un divisor impar
            divisor = 3;
            while( divisor < numero && !encontrado )
            {
                if( numero % divisor == 0 )
                    encontrado = true;
                else
                    divisor += 2;
            }
            return divisor;
        }
    }

    /**
     * Cambia el color de los píxeles de la región al dado como parámetro
     * @param color Color de la nueva región
     * @param xInicial Coordenada x del píxel de inicio
     * @param yInicial Coordenada y del píxel de inicio
     * @param xFinal Coordenada x del píxel final
     * @param yFinal Coordenada y del píxel final
     */
    private void cambiarColorRegion( Color color, int xInicial, int yInicial, int xFinal, int yFinal )
    {
        for( int i = yInicial; i <= yFinal && i < alto; i++ )
            for( int j = xInicial; j <= xFinal && j < ancho; j++ )
            {
                bitmap[ i ][ j ] = color;
            }
    }

    /**
     * Crea una copia de la imagen pero le adiciona un borde de píxeles de color negro, esto con el fin de poder operar con más facilidad la matriz de convolución con las
     * esquinas de la imagen, y sin alterar el resultado de los bordes
     * @param borde ancho en píxeles del borde (sobre un lado)
     * @return copia de la imagen (mapa de colores)
     */
    private Color[][] copiarConBorde( int borde )
    {
        //Crea una copia de la imagen original que incluye un marco de píxeles negros
        Color[][] copia = new Color[alto + 2 * borde][ancho + 2 * borde];

        //Recorre la imagen pero incluye el borde
        for( int i = 0; i < alto + borde * 2; i++ )
            for( int j = 0; j < ancho + borde * 2; j++ )
            {
                //Si el píxel es del borde, es de color negro
                if( i < borde || i >= alto + borde || j < borde || j >= ancho + borde )
                    copia[ i ][ j ] = Color.BLACK;
                else
                    //Si no lo toma de la imagen
                    copia[ i ][ j ] = new Color( bitmap[ i - borde ][ j - borde ].getRGB( ) );
            }
        return copia;
    }
    
   public void visualiza(String nombre){
      JDialog f=new JDialog();
      f.getContentPane().add(new JLabel(new ImageIcon(getImageBuffer())));
        f.pack();
        f.setVisible(true);
    }
    public void visualiza(JLabel comp){
        JDialog f=new JDialog();
        comp.setIcon(new ImageIcon(getImageBuffer()));
    }
    
    public void guardar(String nombre)throws IOException{
      File file = new File(nombre+".png");
      ImageIO.write(getImageBuffer(), "png", file);
    }
    
    public void guardar(File file) throws IOException{
      ImageIO.write(getImageBuffer(), "png", file);
    }

}