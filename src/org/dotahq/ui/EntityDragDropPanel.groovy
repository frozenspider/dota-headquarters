package org.dotahq.ui

import static org.dotahq.util.IconUtil.*

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

class EntityDragDropPanel extends RenderingPanel {

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

    EntityDragDropPanel allowDrag(boolean copy) {
        if (!dragEnabled) {
            DragSource ds = DragSource.getDefaultDragSource()
            ds.createDefaultDragGestureRecognizer(this, copy ? TH.COPY : TH.MOVE, new IconGestureListener(owner: this))
        }
        return this
    }

    EntityDragDropPanel allowDrop(List<Class> expectedClasses, Closure callback) {
        if (!dropEnabled) {
            this.setDropTarget(new IconDropTarget(expectedClasses, callback))
        }
        return this
    }

    class IconGestureListener implements DragGestureListener {
        EntityDragDropPanel owner

        public void dragGestureRecognized(DragGestureEvent event) {
            def cursor = Toolkit.getDefaultToolkit().createCustomCursor(getImage(), new Point(0, 0), "IconDragger")
            event.startDrag(cursor, new DataTransferable(data, owner))
        }
    }

    class IconDropTarget extends DropTarget {

        private final List<DataFlavor> dataFlavors
        private final Closure callback

        public IconDropTarget(List<Class> expectedClasses, Closure callback) {
            this.dataFlavors = expectedClasses.collect { DragAndDropUtil.createDataFlavor(it) }
            this.callback = callback
        }

        private List acceptable(def dropTargetEvent) {
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
            println "dragEnter"
        }

        @Override
        public synchronized void dragExit(DropTargetEvent dte) {
            super.dragExit(dte)
            println "dragExit"
        }

        @Override
        public synchronized void drop(DropTargetDropEvent dtde) {
            println "Drop start"
            List acceptable = acceptable(dtde)
            if (!acceptable) {
                dtde.rejectDrop()
                return
            }
            try {
                def data = dtde.getTransferable().getTransferData(acceptable[0])
                callback(data)
                dtde.acceptDrop(dtde.getDropAction())
                println "Drop successful"
            } catch (ex) {
                dtde.rejectDrop()
                ex.printStackTrace()
            }
        }
    }
}
