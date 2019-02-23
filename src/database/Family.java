package database;

public class Family {
  private IndexScheme scheme;
  private String familyName;
  private String familyNumber;

  public Family(IndexScheme scheme,
                String familyName,
                String familyNumber) {
    this.scheme = scheme;
    this.familyName = familyName;
    this.familyNumber = familyNumber;
  }

  public IndexScheme getScheme() {
    return scheme;
  }

  public String getFamilyName() {
    return familyName;
  }

  public String getFamilyNumber() {
    return familyNumber;
  }
}