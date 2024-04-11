import {ref} from "vue";

export const searchServerTopList = ref("")
export const searchServerList = ref("")
export const searchClassList = ref("")
export const searchMethodList = ref("")
export const server = {
    serverName: "",
    name: "",
    desc: "",
    status: true,
}
export const list = ref({
    searchServerTopList: [],
    serverTopList: [],
    // serverTopList: [],
    serverList: [],
    searchServerList: [],
    searchClassList: [],
    searchMethodList: [],
})
export const po = ref({
    serverDetail: {
        serverName: null,
        name: null,
        desc: null
    },
    serverPo: {
        name: null,
        ip: null,
        port: null,
        weight: null,
        area: null,
        classList: []
    },
    classList: {
        name: null,
        path: null,
        version: null,
        methodList: []
    },
    methodDetail: {
        name: null,
        parameterList: []
    },
    parameterList: {
        name: null,
        parameterList: [],
        parameterJson: null
    },
    dealMethodPo: {
        id: null,
        token: null,
        serverName: null,
        className: null,
        version: null,
        methodName: null,
        data: null,
        result: null,
        jsonData: {}
    }
})
export const dialog = ref({
    editServer: false
})