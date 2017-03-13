package courseProject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class EncryptionUtil {
  public static final String ALGORITHM = "RSA";
  File publickey;
  File privatekey;
   
  public EncryptionUtil(String publickey, String privatekey) {
	super();
	this.publickey = new File(publickey);
	this.privatekey = new File(privatekey);
}
/*public static void generateKey() {
    try {
      final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
      keyGen.initialize(1024);
      final KeyPair key = keyGen.generateKeyPair();

      File privateKeyFile = new File(PRIVATE_KEY_FILE);
      File publicKeyFile = new File(PUBLIC_KEY_FILE);

      if (privateKeyFile.getParentFile() != null) {
        privateKeyFile.getParentFile().mkdirs();
      }
      privateKeyFile.createNewFile();

      if (publicKeyFile.getParentFile() != null) {
        publicKeyFile.getParentFile().mkdirs();
      }
      publicKeyFile.createNewFile();

      ObjectOutputStream publicKeyOS = new ObjectOutputStream(
          new FileOutputStream(publicKeyFile));
      publicKeyOS.writeObject(key.getPublic());
      publicKeyOS.close();

      ObjectOutputStream privateKeyOS = new ObjectOutputStream(
          new FileOutputStream(privateKeyFile));
      privateKeyOS.writeObject(key.getPrivate());
      privateKeyOS.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
  */
  public static String getCurrentDirectory() {
		String current = System.getProperty("user.dir");
		return current;
	
	}
  /*
  public static boolean areKeysPresent() {

    File privateKey = new File(PRIVATE_KEY_FILE);
    File publicKey = new File(PUBLIC_KEY_FILE);

    if (privateKey.exists() && publicKey.exists()) {
      return true;
    }
    return false;
  }
*/
  public static  byte[] encrypt(String text, String publicKeyPath) throws FileNotFoundException, IOException, ClassNotFoundException {
	 
    byte[] cipherText = null;
    try {
    	ObjectInputStream KeyReader = new ObjectInputStream(new FileInputStream(publicKeyPath));
   	  final PublicKey publicKey = (PublicKey) KeyReader.readObject();
      final Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      cipherText = cipher.doFinal(text.getBytes("UTF8"));
      KeyReader.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return cipherText;
  }

  public static String decrypt(byte[] text, String privateKeyPath) throws UnsupportedEncodingException {
    byte[] dectyptedText = null;
    try {
    	ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(privateKeyPath));
       final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
      final Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      dectyptedText = cipher.doFinal(text);
      
inputStream.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return new String(dectyptedText,"UTF8");
  }
//
//  public static void main(String[] args) {
//
//    try {
//     /* if (!areKeysPresent()) {
//        generateKey();
//      }*/
//      final String originalText = "Text to be encrypted ";
//      ObjectInputStream inputStream = null;
//
//      // Encrypt the string using the public key
//      inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
//      final PublicKey publicKey = (PublicKey) inputStream.readObject();
//      final byte[] cipherText = encrypt(originalText, publicKey);
//
//      // Decrypt the cipher text using the private key.
//      inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
//      final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
//      final String plainText = decrypt(cipherText, privateKey);
//
//      // Printing the Original, Encrypted and Decrypted Text
//      System.out.println("Original: " + originalText);
//      System.out.println("Encrypted: " +cipherText.toString());
//      System.out.println("Decrypted: " + plainText);
//
//    } catch (Exception e) {
//      e.printStackTrace(); 
//    }
//  }
}