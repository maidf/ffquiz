import { defineStore } from "pinia"
import { ref } from "vue"
import type { opt_type, qn_diff, qn_type } from "./qn"
import { useTokenStore } from "./token"

export const useMisStore = defineStore('mis', () => {
    const mistakes = ref<mis[]>()

    const req_mis = () => {
        uni.request({
            url: "/api/mistake",
            header: { 'Authorization': useTokenStore().get_token() }
        }).then((res) => {
            if (res.statusCode === 200) {
                const data = res.data as mis_rep[]
                mistakes.value = data.map(e => ({
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

    return { mistakes, req_mis }
})

interface mis {
    id: number
    sub: string
    diff: qn_diff
    type: qn_type
    content: string
    options: opt_type
    answer: string
    usr_ans: string
    ana: string
}

interface mis_rep {
    id: number
    sub: string
    diff: qn_diff
    type: qn_type
    content: string
    options: string
    answer: string
    usr_ans: string
    ana: string
}