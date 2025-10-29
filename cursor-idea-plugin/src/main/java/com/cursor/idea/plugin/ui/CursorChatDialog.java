package com.cursor.idea.plugin.ui;

import com.cursor.idea.plugin.CursorAIService;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.CompletableFuture;

/**
 * Cursor AI 聊天对话框
 */
public class CursorChatDialog extends DialogWrapper {
    
    private final Project project;
    private final CursorAIService aiService;
    
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    
    public CursorChatDialog(@NotNull Project project) {
        super(project);
        this.project = project;
        this.aiService = CursorAIService.getInstance();
        
        setTitle("Cursor AI 聊天");
        setModal(false);
        setResizable(true);
        
        init();
    }
    
    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(500, 400));
        
        // 创建聊天显示区域
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setWrapStyleWord(true);
        chatArea.setLineWrap(true);
        chatArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        chatArea.setText("欢迎使用 Cursor AI 聊天！\n请在下方输入您的问题...\n\n");
        
        JBScrollPane scrollPane = new JBScrollPane(chatArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // 创建输入面板
        JPanel inputPanel = createInputPanel();
        panel.add(inputPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // 输入框
        inputField = new JTextField();
        inputField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !e.isShiftDown()) {
                    sendMessage();
                    e.consume();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        // 发送按钮
        sendButton = new JButton("发送");
        sendButton.addActionListener(e -> sendMessage());
        
        panel.add(inputField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * 发送消息
     */
    private void sendMessage() {
        String message = inputField.getText().trim();
        if (message.isEmpty()) {
            return;
        }
        
        // 显示用户消息
        appendMessage("您", message);
        inputField.setText("");
        sendButton.setEnabled(false);
        
        // 调用 AI 服务
        CompletableFuture<String> future = aiService.chat(message);
        future.whenComplete((result, throwable) -> {
            ApplicationManager.getApplication().invokeLater(() -> {
                sendButton.setEnabled(true);
                
                if (throwable != null) {
                    appendMessage("AI", "抱歉，发生了错误: " + throwable.getMessage());
                } else {
                    appendMessage("AI", result);
                }
                
                // 滚动到底部
                chatArea.setCaretPosition(chatArea.getDocument().getLength());
            });
        });
    }
    
    /**
     * 添加消息到聊天区域
     */
    private void appendMessage(String sender, String message) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(String.format("[%s]: %s\n\n", sender, message));
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }
    
    @Override
    protected Action @NotNull [] createActions() {
        return new Action[]{getCancelAction()};
    }
    
    @Override
    public void show() {
        super.show();
        // 聚焦到输入框
        SwingUtilities.invokeLater(() -> inputField.requestFocus());
    }
}