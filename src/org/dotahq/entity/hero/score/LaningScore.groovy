package org.dotahq.entity.hero.score;

enum LaningScore {
	/** Handling a lane against multiple opponents */
	SOLO,
	/** Holding a line with a single partner */
	DUAL,
	/** Being a part of a tri-lane */
	TRIPLE,
	/** No lane - wandering around the map, killing enemies and/or hindering their farm */
	ROAMER,
	/** Farming in jungle starting from level 1 or 2 */
	JUNGLER,
}
