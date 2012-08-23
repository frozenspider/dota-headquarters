package org.dotahq.entity.hero

import org.dotahq.entity.Attribute;
import org.dotahq.entity.Side;

import groovy.transform.EqualsAndHashCode
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Table
import javax.persistence.Column

/**
 * These are the hero "base" stats - those coming out-of-the-box, i.e. stats of a level 1 hero without any items
 * or abilities.
 *
 * @author FS
 */
@EqualsAndHashCode(excludes = "id")
@Entity(name = "hero_base")
class HeroBaseStats implements Serializable {
	private static final long serialVersionUID = 5641654687910723L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id

	String name
	String title

	@Column(name = "min_damage") Integer minDamage
	@Column(name = "max_damage") Integer maxDamage
	@Column(name = "attack_time") BigDecimal attackTime
	Integer range
	Boolean melee

	BigDecimal armor

	Integer moveSpeed

	Integer strn
	Integer agil
	Integer intl
	@Column(name = "strn_growth") BigDecimal strnGrowth
	@Column(name = "agil_growth") BigDecimal agilGrowth
	@Column(name = "intl_growth") BigDecimal intlGrowth
	@Column(name = "main_attr") Attribute mainAttr

	
	String version
	
	@Override
	public String toString() {
		return "HeroBaseStats ($title)"
	}
}
