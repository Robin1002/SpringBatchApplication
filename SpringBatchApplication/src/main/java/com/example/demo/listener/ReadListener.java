package com.example.demo.listener;

import java.util.List;

import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;

import com.example.demo.dto.User;

public class ReadListener implements ItemReadListener<User>{

	@Override
	public void beforeRead() {
		// TODO Auto-generated method stub
		System.out.println("----beforeRead---");
		
	}

	@Override
	public void afterRead(User item) {
		// TODO Auto-generated method stub
		System.out.println("----afterRead---"+item);
		
	}

	@Override
	public void onReadError(Exception ex) {
		// TODO Auto-generated method stub
		System.out.println("----onReadError---"+ex.getMessage());
		
	}
	

	
}
