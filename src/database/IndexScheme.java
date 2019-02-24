package database;

/**
 * The indexing schemes supported by the `herbarium_index` database.
 * 
 * Note that {@link IndexScheme#OTHER} is reserved for the `synonyms` table 
 * which allows the user to store the custom family/genus numbers that are in
 * the herbarium but do not correspond to any other indexing scheme.
 */
public enum IndexScheme{
  GB_AND_I,
  FLORA_EUROPAEA,
  BENTHAM_HOOKER,
  OTHER;
}