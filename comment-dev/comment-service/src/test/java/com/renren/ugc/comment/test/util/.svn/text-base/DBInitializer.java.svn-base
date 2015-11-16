package com.renren.ugc.comment.test.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ResourceUtils;

import com.renren.ugc.comment.service.CommentCenter;

/**
 * Initialize the db used for test
 * 
 * @author jiankuan.xing
 * 
 */
public class DBInitializer {

    private static Logger logger = Logger.getLogger(DBInitializer.class);

    public static void init() throws FileNotFoundException {
        ApplicationContext context = CommentCenter.getAppContext();
        JdbcTemplate personTemplate = (JdbcTemplate) context.getBean("personJdbcTemplate");
        File file = ResourceUtils.getFile("classpath:reset-test-db.sql");
        List<String> stats = readSqlStatement(file);
        for (String stat : stats) {
            personTemplate.execute(stat);
        }

        logger.info("reset test db");
    }

    private static List<String> readSqlStatement(File file) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            do {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                sb.append(line);
            } while (true);

        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] statements = sb.toString().split(";");
        ArrayList<String> statementsList = new ArrayList<String>();
        for (String statement : statements) {
            if (statement != "") {
                statementsList.add(statement.trim() + ";");
            }
        }

        return statementsList;
    }
}
