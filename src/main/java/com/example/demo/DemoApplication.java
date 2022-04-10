package com.example.demo;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.example.demo.models.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.simple.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cn.com.xuxiaowei.utils.rsa.Rsa;

@SpringBootApplication
@RestController
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@PostMapping(value = "/",consumes ={MediaType.APPLICATION_JSON_VALUE})
	public void hello(@RequestBody String t) throws InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException 
	{
		GsonBuilder builder = new GsonBuilder(); 
		builder.setPrettyPrinting(); 
		
		Gson gson = builder.create(); 
		Transaction temp=gson.fromJson(t, Transaction.class);
		Rsa.StringKey stringKey = Rsa.getStringKey();
		String publicKey = stringKey.getPublicKey();
		temp.setAccountFrom(Rsa.publicKeyEncrypt(publicKey, temp.getAccountFrom()));
		temp.setAccountNumber(Rsa.publicKeyEncrypt(publicKey, temp.getAccountNumber()));
		temp.setAmount(Rsa.publicKeyEncrypt(publicKey, temp.getAmount()));
		temp.setCurrency(Rsa.publicKeyEncrypt(publicKey, temp.getCurrency()));
		temp.setType(Rsa.publicKeyEncrypt(publicKey, temp.getType()));
		
		String encryptedJson=gson.toJson(temp);
		System.out.println(encryptedJson);
	}
}
