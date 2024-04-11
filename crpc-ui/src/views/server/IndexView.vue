<template>
  <div class="container">
    <div class="server-list">
      <el-table :data="searchServerTopListFilter" style="width: 100%" stripe height="250">
        <el-table-column sortable prop="serverName" label="服务名" width="200">
          <template #header>
            <el-input v-model="searchServerTopList" style="width: 80%" size="small" placeholder="Type to search" />
          </template>
          <template #default="scope">
            <el-popover trigger="hover" placement="top" width="auto">
              <template #default>
                <div>名称: {{ scope.row.name }}</div>
                <div>描述: {{ scope.row.desc }}</div>
              </template>
              <template #reference>
                <el-link type="primary" plain>
                  {{ scope.row.serverName }}
                </el-link>
              </template>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="列表" width="80">
          <template #default="scope">
            <div class="table-cell">
              <el-link v-if="scope.row.status" type="primary" plain @click="getServerList(scope.row)">
                查看
              </el-link>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="调用" width="80">
          <template #default="scope">
            <div class="table-cell">
              <el-link v-if="scope.row.status" type="primary" plain @click="getServerUsed(scope.row)">
                查看
              </el-link>
            </div>
          </template>
        </el-table-column>
        <el-table-column sortable prop="status" label="状态" width="80">
          <template #default="scope">
            <div class="table-cell">
              <el-text type="success" v-if="scope.row.status">正常</el-text>
              <el-text type="danger" v-else>停机</el-text>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="负载均衡" width="120">
          <template #default="scope">
            <div class="table-cell">
              <el-link type="primary" plain @click="getServerBalance(scope.row)">随机</el-link>
            </div>
          </template>
        </el-table-column>
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

      <el-table :data="searchServerListFilter" style="width: 100%;margin-top: 2%" stripe height="250">
        <el-table-column sortable prop="name" label="服务名" min-width="150">
          <template #header>
            <el-input v-model="searchServerList" style="width: 80%" size="small" placeholder="Type to search" />
          </template>
          <template #default="scope">
            <div class="table-cell">
              <el-link type="primary" plain @click="showServerDetail(scope.row)">
                {{ scope.row.name }}
              </el-link>
            </div>
          </template>
        </el-table-column>
        <el-table-column sortable prop="ip" label="IP" min-width="90"></el-table-column>
        <el-table-column sortable prop="port" label="端口号" width="90"></el-table-column>
        <el-table-column sortable prop="weight" label="权重" width="90"></el-table-column>
        <el-table-column sortable prop="area" label="区域" width="90"></el-table-column>
      </el-table>

      <el-table :data="searchClassListFilter" style="width: 100%;margin-top: 2%" stripe height="250">
        <el-table-column sortable prop="name" label="类名" min-width="150">
          <template #header>
            <el-input v-model="searchClassList" style="width: 80%" size="small" placeholder="Type to search" />
          </template>
          <template #default="scope">
            <div class="table-cell">
              <el-link type="primary" plain @click="showMethodDetail(scope.row)">
                {{ scope.row.name }}
              </el-link>
            </div>
          </template>
        </el-table-column>
        <el-table-column sortable prop="path" label="类路径" min-width="160"></el-table-column>
        <el-table-column sortable prop="version" label="类标识" min-width="60"></el-table-column>
      </el-table>
    </div>

    <div class="service-list">
      <div>
        <el-table :data="searchMethodListFilter" style="width: 100%" stripe height="250">
          <el-table-column sortable prop="name" label="方法" width="150">
            <template #header>
              <el-input v-model="searchMethodList" style="width: 80%" size="small" placeholder="Type to search" />
            </template>
            <template #default="scope">
              <div class="table-cell">
                <el-link type="primary" plain @click="showMethod(scope.row, po.classList)">
                  {{ scope.row.name }}
                </el-link>
              </div>
            </template>
          </el-table-column>
          <el-table-column sortable prop="deal" label="属性" width="120">
            <template #default="scope">
              <div class="table-cell" v-if="scope.row.parameterList.length > 0">
                {{ scope.row.parameterList[0].name }}
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
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

<script setup>
import {computed, onMounted} from "vue";
import {
  dialog,
  list,
  po,
  searchClassList,
  searchMethodList,
  searchServerList,
  searchServerTopList
} from "@/views/server/model.js";
import {
  getServerBalanceApi,
  getServerListApi,
  getServerUsedApi,
  getTopServerListApi,
  setServerDetailApi
} from "@/views/server/api.js";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";
import {dealMethodApi} from "@/common/common.js";

function getServerUsed(row) {
  po.value.dealMethodPo.serverName = row.serverName
  getServerUsedApi(row.serverName)
      .then(res => {
        list.value.serverList = res.data.data
        list.value.searchServerList = list.value.serverList
      })
}
function getServerList(row) {
  po.value.dealMethodPo.serverName = row.serverName
  getServerListApi(row.serverName)
      .then(res => {
        row.status = res.data.data.length !== 0;
        list.value.serverList = res.data.data
        list.value.searchServerList = list.value.serverList
      })
}
function getServerBalance(row) {
  po.value.dealMethodPo.serverName = row.serverName
  getServerBalanceApi(row.serverName)
      .then(res => {
        row.status = res.data.data.length !== 0;
        list.value.serverList = res.data.data
        list.value.searchServerList = list.value.serverList
      })
}
function editDialog(row) {
  dialog.value.editServer = true
  po.value.serverDetail = row
}
function edit() {
  setServerDetailApi(po.value.serverDetail)
      .then(res => {
        ElMessage.success("修改成功！")
      })
}
function showServerDetail(row) {
  po.value.dealMethodPo.name = row.name
  po.value.serverPo = row
  list.value.searchClassList = row.classList
}
function showMethod(method, row) {
  po.value.dealMethodPo.className = row.name
  po.value.dealMethodPo.methodName = method.name
  po.value.dealMethodPo.version = row.version
  if (method.parameterList.length !== 0) {
    let args = method.parameterList[0].parameterList
    if (args !== null) {
      po.value.dealMethodPo.data = method.parameterList[0].parameterJson
      po.value.dealMethodPo.jsonData = eval('(' + method.parameterList[0].parameterJson + ')')
    } else {
      po.value.dealMethodPo.data = ""
    }
  } else {
    po.value.dealMethodPo.data = ""
  }
}
function dealMethod() {
  po.value.dealMethodPo.token = localStorage.getItem('token')
  dealMethodApi(po.value.dealMethodPo)
      .then(res => {
        po.value.dealMethodPo.result = res.data
        po.value.dealMethodPo.result.dataJson = JSON.stringify(res.data.data, null, 2).replace(/\\n/g, '\n')
      })
}
function showMethodDetail(row) {
  po.value.dealMethodPo.className = row.name
  po.value.dealMethodPo.version = row.version
  po.value.classList = row
  list.value.searchMethodList = po.value.classList.methodList
}
function getTopServerList() {
  getTopServerListApi()
      .then(res => {
        list.value.serverTopList = res.data.data
        list.value.searchServerTopList = list.value.serverTopList
      }).catch(e => {
        router.push("/auth")
      });
}

const searchServerTopListFilter = computed(() =>
    list.value.serverTopList.filter(
        (data) =>
            !searchServerTopList.value ||
            (data.serverName && data.serverName.toLowerCase().includes(searchServerTopList.value.toLowerCase())) ||
            (data.name && data.name.toLowerCase().includes(searchServerTopList.value.toLowerCase())) ||
            (data.desc && data.desc.toLowerCase().includes(searchServerTopList.value.toLowerCase()))
    )
)

const searchServerListFilter = computed(() =>
    list.value.searchServerList.filter(
        (data) =>
            !searchServerList.value ||
            (data.name && data.name.toLowerCase().includes(searchServerList.value.toLowerCase())) ||
            (data.ip && data.ip.includes(searchServerList.value)) ||
            (data.port && data.port.toString().includes(searchServerList.value)) ||
            (data.weight && data.weight.toString().includes(searchServerList.value)) ||
            (data.area && data.area.toLowerCase().includes(searchServerList.value.toLowerCase()))
    )
)

const searchClassListFilter = computed(() =>
    list.value.searchClassList.filter(
        (data) =>
            !searchClassList.value ||
            (data.name && data.name.toLowerCase().includes(searchClassList.value.toLowerCase())) ||
            (data.path && data.path.toLowerCase().includes(searchClassList.value.toLowerCase())) ||
            (data.version && data.version.toLowerCase().includes(searchClassList.value.toLowerCase()))
    )
)

const searchMethodListFilter = computed(() =>
    list.value.searchMethodList.filter(
        (data) =>
            !searchMethodList.value ||
            (data.name && data.name.toLowerCase().includes(searchMethodList.value.toLowerCase())) ||
            (data.parameterList.length > 0 && data.parameterList[0].name.toLowerCase().includes(searchMethodList.value.toLowerCase()))
    )
)

onMounted(() => {
  getTopServerList()
})

</script>

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