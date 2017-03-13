package courseProject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class Customer {
	public static void main(String args[]) throws Exception{
		String inFromPSystem;
		String psysyemPu = getCurrentDirectory().concat("/psystem/public.key");
		String bankPu = getCurrentDirectory().concat("/bank/public.key");
		try{
			Socket customerSocket = new Socket("localhost",1234);
			BufferedReader customerInFromPS = new BufferedReader(new InputStreamReader(customerSocket.getInputStream()));
			BufferedReader inputFromUser = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter outPToPS = new PrintWriter(customerSocket.getOutputStream(),true);
			DataOutputStream customerOutToPS = new DataOutputStream(customerSocket.getOutputStream());
			DataInputStream inFromPS = new DataInputStream(customerSocket.getInputStream());
			OutputStream out = (customerSocket.getOutputStream());
			//InputStream in = (customerSocket.getInputStream());
			outPToPS.println("connect");
			inFromPSystem=customerInFromPS.readLine();
			if(inFromPSystem.equalsIgnoreCase("up")){
				System.out.print("Please enter Username: ");
				String username=inputFromUser.readLine();
				System.out.print("Please enter Password: ");
				String password=inputFromUser.readLine();
				outPToPS.println("updata");
				outPToPS.println("Username:"+username+" Password:"+password);
				while((inFromPSystem=customerInFromPS.readLine()).equalsIgnoreCase("error")){
					System.out.println("the password is incorrect");
					System.out.print("Please enter Password: ");
					password=inputFromUser.readLine();
					outPToPS.println(password);
				}
				if(inFromPSystem.equalsIgnoreCase("success")){
					String privatekey = getCurrentDirectory().concat("/customer/".concat(username.concat("_private.key")));
					
					ObjectInputStream KeyReader = new ObjectInputStream(new FileInputStream(privatekey));
				   	final PrivateKey privKey = (PrivateKey) KeyReader.readObject();
					Signature dsa = Signature.getInstance("MD5WithRSA");
					dsa.initSign(privKey);
					
					String itemlist = customerInFromPS.readLine();
					itemlist = itemlist.replace("/n", "\n");
				System.out.println(itemlist);
					
					System.out.println("Please enter the item #:");
					String itemNo = inputFromUser.readLine();
					byte[] itemNoEnc = EncryptionUtil.encrypt(itemNo,psysyemPu);
					dsa.update(itemNoEnc, 0, itemNoEnc.length);
					byte[] realSig = dsa.sign();
					outPToPS.println(realSig.length);
					out.write(realSig, 0, realSig.length);
					outPToPS.println(itemNoEnc.length);
					String str =  customerInFromPS.readLine();
					out.write(itemNoEnc, 0, itemNoEnc.length);
					
				
					
					System.out.println("Please enter the quantity:");
					String quantity = inputFromUser.readLine();
					byte[] quantityenc = EncryptionUtil.encrypt(quantity, psysyemPu);
					dsa.update(quantityenc, 0, itemNoEnc.length);
					byte[] realSig1 = dsa.sign();
					outPToPS.println(realSig1.length);
					str =  customerInFromPS.readLine();
					out.write(realSig1, 0, realSig1.length);
					outPToPS.println(quantityenc.length);
					str = customerInFromPS.readLine();
					out.write(quantityenc, 0, quantityenc.length);
		
					while((customerInFromPS.readLine()).equalsIgnoreCase("quntityError")){
						System.out.println("the quantity is incorrect");
						System.out.println(itemlist);
						System.out.println("Please enter the quantity:");
						quantity = inputFromUser.readLine();
						quantityenc = EncryptionUtil.encrypt(quantity, psysyemPu);
						dsa.update(quantityenc, 0, itemNoEnc.length);
						realSig1 = dsa.sign();
						outPToPS.println(realSig1.length);
						str =  customerInFromPS.readLine();
						out.write(realSig1, 0, realSig1.length);
						outPToPS.println(quantityenc.length);
						str = customerInFromPS.readLine();
						out.write(quantityenc, 0, quantityenc.length);
					}
					
					System.out.print("Please enter Credit Card Number: ");
					String creditCardNumber=inputFromUser.readLine();
					String tobank = username.concat("%").concat(creditCardNumber);
					byte[] tobankb = EncryptionUtil.encrypt(tobank, bankPu);
					dsa.update(tobankb, 0, tobankb.length);
					realSig = dsa.sign();
					outPToPS.println(realSig.length);
					str =  customerInFromPS.readLine();
					out.write(realSig, 0, realSig.length);
					outPToPS.println(tobankb.length);
					str =  customerInFromPS.readLine();
					out.write(tobankb, 0, tobankb.length);;
					
					inFromPSystem=customerInFromPS.readLine();
					if(inFromPSystem.equalsIgnoreCase("itemUpdateSuccess")){
						System.out.println("Payment Successful.");
						System.out.println("We will process your order soon");
					}
					else{
						System.out.println("Wrong credit card number");
					}
				}
			}
			customerSocket.close();
		}
		catch(Exception e){
			System.out.println("ERROR: "+e.getMessage());
		}
	}
	 public static String getCurrentDirectory() {
			String current = System.getProperty("user.dir");
			return current;
		
		}
	 public byte[] getbytes(String str,String key) throws FileNotFoundException, ClassNotFoundException, IOException
	 {
		byte[] encryptedByte = EncryptionUtil.encrypt(str,key);;
		 
		return encryptedByte;
	 }
	 
}
