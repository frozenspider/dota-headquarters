/**
 * STEP 4: Using parsed hero stats, post them to a database.
 *
 * @author FS
 */

package org.dotahq.playdota

import static org.dotahq.playdota.ScriptPath.folder

import org.dotahq.entity.hero.HeroBaseStats
import org.dotahq.entity.hero.tavern.Tavern
import org.hibernate.cfg.Configuration

File tavernsFile = new File("serializedHeroData", folder)
if (!tavernsFile.exists()) {
	throw new IllegalStateException("\"${tavernsFile.getAbsolutePath()}\" don't exist, you should parse the hero pages.")
}

File dbFile = new File("heroes-parsed-db.sqlite", folder)
if (dbFile.exists()) {
	throw new IllegalStateException("\"${dbFile.getAbsolutePath()}\" already exist. Are we already done here?")
}

def dbUrlStr = "jdbc:sqlite:/${dbFile.getAbsolutePath().replace('\\', '/')}"

//"org.sqlite.JDBC"
//heroesSql = Sql.newInstance(dbUrlStr, "", "", "SQLite.JDBCDriver");
//heroesSql.getConnection().autoCommit = false
//heroesSql.execute('''CREATE TABLE taverns (
//    id integer not null primary key,
//    name varchar(20)
//)''')
//heroesSql.execute('''CREATE TABLE heroes (
//    id integer not null primary key,
//    name varchar(50) not null,
//    title varchar(50) not null,
//)''')
//
//heroesSql.getConnection().close()

def hibProps = [
	"hibernate.dialect": "org.hibernate.dialect.SQLiteDialect",
	"hibernate.connection.driver_class": "SQLite.JDBCDriver",
	"hibernate.connection.url": dbUrlStr,
	"hibernate.connection.username": "",
	"hibernate.connection.password": "",
	"hibernate.connection.pool_size": "1",
	"hibernate.connection.autocommit": "true",
	"hibernate.cache.provider_class": "org.hibernate.cache.NoCacheProvider",
	"hibernate.hbm2ddl.auto": "create", //"create-drop",
	"hibernate.show_sql": "true",
	"hibernate.transaction.factory_class": "org.hibernate.transaction.JDBCTransactionFactory",
	"hibernate.current_session_context_class": "thread"
]


def configureHibernate(props) {
	def config = new Configuration()
	props.each { k, v -> config.setProperty(k, v) }
	config.addAnnotatedClass(Tavern)
	config.addAnnotatedClass(HeroBaseStats)
	return config
}

def factory = configureHibernate(hibProps).buildSessionFactory()
def session = factory.currentSession
def tx = session.beginTransaction()

def taverns = tavernsFile.withObjectInputStream { it.readObject() }
for (Tavern tavern in taverns) {
	session.save(tavern)
}

tx.commit()