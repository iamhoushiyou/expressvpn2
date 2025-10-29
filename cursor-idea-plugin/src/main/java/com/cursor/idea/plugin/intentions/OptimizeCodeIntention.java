package com.cursor.idea.plugin.intentions;

import com.cursor.idea.plugin.CursorAIService;
import com.cursor.idea.plugin.ui.AIResultDialog;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * 优化代码意图动作
 */
public class OptimizeCodeIntention extends PsiElementBaseIntentionAction implements IntentionAction {
    
    private final CursorAIService aiService = CursorAIService.getInstance();
    
    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) 
            throws IncorrectOperationException {
        
        if (!aiService.isAvailable()) {
            return;
        }
        
        String selectedText = editor.getSelectionModel().getSelectedText();
        if (selectedText == null || selectedText.trim().isEmpty()) {
            selectedText = element.getText();
        }
        
        String language = element.getLanguage().getDisplayName();
        
        aiService.optimizeCode(selectedText, language).whenComplete((result, throwable) -> {
            ApplicationManager.getApplication().invokeLater(() -> {
                if (throwable == null && result != null) {
                    AIResultDialog dialog = new AIResultDialog(project, "代码优化", result, selectedText, editor);
                    dialog.show();
                }
            });
        });
    }
    
    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        return aiService.isAvailable() && element.getText().trim().length() > 0;
    }
    
    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getFamilyName() {
        return "Cursor AI";
    }
    
    @NotNull
    @Override
    public String getText() {
        return "使用 Cursor AI 优化代码";
    }
}