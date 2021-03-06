package server;

import java.util.Date;

import org.junit.Test;

import server.database.Database.MessageTableColumns;
import server.database.DatabaseQuery;
import server.database.DatabaseQuery.Condition;
import server.database.DatabaseQuery.Table;
import server.database.DatabaseQuery.Type;
import server.database.DatabaseQueryCreator;

public class DatabaseQueryTest
{
	@Test
	public void createQuery()
	{
		DatabaseQuery databaseQuery = DatabaseQueryCreator.setTable(Table.MESSAGES).setType(Type.SELECT).where()
				.setSelector(MessageTableColumns.RECEIVER, Condition.EQUALS, null).Or()
				.setSelector(MessageTableColumns.TIMESTAMP, Condition.GREATER_THAN, new Date()).And().setSelector(MessageTableColumns.SENDER, Condition.EQUALS, "IBM").getQuery();
		
		System.out.println(databaseQuery);
		
		DatabaseQuery databaseQuery_1 = DatabaseQueryCreator.setTable(Table.MESSAGES).setType(Type.SELECT).where()
				.setSelector(MessageTableColumns.RECEIVER, Condition.EQUALS, "CAT").And()
				.setSelector(MessageTableColumns.TIMESTAMP, Condition.GREATER_THAN, null).And()
				.setSelector(MessageTableColumns.SENDER, Condition.EQUALS, "IBM").getQuery();
		
		DatabaseQuery databaseQuery_2 = DatabaseQueryCreator.setTable(Table.MESSAGES).setType(Type.SELECT).where()
				.setSelector(MessageTableColumns.RECEIVER, Condition.EQUALS, "IBM").And()
				.setSelector(MessageTableColumns.TIMESTAMP, Condition.GREATER_THAN, null).And()
				.setSelector(MessageTableColumns.SENDER, Condition.EQUALS, "CAT").getQuery();
		
		String query = databaseQuery_1 + " UNION " + databaseQuery_2;
		System.out.println(query);
		
		
		
	}

}
