package com.cursor.idea.plugin.inspections;

import com.cursor.idea.plugin.CursorAIService;
import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

/**
 * Cursor AI 代码检查器
 * 使用 AI 分析代码质量和潜在问题
 */
public class CursorAIInspection extends AbstractBaseJavaLocalInspectionTool {
    
    private static final Logger LOG = Logger.getInstance(CursorAIInspection.class);
    private final CursorAIService aiService = CursorAIService.getInstance();
    
    @NotNull
    @Override
    public String getShortName() {
        return "CursorAIInspection";
    }
    
    @NotNull
    @Override
    public String getDisplayName() {
        return "Cursor AI 代码分析";
    }
    
    @NotNull
    @Override
    public String getGroupDisplayName() {
        return "Cursor AI";
    }
    
    @Override
    public boolean isEnabledByDefault() {
        return true;
    }
    
    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        if (!aiService.isAvailable()) {
            return PsiElementVisitor.EMPTY_VISITOR;
        }
        
        return new JavaElementVisitor() {
            
            @Override
            public void visitMethod(PsiMethod method) {
                super.visitMethod(method);
                
                // 检查方法复杂度
                if (isMethodTooComplex(method)) {
                    holder.registerProblem(method.getNameIdentifier(),
                            "方法可能过于复杂，建议使用 Cursor AI 进行重构",
                            new CursorAIQuickFix("优化方法"));
                }
            }
            
            @Override
            public void visitClass(PsiClass aClass) {
                super.visitClass(aClass);
                
                // 检查类的设计
                if (isClassTooLarge(aClass)) {
                    holder.registerProblem(aClass.getNameIdentifier(),
                            "类可能过于庞大，建议使用 Cursor AI 分析重构方案",
                            new CursorAIQuickFix("分析类结构"));
                }
            }
        };
    }
    
    /**
     * 检查方法是否过于复杂
     */
    private boolean isMethodTooComplex(PsiMethod method) {
        if (method.getBody() == null) {
            return false;
        }
        
        // 简单的复杂度检查：行数和嵌套层级
        String methodText = method.getBody().getText();
        int lineCount = methodText.split("\n").length;
        
        return lineCount > 50; // 超过50行认为可能过于复杂
    }
    
    /**
     * 检查类是否过于庞大
     */
    private boolean isClassTooLarge(PsiClass aClass) {
        PsiMethod[] methods = aClass.getMethods();
        PsiField[] fields = aClass.getFields();
        
        // 简单检查：方法数量和字段数量
        return methods.length > 20 || fields.length > 15;
    }
}