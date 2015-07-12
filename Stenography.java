import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.filechooser.*;
final public class Stenography extends JFrame{
  private static JLabel ventana1;
  private static JLabel ventana2;
  private static JButton cargar;
  private static JButton encriptar;
  private static JButton desencriptar;
  private static JButton guardar;
  private static JTextArea aEncriptar;
  private static JTextArea claveEncripta,claveDesencripta;
  private static JTextArea encriptado,desencriptado;
  private static JScrollPane panelIzquierdo;
  private static JScrollPane panelDerecho;
  private static Imagen imgOriginal;
  private static String textoEncriptado;

  private static JButton cargarEncriptado;
  private static JTextArea jtDesencriptado;
  private static JLabel ventana3;
  private static JScrollPane panelUnico;
  private static Imagen imgEncriptada;
  
  private static Imagen imgTextoEncriptado;

  static Stenography  ventana;

  static{
    ventana=new Stenography();
    Container contenedor=ventana.getContentPane();
    contenedor.setLayout (new BorderLayout());
    JTabbedPane panelPrincipal = new JTabbedPane();
    contenedor.add(panelPrincipal,BorderLayout.CENTER);
    JPanel centro=new JPanel(new GridBagLayout());
    panelPrincipal.addTab("Encrypt",centro);
    GridBagConstraints c = new GridBagConstraints();
    ventana1= new JLabel();
    ventana2= new JLabel();
    panelIzquierdo =new JScrollPane(ventana1);
    panelIzquierdo.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    panelIzquierdo.getViewport().setBackground(new Color(0xDDDDDD));
    panelDerecho =new JScrollPane(ventana2);
    panelDerecho.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    panelDerecho.getViewport().setBackground(new Color(0xDDDDDD));
    c.fill=c.NONE;
    c.gridy = 0;
    c.gridx = 1;
    centro.add(new JLabel("              Original Image            "),c);
    c.gridx = 5;
    centro.add(new JLabel("Image with ciphertext"),c);
    c.fill=c.BOTH;
    c.weightx =1;
    c.weighty =1;
    c.gridx = 1;
    c.gridy = 1;
    centro.add(panelIzquierdo,c);
    c.fill=c.BOTH;
    c.weightx =1;
    c.weighty =1;
    c.gridx = 5;
    centro.add(panelDerecho,c);
    c.fill=c.NONE;
    c.weightx =0;
    c.weighty =0;
    c.gridx = 2;

    guardar=new JButton("Save png");
    guardar.setEnabled(false);
    c.fill=c.NONE;
    c.weightx =0;
    c.weighty =0;
    c.gridx = 5;
    c.gridy = 2;
    centro.add(guardar,c);
    cargar=new JButton("Load png");
    c.fill=c.NONE;
    c.weightx =0;
    c.weighty =0;
    c.gridx = 1;
    c.gridy = 2;
    centro.add(cargar,c);
    c.gridx = 2;
    c.gridy = 1;
    centro.add(Box.createRigidArea(new Dimension(30,30) ),c);
    c.gridx = 4;
    centro.add(Box.createRigidArea(new Dimension(30,30) ),c);
    c.gridx = 6;
    centro.add(Box.createRigidArea(new Dimension(30,30) ),c);
    c.gridy = 2;
    centro.add(Box.createRigidArea(new Dimension(30,30) ),c);
    c.gridy = 0;
    c.gridx = 0;
    centro.add(Box.createRigidArea(new Dimension(30,30) ),c);

    JPanel lineaBaja=new JPanel(new GridBagLayout());
    c.fill = GridBagConstraints.HORIZONTAL;
    aEncriptar=new JTextArea("Encrypt text");
    aEncriptar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    c.fill=c.BOTH;
    c.weightx =1;
    c.weighty =1;
    c.gridx = 0;
    c.gridy = 1;
    lineaBaja.add(new JScrollPane(aEncriptar),c);

    
    JPanel pp1= new JPanel(new GridLayout(2,1));
    claveEncripta=new JTextArea("");
    claveEncripta.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    c.fill=c.BOTH;
    c.weightx =1;
    c.weighty =0.1;
    c.gridx = 0;
    c.gridy = 2;
    pp1.add(new JLabel("Key: "));
    pp1.add(new JScrollPane(claveEncripta));
    lineaBaja.add(pp1,c);

    encriptar=new JButton("Encrypt text");
    encriptar.setEnabled(false);
    c.weighty =1;
    c.fill=c.NONE;
    c.gridx = 0;
    c.gridy = 3;
    lineaBaja.add(encriptar,c);
    encriptado=new JTextArea();
    encriptado.setEditable(false);
    encriptado.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    c.fill=c.BOTH;
    c.gridx = 0;
    c.gridy = 4;
    lineaBaja.add(new JScrollPane(encriptado),c);
    c.weightx =1;
    c.weighty =0;
    c.gridx = 3;
    c.gridy = 1;
    c.fill=c.BOTH;
    centro.add(lineaBaja,c);

    JPanel centroDesencriptado=new JPanel(new BorderLayout());
    panelPrincipal.addTab("Decrypt",centroDesencriptado);

    JPanel lineaBajaDesencriptado=new JPanel(new GridBagLayout());
    c.fill=c.NONE;
    c.weightx =0;
    c.weighty =0;
    c.gridx = 0;
    c.gridy = 9;
    lineaBajaDesencriptado.add(Box.createRigidArea(new Dimension(30,30) ),c);
    c.gridx = 2;
    c.gridy = 2;
    lineaBajaDesencriptado.add(Box.createRigidArea(new Dimension(30,30) ),c);
    c.gridx = 1;
    c.gridy = 0;
    lineaBajaDesencriptado.add(new JLabel("Write the key to decrypt and then load the image"),c);

    cargarEncriptado=new JButton("Load png");
    c.fill=c.NONE;
    c.weightx =0;
    c.weighty =0;
    c.gridx = 1;
    c.gridy = 1;
    lineaBajaDesencriptado.add(cargarEncriptado,c);
	

    JPanel pp= new JPanel(new GridLayout(1,2));
    claveDesencripta=new JTextArea("");
    claveDesencripta.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    c.fill=c.BOTH;
    c.weightx =1;
    c.gridx = 1;
    c.gridy = 2;
    c.weighty =0;
    c.gridheight  = 7;
    pp.add(new JLabel("Key: "));
    pp.add(new JScrollPane(claveDesencripta));
    lineaBajaDesencriptado.add(pp,c);

    jtDesencriptado=new JTextArea("");
    jtDesencriptado.setEditable(false);
    jtDesencriptado.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    c.fill=c.BOTH;
    c.weightx =1;
    c.gridx = 3;
    c.gridy = 3;
    c.weighty =1;
    c.gridheight  = 7;
    lineaBajaDesencriptado.add(new JScrollPane(jtDesencriptado),c);
    ventana3 = new JLabel();
    panelUnico = new JScrollPane(ventana3);
    panelUnico.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    panelUnico.getViewport().setBackground(new Color(0xDDDDDD));
    c.fill=c.BOTH;
    c.gridx = 1;
    c.gridy = 3;
    c.weighty =1;
    c.gridheight  = 7;
    lineaBajaDesencriptado.add(panelUnico,c);


    centroDesencriptado.add(lineaBajaDesencriptado);

     ventana.setSize(800,600);
     ventana.setTitle("Stenography");
     ventana.setDefaultCloseOperation(ventana.EXIT_ON_CLOSE);
     ventana.setVisible(true);
     MouseListener ml= new  MouseListener(){
        public void mouseClicked(MouseEvent e){
          if (e.getSource() instanceof JButton){
              JButton boton = (JButton) e.getSource();
              if (boton.isEnabled()){
                if (boton==cargar){
                    JFileChooser jf = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG", "png");
                    jf.setFileFilter(filter);
                    int seleccion = jf.showOpenDialog(ventana);
                    if (seleccion == JFileChooser.APPROVE_OPTION)
                    {
                       File fichero = jf.getSelectedFile();
                       try{
                           imgOriginal=new Imagen(fichero);
                           imgOriginal.visualiza(ventana1);
                           encriptar.setEnabled(true);
                       }catch(Exception ex){
                           JOptionPane.showMessageDialog(null, "The file could not be loaded: "+ fichero.getName());
                       }
                    }
                }else if (boton==guardar){
                    JFileChooser jf = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG", "png");
                    jf.setFileFilter(filter);
                    int seleccion = jf.showSaveDialog(ventana);
                    if (seleccion == JFileChooser.APPROVE_OPTION)
                    {
                       File fichero = jf.getSelectedFile();
                       try{
                         String pat=fichero.getPath();
                           if (pat.substring(pat.length()-4).equals(".png"))
                              imgTextoEncriptado.guardar(new File(pat));
                           else
                              imgTextoEncriptado.guardar(new File(pat+".png"));

                       }catch(Exception ex){
                           JOptionPane.showMessageDialog(null, "The file could not be saved: "+ fichero.getName());
                       }
                    }
                }
                else if (boton==cargarEncriptado){
                  int clave;
                  try{
                    clave=Integer.parseInt(claveDesencripta.getText());
                    if (clave==0)
                       throw new NumberFormatException();
                    JFileChooser jf = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG", "png");
                    jf.setFileFilter(filter);
                    int seleccion = jf.showOpenDialog(ventana);
                    if (seleccion == JFileChooser.APPROVE_OPTION)
                    {
                       File fichero = jf.getSelectedFile();
                       try{
                           imgEncriptada=new Imagen(fichero);
                           desencripta(clave);
                       }catch(IOException ex){
                           JOptionPane.showMessageDialog(null, "The file could not be opened "+ fichero.getName());
                       }
                    }
                  }catch(Exception Es){
                             JOptionPane.showMessageDialog(null, "Invalid Format/key encryption.");
                  }

                }else if (boton==encriptar){
                          int clave;
                          try{
                            clave=Integer.parseInt(claveEncripta.getText());
                            if (clave==0)
                               throw new NumberFormatException();
  
                            String aEnc=aEncriptar.getText();
                            try{
                            if (aEnc.length()>0){
                                encripta(clave);
                                guardar.setEnabled(true);
                            }else
                                 JOptionPane.showMessageDialog(null, "No text for encrypt");
                            }catch (LimitExcededException esr8){
                                 JOptionPane.showMessageDialog(null, "Image too small to crypt current message.");
                            }
                          
                          }catch(Exception Es){
                             JOptionPane.showMessageDialog(null, "Invalid Format/key encryption.");
                          }

                }
              }
           }
        }
        public void mouseEntered(MouseEvent e){
        }
        public void mouseExited(MouseEvent e){
        }
        public void mousePressed(MouseEvent e){
        }
        public void mouseReleased(MouseEvent e){
        }
     };
     cargar.addMouseListener(ml);
     encriptar.addMouseListener(ml);
     guardar.addMouseListener(ml);
     cargarEncriptado.addMouseListener(ml);

  }
   private Stenography(){
   }
   private static void desencripta(int c){
     try{
       String textoDesencriptado=Encriptar.desencripta(imgEncriptada,c);
       jtDesencriptado.setText(textoDesencriptado);
       System.out.println("text: "+textoDesencriptado);
       imgEncriptada.visualiza(ventana3);
     }catch(Exception e){
       e.printStackTrace();
       JOptionPane.showMessageDialog(null, e.toString());
     }
   }

   private static void encripta(int c) throws LimitExcededException{
     try{
       imgTextoEncriptado = (Imagen) imgOriginal.clone();
       textoEncriptado=Encriptar.encripta(aEncriptar.getText(), imgTextoEncriptado,c);
       encriptado.setText(textoEncriptado);
       imgTextoEncriptado.visualiza(ventana2);
     }catch(Exception e){
      e.printStackTrace();
       JOptionPane.showMessageDialog(null, e.toString());

     }
   }


  public static void main(String[] args){

  }
}