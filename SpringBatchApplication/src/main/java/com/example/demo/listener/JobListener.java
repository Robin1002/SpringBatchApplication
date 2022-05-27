package com.example.demo.listener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.example.demo.validator.AccountUpdateFileValidator;

@JobScope
public class JobListener implements JobExecutionListener {

	@Autowired
	AccountUpdateFileValidator accountUpdateFileValidator;

	@Value("${dataFilePath}")
	private String dataFilePath;

	@Value("${invalidFilePath}")
	private String invalidFilePath;

	@Value("${filePath}")
	private String filePath;

	@Value("${completedPath}")
	private String completedPath;

	public static JobExecution jobExecution;

	@Override
	public void beforeJob(JobExecution jobExecution) 
	{
		JobListener.jobExecution = jobExecution;
		boolean lMoveFile = false, lIsFileNameValid = false, isFileEmptyTemp = false;
		try
		{
			File sourceFolder = new File(filePath);
			String fileExt = "csv";
			System.out.println("-----total files in the specified path---->"+sourceFolder.listFiles().length);

			if(sourceFolder.listFiles().length > 0)
			{
				for(File sourceFile : sourceFolder.listFiles())
				{
					String fileName = sourceFile.getName();
					System.out.println("----Reading file with name-->"+fileName);
					if(fileName.endsWith(fileExt))
					{
						//Validate File Name->length and pattern
						lIsFileNameValid = accountUpdateFileValidator.fileNameValidator(fileName.substring(0,fileName.lastIndexOf(".")),fileName);

						//if file name is valid then check file is empty or not
						if(lIsFileNameValid)
							isFileEmptyTemp = accountUpdateFileValidator.validateEmptyFile(sourceFile);

						//if file is empty or file name is invalid then set true
						if(isFileEmptyTemp || !lIsFileNameValid)
							lMoveFile = true;
						else
							System.out.println("----File has records-->");
					}
					else
					{
						lMoveFile = true;
						System.out.println("--File Extension not supported-----");
					}

					//move invalid file to diff location
					if(lMoveFile)
						accountUpdateFileValidator.moveFile(fileName);
				}
				//System.out.println("--ttttttttttt--"+sourceFolder.listFiles().length);
			}
			//If there is no file in folder then terminate the job
			if(sourceFolder.listFiles().length == 0)
			{
				System.out.println("No CSV File exists");
				accountUpdateFileValidator.terminateJob(jobExecution);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			//throw new AccountUpdateException("Exception occured while validating data data file for empty file");
		}

	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		//System.out.println("-----beforeJob----dataFilePath----22-------"+dataFilePath);
		//new StepSkipListener().close();
		System.out.println("---jobExecution.getStatus()---"+jobExecution.getStatus()+"--jobExecution.getStatus().STOPPED----"+jobExecution.getStatus().STOPPED);
		try {
			System.out.println(dataFilePath);
			if(jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
				System.out.println("================SUCCESS===================");
				System.out.println(this.completedPath);
				Files.move(Paths.get(dataFilePath), Paths.get(completedPath+"rtau_pulse_"+"202205050015"+".csv"));
			}
			else if (jobExecution.getStatus().equals(BatchStatus.FAILED)) {
				System.out.println("==================FAILED=================");
				System.out.println(invalidFilePath);
				Files.move(Paths.get(dataFilePath), Paths.get(invalidFilePath+"rtau_pulse_"+"202205050015"+".csv"));
			} else if(jobExecution.getStatus().equals(BatchStatus.STOPPED)) {
					System.out.println("=================Job Stopped==========================");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
