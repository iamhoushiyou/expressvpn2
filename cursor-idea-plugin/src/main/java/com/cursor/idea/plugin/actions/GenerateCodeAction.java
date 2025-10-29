package com.cursor.idea.plugin.actions;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * 生成代码动作
 */
public class GenerateCodeAction extends BaseAIAction {
    
    @Override
    protected CompletableFuture<String> performAIOperation(@Nullable String selectedText, 
                                                         @Nullable String language, 
                                                         @NotNull Project project) {
        // 获取用户输入的需求描述
        String description = Messages.showInputDialog(
            project,
            "请描述您想要生成的代码功能：",
            "生成代码",
            Messages.getQuestionIcon()
        );
        
        if (description == null || description.trim().isEmpty()) {
            return CompletableFuture.completedFuture("用户取消了操作");
        }
        
        return aiService.generateCode(description, language);
    }
    
    @Override
    protected boolean requiresSelection() {
        return false;
    }
    
    @Override
    protected String getProgressTitle() {
        return "正在生成代码...";
    }
    
    @Override
    protected String getActionName() {
        return "代码生成";
    }
}