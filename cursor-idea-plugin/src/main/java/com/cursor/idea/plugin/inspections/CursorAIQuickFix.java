package com.cursor.idea.plugin.inspections;

import com.cursor.idea.plugin.CursorAIService;
import com.cursor.idea.plugin.ui.AIResultDialog;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * Cursor AI 快速修复
 */
public class CursorAIQuickFix implements LocalQuickFix {
    
    private final String actionName;
    private final CursorAIService aiService = CursorAIService.getInstance();
    
    public CursorAIQuickFix(String actionName) {
        this.actionName = actionName;
    }
    
    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getFamilyName() {
        return "Cursor AI";
    }
    
    @NotNull
    @Override
    public String getName() {
        return "使用 Cursor AI " + actionName;
    }
    
    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        if (!aiService.isAvailable()) {
            return;
        }
        
        PsiElement element = descriptor.getPsiElement();
        if (element == null) {
            return;
        }
        
        String code = element.getText();
        String language = element.getLanguage().getDisplayName();
        
        // 根据动作类型调用不同的 AI 服务
        if (actionName.contains("优化")) {
            aiService.optimizeCode(code, language).whenComplete((result, throwable) -> {
                showResult(project, result, code, "代码优化建议");
            });
        } else if (actionName.contains("分析")) {
            aiService.explainCode(code, language).whenComplete((result, throwable) -> {
                showResult(project, result, code, "代码分析");
            });
        } else {
            // 默认使用聊天功能
            String prompt = String.format("请分析以下 %s 代码并提供改进建议：\n\n%s", language, code);
            aiService.chat(prompt).whenComplete((result, throwable) -> {
                showResult(project, result, code, actionName);
            });
        }
    }
    
    /**
     * 显示 AI 结果
     */
    private void showResult(Project project, String result, String originalCode, String title) {
        if (result == null) {
            return;
        }
        
        ApplicationManager.getApplication().invokeLater(() -> {
            Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
            if (editor != null) {
                AIResultDialog dialog = new AIResultDialog(project, title, result, originalCode, editor);
                dialog.show();
            }
        });
    }
}