package com.sally.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SendingEmailJobConfiguration {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DataSource dataSource;

	private static final int CHUNK_SIZE = 4;

	/**
	 * job은 step을 필요로 한다.
	 * step 에는 reader, processor, writer 가 있다.
	 *   이 중 가공할 일이 없으면 reader, writer 만 사용
	 *   테이블 2개를 읽어 이메일 보내고자 한다면 Step에 리더를 2개로 할 수도 있다.
	 *     - 2개를 start().next()의 파라미터로 각각 넣어준다.
	 *     - 스레드 하나로 순차적으로 가는데, 병렬도 가능하다.
	 */
	@Bean
	public Job sendEmailJob(Step sendEmailTargetStep) {
		return jobBuilderFactory.get("sendEmailJob")
			.start(sendEmailTargetStep)
			.build();
	}

	@Bean
	public Step sendEmailTargetStep(
		ItemReader<TargetEmailRequest> sendEmailTargetReader,
		ItemWriter<TargetEmailRequest> sendEmailTargetWriter) {
		return stepBuilderFactory.get("sendEmailTargetStep")
			.<TargetEmailRequest, TargetEmailRequest>chunk(CHUNK_SIZE)
			.reader(sendEmailTargetReader)
			.writer(sendEmailTargetWriter)
			.allowStartIfComplete(true)
			.build();
	}

	@Bean
	public JdbcCursorItemReader<TargetEmailRequest> sendEmailTargetReader() {
		return new JdbcCursorItemReaderBuilder<TargetEmailRequest>()
			.dataSource(dataSource)
			.rowMapper(new BeanPropertyRowMapper<>(TargetEmailRequest.class))
			.sql("select \n"
				+ "\ta.email, \n"
				+ "    a.target_title, a.target_at, a.label_name,\n"
				+ "    a.assemble_title, a.assemble_start_at, a.assemble_end_at\n"
				+ "from tb_delayed_target_manager as a\n"
				+ "where a.to_team = false\n"
				+ "and a.status = 'APPENDING'\n"
				+ "and a.notify_at >= now() + interval 1 day\n"
				+ "and a.notify_at < now() + interval 2 day\n"
				+ "order by id asc;")
			.name("jdbcCursorItemReader")
			.build();
	}

	@Bean
	public ItemWriter<TargetEmailRequest> sendEmailTargetWriter() {
		// email 발송 오래 걸리기 때문에 청크 사이즈만큼 이메일 보내고 있기 어려움
		// WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build();
		// 결과를 배치에서 받아오지 않고, 리턴타입에 대해 받아오지 않고 정의않고 Object로 반환
		
		return (list) -> new RestTemplate()
			.postForObject("http://localhost:8080/api/batch/send/target-mail", list, Object.class); // TODO
	}
}
