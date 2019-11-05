package com.pres.lucene;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Dora
 * @date 2019/11/1 15:42
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class DataSourceTests {
    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void testDataSource() throws Exception{
        DataSource dataSource =  applicationContext.getBean(DataSource.class);
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from user_info");
        int columnCount = resultSet.getMetaData().getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            System.out.println("resultSet = " + resultSet.getMetaData().getColumnName(i));
        }

    }
}
