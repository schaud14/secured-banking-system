package courseProject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Digest {
//	String plainText;
//	String cipherText;
//	
	public static String encryptPassword(String plainText) throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(plainText.getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		String cipherText = sb.toString();
		return cipherText;
	}
//	public static void main(String args[]) throws NoSuchAlgorithmException
//	{
//		//MD5Digest md = new MD5Digest();
//		//md.plainText = "5678";
//		//md.encryptPassword();
//		System.out.println(md.cipherText);
//	}
	
	
}