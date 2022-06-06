package rules;

public interface Rule {

    public String check(String query);

    public String getFixMessage();
}
