import { defineStore } from "pinia"
import { ref } from "vue"
import { useTokenStore } from "./token"

export const useQnStore = defineStore('qn', () => {
    const qn = ref<qn>()
    const qs = ref<qn[]>()

    const req_qs = (bank_id: number) => {
        const { get_token } = useTokenStore()
        const token = get_token()
        uni.request({
            url: "/api/qn/bank/" + bank_id + "/qs",
            header: { 'Authorization': token }
        }).then((rep: any) => {
            if (rep.statusCode == 200) {
                const qn_rep: qn_rep[] = rep.data
                qs.value = qn_rep.map(e => ({
                    ...e,
                    options: e.options ? JSON.parse(e.options) : {}
                }))
            } else {

                uni.showToast({ title: rep.data.toString() })
            }
        }).catch(err => uni.showToast({ title: err }))
    }


    const req_qn = () => {

    }

    const req_daily_qn = () => {
        const { get_token } = useTokenStore()
        const token = get_token()
        uni.request({
            url: '/api/ans/daily',
            header: { 'Authorization': token }
        }).then((res: any) => {
            if (res.statusCode == 200) {
                const qn_rep: qn_rep = res.data
                qn.value = {
                    ...qn_rep,
                    options: qn_rep.options ? JSON.parse(qn_rep.options) : {}
                }
            } else {
                uni.showToast({ title: res.data.toString() })
            }
        }).catch(err => uni.showToast({ title: err }))
    }


    const add_qn = (bank_id: number, q: qn) => {
        const { get_token } = useTokenStore()
        const token = get_token()

        uni.request({
            url: '/api/qn',
            data: {
                ...q,
                options: q.options ? JSON.stringify(q.options) : null,
                bank_id: bank_id,
                create_time: null
            },
            method: "POST",
            header: { 'Authorization': token }
        }).then((res: any) => {
            uni.showToast({ title: res.data.toString() })
        }).catch(err => uni.showToast({ title: err }))
    }

    const delete_qn = (id: number) => {
        const { get_token } = useTokenStore()
        const token = get_token()

        uni.request({
            url: '/api/qn/' + id,
            method: "DELETE",
            header: { 'Authorization': token }
        }).then((res: any) => {
            uni.showToast({ title: res.data.toString() })
        }).catch(err => uni.showToast({ title: err }))
    }

    return { qn, qs, req_qs, req_daily_qn, add_qn, delete_qn }
})

export enum qn_type {
    FILL_BLANK = 'FILL_BLANK',
    SINGLE_CHOICE = 'SINGLE_CHOICE',
    MULTIPLE_CHOICE = 'MULTIPLE_CHOICE',
    TRUE_FALSE = 'TRUE_FALSE'
}

export enum qn_diff {
    EASY = 'EASY',
    MEDIUM = 'MEDIUM',
    HARD = 'HARD'
}

export interface qn {
    id: number
    sub: string
    type: qn_type
    content: string
    options: opt_type
    answer: string
    ana: string
    diff: qn_diff
    creator: string
    create_time: Date
}

interface opt_type {
    A: string
    B: string
    C: string
    D: string
}

interface qn_rep {
    id: number
    sub: string
    type: qn_type
    content: string
    options: string
    answer: string
    ana: string
    diff: qn_diff
    creator: string
    create_time: Date
}