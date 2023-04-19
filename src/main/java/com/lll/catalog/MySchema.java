package com.lll.catalog;

import lombok.Data;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;

import java.util.HashMap;
import java.util.Map;

@Data
public class MySchema extends AbstractSchema {
    //不需要名字，因为在catalog里面用键值对存的保有名字，从而找到对应Schema
    private Map<String, Table> tables = new HashMap<>();

    public void addTable(String name, Table table) {
        tables.put(name, table);
    }

    @Override
    protected Map<String, Table> getTableMap() {
        return tables;
    }
}
