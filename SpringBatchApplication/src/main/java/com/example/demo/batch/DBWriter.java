package com.example.demo.batch;

import com.example.demo.dto.User;
import com.example.demo.repository.UserRepository;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

// To write data in to DB
@Component
public class DBWriter implements ItemWriter<User> {

	private UserRepository userRepository;


	@Autowired public DBWriter (UserRepository userRepository) {
		this.userRepository = userRepository; }


	@Override
	public void write(List<? extends User> users) throws Exception{
		users = users.stream().filter(user->user.getIsserMemberId() > 0).collect(Collectors.toList());
		/*for(User u1 : users)
    	{
    		if (u1.getId() ==  8 )
            	throw new RuntimeException();
    	}*/
		System.out.println("Data Saved for Users: " + users);

		userRepository.saveAll(users);
		//throw new Exception();
	}
}
