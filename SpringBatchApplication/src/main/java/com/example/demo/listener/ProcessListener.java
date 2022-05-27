package com.example.demo.listener;

import java.util.List;

import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Value;

import com.example.demo.dto.User;


public class ProcessListener implements ItemProcessListener<User, User>{

	@Value("#{jobExecution}")
    public JobExecution jobExecution;
	
	@Override
	public void beforeProcess(User item) {
		// TODO Auto-generated method stub
		System.out.println("----beforeProcess---"+item);
		
	}

	@Override
	public void afterProcess(User item, User result) {
		// TODO Auto-generated method stub
		System.out.println("----afterProcess---"+item+"----resulty-----"+result);
		
	}

	@Override
	public void onProcessError(User item, Exception e) {
		// TODO Auto-generated method stub
		System.out.println("----onProcessError---"+item+"-----msg----------"+e.getMessage());
		//System.out.println("---jobExecution---"+JobListener.jobExecution);
		//JobListener.jobExecution.stop();
		//System.exit(1);
		//System.out.println("---jobExecution---"+JobListener.jobExecution);
		//System.out.println("---stopping the job00----");
	}

	
}
