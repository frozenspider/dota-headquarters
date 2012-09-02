package org.dotahq.util

class FileSysUtil {
	
	static String strategiesExt = "strat"
	
	static File getStrategiesDir(){
		File result = new File("saves/strategies")
		result.mkdirs()
		return result
	}
}
