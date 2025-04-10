import { defineStore } from "pinia"
import { useTokenStore } from "./token"
import type { paper_qn, paper_qn_rep } from "./paper"
import { ref } from "vue"
import type { start_req } from "./ans"

export const useExamStore = defineStore('exam', () => {
    const qs = ref<exam_qn[]>()
    const token = ref<string>()

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

    return { qs, req_qs, start_exam, end_exam }
})


export interface end_req {
    qn_id: number
    usr_ans: string
}

export interface exam_qn extends paper_qn {
    usr_ans: ''
}
