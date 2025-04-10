import { defineStore } from "pinia"
import { ref } from "vue"
import type { opt_type, qn, qn_diff, qn_rep, qn_type } from "./qn"
import { useTokenStore } from "./token"

export const useAnsStore = defineStore('ans', () => {
    const qn = ref<qn>()
    const cor_ans = ref<string>()
    const token = ref<string>()
    const records = ref<ans[]>()

    const req_qn = (bank_id: number) => {
        uni.request({
            url: "/api/ans/" + bank_id,
            header: { 'Authorization': useTokenStore().get_token() }
        }).then((res: any) => {
            if (res.statusCode == 200) {
                const qn_rep: qn_rep = res.data
                qn.value = {
                    ...qn_rep,
                    options: qn_rep.options ? JSON.parse(qn_rep.options) : {}
                }
            }
        }).catch(err => {
            uni.showToast({ title: err })
        })
    }


    const start_ans = (start_req: start_req) => {
        cor_ans.value = ''
        uni.request({
            url: "/api/ans/start",
            data: {
                ...start_req,
                exam_id: null
            },
            method: "POST",
            header: { 'Authorization': useTokenStore().get_token() }
        }).then((res: any) => {
            if (res.statusCode == 200) {
                token.value = res.data
            } else {
                uni.showToast({ title: res.data.toString() })
            }
        }).catch(err => {
            uni.showToast({ title: err })
        })
    }

    const end_ans = (usr_ans: string) => {
        uni.request({
            url: "/api/ans/end",
            data: {
                usr_ans: usr_ans,
                token: token.value
            },
            method: "POST",
            header: { 'Authorization': useTokenStore().get_token() }
        }).then((res: any) => {
            if (res.statusCode == 200) {
                console.log("res.data: ", res.data)
                cor_ans.value = res.data
                token.value = ''
            } else {
                uni.showToast({ title: res.data.toString() })
            }
        }).catch(err => {
            uni.showToast({ title: err })
        })
    }


    const req_rds = () => {
        uni.request({
            url: "/api/ans/record",
            header: { 'Authorization': useTokenStore().get_token() }
        }).then((res: any) => {
            if (res.statusCode == 200) {
                const rep: ans_rep[] = res.data
                records.value = rep.map(e => ({
                    ...e,
                    options: e.options ? JSON.parse(e.options) : {}
                }))
            } else {
                uni.showToast({ title: res.data.toString() })
            }
        }).catch(err => {
            uni.showToast({ title: err })
        })
    }

    return { qn, cor_ans, records, req_qn, start_ans, end_ans, req_rds }
})


export interface start_req {
    qn_id: number | null
    paper_id: number | null
}

export interface end_req {
    token: string
    usr_ans: string
}

interface ans_rep {
    sub: string
    type: qn_type
    content: string
    options: string
    ans: string
    ana: string
    diff: qn_diff
    usr_ans: string
    start_time: string
    end_time: string
}

export interface ans {
    sub: string
    type: qn_type
    content: string
    options: opt_type
    ans: string
    ana: string
    diff: qn_diff
    usr_ans: string
    start_time: string
    end_time: string
}