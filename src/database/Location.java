package database;

public class Location {
  private String code;
  private String definition;

  public Location(String code, String definition) {
    this.code = code;
    this.definition = definition;
  }

  public String getCode() {
    return code;
  }

  public String getDefinition() {
    return definition;
  }
}