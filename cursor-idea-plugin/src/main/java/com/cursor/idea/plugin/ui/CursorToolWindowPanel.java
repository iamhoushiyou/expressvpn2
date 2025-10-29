package com.cursor.idea.plugin.ui;

import com.cursor.idea.plugin.CursorAIService;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.CompletableFuture;

/**
 * Cursor AI 工具窗口面板
 */
public class CursorToolWindowPanel extends JPanel {
    
    private final Project project;
    private final CursorAIService aiService;
    
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton clearButton;
    
    public CursorToolWindowPanel(@NotNull Project project) {
        this.project = project;
        this.aiService = CursorAIService.getInstance();
        
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // 创建顶部工具栏
        JPanel toolBar = createToolBar();
        add(toolBar, BorderLayout.NORTH);
        
        // 创建聊天区域
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setWrapStyleWord(true);
        chatArea.setLineWrap(true);
        chatArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        chatArea.setText("Cursor AI 助手已就绪\n输入您的问题开始对话...\n\n");
        
        JBScrollPane scrollPane = new JBScrollPane(chatArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
        
        // 创建输入面板
        JPanel inputPanel = createInputPanel();
        add(inputPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createToolBar() {
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        clearButton = new JButton("清空");
        clearButton.setToolTipText("清空聊天记录");
        clearButton.addActionListener(e -> clearChat());
        toolBar.add(clearButton);
        
        JButton helpButton = new JButton("帮助");
        helpButton.setToolTipText("显示使用帮助");
        helpButton.addActionListener(e -> showHelp());
        toolBar.add(helpButton);
        
        return toolBar;
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // 输入框
        inputField = new JTextField();
        inputField.setToolTipText("输入您的问题，按 Enter 发送");
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
        sendButton.setToolTipText("发送消息");
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
        
        // 检查服务是否可用
        if (!aiService.isAvailable()) {
            appendMessage("系统", "AI 服务不可用，请检查配置");
            return;
        }
        
        // 显示用户消息
        appendMessage("您", message);
        inputField.setText("");
        setInputEnabled(false);
        
        // 显示正在处理的提示
        appendMessage("AI", "正在思考...");
        
        // 调用 AI 服务
        CompletableFuture<String> future = aiService.chat(message);
        future.whenComplete((result, throwable) -> {
            ApplicationManager.getApplication().invokeLater(() -> {
                // 移除"正在思考..."的消息
                removeLastMessage();
                
                setInputEnabled(true);
                inputField.requestFocus();
                
                if (throwable != null) {
                    appendMessage("AI", "抱歉，发生了错误: " + throwable.getMessage());
                } else {
                    appendMessage("AI", result);
                }
                
                // 滚动到底部
                scrollToBottom();
            });
        });
    }
    
    /**
     * 添加消息到聊天区域
     */
    private void appendMessage(String sender, String message) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = java.time.LocalTime.now().toString().substring(0, 8);
            chatArea.append(String.format("[%s %s]: %s\n\n", timestamp, sender, message));
            scrollToBottom();
        });
    }
    
    /**
     * 移除最后一条消息
     */
    private void removeLastMessage() {
        String text = chatArea.getText();
        int lastIndex = text.lastIndexOf("[");
        if (lastIndex > 0) {
            chatArea.setText(text.substring(0, lastIndex));
        }
    }
    
    /**
     * 滚动到底部
     */
    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }
    
    /**
     * 清空聊天记录
     */
    private void clearChat() {
        chatArea.setText("Cursor AI 助手已就绪\n输入您的问题开始对话...\n\n");
    }
    
    /**
     * 显示帮助信息
     */
    private void showHelp() {
        String helpText = "Cursor AI 助手使用说明:\n\n" +
                "• 在输入框中输入问题，按 Enter 发送\n" +
                "• 支持代码相关的问题和解答\n" +
                "• 可以请求代码解释、优化建议等\n" +
                "• 使用右键菜单快速调用 AI 功能\n" +
                "• 快捷键:\n" +
                "  - Ctrl+Shift+E: 解释代码\n" +
                "  - Ctrl+Shift+O: 优化代码\n" +
                "  - Ctrl+Shift+G: 生成代码\n" +
                "  - Ctrl+Shift+F: 修复代码\n" +
                "  - Ctrl+Shift+C: 打开聊天窗口";
        
        appendMessage("帮助", helpText);
    }
    
    /**
     * 设置输入控件的启用状态
     */
    private void setInputEnabled(boolean enabled) {
        inputField.setEnabled(enabled);
        sendButton.setEnabled(enabled);
    }
}