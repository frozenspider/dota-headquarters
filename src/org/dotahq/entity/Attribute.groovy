package org.dotahq.entity

public enum Attribute {
	STR, AGI, INT
	
	def asType(Class type){
		(type in [Integer.class, Integer.TYPE]) ? ordinal() : super.asType(type)
	}
}
