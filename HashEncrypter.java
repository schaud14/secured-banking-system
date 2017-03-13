package courseProject;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

public class HashEncrypter {
	String publicKey;
	String privateKey;
	void generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException
	{
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		keyGen.initialize(1024, random);
		KeyPair pair = keyGen.generateKeyPair();
		PrivateKey priv = pair.getPrivate();
		PublicKey pub = pair.getPublic();
		this.publicKey = pub.toString();
		this.privateKey = priv.toString();
	}
	public static void main(String args[]) throws NoSuchAlgorithmException, NoSuchProviderException
	{
		HashEncrypter newKeyPair = new HashEncrypter();
		newKeyPair.generateKeyPair();
		newKeyPair.printKeyPair();
	}
	void printKeyPair()
	{
		System.out.println("Private Key: "+ this.privateKey);
		System.out.println("Public Key: "+ this.publicKey);
	}
}
