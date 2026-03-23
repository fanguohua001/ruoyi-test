<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="预警类型" prop="alertType">
        <el-select v-model="queryParams.alertType" placeholder="预警类型" clearable>
          <el-option label="低于安全库存" value="1" />
          <el-option label="高于最高库存" value="2" />
          <el-option label="近效期" value="3" />
          <el-option label="已过期" value="4" />
          <el-option label="呆滞" value="5" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="状态" clearable>
          <el-option label="未处理" value="0" />
          <el-option label="已处理" value="1" />
          <el-option label="已忽略" value="2" />
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
          icon="el-icon-refresh"
          size="mini"
          @click="handleGenerate"
          v-hasPermi="['wms:alert:generate']"
        >生成预警</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['wms:alert:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="alertList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="物料编码" align="center" prop="productCode" />
      <el-table-column label="物料名称" align="center" prop="productName" :show-overflow-tooltip="true" />
      <el-table-column label="预警类型" align="center" prop="alertType">
        <template slot-scope="scope">
          <span v-if="scope.row.alertType == '1'">低于安全库存</span>
          <span v-else-if="scope.row.alertType == '2'">高于最高库存</span>
          <span v-else-if="scope.row.alertType == '3'">近效期</span>
          <span v-else-if="scope.row.alertType == '4'">已过期</span>
          <span v-else-if="scope.row.alertType == '5'">呆滞</span>
        </template>
      </el-table-column>
      <el-table-column label="预警级别" align="center" prop="alertLevel">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.alertLevel == '1'" type="info">提示</el-tag>
          <el-tag v-else-if="scope.row.alertLevel == '2'" type="warning">警告</el-tag>
          <el-tag v-else-if="scope.row.alertLevel == '3'" type="danger">严重</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="当前库存" align="center" prop="qtyOnHand" />
      <el-table-column label="安全库存" align="center" prop="safetyStock" />
      <el-table-column label="有效期至" align="center" prop="expiryDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expiryDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="距离过期天数" align="center" prop="daysUntilExpiry" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <span v-if="scope.row.status == '0'">未处理</span>
          <span v-else-if="scope.row.status == '1'">已处理</span>
          <span v-else-if="scope.row.status == '2'">已忽略</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status == '0'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleHandle(scope.row)"
            v-hasPermi="['wms:alert:handle']"
          >处理</el-button>
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

    <!-- 处理预警对话框 -->
    <el-dialog title="处理预警" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="处理备注">
          <el-input v-model="form.handleRemark" type="textarea" placeholder="请输入处理备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitHandle">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAlert, handleAlert, generateAlerts, exportAlert } from "@/api/wms/alert"

export default {
  name: "Alert",
  data() {
    return {
      loading: true,
      ids: [],
      showSearch: true,
      total: 0,
      alertList: [],
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        alertType: undefined,
        status: undefined
      },
      form: {
        alertId: undefined,
        handleRemark: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listAlert(this.queryParams).then(response => {
        this.alertList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.form.handleRemark = undefined
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.alertId)
    },
    handleGenerate() {
      this.$modal.confirm('是否生成库存预警？').then(() => {
        return generateAlerts()
      }).then(() => {
        this.$modal.msgSuccess("生成成功")
        this.getList()
      }).catch(() => {})
    },
    handleHandle(row) {
      this.form.alertId = row.alertId
      this.open = true
    },
    submitHandle() {
      handleAlert(this.form.alertId, this.form.handleRemark).then(() => {
        this.$modal.msgSuccess("处理成功")
        this.open = false
        this.getList()
      })
    },
    handleExport() {
      this.download('wms/alert/export', {
        ...this.queryParams
      }, `alert_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
