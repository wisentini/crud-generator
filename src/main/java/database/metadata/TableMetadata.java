package database.metadata;

import java.util.List;

public class TableMetadata {
    private String name;
    private List<ColumnMetadata> columnsMetadata;

    public TableMetadata(String name, List<ColumnMetadata> columnsMetadata) {
        this.name = name;
        this.columnsMetadata = columnsMetadata;
    }

    public String getName() {
        return this.name;
    }

    public List<ColumnMetadata> getColumnsMetadata() {
        return this.columnsMetadata;
    }

    public ColumnMetadata getPrimaryKeyColumn() {
        for (ColumnMetadata columnMetadata : this.columnsMetadata) {
            if (columnMetadata.getIsPrimaryKey()) {
                return columnMetadata;
            }
        }

        return null;
    }
}
