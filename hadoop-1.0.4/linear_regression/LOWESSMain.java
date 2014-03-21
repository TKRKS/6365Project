import org.apache.commons.math.linear.*;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class LOWESSMain {
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {		
		FileInputStream file = new FileInputStream(args[0]);
		BufferedReader reader = new BufferedReader(new InputStreamReader(file));
		reader.mark(1);
		String line = reader.readLine();
		StringTokenizer tokenizer = new StringTokenizer(line,",");

		int numTrain = Integer.parseInt(args[1]);
		int numTest = Integer.parseInt(args[2]);
		int numFeats = tokenizer.countTokens()-1;

		RealMatrix trainX = MatrixUtils.createRealMatrix(numTrain,numFeats);
		RealMatrix trainY = MatrixUtils.createRealMatrix(numTrain,1);
		RealMatrix testX = MatrixUtils.createRealMatrix(numTest, numFeats);
		RealMatrix testY = MatrixUtils.createRealMatrix(numTest,1);
		
		reader.reset();
		
		//Parse training set
		for(int i=0 ; i<numTrain; i++){
			line = reader.readLine();
			tokenizer = new StringTokenizer(line,",");
			
			double[] y = new double[1];
			double[] x = new double[numFeats];
			
			y[0] = Double.parseDouble(tokenizer.nextToken());
			for(int j=0; j<numFeats; j++){
				x[j] = Double.parseDouble(tokenizer.nextToken());
			}
			trainX.setRow(i, x);
			trainY.setRow(i, y);
		}

		//Parse tesing set
		for(int i=0 ; i<numTest; i++){
			line = reader.readLine();
			tokenizer = new StringTokenizer(line,",");
			
			double[] y = new double[1];
			double[] x = new double[numFeats];
			
			y[0] = Double.parseDouble(tokenizer.nextToken());
			for(int j=0; j<numFeats; j++){
				x[j] = Double.parseDouble(tokenizer.nextToken());
			}
			testX.setRow(i, x);
			testY.setRow(i, y);
		}

		LOWESSLearner learner = new LOWESSLearner(1,0.5);
		learner.train(trainX, trainY);
		RealMatrix result = learner.predict(testX);
		reader.close();
		
	}

}