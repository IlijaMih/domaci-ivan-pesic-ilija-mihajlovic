package tree;

import resource.implementation.InformationResource;

import javax.swing.tree.DefaultTreeModel;

public interface Tree {     //INTERFEJS ZA STABLO

    DefaultTreeModel generateTree(InformationResource informationResource);
        ///PROSLEDJUJE SE ROOT I ZA NJEGA SE DEFINISU NJEGOVA DECA KOJA CE BITI PRIKAZANA U STABLU
}
