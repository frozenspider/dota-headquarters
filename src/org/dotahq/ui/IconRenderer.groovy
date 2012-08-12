package org.dotahq.ui

import static org.dotahq.util.CustomUtil.*
import static org.dotahq.util.IconUtil.*

import java.awt.Color
import java.awt.Cursor
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

import javax.swing.JPanel
import javax.swing.TransferHandler

import org.dotahq.entity.hero.HeroBaseStats

class IconRenderer extends JPanel {
	
	private BufferedImage icon
	private Object data
	private Color cl
	
	public IconRenderer(){
		this.icon = emptyIcon
		this.setPreferredSize(new Dimension(icon.getWidth(), icon.getHeight()))
		DragSource ds = DragSource.getDefaultDragSource()
		ds.createDefaultDragGestureRecognizer(this, TransferHandler.COPY, new IconGestureListener())
		
		this.setDropTarget(new IconDropTarget())
		cl = getBackground()
	}
	
	void setData(Object data, BufferedImage icon){
		this.data = data
		this.icon = icon ?: emptyIcon
	}
	
	class IconGestureListener implements DragGestureListener {
		public void dragGestureRecognized(DragGestureEvent event){
			def cursor = Toolkit.getDefaultToolkit().createCustomCursor(icon, new Point(0,0), "IconDragger")
			event.startDrag(cursor, new DataTransferrable(data))
		}
	}
	
	
	class IconDropTarget extends DropTarget {
		
		@Override
		public synchronized void dragEnter(DropTargetDragEvent dtde) {
			println "dragEnter"
			IconRenderer.this.setBackground(Color.red)
			super.dragEnter(dtde)
		}
		
		@Override
		public synchronized void dragExit(DropTargetEvent dte) {
			println "dragExit"
			IconRenderer.this.setBackground(cl)
			super.dragExit(dte)
		}
		
		@Override
		public synchronized void drop(DropTargetDropEvent dtde) {
			println "drop"
			// TODO Auto-generated method stub
			super.drop(dtde)
		}
		
	}
}
