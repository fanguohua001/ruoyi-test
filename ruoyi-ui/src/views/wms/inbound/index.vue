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
          <el-option label="草稿" value="0" />
          <el-option label="待收货" value="1" />
          <el-option label="收货中" value="2" />
          <el-option label="待质检" value="3" />
          <el-option label="待上架" value="4" />
          <el-option label="已完成" value="5" />
          <el-option label="已取消" value="6" />
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
      <el-table-column label="入库单 ID" align="center" prop="inboundId" />
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
          <span v-if="scope.row.status == '0'">草稿</span>
          <span v-else-if="scope.row.status == '1'">待收货</span>
          <span v-else-if="scope.row.status == '2'">收货中</span>
          <span v-else-if="scope.row.status == '3'">待质检</span>
          <span v-else-if="scope.row.status == '4'">待上架</span>
          <span v-else-if="scope.row.status == '5'">已完成</span>
          <span v-else-if="scope.row.status == '6'">已取消</span>
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
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['wms:inbound:edit']"
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
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="入库单号" prop="inboundNo">
              <el-input v-model="form.inboundNo" placeholder="请输入入库单号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入库类型" prop="inboundType">
              <el-select v-model="form.inboundType" placeholder="请选择入库类型">
                <el-option label="采购入库" value="1" />
                <el-option label="退货入库" value="2" />
                <el-option label="调拨入库" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="供应商 ID" prop="supplierId">
              <el-input-number v-model="form.supplierId" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商名称" prop="supplierName">
              <el-input v-model="form.supplierName" placeholder="请输入供应商名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="仓库 ID" prop="warehouseId">
              <el-input-number v-model="form.warehouseId" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预计到货日期" prop="expectedDate">
              <el-date-picker v-model="form.expectedDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 100%"/>
            </el-form-item>
          </el-col>
        </el-row>
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
import { listInbound, getInbound, delInbound, addInbound, updateInbound, exportInbound } from "@/api/wms/inbound"

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
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        inboundNo: undefined,
        inboundType: undefined,
        status: undefined
      },
      form: {},
      rules: {
        inboundNo: [
          { required: true, message: "入库单号不能为空", trigger: "blur" }
        ],
        inboundType: [
          { required: true, message: "入库类型不能为空", trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getList()
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
        remark: undefined
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
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.inboundId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handleUpdate(row) {
      this.reset()
      const inboundId = row.inboundId || this.ids
      getInbound(inboundId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改入库单"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.inboundId != undefined) {
            updateInbound(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addInbound(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
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
