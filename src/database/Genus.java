package database;

public class Genus {
  private IndexScheme scheme;
  private String genusName;
  private String familyNumber;
  private String genusNumber;

  public Genus(IndexScheme scheme, 
               String genusName,
               String familyNumber,
               String genusNumber) {
    this.scheme = scheme;
    this.genusName = genusName;
    this.familyNumber = familyNumber;
    this.genusNumber = genusNumber;
  }

  public IndexScheme getScheme() {
    return scheme;
  }

  public String getGenusName() {
    return genusName;
  }

  public String getFamilyNumber() {
    return familyNumber;
  }

  public String getGenusNumber() {
    return genusNumber;
  }
}