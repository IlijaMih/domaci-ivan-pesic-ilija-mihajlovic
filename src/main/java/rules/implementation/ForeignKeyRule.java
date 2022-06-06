package rules.implementation;

import gui.MainFrame;
import resource.DBNode;
import resource.DBNodeComposite;
import resource.enums.ConstraintType;
import resource.implementation.Attribute;
import resource.implementation.AttributeConstraint;
import resource.implementation.Entity;
import resource.implementation.InformationResource;
import rules.Rule;
import tree.Tree;
import utils.Config;
import utils.Table;

import javax.swing.tree.DefaultTreeModel;

public class ForeignKeyRule implements Rule {

    @Override
    public String check(String query) {

//        String tableName = findTableName(query);
//        Entity entity = (Entity) findTable(tableName);
//        String fkName = findFKName(query);
//        Attribute attribute = (Attribute) findColumn(entity,fkName);
//
//        return isFk(attribute, fkName);
        String tableName = findTableName(query);
        Table table = findTableByName(tableName);
        String primKey = findFKName(query);
        if(isPrimaryKey(primKey,table)) {
            return null;
        }
        return getFixMessage();
    }

    @Override
    public String getFixMessage() {
        String message = "Wrong foreign key!";
        return message;
    }

    private String findTableName(String query){
        String from[] = query.split("from ");
        String join[] = from[1].split(" join ");
        String dot[] = join[0].split("\\.");
        String space[] = dot[1].split(" ");
        String tableName = space[0];

        return tableName;
    }

    private String findFKName(String query){
        String on[] = query.split("on ");
        for (int i = 0; i < on.length; i++) {
            System.out.println(on[i]);
        }
        String eq[] = on[1].split("=");
        for (int i = 0; i < eq.length; i++) {
            System.out.println(eq[i]);
        }
        String dot[] = eq[0].split("\\.");
        for (int i = 0; i < dot.length; i++) {
            System.out.println(dot[i]);
        }
        String space[] = dot[1].split(" ");
        String fkName = space[0];

        return fkName;
    }

    private DBNodeComposite findTable(String tableName) {
        DefaultTreeModel defaultTreeModel = MainFrame.getInstance().getDefaultTreeModel();
        Object root = defaultTreeModel.getRoot();
        InformationResource ir = null;
        if (root instanceof InformationResource) {
            ir = (InformationResource) root;
        }
        for (DBNode entity : ir.getChildren()) {
            if (entity.getName().equals(tableName)) {
                return (DBNodeComposite) entity;
            }
        }

        return null;
    }

    private DBNode findColumn(Entity entity, String fkName){
        for (DBNode fkCol: entity.getChildren()){
            if (fkCol.getName().equals(fkName)){
                return fkCol;
            }
        }
        return null;
    }

    private boolean isFk(Attribute attribute, String fkName){
        for (DBNode fkCol: attribute.getChildren()){
            if (fkCol.getName().equals(fkName)){
                AttributeConstraint attributeConstraint = (AttributeConstraint) fkCol;
                if(attributeConstraint.getConstraintType().equals(ConstraintType.PRIMARY_KEY)){
                    return true;
                }
            }
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////
    private Table findTableByName(String name) {
        System.out.println("TRAZI SE TABELA SA IMENOM "+name);
        for (Table t : Config.tables) {
            if (t.getName().equals(name)) {
                System.out.println("Pronadjena tabela "+t.getName());
                return t;
            }
        }
        return null;
    }

    private boolean isPrimaryKey(String primaryKey, Table table){
        for (String key: table.getPrimaryKeys()){
            System.out.println(key + " trenutni kljuc koji se proverava sa duzinom " + key.length());
            System.out.println(primaryKey + " prosledjeni kljuc koji se proverava sa duzinom " +primaryKey.length());
            if (key.equals(primaryKey)){
                System.out.println("JESTE PRIMARNI KLJUC");

                return true;
            }
        }
        System.out.println("NIJE PRIMARNI KLJUC");
        return false;
    }
}
