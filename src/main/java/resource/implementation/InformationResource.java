package resource.implementation;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import resource.DBNode;
import resource.DBNodeComposite;

                    ///////predstavlja bazu podataka na koju se kacimo
@Getter
@Setter
@ToString(callSuper = true)
public class InformationResource extends DBNodeComposite {


    public InformationResource(String name) {
        super(name, null);
    }       ////ima ime i parent = null jer predstavlja root

    @Override
    public void addChild(DBNode child) {        //dopusta dodavanje deteta samo ako je dete Entity
        if (child != null && child instanceof Entity){
            Entity entity = (Entity) child;
            this.getChildren().add(entity);
        }
    }
}
