<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="库位编码" prop="locationCode">
        <el-input
          v-model="queryParams.locationCode"
          placeholder="请输入库位编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="库位名称" prop="locationName">
        <el-input
          v-model="queryParams.locationName"
          placeholder="请输入库位名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="库位类型" prop="locationType">
        <el-select v-model="queryParams.locationType" placeholder="库位类型" clearable>
          <el-option label="存储位" value="1" />
          <el-option label="拣货位" value="2" />
          <el-option label="暂存位" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="仓库" prop="warehouseId">
        <el-select v-model="queryParams.warehouseId" placeholder="请选择仓库" clearable @change="handleQuery">
          <el-option
            v-for="item in warehouseOptions"
            :key="item.warehouseId"
            :label="item.warehouseName"
            :value="item.warehouseId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="状态" clearable>
          <el-option label="正常" value="0" />
          <el-option label="停用" value="1" />
          <el-option label="占用" value="2" />
          <el-option label="锁定" value="3" />
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
          v-hasPermi="['wms:location:add']"
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
          v-hasPermi="['wms:location:edit']"
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
          v-hasPermi="['wms:location:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['wms:location:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="locationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="库位编码" align="center" prop="locationCode" />
      <el-table-column label="库位名称" align="center" prop="locationName" :show-overflow-tooltip="true" />
      <el-table-column label="仓库名称" align="center" prop="warehouseName" :show-overflow-tooltip="true" />
      <el-table-column label="库区名称" align="center" prop="zoneName" :show-overflow-tooltip="true" />
      <el-table-column label="库位类型" align="center" prop="locationType">
        <template slot-scope="scope">
          <span v-if="scope.row.locationType == '1'">存储位</span>
          <span v-else-if="scope.row.locationType == '2'">拣货位</span>
          <span v-else-if="scope.row.locationType == '3'">暂存位</span>
        </template>
      </el-table-column>
      <el-table-column label="区域编码" align="center" prop="areaCode" />
      <el-table-column label="行号" align="center" prop="rowNo" />
      <el-table-column label="列号" align="center" prop="columnNo" />
      <el-table-column label="层号" align="center" prop="levelNo" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <span v-if="scope.row.status == '0'">正常</span>
          <span v-else-if="scope.row.status == '1'">停用</span>
          <span v-else-if="scope.row.status == '2'">占用</span>
          <span v-else-if="scope.row.status == '3'">锁定</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['wms:location:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['wms:location:remove']"
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

    <!-- 添加或修改库位对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="库位编码" prop="locationCode">
              <el-input v-model="form.locationCode" placeholder="请输入库位编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="库位名称" prop="locationName">
              <el-input v-model="form.locationName" placeholder="请输入库位名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="仓库" prop="warehouseId">
              <el-select v-model="form.warehouseId" placeholder="请选择仓库" style="width: 100%" @change="handleWarehouseChange">
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
            <el-form-item label="库区" prop="zoneId">
              <el-select v-model="form.zoneId" placeholder="请选择库区" style="width: 100%">
                <el-option
                  v-for="item in zoneOptions"
                  :key="item.zoneId"
                  :label="item.zoneName"
                  :value="item.zoneId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="库位类型" prop="locationType">
              <el-select v-model="form.locationType" placeholder="请选择库位类型">
                <el-option label="存储位" value="1" />
                <el-option label="拣货位" value="2" />
                <el-option label="暂存位" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态">
                <el-option label="正常" value="0" />
                <el-option label="停用" value="1" />
                <el-option label="占用" value="2" />
                <el-option label="锁定" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="区域编码" prop="areaCode">
              <el-input v-model="form.areaCode" placeholder="请输入区域编码" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="行号" prop="rowNo">
              <el-input v-model="form.rowNo" placeholder="请输入行号" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="列号" prop="columnNo">
              <el-input v-model="form.columnNo" placeholder="请输入列号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="层号" prop="levelNo">
              <el-input v-model="form.levelNo" placeholder="请输入层号" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="最大承重" prop="maxWeight">
              <el-input-number v-model="form.maxWeight" :min="0" :precision="2" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="最大体积" prop="maxVolume">
              <el-input-number v-model="form.maxVolume" :min="0" :precision="2" />
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
import { listLocation, getLocation, delLocation, addLocation, updateLocation, exportLocation } from "@/api/wms/location"
import { allWarehouse } from "@/api/wms/warehouse"
import { listZoneByWarehouse } from "@/api/wms/zone"

export default {
  name: "Location",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      locationList: [],
      open: false,
      title: "",
      warehouseOptions: [],
      zoneOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        locationCode: undefined,
        locationName: undefined,
        warehouseId: undefined,
        zoneId: undefined,
        locationType: undefined,
        areaCode: undefined,
        status: undefined
      },
      form: {},
      rules: {
        locationCode: [
          { required: true, message: "库位编码不能为空", trigger: "blur" }
        ],
        locationName: [
          { required: true, message: "库位名称不能为空", trigger: "blur" }
        ],
        warehouseId: [
          { required: true, message: "请选择仓库", trigger: "change" }
        ],
        zoneId: [
          { required: true, message: "请选择库区", trigger: "change" }
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
      listLocation(this.queryParams).then(response => {
        this.locationList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getWarehouseList() {
      allWarehouse().then(response => {
        this.warehouseOptions = response.data
      })
    },
    getZoneList(warehouseId) {
      listZoneByWarehouse(warehouseId).then(response => {
        this.zoneOptions = response.data
      })
    },
    handleWarehouseChange(warehouseId) {
      if (warehouseId) {
        this.getZoneList(warehouseId)
      } else {
        this.zoneOptions = []
      }
      this.form.zoneId = undefined
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        locationId: undefined,
        locationCode: undefined,
        locationName: undefined,
        warehouseId: undefined,
        zoneId: undefined,
        locationType: "1",
        areaCode: undefined,
        rowNo: undefined,
        columnNo: undefined,
        levelNo: undefined,
        maxWeight: undefined,
        maxVolume: undefined,
        status: "0",
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
      // 确保仓库列表已加载
      if (this.warehouseOptions.length === 0) {
        this.getWarehouseList()
      }
      this.open = true
      this.title = "添加库位"
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.locationId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handleUpdate(row) {
      this.reset()
      const locationId = row.locationId || this.ids
      getLocation(locationId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改库位"
        // 根据仓库 ID 加载库区列表
        if (this.form.warehouseId) {
          this.getZoneList(this.form.warehouseId)
        }
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.locationId != undefined) {
            updateLocation(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addLocation(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const locationIds = row.locationId || this.ids
      this.$modal.confirm('是否确认删除库位编号为"' + locationIds + '"的数据项？').then(function() {
        return delLocation(locationIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('wms/location/export', {
        ...this.queryParams
      }, `location_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
