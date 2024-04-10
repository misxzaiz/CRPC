<template>
  <div style="height: 900px">
    <el-menu
        class="el-menu-demo"
        mode="horizontal"
        :ellipsis="false"
    >
      <el-menu-item index="0">
        <h1><navigator url="/pages/auth/login" >CRPC</navigator></h1>
      </el-menu-item>
      <div class="flex-grow" />
      <el-sub-menu index="1">
        <template #title>工作台</template>
        <el-menu-item index="1-1" @click="login">登录</el-menu-item>
        <el-menu-item index="1-2" @click="logout">注销</el-menu-item>
      </el-sub-menu>
    </el-menu>
    <el-row>
      <el-col :span="2">
        <el-menu
            class="el-menu-vertical-demo"
            @select="handleMenuSelect"
            default-active="index"
        >
          <el-menu-item index="index">
            <span style="font-size: large;font-weight: bolder">首页</span>
          </el-menu-item>
<!--          <el-menu-item index="serverInfo">-->
<!--            <span style="font-size: large;font-weight: bolder">服务注册</span>-->
<!--          </el-menu-item>-->
<!--          <el-menu-item index="serverInterface">-->
<!--            <span style="font-size: large;font-weight: bolder">服务调用</span>-->
<!--          </el-menu-item>-->
<!--          <el-menu-item index="server">-->
<!--            <span style="font-size: large;font-weight: bolder">服务</span>-->
<!--          </el-menu-item>-->
          <el-menu-item index="serverTwo">
            <span style="font-size: large;font-weight: bolder">服务</span>
          </el-menu-item>
          <el-menu-item index="requestLog">
            <span style="font-size: large;font-weight: bolder">日志</span>
          </el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="20">
        <div v-if="activeTab === 'index'">
          <Main></Main>
        </div>
        <div v-if="activeTab === 'serverInfo'">
          <ServerInfo></ServerInfo>
        </div>
        <div v-if="activeTab === 'serverInterface'">
          <ServerInterface></ServerInterface>
        </div>
        <div v-if="activeTab === 'server'">
          <Server></Server>
        </div>
        <div v-if="activeTab === 'serverTwo'">
          <ServerTwo></ServerTwo>
        </div>
        <div v-if="activeTab === 'requestLog'">
          <RequestLog></RequestLog>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Server from "../client/server.vue";
import ServerTwo from "../client/serverTwo.vue";
import ServerInfo from "../client/index.vue";
import ServerInterface from "../client/interface.vue";
import RequestLog from "../client/requestLog.vue";
import Main from "../main/main.vue";

export default {
  components: {
    Main,
    Server,
    ServerTwo,
    ServerInfo,
    ServerInterface,
    RequestLog
  },
  data() {
    return {
      activeTab: "index"
    }
  },
  onLoad() {},
  methods: {
    handleMenuSelect(index) {
      // 切换选项卡时触发的方法
      this.activeTab = index;
    },
    login() {
      uni.navigateTo({
        url: '/pages/auth/login',
      }).then(r => {});
    },
    logout() {
      uni.removeStorageSync("token")
      this.login()
    }
  },
}
</script>

<style scoped>
.flex-grow {
  flex-grow: 1;
}
</style>
