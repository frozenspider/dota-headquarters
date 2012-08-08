package org.dotahq.entity

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class Damage {
	int from
	int to
	
	public Damage(int from, int to) {
		this.from = from
		this.to = to
	}
	
	def plus(int other){
		new Damage(this.from + other, this.to + other)
	}
	
	def minus(int other){
		new Damage(this.from - other, this.to - other)
	}
	
	public String toString() {
		return "Damage [$from to $to]"
	}
}
