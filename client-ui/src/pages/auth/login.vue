<template>
  <div class="sci-fi-login">
    <h1>欢迎您的到来！</h1>
    <h1>Welcome to your arrival!</h1>
    <div style="margin-top: 10%">
      <el-form :model="userInfo" label-width="auto" style="max-width: 600px">
        <el-form-item label="账号">
          <el-input v-model="userInfo.account" placeholder="请输入您的账号！" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="userInfo.password" type="password" placeholder="请输入您的密码！" />
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="rememberPassword">记住密码</el-checkbox>
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
      rememberPassword: true
    }
  },
  created() {
    // 页面初始化时，尝试从本地存储中获取已保存的账号密码
    const savedUserInfo = uni.getStorageSync('userInfo');
    if (savedUserInfo) {
      this.userInfo = savedUserInfo;
    }
  },
  methods: {
    login() {
      loginApi(this.userInfo).then(res => {
        if (res.data.data.code === 200) {
          uni.setStorageSync('token', res.data.data.data);
          ElMessage.success(res.data.data.message);
          // 如果勾选了记住密码，则保存账号密码到本地存储
          if (this.rememberPassword) {
            uni.setStorageSync('userInfo', this.userInfo);
          } else {
            // 否则清除本地存储中的账号密码
            uni.removeStorageSync('userInfo');
          }
          uni.navigateTo({
            url: '/pages/index/index',
          }).then(r => {});
        } else {
          ElMessage.error(res.data.data.message);
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
