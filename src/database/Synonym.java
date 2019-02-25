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
                 String familyName,
                 IndexScheme scheme,
                 String familyNumber,
                 String genusNumber,
                 boolean isAccepted,
                 boolean isBasionym,
                 String note) {
    this.name = name;
    this.familyName = familyName;
    this.scheme = scheme;
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

  /**
   * Returns true if two synonyms are equal (case-insensitive comparison).
   *
   * @param other synonym to be compared
   * @return whether the two synonyms are equal
   */
  public boolean equals(Synonym other) {
    return this.getName().equalsIgnoreCase(other.getName())
            && this.getFamilyName().equalsIgnoreCase(other.getFamilyName())
            && this.getScheme().equals(other.getScheme())
            && this.getFamilyNumber().equalsIgnoreCase(other.getFamilyNumber())
            && this.getGenusNumber().equalsIgnoreCase(other.getGenusNumber())
            && (this.isAccepted() == other.isAccepted())
            && (this.isBasionym() == other.isBasionym())
            && (this.getNote().equalsIgnoreCase(other.getNote()));
  }

  /**
   * Checks the validity of a {@link Synonym} for insertion as a record.
   * In particular, this checks if all non-null fields as defined in
   * `herbarium_index` are present.
   *
   * @return true if non-null fields are all defined
   */
  public boolean isValid() {
    return this.name != null
            && this.familyName != null
            && this.familyNumber != null;
  }
}