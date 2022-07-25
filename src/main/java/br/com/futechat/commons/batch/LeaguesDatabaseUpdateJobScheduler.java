package br.com.futechat.commons.batch;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class LeaguesDatabaseUpdateJobScheduler {
	
    @Autowired
    private JobLauncher jobLauncher;
    
    @Autowired
    @Qualifier("leagueDatabaseUpdateJob")
    private Job leagueDatabaseUpdateJob;
	
    @Scheduled(fixedDelay = 432_000_000, initialDelay = 1000)
    public void launchJob() throws Exception {
        Date date = new Date();
        jobLauncher.run(leagueDatabaseUpdateJob, new JobParametersBuilder().addDate("launchDate", date)
                .toJobParameters());
    }

}
