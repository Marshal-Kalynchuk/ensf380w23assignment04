package edu.ucalgary.oop;

import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Translator {

  private static String CODE_REGEX = "(([a-z]{2})-([A-Z]{2}))";
  private static Pattern CODE_PATTERN = Pattern.compile(CODE_REGEX);
  private final static int currentYear = 2023;


  private final String languageCode;
  private final String regionCode;

  private TranslationText translationText;

    /* Constructor
   * Accepts a String of a two-letter language code (lowercase), dash, and two-letter 
   * region (caps) code, e.g., te-IN and throws an IllegalArgumentException if the language 
   * and region code are not in the correct format. Language codes are ISO 639-1 and
   * region codes are ISO 3166, but this method only checks the format of the String, 
   * not if the region and language codes are valid according to the ISO specifications.
   * The input parameter must exactly match the expected format.
   * It calls importTranslation().
  */
  public Translator(String code) throws IllegalArgumentException {
    Matcher m = CODE_PATTERN.matcher(code);
    if (m.matches()) {
      this.languageCode = m.group(2);
      this.regionCode = m.group(3);
    } else {
      throw new IllegalArgumentException("Invalid code: " + code);
    }
    try {
      this.importTranslation();
    } catch (ArgFileNotFoundException e) {
      throw new IllegalArgumentException("Invalid code: " + code + "\n" + e);
    } 
  }

  /* getTranslation()
   * getter method returning a stored TranslationText object.
  */
  public TranslationText getTranslation() { return this.translationText; }

  /* translate()
   * Accepts a month number (e.g., 1 for January), a day number (e.g., 31 for
   * the 31st), and a year. Note that years may be any previous year in the common era 
   * (CE) from 0 to the previous year, or they may be before the common era (BCE),
   * represented by negative numbers. Thus 2021, 800, and -1600 are all valid years.
   * Method throws an IllegalArgumentException if monthNum or dayNum is not
   * valid. Returns the formatted sentence as a String. Notice that the String
   * containing formatting uses numbered arguments - this is because some languages
   * will put the words in the sentence in a different order, but the translate()
   * method must be able to work without knowledge of the language structure.
   * Note: You do not have to check if a day is valid for a particular month/year;
   * February 31 or February 29, 2021 can both be accepted, but out of range values
   * e.g., month 15 day 0, are not valid and should be handled with an 
   * IllegalArgumentException. 
  */
  public String translate(int month, int day, int year) throws IllegalArgumentException {
    if (month < 0 || month > 12 || day < 0 || day > 31 || year > currentYear) {
      throw new IllegalArgumentException();
    }
    return String.format(
      this.translationText.getSentence(),
      this.translationText.getDay(day - 1),
      this.translationText.getMonth(month - 1),
      year
    );
  }

  /* importTranslation()
   * Calls deserialize() if the appropriate file exists, otherwise calls importFromText().
   * No arguments. Returns void.
  */
  public void importTranslation() throws ArgFileNotFoundException {

    // Import from .ser
    try {
      deserialize();
      return;
    } catch (ArgFileNotFoundException e) {
    } catch (IOException e) {}

    // Import from .txt
    try {
      importFromText();
      return;
    } catch (ArgFileNotFoundException e) {
      throw e;
    } catch (IOException e) {}
  }

  /* importFromText()
   * Reads in from a the two-letter language code, dash, two-letter region code text 
   * file, in the form of ab-XY.txt, and instantiates a TranslationText object with
   * the data. It can throw I/O exceptions. Throw a custom ArgFileNotFoundException
   * when the file isn't found. 
   * We expect the .txt file to be in a valid format. The file is expected to be in the same 
   * directory. The files en-US.txt and el-GR.txt are examples of a valid .txt files. They will 
   * always consist of the month names, one per line, followed by the day names, one per line, 
   * followed by the sentence containing formatting strings. This is the last line in the file. You
   * cannot make any assumptions about what will appear on each line, only that each line
   * will contain only one data element, and that it will not contain an empty line.
   * No arguments. Returns void.
  */
  public void importFromText() throws ArgFileNotFoundException, IOException {
    String filename = "./edu/ucalgary/oop/" + this.languageCode + "-" + this.regionCode + ".txt";
    try (
      FileInputStream fileInput = new FileInputStream(filename);
      Scanner scanner = new Scanner(fileInput);
    ) {
        // Read in the month names
        String[] monthNames = new String[12];
        for (int i = 0; i < 12; i++) {
            String line = scanner.nextLine();
            monthNames[i] = line;
        }

        // Read in the day names
        String[] dayNames = new String[31];
        for (int i = 0; i < 31; i++) {
          String line = scanner.nextLine();
          dayNames[i] = line;
        }

        // Read in the formatting string
        String formatString = scanner.nextLine();

        // Close the input stream
        fileInput.close();

        // Create a new TranslationText object with the read-in data
        this.translationText = new TranslationText(monthNames, dayNames, formatString);

    } catch (FileNotFoundException e) {
      throw new ArgFileNotFoundException(filename + " does not exist");
    }  catch (IOException e) {
      throw new IOException("Error reading file: " + filename, e);
    }
  }

  /* serialize()
  * Creates a serialized object file of the TranslationText object, with the
  * name format la-CO.ser, where la is the two-letter language code and CO is
  * the two-letter region code. An example of a serialized object file can be
  * found in the exercise directory as es-BO.ser
  * I/O exceptions can be thrown.
  * No arguments. Returns void.
  */
  public void serialize() throws IOException {
    String filename = "./edu/ucalgary/oop/" + this.languageCode + "-" + this.regionCode + ".ser";
    try (
      FileOutputStream fileOutput = new FileOutputStream(filename);
      ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
    ) {
        objectOutput.writeObject(this.translationText);
        objectOutput.close();
        fileOutput.close();
    } catch (IOException e) {
      throw new IOException("Error reading file: " + filename, e);
    }
  }

  /* deserialize()
   * Creates a TranslationText object from a .ser file. The files are named
   * xx-YY.ser, where xx is the two-letter language code and YY is the two-
   * letter region code. es-BO.ser is an example. It can throw I/O exceptions.
   * No arguments. Returns void.
  */
  public void deserialize() throws ArgFileNotFoundException, IOException {
    String filename = "./edu/ucalgary/oop/" + this.languageCode + "-" + this.regionCode + ".ser";
    try (
      FileInputStream fileInput = new FileInputStream(filename);
      ObjectInputStream objectInput = new ObjectInputStream(fileInput);
    ) {
        this.translationText = (TranslationText) objectInput.readObject();
        objectInput.close();
        fileInput.close();
    } catch (FileNotFoundException e) {
      throw new ArgFileNotFoundException(filename + " does not exist");
    } catch (IOException | ClassNotFoundException e) {
      throw new IOException("Error reading file: " + filename, e);
    }
  }
}
