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
          <el-option label="草稿" value="0" />
          <el-option label="待审核" value="1" />
          <el-option label="待拣货" value="2" />
          <el-option label="拣货中" value="3" />
          <el-option label="待复核" value="4" />
          <el-option label="待打包" value="5" />
          <el-option label="待发货" value="6" />
          <el-option label="已发货" value="7" />
          <el-option label="已完成" value="8" />
          <el-option label="已取消" value="9" />
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
      <el-table-column label="出库单 ID" align="center" prop="outboundId" />
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
          <span v-if="scope.row.status == '0'">草稿</span>
          <span v-else-if="scope.row.status == '1'">待审核</span>
          <span v-else-if="scope.row.status == '2'">待拣货</span>
          <span v-else-if="scope.row.status == '3'">拣货中</span>
          <span v-else-if="scope.row.status == '4'">待复核</span>
          <span v-else-if="scope.row.status == '5'">待打包</span>
          <span v-else-if="scope.row.status == '6'">待发货</span>
          <span v-else-if="scope.row.status == '7'">已发货</span>
          <span v-else-if="scope.row.status == '8'">已完成</span>
          <span v-else-if="scope.row.status == '9'">已取消</span>
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
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['wms:outbound:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['wms:outbound:remove']"
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
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="出库单号" prop="outboundNo">
              <el-input v-model="form.outboundNo" placeholder="请输入出库单号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出库类型" prop="outboundType">
              <el-select v-model="form.outboundType" placeholder="请选择出库类型">
                <el-option label="销售出库" value="1" />
                <el-option label="采购退货" value="2" />
                <el-option label="调拨出库" value="3" />
                <el-option label="其他出库" value="4" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="订单编号" prop="orderNo">
              <el-input v-model="form.orderNo" placeholder="请输入订单编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户名称" prop="customerName">
              <el-input v-model="form.customerName" placeholder="请输入客户名称" />
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
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOutbound, getOutbound, delOutbound, addOutbound, updateOutbound, exportOutbound } from "@/api/wms/outbound"

export default {
  name: "Outbound",
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
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        outboundNo: undefined,
        outboundType: undefined,
        status: undefined
      },
      form: {},
      rules: {
        outboundNo: [
          { required: true, message: "出库单号不能为空", trigger: "blur" }
        ],
        outboundType: [
          { required: true, message: "出库类型不能为空", trigger: "change" }
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
      listOutbound(this.queryParams).then(response => {
        this.outboundList = response.rows
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
      this.title = "添加出库单"
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.outboundId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handleUpdate(row) {
      this.reset()
      const outboundId = row.outboundId || this.ids
      getOutbound(outboundId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改出库单"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
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
    handleExport() {
      this.download('wms/outbound/export', {
        ...this.queryParams
      }, `outbound_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
