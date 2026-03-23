<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="盘点单号" prop="checkNo">
        <el-input
          v-model="queryParams.checkNo"
          placeholder="请输入盘点单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="状态" clearable>
          <el-option label="未开始" value="0" />
          <el-option label="盘点中" value="1" />
          <el-option label="已完成" value="2" />
          <el-option label="已审核" value="3" />
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
          v-hasPermi="['wms:stockCheck:add']"
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
          v-hasPermi="['wms:stockCheck:edit']"
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
          v-hasPermi="['wms:stockCheck:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['wms:stockCheck:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="stockCheckList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="盘点单号" align="center" prop="checkNo" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '0'" type="info">未开始</el-tag>
          <el-tag v-else-if="scope.row.status === '1'" type="warning">盘点中</el-tag>
          <el-tag v-else-if="scope.row.status === '2'" type="success">已完成</el-tag>
          <el-tag v-else-if="scope.row.status === '3'" type="success" effect="dark">已审核</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="盘点日期" align="center" prop="checkDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.checkDate, '{y}-{m}-{d}') }}</span>
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
            v-hasPermi="['wms:stockCheck:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['wms:stockCheck:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-circle-check"
            @click="handleStartCheck(scope.row)"
            v-hasPermi="['wms:stockCheck:check']"
          >开始盘点</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-complete"
            @click="handleFinishCheck(scope.row)"
            v-hasPermi="['wms:stockCheck:check']"
          >完成盘点</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-doc-copy"
            @click="handleApproveCheck(scope.row)"
            v-hasPermi="['wms:stockCheck:check']"
          >审核</el-button>
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

    <!-- 添加或修改盘点单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="盘点单号" prop="checkNo">
          <el-input v-model="form.checkNo" placeholder="请输入盘点单号" disabled />
        </el-form-item>
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
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listStockCheck, getStockCheck, delStockCheck, addStockCheck, updateStockCheck, exportStockCheck, startCheck, finishCheck, approveCheck } from "@/api/wms/stockCheck"
import { allWarehouse } from "@/api/wms/warehouse"

export default {
  name: "StockCheck",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      stockCheckList: [],
      open: false,
      title: "",
      warehouseOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        checkNo: undefined,
        status: undefined
      },
      form: {},
      rules: {
        checkNo: [
          { required: true, message: "盘点单号不能为空", trigger: "blur" }
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
      listStockCheck(this.queryParams).then(response => {
        this.stockCheckList = response.rows
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
        checkId: undefined,
        checkNo: undefined,
        warehouseId: undefined,
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
      this.title = "添加盘点单"
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.checkId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleUpdate(row) {
      this.reset()
      const checkId = row.checkId || this.ids
      getStockCheck(checkId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改盘点单"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.checkId != undefined) {
            updateStockCheck(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addStockCheck(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const checkIds = row.checkId || this.ids
      this.$modal.confirm('是否确认删除盘点单编号为"' + checkIds + '"的数据项？').then(function() {
        return delStockCheck(checkIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('wms/stockCheck/export', {
        ...this.queryParams
      }, `stockCheck_${new Date().getTime()}.xlsx`)
    },
    handleStartCheck(row) {
      this.$modal.confirm('确认开始盘点单 "' + row.checkNo + '" 吗？').then(() => {
        return startCheck(row.checkId)
      }).then(() => {
        this.$modal.msgSuccess("开始盘点成功")
        this.getList()
      }).catch(() => {})
    },
    handleFinishCheck(row) {
      this.$modal.confirm('确认完成盘点单 "' + row.checkNo + '" 吗？').then(() => {
        return finishCheck(row.checkId)
      }).then(() => {
        this.$modal.msgSuccess("完成盘点成功")
        this.getList()
      }).catch(() => {})
    },
    handleApproveCheck(row) {
      this.$modal.confirm('确认审核盘点单 "' + row.checkNo + '" 吗？').then(() => {
        return approveCheck(row.checkId)
      }).then(() => {
        this.$modal.msgSuccess("审核成功")
        this.getList()
      }).catch(() => {})
    }
  }
}
</script>
