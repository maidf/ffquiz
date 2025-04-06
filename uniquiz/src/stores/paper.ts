import { defineStore } from "pinia"
import { ref } from "vue"
import { useTokenStore } from "./token"
import type { qn, qn_diff, qn_rep } from "./qn"

export const usePaperStore = defineStore('paper', () => {
    const papers = ref<paper[]>()
    const qs = ref<qn[]>()

    const req_papers = () => {
        const token = useTokenStore().get_token()
        uni.request({
            url: "/api/paper",
            header: { 'Authorization': token }
        }).then((res: any) => {
            if (res.statusCode == 200) {
                papers.value = res.data
            } else {
                uni.showToast({ title: res.data.toString() })
            }
        }).catch(err => {
            uni.showToast({ title: err })
        })
    }

    const req_qs = (paper_id: number) => {
        const token = useTokenStore().get_token()
        uni.request({
            url: "/api/paper/" + paper_id + "/qs",
            header: { 'Authorization': token }
        }).then((res: any) => {
            if (res.statusCode == 200) {
                const qn_rep: qn_rep[] = res.data
                qs.value = qn_rep.map(e => ({
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

    const add_paper = (paper: paper) => {
        const token = useTokenStore().get_token()
        uni.request({
            url: "/api/paper",
            data: {
                ...paper,
                create_time: null,
                total_score: null
            },
            method: "POST",
            header: { 'Authorization': token }
        }).then((res: any) => {
            uni.showToast({ title: res.data.toString() })
        }).catch(err => {
            uni.showToast({ title: err })
        })
    }

    return { papers, qs, req_papers, add_paper, req_qs }
})


export interface paper {
    id: number
    name: string
    diff: qn_diff
    time_limit: number
    total_score: number
    creator: string
    create_time: Date
}