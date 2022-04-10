package com.example.demo;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.example.demo.models.Transaction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import cn.com.xuxiaowei.utils.rsa.Rsa;

@SpringBootApplication
@RestController
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@PostMapping(value = "/",consumes ={MediaType.APPLICATION_JSON_VALUE})
	public String encryptInfo(@RequestBody String t) throws InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException 
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
		RestTemplate restTemplate = new RestTemplate();
   		HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request =  new HttpEntity<String>(encryptedJson, headers);
		String res=restTemplate.postForObject("http://localhost:8080/store", request, String.class);
		return res;
	}
	@PostMapping(value = "/store",consumes = {MediaType.APPLICATION_JSON_VALUE})
	public String storeInfo(@RequestBody String t) throws SQLException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, ClassNotFoundException
	{
		GsonBuilder builder = new GsonBuilder(); 
		builder.setPrettyPrinting(); 
		
		Gson gson = builder.create(); 
		Transaction temp=gson.fromJson(t, Transaction.class);
		Class.forName("org.hsqldb.jdbc.JDBCDriver");
		Connection conn=DriverManager.getConnection("http://localhost:3306/demo", "root", "root");
		Statement st=conn.createStatement();
		Rsa.StringKey stringKey = Rsa.getStringKey();
		String privateKey = stringKey.getPrivateKey();
		temp.setAccountFrom(Rsa.privateKeyDecrypt(privateKey, temp.getAccountFrom()));
		temp.setAccountNumber(Rsa.privateKeyDecrypt(privateKey, temp.getAccountNumber()));
		temp.setAmount(Rsa.privateKeyDecrypt(privateKey, temp.getAmount()));
		temp.setCurrency(Rsa.privateKeyDecrypt(privateKey, temp.getCurrency()));
		temp.setType(Rsa.privateKeyDecrypt(privateKey, temp.getType()));

		String query=String.format("INSERT INTO Transactions value(%s,%s,%s,%s,%s",temp.getAccountNumber(),temp.getType(),temp.getAmount(),temp.getCurrency(),temp.getAccountFrom());
		boolean resultQuery=st.execute(query);
		if(!resultQuery)
		{
			return "Unsuccessful Insertion";
		}
		return "Successful Insertion";
	}
}
