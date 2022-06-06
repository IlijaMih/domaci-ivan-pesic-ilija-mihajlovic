package tree.implementation;

import resource.DBNode;
import resource.DBNodeComposite;
import resource.implementation.InformationResource;
import tree.Tree;
import tree.TreeItem;

import javax.swing.tree.DefaultTreeModel;
import java.util.List;

public class TreeImplementation implements Tree {

    @Override
    public DefaultTreeModel generateTree(InformationResource informationResource) {
                /////PRAVI JEDAN KORENSKI CVOR
        TreeItem root = new TreeItem(informationResource, informationResource.getName());
        connectChildren(root);
        return new DefaultTreeModel(root);
        //VRACA DEFAULT TREE MODEL SA PROSLEDJENIM ROOT-OM, TAKO DA JTREE ZNA SAM DA PRESLOZI SVE PO STABLU
    }


    private void connectChildren(TreeItem current){
        //////PROLAZI KROZ SVU DECU ROOT-A I ZA SVAKOG PRAVI TREE ITEM I UVEZUJE STABLO
        if (!(current.getDbNode() instanceof DBNodeComposite)) return;

        List<DBNode> children = ((DBNodeComposite) current.getDbNode()).getChildren();
        for (int i = 0; i<children.size();i++){
            TreeItem child = new TreeItem(children.get(i), children.get(i).getName());
            current.insert(child,i);
            connectChildren(child);
        }

    }

}
