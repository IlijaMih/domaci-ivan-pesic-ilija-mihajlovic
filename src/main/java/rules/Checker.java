package rules;

import lombok.Getter;
import lombok.Setter;
import rules.implementation.ForeignKeyRule;
import rules.implementation.SequenceStatementsRule;
import rules.implementation.TablesColumnsRule;

import java.util.ArrayList;
import java.util.Stack;

@Getter
@Setter
public class Checker {

    private ArrayList<Rule> rules;
    private Stack<String> stack;

    public Checker() {
        rules = new ArrayList<>();
        stack = new Stack<>();
        initRules();
    }

    public Stack<String> check(String query){
        for (Rule rule:rules){
            String s = rule.check(query);
            if(s != null){
                this.stack.push(s);
            }
        }
        return this.stack;
    }

    private void initRules(){
        rules.add(new TablesColumnsRule());
        rules.add(new SequenceStatementsRule());
      //  rules.add(new ForeignKeyRule());
    }
}
