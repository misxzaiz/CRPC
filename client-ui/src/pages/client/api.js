import {request} from "@dcloudio/uni-h5";
export const MAIN_URI = "http://127.0.0.1:8000/server/dealMethod"
// export const MAIN_URI = "https://dominant-ant-formerly.ngrok-free.app"

export function getTopServerListApi() {
    return deal("CrpcServer/CenterService/getTopServerList/V1", null)
}

export function getServerListApi(serverName) {
    return deal("CrpcServer/CenterService/getServerList/V1", serverName)
}

export function getServerBalanceApi(serverName) {
    return deal("CrpcServer/CenterService/getServerList/V1", serverName)
}

export function setServerDetailApi(serverDetail) {
    return deal("CrpcServer/CenterService/setServerDetail/V1", serverDetail)
}

export const dealMethodApi = (dealMethodPo) => {
    return request({
        url: `${MAIN_URI}`,
        method: 'POST',
        data: dealMethodPo
    });
};

export const deal = (uri, data) => {
    return dealWithToken(uri, data, '')
}

export const dealWithToken = (uri, data, token) => {
    const requestVo = getRequestVo(uri, data, token);
    return uni.request({
        url: `${MAIN_URI}`,
        method: 'POST',
        data: requestVo
    })
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