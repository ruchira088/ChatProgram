package server.database;

import java.util.LinkedList;
import java.util.ListIterator;

import server.database.Database.SearchableAttribute;
import server.database.DatabaseQuery.QueryFragment;
import server.database.DatabaseQuery.QuerySelectors.Selector;

public class DatabaseQuery
{
	public enum Type
	{
		SELECT;
	}

	public enum Table
	{
		MESSAGES;
	}

	public enum Condition
	{
		EQUALS("="), GREATER_THAN(">"), LESS_THAN("<");

		private String m_symbol;

		Condition(String p_symbol)
		{
			m_symbol = p_symbol;
		}

		public String getSymbol()
		{
			return m_symbol;
		}
	}

	public interface QueryFragment
	{

	}

	private Type m_queryType;

	private Table m_queryTable;

	private QuerySelectors m_selectors;

	public DatabaseQuery setType(Type p_queryType)
	{
		m_queryType = p_queryType;
		return this;
	}

	public DatabaseQuery setTable(Table p_queryTable)
	{
		m_queryTable = p_queryTable;
		return this;
	}
	
//	public QueryAnalyzer analyze()
//	{
//		return new QueryAnalyzer();
//	}

	public QuerySelectors where()
	{
		m_selectors = new QuerySelectors();

		return m_selectors;
	}

	DatabaseQuery()
	{
	}

	@Override
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(m_queryType.name());
		stringBuilder.append(" * FROM ");
		stringBuilder.append(m_queryTable + "_TABLE");
		stringBuilder.append(" WHERE ");
		stringBuilder.append(m_selectors);

		return stringBuilder.toString();
	}

//	public class QueryAnalyzer
//	{
//		LinkedList<SearchableAttribute> m_searchableAttributes;
//		
//		public boolean hasSelector(SearchableAttribute p_searchableAttribute)
//		{						
//			return getPosition(p_searchableAttribute) != 0;
//		}
//		
//		public int getPosition(SearchableAttribute p_searchableAttribute)
//		{
//			if(m_searchableAttributes == null)
//			{
//				init();
//			}
//			
//			return m_searchableAttributes.indexOf(p_searchableAttribute) + 1;
//		}
//		
//		private void init()
//		{
//			LinkedList<QueryFragment> queryFragments = m_selectors.getQueryFragments();
//			
//			m_searchableAttributes = new LinkedList<Database.SearchableAttribute>();
//						
//			for (QueryFragment queryFragment : queryFragments)
//			{
//				if(queryFragment instanceof Selector)
//				{
//					Selector<?> selector = (Selector<?>) queryFragment;
//					
//					if(!selector.getAttribute().isStringValue())
//					{
//						m_searchableAttributes.add(selector.getAttribute());						
//					}
//				}				
//			}
//			
//		}
//	}
	
	public class QuerySelectors
	{
		private LinkedList<QueryFragment> m_queryFragments = new LinkedList<QueryFragment>();
		
		public <T> Conjunction setSelector(SearchableAttribute p_searchableAttribute, Condition p_condition, T p_value)
		{
			if(p_value != null)
			{
				m_queryFragments.add(new Selector<T>(p_searchableAttribute, p_condition, p_value));				
			} 
			else 
			{
				if(!m_queryFragments.isEmpty())
				{
					m_queryFragments.removeLast();					
				}
			}
			return new Conjunction();
		}
		
		LinkedList<QueryFragment> getQueryFragments()
		{
			return m_queryFragments;
		}

		public class Conjunction implements QueryFragment
		{
			private String m_string = null;

			public QuerySelectors And()
			{
				m_string = "AND";
				return addConjunction();
			}

			public QuerySelectors Or()
			{
				m_string = "OR";
				return addConjunction();
			}
			
			private QuerySelectors addConjunction()
			{
				if(!m_queryFragments.isEmpty())
				{
					m_queryFragments.add(this);					
				}
				
				return QuerySelectors.this;
			}

			public DatabaseQuery getQuery()
			{
				return QuerySelectors.this.getQuery();
			}

			@Override
			public String toString()
			{
				return m_string == null ? "" : " " + m_string + " ";
			}
		}

		public class Selector<T> implements QueryFragment
		{
			private SearchableAttribute m_attribute;

			private Condition m_condition;

			private T m_value;
			
			private Class<T> m_class;
			
			public Selector(SearchableAttribute p_attribute, Condition p_condition, T p_value)
			{
				m_attribute = p_attribute;
				m_condition = p_condition;
				m_value = p_value;
				m_class = (Class<T>) (p_value != null ? p_value.getClass() : null);
			}
			
			public SearchableAttribute getAttribute()
			{
				return m_attribute;
			}

			@Override
			public String toString()
			{
				StringBuilder stringBuilder = new StringBuilder();
				
				if (m_value != null)
				{
					stringBuilder.append(m_attribute.getAttribute());
					stringBuilder.append(" ");
					stringBuilder.append(m_condition.getSymbol());
					stringBuilder.append(" ");
					
					if(m_class == null || m_class.isAssignableFrom(String.class))
					{
						stringBuilder.append("'" + m_value + "'");
					} else 
					{
						stringBuilder.append("?");
					}
				}

				return stringBuilder.toString();
			}
		}

		private DatabaseQuery getQuery()
		{
			return DatabaseQuery.this;
		}

		@Override
		public String toString()
		{
			StringBuilder stringBuilder = new StringBuilder();

			ListIterator<QueryFragment> listIterator = m_queryFragments.listIterator();
			
			if(listIterator.hasNext())
			{
				stringBuilder.append(listIterator.next());
			}

			while (listIterator.hasNext())
			{
				QueryFragment queryFragment = listIterator.next();
					if(listIterator.hasNext())
					{
						stringBuilder.append(queryFragment);
						stringBuilder.append(listIterator.next());
					}
			}

			return stringBuilder.toString();
		}
	}

}
