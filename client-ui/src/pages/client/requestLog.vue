<template>
  <div class="container">
    <div v-for="items in list">
      <el-table :data="items" style="width: 100%;margin-bottom: 2%">
        <el-table-column type="expand">
          <template #default="props">
            <div style="margin: 0 3% 0 3%">
              <div style="width: 100%;margin-bottom: 2%">
                <el-descriptions title="请求数据">
                  <el-descriptions-item label="ID">{{props.row.id}}</el-descriptions-item>
                  <el-descriptions-item label="TOKEN">{{props.row.token}}</el-descriptions-item>
                  <el-descriptions-item label="服务名">{{props.row.serverName}}</el-descriptions-item>
                  <el-descriptions-item label="类名">{{props.row.className}}</el-descriptions-item>
                  <el-descriptions-item label="方法名">{{props.row.methodName}}</el-descriptions-item>
                  <el-descriptions-item label="版本">{{props.row.version}}</el-descriptions-item>
                  <el-descriptions-item label="地区">{{props.row.area}}</el-descriptions-item>
                  <el-descriptions-item label="开始时间">{{props.row.startTime}}</el-descriptions-item>
                  <el-descriptions-item label="结束时间">{{props.row.endTime}}</el-descriptions-item>
                  <el-descriptions-item label="花费时间">{{props.row.spendTime}} ms</el-descriptions-item>
                  <el-descriptions-item label="请求参数">{{props.row.data}}</el-descriptions-item>
                </el-descriptions>
              </div>
              <div style="width: 100%;margin-bottom: 2%">
                <el-descriptions title="响应结果">
                  <el-descriptions-item label="ID">{{props.row.rpcResponse.id}}</el-descriptions-item>
                  <el-descriptions-item label="Code">
                    <el-tag
                        v-if="props.row.rpcResponse.code === 200"
                        class="ml-2"
                        type="success"
                        size="small"
                    >
                      {{ props.row.rpcResponse.code }}
                    </el-tag>
                    <el-tag v-else class="ml-2" type="danger" size="small">
                      {{ props.row.rpcResponse.code }}
                    </el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item label="消息">{{props.row.rpcResponse.message}}</el-descriptions-item>
                  <el-descriptions-item label="数据">{{props.row.rpcResponse.data}}</el-descriptions-item>
                </el-descriptions>
              </div>
              <div style="width: 100%;margin-bottom: 2%" v-if="props.row.e">
                <el-descriptions title="异常信息">
                  <el-descriptions-item label="数据">
                    <div v-for="item in props.row.e">
                      {{item}}
                    </div>
                  </el-descriptions-item>
                </el-descriptions>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column sortable prop="id" label="id" width="180" />
        <el-table-column sortable prop="token" label="token" width="180" />
        <el-table-column sortable prop="serverName" label="serverName" width="180" />
        <el-table-column sortable prop="className" label="className" width="180" />
        <el-table-column sortable prop="methodName" label="methodName" width="180" />
        <el-table-column sortable prop="version" label="version" width="100" />
        <el-table-column sortable prop="area" label="area" width="100" />
        <el-table-column sortable prop="startTime" label="startTime" width="220" />
        <el-table-column sortable prop="endTime" label="endTime" width="220" />
        <el-table-column sortable prop="spendTime" label="spendTime/ms" width="180" />
      </el-table>
    </div>

  </div>
</template>

<script>

import {getRequestLogsApi} from "./api";

export default {
  data() {
    return {
      list: null
    }
  },
  onLoad() {},
  mounted() {
    this.getRequestLogs()
  },
  methods: {
    getRequestLogs() {
      getRequestLogsApi().then(res => {
        this.list = res.data.data
      })
    }
  },
}
</script>

<style scoped>



</style>