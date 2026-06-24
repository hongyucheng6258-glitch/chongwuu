# 🐾 初晴萌宠空间 - 宠物商城系统

一个基于前后端分离架构的综合性宠物商城平台，涵盖商品管理、购物车、订单系统、AI 智能助手、宠物视频、附近商店等功能模块。

## 📌 项目简介

初晴萌宠空间是一个功能完善的宠物电商平台，为宠物爱好者提供一站式购物体验。系统包含用户端、管理后台和帮助中心三大模块，支持商品浏览与搜索、在线购物、订单管理、AI 宠物咨询、宠物视频观看等功能。

## 🛠️ 技术栈

### 后端

| 技术 | 说明 |
|------|------|
| Java 17 | 编程语言 |
| Spring Boot 3.2.5 | 后端框架 |
| Spring Data JPA | ORM 数据持久化 |
| SQLite | 主数据库（文件数据库） |
| MongoDB | 地理位置数据存储 |
| Redis | 缓存服务 |
| JWT (jjwt) | 用户认证与鉴权 |
| 智谱 AI (GLM-4-Flash) | AI 智能助手 |
| Lombok | 简化 Java 代码 |

### 前端

| 技术 | 说明 |
|------|------|
| Vue 3 | 前端框架（Composition API） |
| Vue Router 4 | 前端路由 |
| Pinia | 状态管理 |
| Element Plus | UI 组件库 |
| Axios | HTTP 请求 |
| Vite 5 | 构建工具 |

## 📁 项目结构

```
宠物商城/
├── backend/                          # 后端项目（Spring Boot）
│   ├── src/main/java/com/petshop/
│   │   ├── controller/              # 控制器层（14个接口）
│   │   ├── service/                  # 业务逻辑层（17个服务）
│   │   ├── entity/                   # JPA 实体（11个）
│   │   ├── repository/               # 数据访问层
│   │   ├── dto/                      # 数据传输对象
│   │   ├── config/                   # 配置类
│   │   ├── security/                 # JWT 安全模块
│   │   ├── common/                   # 统一响应与异常处理
│   │   ├── mongo/                    # MongoDB 地理位置
│   │   └── util/                     # 工具类
│   ├── src/main/resources/
│   │   └── application.yml          # 应用配置
│   ├── petshop.db                    # SQLite 数据库文件
│   └── pom.xml                       # Maven 依赖配置
│
├── frontend/                         # 前端项目（Vue 3）
│   ├── src/
│   │   ├── views/                    # 页面组件
│   │   │   ├── Home.vue              # 首页
│   │   │   ├── Login.vue             # 登录
│   │   │   ├── Register.vue          # 注册
│   │   │   ├── ProductDetail.vue     # 商品详情
│   │   │   ├── Cart.vue              # 购物车
│   │   │   ├── Checkout.vue          # 结算
│   │   │   ├── Orders.vue            # 我的订单
│   │   │   ├── Profile.vue           # 个人中心
│   │   │   ├── AiChat.vue            # AI 助手
│   │   │   ├── Videos.vue            # 宠物视频
│   │   │   ├── Stores.vue           # 附近商店
│   │   │   ├── help/                 # 帮助中心（9个页面）
│   │   │   └── admin/                # 管理后台（9个页面）
│   │   ├── router/index.js           # 路由配置
│   │   ├── api/index.js              # API 接口
│   │   ├── store/user.js             # 用户状态管理
│   │   └── utils/request.js          # Axios 封装
│   ├── public/videos/                # 视频资源
│   └── package.json                  # npm 依赖配置
```

## ✨ 核心功能

### 用户端
- **用户系统**：注册/登录、Gitee 第三方登录、JWT 认证、个人中心、收货地址管理
- **商品系统**：商品浏览、分类筛选、商品详情、搜索
- **购物车 & 订单**：购物车管理、下单结算、订单列表/详情、退换货/退款
- **商店系统**：附近商店浏览、商店详情（基于 MongoDB 地理位置）
- **视频系统**：宠物视频浏览与播放
- **AI 助手**：基于智谱 GLM-4-Flash 的智能宠物咨询
- **消息通知**：用户消息中心
- **评价系统**：商品评价

### 管理后台
- **数据仪表盘**：销售统计、用户统计等数据概览
- **商品管理**：商品 CRUD、分类管理
- **订单管理**：订单查看与处理
- **商店管理**：商店信息维护
- **视频管理**：视频内容管理
- **会员管理**：用户信息管理
- **评价管理**：评价审核与管理

### 帮助中心
- 购物流程、会员介绍、常见问题、配送方式、运费说明、退换货政策、退款指南、维修退货

## 🚀 快速开始

### 环境要求

- JDK 17+
- Node.js 16+
- MongoDB（可选，用于地理位置功能）
- Redis（可选，用于缓存功能）

### 后端启动

```bash
cd backend
# 数据库文件 petshop.db 已包含在项目中，无需额外配置
mvn spring-boot:run
# 后端服务运行在 http://localhost:8080
```

### 前端启动

```bash
cd frontend
npm install
npm run dev
# 前端服务运行在 http://localhost:5173
```

## 📋 配置说明

后端配置文件为 `backend/src/main/resources/application.yml`：

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| 服务端口 | 8080 | Spring Boot 启动端口 |
| 数据库 | petshop.db | SQLite 文件数据库 |
| MongoDB | localhost:27017/petshop | 地理位置数据 |
| Redis | localhost:6379 | 缓存服务 |
| JWT 过期时间 | 24 小时 | Token 有效期 |

## 🏗️ 架构特点

- **前后端分离**：前端 Vite + Vue3，后端 Spring Boot，通过 Vite 代理解决跨域
- **多数据源**：SQLite（主业务）+ MongoDB（地理位置）+ Redis（缓存）
- **安全认证**：JWT Token + 拦截器鉴权，路由守卫区分用户和管理员
- **轻量部署**：SQLite 文件数据库无需额外安装数据库服务，适合实训和演示场景
