package com.cursor.idea.plugin;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

/**
 * Cursor AI 插件主组件
 * 负责插件的初始化和生命周期管理
 */
public class CursorPluginComponent implements ApplicationComponent {
    
    private static final Logger LOG = Logger.getInstance(CursorPluginComponent.class);
    private CursorAIService cursorAIService;
    
    @Override
    public void initComponent() {
        LOG.info("Cursor AI Plugin 正在初始化...");
        
        // 初始化 AI 服务
        cursorAIService = CursorAIService.getInstance();
        
        LOG.info("Cursor AI Plugin 初始化完成");
    }
    
    @Override
    public void disposeComponent() {
        LOG.info("Cursor AI Plugin 正在关闭...");
        
        if (cursorAIService != null) {
            cursorAIService.dispose();
        }
        
        LOG.info("Cursor AI Plugin 已关闭");
    }
    
    @NotNull
    @Override
    public String getComponentName() {
        return "CursorPluginComponent";
    }
    
    /**
     * 获取 AI 服务实例
     */
    public static CursorAIService getAIService() {
        return CursorAIService.getInstance();
    }
    
    /**
     * 插件启动活动
     */
    public static class CursorStartupActivity implements StartupActivity {
        
        @Override
        public void runActivity(@NotNull Project project) {
            LOG.info("Cursor AI Plugin 在项目中启动: " + project.getName());
            
            // 在项目启动时进行必要的初始化
            CursorAIService aiService = CursorAIService.getInstance();
            aiService.initializeForProject(project);
        }
    }
}