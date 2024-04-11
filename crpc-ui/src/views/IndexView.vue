<template>
  <div>
    <div>
      <el-menu
          class="el-menu-demo"
          mode="horizontal"
          :ellipsis="false"
      >
        <el-menu-item index="0">
          <h1 @click="login">CRPC</h1>
        </el-menu-item>
        <div class="flex-grow" />
        <el-sub-menu index="1">
          <template #title>工作台</template>
          <el-menu-item index="1-1" @click="login">登录</el-menu-item>
          <el-menu-item index="1-2" @click="logout">注销</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </div>
    <div>
      <el-row>
        <el-col :span="2">
          <el-menu
              class="el-menu-vertical-demo"
              @select="handleMenuSelect"
              default-active="index"
          >
            <el-menu-item index="home">
              <span style="font-size: large;font-weight: bolder">首页</span>
            </el-menu-item>
            <el-menu-item index="server">
              <span style="font-size: large;font-weight: bolder">服务</span>
            </el-menu-item>
            <el-menu-item index="requestLog">
              <span style="font-size: large;font-weight: bolder">日志</span>
            </el-menu-item>
          </el-menu>
        </el-col>
        <el-col :span="20">
          <div v-if="activeTab === 'home'">
            <home/>
          </div>
          <div v-if="activeTab === 'server'">
            <server/>
          </div>
          <div v-if="activeTab === 'requestLog'">
            <requestLog/>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import home from "./HomeView.vue"
import server from "./server/IndexView.vue"
import requestLog from "./server/RequestLog.vue"

const activeTab = ref("home")

function handleMenuSelect(index) {
  activeTab.value = index;
}

function login() {
  router.push("/auth");
}

function logout() {
  localStorage.removeItem("token");
  ElMessage.success("注销成功");
}
</script>

<style scoped>
.flex-grow {
  flex-grow: 1;
}
</style>
