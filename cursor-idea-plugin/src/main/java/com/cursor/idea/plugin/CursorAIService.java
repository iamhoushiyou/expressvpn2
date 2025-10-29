package com.cursor.idea.plugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Cursor AI 服务类
 * 负责与 Cursor AI API 的通信和交互
 */
public class CursorAIService {
    
    private static final Logger LOG = Logger.getInstance(CursorAIService.class);
    private static CursorAIService instance;
    
    // API 配置
    private static final String DEFAULT_API_URL = "https://api.cursor.com/v1/chat/completions";
    private String apiUrl = DEFAULT_API_URL;
    private String apiKey = "";
    
    // 线程池
    private final ExecutorService executorService;
    
    private CursorAIService() {
        this.executorService = Executors.newFixedThreadPool(3);
        loadConfiguration();
    }
    
    public static synchronized CursorAIService getInstance() {
        if (instance == null) {
            instance = new CursorAIService();
        }
        return instance;
    }
    
    /**
     * 初始化项目相关设置
     */
    public void initializeForProject(@NotNull Project project) {
        LOG.info("为项目初始化 Cursor AI 服务: " + project.getName());
        // 这里可以添加项目特定的初始化逻辑
    }
    
    /**
     * 加载配置
     */
    private void loadConfiguration() {
        // 从系统属性或环境变量加载 API 密钥
        apiKey = System.getProperty("cursor.api.key", 
                System.getenv("CURSOR_API_KEY"));
        
        if (StringUtil.isEmpty(apiKey)) {
            LOG.warn("未找到 Cursor API 密钥，请设置 CURSOR_API_KEY 环境变量或 cursor.api.key 系统属性");
        }
        
        // 加载 API URL
        String customUrl = System.getProperty("cursor.api.url", 
                System.getenv("CURSOR_API_URL"));
        if (!StringUtil.isEmpty(customUrl)) {
            apiUrl = customUrl;
        }
    }
    
    /**
     * 异步解释代码
     */
    public CompletableFuture<String> explainCode(@NotNull String code, @Nullable String language) {
        return CompletableFuture.supplyAsync(() -> {
            String prompt = buildExplainPrompt(code, language);
            return callAI(prompt);
        }, executorService);
    }
    
    /**
     * 异步优化代码
     */
    public CompletableFuture<String> optimizeCode(@NotNull String code, @Nullable String language) {
        return CompletableFuture.supplyAsync(() -> {
            String prompt = buildOptimizePrompt(code, language);
            return callAI(prompt);
        }, executorService);
    }
    
    /**
     * 异步生成代码
     */
    public CompletableFuture<String> generateCode(@NotNull String description, @Nullable String language) {
        return CompletableFuture.supplyAsync(() -> {
            String prompt = buildGeneratePrompt(description, language);
            return callAI(prompt);
        }, executorService);
    }
    
    /**
     * 异步修复代码
     */
    public CompletableFuture<String> fixCode(@NotNull String code, @NotNull String error, @Nullable String language) {
        return CompletableFuture.supplyAsync(() -> {
            String prompt = buildFixPrompt(code, error, language);
            return callAI(prompt);
        }, executorService);
    }
    
    /**
     * 自由对话
     */
    public CompletableFuture<String> chat(@NotNull String message) {
        return CompletableFuture.supplyAsync(() -> {
            return callAI(message);
        }, executorService);
    }
    
    /**
     * 构建解释代码的提示
     */
    private String buildExplainPrompt(String code, String language) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("请详细解释以下");
        if (language != null) {
            prompt.append(language);
        }
        prompt.append("代码的功能和实现逻辑：\n\n");
        prompt.append("```");
        if (language != null) {
            prompt.append(language.toLowerCase());
        }
        prompt.append("\n");
        prompt.append(code);
        prompt.append("\n```\n\n");
        prompt.append("请包括：\n");
        prompt.append("1. 代码的主要功能\n");
        prompt.append("2. 关键算法和逻辑\n");
        prompt.append("3. 可能的改进建议\n");
        return prompt.toString();
    }
    
    /**
     * 构建优化代码的提示
     */
    private String buildOptimizePrompt(String code, String language) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("请优化以下");
        if (language != null) {
            prompt.append(language);
        }
        prompt.append("代码，提高其性能、可读性和可维护性：\n\n");
        prompt.append("```");
        if (language != null) {
            prompt.append(language.toLowerCase());
        }
        prompt.append("\n");
        prompt.append(code);
        prompt.append("\n```\n\n");
        prompt.append("请提供优化后的代码和优化说明。");
        return prompt.toString();
    }
    
    /**
     * 构建生成代码的提示
     */
    private String buildGeneratePrompt(String description, String language) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("请根据以下需求生成");
        if (language != null) {
            prompt.append(language);
        }
        prompt.append("代码：\n\n");
        prompt.append(description);
        prompt.append("\n\n请提供完整的、可运行的代码实现。");
        return prompt.toString();
    }
    
    /**
     * 构建修复代码的提示
     */
    private String buildFixPrompt(String code, String error, String language) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("以下");
        if (language != null) {
            prompt.append(language);
        }
        prompt.append("代码出现了错误，请帮助修复：\n\n");
        prompt.append("代码：\n```");
        if (language != null) {
            prompt.append(language.toLowerCase());
        }
        prompt.append("\n");
        prompt.append(code);
        prompt.append("\n```\n\n");
        prompt.append("错误信息：\n");
        prompt.append(error);
        prompt.append("\n\n请提供修复后的代码和修复说明。");
        return prompt.toString();
    }
    
    /**
     * 调用 AI API
     */
    private String callAI(String prompt) {
        if (StringUtil.isEmpty(apiKey)) {
            return "错误：未配置 Cursor API 密钥。请设置 CURSOR_API_KEY 环境变量。";
        }
        
        try {
            // 构建请求 JSON
            String jsonRequest = buildJsonRequest(prompt);
            
            // 发送 HTTP 请求
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setDoOutput(true);
            
            // 发送请求体
            try (OutputStreamWriter writer = new OutputStreamWriter(
                    connection.getOutputStream(), StandardCharsets.UTF_8)) {
                writer.write(jsonRequest);
                writer.flush();
            }
            
            // 读取响应
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readResponse(connection.getInputStream());
            } else {
                String errorResponse = readResponse(connection.getErrorStream());
                LOG.error("API 调用失败，响应代码: " + responseCode + ", 错误: " + errorResponse);
                return "API 调用失败: " + errorResponse;
            }
            
        } catch (Exception e) {
            LOG.error("调用 Cursor AI API 时发生异常", e);
            return "调用 AI 服务时发生错误: " + e.getMessage();
        }
    }
    
    /**
     * 构建 JSON 请求
     */
    private String buildJsonRequest(String prompt) {
        // 简化的 JSON 构建，实际项目中建议使用 JSON 库
        return String.format(
            "{\"model\":\"gpt-4\",\"messages\":[{\"role\":\"user\",\"content\":\"%s\"}],\"max_tokens\":2000,\"temperature\":0.7}",
            escapeJson(prompt)
        );
    }
    
    /**
     * 转义 JSON 字符串
     */
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    /**
     * 读取响应
     */
    private String readResponse(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return "无响应内容";
        }
        
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
        }
        
        // 简化的 JSON 解析，提取 content 字段
        String responseText = response.toString();
        int contentStart = responseText.indexOf("\"content\":\"");
        if (contentStart != -1) {
            contentStart += 11; // "content":"的长度
            int contentEnd = responseText.indexOf("\",", contentStart);
            if (contentEnd == -1) {
                contentEnd = responseText.indexOf("\"}", contentStart);
            }
            if (contentEnd != -1) {
                return unescapeJson(responseText.substring(contentStart, contentEnd));
            }
        }
        
        return responseText;
    }
    
    /**
     * 反转义 JSON 字符串
     */
    private String unescapeJson(String text) {
        return text.replace("\\n", "\n")
                  .replace("\\r", "\r")
                  .replace("\\t", "\t")
                  .replace("\\\"", "\"")
                  .replace("\\\\", "\\");
    }
    
    /**
     * 设置 API 密钥
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    /**
     * 设置 API URL
     */
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
    
    /**
     * 检查服务是否可用
     */
    public boolean isAvailable() {
        return !StringUtil.isEmpty(apiKey);
    }
    
    /**
     * 释放资源
     */
    public void dispose() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}