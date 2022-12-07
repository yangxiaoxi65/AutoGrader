package AutoGrader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private JFrame mainForm = new JFrame("Auto Grader");
	private JLabel label1 = new JLabel("Answer File: ");
	private JLabel label2 = new JLabel("AS File Location: ");
	private JLabel label3 = new JLabel("Result Location: ");
	public static JTextField sourcefile = new JTextField();
	public static JTextField targetfile = new JTextField();
	public static JTextField resultfile = new JTextField();
	String space = "no";
	String answerFilePath = "";
	String ASFilePath = "";
	public String resultFilePath = "";
	public static JButton buttonBrowseSource = new JButton("browse");
	public static JButton buttonBrowseTarget = new JButton("browse");
	public static JButton buttonBrowseResult = new JButton("browse");
	public static JButton buttonEncrypt = new JButton("start");
	private JProgressBar processBar = new JProgressBar();;
	static int processValue = 0;
	static String processString = "";
	static String finishedFile = "";
	public static JTextArea txt = new JTextArea();
	public static JScrollPane jsp = new JScrollPane();
	static long totalStartTime = 0;

	public GUI() {
		Container container = mainForm.getContentPane();
		mainForm.setSize(400, 550);
		mainForm.setLocationRelativeTo(null);
		mainForm.setResizable(false);
		// mainForm.setLayout(null);
		mainForm.setVisible(true);
		label1.setBounds(30, 10, 300, 30);
		sourcefile.setBounds(50, 50, 200, 30);
		buttonBrowseSource.setBounds(270, 50, 80, 30);
		label2.setBounds(30, 90, 300, 30);
		targetfile.setBounds(50, 130, 200, 30);
		buttonBrowseTarget.setBounds(270, 130, 80, 30);
		label3.setBounds(30, 170, 300, 30);
		resultfile.setBounds(50, 210, 200, 30);
		buttonBrowseResult.setBounds(270, 210, 80, 30);
		buttonEncrypt.setBounds(270, 270, 80, 30);
		buttonBrowseSource.addActionListener(new BrowseAction());
		buttonBrowseTarget.addActionListener(new BrowseAction());
		buttonBrowseResult.addActionListener(new BrowseAction());
		buttonEncrypt.addActionListener(new MainAutoG());// invoke MainAutoG
		sourcefile.setEditable(false);
		targetfile.setEditable(false);
		resultfile.setEditable(false);
		ButtonGroup spaceGrip = new ButtonGroup();// �����Ա��飬��ֻ֤��ѡһ��

		txt = new JTextArea(10, 10);
		jsp = new JScrollPane(txt);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.setBounds(50, 330, 300, 150);// �����һ��
		txt.setEditable(false);
		// txt.append("Finished Tasks");

		processBar.setStringPainted(true);
		processBar.setBounds(50, 270, 200, 30);
		processBar.setValue(0);
		processBar.setString("Progress Bar");

		container.add(label1);
		container.add(label2);
		container.add(label3);
		container.add(sourcefile);
		container.add(targetfile);
		container.add(resultfile);
		container.add(buttonBrowseSource);
		container.add(buttonBrowseTarget);
		container.add(buttonBrowseResult);
		container.add(buttonEncrypt);
		container.add(processBar);
		container.add(jsp);
		mainForm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// create thread to reveal process
		new Thread() {

			public void run() {
				while (true) {
					try {
						Thread.sleep(100); // sleep 0.1ms
					} catch (InterruptedException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					Dimension d = processBar.getSize();
					Rectangle rect = new Rectangle(0, 0, d.width, d.height);
					// Rectangle rect = new Rectangle();
					processBar.setString(processString); // set process value
					if (processValue == 100)
						break;
					if (processValue == 0)
						processBar.setString("Progress Bar");
					processBar.paintImmediately(rect);
				}
				processBar.setString("Progress Bar: Complete!");// when finish, display "finish"
			}
		}.start(); // start process bar
	}

	public class MainAutoG implements ActionListener {
		private int txtLength;

		@Override
		public void actionPerformed(ActionEvent t) {
			try {
//		        System.out.println(space);
				totalStartTime = System.currentTimeMillis();
				// preparation
				long prepatationStartTime = System.currentTimeMillis();
				// GUI userInterface
				// Step1: Reach the ASs file location;
				@SuppressWarnings("resource")
				String path = ASFilePath;
				// ASs stores all the assignments of all the students in a class
				File[] ASs = readFile(path);
				// Alphabetical order by student name
				Arrays.sort(ASs);
//    	        String studentAnswer;
				// Step2: read and get the correct answer entered by Prof. and input(if exist);
				String studentResultPath = resultFilePath;
				List<String> taskNames = Grading.taskName(answerFilePath);
				List<Map> standardAnswer = Grading.readStandardAnswer(answerFilePath);
				Map<Integer, List<String>> score = Grading.readScore(answerFilePath);
				Map<Integer, List<String>> spaceRequirement = Grading.readSpace(answerFilePath);
				Map<Integer, List<String>> caseRequirement = Grading.readCase(answerFilePath);
				List[] standardAnswerInput = new List[standardAnswer.size()];
				List[] standardAnswerOutput = new List[standardAnswer.size()];

				for (int i = 0; i < standardAnswer.size(); i++) {
					standardAnswerInput[i] = (List) standardAnswer.get(i).keySet().toArray()[0];
					standardAnswerOutput[i] = (List) standardAnswer.get(i).values().toArray()[0];
				}
				int totalExample = 0;
				for (List item : standardAnswerInput) {
					totalExample += item.size();
//			    	System.err.println(item.size());
//			    	System.err.println(totalExample);
				}

				// get the number of tasks in this AS
				int taskNumber = Grading.readStandardAnswer(answerFilePath).size();
				File[] eachTaskResult = new File[taskNumber];
				// get the number of students in a class
				int studentNumber = getStudentNumber(ASs);
				int totalTask = studentNumber * taskNumber;
				processString = "Progress Bar: " + 0 + " / " + totalTask;
				processBar.setString(processString);
				// grade all students' ASs by tasks �ĵ�����
				File[][] tasks = new File[taskNumber][studentNumber];
				// order ASs into array "tasks"
				for (int task = 0; task < taskNumber; task++) {
					for (int student = 0; student < studentNumber; student++) {
						File[] eachStudentFile = ASs[student].listFiles();// tasks of one student
						// avoid missing tasks
						File emptyFile = new File("emptyFile.java");
						tasks[task][student] = emptyFile;
						int eachTaskNumber = 0;
						int lastWordNumber = 0;
						for (File eachTask : eachStudentFile) {
							String eachTaskName = eachStudentFile[eachTaskNumber].getName().replaceAll(" ", "").toLowerCase().trim();
							String noSpacePath = eachStudentFile[eachTaskNumber].getPath().replaceAll(" ", "");
							System.out.println(noSpacePath);
							File noSpaceFile = new File(noSpacePath);
							String standardName = taskNames.get(task).toLowerCase().trim();
							if (standardName.indexOf(eachTaskName) != -1){
//								System.out.print(eachStudentFile[eachTaskNumber].getPath());
								eachStudentFile[eachTaskNumber].renameTo(noSpaceFile);
//								System.out.print(eachStudentFile[eachTaskNumber].getPath());
//								tasks[task][student] = eachStudentFile[eachTaskNumber];
								tasks[task][student] = eachStudentFile[eachTaskNumber];
								lastWordNumber++;
							}
							eachTaskNumber++;
							if (lastWordNumber == taskNames.size())
								break;
						}
					}
				}

				// create a new file in the current file to store the edited student file
				File directory = new File(".");
				String currentPath = null;
				try {
					currentPath = directory.getCanonicalPath();// ��ȡ��ǰ·��
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				File dirSSS = new File(currentPath+"\\src");
				// 文件对象创建后，指定的文件或目录不一定物理上存在
				if(!dirSSS.exists()) {       //A
					dirSSS.mkdir();          //B
				}

				currentPath += "\\src\\" + findLastWord(path);
				// 注意看这里 src 是写死的 说明你们默认在根目录下有这个 src 文件夹
				// 但是我这里没有 好吧 我自己新建一个 看着
				// 建好了 现在我运行给你看
				File file = new File(currentPath);
		        System.out.println("****216   GUI.java  "+currentPath);

				// create file
				if (file.exists()) {
					file.delete();
				}
				file.mkdir();
				for (int fileNumber = 0; fileNumber < studentNumber; fileNumber++) {
					String fileName = findLastWord(ASs[fileNumber].toString());
					String filePath = currentPath + "\\" + fileName;
					File stdFile = new File(filePath);
//			        System.out.println(filePath);
					if (stdFile.exists()) {
						stdFile.delete();
					}
					stdFile.mkdir();
					for (int taskNum = 0; taskNum < taskNumber; taskNum++) {
						String taskName = tasks[taskNum][fileNumber].getName();
						String taskPath = filePath + "\\" + taskName;
						File taskFile = new File(taskPath);
						if (taskFile.exists()) {
							taskFile.delete();
						}
						taskFile.mkdir();
					}
				}
				long prepatationEndTime = System.currentTimeMillis();

//		        System.out.println("Preparation time: " + (prepatationEndTime-prepatationStartTime));
//		        System.out.println("Preprocessing time:");
				long commentGradeTime = 0;
				long editFileTime = 0;
				long gettingAnswerTime = 0;
				long gradingTime = 0;

				// start grading each task in this AS
				for (int numberOfTask = 0; numberOfTask < taskNumber; numberOfTask++) {
					File[] eachTask = tasks[numberOfTask];
					// get comment grade for this task
					long commentGradeStartTime = System.currentTimeMillis();
					int[] studentsCommentsGrade = new int[studentNumber];
					studentsCommentsGrade = Preprocessing.getCodeCommentsGrade(eachTask);
//					for(int grade: studentsCommentsGrade) {
//						System.out.println(grade);
//					}
					String[] stdPreprocessingComments = new String[studentNumber];
					stdPreprocessingComments = Preprocessing.getPreprocessingComments(eachTask);
//					for(String comment: stdPreprocessingComments) {
//						System.out.println(comment);
//					}
					long commentGradeEndTime = System.currentTimeMillis();
					commentGradeTime += (commentGradeEndTime - commentGradeStartTime);

					// check whether this task has input or not
					if (standardAnswerInput[numberOfTask] != null) {
						// get students' outputs for this task
						String[][] studentOutput = new String[studentNumber][standardAnswerOutput[numberOfTask].size()];
						for (int orderOfStudent = 0; orderOfStudent < studentNumber; orderOfStudent++) {
							String studentInformation = ASs[orderOfStudent].toString().substring(0,
									ASs[orderOfStudent].toString().length());
							path = currentPath + "\\" + findLastWord(studentInformation);
//							System.out.println(path);
							// create new .java file in the same location as student file
							String location = findLastWord(path);
							// Grade a certain task student by student
							long editFileStartTime = System.currentTimeMillis();
							File newFile = Preprocessing.editFile(eachTask[orderOfStudent], path, numberOfTask,
									location);
							long editFileEndTime = System.currentTimeMillis();
							editFileTime += (editFileEndTime - editFileStartTime);
							// compile this .java file into .class file
							String loc = findLastTwoWords(path) + "/" + eachTask[orderOfStudent].getName();
							long gettingAnswerStartTime = System.currentTimeMillis();
							File[] classes = eachTask[orderOfStudent].listFiles();
							rrunProcess("javac -cp src src/" + loc + "/ZZZnewfile" + numberOfTask + ".java");
//							Robot delay = new Robot();
							// delay.delay(100);
							// test .class file using different inputs in one student

							for (int example = 0; example < standardAnswerInput[numberOfTask].size(); example++) {
								String inp = (String) standardAnswerInput[numberOfTask].get(example);
								// System.out.print(inp);
								String studentAnswer = "";
								String cmd = "java -cp src " + loc + "/ZZZnewfile" + numberOfTask + " " + inp;
								//It will be interrupted after 6s!!!!!
								ExecutorService executor = Executors.newSingleThreadExecutor();
								Future future = executor.submit(new RunProcess(cmd));
								try {
									future.get(6, TimeUnit.SECONDS); // Set the time out of the prime no. search task
									studentAnswer = RunProcess.result;
									executor.shutdown();
								} catch (TimeoutException e) {
									executor.shutdown();
									studentAnswer = "TIME OUT!!!!";
								}

								executor.shutdownNow();
//								studentAnswer = runProcess("java -cp src " + loc + "/ZZZnewfile" + numberOfTask + " " + inp);
								System.out.println(orderOfStudent + "######" + studentAnswer);
								// delay.delay(100);
								studentOutput[orderOfStudent][example] = studentAnswer;// this taskNumber should be
																						// changed to student number
								processValue += 100 / ((studentNumber) * totalExample);
							}
							long gettingAnswerEndTime = System.currentTimeMillis();
							gettingAnswerTime += (gettingAnswerEndTime - gettingAnswerStartTime);
						}
						long gradingStartTime = System.currentTimeMillis();
						// change
						eachTaskResult[numberOfTask] = Grading.getStudentResults(studentResultPath, ASs, eachTask,
								standardAnswerOutput[numberOfTask], studentsCommentsGrade, stdPreprocessingComments,
								studentOutput, findLastWord(answerFilePath), numberOfTask, score.get(numberOfTask),
								spaceRequirement.get(numberOfTask), caseRequirement.get(numberOfTask));

						long gradingEndTime = System.currentTimeMillis();
						gradingTime += (gradingEndTime - gradingStartTime);
						int finishedTask = (numberOfTask + 1) * studentNumber;
						processString = "Progress Bar: " + finishedTask + " / " + totalTask;
						processBar.setString(processString);
						String[] finished = Grading.getGradingTask();
						txt.append("Task" + (numberOfTask + 1) + " finished!\n");
						txt.update(txt.getGraphics());
						for (int finishedNumber = 0; finishedNumber < finished.length; finishedNumber++) {
							finishedFile = finished[finishedNumber];
							txt.append(finishedFile + "\n");
							jsp.getVerticalScrollBar().setValue(numberOfTask * 48);
							// System.out.println(jsp.getVerticalScrollBar().getValue());

							txt.update(txt.getGraphics());
							jsp.update(jsp.getGraphics());
						}
						// û��input���������⣡����
					} else {
						String[] studentOutput = new String[studentNumber];
						for (int orderOfStudent = 0; orderOfStudent < studentNumber; orderOfStudent++) {
							String fileName = eachTask[orderOfStudent].getName();
							String name = fileName.substring(0, fileName.length() - 5);
							String location = findLastWord(path);
							rrunProcess("javac -cp src src/" + location + "/" + fileName);
//							String studentAnswer = runProcess("java -cp src " + location + "/" + name);
//							studentOutput[orderOfStudent] = studentAnswer;
						}
						// change
						eachTaskResult[numberOfTask] = Grading.getStudentResults2(studentResultPath, eachTask,
								standardAnswerOutput[numberOfTask], studentsCommentsGrade, stdPreprocessingComments,
								studentOutput);

					}
				}
				long gradingStartTime = System.currentTimeMillis();
				Grading.getResultTotalFile(studentResultPath, ASs, eachTaskResult, studentNumber,
						findLastWord(answerFilePath));
				long gradingEndTime = System.currentTimeMillis();
				gradingTime += (gradingEndTime - gradingStartTime);
//				System.out.println("Grading comment time: " + commentGradeTime);
//				System.out.println("Editing file time: " + editFileTime);
//				System.out.println("Getting answer time: " + gettingAnswerTime);
//				System.out.println("Grading time: " + gradingTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			processValue = 100;

			long totalEndTime = System.currentTimeMillis();
//	        System.out.println("Total time: " + (totalEndTime-totalStartTime));
		}

	}

	// click "browse" and choose the file
	public class BrowseAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(GUI.buttonBrowseSource)) {
				JFileChooser fcDlg = new JFileChooser();
				fcDlg.setDialogTitle("Answer File...");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("(*.txt;*.kcd;*.csv)", "txt", "kcd",
						"csv");
				fcDlg.setFileFilter(filter);
				int returnVal = fcDlg.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String filepath = fcDlg.getSelectedFile().getPath();
					GUI.sourcefile.setText(filepath);
					String resultPath = filepath.substring(0, filepath.length() - findLastWord(filepath).length() - 1);
					GUI.resultfile.setText(resultPath);
					resultFilePath = resultPath;
					answerFilePath = filepath;
				}
			} else if (e.getSource().equals(GUI.buttonBrowseTarget)) {
				JFileChooser fcDlg = new JFileChooser();
				fcDlg.setDialogTitle("AS File");
				fcDlg.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fcDlg.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String filepath = fcDlg.getSelectedFile().getPath();
					GUI.targetfile.setText(filepath);
					ASFilePath = filepath;
				}
			} else if (e.getSource().equals(GUI.buttonBrowseResult)) {
				JFileChooser fcDlg = new JFileChooser();
				fcDlg.setDialogTitle("Result File");
				fcDlg.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fcDlg.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String filepath = fcDlg.getSelectedFile().getPath();
					GUI.resultfile.setText(filepath);
					resultFilePath = filepath;
				}
			}
		}
	}

	public static String printLines(String cmd, InputStream ins) throws Exception {
		String line = "";
		BufferedReader in = new BufferedReader(new InputStreamReader(ins));
//	    while ((line = in.readLine()) != null) {
//	    	return line;
//	    }
		String add = in.readLine();
		while (add != null) {
			line += add;
			add = in.readLine();
		}
		return line;
	}

	// transmit the input to student's tasks and get the result
	public static String rrunProcess(String command) throws Exception {
		Process pro = Runtime.getRuntime().exec(command);
		String studentAnswer = printLines(command, pro.getInputStream());
		pro.waitFor();
		return studentAnswer;
	}

	public static File[] readFile(String path) throws Exception {
		File file = new File(path);
		File[] files = file.listFiles();
		Arrays.sort(files);
		for (int i = 0; i < files.length; i++) {
			if (!files[i].isFile()) {
				files[i].delete();
			}
		}
		return files;
	}

	// find the location where tasks put in(each student's file)
	public static String findLastWord(String line) {
		String word = "";
		line = line.trim();
		int k = 0;
		while (k < line.length()) {
			String ww = line.substring(k, k + 1);
			if (ww.equals("\\") == true) {
				word = "";
				k++;
			}
			ww = line.substring(k, k + 1);
			word += ww;
			k++;
		}
		// System.out.println(word);
		return word;
	}

	// find the location where tasks put in(each student's file)
	public static String findLastTwoWords(String line) {
		String word = "";
		line = line.trim();
		int k = 0;
		String preWord = "";
		while (k < line.length()) {
			String ww = line.substring(k, k + 1);
			if (ww.equals("\\") == true) {
				preWord = word;
				word = "";
				k++;
			}
			ww = line.substring(k, k + 1);
			word += ww;
			k++;
		}
		// System.out.println(word);
		return preWord + "/" + word;
	}


	// get the original number of students' task in the file(after editing, the
	// number of file will increase)
	public static int getStudentNumber(File[] ASs) {
		int studentNumber = 0;
		for (int number = 0; number < ASs.length; number++) {
			if (ASs[number].getName().indexOf("newfile") == -1) {
				studentNumber++;
//				System.out.println("!!!!!!!!!!!!"+ASs[number].getName());
			}
		}
		return studentNumber;
	}

	public static void main(String[] args) {
		GUI JPBD = new GUI();
//		JPBD.setVisible(true);
		// new GUI();

	}

}

class RunProcess implements Runnable {
	private final String command;
	public static String result = "";

	RunProcess(String command) {
		this.command = command;
	}


	public void run() {
//		System.out.println("Started..");
		try {
			result = runProcess(command);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("Finished!");
	}

//transmit the input to student's tasks and get the result
	private static String runProcess(String command) throws Exception {
		Process pro = Runtime.getRuntime().exec(command);
		String studentAnswer = printLines(command, pro.getInputStream());
		pro.waitFor();
		return studentAnswer;
	}

	public static String printLines(String cmd, InputStream ins) throws Exception {
		String line = "";
		BufferedReader in = new BufferedReader(new InputStreamReader(ins));
//	    while ((line = in.readLine()) != null) {
//	    	return line;
//	    }
		String add = in.readLine();
		while (add != null) {
			line += add;
			add = in.readLine();
		}
		return line;
	}


}
