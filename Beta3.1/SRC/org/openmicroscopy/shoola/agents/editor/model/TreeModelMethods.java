 /*
 *  org.openmicroscopy.shoola.agents.editor.model.TreeModelMethods 
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2008 University of Dundee. All rights reserved.
 *
 *
 * 	This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *------------------------------------------------------------------------------
 */
package org.openmicroscopy.shoola.agents.editor.model;

//Java imports

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

//Third-party libraries

//Application-internal dependencies

/** 
 * This class contains a number of static methods that are useful for 
 * JTree manipulation and node selection etc. 
 *
 * @author  William Moore &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:will@lifesci.dundee.ac.uk">will@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $Date: $)
 * </small>
 * @since OME3.0
 */
public class TreeModelMethods {
	
	/**
	 * This method adds the nodes specified as children of the parent node,
	 * starting at the index specified. 
	 * 
	 * @param treeModel		The treeModel being edited. Will notify JTree of update
	 * @param nodes		The list of nodes to add as children
	 * @param parent	The parent to accept the nodes as children
	 * @param index		The index to begin adding the children
	 */
	public static void insertNodesInto(DefaultTreeModel treeModel,
			List<MutableTreeNode> nodes, 
			MutableTreeNode parent,
			int index) 
	{
		
		// If the index = childCount, nodes will be added after the last child.
		// Make sure that the index is not greater (will cause Exception)
		if(index > parent.getChildCount()) {
			index = parent.getChildCount();
		}
		// Go through the list of nodes, adding them at incremental index position
		for (MutableTreeNode node: nodes) {
			treeModel.insertNodeInto(node, parent, index);
			index++;
		}
	}
	
	/**
	 * This method will remove the List of nodes specified from their parent.
	 * 
	 * @param treeModel		The TreeModel being edited. Will notify JTree of update
	 * @param nodes		The nodes to be removed from their parent (don't 
	 * 				need to have the same parent)
	 */
	public static void removeNodesFromParent(DefaultTreeModel treeModel,
			List<MutableTreeNode> nodes) 
	{	
		for (MutableTreeNode node: nodes) {
			treeModel.removeNodeFromParent(node);
		}
	}
	
	/**
	 * Set the selected nodes in the JTree.
	 * This builds an array of TreePath[] and calls setSelectedPaths on JTree
	 * It will then call scrollPathToVisible(path) on the first path, so
	 * as to display it if it's in a ScrollPane. 
	 * NB. nodes need to be instances of DefaultMutableTreeNode for this
	 * method to work. Nodes that cannot be cast to DefaultMutableTreeNodes
	 * will be ignored.
	 * 
	 * @param nodes		The List of nodes to select	
	 * @param tree		The JTree in which these nodes exist
	 */
	public static void selectNodes(List<MutableTreeNode> nodes, JTree tree) 
	{
		if (tree == null) return;
		
		TreePath[] paths = new TreePath[nodes.size()];
		int index = 0;
		for (TreeNode node : nodes) {
			if (node instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode dnode = (DefaultMutableTreeNode)node;
				paths[index++] = new TreePath(dnode.getPath());
			}
		}
		tree.setSelectionPaths(paths);
		
		if (paths.length > 0) {
			tree.scrollPathToVisible(paths[0]);
		}
	}
	
	/**
	 * Sets the selected nodes in the tree.
	 * Delegates to {@link #selectNodes(List, JTree)}
	 * 
	 * @param nodes		The List of nodes to select	
	 * @param tree		The JTree in which these nodes exist
	 */
	public static void selectDNodes(List<DefaultMutableTreeNode> nodes, JTree tree) 
	{
		ArrayList<MutableTreeNode> mtNodes = new ArrayList<MutableTreeNode>();
		for (MutableTreeNode node : nodes) {
			mtNodes.add(node);
		}
		selectNodes(mtNodes, tree);
	}
	
	/**
	 * Set the selected node in the JTree, and scroll to visible.
	 * This method delegates to selectNodes(nodes, tree).
	 * 
	 * @see selectNodes(List<TreeNode> nodes, JTree tree)
	 * 
	 * @param node		The node to select	
	 * @param tree		The JTree in which these nodes exist
	 */
	public static void selectNode(TreeNode node, JTree tree) 
	{
		List<MutableTreeNode> nodes = new ArrayList<MutableTreeNode>();
		if (node instanceof MutableTreeNode) {
			nodes.add((MutableTreeNode)node);
			selectNodes(nodes, tree);
		}
	}
	
	
	/**
	 * Duplicates the child of oldNode and adds them to newNode.
	 * Also recursively calls this method for child nodes to copy
	 * the entire sub-tree of oldNode to newNode. 
	 * This method casts the User Object from each node to a 
	 * Field and duplicates this object using Field.clone() before
	 * placing this in a new node. 
	 * 
	 * @param oldNode	The existing node with children to copy
	 * @param newNode	The new node, with no children added yet.
	 */
	public static void duplicateNode(DefaultMutableTreeNode oldNode,
			DefaultMutableTreeNode newNode) 
	{
		
		for(int i=0; i<oldNode.getChildCount(); i++) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)
				oldNode.getChildAt(i);
			Field oldField = (Field)child.getUserObject();
			Field newField = (Field)oldField.clone();
			
			DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(newField);
			newNode.add(newChild);
			
			duplicateNode(child, newChild);
		}
	}
	
	/**
	 * This method 'indents' the nodes to the right, by
	 * making them all children of the first node's previous sibling. 
	 * It is intended that all the nodes
	 * in the list are contiguous siblings. 
	 * If the first node has no previous sibling, nothing happens. 
	 * 
	 * @param nodes		The list of nodes to move.
	 * @param treeModel		The treeModel being edited. Will notify JTree of update
	 * 
	 */
	public static void indentNodesRight(List<DefaultMutableTreeNode> nodes,
			DefaultTreeModel treeModel)
	{
		if (nodes == null)		return;
		if (nodes.isEmpty())	return;
		
		DefaultMutableTreeNode firstNode = nodes.get(0);
		
		// fields need to become children of their preceding sibling 
		DefaultMutableTreeNode previousSibling = firstNode.getPreviousSibling();
		
		// if no previous sibling, can't indent
		if (previousSibling == null) return;
		
		// move each node to be a child of the previous sibling
		for (DefaultMutableTreeNode node: nodes) {
			previousSibling.add(node);
		}
		treeModel.nodeStructureChanged(previousSibling.getParent());
	}
	
	/**
	 * Indents nodes to the left in the tree structure (move to a higher level).
	 * Nodes become siblings of their parent.
	 * Siblings of the nodes stay at the same level in the tree:
	 *  - Previous siblings are not moved
	 *  - Subsequent siblings become children of the last node before it is 
	 *  	promoted (indented left)
	 * 
	 * @param nodes		The list of nodes to indent left (move to a higher 
	 * 					level in the tree hierarchy). 
	 * @param treeModel		The treeModel being edited. Will notify JTree of update
	 */
	public static void indentNodesLeft(List<DefaultMutableTreeNode> nodes,
			DefaultTreeModel treeModel) 
	{
		if (nodes == null)		return;
		if (nodes.isEmpty())	return;
		
		DefaultMutableTreeNode firstNode = nodes.get(0);
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)
													firstNode.getParent();
		if (parentNode == null)	return;
		DefaultMutableTreeNode grandParentNode = (DefaultMutableTreeNode)
													parentNode.getParent();

		// if parent is root (grandparent null) then can't promote
		if (grandParentNode == null)	return;
		
		// any fields that are subsequent siblings of the last to be promoted, 
		// must first become children of that node. 
		DefaultMutableTreeNode lastNode = nodes.get(nodes.size()-1);
		DefaultMutableTreeNode lastNodeSibling = lastNode.getNextSibling();
		while (lastNodeSibling != null) {
			lastNode.add(lastNodeSibling);
			lastNodeSibling = lastNode.getNextSibling();
		}
		
		// now you can indent nodes left
		for (DefaultMutableTreeNode node: nodes) {
			indentNodeLeft(node);
		}
		treeModel.nodeStructureChanged(grandParentNode);
	}
	
	/**
	 * Indents a single node to the left (one level higher in the tree 
	 * hierarchy). 
	 * The node will become the next sibling of it's parent.
	 * If the node has no grandparent, nothing happens.
	 * 
	 * @param node		The node to indent (promote to sibling of it's parent)
	 * @param treeModel		The treeModel being edited. Will notify JTree of update
	 */
	public static void indentNodeLeft(DefaultMutableTreeNode node)
	{
		if (node == null)  	return;
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)
												node.getParent();
		if (parentNode == null)		return;
		DefaultMutableTreeNode grandParentNode = (DefaultMutableTreeNode)
													parentNode.getParent();
		if (grandParentNode == null)		return;
		
		int indexOfParent = grandParentNode.getIndex(parentNode);
		
		// adds after parent
		grandParentNode.insert(node, indexOfParent + 1);
	}
	
}
