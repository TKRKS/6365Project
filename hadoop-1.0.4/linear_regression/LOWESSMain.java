import org.apache.commons.math3.linear.*;

import java.io.*;
import java.util.StringTokenizer;

public class LOWESSMain {
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {		
		FileInputStream file = new FileInputStream(args[0]);
		BufferedReader reader = new BufferedReader(new InputStreamReader(file));
		String line = reader.readLine();
		StringTokenizer tokenizer = new StringTokenizer(line);

		int numTrain=0, numTest = 0;
		int numFeats = tokenizer.countTokens()-2;
		
		while( line!=null ){
			tokenizer = new StringTokenizer(line);
			String type = tokenizer.nextToken();
			if(type.compareTo("trainx")==0){
				numTrain++;
			}
			else if(type.compareTo("testx")==0){
				numTest++;
			}
			line = reader.readLine();
		}
		RealMatrix trainX = MatrixUtils.createRealMatrix(numTrain,numFeats);
		RealMatrix trainY = MatrixUtils.createRealMatrix(numTrain,1);
		RealMatrix testX = MatrixUtils.createRealMatrix(numTest, numFeats);
		RealMatrix testY = MatrixUtils.createRealMatrix(numTest,1);

		reader.close();
		file = new FileInputStream(args[0]);
		reader = new BufferedReader(new InputStreamReader(file));
		
		//Parse training set
		for(int i=0; i<numTrain; i++){
			line = reader.readLine();
			tokenizer = new StringTokenizer(line);
			
			double[] x = new double[numFeats];
			tokenizer.nextToken();
			tokenizer.nextToken();
			for(int j=0; j<numFeats; j++){
				x[j] = Double.parseDouble(tokenizer.nextToken());
			}
			trainX.setRow(i, x);
		}

		for(int i=0; i<numTrain; i++){
			line = reader.readLine();
			tokenizer = new StringTokenizer(line);
			
			double[] y = new double[1];
			tokenizer.nextToken();
			tokenizer.nextToken();
			y[0] = Double.parseDouble(tokenizer.nextToken());
			trainY.setRow(i, y);			
		}

		//Parse tesing set
		for(int i=0; i<numTest; i++){
			line = reader.readLine();
			tokenizer = new StringTokenizer(line);
			
			double[] x = new double[numFeats];
			tokenizer.nextToken();
			tokenizer.nextToken();
			for(int j=0; j<numFeats; j++){
				x[j] = Double.parseDouble(tokenizer.nextToken());
			}
			testX.setRow(i, x);
		}

		for(int i=0; i<numTest; i++){
			line = reader.readLine();
			tokenizer = new StringTokenizer(line);
			
			double[] y = new double[1];
			tokenizer.nextToken();
			tokenizer.nextToken();
			y[0] = Double.parseDouble(tokenizer.nextToken());
			testY.setRow(i, y);			
		}
		LOWESSLearner learner = new LOWESSLearner(1,0.5);
		learner.train(trainX, trainY);
		RealMatrix result = learner.predict(testX);
		System.out.println(result);
		reader.close();
		
	}

}