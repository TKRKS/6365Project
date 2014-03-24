import java.util.Arrays;

import org.apache.commons.math3.linear.*;

public class LOWESSLearner {
	private RealMatrix trainX, trainY, testX, testY;
	private double a,c;
	
	public LOWESSLearner(double a, double c){
		this.a = a;
		this.c = c;
	}
	
	public void train(RealMatrix trainX, RealMatrix trainY){
		this.trainX = trainX;
		this.trainY = trainY;
	}
	
	public RealMatrix predict(RealMatrix testX){
		this.testX = testX;
		int n = this.testX.getRowDimension();
		RealMatrix result = MatrixUtils.createRealMatrix(1, n);
		for(int i=0; i<n; i++){
			RealMatrix input = testX.getRowMatrix(i);
			double predict = predictPoint(input);
			result.setEntry(0, i, predict);
		}
		return null;
	}
	
	public void setA(int a){
		this.a = a;
	}

	public void setC(int c){
		this.c = c;
	}
	
	private double gaussianKernel(RealMatrix x, RealMatrix p){
		RealMatrix diff = x.subtract(p);
		RealMatrix dpMatrix = diff.multiply(diff.transpose());
		double dotProduct = dpMatrix.getEntry(0, 0);
		return a * Math.exp(dotProduct / (-2.0 * Math.pow(c, 2)));
	}
	
	private RealMatrix getWeights(RealMatrix p){
		int n = trainX.getRowDimension();
		RealMatrix weights = MatrixUtils.createRealMatrix(n,n);
		for(int i=0;i<n;i++){
			double gk = gaussianKernel(trainX.getRowMatrix(i),p);
			weights.addToEntry(i, i, gk);
		}
		System.out.println(weights);
		return weights;
	}
	
	private double predictPoint(RealMatrix p){
		double result = 0;
		RealMatrix weights = getWeights(p);
		
		RealMatrix x = this.trainX;
		RealMatrix y = this.trainY;
		RealMatrix xt = x.transpose().multiply(weights.multiply(x));
		RealMatrix inv_xt = new LUDecomposition(xt).getSolver().getInverse();
		RealMatrix betas = inv_xt.multiply(x.transpose().multiply(weights.multiply(y)));
		RealMatrix product = betas.multiply(p);
		result = product.getEntry(0,0);
		return result;
	}
}