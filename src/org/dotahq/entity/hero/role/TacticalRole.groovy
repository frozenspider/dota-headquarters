package org.dotahq.entity.hero.role;

enum TacticalRole {
	/** Destroys towers */
	PUSHER,
	/** Strong AOE that can stop enemy push */
	DEFENDER,
	/** Farms for a long time, but can win the game alone */
	HARDCARRY,
	/** Mostly shines in mid-game. Can be almost as effective, as hard carry. */
	SEMICARRY,
	/** Supports an allies with buffs/heal/etc. */
	SUPPORT,
	/** Have stun/silence/hex/any other disabling ability */
	DISABLER,
	/** Engages the enemy */
	INITIATOR,
	/** Item independent, can buy and place wards */
	WARDBITCH,
	/** Kills enemy heroes early game */
	GANKER,
	/** Makes lane partner's life easier */
	BABYSITTER,
	/** Has some handy way of revealing the fog of war */
	SCOUT,
}
