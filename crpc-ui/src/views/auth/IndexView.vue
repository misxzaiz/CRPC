<template>
  <div class="sci-fi-login">
    <h1>欢迎您的到来！</h1>
    <h1>Welcome to your arrival!</h1>
    <div style="margin-top: 5%;">
      <el-form :model="userInfo" label-width="auto" style="max-width: 600px">
        <el-form-item label="协议" v-if="rememberServer">
          <el-input v-model="server.protocol" placeholder="请输入您的协议！" />
        </el-form-item>
        <el-form-item label="IP" v-if="rememberServer">
          <el-input v-model="server.ip" placeholder="请输入您的IP！" />
        </el-form-item>
        <el-form-item label="端口" v-if="rememberServer">
          <el-input v-model="server.port" placeholder="请输入您的端口！" />
        </el-form-item>
        <el-form-item label="账号">
          <el-input v-model="userInfo.account" placeholder="请输入您的账号！" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="userInfo.password" type="password" placeholder="请输入您的密码！" />
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="rememberPassword">记住密码</el-checkbox>
          <el-checkbox v-model="rememberServer">远程服务</el-checkbox>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import {ElMessage} from "element-plus";
import {loginApi} from "@/views/auth/api.js";
import router from "@/router/index.js";

const userInfo = ref({
  account: "",
  password: ""
})

const server = ref({
  protocol: "",
  ip: "",
  port: ""
})

const rememberPassword = ref(true)
const rememberServer = ref(true)

function  login() {
  if (rememberServer.value) {
    localStorage.setItem('server', JSON.stringify(server.value))
  } else {
    localStorage.removeItem('server')
  }
  if (rememberPassword.value) {
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
  } else {
    localStorage.removeItem('userInfo')
  }
  loginApi(userInfo.value).then(res => {
    if (res.data.data.code === 200) {
      localStorage.setItem('token', res.data.data.data);
      ElMessage.success(res.data.data.message);
      router.push('/'); // 跳转到指定的路由
    } else {
      ElMessage.error(res.data.data.message);
    }
  }).catch(e => {
    console.log(e.errMsg);
    if (e.errMsg === 'request:fail') {
      ElMessage.error("登录失败！请检查协议、ip、端口是否正确！");
    } else {
      ElMessage.error("登录失败！请检查协议、ip、端口、账号和密码是否正确！");
    }
  });
}

onMounted(() => {
  const savedUserInfo = localStorage.getItem("userInfo");
  if (savedUserInfo) {
    userInfo.value = JSON.parse(savedUserInfo);
  }
  const savedServer = localStorage.getItem("server");
  if (savedServer) {
    server.value = JSON.parse(savedServer);
  }
})

</script>

<style>
.sci-fi-login {
  text-align: center;
  margin-top: 50px;
}

.sci-fi-login h1 {
  font-size: 36px;
  color: #00a0e9;
}

.sci-fi-login {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 50px;
}

.sci-fi-login p {
  margin-top: 20px;
  font-size: 18px;
  color: #666;
}

.login-btn {
  width: 240px; /* 设置按钮宽度 */
  height: 40px; /* 设置按钮高度 */
}
</style>