<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="出库单号" prop="outboundNo">
        <el-input
          v-model="queryParams.outboundNo"
          placeholder="请输入出库单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="出库类型" prop="outboundType">
        <el-select v-model="queryParams.outboundType" placeholder="出库类型" clearable>
          <el-option label="销售出库" value="1" />
          <el-option label="采购退货" value="2" />
          <el-option label="调拨出库" value="3" />
          <el-option label="其他出库" value="4" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="状态" clearable>
          <el-option label="无" value="0" />
          <el-option label="待审核" value="1" />
          <el-option label="待拣货" value="2" />
          <el-option label="拣货中" value="3" />
          <el-option label="待复核" value="4" />
          <el-option label="待打包" value="5" />
          <el-option label="待发货" value="6" />
          <el-option label="已发货" value="7" />
          <el-option label="已取消" value="8" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['wms:outbound:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['wms:outbound:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['wms:outbound:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['wms:outbound:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="outboundList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="出库单号" align="center" prop="outboundNo" />
      <el-table-column label="出库类型" align="center" prop="outboundType">
        <template slot-scope="scope">
          <span v-if="scope.row.outboundType == '1'">销售出库</span>
          <span v-else-if="scope.row.outboundType == '2'">采购退货</span>
          <span v-else-if="scope.row.outboundType == '3'">调拨出库</span>
          <span v-else-if="scope.row.outboundType == '4'">其他出库</span>
        </template>
      </el-table-column>
      <el-table-column label="订单编号" align="center" prop="orderNo" />
      <el-table-column label="客户名称" align="center" prop="customerName" />
      <el-table-column label="总数量" align="center" prop="totalQty" />
      <el-table-column label="已拣货数量" align="center" prop="pickedQty" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <span v-if="scope.row.isFinished === 1" style="color: green;">已完成</span>
          <span v-else-if="scope.row.status == '0'">无</span>
          <span v-else-if="scope.row.status == '1'">待审核</span>
          <span v-else-if="scope.row.status == '2'">待拣货</span>
          <span v-else-if="scope.row.status == '3'">拣货中</span>
          <span v-else-if="scope.row.status == '4'">待复核</span>
          <span v-else-if="scope.row.status == '5'">待打包</span>
          <span v-else-if="scope.row.status == '6'">待发货</span>
          <span v-else-if="scope.row.status == '7'">已发货</span>
          <span v-else-if="scope.row.status == '8'">已取消</span>
        </template>
      </el-table-column>
      <el-table-column label="优先级" align="center" prop="priority">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.priority == '1'" type="info">普通</el-tag>
          <el-tag v-else-if="scope.row.priority == '2'" type="warning">加急</el-tag>
          <el-tag v-else-if="scope.row.priority == '3'" type="danger">特急</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleFinish(scope.row)"
            v-hasPermi="['wms:outbound:finish']"
            v-if="scope.row.isFinished !== 1"
          >完成</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['wms:outbound:edit']"
            v-if="scope.row.isFinished !== 1"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['wms:outbound:remove']"
            v-if="scope.row.isFinished !== 1"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改出库单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="出库单号" prop="outboundNo" v-if="form.outboundId">
              <el-input v-model="form.outboundNo" placeholder="出库单号" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出库类型" prop="outboundType">
              <el-select v-model="form.outboundType" placeholder="请选择出库类型" @change="handleOutboundTypeChange">
                <el-option label="销售出库" value="1" />
                <el-option label="采购退货" value="2" />
                <el-option label="调拨出库" value="3" />
                <el-option label="其他出库" value="4" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 销售出库时显示订单编号选择 -->
        <el-row v-if="form.outboundType == '1'">
          <el-col :span="12">
            <el-form-item label="订单编号" prop="orderNo">
              <el-input
                v-model="form.orderNo"
                placeholder="请选择客户订单"
                readonly
                @click.native="handleOpenOrderDialog"
                style="cursor: pointer"
              >
                <i slot="suffix" class="el-icon-search" style="cursor: pointer"></i>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户名称" prop="customerName">
              <el-input v-model="form.customerName" placeholder="自动引用" disabled />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 非销售出库显示客户选择 -->
        <el-row v-else>
          <el-col :span="12">
            <el-form-item label="客户" prop="customerId">
              <el-select v-model="form.customerId" placeholder="请选择客户" filterable clearable style="width: 100%">
                <el-option
                  v-for="customer in customerOptions"
                  :key="customer.customerId"
                  :label="customer.customerName"
                  :value="customer.customerId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="仓库" prop="warehouseId">
              <el-select v-model="form.warehouseId" placeholder="请选择仓库" clearable style="width: 100%">
                <el-option
                  v-for="item in warehouseOptions"
                  :key="item.warehouseId"
                  :label="item.warehouseName"
                  :value="item.warehouseId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-select v-model="form.priority" placeholder="请选择优先级">
                <el-option label="普通" value="1" />
                <el-option label="加急" value="2" />
                <el-option label="特急" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>

        <!-- 出库明细 -->
        <el-divider>出库明细</el-divider>
        <el-table :data="form.items" style="width: 100%" max-height="300">
          <el-table-column label="物料选择" width="250">
            <template slot-scope="scope">
              <el-row :gutter="5">
                <el-col :span="18">
                  <el-input
                    v-model="scope.row.productName"
                    placeholder="选择物料"
                    readonly
                  />
                </el-col>
                <el-col :span="6">
                  <el-button type="info" icon="el-icon-search" size="small" @click="handleOpenProductDialog(scope.$index)">选择</el-button>
                </el-col>
              </el-row>
            </template>
          </el-table-column>
          <el-table-column label="物料编码" prop="productCode" width="150" />
          <el-table-column label="物料名称" prop="productName" width="180" />
          <el-table-column label="单位" prop="unit" width="80" />
          <el-table-column label="出库数量" width="120">
            <template slot-scope="scope">
              <el-input-number
                v-model="scope.row.orderQty"
                :min="0"
                :precision="2"
                :step="1"
                size="small"
                controls-position="right"
                style="width: 100%"
              />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80" align="center">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="danger"
                icon="el-icon-delete"
                circle
                @click="handleDeleteItem(scope.$index)"
              />
            </template>
          </el-table-column>
        </el-table>
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="small"
          @click="handleAddItem"
          style="margin-top: 10px"
        >添加明细</el-button>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看出库单详情对话框 -->
    <el-dialog title="出库单详情" :visible.sync="viewOpen" width="900px" append-to-body>
      <el-form label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="出库单号">
              <span>{{ viewForm.outboundNo }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出库类型">
              <span v-if="viewForm.outboundType == '1'">销售出库</span>
              <span v-else-if="viewForm.outboundType == '2'">采购退货</span>
              <span v-else-if="viewForm.outboundType == '3'">调拨出库</span>
              <span v-else-if="viewForm.outboundType == '4'">其他出库</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row v-if="viewForm.outboundType == '1'">
          <el-col :span="12">
            <el-form-item label="订单编号">
              <span>{{ viewForm.orderNo }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户名称">
              <span>{{ viewForm.customerName }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row v-else>
          <el-col :span="12">
            <el-form-item label="客户名称">
              <span>{{ viewForm.customerName }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="仓库">
              <span>{{ getWarehouseName(viewForm.warehouseId) }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优先级">
              <span v-if="viewForm.priority == '1'">普通</span>
              <span v-else-if="viewForm.priority == '2'">加急</span>
              <span v-else-if="viewForm.priority == '3'">特急</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="状态">
              <span v-if="viewForm.isFinished === 1" style="color: green;">已完成</span>
              <span v-else-if="viewForm.status == '0'">无</span>
              <span v-else-if="viewForm.status == '1'">待审核</span>
              <span v-else-if="viewForm.status == '2'">待拣货</span>
              <span v-else-if="viewForm.status == '3'">拣货中</span>
              <span v-else-if="viewForm.status == '4'">待复核</span>
              <span v-else-if="viewForm.status == '5'">待打包</span>
              <span v-else-if="viewForm.status == '6'">待发货</span>
              <span v-else-if="viewForm.status == '7'">已发货</span>
              <span v-else-if="viewForm.status == '8'">已取消</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总数量">
              <span>{{ viewForm.totalQty }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注" prop="remark">
          <span>{{ viewForm.remark }}</span>
        </el-form-item>

        <!-- 出库明细 -->
        <el-divider>出库明细</el-divider>
        <el-table :data="viewForm.items" style="width: 100%" max-height="300">
          <el-table-column label="物料编码" prop="productCode" width="150" />
          <el-table-column label="物料名称" prop="productName" width="180" />
          <el-table-column label="单位" prop="unit" width="80" />
          <el-table-column label="出库数量" prop="orderQty" width="120" align="right" />
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 选择客户订单对话框 -->
    <el-dialog title="选择客户订单" :visible.sync="orderDialogOpen" width="900px" append-to-body>
      <el-form :model="orderQuery" size="small" :inline="true">
        <el-form-item label="订单编号" prop="orderNo">
          <el-input v-model="orderQuery.orderNo" placeholder="请输入订单编号" clearable @keyup.enter.native="handleOrderQuery" />
        </el-form-item>
        <el-form-item label="客户名称" prop="customerName">
          <el-input v-model="orderQuery.customerName" placeholder="请输入客户名称" clearable @keyup.enter.native="handleOrderQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleOrderQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetOrderQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table
        v-loading="orderLoading"
        :data="orderList"
        highlight-current-row
        @current-change="handleOrderCurrentChange"
        @row-dblclick="handleOrderDblClick"
        max-height="400"
      >
        <el-table-column label="订单编号" prop="orderNo" />
        <el-table-column label="客户名称" prop="customerName" />
        <el-table-column label="订单日期" prop="orderDate" width="120">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.orderDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="总数量" prop="totalQty" width="100" align="right" />
        <el-table-column label="金额合计" prop="totalAmount" width="120" align="right">
          <template slot-scope="scope">
            <span>{{ scope.row.totalAmount ? scope.row.totalAmount.toFixed(2) : '0.00' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              icon="el-icon-check"
              circle
              @click="handleSelectOrder(scope.row)"
            />
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="orderTotal>0"
        :total="orderTotal"
        :page.sync="orderQuery.pageNum"
        :limit.sync="orderQuery.pageSize"
        @pagination="getOrderList"
      />
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleOrderConfirm">确 定</el-button>
        <el-button @click="orderDialogOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 选择物料对话框 -->
    <el-dialog title="选择物料" :visible.sync="productDialogOpen" width="800px" :modal-append-to-body="true" :close-on-click-modal="false" append-to-body>
      <el-form :model="productQuery" size="small" :inline="true">
        <el-form-item label="物料名称" prop="productName">
          <el-input v-model="productQuery.productName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleProductQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleProductQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetProductQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table
        v-loading="productLoading"
        :data="productList"
        highlight-current-row
        @current-change="handleProductCurrentChange"
        @row-dblclick="handleProductDblClick"
        max-height="400"
      >
        <el-table-column label="物料编码" prop="productCode" />
        <el-table-column label="物料名称" prop="productName" />
        <el-table-column label="规格型号" prop="specification" />
        <el-table-column label="单位" prop="unit" />
        <el-table-column label="类别" prop="categoryName" />
        <el-table-column label="操作" width="80" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              icon="el-icon-check"
              circle
              @click="handleSelectProduct(scope.row)"
            />
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="productTotal>0"
        :total="productTotal"
        :page.sync="productQuery.pageNum"
        :limit.sync="productQuery.pageSize"
        @pagination="getProductList"
      />
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleProductConfirm">确 定</el-button>
        <el-button @click="productDialogOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOutbound, getOutbound, delOutbound, addOutbound, updateOutbound, exportOutbound, finishOutbound } from "@/api/wms/outbound"
import { allCustomer } from "@/api/wms/customer"
import { allWarehouse } from "@/api/wms/warehouse"
import { listProduct } from "@/api/wms/product"
import { listFinishedCustomerOrder } from "@/api/wms/customerOrder"

export default {
  name: "Outbound",
  dicts: ['sys_normal_disable'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      outboundList: [],
      open: false,
      title: "",
      customerOptions: [],
      warehouseOptions: [],
      productOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        outboundNo: undefined,
        outboundType: undefined,
        status: undefined
      },
      form: {
        items: [],
        outboundType: "1"
      },
      // 查看详情相关
      viewOpen: false,
      viewForm: {
        items: []
      },
      rules: {
        outboundType: [
          { required: true, message: "出库类型不能为空", trigger: "change" }
        ],
        orderNo: [
          { required: true, message: "销售出库必须选择订单编号", trigger: "blur", trigger: "change" }
        ]
      },
      // 物料选择相关
      productDialogOpen: false,
      productLoading: false,
      productTotal: 0,
      productList: [],
      productQuery: {
        pageNum: 1,
        pageSize: 10,
        productName: undefined
      },
      selectedProduct: null,
      currentItemIndex: -1,
      // 客户订单选择相关
      orderDialogOpen: false,
      orderLoading: false,
      orderTotal: 0,
      orderList: [],
      orderQuery: {
        pageNum: 1,
        pageSize: 10,
        orderNo: undefined,
        customerName: undefined
      },
      selectedOrder: null
    }
  },
  created() {
    this.getList()
    this.getCustomerOptions()
    this.getWarehouseList()
    this.getProductOptions()
  },
  methods: {
    getList() {
      this.loading = true
      listOutbound(this.queryParams).then(response => {
        this.outboundList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getCustomerOptions() {
      allCustomer().then(response => {
        this.customerOptions = response.data || []
      })
    },
    getWarehouseList() {
      allWarehouse().then(response => {
        this.warehouseOptions = response.data || []
      })
    },
    // 获取物料下拉列表
    getProductOptions() {
      listProduct({ pageNum: 1, pageSize: 1000 }).then(response => {
        this.productOptions = response.rows || []
      })
    },
    // 获取物料列表（用于选择对话框）
    getProductList() {
      this.productLoading = true
      listProduct(this.productQuery).then(response => {
        this.productList = response.rows
        this.productTotal = response.total
        this.productLoading = false
      })
    },
    handleProductQuery() {
      this.productQuery.pageNum = 1
      this.getProductList()
    },
    resetProductQuery() {
      this.productQuery = {
        pageNum: 1,
        pageSize: 10,
        productName: undefined
      }
      this.getProductList()
    },
    // 物料行选择变化
    handleProductCurrentChange(val) {
      this.selectedProduct = val
    },
    // 双击行确认选择
    handleProductDblClick(row) {
      this.handleSelectProduct(row)
    },
    // 选择物料
    handleSelectProduct(product) {
      this.selectedProduct = product
      this.handleProductConfirm()
    },
    handleProductConfirm() {
      if (!this.selectedProduct) {
        this.$modal.msgWarning("请选择物料")
        return
      }
      // 更新到对应的明细行
      if (this.currentItemIndex >= 0 && this.currentItemIndex < this.form.items.length) {
        const item = this.form.items[this.currentItemIndex]
        item.productId = this.selectedProduct.productId
        item.productCode = this.selectedProduct.productCode
        item.productName = this.selectedProduct.productName
        item.unit = this.selectedProduct.unit
        if (!item.orderQty) {
          item.orderQty = 0
        }
      }
      this.productDialogOpen = false
      this.currentItemIndex = -1
    },
    // 获取已完成客户订单列表
    getOrderList() {
      this.orderLoading = true
      listFinishedCustomerOrder(this.orderQuery).then(response => {
        this.orderList = response.rows
        this.orderTotal = response.total
        this.orderLoading = false
      })
    },
    handleOrderQuery() {
      this.orderQuery.pageNum = 1
      this.getOrderList()
    },
    resetOrderQuery() {
      this.orderQuery = {
        pageNum: 1,
        pageSize: 10,
        orderNo: undefined,
        customerName: undefined
      }
      this.getOrderList()
    },
    // 订单行选择变化
    handleOrderCurrentChange(val) {
      this.selectedOrder = val
    },
    // 双击行确认选择
    handleOrderDblClick(row) {
      this.handleSelectOrder(row)
    },
    // 选择客户订单
    handleSelectOrder(order) {
      this.selectedOrder = order
      this.handleOrderConfirm()
    },
    handleOrderConfirm() {
      if (!this.selectedOrder) {
        this.$modal.msgWarning("请选择客户订单")
        return
      }
      // 填充订单信息
      this.form.orderNo = this.selectedOrder.orderNo
      this.form.customerName = this.selectedOrder.customerName
      // 复制订单明细到出库单明细
      if (this.selectedOrder.items && this.selectedOrder.items.length > 0) {
        // 先检查所有物料是否都能找到
        const missingProducts = []
        this.form.items = this.selectedOrder.items.map(item => {
          // 根据 productCode 查找 productId
          const product = this.productOptions.find(p => p.productCode === item.productCode)
          if (!product) {
            missingProducts.push(item.productCode)
          }
          return {
            productId: product ? product.productId : null,
            productCode: item.productCode,
            productName: item.productName,
            unit: product ? product.unit : '',
            orderQty: item.quantity, // 客户订单的 quantity 字段
            pickedQty: 0
          }
        })
        // 如果有找不到的物料，提示用户
        if (missingProducts.length > 0) {
          this.$modal.msgWarning(`以下物料编码不存在：${missingProducts.join(', ')}，请先添加这些物料`)
          this.form.items = []
          return
        }
      }
      this.orderDialogOpen = false
      this.selectedOrder = null
    },
    // 打开客户订单选择对话框
    handleOpenOrderDialog() {
      this.orderQuery = {
        pageNum: 1,
        pageSize: 10,
        orderNo: undefined,
        customerName: undefined
      }
      this.getOrderList()
      this.orderDialogOpen = true
    },
    // 出库类型改变
    handleOutboundTypeChange() {
      // 切换出库类型时清空订单编号
      if (this.form.outboundType !== '1') {
        this.form.orderNo = undefined
        this.form.customerName = undefined
        this.form.items = []
      }
      // 更新校验规则
      if (this.form.outboundType === '1') {
        this.rules.orderNo = [
          { required: true, message: "销售出库必须选择订单编号", trigger: "change" }
        ]
      } else {
        this.rules.orderNo = []
      }
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        outboundId: undefined,
        outboundNo: undefined,
        outboundType: "1",
        orderNo: undefined,
        customerId: undefined,
        customerName: undefined,
        warehouseId: undefined,
        status: "0",
        priority: "1",
        waveNo: undefined,
        expectedDate: undefined,
        remark: undefined,
        items: []
      }
      this.resetForm("form")
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加出库单"
    },
    // 添加出库明细
    handleAddItem() {
      this.form.items.push({
        productId: undefined,
        productCode: '',
        productName: '',
        unit: '',
        orderQty: 0,
        pickedQty: 0
      })
    },
    // 删除出库明细
    handleDeleteItem(index) {
      this.form.items.splice(index, 1)
    },
    // 打开物料选择对话框
    handleOpenProductDialog(index) {
      this.currentItemIndex = index
      this.selectedProduct = null
      this.productQuery = {
        pageNum: 1,
        pageSize: 10,
        productName: undefined
      }
      this.getProductList()
      this.productDialogOpen = true
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.outboundId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleUpdate(row) {
      this.reset()
      const outboundId = row.outboundId || this.ids
      getOutbound(outboundId).then(response => {
        this.form = response.data
        // 确保 items 数组存在
        if (!this.form.items) {
          this.form.items = []
        }
        this.open = true
        this.title = "修改出库单"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 校验是否有出库明细
          if (!this.form.items || this.form.items.length === 0) {
            this.$modal.msgWarning("请至少添加一条出库明细")
            return
          }
          // 校验明细中物料和数量是否填写
          let totalQty = 0
          for (let i = 0; i < this.form.items.length; i++) {
            const item = this.form.items[i]
            if (!item.productId) {
              this.$modal.msgWarning(`第${i + 1}行明细请选择物料`)
              return
            }
            if (!item.orderQty || item.orderQty <= 0) {
              this.$modal.msgWarning(`第${i + 1}行明细请填写出库数量`)
              return
            }
            totalQty += parseFloat(item.orderQty)
          }

          // 计算总数量
          this.form.totalQty = totalQty

          // 获取客户名称
          if (this.form.customerId && this.form.outboundType !== '1') {
            const customer = this.customerOptions.find(c => c.customerId === this.form.customerId)
            if (customer) {
              this.form.customerName = customer.customerName
            }
          }

          if (this.form.outboundId != undefined) {
            updateOutbound(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addOutbound(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const outboundIds = row.outboundId || this.ids
      this.$modal.confirm('是否确认删除出库单编号为"' + outboundIds + '"的数据项？').then(function() {
        return delOutbound(outboundIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleFinish(row) {
      this.$modal.confirm('是否确认完成出库单编号为"' + row.outboundNo + '"？').then(() => {
        return finishOutbound(row.outboundId)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("完成成功")
      }).catch(() => {})
    },
    // 查看出库单详情
    handleView(row) {
      const outboundId = row.outboundId
      getOutbound(outboundId).then(response => {
        this.viewForm = response.data
        if (!this.viewForm.items) {
          this.viewForm.items = []
        }
        this.viewOpen = true
      })
    },
    // 根据仓库 ID 获取仓库名称
    getWarehouseName(warehouseId) {
      if (!warehouseId) return ''
      const warehouse = this.warehouseOptions.find(w => w.warehouseId === warehouseId)
      return warehouse ? warehouse.warehouseName : ''
    },
    handleExport() {
      this.download('wms/outbound/export', {
        ...this.queryParams
      }, `outbound_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
