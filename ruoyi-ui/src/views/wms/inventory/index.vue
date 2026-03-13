<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="商品编码" prop="productCode">
        <el-input
          v-model="queryParams.productCode"
          placeholder="请输入商品编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品名称" prop="productName">
        <el-input
          v-model="queryParams.productName"
          placeholder="请输入商品名称"
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
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="状态" clearable>
          <el-option label="正常" value="1" />
          <el-option label="锁定" value="2" />
          <el-option label="冻结" value="3" />
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
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['wms:inventory:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="inventoryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="库存 ID" align="center" prop="inventoryId" />
      <el-table-column label="商品编码" align="center" prop="productCode" />
      <el-table-column label="商品名称" align="center" prop="productName" :show-overflow-tooltip="true" />
      <el-table-column label="批次号" align="center" prop="batchNo" />
      <el-table-column label="有效期至" align="center" prop="expiryDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expiryDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="现有数量" align="center" prop="qtyOnHand" />
      <el-table-column label="可用数量" align="center" prop="qtyAvailable" />
      <el-table-column label="锁定数量" align="center" prop="qtyLocked" />
      <el-table-column label="单位成本" align="center" prop="unitCost" />
      <el-table-column label="总成本" align="center" prop="totalCost" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <span v-if="scope.row.status == '1'">正常</span>
          <span v-else-if="scope.row.status == '2'">锁定</span>
          <span v-else-if="scope.row.status == '3'">冻结</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status == '1'"
            size="mini"
            type="text"
            icon="el-icon-lock"
            @click="handleFreeze(scope.row)"
            v-hasPermi="['wms:inventory:freeze']"
          >冻结</el-button>
          <el-button
            v-else-if="scope.row.status == '3'"
            size="mini"
            type="text"
            icon="el-icon-unlock"
            @click="handleUnfreeze(scope.row)"
            v-hasPermi="['wms:inventory:unfreeze']"
          >解冻</el-button>
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
  </div>
</template>

<script>
import { listInventory, freezeStock, unfreezeStock, exportInventory } from "@/api/wms/inventory"

export default {
  name: "Inventory",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      inventoryList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        productCode: undefined,
        productName: undefined,
        batchNo: undefined,
        status: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listInventory(this.queryParams).then(response => {
        this.inventoryList = response.rows
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
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.inventoryId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handleFreeze(row) {
      this.$modal.confirm('是否确认冻结该库存？').then(() => {
        return freezeStock(row.inventoryId)
      }).then(() => {
        this.$modal.msgSuccess("冻结成功")
        this.getList()
      }).catch(() => {})
    },
    handleUnfreeze(row) {
      this.$modal.confirm('是否确认解冻该库存？').then(() => {
        return unfreezeStock(row.inventoryId)
      }).then(() => {
        this.$modal.msgSuccess("解冻成功")
        this.getList()
      }).catch(() => {})
    },
    handleExport() {
      this.download('wms/inventory/export', {
        ...this.queryParams
      }, `inventory_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
