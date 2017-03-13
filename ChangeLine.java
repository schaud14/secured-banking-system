	package courseProject;
	
	import java.io.BufferedReader;
	import java.io.BufferedWriter;
	import java.io.File;
	import java.io.FileReader;
	import java.io.FileWriter;
	import java.io.IOException;
	
	public class ChangeLine {
		public static void main (String argv[]) throws Exception{
		String line;
		String name = "bob";
		String fileName = "balance.txt";
		String path = getCurrentDirectory().concat("/bank/".concat(fileName));
		try {
			File bankFile = new File(path);
			FileReader fileReader = new FileReader(bankFile);
			BufferedReader br = new BufferedReader(fileReader);
			 /*
			 FileWriter fileWriterEn = new FileWriter(bankFile);
			 BufferedWriter bufferedWriterEn = new BufferedWriter(fileWriterEn);
			  */	 
			 while((line = br.readLine()) != null){
				 String[] input = line.split(", ");
				 if (input[0].equals(name))
					 {
					 	System.out.println(input[2]);
					 	input[2] = "100";
					 	System.out.println(line);
					 	String line1 = input[0].concat(", ".concat(input[1].concat(", ").concat(input[2])));
					 	System.out.println(line1);
					 	chanageLine(line1,bankFile);
					 }
				 else
				 {
					 //Do Nothing
				 }
				 
			 }
			 //bankFile.deleteOnExit();
			 br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		public static void chanageLine(String changedLine,File bankFile) throws IOException
		{
			String lineInFile;
			String path = getCurrentDirectory().concat("/bank/".concat("balance1.txt"));
			//FileReader fileReader = new FileReader(bankFile);
			BufferedReader bufferedReader = new BufferedReader(new FileReader(bankFile));
			//String fileName = "balance1.txt";
			
			//File newFile = new File(path);
			
			//FileWriter fileWriter = new FileWriter(newFile);
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(path)));//(new FileWriter(fileName));
			
			String[] changedLineInput = changedLine.split(", ");
			while((lineInFile = bufferedReader.readLine()) != null){
				String[] lineInFileInput = lineInFile.split(", ");
				if (lineInFileInput[0].equals(changedLineInput[0]))
				{
					bufferedWriter.write(changedLine);
					bufferedWriter.write("\r\n");
				}
				else
				{
					bufferedWriter.write(lineInFile);
					bufferedWriter.write("\r\n");
				}
			}
			
			bankFile.deleteOnExit();
			{
				System.out.println("deleted");
			}
//			if (newFile.renameTo(new File(path.concat("balance.txt")))){System.out.println("Renamed");}
//			bufferedWriterEn.close();
//			br.close();
		}
		
		public static String getCurrentDirectory() {
			String current = System.getProperty("user.dir");
			return current;
		}
	}
