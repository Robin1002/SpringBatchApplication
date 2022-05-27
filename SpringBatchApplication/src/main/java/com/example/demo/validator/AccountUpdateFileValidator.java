package com.example.demo.validator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.Exception.LengthException;

@Component
public class AccountUpdateFileValidator {

	@Value("${dataFilePath}")
	private String dataFilePath;

	@Value("${filePath}")
	private String filePath;

	@Value("${invalidFilePath}")
	private String invalidFilePath;

	public static boolean lProceedJob = true;

	//file exist or not
	//whthr file name validatn -> length, name pattern, date format
	//empty file->no records
	public boolean isFileExist() throws Exception 
	{
		boolean lIsFileExist = true;
		try {
			File file=new File(dataFilePath);
			String fileName=file.getName();

			if(!file.exists()) {
				lIsFileExist = false;
				//throw new AccountUpdateException("No File exists");
			}
			else
			{
				System.out.println("-------Files exists-------");
				lIsFileExist = fileNameValidator(fileName.substring(0,fileName.lastIndexOf(".")), fileName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lIsFileExist;
	}
	
	public boolean fileNameValidator(String fileName,String fileNameWithExt) throws LengthException {
		System.out.println("Filename: "+fileName+" its length is: "+fileName.length());
		boolean lIsFileNameValid = true;
		
		String fileRegex = "^rtau_pulse_\\d{12}.csv$";
		Pattern ps = Pattern.compile(fileRegex,Pattern.CASE_INSENSITIVE);
		Matcher ms = ps.matcher(fileNameWithExt);

		System.out.println("---ms.matches()----"+ms.matches());
		if(fileName.length() != 23 || !ms.matches()) {
			lIsFileNameValid = false;
			if(fileName.length() != 23)
				System.out.println("Invalid File Length");
			else
				System.out.println("Invalid File Pattern");
		}
		else {
			System.out.println("File name is fine");
			//lIsFileNameValid = false;
			//System.out.println("Invalid File name");
			//throw new LengthException("File name length shoud be 23 characters");
		}
		return lIsFileNameValid;
	}

	public boolean validateEmptyFile(int fileCount, int recordCount)
	{
		boolean lIsFileEmpty = false;
		boolean lMoveFile = false;
		try
		{
			File sourceFolder = new File(filePath);
			String fileExt = "csv";
			System.out.println("-----We have read-file-of length---->"+sourceFolder.listFiles().length);
				
			if(sourceFolder.listFiles().length > 0)
			{
				for(File sourceFile : sourceFolder.listFiles())
				{
					String fileName = sourceFile.getName();
					System.out.println("-----We have read-file-->"+fileName);
					if(fileName.endsWith(fileExt))
					{
						boolean isFileEmptyTemp = validateEmptyFile(sourceFile);
						if(isFileEmptyTemp)
							lMoveFile = true;
						else
							System.out.println("----File has valid records-->");
					}
					else
					{
						lMoveFile = true;
						System.out.println("--File Extension not supported-----");
					}
					
					//move file if invalid
					if(lMoveFile)
					{
						//Creating a source Path object
						Path source = Paths.get(filePath+fileName);
						//Creating a destination Path object
						Path dest = Paths.get(invalidFilePath+fileName);
						//copying the file
						Files.move(source, dest);
						System.out.println("File moved successfully ........");
					}
				}
				System.out.println("--ttttttttttt--"+sourceFolder.listFiles().length);
			}
			if(sourceFolder.listFiles().length == 0)
				lIsFileEmpty = true;
		}
		catch (Exception e) {
			e.printStackTrace();
			//throw new AccountUpdateException("Exception occured while validating data data file for empty file");
		}
		System.out.println("--lIsFileEmpty----"+lIsFileEmpty);
		return lIsFileEmpty;
	}

	public boolean validateEmptyFile(File sourceFile)
	{
		boolean lIsFileEmpty = false;
		int lines = 0;
		try {
			lines = (int) Files.lines(Paths.get(sourceFile.getPath())).filter(line -> line.length()>0).count();
			lines=lines-1; //-1 bcz of header
		} catch (IOException e) {
			e.printStackTrace();
		} 
		//System.out.println("----lines-------*****--------------"+lines);
		if(lines == 0)
		{
			System.out.println("---File is Empty (No records found in the file)------------");
			lIsFileEmpty = true;
		}
		return lIsFileEmpty;
	}
	
	public void terminateJob(JobExecution pJobExecution)
	{
		System.out.println("---pJobExecution.getJobConfigurationName()------------"+pJobExecution.getJobConfigurationName());
		pJobExecution.setStatus(BatchStatus.STOPPED);
		pJobExecution.stop();
	}

	public void moveFile(String fileName)
	{
		try
		{
			//Creating a source Path object
			Path source = Paths.get(filePath+fileName);
			//Creating a destination Path object
			Path dest = Paths.get(invalidFilePath+fileName);
			//copying the file
			Files.move(source, dest);
			System.out.println("File moved successfully ........");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
