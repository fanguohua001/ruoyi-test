<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="入库单号" prop="inboundNo">
        <el-input
          v-model="queryParams.inboundNo"
          placeholder="请输入入库单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="入库类型" prop="inboundType">
        <el-select v-model="queryParams.inboundType" placeholder="入库类型" clearable>
          <el-option label="采购入库" value="1" />
          <el-option label="退货入库" value="2" />
          <el-option label="调拨入库" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="状态" clearable>
          <el-option label="无" value="0" />
          <el-option label="待收货" value="1" />
          <el-option label="收货中" value="2" />
          <el-option label="待质检" value="3" />
          <el-option label="待上架" value="4" />
          <el-option label="已入库" value="5" />
        </el-select>
      </el-form-item>
      <el-form-item label="是否完成" prop="isFinished">
        <el-select v-model="queryParams.isFinished" placeholder="是否完成" clearable>
          <el-option label="草稿" value="0" />
          <el-option label="已完成" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="供应商" prop="supplierId">
        <el-select v-model="queryParams.supplierId" placeholder="请选择供应商" clearable>
          <el-option
            v-for="item in supplierOptions"
            :key="item.supplierId"
            :label="item.supplierName"
            :value="item.supplierId"
          />
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
          v-hasPermi="['wms:inbound:add']"
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
          v-hasPermi="['wms:inbound:edit']"
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
          v-hasPermi="['wms:inbound:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['wms:inbound:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="inboundList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="入库单号" align="center" prop="inboundNo" />
      <el-table-column label="入库类型" align="center" prop="inboundType">
        <template slot-scope="scope">
          <span v-if="scope.row.inboundType == '1'">采购入库</span>
          <span v-else-if="scope.row.inboundType == '2'">退货入库</span>
          <span v-else-if="scope.row.inboundType == '3'">调拨入库</span>
        </template>
      </el-table-column>
      <el-table-column label="供应商名称" align="center" prop="supplierName" />
      <el-table-column label="总数量" align="center" prop="totalQty" />
      <el-table-column label="已收货数量" align="center" prop="receivedQty" />
      <el-table-column label="合格数量" align="center" prop="qualifiedQty" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <span v-if="scope.row.isFinished === 1">已完成</span>
          <span v-else-if="scope.row.status == '0'">无</span>
          <span v-else-if="scope.row.status == '1'">待收货</span>
          <span v-else-if="scope.row.status == '2'">收货中</span>
          <span v-else-if="scope.row.status == '3'">待质检</span>
          <span v-else-if="scope.row.status == '4'">待上架</span>
          <span v-else-if="scope.row.status == '5'">已入库</span>
        </template>
      </el-table-column>
      <el-table-column label="是否完成" align="center" prop="isFinished">
        <template slot-scope="scope">
          <span v-if="scope.row.isFinished === 1" style="color: green;">已完成</span>
          <span v-else style="color: orange;">草稿</span>
        </template>
      </el-table-column>
      <el-table-column label="预计到货日期" align="center" prop="expectedDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expectedDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['wms:inbound:query']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleFinish(scope.row)"
            v-hasPermi="['wms:inbound:finish']"
            v-if="scope.row.isFinished !== 1"
          >完成</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['wms:inbound:edit']"
            v-if="scope.row.isFinished !== 1"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['wms:inbound:remove']"
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

    <!-- 添加或修改入库单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="入库单号" prop="inboundNo" v-if="form.inboundId">
              <el-input v-model="form.inboundNo" placeholder="入库单号" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入库类型" prop="inboundType">
              <el-select v-model="form.inboundType" placeholder="请选择入库类型" @change="handleInboundTypeChange">
                <el-option label="采购入库" value="1" />
                <el-option label="退货入库" value="2" />
                <el-option label="调拨入库" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="供应商" prop="supplierId">
              <el-select v-model="form.supplierId" placeholder="请选择供应商" clearable style="width: 100%" @change="handleSupplierChange">
                <el-option
                  v-for="item in supplierOptions"
                  :key="item.supplierId"
                  :label="item.supplierName"
                  :value="item.supplierId"
                />
              </el-select>
            </el-form-item>
          </el-col>
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
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="预计到货日期" prop="expectedDate">
              <el-date-picker v-model="form.expectedDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 100%"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>

        <!-- 入库明细 -->
        <el-divider>入库明细</el-divider>
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
          <el-table-column label="应收数量" width="120">
            <template slot-scope="scope">
              <el-input-number
                v-model="scope.row.expectedQty"
                :min="0"
                :precision="2"
                :step="1"
                size="small"
                controls-position="right"
                style="width: 100%"
              />
            </template>
          </el-table-column>
          <el-table-column label="批次号" width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.batchNo" size="small" placeholder="请输入批次号" />
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

    <!-- 入库单详情对话框 -->
    <el-dialog title="入库单详情" :visible.sync="viewOpen" width="900px" append-to-body>
      <el-form label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="入库单号">
              <span>{{ viewForm.inboundNo }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入库类型">
              <span v-if="viewForm.inboundType == '1'">采购入库</span>
              <span v-else-if="viewForm.inboundType == '2'">退货入库</span>
              <span v-else-if="viewForm.inboundType == '3'">调拨入库</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="供应商">
              <span>{{ viewForm.supplierName }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="仓库">
              <span>{{ getWarehouseName(viewForm.warehouseId) }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="预计到货日期">
              <span>{{ parseTime(viewForm.expectedDate, '{y}-{m}-{d}') }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <span v-if="viewForm.status == '0'">草稿</span>
              <span v-else-if="viewForm.status == '1'">待收货</span>
              <span v-else-if="viewForm.status == '2'">收货中</span>
              <span v-else-if="viewForm.status == '3'">待质检</span>
              <span v-else-if="viewForm.status == '4'">待上架</span>
              <span v-else-if="viewForm.status == '5'">已完成</span>
              <span v-else-if="viewForm.status == '6'">已取消</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <span>{{ viewForm.remark }}</span>
        </el-form-item>

        <!-- 入库明细 -->
        <el-divider>入库明细</el-divider>
        <el-table :data="viewForm.items" style="width: 100%" max-height="400">
          <el-table-column label="物料编码" prop="productCode" width="150" />
          <el-table-column label="物料名称" prop="productName" width="180" />
          <el-table-column label="规格型号" prop="specification" width="120" />
          <el-table-column label="单位" prop="unit" width="80" />
          <el-table-column label="应收数量" prop="expectedQty" width="100" align="right" />
          <el-table-column label="批次号" prop="batchNo" width="120" />
        </el-table>
        <el-row style="margin-top: 10px; font-weight: bold;">
          <el-col :span="12">
            总数量：{{ viewForm.totalQty }}
          </el-col>
        </el-row>
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
import { listInbound, getInbound, delInbound, addInbound, updateInbound, exportInbound, finishInbound, checkFinished } from "@/api/wms/inbound"
import { allSupplier } from "@/api/wms/supplier"
import { allWarehouse } from "@/api/wms/warehouse"
import { listProduct } from "@/api/wms/product"

export default {
  name: "Inbound",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      inboundList: [],
      open: false,
      title: "",
      supplierOptions: [],
      warehouseOptions: [],
      productOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        inboundNo: undefined,
        inboundType: undefined,
        supplierId: undefined,
        status: undefined,
        isFinished: undefined
      },
      form: {
        items: [],
        isFinished: 0
      },
      rules: {
        inboundType: [
          { required: true, message: "入库类型不能为空", trigger: "change" }
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
      // 查看详情相关
      viewOpen: false,
      viewForm: {
        items: []
      }
    }
  },
  created() {
    this.getList()
    this.getSupplierList()
    this.getWarehouseList()
    this.getProductOptions()
  },
  methods: {
    getList() {
      this.loading = true
      listInbound(this.queryParams).then(response => {
        this.inboundList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getSupplierList() {
      allSupplier().then(response => {
        this.supplierOptions = response.data
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
    handleProductSelectionChange(selection) {
      if (selection && selection.length > 0) {
        this.selectedProduct = selection[0]
      } else {
        this.selectedProduct = null
      }
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
        if (!item.expectedQty) {
          item.expectedQty = 0
        }
      }
      this.productDialogOpen = false
      this.currentItemIndex = -1
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        inboundId: undefined,
        inboundNo: undefined,
        inboundType: "1",
        sourceType: undefined,
        sourceNo: undefined,
        supplierId: undefined,
        supplierName: undefined,
        warehouseId: undefined,
        status: "0",
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
      this.title = "添加入库单"
    },
    // 添加入库明细
    handleAddItem() {
      this.form.items.push({
        productId: undefined,
        productCode: '',
        productName: '',
        unit: '',
        expectedQty: 0,
        batchNo: '',
        status: '0'
      })
    },
    // 删除入库明细
    handleDeleteItem(index) {
      this.form.items.splice(index, 1)
    },
    handleInboundTypeChange() {
      // 入库类型改变时的处理
    },
    handleSupplierChange() {
      // 供应商改变时的处理
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
      this.ids = selection.map(item => item.inboundId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    // 查看详情
    handleView(row) {
      const inboundId = row.inboundId
      getInbound(inboundId).then(response => {
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
    // 完成入库单
    handleFinish(row) {
      const inboundId = row.inboundId
      this.$modal.confirm('是否确认完成入库单 "' + row.inboundNo + '"？完成后将不允许再修改').then(() => {
        return finishInbound(inboundId)
      }).then(() => {
        this.$modal.msgSuccess("入库单已完成")
        this.getList()
      }).catch(() => {})
    },
    handleUpdate(row) {
      this.reset()
      const inboundId = row.inboundId || this.ids
      getInbound(inboundId).then(response => {
        this.form = response.data
        // 确保 items 数组存在
        if (!this.form.items) {
          this.form.items = []
        }
        // 检查是否已完成，如果已完成则不允许修改
        if (this.form.isFinished === 1) {
          this.$modal.msgError("入库单已完成，不允许修改")
          return
        }
        this.open = true
        this.title = "修改入库单"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 校验是否有入库明细
          if (!this.form.items || this.form.items.length === 0) {
            this.$modal.msgWarning("请至少添加一条入库明细")
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
            if (!item.expectedQty || item.expectedQty <= 0) {
              this.$modal.msgWarning(`第${i + 1}行明细请填写应收数量`)
              return
            }
            totalQty += parseFloat(item.expectedQty)
          }

          // 计算总数量
          this.form.totalQty = totalQty

          // 获取供应商名称
          if (this.form.supplierId) {
            const supplier = this.supplierOptions.find(s => s.supplierId === this.form.supplierId)
            if (supplier) {
              this.form.supplierName = supplier.supplierName
            }
          }

          // 如果是新增入库单，询问是否直接完成
          if (this.form.inboundId == undefined) {
            this.$modal.confirm('是否直接完成入库单？完成后将不允许再修改。', {
              confirmButtonText: '是',
              cancelButtonText: '否',
              type: 'warning'
            }).then(() => {
              // 用户选择完成
              this.form.isFinished = 1
              this.form.status = "5"
              addInbound(this.form).then(() => {
                this.$modal.msgSuccess("新增成功")
                this.open = false
                this.getList()
              })
            }).catch(() => {
              // 用户选择草稿
              this.form.isFinished = 0
              this.form.status = "0"
              addInbound(this.form).then(() => {
                this.$modal.msgSuccess("新增成功")
                this.open = false
                this.getList()
              })
            })
          } else {
            // 修改入库单
            updateInbound(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const inboundIds = row.inboundId || this.ids
      this.$modal.confirm('是否确认删除入库单编号为"' + inboundIds + '"的数据项？').then(function() {
        return delInbound(inboundIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('wms/inbound/export', {
        ...this.queryParams
      }, `inbound_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
