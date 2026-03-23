<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="交易编号" prop="transactionNo">
        <el-input
          v-model="queryParams.transactionNo"
          placeholder="请输入交易编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="交易类型" prop="transactionType">
        <el-select v-model="queryParams.transactionType" placeholder="交易类型" clearable>
          <el-option label="入库" value="1" />
          <el-option label="出库" value="2" />
          <el-option label="盘点调整" value="3" />
          <el-option label="移库" value="4" />
          <el-option label="冻结/解冻" value="5" />
        </el-select>
      </el-form-item>
      <el-form-item label="物料编码" prop="productCode">
        <el-input
          v-model="queryParams.productCode"
          placeholder="请输入物料编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物料名称" prop="productName">
        <el-input
          v-model="queryParams.productName"
          placeholder="请输入物料名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="批次号" prop="batchNo">
        <el-input
          v-model="queryParams.batchNo"
          placeholder="请输入批次号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联单号" prop="referenceNo">
        <el-input
          v-model="queryParams.referenceNo"
          placeholder="请输入关联单号"
          clearable
          @keyup.enter.native="handleQuery"
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
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['wms:inventoryLedger:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="ledgerList" max-height="600">
      <el-table-column label="交易编号" align="center" prop="transactionNo" width="150" />
      <el-table-column label="交易类型" align="center" prop="transactionType" width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.transactionType == '1'">入库</span>
          <span v-else-if="scope.row.transactionType == '2'">出库</span>
          <span v-else-if="scope.row.transactionType == '3'">盘点调整</span>
          <span v-else-if="scope.row.transactionType == '4'">移库</span>
          <span v-else-if="scope.row.transactionType == '5'">冻结/解冻</span>
        </template>
      </el-table-column>
      <el-table-column label="物料编码" align="center" prop="productCode" width="120" />
      <el-table-column label="物料名称" align="center" prop="productName" width="150" />
      <el-table-column label="批次号" align="center" prop="batchNo" width="100" />
      <el-table-column label="变动前数量" align="center" prop="qtyBefore" width="100" />
      <el-table-column label="变动数量" align="center" prop="qtyChange" width="100">
        <template slot-scope="scope">
          <span :style="{ color: scope.row.qtyChange >= 0 ? 'green' : 'red' }">
            {{ scope.row.qtyChange >= 0 ? '+' : '' }}{{ scope.row.qtyChange }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="变动后数量" align="center" prop="qtyAfter" width="100" />
      <el-table-column label="关联单据号" align="center" prop="referenceNo" width="150" />
      <el-table-column label="交易时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" width="150" :show-overflow-tooltip="true" />
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { listLedger, exportLedger } from "@/api/wms/inventoryLedger"

export default {
  name: "InventoryLedger",
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      ledgerList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        transactionNo: undefined,
        transactionType: undefined,
        productCode: undefined,
        productName: undefined,
        batchNo: undefined,
        referenceNo: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listLedger(this.queryParams).then(response => {
        this.ledgerList = response.rows
        this.total = response.total
        this.loading = false
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
    handleExport() {
      this.download('wms/inventoryLedger/export', {
        ...this.queryParams
      }, `inventoryLedger_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
