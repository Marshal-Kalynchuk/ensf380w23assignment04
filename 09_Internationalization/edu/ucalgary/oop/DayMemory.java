package edu.ucalgary.oop;

  /* main()
   * Accept a command-line argument which specifies a translation file.
   * The argument should be in the form of a two-letter language code,
   * followed by a dash and a two-letter region code, e.g., en-US
   * which corresponds with files en-US.txt and en-US.ser
   * If no argument is specified, it throws a custom exception,
   * CommandArgumentNotProvidedException, which extends Exception. 
   * Additional arguments are ignored.
  */
  public class DayMemory {
    
    public static void main(String[] args) throws CommandArgumentNotProvidedException, IllegalArgumentException {
        
        //if (args.length == 0) {
        //    throw new CommandArgumentNotProvidedException("No translation file specified");
        //}
        // String code = args[0];
        // Translator trans = new Translator(code);
        
        String[] goodData = getTranslationFiles();
        String[] badData = getBadTranslationFiles();
        
        System.out.println("\nTesting good data: ");
        for (String point: goodData) {
          System.out.println("\nTesting: " + point);
          Translator t = new Translator(point);
          System.out.println(t.translate(1, 1, 2003));
          System.out.println(t.translate(12, 31, -1000));
        }

        System.out.println("\nTesting serialization: ");
        Translator n = new Translator(goodData[0]);
        System.out.println(n.translate(1, 1, 2003));
        System.out.println(n.translate(12, 31, -1000));
        try {
          n.serialize();
        } catch (Exception e) {

        }

           
        Translator s = new Translator(goodData[0]);
        System.out.println(s.translate(1, 1, 2003));
        System.out.println(s.translate(12, 31, -1000));

        System.out.println("\nTesting bad data: ");
        for (String point: badData) {
          System.out.println("\nTesting: " + point);
          try {
            Translator t = new Translator(point);
            System.out.println(t.translate(1, 1, 2003));
            System.out.println(t.translate(12, 31, -1000));
          } catch (Exception e) {
            System.out.println(e);
          }
        }
    }
    public static String[] getTranslationFiles() { 
      String[] data = {
        "el-GR",
        "en-US",
        "es-BO",
      };
      return data;
    }
    public static String[] getBadTranslationFiles() { 
      String[] data = {
        "",       // Empty code
        "en-Us",  // Invalid region
        "Es-BO",  // Invalid language
        "ess-BO", // Invalid language
        "en-USS", // Invalid region
        "es US"   // Invalid code (Missing -)
      };
      return data;
    }
}