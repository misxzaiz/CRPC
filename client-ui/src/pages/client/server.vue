<template>
  <div class="container">
    <div class="server-list">
      <el-table :data="list.serverTopList" style="width: 100%" stripe>
        <el-table-column prop="serverName" label="项目名" width="120">
          <template #default="scope">
            <div class="table-cell">
              <el-link type="primary" plain @click="getServerList(scope.row)">
                {{ scope.row.serverName }}
              </el-link>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="serverName" label="调用" width="60">
          <template #default="scope">
            <div class="table-cell">
              <el-link type="primary" plain @click="getServerUsed(scope.row)">
                查看
              </el-link>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <div class="table-cell">
              <el-text type="success" v-if="scope.row.status">正常</el-text>
              <el-text type="danger" v-else>停机</el-text>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="serverName" label="负载均衡" width="100">
          <template #default="scope">
            <div class="table-cell">
              <el-link type="primary" plain @click="getServerBalance(scope.row)">随机</el-link>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名字" min-width="120"></el-table-column>
        <el-table-column prop="desc" label="描述" min-width="120"></el-table-column>
        <el-table-column prop="edit" label="编辑" width="80">
          <template #default="scope">
            <div class="table-cell">
              <el-link type="primary" plain @click="editDialog(scope.row)">编辑</el-link>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <el-dialog v-model="dialog.editServer" :title="po.serverDetail.serverName">
        <el-form :model="po.serverDetail">
          <el-form-item label="昵称">
            <el-input v-model="po.serverDetail.name" />
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="po.serverDetail.desc" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialog.editServer = false">取消</el-button>
          <el-button type="primary" @click="edit">确认</el-button>
        </div>
      </el-dialog>

      <el-table :data="list.serverList" style="width: 100%" stripe>
        <el-table-column prop="name" label="服务名" min-width="120">
          <template #default="scope">
            <div class="table-cell">
              <el-link type="primary" plain @click="showServerDetail(scope.row)">
                {{ scope.row.name }}
              </el-link>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="ip" label="IP" min-width="100"></el-table-column>
        <el-table-column prop="port" label="端口号" width="80"></el-table-column>
        <el-table-column prop="weight" label="权重" width="80"></el-table-column>
        <el-table-column prop="area" label="区域" width="80"></el-table-column>
      </el-table>

      <el-table :data="po.serverPo.classList" style="width: 100%" stripe>
        <el-table-column prop="className" label="类名" min-width="100">
          <template #default="scope">
            <div class="table-cell">
              <el-link type="primary" plain @click="showMethodDetail(scope.row)">
                {{ scope.row.name }}
              </el-link>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="类路径" min-width="160"></el-table-column>
        <el-table-column prop="version" label="类标识" min-width="60"></el-table-column>
      </el-table>

      <el-table :data="po.classList.methodList" style="width: 100%" stripe>
        <el-table-column prop="name" label="方法" width="150">
          <template #default="scope">
            <div class="table-cell">
              <el-link type="primary" plain @click="showMethod(scope.row, po.classList)">
                {{ scope.row.name }}
              </el-link>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="deal" label="属性" width="120">
          <template #default="scope">
            <div class="table-cell" v-if="scope.row.parameterList.length > 0">
              {{ scope.row.parameterList[0].name }}
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="service-list">
      <div style="width: 100%; margin-top: 20px;">
        <div style="font-size: large; font-weight: bolder;margin-bottom: 1%">
          {{ po.dealMethodPo.serverName }} -
          {{ po.dealMethodPo.className }} -
          {{ po.dealMethodPo.methodName }} -
          {{ po.dealMethodPo.version }}
        </div>
        <div>
          <el-form :model="po.dealMethodPo" style="margin-top: 10px;">
            <el-form-item label="">
              <el-input
                  type="textarea"
                  v-model="po.dealMethodPo.data"
                  placeholder="data"
                  clearable
                  :rows="6"
                  size="small"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="dealMethod" size="small">执行</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div>
          <div>
            <el-descriptions title="" size="small">
              <el-descriptions-item v-if="po.dealMethodPo.result != null" label="结果状态码">
                <el-tag
                    v-if="po.dealMethodPo.result.code === 200"
                    class="ml-2"
                    type="success"
                    size="small"
                >
                  {{ po.dealMethodPo.result.code }}
                </el-tag>
                <el-tag v-else class="ml-2" type="danger" size="small">
                  {{ po.dealMethodPo.result.code }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item v-else label="结果状态码">
                <el-tag class="ml-2" type="success" size="small">0</el-tag>
              </el-descriptions-item>
              <el-descriptions-item v-if="po.dealMethodPo.result != null" label="结果消息">
                {{ po.dealMethodPo.result.message }}
              </el-descriptions-item>
              <el-descriptions-item v-else label="结果消息">0
              </el-descriptions-item>
            </el-descriptions>
          </div>
          <div label="结果数据">
            <el-input v-if="po.dealMethodPo.result != null" type="textarea" v-model="po.dealMethodPo.result.dataJson" placeholder="data" clearable :rows="6" size="small"/>
            <el-input v-else type="textarea" placeholder="data" clearable :rows="6" size="small"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  margin: 2%;
}

.server-list {
  width: 100%;
  margin-bottom: 20px;
}

.service-list {
  width: 100%;
}

.table-cell {
  display: flex;
  align-items: center;
}

@media screen and (min-width: 768px) {
  .server-list {
    width: 45%;
    margin-bottom: 0;
  }

  .service-list {
    width: 50%;
  }
}


</style>


<script>
import {getServerBalanceApi, getServerListApi, getTopServerListApi, setServerDetailApi} from "./api";
import {dealMethodApi} from "../../common/crpc";
import {getServerUsedApi} from "./interface";

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
          classList: []
        },
        classList: {
          name: null,
          path: null,
          version: null,
          methodList: []
        },
        methodDetail: {
          name: null,
          parameterList: []
        },
        parameterList: {
          name: null,
          parameterList: [],
          parameterJson: null
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
    getServerUsed(row) {
      this.po.dealMethodPo.serverName = row.serverName
      getServerUsedApi(row.serverName)
          .then(res => {
            row.status = res.data.data.length !== 0;
            this.list.serverList = res.data.data
          })
    },
    getTopServerList() {
      getTopServerListApi()
          .then(res => {
            this.list.serverTopList = res.data.data
          })
    },
    getServerList(row) {
      this.po.dealMethodPo.serverName = row.serverName
      getServerListApi(row.serverName)
          .then(res => {
            row.status = res.data.data.length !== 0;
            this.list.serverList = res.data.data
          })
    },
    getServerBalance(row) {
      this.po.dealMethodPo.serverName = row.serverName
      getServerBalanceApi(row.serverName)
          .then(res => {
            row.status = res.data.data.length !== 0;
            this.list.serverList = res.data.data
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
      this.po.dealMethodPo.name = row.name
      this.po.serverPo = row
    },
    showMethod(method, row) {
      this.po.dealMethodPo.className = row.name
      this.po.dealMethodPo.methodName = method.name
      this.po.dealMethodPo.version = row.version
      if (method.parameterList.length !== 0) {
        let args = method.parameterList[0].parameterList
        if (args !== null) {
          this.po.dealMethodPo.data = method.parameterList[0].parameterJson
          this.po.dealMethodPo.jsonData = eval('(' + method.parameterList[0].parameterJson + ')')
          console.log(this.po.dealMethodPo.jsonData)
        } else {
          this.po.dealMethodPo.data = ""
        }
      } else {
        this.po.dealMethodPo.data = ""
      }

    },
    dealMethod() {
      this.po.dealMethodPo.token = uni.getStorageSync('token')
      dealMethodApi(this.po.dealMethodPo)
          .then(res => {
            this.po.dealMethodPo.result = res.data
            this.po.dealMethodPo.result.dataJson = JSON.stringify(res.data.data, null, 2).replace(/\\n/g, '\n')
          })
    },
    showMethodDetail(row) {
      this.po.dealMethodPo.className = row.name
      this.po.dealMethodPo.version = row.version
      this.po.classList = row
    },
  },
}
</script>

