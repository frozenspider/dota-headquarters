package org.dotahq.entity.item

class ItemSet {
	private final List <Item> items = [null, null, null, null, null, null]

	public Item getItemAt(int idx) {
		if (idx < 0 || idx >= 6) throw new IndexOutOfBoundsException("idx = $idx")
		return items[idx]
	}

	public void setItemAt(int idx, Item item) {
		if (idx < 0 || idx >= 6) throw new IndexOutOfBoundsException("idx = $idx")
		items[idx] = item
	}
}
