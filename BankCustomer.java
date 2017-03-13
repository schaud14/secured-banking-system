package courseProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BankCustomer {
	String customerName;
	int cardNumber;
	int balance;
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public int getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(int cardNumber) {
		this.cardNumber = cardNumber;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	public void loadData(String name) throws IOException
	{
		//BankCustomer customer = new BankCustomer();
		String fileName = "balance.txt";
		String path = getCurrentDirectory().concat("/bank/".concat(fileName));
		try 
		{
			String line = null;
			BufferedReader br = new BufferedReader(new FileReader(path));
			while ((line = br.readLine()) != null)
			{
				String[] splitInfo = line.split(", ");
				if(splitInfo[0].equals(name))
				{
					this.customerName = splitInfo[0];
					this.cardNumber = Integer.parseInt(splitInfo[1]);
					this.balance = Integer.parseInt(splitInfo[2]);
					//System.out.println(this.customerName+", "+this.cardNumber+", "+this.balance);
				}
			}
		br.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		//return customer;
	}
	public static String getCurrentDirectory() {
		String current = System.getProperty("user.dir");
		return current;
	
	}
	public boolean verifyData(String custName, int cardNumber)
	{
		if (this.customerName.equals(custName))
		{
			if(this.cardNumber == cardNumber)
			{
				return true;
			}
		}
		return false;
	}
	public boolean updateBalance(Integer balance) throws IOException
	{
		String line;
		String lineInFile;
		String path = getCurrentDirectory().concat("/bank/".concat("balance1.txt"));
		String path1 = getCurrentDirectory().concat("/bank/".concat("balance.txt"));
		File bankFile = new File(path1);
		File newFile = new File(path);
		try
		{
			BufferedReader bufferedReaderRead = new BufferedReader(new FileReader(bankFile));
			BufferedReader bufferedReaderChange = new BufferedReader(new FileReader(bankFile));
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(path)));
			while((line = bufferedReaderRead.readLine()) != null)
			{
				 String[] input = line.split(", ");
				 if (input[0].equals(this.customerName))
					 {
					 	balance = Integer.parseInt(input[2]) + balance;
					 	input[2] = balance.toString();
					 	String newLine = input[0].concat(", ".concat(input[1].concat(", ").concat(input[2])));
					 	String[] changedLineInput = newLine.split(", ");
						while((lineInFile = bufferedReaderChange.readLine()) != null){
							String[] lineInFileInput = lineInFile.split(", ");
							if (lineInFileInput[0].equals(changedLineInput[0]))
							{
								bufferedWriter.write(newLine);
								bufferedWriter.write("\r\n");
							}
							else
							{
								bufferedWriter.write(lineInFile);
								bufferedWriter.write("\r\n");
							}
						}
						
					 }
				 else
				 {
					 //Do Nothing
				 }
			}
			bufferedReaderRead.close();
			bufferedReaderChange.close();
			bufferedWriter.close();
			
			bankFile.delete();
			if (newFile.renameTo(new File(path1)))
			{
				//System.out.println("Renamed");
			}
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return true;
		
	}
	
	public void printData()
	{
		System.out.println(this.customerName+", "+this.cardNumber+", "+this.balance);
	}
}
