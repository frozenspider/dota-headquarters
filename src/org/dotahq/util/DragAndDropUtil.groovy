package org.dotahq.util

import java.awt.datatransfer.DataFlavor;

class DragAndDropUtil {
	
	public static DataFlavor createDataFlavor(Object data){
		return new DataFlavor(data.getClass(), 'application/x-java-serialized-object')
	}
}
