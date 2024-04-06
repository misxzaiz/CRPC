import {deal} from "../../common/crpc";

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
