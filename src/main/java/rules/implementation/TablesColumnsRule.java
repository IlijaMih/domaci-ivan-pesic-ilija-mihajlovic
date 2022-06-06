package rules.implementation;

import rules.Rule;
import utils.Config;
import utils.Table;

import java.util.ArrayList;
import java.util.HashMap;

public class TablesColumnsRule implements Rule {
    @Override
    public String check(String str) {

        HashMap<String, Boolean> columnsMap = new HashMap<>();
        String query = str.toLowerCase().replaceAll("\n", " ");
        ArrayList<String> tables = getTables(query);
        ArrayList<String> columns = getColumns(query);
        for (String s: tables){
            if (!checkTables(s, columns, columnsMap)){
                System.out.println("Tabela: " +s+ " NEPOSTOJI");
                return getFixMessage();
            }
        }
        if(checkColumnsMap(columns,columnsMap)) {
            return null;
        }
        return getFixMessage();
    }

    @Override
    public String getFixMessage() {
        String message = "Some table or column does not exist in database!";
        return message;
    }

    private ArrayList<String> getColumns(String query){
        ArrayList<String> columns = new ArrayList<>();
        String select[] = query.split("select ");
        String from[] = select[1].split("from");
        String space[] = from[0].split(" ");

        for (int i = 0; i < space.length; i++) {
            String col = space[i].replaceAll(",", "");
            if (!col.equals(" ") && !col.equals(" ")){
                columns.add(col);
        }
        }
        return columns;
    }

    private ArrayList<String> getTables(String query){
        ArrayList<String> tables = new ArrayList<>();

        String from[] = query.split("from ");
        String space[] = from[1].split(" ");

        String table1 =space[0].replaceAll("hr.", "");
        tables.add(table1);
        
        String join[] = from[1].split("join ");
        for (int i = 0; i < join.length; i++) {
            String spaceJoin[] = join[i].split(" ");
            String table = spaceJoin[0].replaceAll("hr.", "");
            tables.add(table);
        }

      
        return tables;
    }


    private boolean checkTables(String name, ArrayList<String> columns, HashMap<String, Boolean> columnsMap){
            for(Table table:Config.tables){
                if(table.getName().equals(name)){
                    checkColumns(columns, table, columnsMap);
                    return true;
                }
            }
        return false;
    }

    private void checkColumns(ArrayList<String> columns, Table table, HashMap<String, Boolean> columnsMap){
        for (String s:columns){
            if (table.getColumns().contains(s) || s.equals("*")){
                columnsMap.put(s, true);
            }
        }
    }

    public boolean checkColumnsMap(ArrayList<String> columns, HashMap<String, Boolean> columnsMap){
        for (String s: columns){
            if(columnsMap.containsKey(s) && columnsMap.get(s).equals(true)){
                continue;
            }else {
                return false;
            }
        }
        return true;
    }
}
