package resource.implementation;

import lombok.*;
import resource.DBNode;
import resource.DBNodeComposite;
import resource.enums.AttributeType;

import java.util.List;

@Getter
@Setter
public class Attribute extends DBNodeComposite {


    private AttributeType attributeType;    //tip podatka(char, int...) to je enum
    private int length;     //duzina
    private Attribute inRelationWith;   //atribut u relaciji, koristimo da prikazemo strane kljucevee

    public Attribute(String name, DBNode parent) {
        super(name, parent);
    }

    public Attribute(String name, DBNode parent, AttributeType attributeType, int length) {
        super(name, parent);
        this.attributeType = attributeType;
        this.length = length;
    }

    @Override
    public void addChild(DBNode child) {            ///deca su  mu ogranicenja(Enum)
        if (child != null && child instanceof AttributeConstraint){
            AttributeConstraint attributeConstraint = (AttributeConstraint) child;
            this.getChildren().add(attributeConstraint);
        }
    }


}

