package com.lll;

import com.lll.catalog.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) throws IOException {
        String sql = "SELECT id,name,age FROM data WHERE age > 30";
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/data/data.csv"), Charset.defaultCharset());
        String firstLine = lines.get(0);
        String[] columnData = firstLine.split(",");
        //1.获取catalog
        MyCatalog catalog = MyCatalog.getCatalog();
        //2.创建MySchema
        catalog.createSchema("test");
        //3.创建MyTable
        //获取列名
        List<Column> columns = new ArrayList<>();
        for (String column : columnData){
            columns.add(new Column(column, DataType.STRING, 10, true));
        }
        MyTable table = new MyTable(columns);
        catalog.createTable(catalog.getSchemas().get("test"), "table_name", table);
        System.out.println("11111");
        System.out.println(catalog.getSchemas().get("test").getTables().get("table_name"));

    }
}
