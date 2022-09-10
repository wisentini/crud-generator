package database.metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
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

        ResultSet resultSet = this.databaseMetadata.getTables(null, null, null, types);
        ArrayList<TableMetadata> tablesMetadata = new ArrayList<>();

        while (resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");
            List<ColumnMetadata> columnsMetadata = this.getColumnsMetadata(tableName);
            TableMetadata tableMetadata = new TableMetadata(tableName, columnsMetadata);
            tablesMetadata.add(tableMetadata);
        }

        return tablesMetadata;
    }

    public List<ColumnMetadata> getColumnsMetadata(String tableName) throws SQLException {
        ArrayList<ColumnMetadata> columnsMetadata = new ArrayList<>();
        List<String> primaryKeyColumnNames = this.getPrimaryKeyColumnNames(tableName);
        ResultSet resultSet = this.databaseMetadata.getColumns(null, null, tableName, null);

        while (resultSet.next()) {
            String columnName = resultSet.getString("COLUMN_NAME");
            int columnType = resultSet.getInt("DATA_TYPE");
            String columnRemarks = resultSet.getString("REMARKS");
            int columnSize = resultSet.getInt("COLUMN_SIZE");
            String columnIsNullable = resultSet.getString("IS_NULLABLE");
            String columnIsAutoincrement = resultSet.getString("IS_AUTOINCREMENT");
            boolean columnIsPrimaryKey = primaryKeyColumnNames.contains(columnName);

            ColumnMetadata columnMetadata = new ColumnMetadata(columnName, columnType, columnRemarks, columnSize, columnIsNullable, columnIsAutoincrement, columnIsPrimaryKey, tableName);
            columnsMetadata.add(columnMetadata);
        }

        return columnsMetadata;
    }

    private List<String> getPrimaryKeyColumnNames(String tableName) throws SQLException {
        ArrayList<String> primaryKeyColumnNames = new ArrayList<>();
        ResultSet resultSet = this.databaseMetadata.getPrimaryKeys(null, null, tableName);

        while (resultSet.next()) {
            String primaryKeyColumnName = resultSet.getString("COLUMN_NAME");
            primaryKeyColumnNames.add(primaryKeyColumnName);
        }

        return primaryKeyColumnNames;
    }
}
