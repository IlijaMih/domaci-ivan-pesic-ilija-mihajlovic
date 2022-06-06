package database;

import resource.DBNode;
import resource.data.Row;

import java.util.List;

public interface Database{

    DBNode loadResource();  //KONEKTUJ SE NA BAZU I UCITAJ PODATKE O BAZI PODATAKA(METAPODATKE DA ZNAMO KAKVA JE BAZA)
    ///VRACA KORENSKI CVOR
    List<Row> readDataFromTable(String query, String tableName);  //VRACA LISTU REDOVA KOJI SE PRIKAZUJU U TABELI


    void importData(String file);
}
