package org.dotahq.entity

import static org.junit.Assert.*

import org.junit.Test

class DamageTest {
	def damage = { from, to -> new Damage(from, to)}
	
	@Test
	public void testDamage() {
		assert damage(1, 2) == damage(1, 2)
		assert damage(1, 2) != damage(1, 3)
		assert damage(1, 2) != damage(2, 2)
		assert damage(1, 2) != damage(2, 1)
	}
	
	@Test
	public void testPlus() {
		assert damage(1, 2) + 3 == damage(4, 5)
	}
	
	@Test
	public void testMinus() {
		assert damage(4, 5) - 3 == damage(1, 2)
	}
}
