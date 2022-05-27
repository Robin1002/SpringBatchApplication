package com.example.demo.config;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import com.example.demo.dto.User;
import com.example.demo.dto.UserContact;
import com.example.demo.listener.JobListener;
import com.example.demo.listener.Listener;
import com.example.demo.listener.ProcessListener;
import com.example.demo.listener.ReadListener;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

	 @Value("${dataFilePath}")
	 private String dataFilePath;
	 
	 @Value("${dataFilePath1}")
	 private String dataFilePath1;
	 
	 @Value("${filePath}")
	 private String filePath;

	@Autowired
	 JobOperator jobOperator;
	 
    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory, //JobBuilderFactory provided by the spring
                   StepBuilderFactory stepBuilderFactory,
                   ItemReader<User> itemReader,
                   ItemProcessor<User, User> itemProcessor, //i/p and o/p is type of User
                   ItemWriter<User> itemWriter, // We have just autowired itemReader itemProcessor itemWriter, yet not provided the implementation
                   ItemWriter<UserContact> itemWriterContact
    ) {
    	
    	//creating Step
        Step step1 = stepBuilderFactory.get("stepOne")//provide name to to stepbuilderfac -> ETL-file-load -> any name can be given
                .<User, User>chunk(20) //we can process in chunks (chunk- means batching), batches of 100
                .listener(new ReadListener())
                .listener(new Listener())
                .listener(new ProcessListener())
                .reader(itemReader) // reader implmn line 53
                .processor(itemProcessor) // for processor created Processor.java cls
                .writer(itemWriter) // for writer created DBWriter.java cls
                //.faultTolerant().skipPolicy(new JobSkipPolicy()).listener(new StepSkipListener())
                //.faultTolerant().skipLimit(4).skip(Exception.class)
                //.faultTolerant().retryLimit(2).retry(Exception.class)
                //.taskExecutor(taskExecutor())
                .build();
        
        Step step2 = stepBuilderFactory.get("stepTwo")//provide name to to stepbuilderfac -> ETL-file-load -> any name can be given
                .<UserContact, UserContact>chunk(1) //we can process in chunks (chunk- means batching), batches of 100
                .reader(itemReaderTwo())
                .writer(itemWriterContact) // for writer created DBWriter.java cls
                //.faultTolerant().skipPolicy(skipPolicyHandle()) // if we use chunk more than 1 e.g 5-(run 5 chunk at a time) and there is 2 fault records then also it will not add remaining 3 records
                //.faultTolerant().skipLimit(2).skip(IllegalStateException.class).skip(NonSkippableReadException.class)
                //.taskExecutor(taskExecutor())
                .build();

        //creating Job
        return jobBuilderFactory.get("job1") //sequence of ids that we assign for every run, new RunIdIncrementer() -> default provider, u can also use customized one
                .listener(jobListener())
        		.start(step1) //under a job u can have multiple step,if u have only 1 STEP ->then use start(), if multiple -> flow(step) or start(), then next(another step)
                //.on("*")      //means-> on any outcome even if exception arise for step1 execution, step2 will not terminate and complete its execution
                //.to(step2)
                //.end()
                .build();
    }

	// Impl of Reader, since we are gonna read csv file we will use inbuilt class -> FlatFileItemReader<return type>
    @StepScope
    @Bean
    public FlatFileItemReader<User> itemReader() { 

        FlatFileItemReader<User> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource(dataFilePath));
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1); //to skip header in csv file
        flatFileItemReader.setLineMapper(lineMapper()); // to map csv to User class we use lineMapper
        return flatFileItemReader;
    }
    
    @StepScope
    @Bean
    public LineMapper<User> lineMapper() {

        DefaultLineMapper<User> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter("|");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("isserMemberId", "oldPan", "oldExpirationDate", "newPan", "newExpirationDate", "reasonCode", "effectiveDate"); //same name as POJO

        //To set each field of csv (i.e set each row value) to User Pojo
        //There is BeanWrapperFieldSetMapper wch will help in this case
        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);// Just set target type, it will automatically do the mapping from csv to User Pojo

        defaultLineMapper.setLineTokenizer(lineTokenizer); // add tokenizer to linemapper
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }
    
    @StepScope
    @Bean
    public TaskExecutor taskExecutor()
    {
    	SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
    	simpleAsyncTaskExecutor.setConcurrencyLimit(5); //how many threads u want to introduce
		return simpleAsyncTaskExecutor;
    }
    
    @StepScope
    @Bean
    public FlatFileItemReader<UserContact> itemReaderTwo() 
    {
    	 FlatFileItemReader<UserContact> flatFileItemReader = new FlatFileItemReader<>();
         flatFileItemReader.setResource(new FileSystemResource(dataFilePath));
         flatFileItemReader.setName("CSV-Reader");
         flatFileItemReader.setLinesToSkip(1); //to skip header in csv file
         flatFileItemReader.setLineMapper(lineMapperTwo()); // to map csv to User class we use lineMapper
         return flatFileItemReader;
    }
   
    @StepScope
    @Bean
    public LineMapper<UserContact> lineMapperTwo() {

        LineMapper<UserContact> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("name", "number");

        //To set each field of csv (i.e set each row value) to User Pojo
        //There is BeanWrapperFieldSetMapper wch will help in this case
        BeanWrapperFieldSetMapper<UserContact> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(UserContact.class);// Just set target type, it will automatically do the mapping from csv to User Pojo

        ((DefaultLineMapper<UserContact>) defaultLineMapper).setLineTokenizer(lineTokenizer); // add tokenizer to linemapper
        ((DefaultLineMapper<UserContact>) defaultLineMapper).setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }
    
    @PostConstruct
    public void validateFileMethod()
    {
    	System.out.println("----post construct mth called---------");
		/*
		 * try { AccountUpdateFileValidator.validate(); } catch (AccountUpdateException
		 * e) { // TODO Auto-generated catch block
		 * System.out.println("----post construct catch called---------");
		 * //System.exit(1); e.printStackTrace(); }
		 */
    }
    
    @Bean
    public JobListener jobListener()
    {
		return new JobListener();
    }
	 
}
