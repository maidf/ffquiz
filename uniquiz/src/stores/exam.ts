import { defineStore } from "pinia"
import { useTokenStore } from "./token"
import type { paper_qn, paper_qn_rep } from "./paper"
import { ref } from "vue"
import type { start_req } from "./ans"
import type { opt_type, qn_diff, qn_type } from "./qn"

export const useExamStore = defineStore('exam', () => {
    const qs = ref<exam_qn[]>()
    const token = ref<string>()
    const records = ref<record[]>()
    const rd_qs = ref<rd_qn[]>()


    const req_qs = (paper_id: number) => {
        uni.request({
            url: "/api/paper/" + paper_id + "/qs",
            header: { 'Authorization': useTokenStore().get_token() }
        }).then((res: any) => {
            if (res.statusCode == 200) {
                const qn_rep: paper_qn_rep[] = res.data
                qs.value = qn_rep.map(e => ({
                    ...e,
                    options: e.options ? JSON.parse(e.options) : {},
                    usr_ans: ''
                }))
            } else {
                uni.showToast({ title: res.data.toString() })
            }
        }).catch(err => {
            uni.showToast({ title: err })
        })
    }

    const req_rds = () => {
        uni.request({
            url: "/api/exam/record",
            header: { 'Authorization': useTokenStore().get_token() }
        }).then((res) => {
            if (res.statusCode === 200) {
                records.value = res.data as record[]
            } else {
                uni.showToast({ title: res.data.toString() })
            }
        }).catch(err => {
            uni.showToast({ title: err })
        })
    }

    const req_rd_qs = (exam_id: number) => {
        uni.request({
            url: "/api/exam/record/" + exam_id,
            header: { 'Authorization': useTokenStore().get_token() }
        }).then((res) => {
            if (res.statusCode === 200) {
                const data = res.data as rd_qn_rep[]
                rd_qs.value = data.map(e => ({
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


    const start_exam = (start_req: start_req) => {
        token.value = ''
        uni.request({
            url: "/api/exam/start",
            data: start_req,
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

    const end_exam = (req: end_req[]) => {
        uni.request({
            url: "/api/exam/end",
            data: req,
            method: "POST",
            header: {
                'Authorization': useTokenStore().get_token(),
                'exam': token.value
            }
        }).then((res: any) => {
            if (res.statusCode == 200) {
                console.log("res.data: ", res.data)
                token.value = ''
            } else {
                uni.showToast({ title: res.data.toString() })
            }
        }).catch(err => {
            uni.showToast({ title: err })
        })
    }


    const delete_exam = async (exam_id: number) => {
        await uni.request({
            url: "/api/exam/record/" + exam_id,
            method: "DELETE",
            header: { 'Authorization': useTokenStore().get_token() }
        }).then((res: any) => {
            if (res.statusCode == 200) {
                records.value = records.value?.filter(e => e.id !== exam_id)
            }
            uni.showToast({ title: res.data.toString() })
        }).catch(err => {
            uni.showToast({ title: err })
        })
    }

    return { qs, records, rd_qs, req_qs, start_exam, end_exam, req_rds, delete_exam, req_rd_qs }
})


export interface end_req {
    qn_id: number
    usr_ans: string
}

export interface exam_qn extends paper_qn {
    usr_ans: ''
}

export interface record {
    id: number
    name: string
    time_limit: number
    diff: qn_diff
    score: number
    total_score: number
    start_time: Date
    end_time: Date
}

export interface rd_qn {
    id: number
    sub: string
    type: qn_type
    content: string
    options: opt_type
    answer: string
    ana: string
    diff: qn_diff
    usr_ans: string
}

interface rd_qn_rep {
    id: number
    sub: string
    type: qn_type
    content: string
    options: string
    answer: string
    ana: string
    diff: qn_diff
    usr_ans: string
}