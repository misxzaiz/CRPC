<template>
  <div style="background-color: #545c64;height: 1000px">
    <el-menu
        class="el-menu-demo"
        mode="horizontal"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
    >
      <el-menu-item index="1">
        <h1><navigator url="/pages/auth/login" >CRPC</navigator></h1>
      </el-menu-item>
    </el-menu>
    <el-row>
      <el-col :span="2">
        <el-menu
            active-text-color="#ffd04b"
            background-color="#545c64"
            text-color="#fff"
            class="el-menu-vertical-demo"
            @select="handleMenuSelect"
            default-active="index"
        >
          <el-menu-item index="index">
            <span style="font-size: large;font-weight: bolder">首页</span>
          </el-menu-item>
          <el-menu-item index="serverInfo">
            <span style="font-size: large;font-weight: bolder">服务注册</span>
          </el-menu-item>
          <el-menu-item index="serverInterface">
            <span style="font-size: large;font-weight: bolder">服务调用</span>
          </el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="20">
        <div v-if="activeTab === 'index'">
          <h1 style="color: white;margin: 2%;text-align: center;">首页</h1>
        </div>
        <div v-if="activeTab === 'serverInfo'">
          <ServerInfo></ServerInfo>
        </div>
        <div v-if="activeTab === 'serverInterface'">
          <ServerInterface></ServerInterface>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import ServerInfo from "../client/index.vue";
import ServerInterface from "../client/interface.vue";

export default {
  components: {
    ServerInfo,
    ServerInterface
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
    }
  },
}
</script>

<style scoped>
.main-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
}

.main-btn {
  width: 200px;
  height: 50px;
  margin-bottom: 20px;
  font-size: 18px;
  background-color: #00a0e9;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.main-btn:hover {
  background-color: #0077b6;
}
</style>
