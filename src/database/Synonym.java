package database;

public class Synonym {
  private String name;
  private IndexScheme scheme;
  private String familyName;
  private String familyNumber;
  private String genusNumber;
  private boolean isAccepted;
  private boolean isBasionym;

  public Synonym(String name,
                 IndexScheme scheme,
                 String familyName,
                 String familyNumber,
                 String genusNumber,
                 boolean isAccepted,
                 boolean isBasionym) {
    this.name = name;
    this.scheme = scheme;
    this.familyName = familyName;
    this.familyNumber = familyNumber;
    this.genusNumber = genusNumber;
    this.isAccepted = isAccepted;
    this.isBasionym = isBasionym;
  }

  public String getName() {
    return name;
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

  public String getGenusNumber() {
    return genusNumber;
  }

  public boolean isAccepted() {
    return isAccepted;
  }

  public boolean isBasionym() {
    return isBasionym;
  }

}