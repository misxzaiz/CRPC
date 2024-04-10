import {deal} from "../../common/crpc";


export function loginApi(userInfo) {
    return deal("CrpcServer/AuthService/login/V1", userInfo)
}