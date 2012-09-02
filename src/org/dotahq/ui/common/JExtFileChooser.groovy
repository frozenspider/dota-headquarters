package org.dotahq.ui.common

import java.io.File

import javax.swing.JFileChooser
import javax.swing.JOptionPane

class JExtFileChooser extends JFileChooser {
	
	public JExtFileChooser() {
		super()
	}
	
	public JExtFileChooser(File currentDirectory) {
		super(currentDirectory)
	}
	
	@Override
	public File getSelectedFile() {
		File file = super.getSelectedFile()
		if (file != null && !file.name.contains('.')) {
			String ext = fileFilter.extensions[0]
			file = new File(file.getAbsolutePath() + ".$ext")
		}
		return file
	}
	
	@Override
	public void approveSelection(){
		File f = getSelectedFile()
		if(f.exists() && getDialogType() == SAVE_DIALOG){
			int result = JOptionPane.showConfirmDialog(
					this, "This file exists, overwrite?", "Existing file",
					JOptionPane.YES_NO_CANCEL_OPTION)
			switch(result){
				case JOptionPane.YES_OPTION:
					super.approveSelection()
					break
				default:
					cancelSelection()
			}
		} else {
			super.approveSelection()
		}
	}
}
