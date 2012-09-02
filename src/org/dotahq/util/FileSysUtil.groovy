package org.dotahq.util

class FileSysUtil {
	
	static String strategiesExt = "strat"
	
	private static File makeDir(String dir){
		File result = new File(dir)
		result.mkdirs()
		return result
	}
	
	static File getDatabaseDir(){
		return makeDir("data")
	}
	
	static File getStrategiesDir(){
		return makeDir("saves/strategies")
	}
	
	static File getImagesDir(){
		return makeDir("images")
	}
	
	static File getImagesHeroesDir(){
		return makeDir("images/heroes")
	}
}
