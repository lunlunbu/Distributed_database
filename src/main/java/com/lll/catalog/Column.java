package com.lll.catalog;

import lombok.Data;

@Data
public class Column {
    private final String name;
    private final DataType dataType;
    private final int length;
    private final boolean nullable;

    public Column(String name, DataType dataType, int length, boolean nullable) {
        this.name = name;
        this.dataType = dataType;
        this.length = length;
        this.nullable = nullable;
    }


}
