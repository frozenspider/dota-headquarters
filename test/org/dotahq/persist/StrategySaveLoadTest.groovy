package org.dotahq.persist

import static org.junit.Assert.*

import org.dotahq.entity.StrategyLayout
import org.dotahq.entity.hero.HeroBaseStats as HBS
import org.dotahq.entity.hero.strategy.LanePosition as LP
import org.junit.Test

class StrategySaveLoadTest {
	
	StrategySaveLoad persist = new StrategySaveLoad()
	
	@Test
	void saveEmpty() {
		StrategyLayout layout = new StrategyLayout()
		String xml = persist.saveToXml(layout)
		StrategyLayout layout2 = persist.loadFromXml(xml)
		assert layout == layout2
	}
	
	@Test
	void saveSingle() {
		StrategyLayout layout = new StrategyLayout()
		layout.putIfNew(new HBS(id: 5, name: "Hero1", title: "Title1"), LP.MID)
		String xml = persist.saveToXml(layout)
		assert xml == """
<strategy>
  <comment></comment>
  <heroBase lane="mid">
    <id>5</id>
    <name>Hero1</name>
    <title>Title1</title>
  </heroBase>
</strategy>
""".trim()
		StrategyLayout layout2 = persist.loadFromXml(xml)
		assert layout == layout2
	}
	
	@Test
	void saveMultiple() {
		StrategyLayout layout = new StrategyLayout()
		layout.putIfNew(new HBS(id: 5, name: "Hero1", title: "Title1", version: "6.66"), LP.MID)
		layout.putIfNew(new HBS(id: 4, name: "Hero3", title: "Title3", armor: 3.141g), LP.JUNGLE_ENEMY)
		layout.putIfNew(new HBS(id: 6, name: "Hero2", title: "Title2", range: 666), LP.MID)
		layout.setComment("THIS\nIS\nMY\nCOMMENT!")
		String xml = persist.saveToXml(layout)
		// println xml
		StrategyLayout layout2 = persist.loadFromXml(xml)
		assert layout == layout2
	}
}