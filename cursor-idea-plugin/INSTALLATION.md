# Cursor AI Assistant 安装指南

本文档提供了详细的安装和配置步骤，帮助您快速开始使用 Cursor AI Assistant 插件。

## 📋 系统要求

在开始安装之前，请确保您的系统满足以下要求：

### 最低要求
- **IntelliJ IDEA**: 2023.1 或更高版本
- **Java**: JDK 11 或更高版本
- **内存**: 至少 4GB RAM
- **磁盘空间**: 100MB 可用空间
- **网络**: 稳定的互联网连接

### 推荐配置
- **IntelliJ IDEA**: 最新版本
- **Java**: JDK 17 或更高版本
- **内存**: 8GB RAM 或更多
- **网络**: 高速互联网连接

## 🚀 安装方法

### 方法一：JetBrains 插件市场（推荐）

这是最简单和推荐的安装方法：

1. **打开插件市场**
   - 启动 IntelliJ IDEA
   - 进入 `File` → `Settings` (Windows/Linux) 或 `IntelliJ IDEA` → `Preferences` (macOS)
   - 在左侧面板中选择 `Plugins`

2. **搜索插件**
   - 点击 `Marketplace` 标签
   - 在搜索框中输入 "Cursor AI Assistant"
   - 找到由 Cursor Team 开发的插件

3. **安装插件**
   - 点击插件卡片上的 `Install` 按钮
   - 等待下载和安装完成
   - 点击 `Restart IDE` 重启 IDEA

### 方法二：手动安装 ZIP 文件

如果无法从市场安装，可以手动安装：

1. **下载插件文件**
   - 访问 [GitHub Releases](https://github.com/cursor/cursor-idea-plugin/releases)
   - 下载最新版本的 `cursor-idea-plugin-x.x.x.zip` 文件

2. **安装插件**
   - 打开 IntelliJ IDEA
   - 进入 `File` → `Settings` → `Plugins`
   - 点击齿轮图标 ⚙️
   - 选择 `Install Plugin from Disk...`
   - 浏览并选择下载的 ZIP 文件
   - 点击 `OK` 确认安装

3. **重启 IDEA**
   - 安装完成后重启 IntelliJ IDEA

### 方法三：从源码构建

适合开发者和高级用户：

1. **克隆仓库**
   ```bash
   git clone https://github.com/cursor/cursor-idea-plugin.git
   cd cursor-idea-plugin
   ```

2. **构建插件**
   ```bash
   # 确保已安装 JDK 11+
   ./gradlew buildPlugin
   ```

3. **安装构建的插件**
   - 构建完成后，在 `build/distributions/` 目录下找到生成的 ZIP 文件
   - 按照方法二的步骤手动安装

## ⚙️ 配置 API 密钥

插件安装完成后，需要配置 Cursor AI API 密钥才能使用：

### 获取 API 密钥

1. **访问 Cursor 官网**
   - 打开浏览器访问 [https://cursor.com](https://cursor.com)
   - 如果没有账户，请先注册

2. **登录账户**
   - 使用您的邮箱和密码登录
   - 或使用 GitHub/Google 账户登录

3. **生成 API 密钥**
   - 进入账户设置或 API 管理页面
   - 点击 "生成新的 API 密钥"
   - 复制生成的密钥（请妥善保存）

### 配置密钥

#### 方法一：环境变量（推荐）

**Windows:**
```cmd
# 临时设置（当前会话有效）
set CURSOR_API_KEY=your-api-key-here

# 永久设置
setx CURSOR_API_KEY "your-api-key-here"
```

**macOS/Linux:**
```bash
# 临时设置（当前会话有效）
export CURSOR_API_KEY="your-api-key-here"

# 永久设置（添加到 ~/.bashrc 或 ~/.zshrc）
echo 'export CURSOR_API_KEY="your-api-key-here"' >> ~/.bashrc
source ~/.bashrc
```

#### 方法二：IDEA 启动参数

在 IDEA 的启动配置中添加系统属性：

1. **找到 IDEA 配置文件**
   - Windows: `%APPDATA%\JetBrains\IntelliJIdea2023.x\idea64.exe.vmoptions`
   - macOS: `~/Library/Application Support/JetBrains/IntelliJIdea2023.x/idea.vmoptions`
   - Linux: `~/.config/JetBrains/IntelliJIdea2023.x/idea64.vmoptions`

2. **添加配置行**
   ```
   -Dcursor.api.key=your-api-key-here
   ```

3. **重启 IDEA**

#### 方法三：项目级配置

在项目根目录创建 `.cursor` 文件：
```properties
api.key=your-api-key-here
api.url=https://api.cursor.com/v1/chat/completions
```

### 自定义 API 端点（可选）

如果您使用自定义的 API 端点：

```bash
# 环境变量
export CURSOR_API_URL="https://your-custom-api.com/v1/chat/completions"

# 或系统属性
-Dcursor.api.url=https://your-custom-api.com/v1/chat/completions
```

## ✅ 验证安装

安装和配置完成后，验证插件是否正常工作：

### 1. 检查插件状态
- 进入 `File` → `Settings` → `Plugins`
- 在 `Installed` 标签中找到 "Cursor AI Assistant"
- 确保插件已启用（复选框已勾选）

### 2. 测试基本功能
1. **打开任意代码文件**
2. **选中一段代码**
3. **右键点击**，查看是否有 "Cursor AI" 菜单项
4. **尝试快捷键** `Ctrl+Shift+E` 解释代码

### 3. 检查工具窗口
- 进入 `View` → `Tool Windows`
- 查看是否有 "Cursor AI" 选项
- 点击打开工具窗口

### 4. 测试 API 连接
1. 打开 Cursor AI 工具窗口
2. 输入简单的问题，如 "Hello"
3. 检查是否收到 AI 回复

## 🔧 故障排除

### 插件未显示

**可能原因**:
- IDEA 版本不兼容
- 插件安装失败
- 需要重启 IDEA

**解决方案**:
1. 检查 IDEA 版本是否为 2023.1+
2. 重新安装插件
3. 完全重启 IDEA
4. 检查插件是否在已安装列表中

### API 密钥无效

**症状**: 提示 "AI 服务不可用" 或 "API 密钥未配置"

**解决方案**:
1. 验证 API 密钥是否正确
2. 检查环境变量是否正确设置
3. 重启 IDEA 以加载新的环境变量
4. 确认 API 密钥有足够的配额

### 网络连接问题

**症状**: AI 请求超时或连接失败

**解决方案**:
1. 检查网络连接
2. 配置代理设置（如果需要）
3. 检查防火墙设置
4. 尝试使用不同的网络

### 功能不响应

**症状**: 点击菜单项或快捷键无反应

**解决方案**:
1. 检查是否选中了代码（某些功能需要）
2. 确认 API 密钥已正确配置
3. 查看 IDEA 日志文件
4. 重启插件或 IDEA

## 📊 性能优化

### 内存设置
如果遇到性能问题，可以调整 IDEA 内存设置：

```
# 在 idea.vmoptions 中增加内存
-Xmx4096m
-XX:ReservedCodeCacheSize=1024m
```

### 网络优化
- 使用稳定的网络连接
- 如果在企业网络中，配置适当的代理
- 考虑使用本地 API 端点（如果可用）

## 🔄 更新插件

### 自动更新
- IDEA 会自动检查插件更新
- 在 `File` → `Settings` → `Plugins` 中查看可用更新
- 点击 `Update` 按钮更新到最新版本

### 手动更新
1. 下载最新版本的插件文件
2. 卸载旧版本插件
3. 安装新版本插件
4. 重启 IDEA

## 📞 获取帮助

如果在安装过程中遇到问题：

- **查看文档**: [GitHub Wiki](https://github.com/cursor/cursor-idea-plugin/wiki)
- **报告问题**: [GitHub Issues](https://github.com/cursor/cursor-idea-plugin/issues)
- **社区讨论**: [GitHub Discussions](https://github.com/cursor/cursor-idea-plugin/discussions)
- **邮件支持**: support@cursor.com

## 📝 下一步

安装完成后，建议：

1. 阅读 [用户指南](USER_GUIDE.md) 了解详细功能
2. 查看 [快捷键参考](SHORTCUTS.md)
3. 探索 [高级配置选项](ADVANCED_CONFIG.md)
4. 加入社区获取最新信息和技巧

---

恭喜！您已成功安装 Cursor AI Assistant。现在可以开始享受 AI 辅助编程的便利了！ 🎉