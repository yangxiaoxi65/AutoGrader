package AutoGrader;

import java.io.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

public class Grading {
	public static String[] gradingTask;
	public static List<Map> readStandardAnswer(String standardAnswer) {
		try {
			List<Map> standardInputOutputOrdered = new ArrayList<Map>(); 
			Map<List<String>, List<String>> standardInputOutput = new HashMap<List<String>, List<String>>();
			Scanner input = new Scanner(new FileReader(standardAnswer));
			// Removing two lines of header information
			String line = input.nextLine();
			line = input.nextLine();
			ArrayList<String> eachStandardInput = new ArrayList<String>();
			ArrayList<String> eachStandardOutput = new ArrayList<String>();
			while (input.hasNextLine()) {
				line = input.nextLine();
//				System.out.println(line);
				if (line.length() > 8) {
					String word[] = line.split(",");
					eachStandardInput.add(word[0]);
					eachStandardOutput.add(word[1]);
				}
				else{
					standardInputOutput = new HashMap<List<String>, List<String>>();
					standardInputOutput.put(eachStandardInput, eachStandardOutput);
					standardInputOutputOrdered.add(standardInputOutput);
					eachStandardInput = new ArrayList<String>();
					eachStandardOutput = new ArrayList<String>();
					// Removing two lines of header information
					line = input.nextLine();
					line = input.nextLine();
				}
			}
			standardInputOutput = new HashMap<List<String>, List<String>>();
			standardInputOutput.put(eachStandardInput, eachStandardOutput);
			standardInputOutputOrdered.add(standardInputOutput);
			return standardInputOutputOrdered;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<String> taskName(String standardAnswer) {
		try {
			List<String> taskNames=new ArrayList<>();
			Scanner input = new Scanner(new FileReader(standardAnswer));
			// Removing two lines of header information
			while (input.hasNextLine()) {
				String line = input.nextLine();
				String word[] = line.split(",");
				if(word.length == 1) {
					taskNames.add(word[0]);
				}
			}
			return taskNames;
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Map<Integer, List<String>> readScore(String standardAnswer) {
		try {
			Map<Integer, List<String>> standardScore = new HashMap<Integer, List<String>>();
			Scanner input = new Scanner(new FileReader(standardAnswer));
			// Removing two lines of header information
			String line = input.nextLine();
			line = input.nextLine();
			ArrayList<String> eachScore = new ArrayList<String>();
			Integer i = 0;
			while (input.hasNextLine()) {
				line = input.nextLine();
				if (line.length() > 8) {
					String word[] = line.split(",");
					eachScore.add(word[2]);
				}
				else{
					
//					System.out.println("&&&&&&&&&&&&&&&&"+i);
//					System.out.println("&&&&&&&&&&&&&&&&"+eachScore.get(0));
					standardScore.put(i, eachScore);
					eachScore = new ArrayList<String>();
					// Removing two lines of header information
					line = input.nextLine();
					line = input.nextLine();
					i++;
				}
			}
//			System.out.println("**************************"+i);
//			System.out.println("&&&&&&&&&&&&&&&&"+eachScore.get(0));
			standardScore.put(i, eachScore);
//			System.out.println("&&&&&&&&&&&&&&&&"+(standardScore.get(0)).get(0));
			return standardScore;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Map<Integer, List<String>> readSpace(String standardAnswer) {
		try {
			Map<Integer, List<String>> standardSpace = new HashMap<Integer, List<String>>();
			Scanner input = new Scanner(new FileReader(standardAnswer));
			// Removing two lines of header information
			String line = input.nextLine();
			line = input.nextLine();
			ArrayList<String> eachSpace = new ArrayList<String>();
			Integer i = 0;
			while (input.hasNextLine()) {
				line = input.nextLine();
				if (line.length() > 8) {
					String word[] = line.split(",");
					eachSpace.add(word[3]);
				}
				else{
					
//					System.out.println("&&&&&&&&&&&&&&&&"+i);
//					System.out.println("&&&&&&&&&&&&&&&&"+eachScore.get(0));
					standardSpace.put(i, eachSpace);
					eachSpace = new ArrayList<String>();
					// Removing two lines of header information
					line = input.nextLine();
					line = input.nextLine();
					i++;
				}
			}
//			System.out.println("**************************"+i);
//			System.out.println("&&&&&&&&&&&&&&&&"+eachScore.get(0));
			standardSpace.put(i, eachSpace);
//			System.out.println("&&&&&&&&&&&&&&&&"+(standardScore.get(0)).get(0));
			return standardSpace;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Map<Integer, List<String>> readCase(String standardAnswer) {
		try {
			Map<Integer, List<String>> standardCase = new HashMap<Integer, List<String>>();
			Scanner input = new Scanner(new FileReader(standardAnswer));
			// Removing two lines of header information
			String line = input.nextLine();
			line = input.nextLine();
			ArrayList<String> eachCase = new ArrayList<String>();
			Integer i = 0;
			while (input.hasNextLine()) {
				line = input.nextLine();
				if (line.length() > 8) {
					String word[] = line.split(",");
					eachCase.add(word[4]);
				}
				else{
					
//					System.out.println("&&&&&&&&&&&&&&&&"+i);
//					System.out.println("&&&&&&&&&&&&&&&&"+eachScore.get(0));
					standardCase.put(i, eachCase);
					eachCase = new ArrayList<String>();
					// Removing two lines of header information
					line = input.nextLine();
					line = input.nextLine();
					i++;
				}
			}
//			System.out.println("**************************"+i);
//			System.out.println("&&&&&&&&&&&&&&&&"+eachScore.get(0));
			standardCase.put(i, eachCase);
//			System.out.println("&&&&&&&&&&&&&&&&"+(standardScore.get(0)).get(0));
			return standardCase;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	//change
	public static File getStudentResults (String path, File[] ASs, File[]eachTask, List standardOutput, int[] studentCommentsGrade, String[] stdPreprocessingComments,  String[][] studentOutput, String standardAnswerName, int numberOfTask, List<String> eachScore, List<String> spaceRequirement, List<String> caseRequirement){
		try {
//			File standardAnswer = new File(standardAnswerName);
			path += "\\" + findClass(standardAnswerName)+"_Q"+(numberOfTask+1)+ "_Student_Results.csv";
			File studentResults = new File(path);
			FileWriter csvWriter = new FileWriter(studentResults, false);
			csvWriter.append("FileName");
			csvWriter.append(",");
			csvWriter.append("ID");
			csvWriter.append(",");
			csvWriter.append("Name");
			csvWriter.append(",");
			csvWriter.append("Grade");
			csvWriter.append(",");
			csvWriter.append("Comment");
			csvWriter.append(",");
			csvWriter.append("Code");
			csvWriter.append(",");
			csvWriter.append("orderOfStudent");
			csvWriter.append(",");
			csvWriter.append("commentOfGrade");
			for (int caseNumber = 1; caseNumber <= standardOutput.size(); caseNumber++) {
				csvWriter.append(",");
				csvWriter.append("Case"+caseNumber+"_Standard");
				csvWriter.append(",");
				csvWriter.append("Case"+caseNumber+"_StudentOutput");
			}

			csvWriter.append("\n");
			csvWriter.close();		
			
			
			gradingTask = new String[eachTask.length];
			for (int orderOfStudent = 0; orderOfStudent < eachTask.length; orderOfStudent++) {                
				String studentInformation = eachTask[orderOfStudent].getName();
				gradingTask[orderOfStudent] = studentInformation;
//				System.out.println(studentInformation);
				String studentIDNumber = findIDNumber(ASs[orderOfStudent].getName());
				String studentName = findName(ASs[orderOfStudent].getName());
				String[] totalStudentAnswer = new String[standardOutput.size()];
				for (int example = 0; example < standardOutput.size(); example++) {
					if(studentOutput[orderOfStudent][example] != null) {
					String studentAnswer = studentOutput[orderOfStudent][example];//String.valueOf(orderOfAS*orderOfAS);
					totalStudentAnswer[example] = studentAnswer;
//					System.err.println(example+": ");
//					System.out.println(totalStudentAnswer[example]);
//					System.out.println();
					}
				}
				int commentGrade = studentCommentsGrade[orderOfStudent];
				double codeGrade = Double.valueOf(compareAnswer(totalStudentAnswer, standardOutput, eachScore, spaceRequirement, caseRequirement)[0]);
				double studentGrade = codeGrade + studentCommentsGrade[orderOfStudent];
				String gradeComment = compareAnswer(totalStudentAnswer, standardOutput, eachScore, spaceRequirement, caseRequirement)[1];
				getResultFile(ASs[orderOfStudent].getName(), studentResults, studentIDNumber, studentName, studentGrade, commentGrade, codeGrade, orderOfStudent, stdPreprocessingComments[orderOfStudent],gradeComment, totalStudentAnswer, standardOutput);
			}
//			for(int i = 0; i < gradingTask.length; i++) {
//				System.out.println(gradingTask[i]);
//			} 
			return studentResults;
			//getResultTotalFile();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static File getStudentResults2 (String path, File[]eachTask, List standardOutput, int[] studentCommentsGrade, String[] stdPreprocessingComments, String[] studentOutput){
		try {
			path += "\\" + eachTask[0].getName().substring(eachTask[0].getName().length() - 12, eachTask[0].getName().length() - 5);
			File studentResults = new File(path + "_Student_Results.csv");
			studentResults.createNewFile();
			FileWriter csvWriter = new FileWriter(studentResults, false);
			csvWriter.append("studentIDNumber");
			csvWriter.append(",");
			csvWriter.append("studentName");
			csvWriter.append(",");
			csvWriter.append("studentGrade");
			csvWriter.append(",");
			csvWriter.append("orderOfStudent");
			csvWriter.append("\n");
			csvWriter.close();

			for (int orderOfStudent = 0; orderOfStudent < eachTask.length; orderOfStudent++) {                //change ASs.length into getASNumber(ASs)
				String studentInformation = eachTask[orderOfStudent].getName();
				String studentIDNumber = studentInformation.substring(studentInformation.length() - 19, studentInformation.length() - 12);
				String studentName = studentInformation.substring(0, studentInformation.length() - 20);
				String studentAnswer = studentOutput[orderOfStudent];//String.valueOf(orderOfAS*orderOfAS);
				int commentGrade = studentCommentsGrade[orderOfStudent];
				int codeGrade = compareAnswer2(studentAnswer, standardOutput);
				int studentGrade = compareAnswer2(studentAnswer, standardOutput) + studentCommentsGrade[orderOfStudent];
//				getResultFile(studentResults, studentIDNumber, studentName, studentGrade, orderOfStudent, commentGrade, codeGrade, stdPreprocessingComments[orderOfStudent], gradeComment);
			}
			
			return studentResults;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String[] compareAnswer (String[]studentAnswer, List standardOutput, List<String> eachScore, List<String> spaceRequirement, List<String> caseRequirement){
		int marks = 0;
		int fullMarks = 0;
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMinimumFractionDigits(1);
		nf.setMaximumFractionDigits(1);
		String grade = nf.format((Double.valueOf(marks)/Double.valueOf(fullMarks))*100);
		String comment = "";
		String[] gradeAndComment = new String[2];
		gradeAndComment[0]=grade;
		gradeAndComment[1]=comment;
		for (int example = 0; example < standardOutput.size(); example++) {
			fullMarks += Integer.parseInt(eachScore.get(example));
		}
		outCycle: for (int example = 0; example < standardOutput.size(); example++) { 
			ArrayList<String> numberInStudentAnswer = new ArrayList<String>();
			ArrayList<String> numberInStandardAnswer = new ArrayList<String>();
			String[] splitStudentAnswer = studentAnswer[example].split(" ");
			String[] splitStandardAnswer = ((String) standardOutput.get(example)).split(" ");
			for(int item = splitStudentAnswer.length-1; item >=0; item--){
				if (isNumeric(splitStudentAnswer[item])){
					numberInStudentAnswer.add(splitStudentAnswer[item]);
				}
			}
			for(int item = splitStandardAnswer.length-1; item >=0; item--){
				if (isNumeric(splitStandardAnswer[item])){
					numberInStandardAnswer.add(splitStandardAnswer[item]);
				}
			}
			
			if (numberInStandardAnswer.size()==numberInStudentAnswer.size()) {
				for(int i = numberInStandardAnswer.size()-1; i >=0; i--){
		            if(Math.abs(Double.valueOf(numberInStudentAnswer.get(i))-Double.valueOf(numberInStandardAnswer.get(i)))<=0.000001){
		            	studentAnswer[example].replace(numberInStudentAnswer.get(i), numberInStandardAnswer.get(i));
		            }
		            else {
		            	comment+=" ; Example "+(example+1)+" is wrong.";
		            	continue outCycle;
		            }
		        }
			}
            else {
            	comment+=" ; Example "+(example+1)+" is wrong.";
            	continue outCycle;
            }

			System.out.println(studentAnswer[example]);
			
			switch ((spaceRequirement.get(example)+caseRequirement.get(example)).toLowerCase()) {
				case "tt":
					if (studentAnswer[example].replaceAll("\\s", "").equalsIgnoreCase(((String) standardOutput.get(example)).replaceAll("\\s", ""))) {
						marks+=Integer.parseInt(eachScore.get(example));
					}
					else comment+=" ; Example "+(example+1)+" is wrong.";
					break;
				case "ft":
					if (studentAnswer[example].equalsIgnoreCase((String) standardOutput.get(example))) {
						marks+=Integer.parseInt(eachScore.get(example));
					}
					else comment+=" ; Example "+(example+1)+" is wrong.";
					break;
				case "tf":
					if (studentAnswer[example].replaceAll("\\s", "").equals(((String) standardOutput.get(example)).replaceAll("\\s", ""))) {
						marks+=Integer.parseInt(eachScore.get(example));
					}
					else comment+=" ; Example "+(example+1)+" is wrong.";
					break;
				case "ff":
					if (studentAnswer[example].equals(standardOutput.get(example))) {
						marks+=Integer.parseInt(eachScore.get(example));
					}
					else comment+=" ; Example "+(example+1)+" is wrong.";
					break;
			}
		}
		System.out.println(marks);
		System.out.println(fullMarks);
		System.out.println(Double.valueOf(marks)/Double.valueOf(fullMarks)*100);
		grade = nf.format((Double.valueOf(marks)/Double.valueOf(fullMarks))*100);
		System.out.println(grade);
		gradeAndComment[0]=grade;
		gradeAndComment[1]=comment;
		return gradeAndComment;
	}
	public static int compareAnswer2 (String studentAnswer, List standardOutput){
		int zeroPoints = 0;
		int fullMarks = 100;
		String[] splitStudentAnswer = studentAnswer.split(",");
		String studentKeyAnswer = "";
		for(String item : splitStudentAnswer){
			if (isNumeric(item)){
				studentKeyAnswer = item;
				break;
			}
		}
		if (studentKeyAnswer.equals(standardOutput.get(0)) == false) {
			return zeroPoints;
		}

		return fullMarks;
	}
	//change
	public static void getResultFile (String FileName, File studentResults, String studentIDNumber, String studentName, double studentGrade, int commentGrade, double codeGrade, int orderOfStudent, String stdPreprocessingComments, String gradeComment, String[] totalStudentAnswer, List standardOutput){
		try {
			FileWriter csvWriter = new FileWriter(studentResults, true);
			csvWriter.append(FileName);
			csvWriter.append(",");
			csvWriter.append(studentIDNumber);
			csvWriter.append(",");
			csvWriter.append(studentName);
			csvWriter.append(",");
			csvWriter.append(String.valueOf(studentGrade));
			csvWriter.append(",");
			csvWriter.append(String.valueOf(commentGrade));
			csvWriter.append(",");
			csvWriter.append(String.valueOf(codeGrade));
			csvWriter.append(",");
			csvWriter.append(String.valueOf(orderOfStudent+1));
			csvWriter.append(",");
			csvWriter.append(stdPreprocessingComments+gradeComment);
			for (int caseNumber = 0; caseNumber < standardOutput.size(); caseNumber++) {
				csvWriter.append(",");
				csvWriter.append(String.valueOf(standardOutput.get(caseNumber)));
				csvWriter.append(",");
				csvWriter.append(String.valueOf(totalStudentAnswer[caseNumber]));
			}
			csvWriter.append("\n");
			csvWriter.close();
		} catch (IOException e) {
			System.err.println("The file does not exist!");
		}
	}
	//change
	public static void getResultTotalFile (String path, File[] ASs, File[] eachTaskResult, int studentNumber, String standardAnswerName){
		try {
			path += "\\" + findClass(standardAnswerName) + "_Student_Total_Results.csv";
			File studentResults = new File(path);
//			studentResults.createNewFile();
			FileWriter csvWriter = new FileWriter(studentResults, false);
			csvWriter.append("FileName");
			csvWriter.append(",");
			csvWriter.append("ID");
			csvWriter.append(",");
			csvWriter.append("Name"); 
			csvWriter.append(",");
			for (int numberOfTask = 1; numberOfTask <= eachTaskResult.length; numberOfTask++) {
				csvWriter.append("total_Q"+numberOfTask);
				csvWriter.append(",");
				csvWriter.append("gradeComment_Q"+numberOfTask);
				csvWriter.append(",");
				csvWriter.append("comment_Q"+numberOfTask);
				csvWriter.append(",");
				csvWriter.append("code_Q"+numberOfTask);
				csvWriter.append(",");
			}
			csvWriter.append("TotalGrade"); 
			csvWriter.append(",");
			csvWriter.append("total_comment");
			csvWriter.append(",");
			csvWriter.append("total_code");
			csvWriter.append(",");
			csvWriter.append("orderOfStudent");
			csvWriter.append("\n");
			String [][] totalInformation = new String [studentNumber][7+4*eachTaskResult.length];
			
			for (int orderOfStudent = 0; orderOfStudent < studentNumber; orderOfStudent++) {
				String studentInformation = ASs[orderOfStudent].getName();
				String studentIDNumber = findIDNumber(studentInformation);
				String studentName = findName(studentInformation);
				Scanner scan = new Scanner(eachTaskResult[0]);
				String [] eachStudentInformation;
				String str = "";
				scan.nextLine();
			  	for(int i = 0; i <= orderOfStudent; i++ ){
		  		  	str = scan.nextLine();
		  		}
			  	eachStudentInformation = str.split(",");
			  	totalInformation[orderOfStudent][0] = studentInformation;
	  		  	totalInformation[orderOfStudent][1] = studentIDNumber;
	  		  	totalInformation[orderOfStudent][2] = studentName;
//	  		  	totalInformation[orderOfStudent][2] = eachStudentInformation[2];
	  		  	for (int numberOfTask = 0; numberOfTask < eachTaskResult.length; numberOfTask++) {
	  		  		if(eachTaskResult[numberOfTask] != null) {
		  		  		Scanner scanner = new Scanner(eachTaskResult[numberOfTask]);
		  		  		scanner.nextLine();
					  	for(int i = 0; i <= orderOfStudent; i++){
				  		  	str = scanner.nextLine();
				  		}
					  	eachStudentInformation = str.split(",");
			  		  	totalInformation[orderOfStudent][4*(numberOfTask+1)-1] = eachStudentInformation[3];
			  		  	totalInformation[orderOfStudent][4*(numberOfTask+1)] = eachStudentInformation[7];
			  		  	totalInformation[orderOfStudent][4*(numberOfTask+1)+1] = eachStudentInformation[4];
			  		  	totalInformation[orderOfStudent][4*(1+numberOfTask)+2] = eachStudentInformation[5];
	  		  		}
				}
	  		  	double totalGrade = 0;
	  		  	int totalComment = 0;//the total grade of comment
	  		  	double totalCode = 0;// the total grade of comparing results
	  		  	for (int task = 0; task < eachTaskResult.length; task++) {
	  		  		totalGrade += Double.valueOf(totalInformation[orderOfStudent][4*(task+1)-1]);
	  		  		totalComment += Integer.valueOf(totalInformation[orderOfStudent][4*(task+1)+1]);
	  		  		totalCode += Double.valueOf(totalInformation[orderOfStudent][4*(task+1)+2]);
				}
	  		  	totalInformation[orderOfStudent][3+4*eachTaskResult.length] = String.valueOf(totalGrade);
	  		  	totalInformation[orderOfStudent][4+4*eachTaskResult.length] = String.valueOf(totalComment);
	  		  	totalInformation[orderOfStudent][5+4*eachTaskResult.length] = String.valueOf(totalCode);
	  		  	totalInformation[orderOfStudent][6+4*eachTaskResult.length] = String.valueOf(orderOfStudent+1);
	  		  	
	  		  	
	  		  	
				for (int i = 0; i < totalInformation[orderOfStudent].length-1; i++) {
				csvWriter.append(totalInformation[orderOfStudent][i]);
				csvWriter.append(",");				
				}
				csvWriter.append(totalInformation[orderOfStudent][totalInformation[orderOfStudent].length-1]);
				csvWriter.append("\n");
			}
			csvWriter.close();	
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static int getASNumber (File[]ASs){
		int number = 0;
		for (int ASnumber = 0; ASnumber < ASs.length; ASnumber++) {
			if (ASs[ASnumber].getName().indexOf("newfile") == -1) {
				number++;
			}
		}
		return number;
	}
	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	public static String findIDNumber(String studentInformation){
		String[] studentInformationParts = studentInformation.split("_");
		String studentIDNumber = studentInformationParts[2];

		return studentIDNumber;
	}
	public static String findName(String studentInformation){
		String[] studentInformationParts = studentInformation.split("_");
		String studentName = studentInformationParts[0]+studentInformationParts[1] ;
		return studentName;
	}
	public static String findClass(String standardAnswerName){
		int index = standardAnswerName.lastIndexOf("_");
		String classInformation = standardAnswerName.substring(0,index) ;
		return classInformation;
	}
	public static String[] getGradingTask() {
//		for(int i = 0; i < gradingTask.length; i++) {
//			System.out.println(gradingTask[i]);
//		}
		return gradingTask;
	}
}
