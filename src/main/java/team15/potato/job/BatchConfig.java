package team15.potato.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team15.potato.service.MovieListApiService;
import team15.potato.service.PeopleListApiService;

/**
 * ===========================================
 * Job : 하나의 배치 작업 단위
 * Job 안에는 여러개의 step이 존재할 수 있고,
 * 각 step 안에는 하나의 Tasklet 혹은
 * 하나의 Reader & Processor & Writer가 존재
 * ===========================================
 */

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MovieListApiService movieListApiService;
    private final PeopleListApiService peopleListApiService;

    @Bean
    public Job testJob() {
        return jobBuilderFactory.get("testJob")
                .start(testStep())
                .build();
    }

    @Bean
    @JobScope
    public Step testStep() {
        return stepBuilderFactory.get("testStep")
                .tasklet((contribution, chunkContext) -> {
                    movieListApiService.insertMovie();
                    // peopleListApiService.insertPeople();
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
