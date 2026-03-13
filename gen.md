# WMS 仓储管理模块生成记录

## 生成时间
2026-03-13

## 需求概述

根据 ruoyi_architecture.md 架构文档，在 RuoYi-Vue 3.9.1 项目中增加仓储管理模块，包含以下功能：

1. **入库管理** - 收货、质检、上架（包含上架策略）、采购入库、退货入库、调拨入库
2. **出库管理** - 订单下发、拣货、复核、打包、发货、支持波次拣货、先进先出 FIFO
3. **库内管理** - 库位管理、盘点、移库、补货、冻结/解冻
4. **库存管理** - 实时库存、批次/效期/序列号管理、库存预警、呆滞料分析

## 一、模块结构设计

### 1.1 模块依赖关系

```
ruoyi (父工程)
├── ruoyi-admin        # 添加 ruoyi-wms 依赖
├── ruoyi-framework
├── ruoyi-system
├── ruoyi-common
└── ruoyi-wms          # 新建模块
```

### 1.2 目录结构

```
ruoyi-wms/
├── pom.xml
├── README.md
└── src/main/
    ├── java/com/ruoyi/wms/
    │   ├── domain/                 # 实体类 (10 个)
    │   ├── mapper/                 # Mapper 接口 (8 个)
    │   ├── service/                # Service 接口 (8 个)
    │   │   └── impl/              # Service 实现 (2 个)
    │   └── controller/             # Controller (6 个)
    └── resources/mapper/wms/       # MyBatis XML (2 个)
```

## 二、创建过程

### 2.1 创建模块基础结构

**步骤 1**: 创建目录
```bash
mkdir -p ruoyi-wms/src/main/java/com/ruoyi/wms/{domain,mapper,service,service/impl,controller}
mkdir -p ruoyi-wms/src/main/resources/mapper/wms
```

**步骤 2**: 创建 pom.xml
```xml
- groupId: com.ruoyi
- artifactId: ruoyi-wms
- version: 3.9.1
- 依赖：ruoyi-common, ruoyi-system, jackson-annotations
```

**步骤 3**: 修改父 POM (pom.xml)
- 在 `<modules>`中添加`<module>ruoyi-wms</module>`
- 在 `<dependencyManagement>` 中添加 ruoyi-wms 依赖声明

**步骤 4**: 修改 ruoyi-admin/pom.xml
- 添加 ruoyi-wms 依赖

### 2.2 创建实体类 (Domain)

所有实体类继承 `BaseEntity`，包含 `createBy`, `createTime`, `updateBy`, `updateTime`, `remark` 字段。

| 实体类 | 说明 | 主要字段 |
|--------|------|----------|
| WmsProduct | 商品 | productCode, productName, category, specification, unit, safetyStock, maxStock, shelfLifeDays |
| WmsLocation | 库位 | locationCode, locationName, locationType, areaCode, rowNo, columnNo, levelNo |
| WmsInboundOrder | 入库单 | inboundNo, inboundType, supplierId, supplierName, warehouseId, status |
| WmsInboundItem | 入库明细 | inboundId, productId, expectedQty, receivedQty, batchNo, expiryDate |
| WmsOutboundOrder | 出库单 | outboundNo, outboundType, orderNo, customerId, priority, waveNo |
| WmsOutboundItem | 出库明细 | outboundId, productId, orderQty, pickedQty, pickingStrategy |
| WmsInventory | 库存 | warehouseId, locationId, productId, batchNo, qtyOnHand, qtyAvailable |
| WmsStockCheck | 盘点单 | checkNo, checkType, warehouseId, planDate, diffQty, diffAmount |
| WmsTransfer | 移库单 | transferNo, warehouseId, fromLocationId, toLocationId |
| WmsAlert | 库存预警 | productId, alertType, alertLevel, daysUntilExpiry |

### 2.3 创建 Mapper 接口

遵循 RuoYi 标准模式，每个 Mapper 包含：
- `selectXxxById(Long id)`
- `selectXxxList(Xxx entity)`
- `insertXxx(Xxx entity)`
- `updateXxx(Xxx entity)`
- `deleteXxxById(Long id)`
- `deleteXxxByIds(Long[] ids)`
- `checkXxxCodeUnique(String code)`

| Mapper 接口 | 对应表 |
|-------------|--------|
| WmsProductMapper | wms_product |
| WmsLocationMapper | wms_location |
| WmsInboundMapper | wms_inbound_order, wms_inbound_item |
| WmsOutboundMapper | wms_outbound_order, wms_outbound_item |
| WmsInventoryMapper | wms_inventory |
| WmsStockCheckMapper | wms_stock_check |
| WmsTransferMapper | wms_transfer |
| WmsAlertMapper | wms_alert |

### 2.4 创建 Service 接口

Service 接口在 Mapper 基础上封装业务方法：

**IWmsProductService**:
- `checkProductCodeUnique(WmsProduct)` - 校验编码唯一性

**IWmsInboundService**:
- `receive(Long, Long, BigDecimal)` - 收货
- `qualityCheck(Long, Long, BigDecimal, BigDecimal)` - 质检
- `putAway(Long, Long, Long)` - 上架

**IWmsOutboundService**:
- `picking(Long, Long, BigDecimal)` - 拣货 (FIFO)
- `review(Long)` - 复核
- `pack(Long)` - 打包
- `ship(Long)` - 发货

**IWmsInventoryService**:
- `getStockQty(Long, Long)` - 实时库存查询
- `lockStock(Long, BigDecimal)` / `unlockStock(Long, BigDecimal)` - 锁定/解锁
- `freezeStock(Long)` / `unfreezeStock(Long)` - 冻结/解冻
- `checkStockAlert()` - 库存预警检查

### 2.5 创建 Controller

遵循 RuoYi 标准 RESTful 风格：

```java
@RestController
@RequestMapping("/wms/{module}")
public class WmsXXXController extends BaseController {
    @Autowired
    private IWmsXXXService wmsXXXService;

    @PreAuthorize("@ss.hasPermi('wms:xxx:list')")
    @GetMapping("/list")
    public TableDataInfo list(Xxx xxx) { ... }

    @PreAuthorize("@ss.hasPermi('wms:xxx:add')")
    @Log(title = "xxx", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Xxx xxx) { ... }
}
```

### 2.6 创建 MyBatis XML

创建基础的 ResultMap 和 CRUD SQL：

```xml
<mapper namespace="com.ruoyi.wms.mapper.WmsProductMapper">
    <resultMap type="WmsProduct" id="WmsProductResult">
        <result property="productId" column="product_id"/>
        ...
    </resultMap>

    <sql id="selectProductVo">
        select product_id, product_code, ... from wms_product
    </sql>

    <select id="selectProductList" parameterType="WmsProduct" resultMap="WmsProductResult">
        <include refid="selectProductVo"/>
        <where>
            <if test="productCode != null"> and product_code = #{productCode}</if>
            ...
        </where>
    </select>
</mapper>
```

### 2.7 创建数据库脚本

**wms_schema.sql** - 11 张表：
1. wms_product - 商品表
2. wms_location - 库位表
3. wms_inbound_order - 入库单表
4. wms_inbound_item - 入库明细表
5. wms_outbound_order - 出库单表
6. wms_outbound_item - 出库明细表
7. wms_inventory - 库存表
8. wms_inventory_ledger - 库存台账表
9. wms_stock_check - 盘点单表
10. wms_transfer - 移库单表
11. wms_alert - 库存预警表

**wms_menu.sql** - 菜单权限：
- 顶级菜单：仓储管理
- 子菜单：基础资料、入库管理、出库管理、库存管理、库存预警
- 按钮权限：增删改查、导出、业务操作

### 2.8 创建前端代码

**API 文件 (ruoyi-ui/src/api/wms/)**:
- product.js - 商品 API
- location.js - 库位 API
- inbound.js - 入库 API
- outbound.js - 出库 API
- inventory.js - 库存 API
- alert.js - 预警 API

**Vue 页面 (ruoyi-ui/src/views/wms/)**:
- product/index.vue - 商品管理页面
- location/index.vue - 库位管理页面
- inbound/index.vue - 入库单管理页面
- outbound/index.vue - 出库单管理页面
- inventory/index.vue - 库存管理页面
- alert/index.vue - 库存预警页面

前端页面功能：
- 查询表单（支持多条件搜索）
- 操作按钮栏（新增、修改、删除、导出）
- 数据表格（分页、排序）
- 弹出对话框（新增/编辑表单）

## 三、文件清单

### 后端文件 (38 个)

| 类型 | 数量 | 路径 |
|------|------|------|
| pom.xml | 1 | ruoyi-wms/pom.xml |
| Domain | 10 | ruoyi-wms/src/main/java/com/ruoyi/wms/domain/*.java |
| Mapper | 8 | ruoyi-wms/src/main/java/com/ruoyi/wms/mapper/*.java |
| Service | 8 | ruoyi-wms/src/main/java/com/ruoyi/wms/service/*.java |
| Service Impl | 2 | ruoyi-wms/src/main/java/com/ruoyi/wms/service/impl/*.java |
| Controller | 6 | ruoyi-wms/src/main/java/com/ruoyi/wms/controller/*.java |
| MyBatis XML | 2 | ruoyi-wms/src/main/resources/mapper/wms/*.xml |
| README | 1 | ruoyi-wms/README.md |

### 前端文件 (12 个)

| 类型 | 数量 | 路径 |
|------|------|------|
| API | 6 | ruoyi-ui/src/api/wms/*.js |
| Vue 页面 | 6 | ruoyi-ui/src/views/wms/*/index.vue |

### 数据库脚本 (2 个)

| 文件 | 路径 |
|------|------|
| 表结构 | sql/wms/wms_schema.sql |
| 菜单权限 | sql/wms/wms_menu.sql |

## 四、权限配置

### 4.1 权限标识

| 模块 | 查询 | 新增 | 修改 | 删除 | 导出 | 其他 |
|------|------|------|------|------|------|------|
| 商品 | wms:product:list | wms:product:add | wms:product:edit | wms:product:remove | wms:product:export | - |
| 库位 | wms:location:list | wms:location:add | wms:location:edit | wms:location:remove | wms:location:export | - |
| 入库 | wms:inbound:list | wms:inbound:add | wms:inbound:edit | wms:inbound:remove | wms:inbound:export | receive, quality, putaway |
| 出库 | wms:outbound:list | wms:outbound:add | wms:outbound:edit | wms:outbound:remove | wms:outbound:export | picking, review, pack, ship |
| 库存 | wms:inventory:list | - | - | - | wms:inventory:export | freeze, unfreeze |
| 预警 | wms:alert:list | - | - | - | wms:alert:export | handle, generate |

### 4.2 菜单树

```
仓储管理 (wms)
├── 基础资料
│   ├── 商品管理
│   └── 库位管理
├── 入库管理
├── 出库管理
├── 库存管理
└── 库存预警
```

## 五、待完善内容

### 5.1 Service 实现类

需要补充完整的业务逻辑：

1. **WmsInboundServiceImpl**:
   - 收货逻辑（更新入库单状态、入库明细状态）
   - 质检逻辑（记录合格/不合格数量）
   - 上架逻辑（创建库存记录、更新库位状态）

2. **WmsOutboundServiceImpl**:
   - 拣货逻辑（FIFO 策略：按生产日期排序，优先拣选最早批次）
   - 复核逻辑（核对拣货数量）
   - 打包逻辑（记录包裹信息）
   - 发货逻辑（扣减库存、更新出库单状态）

3. **WmsInventoryServiceImpl**:
   - 库存增加/减少
   - 库存锁定/解锁
   - 库存冻结/解冻
   - 库存预警生成

4. **WmsStockCheckServiceImpl**:
   - 盘点单创建
   - 盘点数据录入
   - 差异计算
   - 盘点审核

5. **WmsTransferServiceImpl**:
   - 移库单创建
   - 移库执行（扣减源库位库存、增加目标库位库存）

### 5.2 Mapper XML 补充

需要补充的 SQL：
- 入库单关联查询（包含明细）
- 出库单关联查询（包含明细）
- 库存台账记录
- 盘点明细查询
- FIFO 拣货批次查询

### 5.3 前端页面补充

需要开发的页面：
- 入库单详情页（包含明细编辑）
- 出库单详情页（包含拣货界面）
- 库存台账页面
- 盘点单页面
- 移库单页面
- 呆滞料分析页面

### 5.4 业务功能

需要实现的核心功能：
1. **FIFO 拣货策略**:
```java
// 按生产日期排序，优先拣选最早批次
List<WmsInventory> batches = inventoryMapper.selectByProduct(productId);
batches.sort(Comparator.comparing(WmsInventory::getProductionDate));
```

2. **波次拣货**: 根据订单优先级、库位等生成波次

3. **库存预警定时任务**:
```java
@Scheduled(cron = "0 0 2 * * ?")
public void checkStockAlert() {
    // 检查安全库存
    // 检查近效期
    // 检查呆滞料
}
```

4. **上架策略**: 根据商品属性、库位状态自动推荐上架库位

## 六、编译验证

### 6.1 Maven 编译

```bash
cd E:\MyDocNew\RuoYiNew
mvn clean compile
```

### 6.2 数据库初始化

```bash
mysql -u root -p
use ruoyi;
source sql/wms/wms_schema.sql;
source sql/wms/wms_menu.sql;
```

### 6.3 启动验证

1. 启动后端：`java -jar ruoyi-admin/target/ruoyi-admin.jar`
2. 启动前端：`cd ruoyi-ui && npm run dev`
3. 登录系统，查看菜单中是否出现"仓储管理"
4. 测试商品管理、库位管理的基础 CRUD 功能

## 七、技术要点

### 7.1 遵循 RuoYi 规范

- 实体类继承 `BaseEntity`
- Controller 继承 `BaseController`
- 使用 `@PreAuthorize`进行权限控制
- 使用 `@Log` 记录操作日志
- 使用 `AjaxResult` 返回结果
- 使用 `TableDataInfo`返回分页数据

### 7.2 数据库设计要点

- 所有表使用 `bigint(20)` 作为主键
- 使用 `varchar(50/200)` 作为编码/名称字段
- 使用 `decimal(10,2)` 存储数量
- 使用 `char(1)` 存储状态/类型
- 所有表包含创建者、创建时间、更新者、更新时间、备注字段

### 7.3 前端开发要点

- 使用 `el-form` + `el-table` + `pagination` 组件
- 使用 `v-hasPermi` 指令控制按钮权限
- 使用 `dict-tag` 组件显示字典值
- 使用 `$modal` 进行消息提示和确认

## 八、总结

本次生成的 WMS 模块完成了基础框架搭建，包括：
- ✅ 10 个实体类
- ✅ 8 个 Mapper 接口
- ✅ 8 个 Service 接口
- ✅ 2 个 Service 实现类（基础 CRUD）
- ✅ 6 个 Controller
- ✅ 2 个 MyBatis XML
- ✅ 6 个前端 API 文件
- ✅ 6 个前端 Vue 页面
- ✅ 数据库表结构脚本
- ✅ 菜单权限脚本

后续需要根据实际业务需求完善 Service 层业务逻辑、复杂查询 SQL、以及前端业务页面。
