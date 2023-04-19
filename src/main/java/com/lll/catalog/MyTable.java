package com.lll.catalog;

import lombok.Data;
import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.Statistic;
import org.apache.calcite.schema.Statistics;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.type.SqlTypeName;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

@Data
public class MyTable implements Table {
    private final List<Column> columns;

    public MyTable(List<Column> columns) {
        this.columns = columns;
    }

    public void print(){
        for (Column column : columns){
            System.out.println(column.toString());
        }
    }

    public Schema getSchema() {
        return null; // TODO: 返回该表所在的Schema
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        RelDataTypeFactory.FieldInfoBuilder builder = typeFactory.builder();
        for (Column column : columns) {
            RelDataType dataType = getRelDataType(column.getDataType(), typeFactory, column.getLength());
            builder.add(column.getName(), dataType).nullable(column.isNullable());
        }
        return builder.build();
    }

    @Override
    public Statistic getStatistic() {
        return Statistics.UNKNOWN;
    }

    @Override
    public Schema.TableType getJdbcTableType() {
        return null;
    }

    @Override
    public boolean isRolledUp(String column) {
        return false;
    }

    @Override
    public boolean rolledUpColumnValidInsideAgg(String column, SqlCall call, SqlNode parent,
                                                CalciteConnectionConfig config) {
        return false;
    }

    public List<Column> getColumns() {
        return columns;
    }

    private static RelDataType getRelDataType(DataType dataType, RelDataTypeFactory typeFactory, int length) {
        switch (dataType) {
            case INT:
                return typeFactory.createSqlType(SqlTypeName.INTEGER);
            case LONG:
                return typeFactory.createSqlType(SqlTypeName.BIGINT);
            case DOUBLE:
                return typeFactory.createSqlType(SqlTypeName.DOUBLE);
            case STRING:
                return typeFactory.createSqlType(SqlTypeName.VARCHAR, length);
            case BOOLEAN:
                return typeFactory.createSqlType(SqlTypeName.BOOLEAN);
            case DATE:
                return typeFactory.createSqlType(SqlTypeName.DATE);
            case TIME:
                return typeFactory.createSqlType(SqlTypeName.TIME);
            case DATETIME:
                return typeFactory.createSqlType(SqlTypeName.TIMESTAMP);
            default:
                throw new IllegalArgumentException("Unsupported data type: " + dataType);
        }
    }
}
