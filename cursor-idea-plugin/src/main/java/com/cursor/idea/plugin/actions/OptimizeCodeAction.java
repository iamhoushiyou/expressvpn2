package com.cursor.idea.plugin.actions;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * 优化代码动作
 */
public class OptimizeCodeAction extends BaseAIAction {
    
    @Override
    protected CompletableFuture<String> performAIOperation(@Nullable String selectedText, 
                                                         @Nullable String language, 
                                                         @NotNull Project project) {
        return aiService.optimizeCode(selectedText != null ? selectedText : "", language);
    }
    
    @Override
    protected boolean requiresSelection() {
        return true;
    }
    
    @Override
    protected String getProgressTitle() {
        return "正在优化代码...";
    }
    
    @Override
    protected String getActionName() {
        return "代码优化";
    }
}