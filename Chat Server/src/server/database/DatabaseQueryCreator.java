package server.database;

public class DatabaseQueryCreator
{
	public static DatabaseQuery setTable(DatabaseQuery.Table p_table)
	{
		DatabaseQuery databaseQuery = new DatabaseQuery();
		databaseQuery.setTable(p_table);
		
		return databaseQuery;
	}

	
}
