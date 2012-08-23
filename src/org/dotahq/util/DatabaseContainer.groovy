package org.dotahq.util

import org.dotahq.entity.hero.tavern.Tavern
import org.dotahq.entity.hero.HeroBaseStats
import org.hibernate.Criteria
import org.hibernate.SessionFactory
import org.hibernate.annotations.FetchMode
import org.hibernate.cfg.Configuration

class DatabaseContainer {
	
	private final List<Tavern> taverns
	
	public DatabaseContainer() {
		String dbpath = "data/database"
		if (!new File("${dbpath}.h2.db").exists()) {
			throw new FileNotFoundException("${dbpath}.h2.db")
		}
		
		def dbUrlStr = "jdbc:h2:/${new File(dbpath).getAbsolutePath().replace('\\', '/')}"
		def hibProps = [
					"hibernate.dialect": "org.hibernate.dialect.H2Dialect",
					"hibernate.connection.driver_class": "org.h2.Driver", //"SQLite.JDBCDriver",
					"hibernate.connection.url": dbUrlStr,
					"hibernate.connection.username": "",
					"hibernate.connection.password": "",
					"hibernate.connection.pool_size": "1",
					"hibernate.connection.autocommit": "true",
					"hibernate.cache.provider_class": "org.hibernate.cache.NoCacheProvider",
					"hibernate.hbm2ddl.auto": "none", //"create-drop",
					// "hibernate.show_sql": "true",
					"hibernate.transaction.factory_class": "org.hibernate.transaction.JDBCTransactionFactory",
					"hibernate.current_session_context_class": "thread"
				]
		
		def Closure <Configuration> configureHibernate = { props ->
			def config = new Configuration()
			props.each { k, v -> config.setProperty(k, v) }
			config.addAnnotatedClass(Tavern.class)
			config.addAnnotatedClass(HeroBaseStats.class)
			return config
		}
		
		SessionFactory factory = configureHibernate(hibProps).buildSessionFactory()
		try {
			def session = factory.currentSession
			def tx = session.beginTransaction()
			def dbTaverns = session.createCriteria(Tavern.class).list()
			this.taverns = dbTaverns.collect { Tavern dbTavern ->
				new Tavern([
							id: dbTavern.id,
							side: dbTavern.side,
							name: dbTavern.name,
							heroBases: new ArrayList(dbTavern.heroBases)
						])
			}
			tx.commit()
		} finally {
			factory.close()
		}
	}
}