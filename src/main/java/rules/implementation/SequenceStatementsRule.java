package rules.implementation;

import rules.Rule;

import java.util.HashMap;

public class SequenceStatementsRule implements Rule {


    private HashMap<String, Integer> statements;


    public SequenceStatementsRule() {
        this.statements = new HashMap<>();
        statements.put("select", 1);
        statements.put("from", 2);
        statements.put("where", 3);

    }

    @Override
    public String check(String str) {
        String query = str.toLowerCase().replaceAll("\n", " ");
        String split[] = query.split(" ");
        int curr = getValue(split[0]);
        int next;

        if(curr != 1){
            return getFixMessage();
        }

        for (int x = 1;x<split.length;x++){
            next = getValue(split[x]);
            if(next == 0){
                continue;
            }
            if(next <= curr){
                return getFixMessage();
            }
            System.out.println(curr + " u for petlji");
            curr = next;
        }

        return null;
    }

    @Override
    public String getFixMessage() {
        String message = "Redosled reci u upitu nije dobar!";
        return message;
    }

    private Integer getValue(String word){
        if (statements.containsKey(word)){
            return statements.get(word);
        }
        return 0;
    }
}
