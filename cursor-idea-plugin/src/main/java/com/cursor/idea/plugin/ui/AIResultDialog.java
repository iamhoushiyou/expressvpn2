package com.cursor.idea.plugin.ui;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * AI 结果显示对话框
 */
public class AIResultDialog extends DialogWrapper {
    
    private final Project project;
    private final String actionName;
    private final String result;
    private final String originalCode;
    private final Editor editor;
    
    private JTextArea resultArea;
    private JButton applyButton;
    private JButton copyButton;
    
    public AIResultDialog(@NotNull Project project, 
                         @NotNull String actionName,
                         @NotNull String result, 
                         @Nullable String originalCode,
                         @NotNull Editor editor) {
        super(project);
        this.project = project;
        this.actionName = actionName;
        this.result = result;
        this.originalCode = originalCode;
        this.editor = editor;
        
        setTitle("Cursor AI - " + actionName);
        setModal(false);
        setResizable(true);
        
        init();
    }
    
    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(600, 400));
        
        // 创建结果显示区域
        resultArea = new JTextArea(result);
        resultArea.setEditable(false);
        resultArea.setWrapStyleWord(true);
        resultArea.setLineWrap(true);
        resultArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JBScrollPane scrollPane = new JBScrollPane(resultArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // 创建按钮面板
        JPanel buttonPanel = createButtonPanel();
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        // 复制按钮
        copyButton = new JButton("复制结果");
        copyButton.addActionListener(e -> copyToClipboard());
        panel.add(copyButton);
        
        // 应用按钮（仅在有原始代码时显示）
        if (!StringUtil.isEmpty(originalCode)) {
            applyButton = new JButton("应用到编辑器");
            applyButton.addActionListener(this::applyToEditor);
            panel.add(applyButton);
        }
        
        return panel;
    }
    
    /**
     * 复制结果到剪贴板
     */
    private void copyToClipboard() {
        ApplicationManager.getApplication().invokeLater(() -> {
            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(new java.awt.datatransfer.StringSelection(result), null);
        });
    }
    
    /**
     * 应用结果到编辑器
     */
    private void applyToEditor(ActionEvent e) {
        if (editor == null || StringUtil.isEmpty(originalCode)) {
            return;
        }
        
        ApplicationManager.getApplication().invokeLater(() -> {
            WriteCommandAction.runWriteCommandAction(project, () -> {
                Document document = editor.getDocument();
                SelectionModel selectionModel = editor.getSelectionModel();
                
                if (selectionModel.hasSelection()) {
                    // 替换选中的文本
                    int start = selectionModel.getSelectionStart();
                    int end = selectionModel.getSelectionEnd();
                    document.replaceString(start, end, extractCodeFromResult(result));
                } else {
                    // 在光标位置插入
                    int offset = editor.getCaretModel().getOffset();
                    document.insertString(offset, extractCodeFromResult(result));
                }
            });
        });
        
        close(OK_EXIT_CODE);
    }
    
    /**
     * 从 AI 结果中提取代码部分
     */
    private String extractCodeFromResult(String result) {
        // 尝试提取代码块
        String[] lines = result.split("\n");
        StringBuilder codeBuilder = new StringBuilder();
        boolean inCodeBlock = false;
        
        for (String line : lines) {
            if (line.trim().startsWith("```")) {
                inCodeBlock = !inCodeBlock;
                continue;
            }
            
            if (inCodeBlock) {
                codeBuilder.append(line).append("\n");
            }
        }
        
        String extractedCode = codeBuilder.toString().trim();
        return StringUtil.isEmpty(extractedCode) ? result : extractedCode;
    }
    
    @Override
    protected Action @NotNull [] createActions() {
        return new Action[]{getCancelAction()};
    }
}