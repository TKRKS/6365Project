package ClusteringSerial;

import java.io.*;
import java.lang.Math;
import ClusteringSerial.CenteroidPair;

public class ClusteringSerialMain {

	final static int clusters = 3;
	final static int runs = 5;
	static int records = 0;
	static double average = 0.0;
	final static CenteroidPair[] pairs = {new CenteroidPair("", 0), new CenteroidPair("", 0), new CenteroidPair("", 0)};

	public static void main(String[] args) throws Exception {
		BufferedReader reader = getReader(args[0]);
		String line = reader.readLine();
    		File tempFile = new File("temp1");
		tempFile.createNewFile();
    		tempFile = new File("temp2");
		tempFile.createNewFile();
		//Pick out first centeroids
		while (line != null) {
			String[] splitLine = line.split("\\s+");
			if (records < clusters) {
				pairs[records].setId(splitLine[0]);
				pairs[records].setValue(splitLine.length - 1);
			}
			records++;
			line = reader.readLine();
		}
		reader = getReader(args[0]);
		FileWriter outputWriter = new FileWriter("temp1");
		line = reader.readLine();
		//Find starting clusters
		while (line != null) {
			String[] splitLine = line.split("\\s+");
			String output = splitLine[0];
			CenteroidPair closest = pairs[0];
			for (int i = 1; i < clusters; i++) {
				if (Math.abs(pairs[i].getValue() - (splitLine.length - 1)) < Math.abs(closest.getValue() - (splitLine.length - 1))) {
					closest = pairs[i];
				}	
			}
			output = output + "\t" + closest.getId();
			for (int i = 1; i < splitLine.length; i++) {
				output = output + "\t" + splitLine[i];
			}
			output = output + "\n";
			outputWriter.write(output);
			line = reader.readLine();
		}
		outputWriter.close();
		//Run through the specified number of runs
		for (int i = 0; i < runs; i++) {
			String inputFile = i % 2 == 0 ? "temp1" : "temp2";
			String outputFile = i % 2 == 0 ? "temp2" : "temp1";
			//Empty the temp file
			double[] averages = calculateAverages(inputFile);
			findAndSetCenters(inputFile, averages);
			if (i != (runs - 1)) {
				findNewGroupsAndOutputFile(inputFile, outputFile);
			} else {
				findNewGroupsAndOutputFile(inputFile, args[1]);
			}
		}
    		tempFile = new File("temp1");
		tempFile.delete();
    		tempFile = new File("temp2");
		tempFile.delete();
	}

	public static BufferedReader getReader(final String inputPath) throws FileNotFoundException {
		FileInputStream file = new FileInputStream(inputPath);
		return new BufferedReader(new InputStreamReader(file));
	}

	/**
	 *	Calculates the average of each cluster.
	 */
	public static double[] calculateAverages(final String path) throws FileNotFoundException, IOException {
		double[] averages = new double[clusters];
		int[] clusterCount = new int[clusters];
		for (int i = 0; i < clusters; i++) {
			averages[i] = 0.0;
			clusterCount[i] = 0;
		}
		BufferedReader reader = getReader(path);
		String line = reader.readLine();
		while (line != null) {
			String[] splitLine = line.split("\\s+");
			for (int i = 0; i < clusters; i++) {
				if (pairs[i].getId().equals(splitLine[1])) {
					averages[i] = (averages[i] * clusterCount[i] + splitLine.length - 2)/(clusterCount[i] + 1);	
					clusterCount[i] = clusterCount[i] + 1;
					break;
				}
			}
			line = reader.readLine();
		}
		return averages;
	}

	/**
	 *	Finds and sets new centers for each group
	 */
	public static void findAndSetCenters(final String path, final double[] averages) throws FileNotFoundException, IOException {
		CenteroidPair[] newPairs = new CenteroidPair[clusters];
		for (int i = 0; i < clusters; i++) {
			newPairs[i] = new CenteroidPair("", Integer.MAX_VALUE);
		}
		BufferedReader reader = getReader(path);
		String line = reader.readLine();
		while (line != null) {
			String[] splitLine = line.split("\\s+");
			String output = splitLine[0];
			for (int i = 0; i < clusters; i++) {
				if (pairs[i].getId().equals(splitLine[1])) {
					if (Math.abs(pairs[i].getValue() - averages[i]) > Math.abs((splitLine.length - 2) - averages[i])) {
						pairs[i].setId(splitLine[0]);
						pairs[i].setValue(splitLine.length - 2);
					}
					break;
				}	
			}
			line = reader.readLine();
		}
	}

	/**
	 *	Creates the new groups and outputs the new temporary file.
	 */
	public static void findNewGroupsAndOutputFile(final String inputPath, final String outputPath) throws FileNotFoundException, IOException {
		BufferedReader reader = getReader(inputPath);
		FileWriter outputWriter = new FileWriter(outputPath);
		String line = reader.readLine();
		while (line != null) {
			String[] splitLine = line.split("\\s+");
			String output = splitLine[0];
			CenteroidPair closest = pairs[0];
			for (int i = 1; i < clusters; i++) {
				if (Math.abs(pairs[i].getValue() - (splitLine.length - 2)) < Math.abs(closest.getValue() - (splitLine.length - 2))) {
					closest = pairs[i];
				}	
			}
			output = output + "\t" + closest.getId();
			for (int i = 2; i < splitLine.length; i++) {
				output = output + "\t" + splitLine[i];
			}
			output = output + "\n";
			outputWriter.write(output);
			line = reader.readLine();
		}
		outputWriter.close();
	}
}

