package resource.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Data
public class Row {      ////PREDSTAVLJA JEDAN RED U PROIZVOLJNOJ TABELI

    private String name;        //IME-TJ MATICNA TABELA
    private Map<String, Object> fields;       //POLJA

    /////ZA JEDNO POLJE- IME MU JE NAZIV TABELE, KLJUC JE JEDNA KOLONA U TABELI, VREDNOST JE PODATAK U JEDNOJ KOLONI

    public Row() {
        this.fields = new HashMap<>();
    }

    public void addField(String fieldName, Object value) {
        this.fields.put(fieldName, value);
    }

    public void removeField(String fieldName) {
        this.fields.remove(fieldName);
    }


    public String printMap() {
        Iterator it = this.fields.entrySet().iterator();
        String line = new String("");
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if (it.hasNext()){
                line += " " + pair.getValue().toString()+",";
            }else {
                line += " " + pair.getValue().toString();
            }
            it.remove(); // avoids a ConcurrentModificationException
        }

        return line;
    }
}
