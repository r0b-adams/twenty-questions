// QuestionTree stores information and provides functionality to play a game of 20 questions
// against the computer, storing a series of yes/no questions and objects that the computer
// can guess and a user interface, as well as the number of games played during one run of the
// program and the number of games the computer has won. The user can load a series of questions
// and answers from a file, or start immediately. If starting immediately, only one object to guess
// is stored initailly. If the computer loses a game, it prompts user for the object they were
// thinking of, a yes/no question to set it apart from the incorrectly guessed object, and a yes/no
// response to the new question. The new question/answer are then saved for the current run of the
// program. The currently loaded series of questions/answers can be saved to a text file for future
// games to use.

import java.io.*;
import java.util.*;

public class QuestionTree {
   private QuestionNode overallRoot;  // reference to the first question or answer
   private UserInterface ui;          // user interface for input/output
   private int gamesPlayed;           // total games played in current run of program
   private int gamesWon;              // number of games won by computer

   // Param: ui: user interface to handle input/output
   //   Pre: ui != null, else throws IllegalArgumentException
   //  Post: Constructs a new QuestionTree; initially contains just one
   //        object to guess, "computer".
   public QuestionTree(UserInterface ui) {
      if (ui == null) {
         throw new IllegalArgumentException();
      }
      this.ui = ui;
      this.overallRoot = new QuestionNode("computer");
      this.gamesPlayed = 0;
      this.gamesWon = 0;
   }

   //  Post: Plays one game of 20 questions. Computer asks yes/no questions until it arrives
   //        at a possible answer. It then guesses this answer. If the guess was correct, the
   //        computer wins and number of games won is incremented by one. Else the guess was
   //        wrong and the user wins. The computer then prompts for what the user was thinking of,
   //        along with a yes/no question to distinguish from guessed object and the yes/no
   //        response to given question. The new question, answer, and response are saved for the
   //        remainder of the program run. Increments number of games played by one.
   public void play() {
      this.overallRoot = play(this.overallRoot);
      gamesPlayed++;
   }

   // Param: root: either a yes/no question asked or object guessed by computer
   //  Post: Plays one game of 20 questions.
   private QuestionNode play(QuestionNode root) {
      if (root.left == null && root.right == null) {       // arrived at potential answer
         ui.print("Would your object happen to be " + root.data + "?"); // guesses answer
         if (ui.nextBoolean()) { // if the guess was correct, computer wins
            ui.println("I win!");
            gamesWon++;
         } else {  // else player wins
            root = add(root);  // prompt for answer and get yes/no question for it
         }
      } else { // else at a question
         ui.print(root.data);  // ask the question
         if (ui.nextBoolean()) {
            root.left = play(root.left);   // user answered "yes"
         } else {
            root.right = play(root.right); // user answered "no"
         }
      }
      return root;
   }

   // @param: root: the incorrectly guessed object by the computer
   //  Post: Returns a new yes/no question and answer given by user to be implemented in the
   //        current set of questions and answers. If the answer to the user's given question
   //        is "yes", the new answer appears as the "yes" reponse and the incorrectly guessed
   //        object becomes the "no" response. Else, the new answer appears as the "no" response
   //        and the incorrectly guessed object appears as the "yes" response
   private QuestionNode add(QuestionNode root) {
      ui.print("I lose. What is your object?");
      String answer = ui.nextLine();   // get the item the user was thinking of
      ui.print("Type a yes/no question to distinguish your item from " + root.data + ":");
      String question = ui.nextLine(); // get a yes/no question from user for the object
      ui.print("And what is the answer for your object?");

      if (ui.nextBoolean()) { // is answer to new question "yes" or "no"?
         return new QuestionNode(question, new QuestionNode(answer), root);
      } else {
         return new QuestionNode(question, root, new QuestionNode(answer));
      }
   }

   // Param: output: PrintStream that writes questions and answers to a file
   //   Pre: output != null, else throws IllegalArgumentException
   //  Post: Saves all questions and answers in preordered format
   public void save(PrintStream output) {
      if (output == null) {
         throw new IllegalArgumentException();
      }
      save(this.overallRoot, output);
   }

   // Param: root: Either a yes/no question or an answer
   //      output: PrintStream that writes questions and answers to a file
   //   Pre: output != null, else throws IllegalArgumentException
   //  Post: Saves all questions and answers in preordered format
   private void save(QuestionNode root, PrintStream output) {
      if (root != null) {
         if (root.left == null && root.right == null) {
            output.println("A:" + root.data); // designated as an answer
         } else {
            output.println("Q:" + root.data); // designated as a question
         }
         save(root.left, output);  // save "yes" responses
         save(root.right, output); // save "no" responses
      }
   }

   // Param: input: Scanner that reads an input file of questions and answers
   //   Pre: input != null, else throws IllegalArgumentException
   //  Post: Loads a new series of yes/no answers and questions to play the game with
   public void load(Scanner input) {
      if (input == null) {
         throw new IllegalArgumentException();
      }
      this.overallRoot = buildTree(input);
   }

   // Param: root: Current question or answer to load
   //       input: Scanner that reads an input file of questions and answers
   //   Pre: input != null, else throws IllegalArgumentException
   //  Post: Loads a new series of yes/no questions and answers from text file.
   private QuestionNode buildTree(Scanner input) {
      String current = input.nextLine();
      String data = current.substring(2);
      QuestionNode root = new QuestionNode(data);

      // if a line is labelled as a question, it has both "yes" and "no" responses
      if (current.startsWith("Q")) {
         root.left = buildTree(input);   // add "yes" responses
         root.right = buildTree(input); // add "no" responses
      }
      return root;
   }

   //  Post: Returns the total number of games played
   public int totalGames() {
      return this.gamesPlayed;
   }

   //  Post: Returns the number of games won by the computer during current run of program
   public int gamesWon() {
      return this.gamesWon;
   }
}