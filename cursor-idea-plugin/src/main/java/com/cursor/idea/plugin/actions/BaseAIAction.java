package com.cursor.idea.plugin.actions;

import com.cursor.idea.plugin.CursorAIService;
import com.cursor.idea.plugin.ui.AIResultDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * AI 动作基类
 * 提供通用的 AI 操作功能
 */
public abstract class BaseAIAction extends AnAction {
    
    private static final Logger LOG = Logger.getInstance(BaseAIAction.class);
    protected final CursorAIService aiService;
    
    public BaseAIAction() {
        this.aiService = CursorAIService.getInstance();
    }
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }
        
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            Messages.showErrorDialog(project, "请在编辑器中选择代码", "Cursor AI");
            return;
        }
        
        if (!aiService.isAvailable()) {
            Messages.showErrorDialog(project, 
                "Cursor AI 服务不可用。请检查 API 密钥配置。", 
                "Cursor AI");
            return;
        }
        
        String selectedText = getSelectedText(editor);
        String language = getLanguage(e);
        
        if (requiresSelection() && StringUtil.isEmpty(selectedText)) {
            Messages.showErrorDialog(project, "请先选择要处理的代码", "Cursor AI");
            return;
        }
        
        // 在后台线程中执行 AI 操作
        ProgressManager.getInstance().run(new Task.Backgroundable(project, getProgressTitle(), true) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.setIndeterminate(true);
                indicator.setText("正在调用 Cursor AI...");
                
                try {
                    CompletableFuture<String> future = performAIOperation(selectedText, language, project);
                    String result = future.get();
                    
                    // 在 EDT 线程中显示结果
                    ApplicationManager.getApplication().invokeLater(() -> {
                        if (!StringUtil.isEmpty(result)) {
                            showResult(project, result, selectedText, editor);
                        } else {
                            Messages.showErrorDialog(project, "AI 服务返回了空结果", "Cursor AI");
                        }
                    });
                    
                } catch (Exception ex) {
                    LOG.error("执行 AI 操作时发生异常", ex);
                    ApplicationManager.getApplication().invokeLater(() -> {
                        Messages.showErrorDialog(project, 
                            "执行 AI 操作时发生错误: " + ex.getMessage(), 
                            "Cursor AI");
                    });
                }
            }
        });
    }
    
    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        
        boolean enabled = project != null && editor != null && aiService.isAvailable();
        
        if (enabled && requiresSelection()) {
            String selectedText = getSelectedText(editor);
            enabled = !StringUtil.isEmpty(selectedText);
        }
        
        e.getPresentation().setEnabled(enabled);
    }
    
    /**
     * 获取选中的文本
     */
    protected String getSelectedText(@NotNull Editor editor) {
        SelectionModel selectionModel = editor.getSelectionModel();
        String selectedText = selectionModel.getSelectedText();
        
        if (StringUtil.isEmpty(selectedText)) {
            // 如果没有选中文本，尝试获取当前行
            int caretLine = editor.getCaretModel().getLogicalPosition().line;
            int lineStartOffset = editor.getDocument().getLineStartOffset(caretLine);
            int lineEndOffset = editor.getDocument().getLineEndOffset(caretLine);
            selectedText = editor.getDocument().getText().substring(lineStartOffset, lineEndOffset);
        }
        
        return selectedText;
    }
    
    /**
     * 获取文件语言
     */
    @Nullable
    protected String getLanguage(@NotNull AnActionEvent e) {
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (psiFile != null) {
            return psiFile.getLanguage().getDisplayName();
        }
        return null;
    }
    
    /**
     * 显示结果
     */
    protected void showResult(@NotNull Project project, @NotNull String result, 
                            @Nullable String originalCode, @NotNull Editor editor) {
        AIResultDialog dialog = new AIResultDialog(project, getActionName(), result, originalCode, editor);
        dialog.show();
    }
    
    /**
     * 执行具体的 AI 操作
     */
    protected abstract CompletableFuture<String> performAIOperation(@Nullable String selectedText, 
                                                                  @Nullable String language, 
                                                                  @NotNull Project project);
    
    /**
     * 是否需要选中文本
     */
    protected abstract boolean requiresSelection();
    
    /**
     * 获取进度条标题
     */
    protected abstract String getProgressTitle();
    
    /**
     * 获取动作名称
     */
    protected abstract String getActionName();
}