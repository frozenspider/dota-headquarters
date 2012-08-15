package org.dotahq.ui

import static org.dotahq.util.ImageUtil.*

import java.awt.Dimension
import java.awt.Point
import java.awt.Toolkit
import java.awt.dnd.DragGestureEvent
import java.awt.dnd.DragGestureListener
import java.awt.dnd.DragSource
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetDragEvent
import java.awt.dnd.DropTargetDropEvent
import java.awt.dnd.DropTargetEvent
import java.awt.image.BufferedImage

import javax.swing.TransferHandler as TH

import java.awt.datatransfer.DataFlavor
import org.dotahq.util.DragAndDropUtil
import java.awt.dnd.DragSourceListener
import javax.swing.border.LineBorder
import java.awt.Color
import java.awt.dnd.DragSourceAdapter

class EntityDragDropPanel extends ImageRenderingPanel {

	private boolean dragEnabled = false
	private boolean dropEnabled = false
	private Object data

	public EntityDragDropPanel() {
		setData(null, null)
	}

	EntityDragDropPanel setData(Object data, BufferedImage image) {
		this.data = data
		this.image = image ?: emptyIcon
		this.setPreferredSize(new Dimension(this.image.width, this.image.height))
		return this
	}

	Object getData() {
		return data
	}

	EntityDragDropPanel allowDrag(boolean copy, Closure dragDropEndAction = null) {
		if (dragEnabled) throw new IllegalStateException("Drag already enabled")
		def action = copy ? TH.COPY : TH.MOVE
		def dragSourceListener = [dragDropEnd: dragDropEndAction ?: {}] as DragSourceAdapter
		def dragGestureListener = new IconDragGestureListener(owner: this, listener: dragSourceListener)
		DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(this, action, dragGestureListener)
		return this
	}

	EntityDragDropPanel allowDrop(List<Class> expectedClasses, Closure dropCallback) {
		if (dropEnabled) throw new IllegalStateException("Drop already enabled")
		this.setDropTarget(new IconDropTarget(expectedClasses, dropCallback, this))
		return this
	}

	private class IconDragGestureListener implements DragGestureListener {
		EntityDragDropPanel owner
		DragSourceListener listener

		public void dragGestureRecognized(DragGestureEvent event) {
			def cursor = Toolkit.getDefaultToolkit().createCustomCursor(getImage(), new Point(0, 0), "IconDragger")
			event.startDrag(cursor, new DataTransferable(data, owner), listener)
		}
	}

	private class IconDropTarget extends DropTarget {

		private final List<DataFlavor> dataFlavors
		private final Closure dropCallback
		private final EntityDragDropPanel owner
		private defaultBorder

		public IconDropTarget(List<Class> expectedClasses, Closure dropCallback, EntityDragDropPanel owner) {
			this.dataFlavors = expectedClasses.collect { DragAndDropUtil.createDataFlavor(it) }
			this.dropCallback = dropCallback
			this.owner = owner
			this.defaultBorder = owner.border
		}

		private List<DataFlavor> acceptable(def dropTargetEvent) {
			if (!active) return []
			return dataFlavors.intersect(dropTargetEvent.getCurrentDataFlavors() as List)
		}

		@Override
		public synchronized void dragEnter(DropTargetDragEvent dtde) {
			super.dragEnter(dtde)
			if (!acceptable(dtde)) {
				dtde.rejectDrag()
				return
			}
			defaultBorder = owner.border
			owner.border = new LineBorder(Color.green, 2)
		}

		@Override
		public synchronized void dragExit(DropTargetEvent dte) {
			super.dragExit(dte)
			owner.border = defaultBorder
		}

		@Override
		public synchronized void drop(DropTargetDropEvent dtde) {
			owner.border = defaultBorder
			def flavor = acceptable(dtde)?.first()
			if (!flavor) {
				dtde.dropComplete(false)
				return
			}
			try {
				def (hero, caller) = dtde.getTransferable().getTransferData(flavor)
				boolean success = dropCallback(hero, caller)
				dtde.dropComplete(success)
				println "Drop ${success ? '' : 'un'}successful"
			} catch (ex) {
				ex.printStackTrace()
				dtde.dropComplete(false)
			}
		}
	}
}
