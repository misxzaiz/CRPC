import axios from 'axios';
import router from "@/router/index.js";
export const getMainUri = () => {
    let savedServer = JSON.parse(localStorage.getItem('server'))
    return `${savedServer.protocol}://${savedServer.ip}:${savedServer.port}/server/dealMethod`
}
export const deal = (uri, data) => {
    return dealWithToken(uri, data, localStorage.getItem('token'))
}
export const dealMethodApi = (dealMethodPo) => {
    return axios.post(getMainUri(), dealMethodPo).then(response => {
        // 在这里处理响应结果
        if (response.data !== null && response.data.code === 401) {
            // 执行跳转到登录页面的操作
            router.push('/auth').then(r => {});
            // 返回一个 reject 状态的 Promise，表示请求被中止
            return Promise.reject('Unauthorized');
        } else {
            return response;
        }
    });
};
export const dealWithToken = (uri, data, token) => {
    const requestVo = getRequestVo(uri, data, token);
    return axios.post(getMainUri(), requestVo).then(response => {
        // 在这里处理响应结果
        if (response.data !== null && response.data.code === 401) {
            // 执行跳转到登录页面的操作
            router.push('/auth').then(r => {});
            // 返回一个 reject 状态的 Promise，表示请求被中止
            return Promise.reject('Unauthorized');
        } else {
            return response;
        }
    });
}

export function getRequestVo(uri, data, token) {
    const uris = uri.split("/");
    return {
        serverName: uris[0],
        name: uris[0],
        className: uris[1],
        data: data,
        methodName: uris[2],
        version: uris[3],
        token: token
    }
}