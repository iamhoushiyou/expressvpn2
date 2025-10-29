package com.cursor.idea.plugin.actions;

import com.intellij.codeInsight.daemon.impl.DaemonCodeAnalyzerImpl;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 修复代码动作
 */
public class FixCodeAction extends BaseAIAction {
    
    @Override
    protected CompletableFuture<String> performAIOperation(@Nullable String selectedText, 
                                                         @Nullable String language, 
                                                         @NotNull Project project) {
        // 尝试获取当前的错误信息
        String errorMessage = getCurrentErrors(project);
        
        if (StringUtil.isEmpty(errorMessage)) {
            // 如果没有自动检测到错误，让用户手动输入
            errorMessage = Messages.showInputDialog(
                project,
                "请描述遇到的错误或问题：",
                "修复代码",
                Messages.getQuestionIcon()
            );
            
            if (errorMessage == null || errorMessage.trim().isEmpty()) {
                return CompletableFuture.completedFuture("用户取消了操作");
            }
        }
        
        return aiService.fixCode(selectedText != null ? selectedText : "", errorMessage, language);
    }
    
    /**
     * 获取当前编辑器中的错误信息
     */
    private String getCurrentErrors(@NotNull Project project) {
        try {
            // 这里可以集成 IDEA 的错误检测系统
            // 简化实现，实际项目中可以获取更详细的错误信息
            return "检测到代码可能存在问题，请使用 AI 进行分析和修复";
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    protected boolean requiresSelection() {
        return true;
    }
    
    @Override
    protected String getProgressTitle() {
        return "正在修复代码...";
    }
    
    @Override
    protected String getActionName() {
        return "代码修复";
    }
}