<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单编号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="供应商编码" prop="supplierCode">
        <el-input
          v-model="queryParams.supplierCode"
          placeholder="请输入供应商编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="供应商名称" prop="supplierName">
        <el-input
          v-model="queryParams.supplierName"
          placeholder="请输入供应商名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="订单状态" clearable>
          <el-option label="草稿" value="0" />
          <el-option label="完成" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="dateRange">
        <el-date-picker
          v-model="queryParams.dateRange"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          style="width: 240px"
        />
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
          v-hasPermi="['wms:purchaseOrder:add']"
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
          v-hasPermi="['wms:purchaseOrder:edit']"
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
          v-hasPermi="['wms:purchaseOrder:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['wms:purchaseOrder:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="purchaseOrderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单编号" align="center" prop="orderNo" />
      <el-table-column label="订单日期" align="center" prop="orderDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.orderDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="供应商编码" align="center" prop="supplierCode" />
      <el-table-column label="供应商名称" align="center" prop="supplierName" />
      <el-table-column label="要求到货日期" align="center" prop="requiredDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.requiredDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单数量" align="center" prop="totalQty" />
      <el-table-column label="金额合计" align="center" prop="totalAmount" />
      <el-table-column label="订单状态" align="center" prop="orderStatus">
        <template slot-scope="scope">
          <span v-if="scope.row.orderStatus === 1" style="color: green;">完成</span>
          <span v-else style="color: orange;">草稿</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['wms:purchaseOrder:query']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleFinish(scope.row)"
            v-hasPermi="['wms:purchaseOrder:finish']"
            v-if="scope.row.orderStatus !== 1"
          >完成</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['wms:purchaseOrder:edit']"
            v-if="scope.row.orderStatus !== 1"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['wms:purchaseOrder:remove']"
            v-if="scope.row.orderStatus !== 1"
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

    <!-- 添加或修改采购订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="950px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="订单编号" prop="orderNo" v-if="form.orderId">
              <el-input v-model="form.orderNo" placeholder="订单编号" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="订单日期" prop="orderDate">
              <el-date-picker v-model="form.orderDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 100%"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="供应商" prop="supplierId">
              <el-row :gutter="5">
                <el-col :span="18">
                  <el-input v-model="form.supplierName" placeholder="请选择供应商" readonly />
                </el-col>
                <el-col :span="6">
                  <el-button type="info" icon="el-icon-search" size="small" @click="handleOpenSupplierDialog">选择</el-button>
                </el-col>
              </el-row>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="要求到货日期" prop="requiredDate">
              <el-date-picker v-model="form.requiredDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 100%"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="说明" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>

        <!-- 订单明细 -->
        <el-divider>订单明细</el-divider>
        <el-table :data="form.items" style="width: 100%" max-height="350">
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
          <el-table-column label="不含税单价" width="120">
            <template slot-scope="scope">
              <el-input-number
                v-model="scope.row.unitPrice"
                :min="0"
                :precision="2"
                :step="0.01"
                size="small"
                controls-position="right"
                style="width: 100%"
                @change="handleItemChange(scope.$index)"
              />
            </template>
          </el-table-column>
          <el-table-column label="数量" width="100">
            <template slot-scope="scope">
              <el-input-number
                v-model="scope.row.quantity"
                :min="0"
                :precision="2"
                :step="1"
                size="small"
                controls-position="right"
                style="width: 100%"
                @change="handleItemChange(scope.$index)"
              />
            </template>
          </el-table-column>
          <el-table-column label="不含税金额" prop="amount" width="120" />
          <el-table-column label="税率%" width="80">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.taxRate"
                size="small"
                placeholder="请输入税率"
                @input="handleItemChange(scope.$index)"
              />
            </template>
          </el-table-column>
          <el-table-column label="含税金额" prop="amountTax" width="120" />
          <el-table-column label="说明" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.remark" size="small" placeholder="请输入说明" />
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
        <el-row style="margin-top: 10px; font-weight: bold;">
          <el-col :span="12">
            订单数量合计：{{ form.totalQty }}
          </el-col>
          <el-col :span="12">
            订单金额合计：{{ form.totalAmount }}
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 采购订单详情对话框 -->
    <el-dialog title="采购订单详情" :visible.sync="viewOpen" width="950px" append-to-body>
      <el-form label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="订单编号">
              <span>{{ viewForm.orderNo }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="订单日期">
              <span>{{ parseTime(viewForm.orderDate, '{y}-{m}-{d}') }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="供应商编码">
              <span>{{ viewForm.supplierCode }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商名称">
              <span>{{ viewForm.supplierName }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="要求到货日期">
              <span>{{ parseTime(viewForm.requiredDate, '{y}-{m}-{d}') }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="订单状态">
              <span v-if="viewForm.orderStatus === 1" style="color: green;">完成</span>
              <span v-else style="color: orange;">草稿</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="订单数量合计">
              <span>{{ viewForm.totalQty }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="金额合计">
              <span>{{ viewForm.totalAmount }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="说明">
          <span>{{ viewForm.remark }}</span>
        </el-form-item>

        <!-- 订单明细 -->
        <el-divider>订单明细</el-divider>
        <el-table :data="viewForm.items" style="width: 100%" max-height="400">
          <el-table-column label="物料编码" prop="productCode" width="150" />
          <el-table-column label="物料名称" prop="productName" width="180" />
          <el-table-column label="不含税单价" prop="unitPrice" width="100" />
          <el-table-column label="数量" prop="quantity" width="80" />
          <el-table-column label="不含税金额" prop="amount" width="100" />
          <el-table-column label="税率%" prop="taxRate" width="70" />
          <el-table-column label="含税金额" prop="amountTax" width="100" />
          <el-table-column label="说明" prop="remark" width="150" />
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="viewOpen = false">关 闭</el-button>
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

    <!-- 选择供应商对话框 -->
    <el-dialog title="选择供应商" :visible.sync="supplierDialogOpen" width="800px" :modal-append-to-body="true" :close-on-click-modal="false" append-to-body>
      <el-form :model="supplierQuery" size="small" :inline="true">
        <el-form-item label="供应商名称" prop="supplierName">
          <el-input v-model="supplierQuery.supplierName" placeholder="请输入供应商名称" clearable @keyup.enter.native="handleSupplierQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleSupplierQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetSupplierQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table
        v-loading="supplierLoading"
        :data="supplierList"
        highlight-current-row
        @current-change="handleSupplierCurrentChange"
        @row-dblclick="handleSupplierDblClick"
        max-height="400"
      >
        <el-table-column label="供应商编码" prop="supplierCode" />
        <el-table-column label="供应商名称" prop="supplierName" />
        <el-table-column label="联系人" prop="contact" />
        <el-table-column label="联系电话" prop="phone" />
        <el-table-column label="操作" width="80" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              icon="el-icon-check"
              circle
              @click="handleSelectSupplier(scope.row)"
            />
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="supplierTotal>0"
        :total="supplierTotal"
        :page.sync="supplierQuery.pageNum"
        :limit.sync="supplierQuery.pageSize"
        @pagination="getSupplierListForDialog"
      />
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleSupplierConfirm">确 定</el-button>
        <el-button @click="supplierDialogOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPurchaseOrder, getPurchaseOrder, delPurchaseOrder, addPurchaseOrder, updatePurchaseOrder, exportPurchaseOrder, finishPurchaseOrder } from "@/api/wms/purchaseOrder"
import { listSupplier } from "@/api/wms/supplier"
import { listProduct } from "@/api/wms/product"

export default {
  name: "PurchaseOrder",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      purchaseOrderList: [],
      open: false,
      title: "",
      productOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: undefined,
        supplierCode: undefined,
        supplierName: undefined,
        orderStatus: undefined,
        dateRange: []
      },
      form: {
        items: [],
        orderStatus: 0
      },
      rules: {
        supplierId: [
          { required: true, message: "供应商不能为空", trigger: "change" }
        ],
        orderDate: [
          { required: true, message: "订单日期不能为空", trigger: "change" }
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
      // 供应商选择相关
      supplierDialogOpen: false,
      supplierLoading: false,
      supplierTotal: 0,
      supplierList: [],
      supplierQuery: {
        pageNum: 1,
        pageSize: 10,
        supplierName: undefined
      },
      selectedSupplier: null,
      // 查看详情相关
      viewOpen: false,
      viewForm: {
        items: []
      }
    }
  },
  created() {
    // 初始化日期范围为最近一个月
    const endDate = new Date()
    const startDate = new Date()
    startDate.setMonth(startDate.getMonth() - 1)
    this.queryParams.dateRange = [
      this.formatDate(startDate),
      this.formatDate(endDate)
    ]
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      // 处理日期范围参数
      let params = {}
      if (this.queryParams.dateRange && this.queryParams.dateRange.length === 2) {
        params.beginTime = this.queryParams.dateRange[0]
        params.endTime = this.queryParams.dateRange[1]
      }
      // 合并其他查询参数
      Object.keys(this.queryParams).forEach(key => {
        if (key !== 'dateRange' && this.queryParams[key] !== undefined && this.queryParams[key] !== '') {
          params[key] = this.queryParams[key]
        }
      })
      listPurchaseOrder(params).then(response => {
        this.purchaseOrderList = response.rows
        this.total = response.total
        this.loading = false
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
        if (!item.unitPrice) {
          item.unitPrice = 0
        }
        if (!item.quantity) {
          item.quantity = 0
        }
        if (!item.taxRate) {
          item.taxRate = 13
        }
        this.handleItemChange(this.currentItemIndex)
      }
      this.productDialogOpen = false
      this.currentItemIndex = -1
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
    // 打开供应商选择对话框
    handleOpenSupplierDialog() {
      this.selectedSupplier = null
      this.supplierQuery = {
        pageNum: 1,
        pageSize: 10,
        supplierName: undefined
      }
      this.getSupplierListForDialog()
      this.supplierDialogOpen = true
    },
    // 获取供应商列表（用于选择对话框）
    getSupplierListForDialog() {
      this.supplierLoading = true
      listSupplier(this.supplierQuery).then(response => {
        this.supplierList = response.rows
        this.supplierTotal = response.total
        this.supplierLoading = false
      })
    },
    handleSupplierQuery() {
      this.supplierQuery.pageNum = 1
      this.getSupplierListForDialog()
    },
    resetSupplierQuery() {
      this.supplierQuery = {
        pageNum: 1,
        pageSize: 10,
        supplierName: undefined
      }
      this.getSupplierListForDialog()
    },
    // 供应商行选择变化
    handleSupplierCurrentChange(val) {
      this.selectedSupplier = val
    },
    // 双击行确认选择
    handleSupplierDblClick(row) {
      this.handleSelectSupplier(row)
    },
    // 选择供应商
    handleSelectSupplier(supplier) {
      this.selectedSupplier = supplier
      this.handleSupplierConfirm()
    },
    handleSupplierConfirm() {
      if (!this.selectedSupplier) {
        this.$modal.msgWarning("请选择供应商")
        return
      }
      this.form.supplierId = this.selectedSupplier.supplierId
      this.form.supplierCode = this.selectedSupplier.supplierCode
      this.form.supplierName = this.selectedSupplier.supplierName
      this.supplierDialogOpen = false
    },
    // 明细项变化，重新计算金额
    handleItemChange(index) {
      const item = this.form.items[index]
      // 计算不含税金额
      if (item.unitPrice && item.quantity) {
        item.amount = parseFloat(item.unitPrice) * parseFloat(item.quantity)
      } else {
        item.amount = 0
      }
      // 计算含税金额
      const taxRate = parseFloat(item.taxRate) || 0
      if (item.amount > 0) {
        const taxAmount = item.amount * (taxRate / 100)
        item.amountTax = item.amount + taxAmount
        item.unitPriceTax = parseFloat(item.unitPrice) * (1 + taxRate / 100)
      } else {
        item.amountTax = 0
        item.unitPriceTax = 0
      }
      // 计算合计
      this.calculateTotals()
    },
    // 计算合计
    calculateTotals() {
      let totalQty = 0
      let totalAmount = 0
      this.form.items.forEach(item => {
        totalQty += parseFloat(item.quantity || 0)
        totalAmount += parseFloat(item.amount || 0)
      })
      this.form.totalQty = totalQty
      this.form.totalAmount = totalAmount
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      const today = new Date().toISOString().split('T')[0]
      this.form = {
        orderId: undefined,
        orderNo: undefined,
        orderDate: today,
        supplierId: undefined,
        supplierCode: undefined,
        supplierName: undefined,
        requiredDate: undefined,
        totalQty: 0,
        totalAmount: 0,
        orderStatus: 0,
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
      // 重置日期范围为最近一个月
      const endDate = new Date()
      const startDate = new Date()
      startDate.setMonth(startDate.getMonth() - 1)
      this.queryParams.dateRange = [
        this.formatDate(startDate),
        this.formatDate(endDate)
      ]
      this.handleQuery()
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加采购订单"
    },
    // 添加订单明细
    handleAddItem() {
      this.form.items.push({
        productCode: '',
        productName: '',
        unitPrice: 0,
        quantity: 0,
        amount: 0,
        taxRate: 13,
        unitPriceTax: 0,
        amountTax: 0,
        remark: ''
      })
      this.calculateTotals()
    },
    // 删除订单明细
    handleDeleteItem(index) {
      this.form.items.splice(index, 1)
      this.calculateTotals()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.orderId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 查看详情
    handleView(row) {
      const orderId = row.orderId
      getPurchaseOrder(orderId).then(response => {
        this.viewForm = response.data
        if (!this.viewForm.items) {
          this.viewForm.items = []
        }
        this.viewOpen = true
      })
    },
    // 完成订单
    handleFinish(row) {
      const orderId = row.orderId
      this.$modal.confirm('是否确认完成订单 "' + row.orderNo + '"？完成后将不允许再编辑').then(() => {
        return finishPurchaseOrder(orderId)
      }).then(() => {
        this.$modal.msgSuccess("订单已完成")
        this.getList()
      }).catch(() => {})
    },
    handleUpdate(row) {
      this.reset()
      const orderId = row.orderId || this.ids
      getPurchaseOrder(orderId).then(response => {
        const data = response.data
        this.form = {
          ...data,
          items: data.items || []
        }
        // 处理每个明细项，确保必要字段存在
        this.form.items = this.form.items.map(item => ({
          productCode: item.productCode || '',
          productName: item.productName || '',
          unitPrice: item.unitPrice || 0,
          quantity: item.quantity || 0,
          amount: item.amount || 0,
          taxRate: item.taxRate || 13,
          unitPriceTax: item.unitPriceTax || 0,
          amountTax: item.amountTax || 0,
          remark: item.remark || ''
        }))
        // 计算合计
        this.$nextTick(() => {
          this.calculateTotals()
        })
        // 检查是否已完成，如果已完成则不允许修改
        if (this.form.orderStatus === 1) {
          this.$modal.msgError("订单已完成，不允许修改")
          return
        }
        this.open = true
        this.title = "修改采购订单"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 校验是否有订单明细
          if (!this.form.items || this.form.items.length === 0) {
            this.$modal.msgWarning("请至少添加一条订单明细")
            return
          }
          // 校验明细中物料和数量是否填写
          for (let i = 0; i < this.form.items.length; i++) {
            const item = this.form.items[i]
            if (!item.productCode) {
              this.$modal.msgWarning(`第${i + 1}行明细请选择物料`)
              return
            }
            if (!item.quantity || item.quantity <= 0) {
              this.$modal.msgWarning(`第${i + 1}行明细请填写数量`)
              return
            }
          }

          // 计算合计
          this.calculateTotals()

          // 如果是新增订单，询问是否直接完成
          if (this.form.orderId == undefined) {
            this.$modal.confirm('是否直接完成订单？完成后将不允许再修改。', {
              confirmButtonText: '是',
              cancelButtonText: '否',
              type: 'warning'
            }).then(() => {
              // 用户选择完成
              this.form.orderStatus = 1
              addPurchaseOrder(this.form).then(() => {
                this.$modal.msgSuccess("新增成功")
                this.open = false
                this.getList()
              })
            }).catch(() => {
              // 用户选择草稿
              this.form.orderStatus = 0
              addPurchaseOrder(this.form).then(() => {
                this.$modal.msgSuccess("新增成功")
                this.open = false
                this.getList()
              })
            })
          } else {
            // 修改订单
            updatePurchaseOrder(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const orderIds = row.orderId || this.ids
      this.$modal.confirm('是否确认删除订单编号为"' + orderIds + '"的数据项？').then(function() {
        return delPurchaseOrder(orderIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('wms/purchaseOrder/export', {
        ...this.queryParams
      }, `purchaseOrder_${new Date().getTime()}.xlsx`)
    },
    // 格式化日期
    formatDate(date) {
      const y = date.getFullYear()
      const m = String(date.getMonth() + 1).padStart(2, '0')
      const d = String(date.getDate()).padStart(2, '0')
      return `${y}-${m}-${d}`
    }
  }
}
</script>
