package com.lll;

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;

public class SqlParseExample {
    public static void main(String[] args) throws SqlParseException {
        String sql = "SELECT id, name, age FROM data WHERE age > 30";
        //1.解析sql
        SqlNode sqlNode = CalciteUtil.parserSql(sql);
        //2.执行
        CalciteUtil.handle(sqlNode);
    }
}
