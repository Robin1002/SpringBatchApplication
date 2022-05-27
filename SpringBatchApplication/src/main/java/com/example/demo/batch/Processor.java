package com.example.demo.batch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import com.example.demo.dto.User;
import com.example.demo.validator.AccDataValidator;

@Component
public class Processor implements ItemProcessor<User, User> {

	@Autowired
	AccDataValidator accDataValidator;
	
    private static final Map<String, String> DEPT_NAMES =
            new HashMap<>();

    @Override
    //@Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(3000))
    public User process(User user) throws Exception {
        System.out.println("----inside process implementation class------");
        accDataValidator.validateAccountData(user);
        //can be rest template call
        if (user.getIsserMemberId() ==  6 )
        {
        	System.out.println("================Throwing RuntimeException==================");
        	throw new RuntimeException();
        	
        }
        return user;
    }
    
    /*
	 @Recover 
	 public User recover(Exception exception, User user) 
	 { System.out.println("--------------------Recover called------"+user+"---------------------------"+exception.getMessage());
	 	//User user1 = new User();
	 	//user1.setId(0);
	 	//throw new SkipLimitExceededException(count, exception);
	 	return null; 
	 }
	 */
}
