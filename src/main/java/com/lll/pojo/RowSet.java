package com.lll.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RowSet {
    private List<Row> rows;

    public RowSet() {
        this.rows = new ArrayList<Row>();
    }

    public void addRow(Row row) {
        rows.add(row);
    }


}
