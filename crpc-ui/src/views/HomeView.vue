<template>
  <div style="margin: 2%">
    <el-container>
      <el-aside width="200px" style="font-size: larger">
        <div @click="scrollTo('overview')">一、概述</div>
        <div @click="scrollTo('basicArch')">1.1 基本架构</div>
        <div @click="scrollTo('composition')">1.1.1 组成</div>
        <div @click="scrollTo('process')">1.1.2 流程</div>
        <br>
        <div @click="scrollTo('projectStructure')">1.2 项目结构</div>
        <div @click="scrollTo('composition1')">1.2.1 组成</div>
        <br>
        <div @click="scrollTo('getStart')">三、入门</div>
      </el-aside>
      <el-main>
        <h1 id="overview">一、概述</h1>
        <br>
        <p>本组件是一个基于RPC（远程过程调用）的通信框架的展示。它涵盖了注册中心、服务提供者和服务消费者等核心组件，并通过负载均衡策略实现高效的服务调用。</p>
        <el-divider />
        <h2 id="basicArch">1.1 基本架构</h2>
        <br>
        <p>基本架构图展示了RPC框架的主要组件和它们之间的交互关系。</p>
        <br>
        <div style="margin-left: 5%">
          <img src="../assets/main/CRPC.drawio.svg" alt="基本架构图">
        </div>
        <el-divider />
        <h3 id="composition">1.1.1 组成</h3>
        <br>
        <p>RPC框架主要由以下几个组件组成：</p>
        <br>
        <ul>
          <li>
            <strong>注册中心（Zookeeper）</strong>
            <p>负责服务的注册，维护服务提供者的信息列表。</p>
          </li>
          <li>
            <strong>中心服务（Center-Server）</strong>
            <p>负责服务的发现，提供负载均衡功能。</p>
          </li>
          <li>
            <strong>服务提供者（Provider-Server）</strong>
            <p>提供服务接口的实现，并将服务信息注册到注册中心。</p>
          </li>
          <li>
            <strong>服务消费者（Consumer-Server）</strong>
            <p>从中心服务获取服务信息，并调用服务提供者提供的接口。</p>
          </li>
        </ul>
        <el-divider />
        <h3 id="process">1.1.2 流程</h3>
        <br>
        <p>RPC框架的工作流程如下：</p>
        <br>
        <ol>
          <li>服务提供者启动后，将服务信息注册到注册中心。</li>
          <li>服务消费者启动时或需要调用服务时，从中心服务获取服务提供者的服务信息。</li>
          <li>中心服务向注册中心请求服务信息。</li>
          <li>注册中心向中心服务响应服务信息。</li>
          <li>中心服务对服务信息进行预处理。</li>
          <li>中心服务根据负载均衡策略（如随机、轮询、权重等），选择一个服务提供者，并返回其ip和端口给服务消费者。</li>
          <li>服务消费者根据返回的服务提供者信息，发起远程调用请求。</li>
          <li>服务提供者接收到请求后，执行相应的业务逻辑，并返回结果给服务消费者。</li>
        </ol>
        <br>
        <span>注：第3、4、5步只有在第一次获取服务或服务更新是才会执行</span>
        <el-divider />
        <h2 id="projectStructure">1.2 项目结构</h2>
        <br>
        <div style="background-color: black;color: white;padding: 2% 0 2% 0 ">
          <ul>
            <li>
              <b>cprc</b>
              <ul>
                <li>crpc-ui：RPC框架客户端，提供服务治理、服务测试等功能。</li>
                <li>crpc-server：中心服务，提供服务均衡、及client-ui客户端所需的接口。</li>
                <li>crpc-common：通用工具库，包含负载均衡、类解析、网络连接、Zookeeper连接等实现。</li>
                <li>crpc-demo：服务测试</li>
              </ul>
            </li>
          </ul>
        </div>
        <el-divider />
        <h3 id="composition1">1.2.1 组成</h3>
        <br>

        <el-divider />
        <h1 id="getStart">二、快速入门</h1>

      </el-main>
    </el-container>
    <el-backtop :right="100" :bottom="100" />
  </div>
</template>

<script setup>
function scrollTo(id) {
  const element = this.$el.querySelector(`#${id}`);
  if (element) {
    element.scrollIntoView({ behavior: 'smooth' });
  }
}
</script>