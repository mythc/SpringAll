package cc.mrbird.batch.job;

import cc.mrbird.batch.entity.TestData;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


/**
 * @author fanglihua
 * @version 1.0
 * @date 2023/12/1 14:48
 */
//@Component
public class DataSourceItemReaderDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private DataSource dataSource;

    @Bean
    public Job dataSourceItemReaderJob() throws Exception {
        return jobBuilderFactory.get("dataSourceItemReaderjob")
                .start(step())
                .build();
    }

    private Step step() throws Exception {
        return stepBuilderFactory.get("step")
                .chunk(2)
                .reader(dataSourceItemReader())
                .writer(list -> list.forEach(System.out::println))
                .build();
    }

    private ItemReader<TestData> dataSourceItemReader() throws Exception {
        JdbcPagingItemReader<TestData> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setFetchSize(5);
        reader.setPageSize(5);

        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setSelectClause("id,field1,field2,field3");
        provider.setFromClause("from TEST");

        reader.setRowMapper(((resultSet, i) -> {
            TestData data = new TestData();
            data.setId(resultSet.getInt("id"));
            data.setField1(resultSet.getString("field1"));
            data.setField2(resultSet.getString("field2"));
            data.setField3(resultSet.getString("field3"));

            return data;
        }));

        Map<String, Order> map = new HashMap<>();
        map.put("id", Order.ASCENDING);
        provider.setSortKeys(map);

        reader.setQueryProvider(provider);
        reader.afterPropertiesSet();
        return reader;
    }


}
