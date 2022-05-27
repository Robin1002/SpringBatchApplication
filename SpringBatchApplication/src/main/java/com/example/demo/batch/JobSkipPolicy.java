package com.example.demo.batch;

import org.springframework.batch.core.step.skip.NonSkippableReadException;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.listener.StepListener;

public class JobSkipPolicy implements SkipPolicy{
	
		@Autowired
		StepListener stepListener;

		@Override
		public boolean shouldSkip(Throwable t, int failedCount) throws SkipLimitExceededException {
		// TODO Auto-generated method stub
			System.out.println("------implementing shouldSkip mthd------failedCount---"+failedCount);
			
			/*
			 * if(t instanceof FlatFileParseException) { FlatFileParseException
			 * flatFileParseException = (FlatFileParseException) t; String message =
			 * flatFileParseException.getMessage() + "-" +
			 * flatFileParseException.getLineNumber();
			 * System.out.println("***************checkingggggggg**********-->"+
			 * flatFileParseException.getInput()); }
			 */
			
			/*
			 * if(t instanceof NonSkippableReadException){
			 * System.out.println("------implementing NonSkippableReadException---------");
			 * return true; }
			 */
			 
			 
			
		//Scenerio ->If there are 100 records and 50 records are failed or are bad records and 
		// in that scenerio you want to fail a job then you can set below condition that failedCount 
		// reaches the limit return false, as returing false it will fail the job
			boolean b=true;
			/*if(failedCount>=2)
			{
				System.out.println("---I an skipping data ----$$$$$$$----------");
				b = false;
			}*/
			
			return b;
		
			//return failedCount>=2?false:true;
		
		//false -> fail the job
		// If you return true, the fault record will not be added and it will continue after that
		//return true;
		}
	
}
