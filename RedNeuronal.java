package redneuronal;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;
/**
 *
 * @author GustavoAdolfo
 */

public class RedNeuronal {
	
	private static final String IMG_SUFIX = ".png";	
	/**
	 * Regresa como BufferedImage el archivo especificado
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private BufferedImage readImage(File file) throws IOException{
		BufferedImage img = ImageIO.read(file); //Este objeto se usa para almacenar una imagen en la RAM
		return img; 
	}
	
	/**
	 * Convertir la imagen en un vector de pixeles (Se asume que todas las imagenes tienen el mismo ancho y alto)
	 * @param img
	 * @return
	 */
	private double[] obtainPixelsList(BufferedImage img){		
            double[] pixelsValues = new double[img.getWidth()*img.getHeight()];

            for(int i=0;i<img.getWidth();i++){
                for(int j=0;j<img.getHeight();j++){
                    int rgb = img.getRGB(i, j);
                    Color color =new Color(rgb);
                    double lum= (double)color.getBlue()/255; //Como en los colores grises se cumple siempre que 
                                                             //red=green=blue se extrae uno de ellos y se divide 
                                                             //entre 255 para escalarlo, de manera que tendremos valores
                                                             //entre 0 y 1, siendo los valores mas cercanos a 0 colores oscuros
                                                             // y cercanos a 1 colores claros.
                    pixelsValues[j*img.getWidth() +i]=lum;
                }
            }
            return pixelsValues;
	}
	
	/**
	 * Cargar los patrones de los numeros en la matriz (vector de vectores)
	 * Procesa cada imagen píxel por píxel almacenando como una vector de píxeles
	 * @throws IOException 
	 */

    int pixels= 28*28;//8*8;
    int num_datos= 100;//9; //Cantidad de imagenes por cada numero
    private double [][] patterns = new double[num_datos*10][pixels];
	private void loadPatterns(String data_Set) throws IOException{
            int k=0;
            for(int i=0;i<10;i++){
                for(int j=1;j<=num_datos;j++){
                    //String patternName = i +"."+j+IMG_SUFIX;
                    String patternName = i +" ("+j+")"+IMG_SUFIX;
                    String folder="/C:/Users/hector/Documents/Documentos/UTP/computacion blanda/Trabajos/RedNeuronal/"+data_Set+"/"+i+"/"+patternName;
                    //String folder="/C:/Users/GustavoAdolfo/Desktop/RedNeuronal/build/classes/"+data_Set+"/"+patternName;
                    File f = new File(folder);
                    BufferedImage pattern = readImage(f);
                    //Cargar el patron
                    patterns[k]=obtainPixelsList(pattern);
                    k+=1;
                }
            }
	}
        
        /**
	 * Cargar los patrones de los simbolos en la matriz (vector de vectores)
	 * Procesa cada imagen píxel por píxel almacenando como una vector de píxeles
	 * @throws IOException 
	 */
        int pixelsSymbol= 28*28;//28*28;
        int numdatosSymbol= 9;//30; //Cantidad de imagenes por cada symbolo
        String [] Symbols = {"+","-","x","d"};
        
        private double [][] patterns_symbols = new double[numdatosSymbol*4][pixelsSymbol];
	private void loadPatterns_symbols(String dataset) throws IOException{
            int k=0;
            for (int i=0; i<Symbols.length; i++){
                for(int j=1;j<=numdatosSymbol;j++){
                    String patternName = Symbols[i] +"."+j+IMG_SUFIX;
                    String folder1 = "/C:/Users/hector/Documents/Documentos/UTP/computacion blanda/Trabajos/RedNeuronal/"+dataset+"_symbol/"+patternName;
                    File f = new File(folder1);
                    BufferedImage pattern = readImage(f);

                    //Cargar el patron
                    patterns_symbols[k]=obtainPixelsList(pattern);
                    k+=1;
                }
            }
        }
        
        private int efectivity (Red network,double [][] patterns_test,int numdate){
            int j=0;
            int count=0;
            for (int i=0; i<patterns_test.length; i++){
                //System.out.println("Pattern #: "+i);
                int n1= network.testNeuralNetwork(patterns_test[i]);
                j=i/numdate;
                if (n1==j){
                    count+=1;
                }
                //System.out.println(n1);
            }
            return count;
        }
                	
	/**
	 * Reconoce los numeros y simbolos de las imegeness provistas
	 * @param f
	 * @return
	 * @throws IOException 
	 */
	private void recognize(File f, File g, File h) throws IOException{
            BufferedImage img1 = readImage(f);
            BufferedImage img2= readImage(g);
            BufferedImage img3 = readImage(h);
            
            //Obtener el vector completo de pixeles para img1, img2 e img3
            double[] imgPixels_1 = obtainPixelsList(img1);
            double[] imgPixels_2 = obtainPixelsList(img2);
            double[] imgPixels_3 = obtainPixelsList(img3);
            
            /*//Cargar los patrones
            loadPatterns("training");
            loadPatterns_symbols("training");
            
            //Creacion de la redes neronales
            
            //Red neuronal para el reconocimiento de numeros
            Red network = new Red(784,100,10,0.5,240);  //Neuronas de entrada, Neuronas cultas, Neuronas de salida, tasa de aprendizaje, iteraciones
            network.training(patterns);
            network.save("PerceptronNumber1");
            
            //Red neuronal para el reconocimiento de simbolos
            Red networkSymbol = new Red(784,100,4,0.5,240);  //Neuronas de entrada, Neuronas cultas, Neuronas de salida, tasa de aprendizaje, iteraciones
            networkSymbol.training(patterns_symbols);
            networkSymbol.save("PerceptronSymbol1");
            
            */
            //Cargar una red ya guardada 
            Red network = new Red(); 
            network.neuralNet=NeuralNetwork.createFromFile("PerceptronNumber3.nnet");
            
            Red networkSymbol = new Red(); 
            networkSymbol.neuralNet=NeuralNetwork.createFromFile("PerceptronSymbol3.nnet");
            /*
            //Verficar efectividad de la red (digitos)
            num_datos=40;    //Numero de imagenes por cada digito de prueba
            patterns= new double[num_datos*10][pixels];
            
            loadPatterns("testing");
            int count= efectivity(network,patterns,num_datos);
            System.out.println("Número de aciertos en el reconocimiento de digitos: "+count+"/"+patterns.length);
            
            //Verificar efectividad de la red (simbolos)
            numdatosSymbol=9;    //Numero de imagenes por cada simbolo de prueba
            patterns_symbols= new double[numdatosSymbol*4][pixelsSymbol];
            loadPatterns_symbols("testing");
            count= efectivity(networkSymbol,patterns_symbols,numdatosSymbol);
            System.out.println("Número de aciertos en el reconocimiento de simbolos: "+count+"/"+patterns_symbols.length);
            
*/
            //Reconocimiento de los dos numeros especificados
            int n1=network.testNeuralNetwork(imgPixels_1);
            int n2=network.testNeuralNetwork(imgPixels_2);
            System.out.println("Numeros: ");
            System.out.println(n1);
            System.out.println(n2);
            
            //Reconocimiento del simbolo especificado
            int n3= networkSymbol.testNeuralNetwork(imgPixels_3);

            double result=0.0;
            switch (n3) {
                case 0:
                    result=n1+n2;
                    System.out.println("Simbolo: +");
                    break;
                case 1:
                    result=n1-n2;
                    System.out.println("Simbolo: -");
                    break;
                case 2:
                    result=n1*n2;
                    System.out.println("Simbolo: *");
                    break;
                case 3:
                    result=(double)n1/n2;
                    System.out.println("Simbolo: /");
                    break;
                default:
                    break;
            }
            System.out.println("Resultado: "+result);
        }

	/**
	 * Main entry
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		RedNeuronal r = new RedNeuronal();
                //String image0="1.4";
                String image0="7 (4)";
                //String image1="3.7";
                String image1="5 (9)";
                String image2="-.5";
		//File test0 = new File("C:\\Users\\GustavoAdolfo\\Desktop\\RedNeuronal\\src\\patterns\\"+image0+".png");
                
                File test0 = new File("C:\\Users\\hector\\Documents\\Documentos\\UTP\\computacion blanda\\Trabajos\\RedNeuronal\\testing\\"+image0.substring(0,1)+"\\"+image0+".png");
                
		//File test1 = new File("C:\\Users\\GustavoAdolfo\\Desktop\\RedNeuronal\\src\\patterns\\"+image1+".png");
                File test1 = new File("C:\\Users\\hector\\Documents\\Documentos\\UTP\\computacion blanda\\Trabajos\\RedNeuronal\\testing\\"+image1.substring(0,1)+"\\"+image1+".png");
                File test2 = new File("C:\\Users\\hector\\Documents\\Documentos\\UTP\\computacion blanda\\Trabajos\\RedNeuronal\\testing_symbol\\"+image2+".png");
                               
                r.recognize(test0,test1,test2);
                
                
                /*
                for (int i=0; i<10; i++){
                    for (int j=1; j<10;j++){
                        String photo=i+"."+j;
                        //System.out.println(photo);
                        File test = new File("C:\\Users\\GustavoAdolfo\\Desktop\\RedNeuronal\\src\\patterns\\"+photo+".png");
                        r.recognize(test,test,test3);
                    }
                }
                    */
                /*for (int i=1;i<10;i++){
                    String photo="x."+i;
                    File test = new File("C:\\Users\\GustavoAdolfo\\Desktop\\RedNeuronal\\src\\patterns_symbol\\"+photo+".png");
                    r.recognize(test1,test2,test);
                }*/
	}

}
