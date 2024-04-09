<template>
  <div class="sci-fi-login">
    <h1>欢迎您的到来！</h1>
    <h1>Welcome to your arrival!</h1>
    <div style="margin-top: 5%;">
      <el-form :model="userInfo" label-width="auto" style="max-width: 600px">
        <el-form-item label="协议">
          <el-input v-model="server.protocol" placeholder="请输入您的协议！" />
        </el-form-item>
        <el-form-item label="IP">
          <el-input v-model="server.ip" placeholder="请输入您的IP！" />
        </el-form-item>
        <el-form-item label="端口">
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
          <el-checkbox v-model="rememberServer">记住服务</el-checkbox>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {loginApi} from "./api";
import {ElMessage} from "element-plus";

export default {
  data() {
    return {
      userInfo: {
        account: "",
        password: ""
      },
      server: {
        ip: "",
        port: ""
      },
      rememberPassword: true,
      rememberServer: true
    }
  },
  created() {
    // 页面初始化时，尝试从本地存储中获取已保存的账号密码
    const savedUserInfo = uni.getStorageSync('userInfo');
    if (savedUserInfo) {
      this.userInfo = savedUserInfo;
    }
    const savedServer = uni.getStorageSync('server');
    if (savedServer) {
      this.server = savedServer;
    }
  },
  methods: {
    login() {
      if (this.rememberServer) {
        uni.setStorageSync('server', this.server);
      } else {
        uni.removeStorageSync('server');
      }
      if (this.rememberPassword) {
        uni.setStorageSync('userInfo', this.userInfo);
      } else {
        // 否则清除本地存储中的账号密码
        uni.removeStorageSync('userInfo');
      }
      loginApi(this.userInfo).then(res => {
        if (res.data.data.code === 200) {
          uni.setStorageSync('token', res.data.data.data);
          ElMessage.success(res.data.data.message);
          // 如果勾选了记住密码，则保存账号密码到本地存储
          uni.navigateTo({
            url: '/pages/index/index',
          }).then(r => {});
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
  },
}
</script>

<style scoped>
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
