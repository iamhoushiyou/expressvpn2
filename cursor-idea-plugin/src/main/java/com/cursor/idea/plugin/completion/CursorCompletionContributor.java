package com.cursor.idea.plugin.completion;

import com.cursor.idea.plugin.CursorAIService;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * Cursor AI 代码补全贡献者
 */
public class CursorCompletionContributor extends CompletionContributor {
    
    private static final Logger LOG = Logger.getInstance(CursorCompletionContributor.class);
    private final CursorAIService aiService = CursorAIService.getInstance();
    
    public CursorCompletionContributor() {
        // 为所有位置提供 AI 补全
        extend(CompletionType.BASIC, 
               PlatformPatterns.psiElement(), 
               new CursorCompletionProvider());
    }
    
    private class CursorCompletionProvider extends CompletionProvider<CompletionParameters> {
        
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters,
                                    @NotNull ProcessingContext context,
                                    @NotNull CompletionResultSet result) {
            
            if (!aiService.isAvailable()) {
                return;
            }
            
            PsiElement position = parameters.getPosition();
            String prefix = result.getPrefixMatcher().getPrefix();
            
            // 只在有意义的前缀时提供 AI 补全
            if (prefix.length() < 2) {
                return;
            }
            
            try {
                // 获取上下文代码
                String context = getContext(position);
                String language = position.getLanguage().getDisplayName();
                
                // 异步获取 AI 建议
                ApplicationManager.getApplication().executeOnPooledThread(() -> {
                    try {
                        String prompt = buildCompletionPrompt(context, prefix, language);
                        aiService.chat(prompt).whenComplete((suggestions, throwable) -> {
                            if (throwable == null && suggestions != null) {
                                ApplicationManager.getApplication().invokeLater(() -> {
                                    addAISuggestions(result, suggestions, prefix);
                                });
                            }
                        });
                    } catch (Exception e) {
                        LOG.warn("AI 补全失败", e);
                    }
                });
                
            } catch (Exception e) {
                LOG.warn("获取代码补全时发生异常", e);
            }
        }
        
        /**
         * 获取代码上下文
         */
        private String getContext(PsiElement position) {
            StringBuilder context = new StringBuilder();
            
            // 获取前面的代码行
            PsiElement current = position;
            int lineCount = 0;
            while (current != null && lineCount < 5) {
                String text = current.getText();
                if (text.contains("\n")) {
                    lineCount++;
                }
                context.insert(0, text);
                current = current.getPrevSibling();
            }
            
            return context.toString();
        }
        
        /**
         * 构建补全提示
         */
        private String buildCompletionPrompt(String context, String prefix, String language) {
            return String.format(
                "基于以下 %s 代码上下文，为前缀 '%s' 提供代码补全建议。只返回补全的代码，不要解释：\n\n%s",
                language, prefix, context
            );
        }
        
        /**
         * 添加 AI 建议到补全结果
         */
        private void addAISuggestions(CompletionResultSet result, String suggestions, String prefix) {
            String[] lines = suggestions.split("\n");
            for (String line : lines) {
                line = line.trim();
                if (line.length() > 0 && line.startsWith(prefix)) {
                    LookupElementBuilder element = LookupElementBuilder
                            .create(line)
                            .withTypeText("Cursor AI")
                            .withIcon(null); // 可以添加自定义图标
                    
                    result.addElement(element);
                }
            }
        }
    }
}