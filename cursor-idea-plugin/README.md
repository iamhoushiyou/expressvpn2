# Cursor AI Assistant for IntelliJ IDEA

[![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)](https://github.com/cursor/cursor-idea-plugin)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)

一个强大的 IntelliJ IDEA 插件，将 Cursor AI 的智能代码助手功能直接集成到您的开发环境中，让您无需切换工具即可享受 AI 辅助编程的便利。

## 🚀 功能特性

### 核心功能
- **智能代码解释** - 使用 AI 详细解释代码逻辑和功能
- **代码优化建议** - 获得性能和可读性改进建议
- **智能代码生成** - 根据描述生成高质量代码
- **错误修复助手** - AI 辅助诊断和修复代码问题
- **实时聊天助手** - 与 AI 进行自然语言交互

### 界面集成
- **右键菜单集成** - 在编辑器中右键即可调用 AI 功能
- **专用工具窗口** - 独立的 AI 聊天和交互面板
- **快捷键支持** - 快速访问常用 AI 功能
- **意图动作** - 智能代码建议和快速修复

### 高级功能
- **代码补全** - AI 驱动的智能代码补全
- **代码检查** - 自动检测代码质量问题
- **多语言支持** - 支持 Java、Python、JavaScript 等多种编程语言

## 📦 安装方法

### 方法一：从 JetBrains 插件市场安装（推荐）
1. 打开 IntelliJ IDEA
2. 进入 `File` → `Settings` → `Plugins`
3. 搜索 "Cursor AI Assistant"
4. 点击 `Install` 安装插件
5. 重启 IDEA

### 方法二：手动安装
1. 下载最新的插件文件 `cursor-idea-plugin-1.0.0.zip`
2. 打开 IntelliJ IDEA
3. 进入 `File` → `Settings` → `Plugins`
4. 点击齿轮图标 → `Install Plugin from Disk...`
5. 选择下载的 zip 文件
6. 重启 IDEA

### 方法三：从源码构建
```bash
# 克隆仓库
git clone https://github.com/cursor/cursor-idea-plugin.git
cd cursor-idea-plugin

# 构建插件
./gradlew buildPlugin

# 生成的插件文件位于 build/distributions/
```

## ⚙️ 配置设置

### API 密钥配置
插件需要 Cursor AI API 密钥才能正常工作。您可以通过以下方式配置：

#### 环境变量（推荐）
```bash
export CURSOR_API_KEY="your-api-key-here"
```

#### 系统属性
```bash
-Dcursor.api.key=your-api-key-here
```

#### 自定义 API 端点
如果您使用自定义的 API 端点：
```bash
export CURSOR_API_URL="https://your-custom-api.com/v1/chat/completions"
```

### 获取 API 密钥
1. 访问 [Cursor 官网](https://cursor.com)
2. 登录您的账户
3. 进入 API 设置页面
4. 生成新的 API 密钥

## 🎯 使用指南

### 基本操作

#### 1. 解释代码
- **快捷键**: `Ctrl+Shift+E`
- **操作**: 选中代码 → 右键 → `Cursor AI` → `解释代码`
- **功能**: AI 会详细解释代码的功能、算法和实现逻辑

#### 2. 优化代码
- **快捷键**: `Ctrl+Shift+O`
- **操作**: 选中代码 → 右键 → `Cursor AI` → `优化代码`
- **功能**: 获得性能优化、可读性改进等建议

#### 3. 生成代码
- **快捷键**: `Ctrl+Shift+G`
- **操作**: 右键 → `Cursor AI` → `生成代码`
- **功能**: 根据需求描述生成相应的代码实现

#### 4. 修复代码
- **快捷键**: `Ctrl+Shift+F`
- **操作**: 选中有问题的代码 → 右键 → `Cursor AI` → `修复代码`
- **功能**: AI 分析并提供错误修复方案

#### 5. AI 聊天
- **快捷键**: `Ctrl+Shift+C`
- **操作**: 菜单栏 → `Cursor AI` → `打开 AI 聊天`
- **功能**: 与 AI 进行自然语言对话，获得编程帮助

### 高级功能

#### 工具窗口
1. 打开 `View` → `Tool Windows` → `Cursor AI`
2. 在工具窗口中可以：
   - 与 AI 实时聊天
   - 查看历史对话
   - 快速访问常用功能

#### 意图动作
当光标位于代码上时，按 `Alt+Enter` 可以看到 Cursor AI 提供的智能建议：
- "使用 Cursor AI 解释代码"
- "使用 Cursor AI 优化代码"

#### 代码补全
在编写代码时，插件会自动提供 AI 驱动的代码补全建议，标记为 "Cursor AI"。

## 🛠️ 开发指南

### 项目结构
```
cursor-idea-plugin/
├── src/main/java/com/cursor/idea/plugin/
│   ├── CursorPluginComponent.java      # 插件主组件
│   ├── CursorAIService.java            # AI 服务核心
│   ├── actions/                        # 动作类
│   │   ├── BaseAIAction.java
│   │   ├── ExplainCodeAction.java
│   │   ├── OptimizeCodeAction.java
│   │   ├── GenerateCodeAction.java
│   │   ├── FixCodeAction.java
│   │   └── OpenChatAction.java
│   ├── ui/                             # 用户界面
│   │   ├── AIResultDialog.java
│   │   ├── CursorChatDialog.java
│   │   ├── CursorToolWindowFactory.java
│   │   └── CursorToolWindowPanel.java
│   ├── intentions/                     # 意图动作
│   │   ├── ExplainCodeIntention.java
│   │   └── OptimizeCodeIntention.java
│   ├── inspections/                    # 代码检查
│   │   ├── CursorAIInspection.java
│   │   └── CursorAIQuickFix.java
│   └── completion/                     # 代码补全
│       └── CursorCompletionContributor.java
├── src/main/resources/
│   ├── META-INF/plugin.xml             # 插件配置
│   └── icons/                          # 图标资源
├── build.gradle                        # 构建配置
└── README.md                           # 项目文档
```

### 构建命令
```bash
# 编译项目
./gradlew compileJava

# 运行测试
./gradlew test

# 构建插件
./gradlew buildPlugin

# 在 IDEA 中运行插件（用于调试）
./gradlew runIde

# 验证插件
./gradlew verifyPlugin

# 发布插件
./gradlew publishPlugin
```

### 调试插件
1. 运行 `./gradlew runIde`
2. 这会启动一个带有插件的 IDEA 实例
3. 在这个实例中测试插件功能
4. 可以在原 IDEA 中设置断点进行调试

## 🔧 故障排除

### 常见问题

#### 1. API 密钥未配置
**症状**: 插件功能不可用，提示 "AI 服务不可用"
**解决方案**: 
- 检查环境变量 `CURSOR_API_KEY` 是否正确设置
- 确认 API 密钥有效且有足够的配额

#### 2. 网络连接问题
**症状**: AI 请求超时或失败
**解决方案**:
- 检查网络连接
- 确认防火墙设置允许访问 Cursor API
- 如果使用代理，请配置相应的代理设置

#### 3. 插件加载失败
**症状**: 插件安装后不显示或功能异常
**解决方案**:
- 检查 IDEA 版本兼容性（支持 2023.1+）
- 重启 IDEA
- 检查插件是否正确启用

#### 4. 代码补全不工作
**症状**: 没有看到 AI 代码补全建议
**解决方案**:
- 确保输入的前缀长度至少 2 个字符
- 检查 API 配置是否正确
- 在设置中确认代码补全功能已启用

### 日志调试
启用调试日志：
1. 打开 `Help` → `Diagnostic Tools` → `Debug Log Settings`
2. 添加 `com.cursor.idea.plugin`
3. 重现问题并查看日志文件

## 📋 系统要求

- **IntelliJ IDEA**: 2023.1 或更高版本
- **Java**: JDK 11 或更高版本
- **操作系统**: Windows, macOS, Linux
- **网络**: 需要互联网连接访问 Cursor AI API

## 🤝 贡献指南

我们欢迎社区贡献！请遵循以下步骤：

1. Fork 本仓库
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

### 开发规范
- 遵循 Java 代码规范
- 添加适当的注释和文档
- 编写单元测试
- 确保代码通过所有检查

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 🆘 支持与反馈

- **问题报告**: [GitHub Issues](https://github.com/cursor/cursor-idea-plugin/issues)
- **功能请求**: [GitHub Discussions](https://github.com/cursor/cursor-idea-plugin/discussions)
- **邮件支持**: support@cursor.com
- **文档**: [Wiki](https://github.com/cursor/cursor-idea-plugin/wiki)

## 🔄 更新日志

### v1.0.0 (2024-01-XX)
- 🎉 初始版本发布
- ✨ 基本 AI 功能集成
- 🎨 用户界面设计
- 🚀 代码补全和检查功能
- 📚 完整文档和使用指南

## 🙏 致谢

感谢以下项目和团队：
- [Cursor](https://cursor.com) - 提供强大的 AI 编程助手
- [JetBrains](https://jetbrains.com) - 优秀的 IDE 平台
- [IntelliJ Platform SDK](https://plugins.jetbrains.com/docs/intellij/) - 插件开发框架

---

**让 AI 成为您编程路上的最佳伙伴！** 🚀✨