package com.example.demo.batch;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepListenerSupport;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.demo.dto.User;

public class JDBCWriter extends StepListenerSupport implements ItemWriter<User>{

	@Override
	public void write(List<? extends User> items) throws Exception {
		// TODO Auto-generated method stub
		
	}
/*
private boolean errorFlag;
Connection connection = null;
private String sql = "{ insert into USER(?, ?,  ?, ?, ?) }";

@Autowired
private JdbcTemplate jdbcTemplate;

@Override
public void beforeStep(StepExecution stepExecution){

    try{
        connection = jdbcTemplate.getDataSource().getConnection();

        connection.setAutoCommit(false);
    }
    catch(SQLException ex){
        setErrorFlag(Boolean.TRUE);
    }
}

@Override
public void write(List<? extends User> items) throws Exception{

    if(!items.isEmpty()){

                CallableStatement callableStatement = connection.prepareCall(sql);

                callableStatement.setInt("1", "FirstName");
                callableStatement.setString("2", "LastName");
                callableStatement.setString("3", "Date of Birth");
                callableStatement.setInt("4", "Year");

                callableStatement.registerOutParameter("errors", Types.INTEGER, "");

                callableStatement.execute();

                if(errors != 0){
                    this.setErrorFlag(Boolean.TRUE);
                    }
            }
    else{
        this.setErrorFlag(Boolean.TRUE);
    }
}

@Override
public void afterChunk(ChunkContext context){
    if(errorFlag){
        context.getStepContext().getStepExecution().setExitStatus(ExitStatus.FAILED); //Fail the Step
        context.getStepContext().getStepExecution().setStatus(BatchStatus.FAILED); //Fail the batch
    }
}

@Override
public ExitStatus afterStep(StepExecution stepExecution){
    try{
        if(!errorFlag){
            connection.commit();
        }
        else{
            connection.rollback();
            stepExecution.setExitStatus(ExitStatus.FAILED);
        }
    }
    catch(SQLException ex){
        LOG.error("Commit Failed!" + ex);
    }

    return stepExecution.getExitStatus();
}

public void setErrorFlag(boolean errorFlag){
    this.errorFlag = errorFlag;
    }
*/
}