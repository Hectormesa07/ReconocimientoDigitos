
package redneuronal;

import java.util.Arrays;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author GustavoAdolfo
 */
public class Red implements LearningEventListener {
    
    MultiLayerPerceptron myMlPerceptron;
    NeuralNetwork neuralNet=myMlPerceptron;
    int inputlayer;
    int hiddenlayer;
    int outputlayer;

    public Red() {
    }
    
    public Red(int inputlayer, int hiddenlayer, int outputlayer, double learning_rate, int iterations) {
        this.inputlayer=inputlayer;
        this.outputlayer =outputlayer;
        this.hiddenlayer=hiddenlayer;
        // Creación del perceptron multicapa
        myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, this.inputlayer, this.hiddenlayer, this.outputlayer);
        //Inicializacion de los pesos
        myMlPerceptron.randomizeWeights(-0.1,0.1);
        //System.out.println(Arrays.toString(myMlPerceptron.getWeights()));
        
        //Establece el algoritmo de aprendizaje para esta red
        //setlearning rule  ->Devuelve el algoritmo de aprendizaje de esta red
        myMlPerceptron.setLearningRule(new BackPropagation());        
        //System.out.println(myMlPerceptron.getLearningRule());  Devuelve backpropagation      
        myMlPerceptron.getLearningRule().setLearningRate(learning_rate);        
        myMlPerceptron.getLearningRule().setMaxError(0.0070);  //--->Asi es posible modificar el error
        myMlPerceptron.getLearningRule().setMaxIterations(iterations);
        //setMaxError-->Establece el error de red permitido, que indica cuándo detener la capacitación de aprendizaje
        
        LearningRule learningRule = myMlPerceptron.getLearningRule();
        learningRule.addListener(this); 
        //System.out.println(new BackPropagation().getMaxError());---->0.01 valor predeterminado y 2147483647 iteraciones
        
    }

    /**
     * @param input 
     */
    public void training(double[][] input) {
        // Creación del conjunto de entrenamiento
        
        //DataSet-->Esta clase representa una colección de filas de datos 
        //(instancias de DataSetRow) utilizadas para entrenar y probar redes neuronales.
        DataSet trainingSet = new DataSet(inputlayer, outputlayer);
        
        double [][] output = new double [outputlayer][outputlayer];
        for (int i=0; i<outputlayer; i++){
            double [] pattern_out = new double [outputlayer];
            pattern_out[i]=1.0;
            output[i]= pattern_out;
        }  

        int out_n=input.length/outputlayer;    
        for (int i=0; i<input.length; i++){
            int index= i/out_n;
            trainingSet.addRow(new DataSetRow(input[i], output[index]));
        }
        
        // Aprender el conjunto de entrenamiento
        System.out.println("Training neural network...");
        myMlPerceptron.learn(trainingSet);   //otra forma -->learn(DataSet trainingSet,L learningRule)
        neuralNet=myMlPerceptron;        
    }
    
    public void save(String name){
        myMlPerceptron.save(name+".nnet");
    }
    /**
     * Imprime la salida de la red para la entrada de prueba 
     * @param numbertest
     * @return 
     */
    public int testNeuralNetwork(double [] numbertest){
        neuralNet.setInput(numbertest);
        
        neuralNet.calculate();
        double[] networkOutput = neuralNet.getOutput();
        
        //System.out.println(NeuralNet.getLearningRule().getTotalNetworkError());  //Devuelve el error de red total en la época de aprendizaje anterior
        double max=0.0;
        int index_max=0;
             
        for (int i=0; i<networkOutput.length;i++){
            if (networkOutput[i]>max){
                max=networkOutput[i];
                index_max=i;
            }
        }
        //System.out.print("Input: " + Arrays.toString( numbertest ) );
            //System.out.println(" Output: " + Arrays.toString( networkOutput) );
            //System.out.println(" Output: " + index_max );
                    
        return index_max;
    }
         
    //Esta clase contiene información sobre la fuente de algún evento de aprendizaje.
    @Override
    public void handleLearningEvent(LearningEvent event) {
        BackPropagation bp = (BackPropagation)event.getSource();
                if (event.getEventType() != LearningEvent.Type.LEARNING_STOPPED)
                    System.out.println(bp.getCurrentIteration() + ". iteration : "+ bp.getTotalNetworkError());
    }    

}