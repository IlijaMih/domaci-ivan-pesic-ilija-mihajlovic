package utils;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Table {

    private String name;
    private ArrayList<String> columns;
    private ArrayList<String> primaryKeys;
    private ArrayList<String> foreignKeys;

    public Table(String name) {
        this.name = name;
        columns = new ArrayList<>();
        primaryKeys = new ArrayList<>();
        foreignKeys = new ArrayList<>();
    }


}
