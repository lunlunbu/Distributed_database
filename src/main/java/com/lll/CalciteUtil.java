package com.lll;

import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;

import java.util.ArrayList;
import java.util.List;

public class CalciteUtil {
    public static SqlNode parserSql(String sql) {
        //1.创建Calcite解析器
        SqlParser parser = SqlParser.create(sql);
        //2.解析sql语句
        SqlNode sqlNode = null;
        try {
            sqlNode = parser.parseStmt();
        } catch (SqlParseException e) {
            e.printStackTrace();
        }
        return sqlNode;
    }

    public static void handle(SqlNode sqlNode) {
        //1.获取类型
        SqlKind kind = sqlNode.getKind();
        //System.out.println(kind);
        //2.根据类型处理
        switch (kind){
            case SELECT:
                SqlSelect sqlSelect = (SqlSelect) sqlNode;
                doSelect(sqlSelect);
                break;
            default:
                throw new RuntimeException("异常");
        }
    }

    private static void doSelect(SqlSelect sqlSelect) {
        //获取查询的表名,得判断是否是多个表
        SqlNode fromNode = sqlSelect.getFrom();
        //用一个数组存表名
        List<String> tableList = new ArrayList<String>();
        if (fromNode instanceof SqlJoin){
            SqlJoin join = (SqlJoin) fromNode;
            handleJoinNode(join,tableList);
            for (String table : tableList){
                System.out.println(table);
            }
        }else if (fromNode instanceof SqlIdentifier){
            SqlIdentifier tableName = (SqlIdentifier) fromNode;
            String table = tableName.getSimple();
            System.out.println("Table name: " + table);
        }else {
            //没有输入表名之类的错误
        }

    }

    private static void handleJoinNode(SqlJoin join, List tableList) {
        SqlNode left = join.getLeft();
        if (left instanceof  SqlJoin){
            handleJoinNode((SqlJoin) left, tableList);
        } else {
            SqlIdentifier leftTable = (SqlIdentifier) left;
            String leftTableName = leftTable.getSimple();
            tableList.add(leftTableName);
        }
        SqlNode  right= join.getRight();
        if (right instanceof  SqlJoin){
            handleJoinNode((SqlJoin) right, tableList);
        } else {
            SqlIdentifier rightTable = (SqlIdentifier) right;
            String rightTableName = rightTable.getSimple();
            tableList.add(rightTableName);
        }
    }
}
