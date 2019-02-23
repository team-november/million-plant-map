package database;

public class Synonym {
  private String name;
  private IndexScheme scheme;
  private String familyName;
  private String familyNumber;
  private String genusNumber;
  private boolean isAccepted;
  private boolean isBasionym;
  private String note;

  public Synonym() {}

  public Synonym(String name,
                 IndexScheme scheme,
                 String familyName,
                 String familyNumber,
                 String genusNumber,
                 boolean isAccepted,
                 boolean isBasionym,
                 String note) {
    this.name = name;
    this.scheme = scheme;
    this.familyName = familyName;
    this.familyNumber = familyNumber;
    this.genusNumber = genusNumber;
    this.isAccepted = isAccepted;
    this.isBasionym = isBasionym;
    this.note = note;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public IndexScheme getScheme() {
    return scheme;
  }

  public void setScheme(IndexScheme scheme) {
    this.scheme = scheme;
  }

  public String getFamilyName() {
    return familyName;
  }

  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  public String getFamilyNumber() {
    return familyNumber;
  }

  public void setFamilyNumber(String familyNumber) {
    this.familyNumber = familyNumber;
  }

  public String getGenusNumber() {
    return genusNumber;
  }

  public void setGenusNumber(String genusNumber) {
    this.genusNumber = genusNumber;
  }

  public boolean isAccepted() {
    return isAccepted;
  }

  public void setAccepted(boolean isAccepted) {
    this.isAccepted = isAccepted;
  }

  public boolean isBasionym() {
    return isBasionym;
  }

  public void setBasionym(boolean isBasionym) {
    this.isBasionym = isBasionym;
  }
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

}