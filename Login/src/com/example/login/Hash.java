package com.example.login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
	String stringToHash;
	byte[] hash;
	MessageDigest md;
	Hash(String Input){
		stringToHash=Input;
		try {
			md = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hash = md.digest(stringToHash.getBytes());
	}
	public String getHash() {
		// TODO Auto-generated method stub
	    StringBuffer result = new StringBuffer();
	    char[] digits = {'0', '1', '2', '3', '4','5','6','7','8','9','a','b','c','d','e','f'};
	    for (int idx = 0; idx < hash.length; ++idx) {
	      byte b = hash[idx];
	      result.append( digits[ (b&0xf0) >> 4 ] );
	      result.append( digits[ b&0x0f] );
	    }
	    return result.toString();
		
	}
	
	
	
	
	
	
}
