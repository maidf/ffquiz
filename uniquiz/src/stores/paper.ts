import { defineStore } from "pinia"
import { ref } from "vue"
import { useTokenStore } from "./token"
import type { opt_type, qn, qn_diff, qn_type } from "./qn"

export const usePaperStore = defineStore('paper', () => {
    const papers = ref<paper[]>()
    const qs = ref<paper_qn[]>()

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
                const qn_rep: paper_qn_rep[] = res.data
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

    const update_paper = (p: paper) => {
        const token = useTokenStore().get_token()
        uni.request({
            url: "/api/paper/" + p.id,
            data: {
                ...p,
                create_time: null,
                total_score: null
            },
            method: "PUT",
            header: { 'Authorization': token }
        }).then((res: any) => {
            uni.showToast({ title: res.data.toString() })
        }).catch(err => {
            uni.showToast({ title: err })
        })
    }

    const rm_paper = (paper_id: number) => {
        const token = useTokenStore().get_token()
        uni.request({
            url: "/api/paper/" + paper_id,
            method: "DELETE",
            header: { 'Authorization': token }
        }).then((res: any) => {
            uni.showToast({ title: res.data.toString() })
        }).catch(err => {
            uni.showToast({ title: err })
        })
    }

    const add_qn = (paper_id: number, qn_id: number, score: number) => {
        uni.request({
            url: "/api/paper/" + paper_id + "/qs",
            data: [{
                qn_id: qn_id,
                score: score
            }],
            method: "POST",
            header: { 'Authorization': useTokenStore().get_token() }
        }).then((res: any) => {
            uni.showToast({ title: res.data.toString() })
        }).catch(err => {
            uni.showToast({ title: err })
        })
    }

    const rm_qn = (paper_id: number, qn_id: number) => {
        uni.request({
            url: "/api/paper/" + paper_id + "/qn/" + qn_id,
            method: "DELETE",
            header: { 'Authorization': useTokenStore().get_token() }
        }).then((res: any) => {
            uni.showToast({ title: res.data.toString() })
        }).catch(err => {
            uni.showToast({ title: err })
        })
    }

    return { papers, qs, req_papers, add_paper, req_qs, add_qn, rm_qn, update_paper, rm_paper }
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

export interface paper_qn extends qn {
    score: number
}

interface paper_qn_rep {
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
    score: number
}