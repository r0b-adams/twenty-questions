// QuestionNode stores a yes/no question and references to its answers, or
// a single answer (an object to be guessed).

public class QuestionNode {
   public String data;        // stores the current question or answer
   public QuestionNode left;  // points to a "yes" response
   public QuestionNode right; // points to a "no" response

   // Constructs a new QuestionNode with given answer
   public QuestionNode(String answer) {
      this(answer, null, null);
   }

   // Constructs a new QuestionNode with given question, "yes" answer, and "no" answer
   public QuestionNode(String question, QuestionNode yes, QuestionNode no) {
      this.data = question;
      this.left = yes;
      this.right = no;
   }
}