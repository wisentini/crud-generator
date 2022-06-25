package database.metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseMetadata {
    private final DatabaseMetaData databaseMetadata;

    public DatabaseMetadata(Connection connection) throws SQLException {
        this.databaseMetadata = connection.getMetaData();
    }

    public List<TableMetadata> getTablesMetadata() throws SQLException {
        String[] types = {"TABLE"};

        var resultSet = this.databaseMetadata.getTables(null, null, null, types);
        var tablesMetadata = new ArrayList<TableMetadata>();

        while (resultSet.next()) {
            var tableName = resultSet.getString("TABLE_NAME");
            var columnsMetadata = this.getColumnsMetadata(tableName);
            var tableMetadata = new TableMetadata(tableName, columnsMetadata);
            tablesMetadata.add(tableMetadata);
        }

        return tablesMetadata;
    }

    public List<ColumnMetadata> getColumnsMetadata(String tableName) throws SQLException {
        var columnsMetadata = new ArrayList<ColumnMetadata>();
        var primaryKeyColumnNames = this.getPrimaryKeyColumnNames(tableName);
        var resultSet = this.databaseMetadata.getColumns(null, null, tableName, null);

        while (resultSet.next()) {
            var columnName = resultSet.getString("COLUMN_NAME");
            var columnType = resultSet.getInt("DATA_TYPE");
            var columnRemarks = resultSet.getString("REMARKS");
            var columnSize = resultSet.getInt("COLUMN_SIZE");
            var columnIsNullable = resultSet.getString("IS_NULLABLE");
            var columnIsAutoincrement = resultSet.getString("IS_AUTOINCREMENT");
            var columnIsPrimaryKey = primaryKeyColumnNames.contains(columnName);

            var columnMetadata = new ColumnMetadata(columnName, columnType, columnRemarks, columnSize, columnIsNullable, columnIsAutoincrement, columnIsPrimaryKey, tableName);
            columnsMetadata.add(columnMetadata);
        }

        return columnsMetadata;
    }

    private List<String> getPrimaryKeyColumnNames(String tableName) throws SQLException {
        var primaryKeyColumnNames = new ArrayList<String>();
        var resultSet = this.databaseMetadata.getPrimaryKeys(null, null, tableName);

        while (resultSet.next()) {
            var primaryKeyColumnName = resultSet.getString("COLUMN_NAME");
            primaryKeyColumnNames.add(primaryKeyColumnName);
        }

        return primaryKeyColumnNames;
    }
}
