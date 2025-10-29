# Cursor AI Assistant 用户指南

欢迎使用 Cursor AI Assistant！本指南将帮助您充分利用插件的所有功能，提升您的编程效率。

## 🎯 快速开始

### 第一次使用

1. **确认插件已激活**
   - 查看 IDEA 右侧是否有 "Cursor AI" 工具窗口
   - 右键点击代码，检查是否有 "Cursor AI" 菜单

2. **测试基本功能**
   ```java
   // 选中这段代码，然后按 Ctrl+Shift+E
   public class HelloWorld {
       public static void main(String[] args) {
           System.out.println("Hello, World!");
       }
   }
   ```

3. **打开聊天窗口**
   - 按 `Ctrl+Shift+C` 或点击菜单 `Cursor AI` → `打开 AI 聊天`
   - 输入 "你好" 测试 AI 响应

## 🛠️ 核心功能详解

### 1. 代码解释 (Ctrl+Shift+E)

**用途**: 理解复杂代码逻辑，学习新的编程概念

**使用方法**:
1. 选中要解释的代码段
2. 按 `Ctrl+Shift+E` 或右键选择 "解释代码"
3. AI 会提供详细的代码分析

**示例场景**:
```java
// 选中这个复杂的算法
public int binarySearch(int[] arr, int target) {
    int left = 0, right = arr.length - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (arr[mid] == target) return mid;
        if (arr[mid] < target) left = mid + 1;
        else right = mid - 1;
    }
    return -1;
}
```

**AI 会解释**:
- 算法的工作原理
- 时间复杂度分析
- 关键步骤说明
- 可能的改进建议

### 2. 代码优化 (Ctrl+Shift+O)

**用途**: 提升代码性能、可读性和可维护性

**使用方法**:
1. 选中需要优化的代码
2. 按 `Ctrl+Shift+O` 或右键选择 "优化代码"
3. 查看 AI 提供的优化建议和改进后的代码

**优化类型**:
- **性能优化**: 减少时间复杂度，优化内存使用
- **可读性提升**: 改善变量命名，简化逻辑
- **代码重构**: 提取方法，消除重复代码
- **最佳实践**: 应用设计模式，遵循编码规范

**示例**:
```java
// 原始代码（可能需要优化）
public List<String> processData(List<String> data) {
    List<String> result = new ArrayList<>();
    for (int i = 0; i < data.size(); i++) {
        String item = data.get(i);
        if (item != null && item.length() > 0) {
            result.add(item.toUpperCase());
        }
    }
    return result;
}
```

### 3. 代码生成 (Ctrl+Shift+G)

**用途**: 根据需求描述快速生成代码实现

**使用方法**:
1. 将光标放在要插入代码的位置
2. 按 `Ctrl+Shift+G` 或右键选择 "生成代码"
3. 在弹出的对话框中描述您的需求
4. AI 会生成相应的代码实现

**生成示例**:

**需求**: "创建一个用户管理类，包含增删改查功能"

**生成的代码**:
```java
public class UserManager {
    private List<User> users;
    
    public UserManager() {
        this.users = new ArrayList<>();
    }
    
    public void addUser(User user) {
        if (user != null && !users.contains(user)) {
            users.add(user);
        }
    }
    
    public boolean removeUser(String userId) {
        return users.removeIf(user -> user.getId().equals(userId));
    }
    
    public User findUser(String userId) {
        return users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }
    
    public void updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(updatedUser.getId())) {
                users.set(i, updatedUser);
                break;
            }
        }
    }
}
```

### 4. 错误修复 (Ctrl+Shift+F)

**用途**: 诊断和修复代码中的错误

**使用方法**:
1. 选中有问题的代码
2. 按 `Ctrl+Shift+F` 或右键选择 "修复代码"
3. 描述遇到的错误或让 AI 自动分析
4. 获得修复建议和正确的代码实现

**修复类型**:
- **语法错误**: 修正编译错误
- **逻辑错误**: 修复程序逻辑问题
- **性能问题**: 解决性能瓶颈
- **安全漏洞**: 修复安全相关问题

### 5. AI 聊天助手 (Ctrl+Shift+C)

**用途**: 与 AI 进行自然语言交互，获得编程帮助

**使用方法**:
1. 按 `Ctrl+Shift+C` 打开聊天窗口
2. 输入您的问题或需求
3. 与 AI 进行对话式交互

**聊天场景**:
- **学习新技术**: "如何在 Spring Boot 中实现 JWT 认证？"
- **解决问题**: "为什么我的代码会出现 NullPointerException？"
- **架构讨论**: "微服务架构的优缺点是什么？"
- **代码审查**: "这段代码有什么可以改进的地方？"

## 🎨 用户界面指南

### 工具窗口

**位置**: IDEA 右侧面板 "Cursor AI"

**功能**:
- **聊天区域**: 显示对话历史
- **输入框**: 输入问题和指令
- **工具栏**: 快速操作按钮
  - 清空: 清除聊天记录
  - 帮助: 显示使用提示

**使用技巧**:
- 按 `Enter` 发送消息
- 使用 `Shift+Enter` 换行
- 点击历史消息可以复制内容

### 结果对话框

当使用代码解释、优化等功能时，会弹出结果对话框：

**功能按钮**:
- **复制结果**: 将 AI 回复复制到剪贴板
- **应用到编辑器**: 将优化后的代码替换原代码
- **关闭**: 关闭对话框

### 右键菜单

在编辑器中右键点击可以看到 "Cursor AI" 子菜单：
- 解释代码
- 优化代码  
- 生成代码
- 修复代码

## ⚡ 高级功能

### 意图动作 (Alt+Enter)

当光标位于代码上时，按 `Alt+Enter` 可以看到智能建议：

```java
public void processData() {
    // 光标在这里按 Alt+Enter
    // 会看到 "使用 Cursor AI 解释代码" 等选项
}
```

### 代码补全

在编写代码时，AI 会提供智能补全建议：

```java
public class Calculator {
    public int add(int a, int b) {
        return // 这里会出现 AI 补全建议
    }
}
```

**识别 AI 建议**: 补全项会标记为 "Cursor AI"

### 代码检查

插件会自动检查代码质量问题：

- **复杂方法**: 超过 50 行的方法会被标记
- **大型类**: 超过 20 个方法的类会被提示重构
- **代码异味**: 检测常见的代码问题

**查看建议**: 在标记的代码上按 `Alt+Enter` 查看 AI 建议

## 💡 使用技巧和最佳实践

### 1. 有效的提问技巧

**好的问题**:
```
"如何优化这个数据库查询的性能？"
"这个算法的时间复杂度是多少？"
"如何重构这个方法以提高可读性？"
```

**避免的问题**:
```
"这是什么？"  // 太模糊
"帮我写代码"  // 缺乏具体需求
```

### 2. 代码选择技巧

- **选择完整的逻辑块**: 包含完整的方法或类
- **包含上下文**: 选择相关的导入语句和注释
- **避免选择片段**: 不要只选择部分代码行

### 3. 结果应用技巧

- **仔细审查**: 不要盲目应用 AI 建议
- **测试验证**: 应用后进行充分测试
- **逐步应用**: 对于大的重构，分步骤进行

### 4. 性能优化技巧

- **批量操作**: 一次处理多个相关问题
- **缓存结果**: 相似的问题可以参考之前的答案
- **合理使用**: 避免过度依赖，保持自己的思考

## 🔍 实际应用场景

### 场景 1: 学习新框架

**情况**: 需要学习 Spring Boot

**步骤**:
1. 打开聊天窗口
2. 询问: "如何创建一个 Spring Boot REST API？"
3. 根据回答创建基础代码
4. 选中生成的代码，使用 "解释代码" 深入理解

### 场景 2: 代码审查

**情况**: 需要审查同事的代码

**步骤**:
1. 选中要审查的方法
2. 使用 "优化代码" 功能
3. 分析 AI 提供的改进建议
4. 在代码审查中提出具体的改进意见

### 场景 3: 调试复杂问题

**情况**: 遇到难以理解的 bug

**步骤**:
1. 选中相关的错误代码
2. 使用 "修复代码" 功能
3. 描述具体的错误现象
4. 根据 AI 建议逐步排查问题

### 场景 4: 重构遗留代码

**情况**: 需要重构老旧的代码

**步骤**:
1. 选中需要重构的类或方法
2. 使用 "解释代码" 理解现有逻辑
3. 使用 "优化代码" 获得重构建议
4. 分步骤应用改进方案

## 🚨 注意事项

### 安全考虑

- **敏感信息**: 不要在代码中包含密码、API 密钥等敏感信息
- **商业机密**: 谨慎处理包含商业逻辑的核心代码
- **数据隐私**: 避免上传包含个人信息的代码

### 准确性考虑

- **验证建议**: AI 建议可能不完全准确，需要人工验证
- **测试重要**: 应用 AI 建议后务必进行充分测试
- **保持判断**: 保持独立思考，不要完全依赖 AI

### 使用限制

- **API 配额**: 注意 API 使用限制，避免过度调用
- **网络依赖**: 需要稳定的网络连接
- **响应时间**: AI 响应可能需要几秒钟时间

## 📊 快捷键参考

| 功能 | 快捷键 | 说明 |
|------|--------|------|
| 解释代码 | `Ctrl+Shift+E` | 解释选中的代码 |
| 优化代码 | `Ctrl+Shift+O` | 优化选中的代码 |
| 生成代码 | `Ctrl+Shift+G` | 根据描述生成代码 |
| 修复代码 | `Ctrl+Shift+F` | 修复代码问题 |
| 打开聊天 | `Ctrl+Shift+C` | 打开 AI 聊天窗口 |
| 意图动作 | `Alt+Enter` | 显示智能建议 |

## 🔧 自定义配置

### 修改快捷键

1. 进入 `File` → `Settings` → `Keymap`
2. 搜索 "Cursor AI"
3. 双击要修改的动作
4. 设置新的快捷键组合

### 调整 AI 行为

虽然插件本身不提供 AI 参数配置，但您可以通过提问方式影响 AI 行为：

```
"请用简洁的方式解释这段代码"
"请详细分析这个算法的每个步骤"
"请只提供代码，不要解释"
```

## 🆘 常见问题解答

**Q: AI 回复速度很慢怎么办？**
A: 检查网络连接，尝试简化问题，或在网络较好的时候使用。

**Q: AI 建议不准确怎么办？**
A: 提供更多上下文信息，使用更具体的描述，并始终验证 AI 建议。

**Q: 如何提高 AI 回复质量？**
A: 提供清晰的问题描述，包含足够的代码上下文，使用具体的技术术语。

**Q: 可以离线使用吗？**
A: 不可以，插件需要网络连接来访问 Cursor AI API。

---

希望这个指南能帮助您更好地使用 Cursor AI Assistant！如有其他问题，请查看 [FAQ](FAQ.md) 或联系支持团队。🚀