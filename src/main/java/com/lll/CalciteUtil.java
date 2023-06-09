package com.lll;

import com.lll.pojo.Cell;
import com.lll.pojo.Row;
import com.lll.pojo.RowSet;
import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        //1.获取表名
        //TODO 实现表join（可先完成单表查询再实现）
        getTables(fromNode,tableList);
        //2.获取所需字段
        SqlNodeList selectList = sqlSelect.getSelectList();
        List<String> columns = new ArrayList<String>();
        for (SqlNode node : selectList){
            if (node instanceof SqlIdentifier){
                SqlIdentifier identifier = (SqlIdentifier) node;
                columns.add(identifier.getSimple().toLowerCase());
            }
        }
        //TODO 3.实现where
        //TODO 4.实现limit

        SqlNode whereNode = sqlSelect.getWhere();

        //3.读取表中数据
        // 根据列名读取数据
        RowSet rowSet = new RowSet();
        loadData(rowSet, tableList, columns);
        //输出表名
        System.out.println(columns.stream().collect(Collectors.joining(" , ")));
        for (Row row : rowSet.getRows()) {
            for (Cell cell : row.getCells()){
                System.out.print(cell.getVal() + "    ");
            }
            System.out.println();
        }


    }

    private static void loadData(RowSet rowSet, List<String> tableList, List<String> columns) {
        for (String table : tableList) {
            table = table.toLowerCase();
            table += ".csv";
            List<String> lines;
            try {
                lines = Files.readAllLines(Paths.get("src/main/resources/data/" + table), Charset.defaultCharset());
            } catch (IOException e) {
                throw new RuntimeException(table + "不存在");
            }
            //遍历读入
            //获取所需的列名,并记录下其所在下标
            String firstLine = lines.get(0);
            String[] columnData = firstLine.split(",");
            List<Integer> column_needed_index = new ArrayList<Integer>();
            //这里以所需column为主体找下表，保证select的列的顺序性
//            for (int i = 0; i < columnData.length; i++){
//                if (!columns.contains(columnData[i])){
//                    continue;
//                }
//                column_needed_index.add(i);
//            }
            for (String column : columns){
                for (int i = 0; i < columnData.length; i++) {
                    if (column.equals(columnData[i])){
                        column_needed_index.add(i);
                    }
                }
            }
            for (String line : lines) {
                if(line == lines.get(0))continue;
                String[] cellsData = line.split(",");
                Cell[] cells = new Cell[column_needed_index.size()];
                //TODO 实现where
                //读取数据
                //这里i表示列里面的index，不能用其表明cells，因为中间列有的不读入，所以i可能越界
                int j = 0;
                for (int i : column_needed_index) {
                    Cell cell = new Cell();
                    cell.setType("string"); // 设置类型
                    cell.setSize(String.valueOf(cellsData[i].length())); // 设置大小
                    cell.setVal(cellsData[i]); // 设置值
                    cells[j++] = cell;
                }
                Row row = new Row();
                row.setCells(cells);
                rowSet.addRow(row);
            }
        }
    }


    private static void getTables(SqlNode fromNode, List<String> tableList) {
        if (fromNode instanceof SqlJoin){
            SqlJoin join = (SqlJoin) fromNode;
            handleJoinNode(join,tableList);
        }else if (fromNode instanceof SqlIdentifier){
            SqlIdentifier tableName = (SqlIdentifier) fromNode;
            String table = tableName.getSimple();
            tableList.add(table);
        }else {
            //没有输入表名之类的错误,这个在calcite解析时就做了
            throw new RuntimeException("未输入表名");
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
