package com.lll.catalog;

import lombok.Getter;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.impl.AbstractSchema;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MyCatalog extends AbstractSchema {

    private Map<String, MySchema> schemas = new HashMap<>();

    public static MyCatalog getCatalog(){
        return new MyCatalog();
    }
    public void addSchema(String name, MySchema schema){
        schemas.put(name, schema);
    }

    public void createSchema(String name) {
        schemas.put(name, new MySchema());
    }

    public void createTable(MySchema schema, String name, MyTable table){
        schema.addTable(name, table);
    }

}
