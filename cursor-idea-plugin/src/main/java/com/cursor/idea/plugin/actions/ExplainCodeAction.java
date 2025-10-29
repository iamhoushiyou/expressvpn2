package com.cursor.idea.plugin.actions;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * 解释代码动作
 */
public class ExplainCodeAction extends BaseAIAction {
    
    @Override
    protected CompletableFuture<String> performAIOperation(@Nullable String selectedText, 
                                                         @Nullable String language, 
                                                         @NotNull Project project) {
        return aiService.explainCode(selectedText != null ? selectedText : "", language);
    }
    
    @Override
    protected boolean requiresSelection() {
        return true;
    }
    
    @Override
    protected String getProgressTitle() {
        return "正在解释代码...";
    }
    
    @Override
    protected String getActionName() {
        return "代码解释";
    }
}