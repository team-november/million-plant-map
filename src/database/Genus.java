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

  public static String getGenusTableForScheme(IndexScheme scheme) {
    if (scheme == IndexScheme.GB_AND_I) return "genera_gbi";
    else if (scheme == IndexScheme.BENTHAM_HOOKER) return "genera_bh";
    else if (scheme == IndexScheme.FLORA_EUROPAEA) return "genera_fe";
    else return null;
  }
}