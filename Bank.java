package courseProject;
import java.net.*;
import java.io.*;

public class Bank {
	public static void main(String argv[]) throws Exception
	{
		String privateKeyPath = getCurrentDirectory().concat("/bank/private.key");
		//String publicKeyPath = getCurrentDirectory().concat("/bank/public.key");
		String custName;
		Integer balance;
		int creditCardNumber = 11111111; 
		//String toPsystem = null;
		String fromPsystem = null;
		ServerSocket BankSocket = new ServerSocket(6986);
		System.out.println("Waiting for Client to Connect on port 4985");
		
		while (true)
		{	
			Socket Connected = BankSocket.accept();
			System.out.println("Client " + Connected.getLocalAddress() + " connected at port " + Connected.getPort());
			PrintWriter outputToPsystem = new PrintWriter(Connected.getOutputStream(), true);
			BufferedReader inputFromPsystem = new BufferedReader(new InputStreamReader(Connected.getInputStream()));
			//OutputStream out = (Connected.getOutputStream());
			 InputStream in = (Connected.getInputStream());
			while(true)
			{	
				fromPsystem = inputFromPsystem.readLine();
				switch(fromPsystem)
				{
					case "Initate Transaction.":
					{
						System.out.println("Transaction Initiated.");
						outputToPsystem.println("Transaction Initiated.");
						
						int numberOfBytes = Integer.parseInt(inputFromPsystem.readLine());
						byte[] EncData = new byte[numberOfBytes];
						in.read(EncData, 0, numberOfBytes);						
						String cardData = EncryptionUtil.decrypt(EncData, privateKeyPath);
						
						numberOfBytes = Integer.parseInt(inputFromPsystem.readLine());
						byte[] EncDatabal = new byte[numberOfBytes];
						outputToPsystem.println("Received");
						in.read(EncDatabal, 0, numberOfBytes);
						balance = Integer.parseInt(EncryptionUtil.decrypt(EncDatabal, privateKeyPath));
						
						String[] custData = cardData.split("%");
						custName = custData[0];
						creditCardNumber = Integer.parseInt(custData[1]);
						
						BankCustomer customer = new BankCustomer();
						customer.loadData(custName);
						if(customer.verifyData(custName, creditCardNumber))
						{
							if(customer.updateBalance(balance))
							{
								System.out.println("Balance Updated for "+custName);
								//customer.loadData(custName);
								//customer.printData();
								outputToPsystem.println("Success");
							}
							else 
							{
								outputToPsystem.println("Fail");
							}
						}
						else
						{
							System.out.println("Wrong customer data");
						}
						break;
					}
					case "Exit":
					{
						//BankSocket.close();
						//inputFromPsystem.close();
						//outputToPsystem.close();
						break;
					}
				}
				break;
		}
	//BankSocket.close();	
	}
}
	public static String getCurrentDirectory() {
		String current = System.getProperty("user.dir");
		return current;
	
	}
	public static boolean changeLine(String changedLine,File bankFile) throws IOException
	{
		
		
		
		
		return false;
		
	}

}
