<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="移库单号" prop="transferNo">
        <el-input
          v-model="queryParams.transferNo"
          placeholder="请输入移库单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="状态" clearable>
          <el-option label="待执行" value="0" />
          <el-option label="已完成" value="1" />
          <el-option label="已取消" value="2" />
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
          v-hasPermi="['wms:transfer:add']"
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
          v-hasPermi="['wms:transfer:edit']"
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
          v-hasPermi="['wms:transfer:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['wms:transfer:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="transferList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="移库单号" align="center" prop="transferNo" />
      <el-table-column label="移库数量" align="center" prop="qty" />
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '0'" type="warning">待执行</el-tag>
          <el-tag v-else-if="scope.row.status === '1'" type="success">已完成</el-tag>
          <el-tag v-else-if="scope.row.status === '2'" type="info">已取消</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['wms:transfer:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['wms:transfer:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleExecuteTransfer(scope.row)"
            v-hasPermi="['wms:transfer:execute']"
          >执行移库</el-button>
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

    <!-- 添加或修改移库单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="移库单号" prop="transferNo">
          <el-input v-model="form.transferNo" placeholder="请输入移库单号" disabled />
        </el-form-item>
        <el-form-item label="仓库" prop="warehouseId">
          <el-select v-model="form.warehouseId" placeholder="请选择仓库" clearable style="width: 100%" @change="handleWarehouseChange">
            <el-option
              v-for="item in warehouseOptions"
              :key="item.warehouseId"
              :label="item.warehouseName"
              :value="item.warehouseId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="源库位" prop="fromLocationId">
          <el-select v-model="form.fromLocationId" placeholder="请选择源库位" clearable style="width: 100%">
            <el-option
              v-for="item in locationOptions"
              :key="item.locationId"
              :label="item.locationName"
              :value="item.locationId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="目标库位" prop="toLocationId">
          <el-select v-model="form.toLocationId" placeholder="请选择目标库位" clearable style="width: 100%">
            <el-option
              v-for="item in locationOptions"
              :key="item.locationId"
              :label="item.locationName"
              :value="item.locationId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="物料" prop="productId">
          <el-select v-model="form.productId" placeholder="请选择物料" filterable clearable style="width: 100%" @change="handleProductChange">
            <el-option
              v-for="item in productOptions"
              :key="item.productId"
              :label="item.productName"
              :value="item.productId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="移库数量" prop="qty">
          <el-input-number v-model="form.qty" :min="0" :precision="2" placeholder="请输入移库数量" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 执行移库对话框 -->
    <el-dialog title="执行移库" :visible.sync="executeOpen" width="400px" append-to-body>
      <el-form ref="executeForm" :model="executeForm" :rules="executeRules" label-width="80px">
        <el-form-item label="物料" prop="productId">
          <el-input v-model="executeForm.productName" placeholder="物料名称" disabled />
        </el-form-item>
        <el-form-item label="移库数量" prop="qty">
          <el-input-number v-model="executeForm.qty" :min="0" :precision="2" placeholder="请输入移库数量" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitExecuteForm">确 定</el-button>
        <el-button @click="executeOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTransfer, getTransfer, delTransfer, addTransfer, updateTransfer, exportTransfer, executeTransfer } from "@/api/wms/transfer"
import { allWarehouse } from "@/api/wms/warehouse"
import { listLocation } from "@/api/wms/location"
import { listProduct } from "@/api/wms/product"

export default {
  name: "Transfer",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      transferList: [],
      open: false,
      executeOpen: false,
      title: "",
      warehouseOptions: [],
      locationOptions: [],
      productOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        transferNo: undefined,
        status: undefined
      },
      form: {},
      executeForm: {},
      rules: {
        transferNo: [
          { required: true, message: "移库单号不能为空", trigger: "blur" }
        ],
        warehouseId: [
          { required: true, message: "仓库不能为空", trigger: "change" }
        ],
        fromLocationId: [
          { required: true, message: "源库位不能为空", trigger: "change" }
        ],
        toLocationId: [
          { required: true, message: "目标库位不能为空", trigger: "change" }
        ],
        productId: [
          { required: true, message: "物料不能为空", trigger: "blur" }
        ],
        qty: [
          { required: true, message: "移库数量不能为空", trigger: "blur" }
        ]
      },
      executeRules: {
        productId: [
          { required: true, message: "物料 ID 不能为空", trigger: "blur" }
        ],
        qty: [
          { required: true, message: "移库数量不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getWarehouseList()
  },
  methods: {
    getList() {
      this.loading = true
      listTransfer(this.queryParams).then(response => {
        this.transferList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        transferId: undefined,
        transferNo: undefined,
        warehouseId: undefined,
        fromLocationId: undefined,
        toLocationId: undefined,
        productId: undefined,
        qty: undefined,
        status: "0",
        remark: undefined
      }
      this.resetForm("form")
    },
    getWarehouseList() {
      allWarehouse().then(response => {
        this.warehouseOptions = response.data || []
      })
    },
    getLocationList(warehouseId) {
      if (!warehouseId) {
        this.locationOptions = []
        return
      }
      listLocation({ warehouseId }).then(response => {
        this.locationOptions = response.rows || []
      })
    },
    getProductList() {
      listProduct({ pageNum: 1, pageSize: 1000 }).then(response => {
        this.productOptions = response.rows || []
      })
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
      this.getProductList()
      this.open = true
      this.title = "添加移库单"
    },
    handleWarehouseChange(value) {
      this.getLocationList(value)
      this.form.fromLocationId = undefined
      this.form.toLocationId = undefined
    },
    handleProductChange(value) {
      const product = this.productOptions.find(p => p.productId === value)
      if (product) {
        this.form.productCode = product.productCode
        this.form.productName = product.productName
      }
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.transferId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleUpdate(row) {
      this.reset()
      const transferId = row.transferId || this.ids
      getTransfer(transferId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改移库单"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.transferId != undefined) {
            updateTransfer(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addTransfer(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const transferIds = row.transferId || this.ids
      this.$modal.confirm('是否确认删除移库单编号为"' + transferIds + '"的数据项？').then(function() {
        return delTransfer(transferIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('wms/transfer/export', {
        ...this.queryParams
      }, `transfer_${new Date().getTime()}.xlsx`)
    },
    handleExecuteTransfer(row) {
      this.executeForm = {
        transferId: row.transferId,
        productId: row.productId,
        productName: row.productName,
        qty: row.qty
      }
      this.executeOpen = true
      this.title = row.transferNo
    },
    submitExecuteForm() {
      this.$refs["executeForm"].validate(valid => {
        if (valid) {
          executeTransfer(this.executeForm.transferId, this.executeForm.productId, this.executeForm.qty).then(() => {
            this.$modal.msgSuccess("执行移库成功")
            this.executeOpen = false
            this.getList()
          })
        }
      })
    }
  }
}
</script>
