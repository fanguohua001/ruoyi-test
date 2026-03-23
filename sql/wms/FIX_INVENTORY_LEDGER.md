# 库存台账问题修复总结

## 问题描述
出入库操作没有更新库存台账表

## 问题原因
数据库表 `wms_inventory_ledger` 缺少以下字段：
- `product_code` - 物料编码
- `product_name` - 物料名称
- `reference_no` - 关联单据号

而实体类 `WmsInventoryLedger.java` 和 Mapper XML 中已经定义了这些字段，导致插入数据时字段不匹配。

## 解决方案

### 1. 执行数据库表结构变更
```sql
ALTER TABLE wms_inventory_ledger ADD COLUMN product_code VARCHAR(50) AFTER product_id;
ALTER TABLE wms_inventory_ledger ADD COLUMN product_name VARCHAR(100) AFTER product_code;
ALTER TABLE wms_inventory_ledger ADD COLUMN reference_no VARCHAR(50) AFTER reference_id;
```

### 2. 已创建的 SQL 脚本
- `sql/wms/wms_inventory_ledger_add_columns.sql` - 表结构变更脚本

## 验证方法

### 方法一：查看数据库表结构
```bash
mysql -h localhost -u root -p ry-vue -e "DESC wms_inventory_ledger;"
```

### 方法二：测试入库单功能
1. 创建一个新的入库单
2. 点击"完成"按钮
3. 查询 `wms_inventory_ledger` 表，确认新增了一条入库履历记录

### 方法三：测试出库单功能
1. 创建一个新的出库单
2. 点击"完成"按钮
3. 查询 `wms_inventory_ledger` 表，确认新增了一条出库履历记录

## 代码逻辑说明

### 入库单完成时（finishInboundOrder）
1. 遍历入库单明细
2. 调用 `inventoryService.increaseStockWithLedger()` 增加库存
3. 同时记录库存履历，交易类型为"1"（入库）

### 出库单完成时（finishOutboundOrder）
1. 遍历出库单明细
2. 调用 `inventoryService.decreaseStockWithLedger()` 减少库存
3. 同时记录库存履历，交易类型为"2"（出库）

### 出库单发货时（ship）
1. 遍历出库明细
2. 调用 `inventoryService.decreaseStockWithLedger()` 减少库存
3. 同时记录库存履历，交易类型为"2"（出库）

## 相关文件

### Java 代码
- `ruoyi-wms/src/main/java/com/ruoyi/wms/service/impl/WmsInboundServiceImpl.java` - 入库单 Service
- `ruoyi-wms/src/main/java/com/ruoyi/wms/service/impl/WmsOutboundServiceImpl.java` - 出库单 Service
- `ruoyi-wms/src/main/java/com/ruoyi/wms/service/impl/WmsInventoryServiceImpl.java` - 库存 Service
- `ruoyi-wms/src/main/java/com/ruoyi/wms/service/impl/WmsInventoryLedgerServiceImpl.java` - 库存台账 Service

### SQL 脚本
- `sql/wms/wms_schema.sql` - 完整的表结构定义（包含所有字段）
- `sql/wms/wms_inventory_ledger_add_columns.sql` - 增量变更脚本

## 注意事项

1. **生产环境执行顺序**：
   - 先备份数据
   - 执行 `wms_inventory_ledger_add_columns.sql`
   - 重启应用

2. **数据一致性**：
   - 库存台账的 `qty_before` 和 `qty_after` 依赖于当前库存记录
   - 如果库存记录不存在，`qty_before` 为 0

3. **Redis 序列号**：
   - 库存台账的交易编号使用 Redis 序列号生成器
   - 如果遇到 Redis 序列号错误，请参考 `README_REDIS_CLEANUP.md` 进行清理

## 修复时间
2026-03-18
