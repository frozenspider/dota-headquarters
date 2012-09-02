package org.dotahq.entity.hero.tavern

import org.dotahq.entity.Side
import groovy.transform.EqualsAndHashCode
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import org.dotahq.entity.hero.HeroBaseStats
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.IndexColumn

import javax.persistence.OneToMany
import javax.persistence.JoinColumn
import javax.persistence.FetchType
import javax.persistence.CascadeType
import javax.persistence.OrderColumn
import javax.persistence.Table

@EqualsAndHashCode(excludes = "id")
@Entity(name = "tavern")
class Tavern implements Serializable {
	private static final long serialVersionUID = 945227514784421963L
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id
	
	Side side
	String name
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "tavern_id")
	List<HeroBaseStats> heroBases
	
	@Override
	public String toString() {
		return name
	}
}
