package com.girbola.imageviewer.imageviewer;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.TreeItem;

public class FileTreeView extends Task<TreeItem<File>> {
	private File file;

	@Override
	protected TreeItem<File> call() throws Exception {
		if (file.exists()) {
			System.out.println("buildFileSystemBrowser is: " + file);
			TreeItem<File> root = createNode(file);
			return root;
		}
		return null;
	}

	public FileTreeView(File aFile) {
		this.file = aFile;
	}

	private TreeItem<File> createNode(final File f) {
		return new TreeItem<File>(f) {
			private boolean isLeaf;
			private boolean isFirstTimeChildren = true;
			private boolean isFirstTimeLeaf = true;

			@Override
			public ObservableList<TreeItem<File>> getChildren() {
				if (isFirstTimeChildren) {
					isFirstTimeChildren = false;
					super.getChildren().setAll(buildChildren(this));
				}
				return super.getChildren();
			}

			@Override
			public boolean isLeaf() {
				if (isFirstTimeLeaf) {
					isFirstTimeLeaf = false;
					File f = (File) getValue();
					isLeaf = f.isFile();
				}
				return isLeaf;
			}

			private ObservableList<TreeItem<File>> buildChildren(TreeItem<File> TreeItem) {
				File f = TreeItem.getValue();
				if (f != null && f.isDirectory()) {
					File[] files = f.listFiles();
					if (files != null) {
						ObservableList<TreeItem<File>> children = FXCollections.observableArrayList();
						for (File childFile : files) {
							if (childFile.isDirectory()) {
								TreeItem.setExpanded(true);
								children.add(createNode(childFile));
							}
						}
						return children;
					}
				}
				return FXCollections.emptyObservableList();
			}
		};
	}
}
