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

  public boolean equals(Family other) {
    return this.getScheme().equals(other.getScheme())
            && this.getFamilyName().equalsIgnoreCase(other.getFamilyName())
            && this.getFamilyNumber().equalsIgnoreCase(other.getFamilyNumber());
  }

  public static String getFamilyTableForScheme(IndexScheme scheme) {
    if (scheme == IndexScheme.GB_AND_I) return "families_gbi";
    else if (scheme == IndexScheme.BENTHAM_HOOKER) return "families_bh";
    else if (scheme == IndexScheme.FLORA_EUROPAEA) return "families_fe";
    else return null;
  }
}