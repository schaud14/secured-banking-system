package courseProject;
import java.net.*;
import java.io.*;

public class Psystem1 
{
	public static void main(String argv[]) throws Exception
	{
		String privateKeyPath = getCurrentDirectory().concat("/bank/private.key");
		String publicKeyPath = getCurrentDirectory().concat("/bank/public.key");
		String custName = "alice";
		String creditCardNumber = "11111111"; 
		Integer balance = 100;
		String toBank = null;
		String fromBank = null;
		Socket pSystemSocket = new Socket("localhost", 6986);	
		BufferedReader inputFromBank = new BufferedReader(new InputStreamReader(pSystemSocket.getInputStream()));
		PrintWriter outputToBank = new PrintWriter(pSystemSocket.getOutputStream(), true);
		OutputStream out1 = (pSystemSocket.getOutputStream());
		//InputStream in1 = (pSystemSocket.getInputStream());
		toBank = "Initate Transaction.";
		outputToBank.println(toBank);
		boolean transactionStatus;
		fromBank = inputFromBank.readLine();
		if(fromBank.equals("Transaction Initiated."))
		{
			toBank = custName.concat("%").concat(creditCardNumber).concat("%".concat(balance.toString()));
			System.out.println(toBank);
			EncryptionUtil EUObj = new EncryptionUtil(publicKeyPath, privateKeyPath);
			byte[] encryptedByte = EUObj.encrypt(toBank,publicKeyPath);
			outputToBank.println(encryptedByte.length);
			out1.write(encryptedByte, 0, encryptedByte.length);
			fromBank = inputFromBank.readLine();
			if(fromBank.equals("Success"))
			{
				transactionStatus =true;
				
			}
		}
		outputToBank.println("Exit");
		pSystemSocket.close();
		inputFromBank.close();
		//outputToBank.close();
		
	}
	  public static String getCurrentDirectory() {
			String current = System.getProperty("user.dir");
			return current;
		
		}
}
