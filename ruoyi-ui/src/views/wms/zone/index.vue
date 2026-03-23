<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="库区编码" prop="zoneCode">
        <el-input
          v-model="queryParams.zoneCode"
          placeholder="请输入库区编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="库区名称" prop="zoneName">
        <el-input
          v-model="queryParams.zoneName"
          placeholder="请输入库区名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
      <el-form-item label="库区类型" prop="zoneType">
        <el-select v-model="queryParams.zoneType" placeholder="请选择库区类型" clearable>
          <el-option label="存储区" value="1" />
          <el-option label="拣货区" value="2" />
          <el-option label="收货区" value="3" />
          <el-option label="发货区" value="4" />
          <el-option label="退货区" value="5" />
          <el-option label="质检区" value="6" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
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
          v-hasPermi="['wms:zone:add']"
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
          v-hasPermi="['wms:zone:edit']"
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
          v-hasPermi="['wms:zone:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['wms:zone:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="zoneList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="库区编码" align="center" prop="zoneCode" />
      <el-table-column label="库区名称" align="center" prop="zoneName" :show-overflow-tooltip="true" />
      <el-table-column label="仓库名称" align="center" prop="warehouseName" :show-overflow-tooltip="true" />
      <el-table-column label="库区类型" align="center" prop="zoneType">
        <template slot-scope="scope">
          <span v-if="scope.row.zoneType == '1'">存储区</span>
          <span v-else-if="scope.row.zoneType == '2'">拣货区</span>
          <span v-else-if="scope.row.zoneType == '3'">收货区</span>
          <span v-else-if="scope.row.zoneType == '4'">发货区</span>
          <span v-else-if="scope.row.zoneType == '5'">退货区</span>
          <span v-else-if="scope.row.zoneType == '6'">质检区</span>
        </template>
      </el-table-column>
      <el-table-column label="顺序号" align="center" prop="orderNum" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['wms:zone:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['wms:zone:remove']"
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

    <!-- 添加或修改库区对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="库区编码" prop="zoneCode">
              <el-input v-model="form.zoneCode" placeholder="请输入库区编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="库区名称" prop="zoneName">
              <el-input v-model="form.zoneName" placeholder="请输入库区名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="仓库" prop="warehouseId">
              <el-select v-model="form.warehouseId" placeholder="请选择仓库" style="width: 100%" @change="handleWarehouseChange" clearable>
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
            <el-form-item label="库区类型" prop="zoneType">
              <el-select v-model="form.zoneType" placeholder="请选择库区类型">
                <el-option label="存储区" value="1" />
                <el-option label="拣货区" value="2" />
                <el-option label="收货区" value="3" />
                <el-option label="发货区" value="4" />
                <el-option label="退货区" value="5" />
                <el-option label="质检区" value="6" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="顺序号" prop="orderNum">
              <el-input-number v-model="form.orderNum" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
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
import { listZone, getZone, delZone, addZone, updateZone, exportZone } from "@/api/wms/zone"
import { allWarehouse } from "@/api/wms/warehouse"

export default {
  name: "WarehouseZone",
  dicts: ['sys_normal_disable'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      zoneList: [],
      open: false,
      title: "",
      warehouseOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        zoneCode: undefined,
        zoneName: undefined,
        warehouseId: undefined,
        zoneType: undefined,
        status: undefined
      },
      form: {},
      rules: {
        zoneCode: [
          { required: true, message: "库区编码不能为空", trigger: "blur" }
        ],
        zoneName: [
          { required: true, message: "库区名称不能为空", trigger: "blur" }
        ],
        warehouseId: [
          { required: true, message: "请选择仓库", trigger: "change" }
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
      listZone(this.queryParams).then(response => {
        this.zoneList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getWarehouseList() {
      allWarehouse().then(response => {
        console.log('仓库 API 响应:', response)
        // 若依框架返回格式：AjaxResult 返回 { code: 200, msg: '操作成功', data: [...] }
        const list = response.data || []
        // 确保 warehouseId 转换为数字类型
        this.warehouseOptions = list.map(item => ({
          warehouseId: Number(item.warehouseId),
          warehouseName: item.warehouseName
        }))
        console.log('仓库列表:', this.warehouseOptions)
      }).catch(error => {
        console.error('获取仓库列表失败:', error)
        this.warehouseOptions = []
      })
    },
    handleWarehouseChange(warehouseId) {
      // 仓库变更时的处理逻辑
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        zoneId: undefined,
        zoneCode: undefined,
        zoneName: undefined,
        warehouseId: undefined,
        zoneType: "1",
        orderNum: 0,
        status: "1",
        remark: undefined
      }
      this.resetForm("form")
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.dateRange = []
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
      this.title = "添加库区"
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.zoneId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handleUpdate(row) {
      this.reset()
      const zoneId = row.zoneId || this.ids
      // 确保仓库列表已加载
      if (this.warehouseOptions.length === 0) {
        this.getWarehouseList()
      }
      getZone(zoneId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改库区"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.zoneId != undefined) {
            updateZone(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addZone(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const zoneIds = row.zoneId || this.ids
      this.$modal.confirm('是否确认删除库区编号为"' + zoneIds + '"的数据项？').then(function() {
        return delZone(zoneIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('wms/zone/export', {
        ...this.queryParams
      }, `zone_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
