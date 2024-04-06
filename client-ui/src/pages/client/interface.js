import {deal} from "../../common/crpc";

export function getTopServerListApi() {
    return deal("CrpcServer/InterfaceService/getTopServerList/V1", null)
}

export function getServerUsedApi(serverName) {
    return deal("CrpcServer/InterfaceService/getServerUsed/V1", serverName)
}

export function getServerListApi(serverName) {
    return deal("CrpcServer/InterfaceService/getServerList/V1", serverName)
}

export function setServerDetailApi(serverDetail) {
    return deal("CrpcServer/InterfaceService/setServerDetail/V1", serverDetail)
}

