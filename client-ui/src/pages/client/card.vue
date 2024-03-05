<template>
  <div>
    <div style="display: flex">
      <div style="margin: 2%">
        <el-table :data="list.serverTopList" style="width: 100%">
          <el-table-column prop="serverName" label="项目名" width="120">
            <template #default="scope">
              <div style="display: flex; align-items: center">
                <el-button type="text" plain @click="getServerList(scope.row)">{{ scope.row.serverName }}</el-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="120">
            <template #default="scope">
              <el-text type="success" v-if="scope.row.status">
                正常
              </el-text>
              <el-text type="warning" v-else>
                停机
              </el-text>
            </template>
          </el-table-column>
          <el-table-column prop="serverName" label="负载均衡" width="120">
            <template #default="scope">
              <div style="display: flex; align-items: center">
                <el-button type="text" plain @click="getServerBalance(scope.row)">随机</el-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="名字" width="120"></el-table-column>
          <el-table-column prop="desc" label="描述" width="120"></el-table-column>
          <el-table-column prop="edit" label="编辑" width="120">
            <template #default="scope">
              <div style="display: flex; align-items: center">
                <el-button type="text" plain @click="editDialog(scope.row)">编辑</el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div>
        <el-dialog v-model="dialog.editServer" :title="po.serverDetail.serverName">
          <el-form :model="po.serverDetail">
            <el-form-item label="昵称">
              <el-input v-model="po.serverDetail.name" />
            </el-form-item>
            <el-form-item label="描述">
              <el-input v-model="po.serverDetail.desc" />
            </el-form-item>
          </el-form>
          <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialog.editServer = false">Cancel</el-button>
        <el-button type="primary" @click="edit">
          Confirm
        </el-button>
      </span>
          </template>
        </el-dialog>
      </div>
      <div style="margin: 2%">
        <el-table :data="list.serverList" style="width: 100%">
          <el-table-column prop="name" label="名字" width="120">
            <template #default="scope">
              <div style="display: flex; align-items: center">
                <el-button type="text" plain @click="showServerDetail(scope.row)">{{ scope.row.name }}</el-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="ip" label="IP" width="120"></el-table-column>
          <el-table-column prop="port" label="端口号" width="120"></el-table-column>
          <el-table-column prop="weight" label="权重" width="120"></el-table-column>
          <el-table-column prop="area" label="区域" width="120"></el-table-column>
        </el-table>
      </div>
    </div>
    <div style="display: flex">
      <div style="margin: 2%">
        <el-table :data="po.serverPo.classItemList" style="width: 100%">
          <el-table-column prop="className" label="服务名" width="120">
            <template #default="scope">
              <div style="display: flex; align-items: center">
                <el-button type="text" plain @click="showMethodDetail(scope.row)">{{ scope.row.className }}</el-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="className" label="类名" width="120"></el-table-column>
          <el-table-column prop="classPath" label="类路径" width="120"></el-table-column>
          <el-table-column prop="classMark" label="类标识" width="120"></el-table-column>
        </el-table>
      </div>
      <div style="margin: 2%">
        <el-table :data="po.classItem.methodList" style="width: 100%">
          <el-table-column prop="methodName" label="方法" width="120">
            <template #default="scope">
              <div style="display: flex; align-items: center">
                <el-button type="text" plain @click="showMethod(scope.row, po.classItem)">
                  {{scope.row.methodName}}
                </el-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="deal" label="属性" width="120">
            <template #default="scope">
              <div style="display: flex; align-items: center" v-if="scope.row.methodArgsTypes.length > 0">
                {{scope.row.methodArgsTypes[0].name}}
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div style="width: 100%;margin: 2%">
        <el-descriptions :title="po.dealMethodPo.methodName">
          <el-descriptions-item label="ID">{{ po.dealMethodPo.id }}</el-descriptions-item>
          <el-descriptions-item label="TOKEN">{{ po.dealMethodPo.token }}</el-descriptions-item>
          <el-descriptions-item label="项目名">{{ po.dealMethodPo.serverName }}</el-descriptions-item>
          <el-descriptions-item label="类名">{{ po.dealMethodPo.className }}</el-descriptions-item>
          <el-descriptions-item label="方法名">
            <el-tag size="small">{{ po.dealMethodPo.methodName }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="版本">{{ po.dealMethodPo.version }}</el-descriptions-item>
          <el-descriptions-item v-if="po.dealMethodPo.result != null" label="结果ID">{{ po.dealMethodPo.result.id }}</el-descriptions-item>
          <el-descriptions-item v-if="po.dealMethodPo.result != null"  label="结果状态码">
            <el-tag v-if="po.dealMethodPo.result.code === 200" class="ml-2" type="success">{{ po.dealMethodPo.result.code }}</el-tag>
            <el-tag v-else class="ml-2" type="warning">{{ po.dealMethodPo.result.code }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item v-if="po.dealMethodPo.result != null"  label="结果消息">{{ po.dealMethodPo.result.message }}</el-descriptions-item>
          <el-descriptions-item v-if="po.dealMethodPo.result != null"  label="结果数据">{{ po.dealMethodPo.result.data }}</el-descriptions-item>
        </el-descriptions>
        <el-form :model="po.dealMethodPo">
          <el-form-item label="数据">
            <el-input type="textarea" v-model="po.dealMethodPo.data" placeholder="data" clearable :rows="6" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="dealMethod">执行</el-button>
          </el-form-item>
        </el-form>
<!--        <div>-->
<!--          <el-form :model="po.dealMethodPo">-->
<!--            <el-form-item v-for="(value, key) in po.dealMethodPo.jsonData" :key="key" :label="key">-->
<!--              <el-input v-model="po.dealMethodPo.jsonData[key]" placeholder="data" clearable :rows="6" />-->
<!--            </el-form-item>-->
<!--            <el-form-item>-->
<!--              <el-button type="primary" @click="dealMethod">执行</el-button>-->
<!--            </el-form-item>-->
<!--          </el-form>-->
<!--        </div>-->
      </div>
    </div>
  </div>
</template>

<script>
import {dealMethodApi, getServerBalanceApi, getServerListApi, getTopServerListApi, setServerDetailApi} from "./api";

export default {
  data() {
    return {
      list: {
        serverTopList: [],
        serverList: [],
      },
      po: {
        serverDetail: {
          serverName: null,
          name: null,
          desc: null
        },
        serverPo: {
          name: null,
          ip: null,
          port: null,
          weight: null,
          area: null,
          classItemList: []
        },
        classItem: {
          className: null,
          classPath: null,
          classMark: null,
          methodList: []
        },
        methodDetail: {
          methodName: null,
          methodArgsTypes: []
        },
        methodArgsType: {
          name: null,
          args: [],
          argsJson: null
        },
        dealMethodPo: {
          id: null,
          token: null,
          serverName: null,
          className: null,
          version: null,
          methodName: null,
          data: null,
          result: null,
          jsonData: {}
        }
      },
      dialog: {
        editServer: false
      }
    }
  },
  onLoad() {},
  mounted() {
    this.getTopServerList()
  },
  methods: {
    getTopServerList() {
      getTopServerListApi()
          .then(res => {
            this.list.serverTopList = res.data
          })
    },
    getServerList(row) {
      this.po.dealMethodPo.serverName = row.serverName
      getServerListApi(row.serverName)
          .then(res => {
            if (res.data.length === 0) {
              row.status = false
            } else {
              row.status = true
            }
            this.list.serverList = res.data
          })
    },
    getServerBalance(row) {
      this.po.dealMethodPo.serverName = row.serverName
      getServerBalanceApi(row.serverName)
          .then(res => {
            if (res.data.length === 0) {
              row.status = false
            } else {
              row.status = true
            }
            this.list.serverList = res.data
          })
    },
    editDialog(row) {
      this.dialog.editServer = true
      this.po.serverDetail = row
    },
    edit() {
      setServerDetailApi(this.po.serverDetail)
          .then(res => {
            this.$message.success("修改成功！")
          })
    },
    showServerDetail(row) {
      this.po.dealMethodPo.className = row.name
      this.po.serverPo = row
    },
    showMethod(method, row) {
      this.po.dealMethodPo.className = row.className
      this.po.dealMethodPo.methodName = method.methodName
      this.po.dealMethodPo.version = row.classMark
      console.log("method")
      console.log(method)
      if (method.methodArgsTypes.length !== 0) {
        let data = "{"
        console.log("method.methodArgsTypes[0]")
        console.log(method.methodArgsTypes[0])
        let args = method.methodArgsTypes[0].args
        if (args !== null) {
          args.forEach(e => {
            data += "\"" + e + "\"" + ": \"\","
          })
          data += "}"
          this.po.dealMethodPo.data = data
          console.log(eval('(' + method.methodArgsTypes[0].argsJson + ')'))
          this.po.dealMethodPo.data = method.methodArgsTypes[0].argsJson
          this.po.dealMethodPo.jsonData = eval('(' + method.methodArgsTypes[0].argsJson + ')')
          // console.log(JSON.parse(this.po.dealMethodPo.data))
          console.log(this.po.dealMethodPo.jsonData)
        } else {
          this.po.dealMethodPo.data = ""
        }
      } else {
        this.po.dealMethodPo.data = ""
      }

    },
    dealMethod() {
      // this.po.dealMethodPo.data = JSON.stringify(this.po.dealMethodPo.jsonData)
      let responseTime; // 声明一个变量来保存响应时间
      console.time('请求响应时间'); // 开始计时
      dealMethodApi(this.po.dealMethodPo)
          .then(res => {
            console.timeEnd('请求响应时间'); // 结束计时，并打印所需时间
            console.log("responseTime")
            this.po.dealMethodPo.result = res.data
          })
    },
    showMethodDetail(row) {
      this.po.classItem = row
    },
  },
}
</script>

<style>

</style>
