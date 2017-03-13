package courseProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;

public class Psystem {
	static int itemCount=0;
	static int itemPrice=0;
	static ArrayList<String> items = new ArrayList<String>();
	static ArrayList<String> itemFile = new ArrayList<String>();
	public static void main(String args[]) throws Exception{
		String customerInput;
		String privateKey = getCurrentDirectory().concat("/psystem/private.key");
		String bankPu = getCurrentDirectory().concat("/bank/public.key");
		try{
			ServerSocket psystemServerSocketForCustomer = new ServerSocket(1234);
			System.out.println("Waiting for Client to Connect on port 1234");
			while(true){
				Socket psystemSocketForCustomer = psystemServerSocketForCustomer.accept();
				System.out.println("Client Connected at port 1234");
				//BufferedReader inputFromUser = new BufferedReader(new InputStreamReader(System.in));
				BufferedReader inputFromCustomer = new BufferedReader(new InputStreamReader(psystemSocketForCustomer.getInputStream()));
				PrintWriter outPToCustomer = new PrintWriter(psystemSocketForCustomer.getOutputStream(),true);
				DataOutputStream outdataToCustomer = new DataOutputStream(psystemSocketForCustomer.getOutputStream());
				//OutputStream out = (psystemSocketForCustomer.getOutputStream());
				InputStream in = (psystemSocketForCustomer.getInputStream());
				DataInputStream in1 = new DataInputStream(psystemSocketForCustomer.getInputStream());
				while(true){
					customerInput = inputFromCustomer.readLine();
					if( customerInput.toLowerCase().equals("connect") )
	       			{
						outPToCustomer.println("up");
	       			}
					else if( customerInput.toLowerCase().equals("updata") ){
						String customerData = inputFromCustomer.readLine();
						String username = getUsernameFromS(customerData);
						String password = getPasswordFromS(customerData);
						password = MD5Digest.encryptPassword(password);
						Boolean passwordM=false;
						passwordM = validatePassword(username, password);
						while(!passwordM){
							//System.out.println("Customer: "+username+" "+password);
							outPToCustomer.println("error");
							password = inputFromCustomer.readLine();
							password = MD5Digest.encryptPassword(password);
							passwordM = validatePassword(username, password);
						}
						outPToCustomer.println("success");
						String custPu = getCurrentDirectory().concat("/customer/".concat(username.concat("_public.key")));
						//send item list
						ObjectInputStream KeyReader = new ObjectInputStream(new FileInputStream(custPu));
						final PublicKey publicKey = (PublicKey) KeyReader.readObject();
						Signature sig = Signature.getInstance("MD5WithRSA");
						sig.initVerify(publicKey);
						itemCount=0;
						itemPrice=0;
						items.clear();
						itemFile.clear();
						String itemlist = getItemFromFile();
						outPToCustomer.println(itemlist);
						itemlist = itemlist.replace("/n", "\n");
						//System.out.println(itemCount);
						
						System.out.println("Item List sent");
						//receive item
						int byteCount = Integer.parseInt(inputFromCustomer.readLine());
						byte[] digsign = new byte[byteCount];
						in.read(digsign, 0, byteCount);
						byteCount = Integer.parseInt(inputFromCustomer.readLine());
						outPToCustomer.println("received");
						byte[] itemnoE = new byte[byteCount];
						in.read(itemnoE, 0, byteCount); 
						sig.update(itemnoE, 0, byteCount);
						if(sig.verify(digsign))
						{
							System.out.println("Digital Signature varified");
						}
						String itemNo = EncryptionUtil.decrypt(itemnoE, privateKey);
						
						
						
						byteCount = Integer.parseInt(inputFromCustomer.readLine());
						byte[] digsign1 = new byte[byteCount];
						outPToCustomer.println("received");
						in.read(digsign1, 0, byteCount);
						byteCount = Integer.parseInt(inputFromCustomer.readLine());
						outPToCustomer.println("received");
						byte[] quantityb = new byte[byteCount];
						in.read(quantityb, 0, byteCount);
						sig.update(quantityb, 0, byteCount);
						if(sig.verify(digsign1))
						{
							System.out.println("Digital Signature varified");
						}
						String quantity = EncryptionUtil.decrypt(quantityb, privateKey);
						
						//System.out.println("Customer: "+itemNo+" "+quantity);
						
						Boolean quantityM=false;
						quantityM = verifyQuantiy(itemNo, quantity);
						while(!quantityM){
							outPToCustomer.println("quntityError");
							byteCount = Integer.parseInt(inputFromCustomer.readLine());
							 digsign1 = new byte[byteCount];
							outPToCustomer.println("received");
							in.read(digsign1, 0, byteCount);
							byteCount = Integer.parseInt(inputFromCustomer.readLine());
							outPToCustomer.println("received");
							quantityb = new byte[byteCount];
							in.read(quantityb, 0, byteCount);
							sig.update(quantityb, 0, byteCount);
							if(sig.verify(digsign1))
							{
								System.out.println("Digital Signature varified");
							}
							 quantity = EncryptionUtil.decrypt(quantityb, privateKey);
							quantityM = verifyQuantiy(itemNo, quantity);
						}
						outPToCustomer.println("quntitySuccess");
						
						byteCount =  Integer.parseInt(inputFromCustomer.readLine());
						byte[] digsig2 = new byte[byteCount];
						outPToCustomer.println("received");
						in.read(digsig2, 0, byteCount);
						byteCount = Integer.parseInt(inputFromCustomer.readLine());
						outPToCustomer.println("received");
						byte[] tobank = new byte[byteCount];
						//outPToCustomer.println("received");
						in.read(tobank, 0, byteCount);
						sig.update(tobank, 0, byteCount);
						if(sig.verify(digsig2))
						{
							System.out.println("Digital Signature varified");
						}
						itemPrice = getItemPrice(itemNo);
						//System.out.println(itemPrice);
						System.out.println("Connect to bank Initiated");
						Boolean itemsUpdate=false;
						
						//connect to bank
						//String custName = null;
						//String creditCardNumber = null; 
						//Integer balance = null;
						String toBank = null;
						String fromBank = null;
						Socket pSystemSocket = new Socket("localhost", 6986);	
						BufferedReader inputFromBank = new BufferedReader(new InputStreamReader(pSystemSocket.getInputStream()));
						PrintWriter outputToBank = new PrintWriter(pSystemSocket.getOutputStream(), true);
						OutputStream out1 = (pSystemSocket.getOutputStream());
						toBank = "Initate Transaction.";
						outputToBank.println(toBank);
						boolean transactionStatus=false;
						fromBank = inputFromBank.readLine();
						if(fromBank.equals("Transaction Initiated."))
						{
							
							int byteNo = tobank.length;
							outputToBank.println(byteNo);
							out1.write(tobank, 0, tobank.length);
							
							Integer bal = getItemPrice(itemNo)* Integer.parseInt(quantity);
							byte[] balEnc = EncryptionUtil.encrypt(bal.toString(), bankPu);
							byteNo = balEnc.length;
							outputToBank.println(byteNo);
							String str2 = inputFromBank.readLine();
							out1.write(balEnc, 0, balEnc.length);
							System.out.println("Payment Request Sent to Bank");
							fromBank = inputFromBank.readLine();
							if(fromBank.equals("Success"))
							{
								transactionStatus =true;
								itemsUpdate = true;
								System.out.println("Payment Successful");
							}
						}
						//outputToBank.println("Exit");
						pSystemSocket.close();
						inputFromBank.close();
						//outputToBank.close();
						
						
						
						//notify to customer
						if(itemsUpdate){
							outPToCustomer.println("itemUpdateSuccess");
							itemFileUpdate(itemNo, quantity);
							System.out.println("Order is UnderProcess");
						}
						else{
							outPToCustomer.println("itemUpdateFailed");
						}
						break;
					}
				}
			}
		}
		catch(Exception e){
			System.out.println("ERROR: "+e.getMessage());
		}
	}
	private static int getItemPrice(String itemNo) {
		for(int i=0;i<itemCount;i++){
			if(i==(Integer.parseInt(itemNo)-1)){
				String fileLine = itemFile.get(i);
				String[] line = fileLine.split(", ");
				if(line.length > 2){
				 String pricestr = line[2].substring(1);
				 return Integer.parseInt(pricestr);
				}
			}
		}
		return 0;
	}
	public static String getCurrentDirectory() {
		String current = System.getProperty("user.dir");
		return current;
	
	}
	private static void itemFileUpdate(String itemNo, String quantity) throws Exception{
		try{
			File itemfile = new File(getCurrentDirectory().concat("/psystem/item.txt"));
			if (!itemfile.exists()) {
				itemfile.createNewFile();
			}
			BufferedWriter filebw = new BufferedWriter(new FileWriter(itemfile));
			filebw.write("");
			for(int i=0;i<itemCount;i++){
				String fileLine = itemFile.get(i);
				if(i==(Integer.parseInt(itemNo)-1)){
					String[] line = fileLine.split(", ");
					fileLine="";
					int j=0;
					while(j<line.length){
						if(j>2){
							fileLine +=(Integer.parseInt(line[j])-Integer.parseInt(quantity));
						}
						else{
							fileLine +=line[j]+", ";
						}
						j++;
					}
				}
				filebw.append(fileLine);
				if(i<itemCount-1){
					filebw.append("\r\n");
				}
			}
			System.out.println("Item File Update Successful");
			filebw.close();
		}
		catch(Exception e){
			System.out.println("ERROR: "+e.getMessage());
		}
		
	}

	private static Boolean verifyQuantiy(String itemNo, String quantity) {
		// TODO Auto-generated method stub
		if(Integer.parseInt(itemNo)<=itemCount && Integer.parseInt(quantity)>0){
			int qty = Integer.parseInt(quantity);
			int itemindex = Integer.parseInt(itemNo)-1;
			int itemqty = Integer.parseInt(items.get(itemindex));
			return (qty <= itemqty);
		}
		return false;
	}

	private static String getItemFromFile() throws Exception{
		try{
			File itemfile = new File(getCurrentDirectory().concat("/psystem/item.txt"));
			BufferedReader filebr = new BufferedReader(new FileReader(itemfile));
			String readLine;
			String itemList="";
			while((readLine=filebr.readLine())!= null){
				itemList+=readLine+"/n";
				items.add(getQuantity(readLine));
				itemFile.add(readLine);
				itemCount++;
			}
			filebr.close();
			return itemList;
		}
		catch(Exception e){
			System.out.println("Error: "+e.getMessage());
		}
		return null;
	}

	private static String getQuantity(String readLine) {
		// TODO Auto-generated method stub
		String[] itemParameter = readLine.split(", ");
		if(itemParameter.length<4){
			return null;
		}
		else{
			return itemParameter[3];
		}
	}

	private static boolean validatePassword(String username, String password) throws Exception{
		// TODO Auto-generated method stub
		//System.out.println(username+" "+password);
		Boolean result=false;
		try{
			File file = new File(getCurrentDirectory().concat("/psystem/customerdata.txt"));
			BufferedReader fbr = new BufferedReader(new FileReader(file));
			String readLine="";
			while((readLine=fbr.readLine()) != null){
				String unFromFile = getUsernameFromS(readLine);
				String pwFromFile = getPasswordFromS(readLine);
				if(unFromFile.equals(username) && pwFromFile.equals(password)){
					result = true;
					break;
				}
			}
		}
		catch(Exception e){
			System.out.println("ERROR: "+e.getMessage());
		}
		return result;
	}

	private static String getPasswordFromS(String customerData) {
		// TODO Auto-generated method stub
		String[] up = customerData.split(" ");
		String passwordS = up[1];
		String[] ups = passwordS.split(":");
		if(ups[1]==null) return "";
		return ups[1];
	}

	private static String getUsernameFromS(String customerData) {
		// TODO Auto-generated method stub
		String[] up = customerData.split(" ");
		String usernameS = up[0];
		String[] ups = usernameS.split(":");
		if(ups[1]==null) return "";
		return ups[1];
	}
}
