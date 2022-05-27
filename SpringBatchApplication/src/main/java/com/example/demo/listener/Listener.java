package com.example.demo.listener;

import java.util.List;

import org.springframework.batch.core.ItemWriteListener;

import com.example.demo.dto.User;

public class Listener implements ItemWriteListener<User>{
	

	@Override
	public void beforeWrite(List<? extends User> items) {
		// TODO Auto-generated method stub
		System.out.println("----beforeWrite---"+items);
		
	}

	@Override
	public void afterWrite(List<? extends User> items) {
		System.out.println("----afterWrite---"+items);
		
	}

	@Override
	public void onWriteError(Exception exception, List<? extends User> items) {
		// TODO Auto-generated method stub
		System.out.println("----onWriteError---"+items+"-----exception--"+exception.getMessage());
	}
	
	

}
