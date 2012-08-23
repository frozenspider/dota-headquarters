package org.dotahq.util

class CustomUtil {
	
	static List asList(Object... objs){
		new ArrayList(objs as List)
	}
	
	static Map asMap(Object container){
		Map result = container.getProperties()
		result.remove("class")
		result.remove("metaClass")
		return result
	}
}
