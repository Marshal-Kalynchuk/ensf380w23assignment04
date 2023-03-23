package edu.ucalgary.oop;

public class CommandArgumentNotProvidedException extends RuntimeException {
  public CommandArgumentNotProvidedException(String message) {
    super(message);
  }
}
