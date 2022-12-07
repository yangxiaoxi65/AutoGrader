package AutoGrader;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class Preprocessing {
	//get the comment grade: purpose 25 points, //1 25 points, //2 25 points, //3 25 points. total 100 points
	//eachTask means all students' one specific task
	public static int[] getCodeCommentsGrade(File[] eachTask) {
		String line;
		int [] studentCommentsGrade = new int [eachTask.length];
		try {
			for (int orderOfStudent = 0; orderOfStudent < eachTask.length; orderOfStudent++){
				int grade = 0;
				boolean[] check = new boolean[4];
				File[] classes = eachTask[orderOfStudent].listFiles();
				if(classes != null) {
					for(File classNum:classes) {
						if(classNum.getName().indexOf(".DS_Store") == -1) {
							boolean findMain = false;
							Scanner scanner = new Scanner(classNum);
							while (scanner.hasNextLine()){
								line = scanner.nextLine().replaceAll(" ","");
								if(line != null) {
									if(line.toLowerCase().indexOf("voidmain(string[]")!=-1) {
										findMain = true;
										Scanner scanAgain = new Scanner(classNum);
										boolean publicClassAppear = false;
										while (scanAgain.hasNextLine()){
											String mainLine = scanAgain.nextLine();
											if(line != null) {
												//purpose should appear before public class
												if(mainLine.replaceAll(" ","").toLowerCase().indexOf("publicclass") != -1) {
													publicClassAppear = true;
												}

												if((mainLine.toLowerCase().indexOf("//")!=-1 || mainLine.toLowerCase().indexOf("/*")!=-1) && mainLine.toLowerCase().indexOf("purpose")!=-1 && !publicClassAppear) {
													check[0] = true;
													/* System.out.println(classNum.getName()); */
												}
												else if((mainLine.toLowerCase().indexOf("//")!=-1 || mainLine.toLowerCase().indexOf("/*")!=-1) && mainLine.toLowerCase().indexOf("1")!=-1) {
													check[1] = true;
												}
												else if((mainLine.toLowerCase().indexOf("//")!=-1 || mainLine.toLowerCase().indexOf("/*")!=-1) && mainLine.toLowerCase().indexOf("2")!=-1) {
													check[2] = true;
												}
												else if((mainLine.toLowerCase().indexOf("//")!=-1 || mainLine.toLowerCase().indexOf("/*")!=-1)  && mainLine.toLowerCase().indexOf("3")!=-1) {
													check[3] = true;
												}
											}
										}
									}
								}
							}
							if(findMain) break;
						}
					}
				}
				for(int i = 0; i < 4; i++) {
					if(check[i])
						grade += 25;
				}
				studentCommentsGrade[orderOfStudent] = grade;
			}
			return studentCommentsGrade;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//change get the comment of preprocessing grade when losing points
	public static String[] getPreprocessingComments(File[] eachTask) {
		String line;
		String [] commentsTotal = new String [eachTask.length];
		try {
			for (int orderOfStudent = 0; orderOfStudent < eachTask.length; orderOfStudent++){
				String comment = "";
				boolean[] check = new boolean[4];
				File[] classes = eachTask[orderOfStudent].listFiles();
				if(classes != null) {
					for(File classNum:classes) {
						if(classNum.getName().indexOf(".DS_Store") == -1) {
							boolean findMain = false;
							Scanner scanner = new Scanner(classNum);
							while (scanner.hasNextLine()){
								line = scanner.nextLine().replaceAll(" ","");
								if(line != null) {
									if(line.toLowerCase().indexOf("voidmain(string[]")!=-1) {
										findMain = true;
										Scanner scanAgain = new Scanner(classNum);
										boolean publicClassAppear = false;
										while (scanAgain.hasNextLine()){
											String mainLine = scanAgain.nextLine();
											if(mainLine != null) {
												//purpose should appear before public class
												if(mainLine.replaceAll(" ","").toLowerCase().indexOf("publicclass") != -1) {
													publicClassAppear = true;
												}

												if((mainLine.toLowerCase().indexOf("//")!=-1 || mainLine.toLowerCase().indexOf("/*")!=-1) && mainLine.toLowerCase().indexOf("purpose")!=-1 && !publicClassAppear) {
													check[0] = true;
												}
												else if((mainLine.toLowerCase().indexOf("//")!=-1 || mainLine.toLowerCase().indexOf("/*")!=-1) && mainLine.toLowerCase().indexOf("1")!=-1) {
													check[1] = true;
												}
												else if((mainLine.toLowerCase().indexOf("//")!=-1 || mainLine.toLowerCase().indexOf("/*")!=-1) && mainLine.toLowerCase().indexOf("2")!=-1) {
													check[2] = true;
												}
												else if((mainLine.toLowerCase().indexOf("//")!=-1 || mainLine.toLowerCase().indexOf("/*")!=-1)  && mainLine.toLowerCase().indexOf("3")!=-1) {
													check[3] = true;
												}
											}
										}
									}
								}
							}
							if(findMain) break;
						}
					}
				}
				int grade = 100;
				String result = "";
				for(int i = 0; i < 4; i++) {
					if(i == 0 && !check[i]) {
						grade -= 25;
						result += " No purpose;";}
					else if(!check[i]) {
						grade -= 25;
						result += " No step" + i +" comment;";}
				}
				if(result == "") {
					result = " No comment points are lost";
				}
				comment = "comment:" + grade + ";" + result;
				if(classes == null) {
					comment = "comment:0; You did not submit this task.";
				}
				commentsTotal[orderOfStudent] = comment;
			}

			return commentsTotal;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	//create a newfile to put the edited code
	public static File editFile(File file, String path, int asNumber, String location) {
		String taskName = file.getName();
		List list =new ArrayList();//put edited code
		try {
			File[] classes = file.listFiles();
			int mainNum = 0;
			int classNumber = 0;
			boolean find = false;
			if(classes != null) {
				for(File classNum:classes) {
					if(classNum.getName().indexOf(".DS_Store") == -1) {
						List otherList =new ArrayList();
		//				System.out.println(classNum.getName());
						Scanner scanner = new Scanner(classNum);
						String loc = findLastTwoWords(path);
						String line="package " + loc + "." + location + "." + taskName + ";";
						otherList.add(line);
						while(scanner.hasNextLine()){
							line = scanner.nextLine().replaceAll(" ","");
							if(line.indexOf("package")!=-1) {
								line="";
							}
							otherList.add(line);
							if(line != null) {
								if(line.toLowerCase().indexOf("voidmain(string[]")!=-1) {
									find = true;
								}
							}
						}
						if(find == true) mainNum = classNumber;
						else {
							String otherClassName = classNum.getName();
							String otherClassPath = path + "\\" + taskName + "\\" + otherClassName;
				        	File otherClassFile = new File(otherClassPath);
				        	OutputStreamWriter osw = null;
				    	    if(otherClassFile.exists()){
				    	        try {
				    	        	otherClassFile.createNewFile();
				    	        } catch (IOException e) {
				    	            // TODO Auto-generated catch block
				    	            e.printStackTrace();
				    	        }
				    	    }
				    	    try {
				    	        osw=new OutputStreamWriter(new FileOutputStream(otherClassFile));
				    	        for(int i=0;i<otherList.size();i++){
				    	        	osw.write((String)otherList.get(i));
				    	        	osw.write("\n");}
				    	    } catch (FileNotFoundException e) {
				    	        // TODO Auto-generated catch block
				    	        e.printStackTrace();
				    	    } catch (IOException e) {
				    	        // TODO Auto-generated catch block
				    	        e.printStackTrace();
				    	    }finally {
				    	        try {
				    	            osw.close();
				    	        } catch (IOException e) {
				    	            // TODO Auto-generated catch block
				    	            e.printStackTrace();
				    	        }
				    	    }


		//		            Files.copy(classNum.toPath(), otherClassFile.toPath());
				        	String locClass = findLastTwoWords(path) + "/" + taskName;
				        	runProcess("javac -cp src src/" + locClass + "/" + otherClassName);
						}
						classNumber++;
					}
				}
				Scanner scan = new Scanner(classes[mainNum]);
				int p=0;//deal with bracket issue
				String scannerName="";// the name to scanner
				String loc = findLastTwoWords(path);
				String packageName="package " + loc + "." + location + "." + taskName + ";";
				list.add(packageName);
			  	while(scan.hasNextLine()){
		  		  	String str=scan.nextLine();//edited code
		  		  	if(str.replaceAll(" ","").toLowerCase().indexOf("voidmain(string[]")!=-1) {
		  		  		list.add("public static void main(String[] args) {");
		  		  		str="for(String str : args){";
		  		  		p=2;
					}
		  		  	if(str.replaceAll(" ","").indexOf("Scanner(System.in)")!=-1) {
		  		  		String str2=str.trim().replaceAll(" ","");
//		  		  		System.out.println(str2);
		  		  		scannerName=str2.substring(7, str2.indexOf("="));
//		  		  		System.out.println(scannerName);
		  		  		scannerName=scannerName.trim()+".";
		  		  	}
		  		  	//delete the System.out.println line, which is not the true answer
		  		  	else if(str.toLowerCase().indexOf("System.out.println")!=-1 &&
		  		  			str.toLowerCase().indexOf("answer")==-1) {
		  		  		str="";
		  		  	}
		  		  	else if(str.toLowerCase().indexOf("// todo auto-generated")!=-1) {
		  		  		str="";
		  		  	}
		  		  	else if(str.toLowerCase().indexOf("input.close()")!=-1) {
		  		  		str="";
		  		  	}

					else if((!scannerName.equals("")) &&(str.indexOf(scannerName)!=-1)) {
						String ff=str.trim();
						String type="";
						if(ff.indexOf(" ")!=-1) {
							type =ff.substring(0,ff.indexOf(" "));
							if(str.indexOf(".nextLong()")!=-1) {
								str=str.replace(scannerName+"nextLong()", "Long.valueOf(str)");
							}
							else if(str.indexOf(".nextInt()")!=-1) {
								str=str.replace(scannerName+"nextInt()", "Integer.parseInt(str)");
							}
							else if(str.indexOf(".next()")!=-1) {
								str = str.replace(scannerName+"next()", "str");
							}
							else if(str.indexOf(".nextDouble()")!=-1) {
								str=str.replace(scannerName+"nextDouble()", "Double.parseDouble(str)");
							}
						}
					}
					else if(str.indexOf("public class")!=-1) {
						String ff=str.trim();//delete "  "
						str=str.replace(str.substring(13, str.indexOf("{")), "ZZZnewfile"+asNumber);
					}
					else if(str.indexOf("package")!=-1) {
						str=" ";
					}

		  		  	if(p>0 && ((str.indexOf("{")!= -1) || (str.indexOf("}")!= -1)) ){
		  		  		for(int u = 0; u < str.length(); u++) {
		  		  			if(str.substring(u, u+1).equals("{"))
		  		  				p++;
		  		  			else if(str.substring(u, u+1).equals("}")) {
		  		  				p--;
		  		  			}
		  		  		}
			  		  	if(p==1) {
			  		  		list.add(str);
	  		  				str = "}";
	  		  		  	}
		  		  	}

		  		  	list.add(str);
				}
				scan.close();
			}
		} catch (Exception e) {
				e.printStackTrace();
		}
		//create a new file to put the edited code
		File newfile=new File(path+"\\"+taskName+"\\ZZZnewfile"+asNumber+".java");
	    OutputStreamWriter osw = null;
	    if(newfile.exists()){
	        try {
	            newfile.createNewFile();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	    try {
//			System.out.println("315:  "+newfile);
//			if (!newfile.exists()) {
//				try {
//					newfile.createNewFile();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				System.out.println("文件未创建,现在才创建");
//			} else {
//				System.out.println("文件已存在");
//			}
	        osw=new OutputStreamWriter(new FileOutputStream(newfile));
	        for(int i=0;i<list.size();i++){
	        	osw.write((String)list.get(i));
	        	osw.write("\n");}
	    } catch (FileNotFoundException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }finally {
	        try {
	            osw.close();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }

			  	return newfile;
	}

	//find the location where tasks put in(each student's file)
	public static String findLastTwoWords(String line){
		String word="";
		line=line.trim();
		int k=0;
		String preWord = "";
		while( k<line.length()) {
		String ww=line.substring(k, k+1);
			if(ww.equals("\\")==true) {
			  preWord = word;
			  word="";
			  k++;
			}
			ww=line.substring(k, k+1);
			word += ww;
			k++;
		}
		//System.out.println(word);
		return preWord;
	}
	public static String runProcess(String command) throws Exception {
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
