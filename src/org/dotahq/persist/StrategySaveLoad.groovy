package org.dotahq.persist

import java.util.Formatter.Conversion

import org.dotahq.entity.StrategyLayout
import org.dotahq.entity.hero.HeroBaseStats
import org.dotahq.entity.hero.strategy.LanePosition

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.converters.ConversionException
import com.thoughtworks.xstream.converters.Converter
import com.thoughtworks.xstream.converters.MarshallingContext
import com.thoughtworks.xstream.converters.UnmarshallingContext
import com.thoughtworks.xstream.io.HierarchicalStreamReader
import com.thoughtworks.xstream.io.HierarchicalStreamWriter

/**
 * Saves and loads the strategy to/from XML.
 * <p>
 * Saves and loads entire hero data, so it may easily become out of synch with actual database.
 * 
 * @author FS
 */
class StrategySaveLoad {
	
	XStream xstream
	
	public StrategySaveLoad(){
		this.xstream =  new XStream()
		this.xstream.alias("strategy", StrategyLayout)
		this.xstream.alias("heroBase", HeroBaseStats)
		this.xstream.classLoader = getClass().classLoader
		this.xstream.registerConverter(new StrategyLayoutConverter())
	}
	public void saveToFile(File file, StrategyLayout strategy) {
		file.createNewFile()
		String xml = saveToXml(strategy)
		file.write(xml)
	}
	
	public StrategyLayout loadFromFile(File file) {
		String xml = file.getText()
		StrategyLayout result = loadFromXml(xml)
		return result
	}
	
	public String saveToXml(StrategyLayout strategy) {
		String result = xstream.toXML(strategy)
		return result
	}
	
	public StrategyLayout loadFromXml(String xml) {
		StrategyLayout result = xstream.fromXML(xml)
		return result
	}
	
	class StrategyLayoutConverter implements Converter {
		
		@Override
		public boolean canConvert(Class arg0) {
			return arg0 == StrategyLayout.class
		}
		
		@Override
		public void marshal(Object obj, HierarchicalStreamWriter writer, MarshallingContext context) {
			Map <HeroBaseStats, LanePosition> lanes = ((StrategyLayout)obj).heroesToLanesMap()
			writer.startNode("comment")
			writer.setValue(((StrategyLayout)obj).comment)
			writer.endNode()
			for (e in lanes) {
				writer.startNode("heroBase")
				writer.addAttribute("lane", e.value.toString().toLowerCase())
				context.convertAnother(e.key)
				writer.endNode()
			}
		}
		
		@Override
		public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
			StrategyLayout strat = new StrategyLayout()
			reader.moveDown()
			strat.setComment(reader.getValue())
			reader.moveUp()
			while (reader.hasMoreChildren()) {
				reader.moveDown()
				LanePosition lane
				try {
					lane = LanePosition.valueOf(reader.getAttribute("lane").toUpperCase())
				} catch (ex) {
					throw new ConversionException("No such lane: " + reader.getAttribute("lane"), ex)
				}
				HeroBaseStats heroBase = context.convertAnother(strat, HeroBaseStats)
				if (!heroBase)
					throw new ConversionException("Unable to parse heroBase for " + reader.getValue())
				if (strat.containsHero(heroBase))
					throw new ConversionException("Duplicate hero found")
				strat.put(heroBase, lane)
				reader.moveUp()
			}
			return strat
		}
	}
}
