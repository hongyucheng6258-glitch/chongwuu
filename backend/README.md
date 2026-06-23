# 宠物商城后端 (Spring Boot + SQLite)

## 运行方式

### 方式一: 使用内置脚本 (推荐, 自动下载 Maven)

**Windows:**
```bash
run.cmd spring-boot:run
```

**Git Bash / Linux / macOS:**
```bash
./run.sh spring-boot:run
```

### 方式二: 使用本地 Maven

```bash
mvn spring-boot:run
```

启动后服务地址: http://localhost:8080

## 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 普通用户 | user | user123 |

## 技术栈

- Spring Boot 3.2.5
- Spring Data JPA + SQLite
- JWT 鉴权
- Lombok
