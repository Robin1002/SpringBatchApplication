package com.example.demo.listener;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.step.skip.SkipListenerFailedException;
import org.springframework.batch.item.file.FlatFileParseException;

import com.example.demo.dto.User;
import com.opencsv.CSVWriter;

public class StepSkipListener implements SkipListener<User, User>{
	
	List<String []> al = new ArrayList<>();
	static CSVWriter writer = null;
	
	/*
	 * public static CSVWriter getCSVWriter() throws IOException { return writer =
	 * new CSVWriter(new
	 * FileWriter("C:\\Users\\mamverma\\Downloads\\temp\\test.csv"));
	 * 
	 * }
	 * 
	 *  try (CSVWriter writer = new CSVWriter(new FileWriter("C:\\Users\\mamverma\\Downloads\\temp\\test.csv"))) {
			 	String[] lTemp= new String[] {flatFileParseException.getInput()};
			 	 al.add(lTemp);
	            writer.writeAll(al);
	        } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 */
	static {
		try {
			writer = new CSVWriter(new FileWriter("C:\\Users\\mamverma\\Downloads\\temp\\test.csv",true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onSkipAddFile(String lTemp)
	{
        String [] record = lTemp.split(",");
        writer.writeNext(record);
        try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
@Override
public void onSkipInRead(Throwable t) {
	// TODO Auto-generated method stub
	System.out.println("StepSkipListener - onSkipInRead----"+t.getMessage());
	if(t instanceof FlatFileParseException) {
		 FlatFileParseException flatFileParseException = (FlatFileParseException) t;
		 String message = flatFileParseException.getMessage() + "-" + flatFileParseException.getLineNumber();
		 
		
			/*
			 * String[] lTemp= new String[] {flatFileParseException.getInput()};
			 * al.add(lTemp); writer.writeAll(al);
			 */
		 
		// try (CSVWriter writer = new CSVWriter(new FileWriter("C:\\Users\\mamverma\\Downloads\\temp\\test.csv"))) {
			 	String[] lTemp= new String[] {flatFileParseException.getInput()};
			 	al = new ArrayList<>();
			 	 al.add(lTemp);
	            writer.writeAll(al);
				/*
				 * } catch (IOException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 */
		 
		 
		System.out.println("***************onSkipInRead**********-->"+flatFileParseException.getInput());
		 }
}

@Override
public void onSkipInWrite(User item, Throwable t) {
	// TODO Auto-generated method stub
	/*
	 * String[] lTemp= item.toString().split(","); al = new ArrayList<>();
	 * al.add(lTemp); writer.writeAll(al);
	 */
   
   String[] lTemp1= new String[] {item.toString()};
	al = new ArrayList<>();
	 al.add(lTemp1);
   writer.writeAll(al);
	System.out.println("***************onSkipInWrite**********--"+item+"----msg---"+t.getMessage());
}

@Override
public void onSkipInProcess(User item, Throwable t) {
	// TODO Auto-generated method stub
	String[] lTemp1= new String[] {item.toString()};
	al = new ArrayList<>();
	 al.add(lTemp1);
   writer.writeAll(al);
	System.out.println("S***************onSkipInProcess**********---"+item+"----msg---"+t.getMessage());
}

public void close() {
	try {
		writer.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

}
