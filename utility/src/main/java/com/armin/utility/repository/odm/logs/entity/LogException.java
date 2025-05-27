package com.armin.utility.repository.odm.logs.entity;

import com.armin.utility.repository.odm.dao.ElasticsearchLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 10.09.22
 */
@Service
public class LogException {
    private final ElasticsearchLogDao elasticsearchLogDao;

    @Autowired
    public LogException(ElasticsearchLogDao elasticsearchLogDao) {
        this.elasticsearchLogDao = elasticsearchLogDao;
    }

    public void bulkLogException(Exception exception, String className, String method, String elasticIndexConstant) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        String stackTrace = stringWriter.toString();
        ExceptionsLogElastic exceptionsLogElastic = new ExceptionsLogElastic(className, method, stackTrace, exception.toString());
        elasticsearchLogDao.bulkIndexWithOutId(elasticIndexConstant, exceptionsLogElastic);
    }
}
