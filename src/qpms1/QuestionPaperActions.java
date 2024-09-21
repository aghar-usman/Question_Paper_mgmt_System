package qpms1;

// Interface for searching, filtering, and deleting question papers
interface QuestionPaperActions {
    void searchQuestionPapers(String query);
    void deleteQuestionPaper(int id);
}
