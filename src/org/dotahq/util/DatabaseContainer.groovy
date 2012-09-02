package org.dotahq.util

import org.dotahq.entity.hero.tavern.Tavern
import org.dotahq.entity.hero.HeroBaseStats
import org.hibernate.Criteria
import org.hibernate.SessionFactory
import org.hibernate.annotations.FetchMode
import org.hibernate.cfg.Configuration

class DatabaseContainer {
	
	final List<Tavern> taverns
	
	public DatabaseContainer() {
		File dbDir = FileSysUtil.databaseDir
		File dbFile = new File(dbDir, "database.h2.db")
		File dbFilePathNoExt = new File(dbDir, "database")
		if (!dbFile.exists()) {
			throw new FileNotFoundException(dbFile.getName())
		}
		
		def dbUrlStr = "jdbc:h2:/${dbFilePathNoExt.getAbsolutePath().replace('\\', '/')}"
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
	
	public HeroBaseStats update(HeroBaseStats unsynched){
		HeroBaseStats current
		
		// Try #1: find by title
		current = findByTitle(unsynched.title)
		if (current) {
			return current
		}
		
		// Try #2: if fails, find by name
		current = findByName(unsynched.name)
		if (current) {
			return current
		}
		
		// Failed
		return null
	}
	
	private HeroBaseStats findByName(String name) {
		for (tavern in taverns) {
			for (heroBase in tavern.heroBases) {
				if (heroBase.name == name) {
					return heroBase
				}
			}
		}
		return null
	}
	
	private HeroBaseStats findByTitle(String title) {
		for (tavern in taverns) {
			for (heroBase in tavern.heroBases) {
				if (heroBase.title == title) {
					return heroBase
				}
			}
		}
		return null
	}
}