package org.dotahq.ui.strategy

import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException

import org.dotahq.util.DragAndDropUtil

class DataTransferable implements Transferable {
	private final Object data
    private final Object owner
	
	public DataTransferable(Object data, Object owner){
		this.data = data
        this.owner = owner
	}
	
	public DataFlavor[] getTransferDataFlavors(){
		def result = new DataFlavor[1]
		result[0] = DragAndDropUtil.createDataFlavor(data)
		return result
	}
	
	public boolean isDataFlavorSupported(DataFlavor flavor){
		return flavor == DragAndDropUtil.createDataFlavor(data)
	}
	
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
		if (flavor != DragAndDropUtil.createDataFlavor(data)) {
			throw new UnsupportedFlavorException()
		}
		return [data, owner]
	}
}