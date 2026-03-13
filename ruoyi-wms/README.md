# WMS 仓储管理模块使用说明

## 一、模块概述

WMS（Warehouse Management System）仓储管理模块是 RuoYi-Vue 系统的扩展模块，提供完整的仓储管理功能，包括：

- **入库管理** - 收货、质检、上架（包含上架策略）、采购入库、退货入库、调拨入库
- **出库管理** - 订单下发、拣货、复核、打包、发货、支持波次拣货、先进先出 FIFO
- **库内管理** - 库位管理、盘点、移库、补货、冻结/解冻
- **库存管理** - 实时库存、批次/效期/序列号管理、库存预警、呆滞料分析

## 二、快速开始

### 2.1 数据库初始化

1. 执行数据库脚本创建表结构：
```bash
mysql -u root -p ruoyi < sql/wms/wms_schema.sql
```

2. 执行菜单权限脚本（可选）：
```bash
mysql -u root -p ruoyi < sql/wms/wms_menu.sql
```

### 2.2 模块编译

WMS 模块已添加到主 POM 中，直接编译整个项目即可：

```bash
mvn clean package
```

### 2.3 启动项目

```bash
# 后端启动
java -jar ruoyi-admin/target/ruoyi-admin.jar

# 前端启动
cd ruoyi-ui
npm install
npm run dev
```

## 三、目录结构

```
ruoyi-wms/
├── pom.xml                                    # Maven 配置
└── src/main/
    ├── java/com/ruoyi/wms/
    │   ├── domain/                            # 实体类
    │   │   ├── WmsProduct.java               # 商品实体
    │   │   ├── WmsLocation.java              # 库位实体
    │   │   ├── WmsInboundOrder.java          # 入库单实体
    │   │   ├── WmsInboundItem.java           # 入库明细实体
    │   │   ├── WmsOutboundOrder.java         # 出库单实体
    │   │   ├── WmsOutboundItem.java          # 出库明细实体
    │   │   ├── WmsInventory.java             # 库存实体
    │   │   ├── WmsStockCheck.java            # 盘点单实体
    │   │   ├── WmsTransfer.java              # 移库单实体
    │   │   └── WmsAlert.java                 # 库存预警实体
    │   ├── mapper/                            # Mapper 接口
    │   ├── service/                           # Service 接口
    │   │   └── impl/                         # Service 实现
    │   └── controller/                        # Controller
    └── resources/mapper/wms/                  # MyBatis XML
        ├── WmsProductMapper.xml
        ├── WmsLocationMapper.xml
        └── ...
```

## 四、API 接口说明

### 4.1 商品管理

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| /wms/product/list | GET | 查询商品列表 | wms:product:list |
| /wms/product/{id} | GET | 查询商品详情 | wms:product:query |
| /wms/product | POST | 新增商品 | wms:product:add |
| /wms/product | PUT | 修改商品 | wms:product:edit |
| /wms/product/{ids} | DELETE | 删除商品 | wms:product:remove |
| /wms/product/export | POST | 导出商品 | wms:product:export |

### 4.2 库位管理

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| /wms/location/list | GET | 查询库位列表 | wms:location:list |
| /wms/location/{id} | GET | 查询库位详情 | wms:location:query |
| /wms/location | POST | 新增库位 | wms:location:add |
| /wms/location | PUT | 修改库位 | wms:location:edit |
| /wms/location/{ids} | DELETE | 删除库位 | wms:location:remove |
| /wms/location/export | POST | 导出库位 | wms:location:export |

### 4.3 入库管理

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| /wms/inbound/list | GET | 查询入库单列表 | wms:inbound:list |
| /wms/inbound/{id} | GET | 查询入库单详情 | wms:inbound:query |
| /wms/inbound | POST | 新增入库单 | wms:inbound:add |
| /wms/inbound | PUT | 修改入库单 | wms:inbound:edit |
| /wms/inbound/{ids} | DELETE | 删除入库单 | wms:inbound:remove |
| /wms/inbound/receive | PUT | 收货 | wms:inbound:receive |
| /wms/inbound/quality | PUT | 质检 | wms:inbound:quality |
| /wms/inbound/putaway | PUT | 上架 | wms:inbound:putaway |

### 4.4 出库管理

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| /wms/outbound/list | GET | 查询出库单列表 | wms:outbound:list |
| /wms/outbound/{id} | GET | 查询出库单详情 | wms:outbound:query |
| /wms/outbound | POST | 新增出库单 | wms:outbound:add |
| /wms/outbound | PUT | 修改出库单 | wms:outbound:edit |
| /wms/outbound/{ids} | DELETE | 删除出库单 | wms:outbound:remove |
| /wms/outbound/picking | PUT | 拣货 (FIFO) | wms:outbound:picking |
| /wms/outbound/review | PUT | 复核 | wms:outbound:review |
| /wms/outbound/pack | PUT | 打包 | wms:outbound:pack |
| /wms/outbound/ship | PUT | 发货 | wms:outbound:ship |

### 4.5 库存管理

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| /wms/inventory/list | GET | 查询库存列表 | wms:inventory:list |
| /wms/inventory/{id} | GET | 查询库存详情 | wms:inventory:query |
| /wms/inventory/qty | GET | 查询实时库存 | - |
| /wms/inventory/freeze | PUT | 冻结库存 | wms:inventory:freeze |
| /wms/inventory/unfreeze | PUT | 解冻库存 | wms:inventory:unfreeze |
| /wms/inventory/export | POST | 导出库存 | wms:inventory:export |

### 4.6 库存预警

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| /wms/alert/list | GET | 查询预警列表 | wms:alert:list |
| /wms/alert/{id} | GET | 查询预警详情 | wms:alert:query |
| /wms/alert/handle | PUT | 处理预警 | wms:alert:handle |
| /wms/alert/generate | POST | 生成预警 | wms:alert:generate |
| /wms/alert/export | POST | 导出预警 | wms:alert:export |

## 五、数据字典

### 5.1 入库类型 (inbound_type)

| 值 | 说明 |
|----|------|
| 1 | 采购入库 |
| 2 | 退货入库 |
| 3 | 调拨入库 |

### 5.2 出库类型 (outbound_type)

| 值 | 说明 |
|----|------|
| 1 | 销售出库 |
| 2 | 采购退货 |
| 3 | 调拨出库 |
| 4 | 其他出库 |

### 5.3 库存状态 (status)

| 值 | 说明 |
|----|------|
| 1 | 正常 |
| 2 | 锁定 |
| 3 | 冻结 |

### 5.4 预警类型 (alert_type)

| 值 | 说明 |
|----|------|
| 1 | 低于安全库存 |
| 2 | 高于最高库存 |
| 3 | 近效期 |
| 4 | 已过期 |
| 5 | 呆滞 |

## 六、业务流程

### 6.1 入库流程

```
创建入库单 → 收货 → 质检 → 上架 → 完成
```

### 6.2 出库流程

```
创建出库单 → 拣货 (FIFO) → 复核 → 打包 → 发货 → 完成
```

### 6.3 盘点流程

```
创建盘点单 → 开始盘点 → 录入实盘数量 → 生成差异 → 审核 → 完成
```

## 七、注意事项

1. **数据库配置**: 确保 MySQL 数据库已启动并创建了对应的表
2. **权限配置**: 需要在系统中为角色分配 WMS 相关权限
3. **序列号生成**: 入库单号、出库单号等需要在 Service 层添加生成规则
4. **FIFO 策略**: 拣货时按照生产日期/有效期自动选择最早批次
5. **库存预警**: 建议配置定时任务定期检查库存状态

## 八、后续扩展

以下功能需要根据实际业务需求进一步完善：

1. **Service 实现类**: 目前仅创建了基础的 CRUD，复杂的业务流程需要完善
2. **Mapper XML**: 部分复杂查询 SQL 需要补充
3. **前端页面**: 入库明细、出库明细、盘点等页面需要开发
4. **波次拣货**: 需要实现波次生成和分配逻辑
5. **报表统计**: 库存周转率、出入库统计等报表

## 九、技术支持

如有问题，请参考 RuoYi 官方文档或联系开发团队。
