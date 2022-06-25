package database.metadata;

import util.DatabaseUtil;

public class ColumnMetadata {
    private String name;
    private int type;
    private String remarks;
    private int size;
    private String isNullable;
    private String isAutoincrement;
    private boolean isPrimaryKey;
    private String tableName;

    public ColumnMetadata(String name, int type, String remarks, int size, String isNullable, String isAutoincrement, boolean isPrimaryKey, String tableName) {
        this.name = name;
        this.type = type;
        this.remarks = remarks;
        this.size = size;
        this.isNullable = isNullable;
        this.isAutoincrement = isAutoincrement;
        this.isPrimaryKey = isPrimaryKey;
        this.tableName = tableName;
    }

    public String getName() {
        return this.name;
    }

    public int getType() {
        return this.type;
    }

    public Class<?> getTypeClass() {
        var typeClass = DatabaseUtil.convertSQLTypeToClass(this.type);
        return typeClass;
    }

    public String getTypeClassName() {
        var typeClass = this.getTypeClass();
        var typeClassName = typeClass.getSimpleName();
        return typeClassName;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public int getSize() {
        return this.size;
    }

    public String getIsNullable() {
        return this.isNullable;
    }

    public String getIsAutoincrement() {
        return this.isAutoincrement;
    }

    public boolean getIsPrimaryKey() {
        return this.isPrimaryKey;
    }

    public String getTableName() {
        return this.tableName;
    }
}
